package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.dto.CollectVO;
import com.itheima.entity.PetCollect;

import java.util.List;

public interface PetCollectService extends IService<PetCollect> {

    //收藏宠物
    void collect(Long userId, Long petId);

    //取消收藏宠物
    void cancelCollect(Long userId, Long petId);

    //查询用户收藏宠物列表
    List<CollectVO> selectCollectList(Long userId);

    //检查是否已收藏
    boolean isCollected(Long userId, Long petId);
}
