package com.baidu.service.impl;

import com.baidu.pojo.User;
import com.baidu.dao.UserMapper;
import com.baidu.service.UserService;
import com.baidu.utils.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User login(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",user.getName());
        queryWrapper.eq("password",user.getPassword());
        queryWrapper.eq("role_id",user.getRoleId());
        User user1=userMapper.selectOne(queryWrapper);
        return user1;
    }


    public PageResult<User> getUserByPage(String name, Integer currentPage, Integer pageSize) {
        PageResult<User> pageResult = new PageResult<>();
        pageResult.setCode(0);
        pageResult.setMsg("分页查询用户成功");

        //计算普通分页或模糊分页的总记录数
        int total=userMapper.myCount(name);
        //将总记录数保存到pageResult
        pageResult.setCount(total);

        //计算起始位置
        Integer start=(currentPage-1)*pageSize;
        List<User> list = userMapper.getUserByPage(name, start, pageSize);
        pageResult.setData(list);
        return pageResult;
    }
}
