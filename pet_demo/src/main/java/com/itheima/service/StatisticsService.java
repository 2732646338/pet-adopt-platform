package com.itheima.service;

import com.itheima.dto.AdminStatisticsVO;
import com.itheima.dto.StatisticsVO;

public interface StatisticsService {

    //基础统计数据
    StatisticsVO getBasicStatistics();

    //完整统计数据
    AdminStatisticsVO getFullStatistics();
}
