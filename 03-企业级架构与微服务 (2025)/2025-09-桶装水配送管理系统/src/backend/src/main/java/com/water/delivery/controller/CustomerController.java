package com.water.delivery.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.water.delivery.common.Result;
import com.water.delivery.dto.CustomerDTO;
import com.water.delivery.dto.CustomerQueryDTO;
import com.water.delivery.service.CustomerService;
import com.water.delivery.vo.CustomerVO;
import com.water.delivery.vo.TicketAccountVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "客户档案与水票余额")
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "分页查询客户档案列表 (支持余票预警过滤)")
    @PostMapping("/page")
    public Result<Page<CustomerVO>> queryPage(@RequestBody CustomerQueryDTO queryDTO) {
        return Result.success(customerService.queryPage(queryDTO));
    }

    @Operation(summary = "获取客户详情")
    @GetMapping("/{id}")
    public Result<CustomerVO> getDetail(@PathVariable Long id) {
        return Result.success(customerService.getCustomerDetail(id));
    }

    @Operation(summary = "查询客户持有的所有水票余额")
    @GetMapping("/{id}/tickets")
    public Result<List<TicketAccountVO>> getTickets(@PathVariable Long id) {
        return Result.success(customerService.getCustomerTickets(id));
    }

    @Operation(summary = "快速搜索客户 (按手机号或姓名快速联想)")
    @GetMapping("/search")
    public Result<List<CustomerVO>> search(@RequestParam String keyword) {
        return Result.success(customerService.searchCustomer(keyword));
    }

    @Operation(summary = "新增或编辑客户档案")
    @PostMapping
    public Result<Boolean> saveOrUpdate(@Valid @RequestBody CustomerDTO dto) {
        return Result.success("保存成功", customerService.saveOrUpdateCustomer(dto));
    }
}
