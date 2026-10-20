package com.baidu.controller;

import com.baidu.pojo.Product;
import com.baidu.service.impl.ProductServiceImpl;
import com.baidu.utils.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductServiceImpl productService;

    //删除多个商品
    @PostMapping("/batchDel")
    public boolean batchDel(@RequestBody Integer pids[]){
        System.out.println("----------batchDel---------");
        System.out.println("pids:"+pids);
        boolean flag = productService.removeBatchByIds(List.of(pids));
        return flag;
    }

    //更新商品
    @PostMapping("/doEdit")
    public boolean doEdit(@RequestBody Product product){
        System.out.println("-------doEdit------");
        System.out.println("product:"+product);
        boolean flag = productService.updateById(product);
        return flag;
    }

    //添加商品
    @PostMapping("/doAdd")
    public boolean doAdd(@RequestBody Product product){
        System.out.println("-------product-------");
        System.out.println("product:"+product);
        product.setAuditStatus("待审核");
        boolean flag = productService.save(product);
        return flag;
    }

    //根据id删除产品
    @RequestMapping("/doDel/{pid}")
    public boolean doDel(@PathVariable("pid")Integer pid){
        System.out.println("--------doDel-------");
        System.out.println("pid:"+pid);
        boolean flag = productService.removeById(pid);
        return flag;
    }

    //执行分页查询与模糊查询
    @PostMapping("/getProductPageByPname")
    public PageResult<Product> getProductPageByPname(@RequestBody Map<String,Object> map){
        System.out.println("----------getProductPageByPname-----------");
        System.out.println("map:"+map);
        String pname = (String) map.get("pname");
        int currentPage = (int) map.get("currentPage");
        int pageSize = (int) map.get("pageSize");
        PageResult<Product> pageResult = productService.getProductPageByPname(pname, currentPage, pageSize);
        return pageResult;
    }
    @PostMapping("/getProductPageByPnameAndUid")
    public PageResult<Product> getProductPageByPnameAndUid(@RequestBody Map<String,Object> map){
        System.out.println("-----------getProductPageByPnameAndUid------------");
        System.out.println("map:"+map);
        String pname = (String) map.get("pname");
        int currentPage = (int) map.get("currentPage");
        int pageSize = (int) map.get("pageSize");
        int uid = (int) map.get("uid");
        PageResult<Product> pageResult = productService.getProductPageByPnameAndUid(pname, currentPage, pageSize, uid);
        return pageResult;
    }

    //分页查询商品信息
    //map中需要保存当前页和页面容量
    @PostMapping("/getProductByPage")
    public PageResult<Product> getProductByPage(@RequestBody Map<String,Object> map){
        System.out.println("--------------getProductByPage--------------");
        System.out.println("map:"+map);
        //获取当前页
        Integer currentPage = (Integer) map.get("currentPage");
        //获取当前页
        Integer pageSize =(Integer) map.get("pageSize");
        PageResult<Product> pageResult = productService.getProductBypage(currentPage,pageSize);
        return pageResult;
    }

    @RequestMapping("/getProductById/{pid}")
    public  Product getProductById(@PathVariable("pid")Integer pid){
        System.out.println("----------getProductById--------------");
        System.out.println("pid:"+pid);
        Product product = productService.getById(pid);
        return product;
    }

    // P2P交易：扣减卖家库存，并把商品放入买家背包
    @PostMapping("/buyProduct/{pid}/{buyerUid}")
    public boolean buyProduct(@PathVariable("pid") Integer pid, @PathVariable("buyerUid") Integer buyerUid, @org.springframework.web.bind.annotation.RequestParam(value="quantity", defaultValue="1") Integer quantity) {
        System.out.println("-------buyProduct------- pid:" + pid + " buyerUid:" + buyerUid + " quantity:" + quantity);
        Product product = productService.getById(pid);
        if (product == null || product.getStock() < quantity) {
            return false;
        }
        
        // 1. 卖家库存减 quantity
        product.setStock(product.getStock() - quantity);
        product.setSales(product.getSales() + quantity); // 顺便加个销量
        if (product.getStock() <= 0) {
            product.setStatus(2); // 2表示已售空/历史快照，不再显示在挂售和背包中，但保留数据供订单查询
        }
        productService.updateById(product);
        
        // 2. 为买家克隆一件进背包，数量为 quantity
        Product buyerProduct = new Product();
        buyerProduct.setPname(product.getPname());
        buyerProduct.setPrice(product.getPrice());
        buyerProduct.setPfile(product.getPfile());
        buyerProduct.setDescription(product.getDescription());
        buyerProduct.setUid(buyerUid); // 归属人变成买家！
        buyerProduct.setStock(quantity);
        buyerProduct.setStatus(0); // 状态0代表下架(放在背包里)
        buyerProduct.setCreateTime(new Date());
        buyerProduct.setSales(0);
        
        return productService.save(buyerProduct);
    }

    // 玩家挂售商品
    @PostMapping("/sellProduct")
    public boolean sellProduct(@RequestParam("pid") Integer pid, 
                               @RequestParam("sellPrice") java.math.BigDecimal sellPrice, 
                               @RequestParam("sellQuantity") Integer sellQuantity, 
                               @RequestParam("uid") Integer uid) {
        System.out.println("-------sellProduct------- pid:" + pid + " uid:" + uid);
        Product product = productService.getById(pid);
        if (product == null || !product.getUid().equals(uid) || product.getStock() < sellQuantity || product.getStatus() != 0) {
            return false;
        }

        if (product.getStock().equals(sellQuantity)) {
            // 全卖了，直接改状态和价格
            product.setPrice(sellPrice);
            product.setStatus(1); // 1 = 上架
            product.setAuditStatus("待审核");
            return productService.updateById(product);
        } else {
            // 卖一部分，拆分
            product.setStock(product.getStock() - sellQuantity);
            productService.updateById(product);

            Product sellProduct = new Product();
            sellProduct.setPname(product.getPname());
            sellProduct.setPrice(sellPrice);
            sellProduct.setPfile(product.getPfile());
            sellProduct.setDescription(product.getDescription());
            sellProduct.setUid(uid);
            sellProduct.setStock(sellQuantity);
            sellProduct.setStatus(1);
            sellProduct.setAuditStatus("待审核");
            sellProduct.setCreateTime(new Date());
            sellProduct.setSales(0);
            return productService.save(sellProduct);
        }
    }

    // 玩家下架商品（退回背包）
    @PostMapping("/cancelSell")
    public boolean cancelSell(@RequestParam("pid") Integer pid, @RequestParam("uid") Integer uid) {
        System.out.println("-------cancelSell------- pid:" + pid + " uid:" + uid);
        Product product = productService.getById(pid);
        // 只能下架自己的、并且状态为1（已挂售）的商品
        if (product == null || !product.getUid().equals(uid) || product.getStatus() != 1) {
            return false;
        }
        product.setStatus(0); // 0 = 放入背包
        return productService.updateById(product);
    }

    // 查询我的背包（只查 status = 0 的商品）
    @PostMapping("/getMyBagPage")
    public PageResult<Product> getMyBagPage(@RequestBody Map<String,Object> map){
        System.out.println("-----------getMyBagPage------------");
        int currentPage = (int) map.get("currentPage");
        int pageSize = (int) map.get("pageSize");
        int uid = (int) map.get("uid");
        
        Page<Product> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        queryWrapper.eq("status", 0);
        
        com.baomidou.mybatisplus.core.metadata.IPage<Product> iPage = productService.page(page, queryWrapper);
        
        PageResult<Product> pageResult = new PageResult<>();
        pageResult.setCode(0);
        pageResult.setMsg("查询背包成功");
        pageResult.setCount((int)iPage.getTotal());
        pageResult.setData(iPage.getRecords());
        return pageResult;
    }

    // 审核商品
    @PostMapping("/auditProduct/{pid}")
    public boolean auditProduct(@PathVariable("pid") Integer pid, @RequestParam("auditStatus") String auditStatus) {
        System.out.println("-------auditProduct------- pid:" + pid + " auditStatus:" + auditStatus);
        Product product = productService.getById(pid);
        if (product == null) {
            return false;
        }
        product.setAuditStatus(auditStatus);
        return productService.updateById(product);
    }

    // 一键整理背包（合成同名商品）
    @PostMapping("/mergeBag")
    public boolean mergeBag(@RequestParam("uid") Integer uid) {
        System.out.println("-------mergeBag------- uid:" + uid);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        queryWrapper.eq("status", 0);
        List<Product> bagList = productService.list(queryWrapper);

        // Group by pname
        Map<String, List<Product>> grouped = bagList.stream().collect(Collectors.groupingBy(Product::getPname));

        for (Map.Entry<String, List<Product>> entry : grouped.entrySet()) {
            List<Product> items = entry.getValue();
            if (items.size() > 1) {
                // Merge into the first item
                Product mainItem = items.get(0);
                int totalStock = items.stream().mapToInt(Product::getStock).sum();
                mainItem.setStock(totalStock);
                productService.updateById(mainItem);

                // Delete the rest
                List<Integer> idsToDelete = items.stream().skip(1).map(Product::getPid).collect(Collectors.toList());
                productService.removeBatchByIds(idsToDelete);
            }
        }
        return true;
    }
}
