package com.water.delivery.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.water.delivery.common.Result;
import com.water.delivery.dto.TicketCancelRefundDTO;
import com.water.delivery.dto.TicketRechargeDTO;
import com.water.delivery.entity.TicketType;
import com.water.delivery.security.JwtTokenProvider;
import com.water.delivery.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Tag(name = "水票资产与流转")
@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final JwtTokenProvider tokenProvider;

    @Operation(summary = "获取所有在售水票品牌规格")
    @GetMapping("/types")
    public Result<List<TicketType>> listTypes() {
        return Result.success(ticketService.listActiveTypes());
    }

    @Operation(summary = "水票充值购买与发放")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/recharge")
    public Result<Boolean> recharge(HttpServletRequest request, @Valid @RequestBody TicketRechargeDTO dto) {
        Long opId = getUserId(request);
        return Result.success("水票充值成功", ticketService.recharge(dto, opId));
    }

    @Operation(summary = "水票作废或退款回收")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/cancel-refund")
    public Result<Boolean> cancelOrRefund(HttpServletRequest request, @Valid @RequestBody TicketCancelRefundDTO dto) {
        Long opId = getUserId(request);
        return Result.success("处理成功", ticketService.cancelOrRefund(dto, opId));
    }

    @Operation(summary = "分页查询水票变动流水")
    @GetMapping("/flow/page")
    public Result<Page<Map<String, Object>>> flowPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer bizType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return Result.success(ticketService.queryFlowPage(page, size, customerId, keyword, bizType, startDate, endDate));
    }

    private Long getUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return tokenProvider.getUserIdFromToken(token.substring(7).trim());
        }
        return 1L;
    }
}
