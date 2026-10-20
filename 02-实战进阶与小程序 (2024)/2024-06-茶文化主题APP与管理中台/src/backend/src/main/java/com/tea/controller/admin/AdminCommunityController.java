package com.tea.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.common.PageResult;
import com.tea.common.Result;
import com.tea.entity.CommunityPost;
import com.tea.entity.PostComment;
import com.tea.mapper.CommunityPostMapper;
import com.tea.mapper.PostCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 社区管理：帖子审核、评论管理
 */
@RestController
@RequestMapping("/api/admin")
public class AdminCommunityController {

    @Autowired private CommunityPostMapper postMapper;
    @Autowired private PostCommentMapper commentMapper;

    /** 帖子分页 */
    @GetMapping("/posts")
    public Result<PageResult<CommunityPost>> postList(
            @RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String keyword, @RequestParam(required = false) Integer status) {
        LambdaQueryWrapper<CommunityPost> qw = new LambdaQueryWrapper<CommunityPost>()
                .and(keyword != null && !keyword.isEmpty(), w ->
                        w.like(CommunityPost::getTitle, keyword).or().like(CommunityPost::getContent, keyword))
                .eq(status != null, CommunityPost::getStatus, status)
                .orderByDesc(CommunityPost::getCreatedAt);
        Page<CommunityPost> p = postMapper.selectPage(new Page<>(page, size), qw);
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    /** 审核帖子（置顶/精华/状态） */
    @PutMapping("/posts/{id}/audit")
    public Result<Void> auditPost(@PathVariable Long id, @RequestBody CommunityPost body) {
        CommunityPost post = postMapper.selectById(id);
        if (post == null) return Result.fail("帖子不存在");
        if (body.getStatus() != null) post.setStatus(body.getStatus());
        if (body.getIsTop() != null) post.setIsTop(body.getIsTop());
        if (body.getIsEssence() != null) post.setIsEssence(body.getIsEssence());
        post.setUpdatedAt(LocalDateTime.now());
        postMapper.updateById(post);
        return Result.ok();
    }

    /** 删除帖子 */
    @DeleteMapping("/posts/{id}")
    public Result<Void> deletePost(@PathVariable Long id) {
        postMapper.deleteById(id);
        return Result.ok();
    }

    /** 评论分页 */
    @GetMapping("/comments")
    public Result<PageResult<PostComment>> commentList(
            @RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<PostComment> qw = new LambdaQueryWrapper<PostComment>()
                .like(keyword != null && !keyword.isEmpty(), PostComment::getContent, keyword)
                .orderByDesc(PostComment::getCreatedAt);
        Page<PostComment> p = commentMapper.selectPage(new Page<>(page, size), qw);
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    /** 删除评论 */
    @DeleteMapping("/comments/{id}")
    public Result<Void> deleteComment(@PathVariable Long id) {
        commentMapper.deleteById(id);
        return Result.ok();
    }
}
