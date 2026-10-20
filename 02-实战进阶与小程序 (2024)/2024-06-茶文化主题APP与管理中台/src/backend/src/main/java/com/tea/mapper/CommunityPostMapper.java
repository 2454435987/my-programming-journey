package com.tea.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.entity.CommunityPost;
import org.apache.ibatis.annotations.Mapper;

/**
 * 社区帖子 Mapper
 */
@Mapper
public interface CommunityPostMapper extends BaseMapper<CommunityPost> {
}
