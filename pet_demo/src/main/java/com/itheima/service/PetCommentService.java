package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.dto.CommentSaveDTO;
import com.itheima.dto.CommentVO;
import com.itheima.entity.PetComment;

import java.util.List;

public interface PetCommentService extends IService<PetComment> {
    //新增留言
    void saveComment(Long userId, CommentSaveDTO commentSaveDTO);

    //查询宠物所有留言
    List<CommentVO> getCommentByPetId(Long petId);

    //删除留言
    void deleteComment(Long commentId);
}
