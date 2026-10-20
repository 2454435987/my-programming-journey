package com.baidu.controller;


import com.baidu.feignservice.CartFeignService;
import com.baidu.feignservice.ProductFeignService;
import com.baidu.feignservice.UserFeignService;
import com.baidu.pojo.Cart;
import com.baidu.pojo.Product;
import com.baidu.pojo.User;
import com.baidu.utils.PageResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartFeignService cartFeignService;

    @Autowired
    UserFeignService userFeignService;

    @Autowired
    ProductFeignService productFeignService;

    //实现购物车计算总价格
    @PostMapping("/countMoney")
    @ResponseBody
    public BigDecimal countMoney(@RequestParam("cids[]")Integer cids[]){
        System.out.println("------countMoney------cids:"+cids);
        List<Cart> list = cartFeignService.getCartByCids(cids);
        System.out.println("list:"+list);
        BigDecimal sum = BigDecimal.ZERO;
        for (Cart cart : list) {
            Product product = productFeignService.getProductById(cart.getPid());
            // 1. 获取单价
            BigDecimal price = product.getPrice();
            // 2. 获取数量（先转成 BigDecimal）
            BigDecimal quantity = new BigDecimal(cart.getPquantity());
            // 3. 计算单项总价：单价 × 数量 (使用 multiply 方法)
            BigDecimal itemTotal = price.multiply(quantity);
            // 4. 累加到总金额：总价 + 单项总价
            sum = sum.add(itemTotal);
        }
        return sum;
    }

    //实现分页查询所有购物车信息
    @PostMapping("/cartList2")
    @ResponseBody
    public PageResult<Cart> cartList(@RequestParam(defaultValue = "")String pname,
                                     @RequestParam(defaultValue = "1")Integer page,
                                     @RequestParam(defaultValue = "5")Integer pageSize){
        System.out.println("-----------cartList---------");
        if(pname.equals("")){
            pname=null;
        }
        System.out.println("pname:"+pname+" page:"+page+" pageSize:"+pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("pname",pname);
        map.put("page",page);
        map.put("limit",pageSize);
        PageResult<Cart> pageResult = cartFeignService.getCartPageByName(map);

        System.out.println(pageResult);
        return pageResult;
    }

    //跳转到购物车管理页面
    @RequestMapping("/toCartList")
    public String toCartList(){
        System.out.println("--------toCartList------");
        return "cart/list";
    }

    //跳转到前台购物车页面
    @RequestMapping("/showcartList")
    public String showcartList(){
        return "cart/showcartList";
    }

    @RequestMapping("/deleteCartByCid/{cid}")
    public String deleteCartByCid(@PathVariable("cid") Integer cid){
        System.out.println("--------deleteCartByCid----------");
        System.out.println("cid:"+cid);
        //远程调用，执行删除
        cartFeignService.deleteCartByCid(cid);

        return "redirect:/cart/cartList";
    }

    //批量删除购物车信息
    @RequestMapping("/batchDelCart")
    public String batchDelCart(@RequestParam(value = "cids[]") Integer cids[])
    {
        System.out.println("---------batchDelCart---------");
        System.out.println("cids:"+cids);
        for (Integer cid:cids) {
            System.out.println("cid:"+cid);
        }
        //远程调用，执行批量删除
        cartFeignService.deleteCartByCids(cids);
        return "redirect:/cart/cartList";
    }

    //分页显示购物车信息，主要包括购物车，商品和用户信息
    @RequestMapping("/cartList")
    @ResponseBody
    public PageResult<Cart> cartList(@RequestParam(defaultValue = "${cart.list.default-pname:}")String pname,
                                     @RequestParam(defaultValue = "${cart.list.default-page:1}")Integer page,
                                     @RequestParam(defaultValue = "${cart.list.default-limit:5}")Integer limit,
                                     HttpSession session
    ){
        System.out.println("-----------cartList----------");
        System.out.println("pname:"+pname);
        System.out.println("page:"+page);
        System.out.println("limit:"+limit);

        //如果pname="",则将值修改成null
        if("".equals(pname)){
            pname=null;
        }
        //从session中获取用户的主键uid
        User user = (User) session.getAttribute("user1");
        int uid=user.getUid();
        System.out.println("uid:"+uid);
        //创建Map<String,Object>对象，将模糊查询的关键字，当前页，页面容量，用户主键保存到map中
        Map<String, Object> map = new HashMap<>();
        map.put("pname",pname);
        map.put("page",page);
        map.put("limit",limit);
        map.put("userId",uid);
        //执行远程调用，根据uid实现分页查询指定用户的购物车信息
        PageResult<Cart> pageResult = cartFeignService.getCartPageByLike(map);
        return pageResult;
    }

    //添加商品到购物车
    @RequestMapping("/addCart")
    public String addCart(Integer pid, HttpSession session){
        System.out.println("---------addCart---------");
        System.out.println("pid"+pid);

        //从session中获取当前的登录用户对象
        User user = (User) session.getAttribute("user1");
        int uid = user.getUid();
        System.out.println("uid:"+uid);

        ///远程调用，根据pid和uid查询购物车信息
        Cart cart2=cartFeignService.getCartByPidAndUid(pid,uid);
        System.out.println("cart2:"+cart2);

        if(cart2!=null){//购物车中已经存在该用户和商品
            //直接修改商品的数量，使数量+1
            int pquantity=cart2.getPquantity();
            cart2.setPquantity(pquantity+1);

            //远程调用，执行修改
            cartFeignService.updateCartByCid(cart2);
        }else {//购物车中没有对应用户以及对应商品
            int pquantity=1;
            //参数比较多大情况下，如果能够封装成对象，则使用对象传递参数
            Cart cart = new Cart();
            cart.setPid(pid);
            cart.setPquantity(pquantity);
            cart.setUid(uid);
            //远程调用，执行添加
            cartFeignService.addCart(cart);
        }


        //远程调用，添加商品到购物车
        return "cart/showcartList";
    }


    // 直接在购物车页面修改数量
    @PostMapping("/updateCartQuantity")
    @ResponseBody
    public Map<String, Object> updateCartQuantity(Integer cid, Integer pquantity) {
        Map<String, Object> map = new HashMap<>();
        Cart cart = new Cart();
        cart.setCid(cid);
        cart.setPquantity(pquantity);
        boolean result = cartFeignService.updateCartByCid(cart);
        if (result) {
            map.put("code", 0);
            map.put("msg", "数量修改成功");
        } else {
            map.put("code", 1);
            map.put("msg", "数量修改失败");
        }
        return map;
    }

}
