package com.itheima.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CollectDTO {
    @NotNull(message = "宠物id不能为空")
    private Long petId;
}
