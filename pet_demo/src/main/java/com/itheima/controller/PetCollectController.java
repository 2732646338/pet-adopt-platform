package com.itheima.controller;

import com.itheima.common.Result;
import com.itheima.dto.CollectDTO;
import com.itheima.dto.CollectVO;

import com.itheima.service.PetCollectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collect")
@RequiredArgsConstructor
public class PetCollectController {

    private final PetCollectService petCollectService;

    //收藏宠物
    @PostMapping("add")
    public Result<?> collect(@Validated @RequestBody CollectDTO collectDTO, HttpServletRequest request) {

        Long userId=(Long) request.getAttribute("userId");
        petCollectService.collect(userId, collectDTO.getPetId());
        return Result.success("收藏成功");
    }

    //取消收藏宠物
    @PostMapping("cancel")
    public Result<?> cancelCollect(@Validated @RequestBody CollectDTO collectDTO, HttpServletRequest request) {
        Long userId=(Long) request.getAttribute("userId");
        petCollectService.cancelCollect(userId, collectDTO.getPetId());
        return Result.success("取消收藏成功");
    }

    //查询用户的收藏列表
    @PostMapping("/myList")
    public Result<List<CollectVO>> myCollects(HttpServletRequest request) {
        Long userId=(Long) request.getAttribute("userId");
        return Result.success(petCollectService.selectCollectList(userId));
    }
    //检查是否已收藏
    @PostMapping("/isCollected")
    public Result<Boolean> isCollected(HttpServletRequest request, @RequestParam Long petId) {
        Long userId=(Long) request.getAttribute("userId");
        return Result.success(petCollectService.isCollected(userId, petId));
    }
}