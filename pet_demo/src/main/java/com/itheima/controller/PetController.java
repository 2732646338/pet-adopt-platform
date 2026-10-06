package com.itheima.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.Result;
import com.itheima.dto.PetPageDTO;
import com.itheima.dto.PetSaveDTO;
import com.itheima.dto.PetUpdateStatusDTO;
import com.itheima.entity.Pet;
import com.itheima.service.PetService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pet")
public class PetController {

    private final PetService petService;
    //=============用户端接口=============================
    @PostMapping("/list")
    public Result<Page<Pet>> pageUser(@RequestBody PetPageDTO petPageDTO){
        return Result.success(petService.pageUser(petPageDTO));
    }

    @GetMapping("/detail/{id}")
    public Result<Pet> detailPet(@PathVariable Long id){
        return Result.success(petService.getDetail(id));
    }

    //=============管理员端接口=============================

    @PostMapping("/adminList")
    public Result<Page<Pet>> pageAdmin(@RequestBody PetPageDTO petPageDTO){
        return Result.success(petService.pageAdmin(petPageDTO));
    }

    @PostMapping("/add")
    public Result<?> savePet(@Validated @RequestBody PetSaveDTO petSaveDTO, HttpServletRequest request){
        Long adminId = (Long) request.getAttribute("userId");

        String role = (String) request.getAttribute("role");
        if(!"admin".equals(role)){
            return Result.error(403,"您没有权限操作");
        }

        petService.savePet(petSaveDTO,adminId);
        return Result.success("保存成功");
    }
    @PostMapping("/update")
    public Result<?> updatePetStatus(@PathVariable Long id, @Validated @RequestBody PetUpdateStatusDTO petUpdateStatusDTO){
        petService.updatePetStatus(id,petUpdateStatusDTO);
        return Result.success("更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> deletePet(@PathVariable Long id){
        petService.deletePet(id);
        return Result.success("删除成功");
    }

    //图片上传
    @PostMapping("/admin/upload")
    public Result<?> uploadImage(@RequestParam("file") MultipartFile file){
        String url = "http://your-domain/pet/" + System.currentTimeMillis() + ".jpg";
        return Result.success(url);
    }
}