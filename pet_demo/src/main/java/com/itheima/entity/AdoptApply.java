package com.itheima.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("adopt_apply")
public class AdoptApply {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private Long petId;
    private String phone;
    private String liveEnv;
    private String petExp;
    private Integer applyStatus;
    private String auditNote;
    private LocalDateTime createTime;




}
