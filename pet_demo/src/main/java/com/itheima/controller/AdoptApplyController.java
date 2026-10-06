package com.itheima.controller;


import com.itheima.common.Result;
import com.itheima.dto.AdoptApplyDTO;
import com.itheima.dto.AdoptApplyVO;
import com.itheima.dto.AdoptAuditDTO;
import com.itheima.service.AdoptApplyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adopt")
@RequiredArgsConstructor
public class AdoptApplyController {

    private final AdoptApplyService adoptApplyService;

    //用户端：提交领养申请
    @PostMapping("apply")
    public Result<?> submitApply(@Validated @RequestBody AdoptApplyDTO applyDTO, HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        adoptApplyService.submitApply(userId, applyDTO);
        return Result.success("提交领养申请成功");
    }

    //用户端：查询我的领养申请记录
    @GetMapping("myApply")
    public Result<List<AdoptApplyVO>> getMyApply(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(adoptApplyService.myApplyList(userId));
    }

    //管理员端：查询所有领养申请记录
    @GetMapping("adminList")
    public Result<List<AdoptApplyVO>> allApplies(@RequestParam(required = false) Integer status){
        if(status == null){
            status = 0;
        }
        return Result.success(adoptApplyService.adminApplyList(status));
    }

    //管理员端：审核领养申请
    @PutMapping("audit/{id}")
    public Result<?> auditApply(@PathVariable Long id,@Validated @RequestBody AdoptAuditDTO auditDTO){
        adoptApplyService.auditApply(id, auditDTO);
        return Result.success(auditDTO.getApplyStatus() == 1 ? "审核通过" : "审核拒绝");
    }
}
