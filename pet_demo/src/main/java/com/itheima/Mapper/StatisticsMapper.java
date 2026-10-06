package com.itheima.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


@Mapper
public interface StatisticsMapper {
    //宠物总数
    @Select("select COUNT(*) from pet where is_deleted=0")
    Long countTotalPets();
    //已申请宠物总数
    @Select("select count(*) from pet where status=1 and is_deleted=0")
    Long countAdoptedPets();
    //用户总数
    @Select("select count(*) from user where is_deleted=0")
    Long countTotalUsers();
    //待审核申请数
    @Select("select count(*) from adopt_apply where apply_status=0")
    Long countPendingApplies();

    //近七日每天申请量
    @Select("select DATE(create_time) as date, count(*) as count"
            +"from adopt_apply "
            +"where create_time >= now() - INTERVAL 7 DAY"
            +"group by DATE(create_time)"
            +"order by DATE(create_time) desc")
       List<Map<String,Object>> selectApplyTrend();


    //宠物分类统计
    @Select("select category, count(*) as count"
            +"from pet "
            +"where is_deleted=0 and status=0"
            +"group by category")
       List<Map<String,Object>> selectCategoryStats();
}
