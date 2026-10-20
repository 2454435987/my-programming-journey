package com.tea.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.common.PageResult;
import com.tea.common.Result;
import com.tea.entity.Banner;
import com.tea.entity.MessageNotice;
import com.tea.mapper.BannerMapper;
import com.tea.mapper.MessageNoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 运营管理：Banner、消息通知
 */
@RestController
@RequestMapping("/api/admin")
public class AdminOperationController {

    @Autowired private BannerMapper bannerMapper;
    @Autowired private MessageNoticeMapper messageMapper;

    /** Banner列表 */
    @GetMapping("/banners")
    public Result<PageResult<Banner>> bannerList(
            @RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size) {
        Page<Banner> p = bannerMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<Banner>().orderByAsc(Banner::getSortOrder));
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    @PostMapping("/banners")
    public Result<Banner> addBanner(@RequestBody Banner body) {
        body.setBannerId(null);
        body.setCreatedAt(LocalDateTime.now());
        bannerMapper.insert(body);
        return Result.ok(body);
    }

    @PutMapping("/banners/{id}")
    public Result<Void> updateBanner(@PathVariable Long id, @RequestBody Banner body) {
        body.setBannerId(id);
        bannerMapper.updateById(body);
        return Result.ok();
    }

    @DeleteMapping("/banners/{id}")
    public Result<Void> deleteBanner(@PathVariable Long id) {
        bannerMapper.deleteById(id);
        return Result.ok();
    }

    /** 消息列表 */
    @GetMapping("/messages")
    public Result<PageResult<MessageNotice>> messageList(
            @RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<MessageNotice> qw = new LambdaQueryWrapper<MessageNotice>()
                .like(keyword != null && !keyword.isEmpty(), MessageNotice::getTitle, keyword)
                .orderByDesc(MessageNotice::getCreatedAt);
        Page<MessageNotice> p = messageMapper.selectPage(new Page<>(page, size), qw);
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    /** 发送消息（userId=0 表示广播） */
    @PostMapping("/messages")
    public Result<MessageNotice> sendMessage(@RequestBody MessageNotice body) {
        body.setMessageId(null);
        body.setIsRead(0);
        body.setCreatedAt(LocalDateTime.now());
        messageMapper.insert(body);
        return Result.ok(body);
    }

    @DeleteMapping("/messages/{id}")
    public Result<Void> deleteMessage(@PathVariable Long id) {
        messageMapper.deleteById(id);
        return Result.ok();
    }
}
