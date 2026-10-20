package com.tea.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.entity.AppUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * App用户 Mapper
 */
@Mapper
public interface AppUserMapper extends BaseMapper<AppUser> {
}
