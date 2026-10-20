package com.water.delivery.controller;

import com.water.delivery.common.Result;
import com.water.delivery.service.DashboardService;
import com.water.delivery.vo.DashboardMetricsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "经营分析与统计看板")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "获取全盘经营指标与统计看板数据")
    @GetMapping("/metrics")
    public Result<DashboardMetricsVO> getMetrics() {
        return Result.success(dashboardService.getMetrics());
    }
}
