package com.baidu.controller;

import com.baidu.pojo.Cart;
import com.baidu.service.impl.CartServiceImpl;
import com.baidu.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartServiceImpl cartService;

    //根据cid批量查询数据
    @PostMapping("/getCartByCids")
    public List<Cart> getCartByCids(@RequestBody Integer cids[]){
        System.out.println("----------getCartByCids--------");
        System.out.println("cids:"+cids);
        for (Integer cid : cids) {
            System.out.println("cid:"+cid);
        }
        //将数组转换成List<Integer>集合
        List<Integer> list = new ArrayList<>();
        for (Integer cid : cids) {
            list.add(cid);
        }
        List<Cart> list1 = cartService.listByIds(list);
        return list1;
    }

    //根据商品名称进行模糊分页查询和普通分页查询
    @PostMapping("/getCartPageByName")
    public PageResult<Cart> getCartPageByName(@RequestBody Map<String,Object> map){
        System.out.println("-----------getCartPageByName-------------");
        //从map中获取pname,limit,page

        String pname = (String) map.get("pname");
        Integer currentPage = (Integer) map.get("page");
        Integer pageSize = (Integer) map.get("limit");
        System.out.println("pname:"+pname+" currentPage"+currentPage+" pageSize:"+pageSize);

        //执行查询
        PageResult<Cart> pageResult=cartService.getCartPageByName(pname,currentPage,pageSize);

        return pageResult;
    }


    //根据cid删除
    @GetMapping("/deleteCartByCid/{cid}")
    public boolean deleteCartByCid(@PathVariable("cid") Integer cid){
        System.out.println("-------deleteCartByCid-------");
        System.out.println("cid:"+cid);

        boolean flag = cartService.removeById(cid);
        return flag;
    }

    //根据cid执行批量删除
    @PostMapping("/deleteCartByCids")
    public boolean deleteCartByCids(@RequestBody Integer cids[]){
        System.out.println("----------deleteCartByCids--------");
        System.out.println("cids:"+cids);
        for (Integer cid : cids) {
            System.out.println("cid:"+cid);
        }
        //将数组转换成List<Integer>集合
        List<Integer> list = new ArrayList<>();
        for (Integer cid : cids) {
            list.add(cid);
        }
        boolean flag = cartService.removeBatchByIds(list);
        return flag;
    }

    //根据用户主键和商品名称进行模糊分页查询和普通分页查询
    @PostMapping("/getCartPageByLike")
    public PageResult<Cart> getCartPageByLike(@RequestBody Map<String,Object> map){
        System.out.println("-----------getCartPageByLike-------------");
        //从map中获取uid,pname,limit,page
        Integer uid=(Integer) map.get("userId");
        String pname = (String) map.get("pname");
        Integer currentPage = (Integer) map.get("page");
        Integer pageSize = (Integer) map.get("limit");
        System.out.println("uid:"+uid+" pname:"+pname+" currentPage"+currentPage+" pageSize:"+pageSize);

        //执行查询
        PageResult<Cart> pageResult=cartService.getCartPageByUidAndPname(uid,pname,currentPage,pageSize);

        return pageResult;
    }

    //实现根据uid和pid查询指定的购物车信息
    @RequestMapping("/getCartByPidAndUid/{pid}/{uid}")
    public Cart getCartByPidAndUid(@PathVariable("pid")Integer pid,@PathVariable("uid")Integer uid){
        System.out.println("-----------getCartByPidAndUid-----------");
        System.out.println("pid:"+pid);
        System.out.println("uid:"+uid);
        //执行查询
        Cart cart = cartService.getCartByUidAndPid(uid, pid);
        return cart;
    }
    //实现修改购物车中商品数量
    @PostMapping("/updateCartByCid")
    public boolean updateCartByCid(@RequestBody Cart cart){
        System.out.println("-----------updateCartByCid-----------");
        System.out.println("cart:"+cart);
        //执行修改
        boolean flag = cartService.updateById(cart);
        return flag;
    }

    //实现添加到购物车
    @PostMapping("/addCart")
    public boolean addCart(@RequestBody Cart cart){
        System.out.println("---------addCart-----------");
        System.out.println("cart:"+cart);

        //执行添加
        boolean flag = cartService.save(cart);
        return flag;
    }
}
