package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.Mapper.AdoptApplyMapper;
import com.itheima.common.BusinessException;
import com.itheima.dto.AdoptApplyDTO;
import com.itheima.dto.AdoptApplyVO;
import com.itheima.dto.AdoptAuditDTO;
import com.itheima.entity.AdoptApply;
import com.itheima.entity.Pet;
import com.itheima.service.AdoptApplyService;
import com.itheima.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdoptApplyServiceImpl extends ServiceImpl<AdoptApplyMapper, AdoptApply> implements AdoptApplyService {

    private final AdoptApplyMapper adoptApplyMapper;
    private final PetService petService;

    private static String str(Map<String, Object> map, String key) {
        Object v = map.get(key);
        return v == null ? null : v.toString();
    }

    @Override
    @Transactional
    public void submitApply(Long userId, AdoptApplyDTO applyDTO) {
        Pet pet = petService.getById(applyDTO.getPetId());
        if (pet == null) {
            throw new IllegalArgumentException("宠物不存在或已删除");
        }
        if (pet.getStatus() != 0) {
            throw new BusinessException("宠物已被领养");
        }
        if (isApplied(userId, applyDTO.getPetId())) {
            throw new BusinessException("您已对该宠物提交申请,请不要重复提交");
        }
        AdoptApply adoptApply = new AdoptApply();
        adoptApply.setUserId(userId);
        adoptApply.setPetId(applyDTO.getPetId());
        adoptApply.setPhone(applyDTO.getPhone());
        adoptApply.setLiveEnv(applyDTO.getLiveEnv());
        adoptApply.setPetExp(applyDTO.getPetExp());
        adoptApply.setApplyStatus(0);
        adoptApply.setCreateTime(LocalDateTime.now());
        this.save(adoptApply);
    }

    @Override
    public List<AdoptApplyVO> myApplyList(Long userId) {
        List<Map<String, Object>> maps = adoptApplyMapper.selectMyApplyList(userId);
        return convertToList(maps, true);
    }

    @Override
    public List<AdoptApplyVO> adminApplyList(Integer status) {
        List<Map<String, Object>> maps = adoptApplyMapper.selectAdminApplyList(status);
        return convertToList(maps, false);
    }

    @Override
    @Transactional
    public void auditApply(Long applyId, AdoptAuditDTO auditDTO) {
        AdoptApply adoptApply = this.getById(applyId);
        if (adoptApply == null) {
            throw new BusinessException("申请记录不存在");
        }
        if (adoptApply.getApplyStatus() != 0) {
            throw new BusinessException("申请记录已被审核,请勿重复审核");
        }
        Pet pet = petService.getById(adoptApply.getPetId());
        if (pet == null) {
            throw new IllegalArgumentException("宠物不存在或已删除");
        }
        if (auditDTO.getApplyStatus() == 1) {
            if (pet.getStatus() != 0) {
                throw new BusinessException("宠物已被领养");
            }
            pet.setStatus(1);
            petService.updateById(pet);
            adoptApply.setApplyStatus(1);
        } else if (auditDTO.getApplyStatus() == 2) {
            adoptApply.setApplyStatus(2);
        } else {
            throw new BusinessException("审核状态参数错误");
        }
        if (StringUtils.hasText(auditDTO.getAuditNote())) {
            adoptApply.setAuditNote(auditDTO.getAuditNote());
        }
        this.updateById(adoptApply);
    }

    @Override
    public boolean isApplied(Long userId, Long petId) {
        LambdaQueryWrapper<AdoptApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdoptApply::getUserId, userId)
                .eq(AdoptApply::getPetId, petId)
                .in(AdoptApply::getApplyStatus, 0, 1);
        return this.count(wrapper) > 0;
    }

    private List<AdoptApplyVO> convertToList(List<Map<String, Object>> maps, boolean isMyApply) {
        List<AdoptApplyVO> result = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            AdoptApplyVO vo = new AdoptApplyVO();
            vo.setId(Long.parseLong(str(map, "id")));
            vo.setUserId(Long.parseLong(str(map, "user_id")));
            vo.setPetId(Long.parseLong(str(map, "pet_id")));
            vo.setPhone(str(map, "phone"));
            vo.setLiveEnv(str(map, "live_env"));
            vo.setPetExp(str(map, "pet_exp"));
            vo.setApplyStatus(Integer.parseInt(str(map, "apply_status")));
            vo.setApplyStatusText(getStatusText(vo.getApplyStatus()));
            vo.setAuditNote(str(map, "audit_note"));
            vo.setCreateTime((LocalDateTime) map.get("create_time"));
            vo.setPetName(str(map, "pet_name"));
            vo.setPetCategory(str(map, "pet_category"));
            vo.setPetImgUrl(str(map, "pet_img_url"));

            if (!isMyApply) {
                vo.setUserNickName(str(map, "user_nickname"));
                String userPhone = str(map, "user_phone");
                if (userPhone != null && userPhone.length() >= 11) {
                    vo.setUserPhone(userPhone.substring(0, 3) + "****" + userPhone.substring(7));
                } else {
                    vo.setUserPhone(userPhone);
                }
            }
            result.add(vo);
        }
        return result;
    }

    private String getStatusText(Integer applyStatus) {
        if (applyStatus == null) {
            return "未知";
        }
        switch (applyStatus) {
            case 0:
                return "待审核";
            case 1:
                return "已通过";
            case 2:
                return "已拒绝";
            default:
                return "未知状态";
        }
    }
}