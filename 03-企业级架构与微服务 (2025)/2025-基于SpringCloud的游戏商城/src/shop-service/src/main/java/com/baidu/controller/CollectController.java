package com.baidu.controller;

import com.baidu.pojo.Collect;
import com.baidu.service.CollectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baidu.pojo.Product;
import com.baidu.service.ProductService;
import com.baidu.utils.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;

@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private CollectService collectService;

    @Autowired
    private ProductService productService;

    // 切换收藏状态
    @PostMapping("/toggle")
    public boolean toggleCollect(@RequestBody Collect collect) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", collect.getUid());
        wrapper.eq("pid", collect.getPid());
        Collect exist = collectService.getOne(wrapper);
        if (exist != null) {
            collectService.removeById(exist.getCid());
            return false; // 取消收藏
        } else {
            collectService.save(collect);
            return true; // 收藏成功
        }
    }

    // 获取某个商品的收藏数量
    @GetMapping("/count/{pid}")
    public long getCollectCount(@PathVariable("pid") Integer pid) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.eq("pid", pid);
        return collectService.count(wrapper);
    }
    
    // 查询某用户是否收藏了某商品
    @PostMapping("/isCollected")
    public boolean isCollected(@RequestBody Collect collect) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", collect.getUid());
        wrapper.eq("pid", collect.getPid());
        return collectService.count(wrapper) > 0;
    }

    //分页查询收藏记录
    @PostMapping("/getCollectPageByUid")
    public PageResult<Collect> getCollectPageByUid(@RequestBody Map<String, Object> map) {
        Integer currentPage = (Integer) map.get("currentPage");
        Integer limit = (Integer) map.get("limit");
        Integer uid = (Integer) map.get("uid");

        Page<Collect> page = new Page<>(currentPage, limit);
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid);
        Page<Collect> pageResult = collectService.page(page, wrapper);

        for (Collect collect : pageResult.getRecords()) {
            Product product = productService.getById(collect.getPid());
            collect.setProduct(product);
        }

        PageResult<Collect> result = new PageResult<>();
        result.setCount((int) pageResult.getTotal());
        result.setCode(0);
        result.setMsg("查询成功");
        result.setData(pageResult.getRecords());
        return result;
    }
}
