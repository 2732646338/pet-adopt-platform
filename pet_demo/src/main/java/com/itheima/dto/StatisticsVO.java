package com.itheima.dto;

import lombok.Data;

import java.util.List;

@Data
public class StatisticsVO {

    //核心指标
    private Long totalPets;  //宠物总数量
    private Long adoptedPets;  //已领养宠物数量
    private Long totalUsers;  //用户总数量
    private Long pendingApplies;  //待审核申请数量
    private Long totalComments;  //留言总数量

    //Echarts数据
    private List<String> dateRange;   //日期范围
    private List<Long> applyCounts;   //申请数量

}
