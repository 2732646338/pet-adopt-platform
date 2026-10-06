package com.itheima.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AdminStatisticsVO {

    private Long totalUsers;  //用户总数量
    private Long adoptedPets;  //已领养宠物数量
    private Long totalPets;  //宠物总数量
    private Long pendingApplies;  //待审核申请数量

    //Echarts数据
    private List<String>  dateRange;
    private List<Long> applyCounts;

    private List<Map<String, Object>> categoryStats;  //宠物分类统计
}
