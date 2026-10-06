package com.itheima.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.entity.AdoptApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdoptApplyMapper extends BaseMapper<AdoptApply> {

    @Select("select a.*,"
            +"p.pet_name as pet_name, p.category as pet_category, p.img_url as pet_img_url "
            +"from adopt_apply a left join pet p on p.id = a.pet_id "
            +"where a.user_id = #{userId} "
            +"order by a.create_time desc")
    List<Map<String, Object>> selectMyApplyList(@Param("userId") Long userId);

    @Select("select a.*,"
            +"u.nickname as user_nickname, u.phone as user_phone,"
            +"p.pet_name as pet_name, p.category as pet_category, p.img_url as pet_img_url "
            +"from adopt_apply a left join user u on u.id = a.user_id "
            +"left join pet p on p.id = a.pet_id "
            +"where (#{status} is null or a.apply_status = #{status}) "
            +"order by a.create_time desc")
    List<Map<String, Object>> selectAdminApplyList(@Param("status") Integer status);
}