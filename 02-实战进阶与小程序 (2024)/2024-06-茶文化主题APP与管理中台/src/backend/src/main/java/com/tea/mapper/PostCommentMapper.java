package com.tea.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.entity.PostComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 帖子评论 Mapper
 */
@Mapper
public interface PostCommentMapper extends BaseMapper<PostComment> {
}
