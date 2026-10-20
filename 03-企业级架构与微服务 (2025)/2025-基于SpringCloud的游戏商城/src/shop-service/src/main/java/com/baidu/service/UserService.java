package com.baidu.service;

import com.baidu.pojo.User;
import com.baidu.utils.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
public interface UserService extends IService<User> {
    public User login(User user);

    public PageResult<User> getUserByPage(String name,Integer currentPage, Integer pageSize);

}
