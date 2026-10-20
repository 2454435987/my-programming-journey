package com.tea.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.entity.ContentLike;
import org.apache.ibatis.annotations.Mapper;

/**
 * 内容点赞 Mapper
 */
@Mapper
public interface ContentLikeMapper extends BaseMapper<ContentLike> {
}
