package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.Mapper.PetCollectMapper;
import com.itheima.dto.CollectVO;
import com.itheima.entity.Pet;
import com.itheima.entity.PetCollect;
import com.itheima.service.PetCollectService;
import com.itheima.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PetCollectServiceImpl extends ServiceImpl<PetCollectMapper, PetCollect> implements PetCollectService {

    private final PetCollectMapper petCollectMapper;
    private final PetService petService;

    @Override
    @Transactional
    public void collect(Long userId, Long petId) {
        Pet pet = petService.getById(petId);
        if (pet == null) {
            throw new IllegalArgumentException("宠物不存在或已删除");
        }
        if(isCollected(userId,petId)){
            throw new IllegalArgumentException("已收藏该宠物");
        }
        PetCollect petCollect = new PetCollect();
        petCollect.setUserId(userId);
        petCollect.setPetId(petId);
        petCollect.setCollectTime(LocalDateTime.now());
        save(petCollect);
    }

    @Override
    @Transactional
    public void cancelCollect(Long userId, Long petId) {
        LambdaQueryWrapper<PetCollect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PetCollect::getUserId,userId).eq(PetCollect::getPetId,petId);
        if (this.count(wrapper) != 1) {
            throw new IllegalArgumentException("未收藏该宠物");
        }
        remove(wrapper);
    }

    @Override
    public List<CollectVO> selectCollectList(Long userId) {

        List<Map<String,Object>> maps = petCollectMapper.selectCollectMap(userId);
        List<CollectVO> result = new ArrayList<>();
        for(Map<String,Object> map:maps){
            CollectVO collectVO = new CollectVO();
            collectVO.setId((Long) map.get("id"));
            collectVO.setPetId((Long) map.get("pet_id"));
            collectVO.setPetName((String) map.get("pet_name"));
            collectVO.setCategory((String) map.get("category"));
            collectVO.setImgUrl((String) map.get("img_url"));
            collectVO.setAddress((String) map.get("address"));
            collectVO.setStatus((Integer) map.get("status"));
            collectVO.setCollectTime((LocalDateTime) map.get("collect_time"));
            result.add(collectVO);
        }
        return result;
    }

    @Override
    public boolean isCollected(Long userId, Long petId) {
        LambdaQueryWrapper<PetCollect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PetCollect::getUserId,userId).eq(PetCollect::getPetId,petId);
        return this.count(wrapper) > 0;
    }
}