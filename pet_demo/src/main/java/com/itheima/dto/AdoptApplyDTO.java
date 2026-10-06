package com.itheima.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class AdoptApplyDTO {
    @NotNull(message = "请选择宠物")
    private Long petId;

    @NotNull(message = "请输入手机号")
    private String phone;

    private String liveEnv;
    private String petExp;

}
