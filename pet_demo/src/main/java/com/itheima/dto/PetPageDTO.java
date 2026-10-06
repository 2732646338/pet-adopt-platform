package com.itheima.dto;

import lombok.Data;

@Data
public class PetPageDTO {
    private Integer pageNo=1;
    private Integer pageSize=10;
    private String petName;
    private String category;
    private Integer status;
    private String gender;
    private Integer ageMin;
    private Integer ageMax;
    private String address;
}
