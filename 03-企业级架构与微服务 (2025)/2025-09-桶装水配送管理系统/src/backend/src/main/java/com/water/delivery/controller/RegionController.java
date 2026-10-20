package com.water.delivery.controller;

import com.water.delivery.common.Result;
import com.water.delivery.entity.Region;
import com.water.delivery.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "服务片区管理")
@RestController
@RequestMapping("/api/region")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @Operation(summary = "获取所有片区列表")
    @GetMapping("/list")
    public Result<List<Region>> list() {
        return Result.success(regionService.listAll());
    }

    @Operation(summary = "新增或编辑服务片区")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Result<Boolean> saveOrUpdate(@RequestBody Region region) {
        return Result.success(regionService.saveOrUpdate(region));
    }
}
