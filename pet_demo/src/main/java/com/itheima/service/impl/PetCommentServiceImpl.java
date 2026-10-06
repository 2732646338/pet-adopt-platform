package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.Mapper.PetCommentMapper;
import com.itheima.dto.CommentSaveDTO;
import com.itheima.dto.CommentVO;
import com.itheima.entity.Pet;
import com.itheima.entity.PetComment;
import com.itheima.service.PetCommentService;
import com.itheima.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PetCommentServiceImpl extends ServiceImpl<PetCommentMapper, PetComment>
        implements PetCommentService {

    private final PetCommentMapper petCommentMapper;
    private final PetService petService;

    @Override
    public void saveComment(Long userId, CommentSaveDTO commentSaveDTO) {
        Pet pet = petService.getById(commentSaveDTO.getPetId());
        if (pet == null) {
            throw new IllegalArgumentException("宠物不存在或已删除");
        }
        Integer isDeleted = pet.getIsDeleted();
        if (isDeleted != null && isDeleted == 1) {
            throw new IllegalArgumentException("宠物不存在或已删除");
        }


        PetComment petComment = new PetComment();
        petComment.setUserId(userId);
        petComment.setPetId(commentSaveDTO.getPetId());
        petComment.setContent(commentSaveDTO.getContent());
        save(petComment);
    }

    @Override
    public List<CommentVO> getCommentByPetId(Long petId) {

        Pet pet = petService.getById(petId);
        if (pet == null) {
            throw new IllegalArgumentException("宠物不存在或已删除");
        }
        Integer isDeleted = pet.getIsDeleted();
        if (isDeleted != null && isDeleted == 1) {
            throw new IllegalArgumentException("宠物不存在或已删除");
        }
        List<Map<String, Object>> maps = petCommentMapper.selectCommentList(petId);
        List<CommentVO> result = new ArrayList<>();
        for(Map<String, Object> map:maps){
            CommentVO commentVO = new CommentVO();
            commentVO.setId((Long)map.get("id"));
            commentVO.setPetId((Long)map.get("pet_id"));
            commentVO.setContent((String)map.get("content"));
            commentVO.setUserId((Long)map.get("user_id"));
            commentVO.setCreateTime((LocalDateTime)map.get("create_time"));
            commentVO.setUserNickname((String)map.get("user_nickname"));
            commentVO.setUserAvatar((String)map.get("user_avatar"));
            result.add(commentVO);
        }
        return result;
    }

    @Override
    public void deleteComment(Long commentId) {
        PetComment petComment = petCommentMapper.selectById(commentId);
        if(petComment==null) {
            throw new IllegalArgumentException("留言不存在");
        }
        petCommentMapper.deleteById(commentId);
    }
}
