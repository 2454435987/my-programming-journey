package com.baidu.dao;

import com.baidu.pojo.Location;
import com.baidu.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
@Mapper
public interface LocationMapper extends BaseMapper<Location> {
    //根据用户电话进行模糊分页查询和普通分页查询
    public List<Location> getLocationByPage(
            @Param("phone")String phone,
            @Param("start")Integer start,
            @Param("pageSize")Integer pageSize,
            @Param("uid")Integer uid);


    //计算普通分页和模糊分页的总记录数
    public int myCount(@Param("phone") String phone,@Param("uid")Integer uid);
}
