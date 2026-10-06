package com.itheima.controller;

import com.itheima.common.Result;
import com.itheima.dto.CommentSaveDTO;
import com.itheima.dto.CommentVO;
import com.itheima.entity.PetComment;
import com.itheima.service.PetCommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class PetCommentController {

    private final PetCommentService petCommentService;

    //新增留言
    @PostMapping("/add")
    public Result<?> save(@Validated @RequestBody CommentSaveDTO commentSaveDTO, HttpServletRequest request) {
        Long userId = Long.parseLong((String) request.getAttribute("userId").toString());
        petCommentService.saveComment(userId, commentSaveDTO);
        return Result.success("留言成功");
    }

    //查询宠物所有留言
    @GetMapping("/list/{petId}")
    public Result<List<CommentVO>> list(@PathVariable Long petId) {
        List<CommentVO> commentVOList = petCommentService.getCommentByPetId(petId);
        return Result.success(commentVOList);


    }

    //删除留言
    @DeleteMapping("/delete/{commentId}")
    public Result<?> delete(@PathVariable Long commentId, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");

        if(!role.equals("admin")){
            return Result.error(403,"非管理员，无权限删除留言");
        }

        petCommentService.deleteComment(commentId);
        return Result.success("删除成功");
    }
}
