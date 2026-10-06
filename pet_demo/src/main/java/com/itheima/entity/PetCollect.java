package com.itheima.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pet_collect")
public class PetCollect {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long petId;
    private Long userId;
    private LocalDateTime collectTime;
}
