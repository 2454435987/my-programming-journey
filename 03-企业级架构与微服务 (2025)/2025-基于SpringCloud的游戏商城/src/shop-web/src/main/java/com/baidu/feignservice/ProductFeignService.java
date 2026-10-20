package com.baidu.feignservice;

import com.baidu.pojo.Product;
import com.baidu.utils.PageResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(value = "shop-service",path = "/product")
public interface ProductFeignService {



    @PostMapping("/getProductPageByPnameAndUid")
    public PageResult<Product> getProductPageByPnameAndUid(@RequestBody Map<String,Object> map);

    //删除多个商品
    @PostMapping("/batchDel")
    public boolean batchDel(Integer pids[]);

    //更新商品
    @PostMapping("/doEdit")
    public boolean doEdit(@RequestBody Product product);

    //添加商品
    @PostMapping("/doAdd")
    public boolean doAdd(@RequestBody Product product);

    @RequestMapping("/doDel/{pid}")
    public boolean doDel(@PathVariable("pid")Integer pid);;
    //执行分页查询与模糊查询
    @PostMapping("/getProductPageByPname")
    public PageResult<Product> getProductPageByPname(@RequestBody Map<String,Object> map);

    @PostMapping("/getProductByPage")
    public PageResult<Product> getProductByPage(@RequestBody Map<String,Object> map);

    @RequestMapping("/getProductById/{pid}")
    public  Product getProductById(@PathVariable("pid")Integer pid);

    @PostMapping("/buyProduct/{pid}/{buyerUid}")
    public boolean buyProduct(@PathVariable("pid") Integer pid, @PathVariable("buyerUid") Integer buyerUid, @org.springframework.web.bind.annotation.RequestParam(value="quantity", defaultValue="1") Integer quantity);

    @PostMapping("/sellProduct")
    public boolean sellProduct(@org.springframework.web.bind.annotation.RequestParam("pid") Integer pid, 
                               @org.springframework.web.bind.annotation.RequestParam("sellPrice") java.math.BigDecimal sellPrice, 
                               @org.springframework.web.bind.annotation.RequestParam("sellQuantity") Integer sellQuantity, 
                               @org.springframework.web.bind.annotation.RequestParam("uid") Integer uid);

    @PostMapping("/cancelSell")
    public boolean cancelSell(@org.springframework.web.bind.annotation.RequestParam("pid") Integer pid,
                              @org.springframework.web.bind.annotation.RequestParam("uid") Integer uid);

    @PostMapping("/mergeBag")
    public boolean mergeBag(@org.springframework.web.bind.annotation.RequestParam("uid") Integer uid);

    @PostMapping("/getMyBagPage")
    public PageResult<Product> getMyBagPage(@RequestBody Map<String,Object> map);

    @PostMapping("/auditProduct/{pid}")
    public boolean auditProduct(@PathVariable("pid") Integer pid, @org.springframework.web.bind.annotation.RequestParam("auditStatus") String auditStatus);
}
