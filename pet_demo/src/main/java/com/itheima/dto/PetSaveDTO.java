package com.itheima.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PetSaveDTO {
    private Long id;

    @NotBlank(message = "宠物名称不能为空")
    private String petName;

    private String category;
    private String description;
    private String gender;
    private Integer age;
    private String health;
    private String address;
    private String imgUrl;


    @NotNull(message= "状态不能为空")
    private Integer status;

}
