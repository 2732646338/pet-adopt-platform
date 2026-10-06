package com.itheima.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.entity.Pet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PetMapper extends BaseMapper<Pet> {
    @Update("UPDATE pet SET view_count = view_count + 1 WHERE id = #{id}")
    void incrementViewCount(Long id);
}
