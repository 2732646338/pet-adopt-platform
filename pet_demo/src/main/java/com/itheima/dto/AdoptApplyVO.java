package com.itheima.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdoptApplyVO {
    //申请信息
    private Long id;
    private Long userId;
    private String userNickName;
    private String userPhone;
    private Long petId;
    private String petName;
    private String petCategory;
    private String petImgUrl;
    private String phone;
    private String liveEnv;
    private String petExp;
    private Integer applyStatus;
    private String applyStatusText;
    private String auditNote;
    private LocalDateTime createTime;
}
