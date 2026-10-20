package com.baidu.feignservice;

import com.baidu.pojo.Cart;
import com.baidu.utils.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "shop-service",path = "/cart")
public interface CartFeignService {

    //根据cid批量查询数据
    @PostMapping("/getCartByCids")
    public List<Cart> getCartByCids(@RequestBody Integer cids[]);

    //根据商品名称进行模糊分页查询和普通分页查询
    @PostMapping("/getCartPageByName")
    public PageResult<Cart> getCartPageByName(@RequestBody Map<String,Object> map);

    @GetMapping("/deleteCartByCid/{cid}")
    public boolean deleteCartByCid(@PathVariable("cid") Integer cid);

    @PostMapping("/deleteCartByCids")
    public boolean deleteCartByCids(@RequestBody Integer cids[]);

    //根据用户主键和商品名称进行模糊分页查询和普通分页查询
    @PostMapping("/getCartPageByLike")
    public PageResult<Cart> getCartPageByLike(@RequestBody Map<String,Object> map);

    @RequestMapping("/getCartByPidAndUid/{pid}/{uid}")
    public Cart getCartByPidAndUid(@PathVariable("pid")Integer pid, @PathVariable("uid")Integer uid);

    @PostMapping("/updateCartByCid")
    public boolean updateCartByCid(@RequestBody Cart cart);

    @PostMapping("/addCart")
    public boolean addCart(@RequestBody Cart cart);
}
