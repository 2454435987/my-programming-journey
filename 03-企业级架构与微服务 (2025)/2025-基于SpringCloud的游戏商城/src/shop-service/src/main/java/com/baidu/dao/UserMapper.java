package com.baidu.dao;

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
public interface UserMapper extends BaseMapper<User> {

    //根据用户主键和用户姓名进行模糊分页查询和普通分页查询
    public List<User> getUserByPage(
                                              @Param("name")String name,
                                              @Param("start")Integer start,
                                              @Param("pageSize")Integer pageSize);

    //计算普通分页与模糊分页的总记录数
    public int myCount(String name);
}
