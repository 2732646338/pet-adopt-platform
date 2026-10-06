package com.itheima.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommentSaveDTO {
    @NotNull(message = "宠物ID不能为空")
    private Long petId;

    @NotNull(message = "评论内容不能为空")
    private String content;

}
