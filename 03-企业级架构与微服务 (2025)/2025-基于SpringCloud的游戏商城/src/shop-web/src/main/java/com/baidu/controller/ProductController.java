package com.baidu.controller;


import com.baidu.feignservice.ProductFeignService;
import com.baidu.feignservice.CollectFeignService;
import com.baidu.feignservice.CommentFeignService;
import com.baidu.pojo.Collect;
import com.baidu.pojo.Comment;
import com.baidu.pojo.Product;
import com.baidu.pojo.User;
import com.baidu.utils.AuthUtils;
import com.baidu.utils.PageResult;
import com.baidu.utils.R;
import com.baidu.utils.TransFerPlace;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
@Controller
@RequestMapping("/prod")
public class ProductController {
    @Autowired
    private ProductFeignService productFeignService;
    
    @Autowired
    private CollectFeignService collectFeignService;
    
    @Autowired
    private CommentFeignService commentFeignService;
    
    @Autowired
    private com.baidu.feignservice.SystemConfigFeignService systemConfigFeignService;





    //批量删除产品
    @PostMapping("/batchDel")
    @ResponseBody
    public R batchDel(@RequestParam("prodIds[]") Integer pids[]){
        System.out.println("--------batchDel--------");
        System.out.println("pids"+pids);
        boolean flag = productFeignService.batchDel(pids);
        if (flag)
            return R.right();
        return R.error();
    }

    //执行修改
    @PostMapping("/doEdit")
    @ResponseBody
    public R doEdit(Product product){
        System.out.println("--------product--------");
        System.out.println("product:"+product);
        boolean flag = productFeignService.doEdit(product);
        if (flag){
            return R.right();
        }
        return R.error();
    }


    //跳转到编辑页面
    @GetMapping("/toEdit/{pid}")
    public String toEdit(@PathVariable("pid") Integer pid,HttpServletRequest request){
        System.out.println("---------toEdit--------");
        System.out.println("pid:"+pid);
        Product product = productFeignService.getProductById(pid);
        request.setAttribute("editProd",product);
        return "prod/edit";
    }


    //添加产品
    @PostMapping("/doAdd")
    @ResponseBody
    public R doAdd(Product product,HttpSession session){
        System.out.println("----------doAdd------------");
        System.out.println("product:"+product);
        User user = (User)session.getAttribute("user1");
        product.setUid(user.getUid());
        boolean flag = productFeignService.doAdd(product);
        if (flag)
            return R.right();
        //远程调用openfeign接口
        return R.error();
    }

    //跳转到添加页面
    @GetMapping("/toAdd")
    public String toAdd(){
        System.out.println("---------toAdd--------");
        return "prod/add";
    }

    //根据pid删除产品
    @PostMapping("/doDel/{pid}")
    @ResponseBody
    public R doDel(@PathVariable("pid")Integer pid){
        System.out.println("--------doDel-------");
        System.out.println("pid:"+pid);
        boolean flag = productFeignService.doDel(pid);
        if(flag){
            return R.right();
        }
        return R.error();
    }

    //实现普通分页查询与模糊分页查询
    @PostMapping("/getPage")
    @ResponseBody
    public PageResult<Product> getPage(@RequestParam(defaultValue = "${user.list.default-pname:}")String pname,
                                       @RequestParam(defaultValue = "${user.list.default-page:1}")Integer page,
                                       @RequestParam(defaultValue = "${user.list.default-limit:5}")Integer limit,
                                        HttpSession session){
        System.out.println("-----------getPage----------");
        Map<String,Object> map = new HashMap<>();
        if(pname==""){
            pname=null;
        }
        map.put("pname",pname);
        map.put("currentPage",page);
        map.put("pageSize",limit);

        if(AuthUtils.isAdmin(session)){//判断是否为管理员
            PageResult<Product> productByPage = productFeignService.getProductPageByPname(map);
            System.out.println(productByPage.getData());
            return productByPage;
        }else {
            User user = (User) session.getAttribute("user1");
            map.put("uid", user.getUid());
            PageResult<Product> productByPage = productFeignService.getProductPageByPnameAndUid(map);
            System.out.println(productByPage.getData());
            return productByPage;
        }


    }

    //进入产品管理页面
    @RequestMapping("/toList")
    public String toList(){
        System.out.println("--------toList--------");
        return "prod/list";
    }

    //处理进入商城首页的请求
    @RequestMapping("/index.html")
    public String toIndex(@RequestParam(defaultValue = "1")Integer currentPage, HttpServletRequest request, HttpSession session){
        System.out.println("---------index----------");
        System.out.println("currentPage:"+currentPage);
        //创建map<String,Object>用于保存当前页面和页面容量
        Map<String,Object> map = new HashMap<>();
        //保存当前页面
        map.put("currentPage",currentPage);
        //保存页面容量
        map.put("pageSize",6);

        //执行分页查询，页面容量固定为6
        PageResult<Product> pageResult = productFeignService.getProductByPage(map);

        //设置当前页面
        request.setAttribute("pages",currentPage);
        //获取总记录数
        Integer count = pageResult.getCount();
        request.setAttribute("totalCount",count);

        //获取查询的商品集合
        List<Product> list = pageResult.getData();
        
        // 获取当前用户以便查询是否收藏
        User user1 = (User) session.getAttribute("user1");
        
        if (list != null) {
            for (Product p : list) {
                p.setCollectCount(collectFeignService.getCollectCount(p.getPid()));
                if (user1 != null) {
                    Collect collect = new Collect();
                    collect.setUid(user1.getUid());
                    collect.setPid(p.getPid());
                    p.setIsCollected(collectFeignService.isCollected(collect));
                }
            }
        }
        
        // 获取并放入系统配置（包含商城名称和轮播图）
        com.baidu.pojo.SystemConfig sysConfig = systemConfigFeignService.getConfig();
        request.setAttribute("sysConfig", sysConfig);

        //将商品的结合保存到request中
        request.setAttribute("list",list);

        return "index";
    }

    @RequestMapping("/getProdById/{id}")
    public String getProdById(@PathVariable("id") Integer id,HttpServletRequest request){
        System.out.println("----------getProdById---------");
        System.out.println("id:"+id);
        //远程调用该，根据id查询指定信息
        Product product = productFeignService.getProductById(id);
        
        // 远程调用获取评论列表
        List<Comment> comments = commentFeignService.getComments(id);

        //将商品信息保存到request中
        request.setAttribute("product",product);
        request.setAttribute("comments", comments);
        return "prod/info";
    }

    // ========== 我的背包模块 ==========



    // 获取背包数据
    @PostMapping("/getMyBagData")
    @ResponseBody
    public PageResult<Product> getMyBagData(@RequestParam(defaultValue = "${user.list.default-page:1}") Integer page,
                                            @RequestParam(defaultValue = "${user.list.default-limit:5}") Integer limit,
                                            HttpSession session) {
        System.out.println("-----------getMyBagData----------");
        Map<String, Object> map = new HashMap<>();
        map.put("currentPage", page);
        map.put("pageSize", limit);
        User user = (User) session.getAttribute("user1");
        if (user != null) {
            map.put("uid", user.getUid());
        } else {
            // 没有登录则返回空或者报错
            PageResult<Product> emptyResult = new PageResult<>();
            emptyResult.setCode(1);
            emptyResult.setMsg("用户未登录");
            return emptyResult;
        }

        PageResult<Product> bagPage = productFeignService.getMyBagPage(map);
        return bagPage;
    }

    // 管理员审核商品
    @PostMapping("/auditProduct/{pid}")
    @ResponseBody
    public R auditProduct(@PathVariable("pid") Integer pid, @RequestParam("auditStatus") String auditStatus) {
        System.out.println("-------auditProduct------- pid:" + pid + " status:" + auditStatus);
        boolean flag = productFeignService.auditProduct(pid, auditStatus);
        if (flag) {
            return R.right();
        }
        return R.error();
    }

    @PostMapping("/sellProduct")
    @ResponseBody
    public R sellProduct(@RequestParam("pid") Integer pid, 
                         @RequestParam("sellPrice") BigDecimal sellPrice,
                         @RequestParam("sellQuantity") Integer sellQuantity, 
                         HttpSession session) {
        User user = (User) session.getAttribute("user1");
        if (user == null) {
            return R.error("请先登录");
        }
        boolean flag = productFeignService.sellProduct(pid, sellPrice, sellQuantity, user.getUid());
        if (flag) {
            return R.right();
        }
        return R.error("挂售失败");
    }

    @PostMapping("/cancelSell")
    @ResponseBody
    public R cancelSell(@RequestParam("pid") Integer pid, HttpSession session) {
        User user = (User) session.getAttribute("user1");
        if (user == null) {
            return R.error("请先登录");
        }
        boolean flag = productFeignService.cancelSell(pid, user.getUid());
        if (flag) {
            return R.right();
        }
        return R.error("下架失败");
    }

    @PostMapping("/mergeBag")
    @ResponseBody
    public R mergeBag(HttpSession session) {
        User user = (User) session.getAttribute("user1");
        if (user == null) {
            return R.error("请先登录");
        }
        boolean flag = productFeignService.mergeBag(user.getUid());
        if (flag) {
            return R.right();
        }
        return R.error("合并失败");
    }

    @RequestMapping("/toMyBagFront")
    public String toMyBagFront() {
        return "prod/mybag_front";
    }

    @RequestMapping("/toMyListedFront")
    public String toMyListedFront() {
        return "prod/mylist_front";
    }
}
