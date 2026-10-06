package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.dto.AdoptApplyDTO;
import com.itheima.dto.AdoptApplyVO;
import com.itheima.dto.AdoptAuditDTO;
import com.itheima.entity.AdoptApply;

import java.util.List;

public interface AdoptApplyService extends IService<AdoptApply> {
    //用户端：提交养养申请
    void submitApply(Long userId,AdoptApplyDTO applyDTO);

    //查询我的申请记录
    List<AdoptApplyVO> myApplyList(Long userId);

    //管理员端：查询所有申请记录
    List<AdoptApplyVO> adminApplyList(Integer status);

    //管理员端：审核养养申请
    void auditApply(Long applyId, AdoptAuditDTO auditDTO);

    //检查用户是否已对该宠物提交申请过
    boolean isApplied(Long userId, Long petId);
}
