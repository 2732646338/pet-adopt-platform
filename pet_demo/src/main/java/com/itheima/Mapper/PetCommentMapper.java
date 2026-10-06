package com.itheima.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.entity.PetComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface PetCommentMapper extends BaseMapper<PetComment> {

    //联表查询留言列表
    @Select("select c.id,c.pet_id,c.user_id,c.content,c.create_time"
            +",u.nickname as user_nickname,u.avatar as user_avatar"
            +" from pet_comment c left join user u on c.user_id=u.id"
            +" where c.pet_id=#{petId} order by c.create_time desc")
    List<Map<String, Object>> selectCommentList(@Param("petId") Long petId);
}
