package com.water.delivery.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.water.delivery.common.Result;
import com.water.delivery.dto.DeliveryHistoryQueryDTO;
import com.water.delivery.dto.VerifyDeliverDTO;
import com.water.delivery.security.JwtTokenProvider;
import com.water.delivery.service.DeliveryService;
import com.water.delivery.vo.AiInspectVO;
import com.water.delivery.vo.DeliveryOrderVO;
import com.water.delivery.vo.DeliverySummaryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@Tag(name = "核心业务：水票核销配送与AI质检")
@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final JwtTokenProvider tokenProvider;

    @Operation(summary = "核心业务闭环：水票核销与配送生成 (水票-N, 历史+1, 空桶对冲)")
    @PostMapping("/verify-and-deliver")
    public Result<Map<String, Object>> verifyAndDeliver(HttpServletRequest request, @Valid @RequestBody VerifyDeliverDTO dto) {
        Long opUserId = getUserId(request);
        return Result.success("核销配送成功", deliveryService.verifyAndDeliver(dto, opUserId));
    }

    @Operation(summary = "多模态AI空桶智能质检 (支持上传拍照图片或指定测试样例)")
    @PostMapping("/barrel/ai-inspect")
    public Result<AiInspectVO> aiInspect(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "sampleType", required = false) String sampleType) {
        return Result.success(deliveryService.aiInspectBarrel(file, sampleType));
    }

    @Operation(summary = "多条件分页查询配送历史")
    @PostMapping("/history/page")
    public Result<Page<DeliveryOrderVO>> historyPage(@RequestBody DeliveryHistoryQueryDTO queryDTO) {
        return Result.success(deliveryService.queryHistoryPage(queryDTO));
    }

    @Operation(summary = "当前筛选条件下的累计配送量与回桶汇总统计")
    @PostMapping("/history/summary")
    public Result<DeliverySummaryVO> historySummary(@RequestBody DeliveryHistoryQueryDTO queryDTO) {
        return Result.success(deliveryService.aggregateSummary(queryDTO));
    }

    @Operation(summary = "获取送水工片区待配任务列表 (手机端工作台专用)")
    @GetMapping("/pending-tasks")
    public Result<List<Map<String, Object>>> getPendingTasks(@RequestParam Long courierId) {
        return Result.success(deliveryService.getPendingTasks(courierId));
    }

    private Long getUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return tokenProvider.getUserIdFromToken(token.substring(7).trim());
        }
        return 1L;
    }
}
