package com.itheima.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.Mapper.PetMapper;
import com.itheima.common.BusinessException;
import com.itheima.dto.PetPageDTO;
import com.itheima.dto.PetSaveDTO;
import com.itheima.dto.PetUpdateStatusDTO;
import com.itheima.entity.Pet;
import com.itheima.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class PetServiceImpl extends ServiceImpl<PetMapper, Pet> implements PetService {
    private final PetMapper petMapper;

    @Override
    public Page<Pet> pageUser(PetPageDTO petPageDTO) {
        LambdaQueryWrapper<Pet> queryWrapper = buildWrapper(petPageDTO);
        queryWrapper.eq(Pet::getStatus, 0);
        Page<Pet> page = new Page<>(petPageDTO.getPageNo(), petPageDTO.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public Page<Pet> pageAdmin(PetPageDTO petPageDTO) {
        LambdaQueryWrapper<Pet> queryWrapper = buildWrapper(petPageDTO);
        Page<Pet> page = new Page<>(petPageDTO.getPageNo(), petPageDTO.getPageSize());
        return this.page(page, queryWrapper);
    }

    private LambdaQueryWrapper<Pet> buildWrapper(PetPageDTO petPageDTO) {
        LambdaQueryWrapper<Pet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(petPageDTO.getPetName()), Pet::getPetName, petPageDTO.getPetName())
                .eq(StringUtils.hasText(petPageDTO.getCategory()), Pet::getCategory, petPageDTO.getCategory())
                .eq(petPageDTO.getStatus() != null, Pet::getStatus, petPageDTO.getStatus())
                .eq(StringUtils.hasText(petPageDTO.getGender()), Pet::getGender, petPageDTO.getGender())
                .between(petPageDTO.getAgeMin() != null && petPageDTO.getAgeMax() != null, Pet::getAge, petPageDTO.getAgeMin(), petPageDTO.getAgeMax())
                .like(StringUtils.hasText(petPageDTO.getAddress()), Pet::getAddress, petPageDTO.getAddress())
                .orderByDesc(Pet::getCreateTime);
        return queryWrapper;
    }

    @Override
    public Pet getDetail(Long id) {
        Pet pet = this.getById(id);
        if (pet == null) {
            throw new BusinessException("宠物不存在");
        }
        petMapper.incrementViewCount(id);
        return pet;
    }

    @Override
    @Transactional
    public void savePet(PetSaveDTO petSaveDTO, Long adminId) {
        Pet pet = new Pet();
        if (petSaveDTO.getId() != null) {
            Pet exist = this.getById(petSaveDTO.getId());
            if (exist == null) {
                throw new BusinessException("宠物不存在");
            }
            pet.setId(petSaveDTO.getId());
        }
        pet.setGender(petSaveDTO.getGender());
        pet.setPetName(petSaveDTO.getPetName());
        pet.setCategory(petSaveDTO.getCategory());
        pet.setAge(petSaveDTO.getAge());
        pet.setAddress(petSaveDTO.getAddress());
        pet.setImgUrl(petSaveDTO.getImgUrl());
        pet.setHealth(petSaveDTO.getHealth());
        pet.setDescription(petSaveDTO.getDescription());
        pet.setCreateTime(LocalDateTime.now());
        if (pet.getId() == null) {
            pet.setStatus(0);
            pet.setViewCount(0);
            this.save(pet);
        } else {
            pet.setStatus(petSaveDTO.getStatus());
            this.updateById(pet);
        }
    }

    @Override
    public void updatePetStatus(Long id, PetUpdateStatusDTO petUpdateStatusDTO) {
        Pet pet = this.getById(id);
        if (pet == null) {
            throw new BusinessException("宠物不存在");
        }
        pet.setStatus(petUpdateStatusDTO.getStatus());
        this.updateById(pet);
    }

    @Override
    public void deletePet(Long id) {
        Pet pet = this.getById(id);
        if (pet == null) {
            throw new BusinessException("宠物不存在");
        }
        this.removeById(id);
    }


}