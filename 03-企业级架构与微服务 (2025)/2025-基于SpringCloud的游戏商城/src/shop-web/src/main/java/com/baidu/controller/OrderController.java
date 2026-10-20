package com.baidu.controller;


import com.baidu.feignservice.CartFeignService;
import com.baidu.feignservice.OrderFeignService;
import com.baidu.feignservice.ProductFeignService;
import com.baidu.feignservice.UserFeignService;
import com.baidu.pojo.Cart;
import com.baidu.pojo.Order;
import com.baidu.pojo.Product;
import com.baidu.pojo.User;
import com.baidu.utils.AuthUtils;
import com.baidu.utils.PageResult;
import com.baidu.utils.R;
import jakarta.servlet.http.HttpSession;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderFeignService orderFeignService;
    @Autowired
    UserFeignService userFeignService;
    @Autowired
    CartFeignService cartFeignService;
    @Autowired
    ProductFeignService productFeignService;


    //添加订单 结算购物车
    @PostMapping("/addOrder")
    @ResponseBody
    public R addOrder(@RequestParam("totalPrice")BigDecimal totalPrice,@RequestParam("cids[]")Integer cids[],HttpSession session){
        System.out.println("---------addOrder-------");
        System.out.println("totalPrice:"+totalPrice);
        System.out.println("cids:"+cids);
        if(totalPrice==null&&cids==null){
            return R.error("请选中商品");
        }

        List<Cart> cartByCids = cartFeignService.getCartByCids(cids);
        List<Order> orderList = cartByCids.stream()
                .map(cart -> {
                    Order order = new Order();
                    Product product = productFeignService.getProductById(cart.getPid());
                    order.setUid(cart.getUid());          //订单绑定的对应的用户
                    order.setTotalPrice(totalPrice);      //设置总金额
                    order.setCreatetime(new Date());      //获取当前时间
                    order.setMerchantId(product.getUid());//设置卖家id
                    order.setStatus("未支付");             //设置支付状态
                    order.setGoodsStatus("未发货");        //设置发货状态
                    order.setPid(cart.getPid());          //设置商品ID
                    order.setQuantity(cart.getPquantity() != null ? cart.getPquantity() : 1); //设置数量
                    return order;
                }).collect(Collectors.toList());
        System.out.println("orderList:"+orderList);

        //提取出购物车所有购买的商品ID
        List<Integer> pids = cartByCids.stream()
                .map(Cart::getPid)
                .collect(Collectors.toList());
        session.setAttribute("buyPids", pids);
        session.setAttribute("buyCids", java.util.Arrays.asList(cids));

        //添加订单到数据库
        Order order = orderFeignService.addOrder(orderList.get(0));
        session.setAttribute("order",order);
        if (order!=null){
            return R.right();
        }
        return R.error();
    }

    //直接购买
    @RequestMapping("/buyDirect")
    public String buyDirect(@RequestParam("pid") Integer pid, HttpSession session) {
        System.out.println("---------buyDirect-------");
        System.out.println("pid:" + pid);

        User user = (User) session.getAttribute("user1");
        Product product = productFeignService.getProductById(pid);
        Order order = new Order();
        order.setUid(user.getUid());                  //订单绑定的对应的用户
        order.setTotalPrice(product.getPrice());      //设置总金额为单价(默认数量1)
        order.setCreatetime(new Date());              //获取当前时间
        order.setMerchantId(product.getUid());        //设置卖家id
        order.setStatus("未支付");                     //设置支付状态
        order.setGoodsStatus("未发货");                //设置发货状态
        order.setPid(pid);                            //设置商品ID
        order.setQuantity(1);                         //设置数量
        // 存入单独购买的 pid
        session.setAttribute("buyPids", java.util.Arrays.asList(pid));

        //添加订单到数据库
        Order savedOrder = orderFeignService.addOrder(order);
        session.setAttribute("order", savedOrder);

        // 直接重定向到支付页面，跟购物车结算后的走向一模一样！
        return "redirect:/user/toPay";
    }

    //订单发货
    @PostMapping("/editOrder/{oid}")
    @ResponseBody
    public R editOrder(@PathVariable("oid") Integer oid){
        System.out.println("-----------editOrder---------oid:"+oid);
        boolean flag = orderFeignService.editOrder(oid);
        if (flag) {
            // 发货成功后，扣减卖家库存，并把物品发放到买家背包
            List<Order> orders = orderFeignService.getOrderByOids(new Integer[]{oid});
            if (orders != null && !orders.isEmpty()) {
                Order order = orders.get(0);
                if (order.getPid() != null) {
                    productFeignService.buyProduct(order.getPid(), order.getUid(), order.getQuantity() != null ? order.getQuantity() : 1);
                }
            }
            return R.right();
        }
        return R.error();
    }

    //确认收货
    @PostMapping("/receiveOrder/{oid}")
    @ResponseBody
    public R receiveOrder(@PathVariable("oid") Integer oid){
        System.out.println("-----------receiveOrder---------oid:"+oid);
        boolean flag = orderFeignService.receiveOrder(oid);
        if (flag)
            return R.right();
        return R.error();
    }

    //批量删除订单
    @PostMapping("/batchDelOrder")
    @ResponseBody
    public R batchDelOrder(@RequestParam("oids[]") Integer oids[]){
        System.out.println("--------batchDelOrder-------");
        boolean flag = orderFeignService.batchDelOrder(oids);
        if (flag)
            return R.right();
        return R.error();
    }

    //通过id删除订单
    @PostMapping("/deleteOrderByOid/{oid}")
    @ResponseBody
    public R deleteOrderByOid(@PathVariable("oid") Integer oid){
        System.out.println("-------deleteOrderByOid-------");
        boolean flag = orderFeignService.deleteOrderByOid(oid);
        if (flag)
            return R.right();
        return R.error();
    }

    //渲染订单管理页面
    @PostMapping("/orderList")
    @ResponseBody
    public PageResult<Order> orderList(@RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "5") Integer limit,
                                       HttpSession session)
    {
        System.out.println("-----orderList----");
        System.out.println("page:"+page+" limit:"+limit);
        Map<String,Object> map = new HashMap<>();
        map.put("currentPage",page);
        map.put("limit",limit);
        System.out.println("map:"+map);
        if(AuthUtils.isAdmin(session)){//判断是否为管理员
            //是管理员则查询所有
            PageResult<Order> pageResult = orderFeignService.getAllOrder(map);
            return pageResult;
        }else {
            //不是则根据用户id查询订单
            User user = (User) session.getAttribute("user1");
            map.put("uid",user.getUid());
            PageResult<Order> pageResult = orderFeignService.getOrderByUid(map);
            return pageResult;
        }


    }

    //跳转到订单管理页面
    @RequestMapping("/toOrderList")
    public String toOrderList(){
        System.out.println("------toOrderList-------");
        return "order/list";
    }
}
