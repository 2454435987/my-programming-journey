package com.baidu.service.impl;

import com.baidu.pojo.Cart;
import com.baidu.dao.CartMapper;
import com.baidu.service.CartService;
import com.baidu.utils.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private CartMapper cartMapper;

    //根据商品名称进行模糊分页查询和普通分页查询
    public PageResult<Cart> getCartPageByName(String pname, Integer currentPage, Integer pageSize){
        System.out.println("-----------getCartPageByName---------");
        PageResult<Cart> pageResult = new PageResult<>();
        pageResult.setCode(0);
        pageResult.setMsg("分页查询购物车成功");

        // 1. 统计符合条件的购物车总数 (传入 uid=null 代表不按用户过滤)
        int total = cartMapper.myCount(null, pname);
        pageResult.setCount(total);

        // 2. 计算分页偏移量
        Integer start = (currentPage - 1) * pageSize;

        // 3. 执行多表联查获取列表 (传入 uid=null 代表不按用户过滤)
        List<Cart> list = cartMapper.getCartPageByUidAndPname(null, pname, start, pageSize);
        pageResult.setData(list);

        return pageResult;
    }


    //根据uid和pid查询购物车信息
    public Cart getCartByUidAndPid(int uid, int pid) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        //封装uid和pid
        queryWrapper.eq("pid",pid);
        queryWrapper.eq("uid",uid);
        //执行查询
        Cart cart = cartMapper.selectOne(queryWrapper);
        return cart;
    }
    public int myCount(Integer uid, String pname) {
        return 0;
    }

    //根据用户主键和商品名称进行模糊分页查询和普通分页查询
    public PageResult<Cart> getCartPageByUidAndPname(Integer uid, String
            pname, Integer currentPage, Integer pageSize) {
        //创建PageResult<Cart>对象，用于封装最终的返回结果
        PageResult<Cart> pageResult = new PageResult<>();
        pageResult.setCode(0);
        pageResult.setMsg("分页查询购物车成功");

        //计算普通分页与模糊分页的总记录数
        int total=cartMapper.myCount(uid,pname);
        //将总记录数保存到pageResult中
        pageResult.setCount(total);

        //计算起始位置
        Integer start = (currentPage-1)*pageSize;
        //普通分页与模糊分页，实现分页查询购物车信息
        List<Cart> list=cartMapper.getCartPageByUidAndPname(uid, pname, start, pageSize);
        //将集合保存pageResult中国
        pageResult.setData(list);
        return pageResult;
    }






}
