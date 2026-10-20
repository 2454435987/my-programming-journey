package com.tea.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.entity.TeaProduct;
import org.apache.ibatis.annotations.Mapper;

/**
 * 茶品商品 Mapper
 */
@Mapper
public interface TeaProductMapper extends BaseMapper<TeaProduct> {
}
