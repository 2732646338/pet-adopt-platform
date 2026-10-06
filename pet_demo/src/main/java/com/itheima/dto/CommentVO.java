package com.itheima.dto;

import lombok.Data;

import java.time.LocalDateTime;

//留言列表返回
@Data
public class CommentVO {

    private Long id;

    private Long petId;
    private Long userId;
    private String userNickname;
    private String userAvatar;
    private String content;
    private LocalDateTime createTime;
}
