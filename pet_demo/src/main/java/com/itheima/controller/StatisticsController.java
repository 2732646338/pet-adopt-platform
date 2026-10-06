package com.itheima.controller;

import com.itheima.common.Result;
import com.itheima.service.StatisticsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;


    @GetMapping("/basic")
    public Result<?> getBasicStatistics(HttpServletRequest request){
        String role = (String) request.getAttribute("role");
        if(!role.equals("admin")){
            return Result.error(403,"权限不足");
        }
        return Result.success(statisticsService.getBasicStatistics());
    }

    @GetMapping("/full")
    public Result<?> getFullStatistics(HttpServletRequest request){
        String role = (String) request.getAttribute("role");
        if(!role.equals("admin")){
            return Result.error(403,"权限不足");
        }
        return Result.success(statisticsService.getFullStatistics());
    }
}
