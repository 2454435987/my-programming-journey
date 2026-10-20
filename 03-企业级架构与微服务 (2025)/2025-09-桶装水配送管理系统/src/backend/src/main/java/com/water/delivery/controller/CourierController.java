package com.water.delivery.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.water.delivery.common.Result;
import com.water.delivery.dto.CourierDTO;
import com.water.delivery.entity.Courier;
import com.water.delivery.service.CourierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Tag(name = "送水工与片区管理")
@RestController
@RequestMapping("/api/courier")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;

    @Operation(summary = "分页查询送水工档案列表")
    @GetMapping("/page")
    public Result<Page<Map<String, Object>>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Integer status) {
        return Result.success(courierService.queryPage(page, size, keyword, regionId, status));
    }

    @Operation(summary = "新增或编辑送水工档案")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Result<Boolean> saveOrUpdate(@Valid @RequestBody CourierDTO dto) {
        return Result.success("操作成功", courierService.saveOrUpdateCourier(dto));
    }

    @Operation(summary = "划定负责片区")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/assign-region")
    public Result<Boolean> assignRegion(
            @RequestParam(required = false) Long courierId,
            @RequestParam(required = false) Long regionId,
            @RequestBody(required = false) Map<String, Object> body) {
        if (body != null) {
            if (courierId == null && body.get("courierId") != null) {
                courierId = Long.valueOf(body.get("courierId").toString());
            }
            if (regionId == null && body.get("regionId") != null) {
                regionId = Long.valueOf(body.get("regionId").toString());
            }
        }
        if (courierId == null || regionId == null) {
            throw new com.water.delivery.common.exception.BusinessException("缺少必要的参数: courierId 或 regionId");
        }
        return Result.success("片区划分成功", courierService.assignRegion(courierId, regionId));
    }

    @Operation(summary = "切换在职/停用状态")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/status")
    public Result<Boolean> updateStatus(
            @RequestParam(required = false) Long courierId,
            @RequestParam(required = false) Integer status,
            @RequestBody(required = false) Map<String, Object> body) {
        if (body != null) {
            if (courierId == null) {
                if (body.get("courierId") != null) {
                    courierId = Long.valueOf(body.get("courierId").toString());
                } else if (body.get("id") != null) {
                    courierId = Long.valueOf(body.get("id").toString());
                }
            }
            if (status == null && body.get("status") != null) {
                status = Integer.valueOf(body.get("status").toString());
            }
        }
        if (courierId == null || status == null) {
            throw new com.water.delivery.common.exception.BusinessException("缺少必要的参数: courierId 或 status");
        }
        return Result.success("状态更新成功", courierService.updateStatus(courierId, status));
    }

    @Operation(summary = "获取某片区下的在职送水工列表")
    @GetMapping({"/list-by-region", "/list-by-region/{regionId}"})
    public Result<List<Courier>> listByRegion(
            @PathVariable(required = false) Long regionId,
            @RequestParam(required = false) Long regionIdParam) {
        Long targetRegionId = regionId != null ? regionId : regionIdParam;
        if (targetRegionId == null) {
            targetRegionId = 1L;
        }
        return Result.success(courierService.listActiveByRegion(targetRegionId));
    }
}
