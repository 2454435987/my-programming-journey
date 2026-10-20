package com.tea.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.entity.UserFavorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户收藏 Mapper
 */
@Mapper
public interface UserFavoriteMapper extends BaseMapper<UserFavorite> {
}
