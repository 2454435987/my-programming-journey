package com.tea.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.entity.TeaCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类 Mapper
 */
@Mapper
public interface TeaCategoryMapper extends BaseMapper<TeaCategory> {
}
