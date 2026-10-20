package com.baidu.controller;


import com.baidu.feignservice.LocationFeignService;
import com.baidu.pojo.Location;
import com.baidu.pojo.User;
import com.baidu.utils.AuthUtils;
import com.baidu.utils.PageResult;
import com.baidu.utils.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
@Controller
@RequestMapping("/location")
public class LocationController {
    @Autowired
    LocationFeignService locationFeignService;
    @Autowired
    private com.baidu.feignservice.ProductFeignService productFeignService;

    //执行pay里面的添加操作
    @PostMapping("/addAddress")
    public String addAddress(Location location, HttpSession session, HttpServletRequest request){
        System.out.println("-----------addAddress-------------location:"+location);
        User user1 = (User)session.getAttribute("user1");

        Map<String,Object> map = new HashMap<>();
        map.put("realName",location.getRealName());
        map.put("phone",location.getPhone());
        map.put("address",location.getAddress());
        map.put("identity",location.getIdentity());
        map.put("uid",user1.getUid());
        map.put("oid",location.getOid());
        boolean flag = locationFeignService.doAdd(map);
        
        // 组装list供pay2.html显示
        com.baidu.pojo.Order order = (com.baidu.pojo.Order) session.getAttribute("order");
        if(order != null && order.getPid() != null) {
            com.baidu.pojo.Product product = productFeignService.getProductById(order.getPid());
            order.setProduct(product);
            java.util.List<com.baidu.pojo.Order> list = new java.util.ArrayList<>();
            list.add(order);
            request.setAttribute("list", list);
        }
        
        return "user/pay2";
    }

    //执行添加操作
    @PostMapping("/doAdd")
    @ResponseBody
    public R doAdd(@RequestBody Map<String,Object> map,HttpSession session){
        System.out.println("-----------doAdd-------------map:"+map);
        User user1 = (User)session.getAttribute("user1");
        map.put("uid",user1.getUid());
        boolean flag = locationFeignService.doAdd(map);
        if (flag)
            return R.right();
        return R.error();
    }

    //根据id删除地址
    @RequestMapping("/doDel/{aid}")
    @ResponseBody
    public R doDel(@PathVariable("aid")Integer aid){
        System.out.println("--------doDel-------");
        System.out.println("aid:"+aid);
        boolean flag = locationFeignService.doDel(aid);
        if (flag) {
            return R.right();
        }
        return R.error();
    }

    //批量删除地址
    @PostMapping("/batchDel")
    @ResponseBody
    public R batchDel(@RequestParam("aids[]") Integer aids[]){
        System.out.println("--------batchDel--------");
        System.out.println("aids:"+aids);
        boolean flag = locationFeignService.batchDel(aids);
        if (flag)
            return R.right();
        return R.error();
    }

    //跳转到添加页面
    @GetMapping("/toAdd")
    public String toAdd(){
        return "location/add";
    }

    //跳转到修改页面
    @GetMapping("/toEdit/{aid}")
    public String toEdit(@PathVariable("aid") Integer aid, HttpServletRequest request) {
        System.out.println("------toEdit------aid:"+aid);
        Location location = locationFeignService.getLocationById(aid);
        request.setAttribute("editLoc", location);
        return "location/edit";
    }

    //执行修改操作
    @PostMapping("/doEdit")
    @ResponseBody
    public R doEdit(@RequestBody Location location) {
        System.out.println("------doEdit------location:"+location);
        boolean flag = locationFeignService.doEdit(location);
        if (flag) return R.right();
        return R.error();
    }

    //实现地址管理分页查询和模糊分页查询
    @PostMapping("/getPage")
    @ResponseBody
    public PageResult<Location> getPage(@RequestParam(defaultValue = "") String phone,
                                        @RequestParam(defaultValue = "1")Integer page,
                                        @RequestParam(defaultValue = "5")Integer limit,
                                        HttpSession session){
        System.out.println("-------------getPage--------------");
        if("".equals(phone)){
            phone=null;
        }
        System.out.println("phone:"+phone+" page:"+page+" limit:" + limit);


        Map<String,Object> map=new HashMap<>();
        map.put("phone",phone);
        map.put("page",page);
        map.put("limit",limit);
        //判断是否为管理员
        if(AuthUtils.isAdmin(session)){
            PageResult<Location> pageResult = locationFeignService.getPage(map);
            return pageResult;
        }else {
            User user1 = (User) session.getAttribute("user1");
            map.put("uid",user1.getUid());
            PageResult<Location> pageResult = locationFeignService.getPage(map);
            return pageResult;
        }



    }


    //跳转到客户地址管理页面
    @GetMapping("/toList")
    public String  toList(){
        return "location/list";
    }
}
