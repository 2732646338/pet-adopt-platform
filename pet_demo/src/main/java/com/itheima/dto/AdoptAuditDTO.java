package com.itheima.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AdoptAuditDTO {

    @NotNull(message = "审核状态不能为空")
    private Integer applyStatus;

    private String auditNote;
}
