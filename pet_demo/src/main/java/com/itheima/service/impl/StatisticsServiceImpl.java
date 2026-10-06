package com.itheima.service.impl;

import com.itheima.Mapper.StatisticsMapper;
import com.itheima.dto.AdminStatisticsVO;
import com.itheima.dto.StatisticsVO;
import com.itheima.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.LinkOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticsMapper statisticsMapper;

    @Override
    public StatisticsVO getBasicStatistics() {
        StatisticsVO vo = new StatisticsVO();
        vo.setTotalPets(statisticsMapper.countTotalPets());
        vo.setAdoptedPets(statisticsMapper.countAdoptedPets());
        vo.setTotalUsers(statisticsMapper.countTotalUsers());
        vo.setPendingApplies(statisticsMapper.countPendingApplies());

        List<Map<String,Object>> trendData = statisticsMapper.selectApplyTrend();
        vo.setDateRange(getDateRange());
        vo.setApplyCounts(fillTrendData(trendData));
        return vo;
    }

    @Override
    public AdminStatisticsVO getFullStatistics() {
        return null;
    }
    //生成近七日每天申请量的日期范围
    private List<String> getDateRange() {
        List<String> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(int i = 6;i>=0;i--){
            LocalDate date = LocalDate.now().minusDays(i);
            dates.add(date.format(formatter));
        }
        return dates;
    }

    //填充趋势数据
    private List<Long> fillTrendData(List<Map<String,Object>> trendData) {
        Map<String,Long> map = trendData.stream()
                .filter(m -> m.get("date") != null)
                .collect(Collectors.toMap(
                        m->m.get("date").toString(),
                        m->((Number)m.get("count")).longValue()
                ));
        List<Long> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(int i=6;i>=0;i--){
            String dateStr = LocalDate.now().minusDays(i).format(formatter);
            result.add(map.getOrDefault(dateStr,0L));
        }
        return result;
    }
}
