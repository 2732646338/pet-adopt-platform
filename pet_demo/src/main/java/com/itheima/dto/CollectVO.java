package com.itheima.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollectVO {
    private Long id;
    private Long petId;
    private String petName;
    private String category;
    private String imgUrl;
    private String address;
    private Integer status;
    private LocalDateTime collectTime;
}
