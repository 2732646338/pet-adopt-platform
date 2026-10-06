package com.itheima.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.dto.PetPageDTO;
import com.itheima.dto.PetSaveDTO;
import com.itheima.dto.PetUpdateStatusDTO;
import com.itheima.entity.Pet;

public interface PetService extends IService<Pet> {
    //分页+条件查询(用户端)
    Page<Pet> pageUser(PetPageDTO petPageDTO);

    //分页+条件查询(管理员端)
    Page<Pet> pageAdmin(PetPageDTO petPageDTO);

    //宠物详情（增加浏览量）
    Pet getDetail(Long id);

    //新增/编辑宠物
    void savePet(PetSaveDTO petSaveDTO,Long adminId);

    //更新宠物状态
    void updatePetStatus(Long id, PetUpdateStatusDTO petUpdateStatusDTO);

    //删除宠物
    void deletePet(Long id);
}
