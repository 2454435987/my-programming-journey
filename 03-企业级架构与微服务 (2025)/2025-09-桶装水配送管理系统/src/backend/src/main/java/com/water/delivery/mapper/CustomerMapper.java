package com.water.delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.water.delivery.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}
