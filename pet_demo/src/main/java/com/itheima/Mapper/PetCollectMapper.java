package com.itheima.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.dto.CollectVO;
import com.itheima.entity.PetCollect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface PetCollectMapper extends BaseMapper<PetCollect> {
    @Select("select c.id,c.pet_id,c.collect_time,"
            +"p.pet_name,p.category,p.img_url,p.address,p.status "
            +"from pet_collect c left join pet p on c.pet_id = p.id "
            +"where c.user_id = #{userId}")
    List<Map<String,Object>> selectCollectMap(@Param("userId") Long userId);
}