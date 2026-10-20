package com.tea.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.entity.SysAdminRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员角色关联 Mapper
 */
@Mapper
public interface SysAdminRoleMapper extends BaseMapper<SysAdminRole> {
}
