package com.itheima.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pet")
public class Pet extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String petName;
    private String category;
    private Integer age;
    private String gender;
    private String health;
    private String description;
    private String imgUrl;  //封面图
    private String address;
    private Integer status;  //0-待领养 1-已领养 2-下架
    private LocalDateTime createTime;  //发布时间
    private Integer viewCount;
}
