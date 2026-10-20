package com.tea.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.common.BizException;
import com.tea.common.PageResult;
import com.tea.common.Result;
import com.tea.entity.*;
import com.tea.mapper.*;
import com.tea.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * App用户端接口：个人中心、发帖评论、收藏点赞、购物车、地址、消息、报名
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired private AppUserMapper userMapper;
    @Autowired private CommunityPostMapper postMapper;
    @Autowired private PostCommentMapper commentMapper;
    @Autowired private UserFavoriteMapper favoriteMapper;
    @Autowired private ContentLikeMapper likeMapper;
    @Autowired private CartItemMapper cartMapper;
    @Autowired private TeaProductMapper productMapper;
    @Autowired private UserAddressMapper addressMapper;
    @Autowired private MessageNoticeMapper messageMapper;
    @Autowired private ActivityEnrollMapper enrollMapper;
    @Autowired private TeaActivityMapper activityMapper;
    @Autowired private OrdersMapper ordersMapper;
    @Autowired private OrderService orderService;

    private Long uid(@RequestAttribute("userId") Long userId) { return userId; }

    /** 个人资料 */
    @GetMapping("/profile")
    public Result<AppUser> profile(@RequestAttribute("userId") Long userId) {
        AppUser u = userMapper.selectById(userId);
        if (u != null) u.setPassword(null);
        return Result.ok(u);
    }

    /** 修改个人资料 */
    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestAttribute("userId") Long userId, @RequestBody AppUser body) {
        AppUser u = userMapper.selectById(userId);
        if (u == null) throw new BizException(404, "用户不存在");
        if (body.getNickname() != null) u.setNickname(body.getNickname());
        if (body.getAvatar() != null) u.setAvatar(body.getAvatar());
        if (body.getGender() != null) u.setGender(body.getGender());
        if (body.getPhone() != null) u.setPhone(body.getPhone());
        if (body.getEmail() != null) u.setEmail(body.getEmail());
        if (body.getSignature() != null) u.setSignature(body.getSignature());
        u.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(u);
        return Result.ok();
    }

    /** 我的帖子 */
    @GetMapping("/posts")
    public Result<PageResult<CommunityPost>> myPosts(@RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size) {
        Page<CommunityPost> p = postMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<CommunityPost>().eq(CommunityPost::getUserId, userId)
                        .orderByDesc(CommunityPost::getCreatedAt));
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    /** 发帖 */
    @PostMapping("/posts")
    public Result<CommunityPost> createPost(@RequestAttribute("userId") Long userId, @RequestBody CommunityPost body) {
        body.setPostId(null);
        body.setUserId(userId);
        body.setViewCount(0); body.setLikeCount(0); body.setCommentCount(0);
        body.setStatus(1); body.setCreatedAt(LocalDateTime.now());
        postMapper.insert(body);
        return Result.ok(body);
    }

    /** 帖子评论列表 */
    @GetMapping("/posts/{postId}/comments")
    public Result<List<PostComment>> comments(@PathVariable Long postId) {
        return Result.ok(commentMapper.selectList(new LambdaQueryWrapper<PostComment>()
                .eq(PostComment::getPostId, postId).eq(PostComment::getStatus, 1)
                .orderByDesc(PostComment::getCreatedAt)));
    }

    /** 发表评论 */
    @PostMapping("/posts/{postId}/comments")
    public Result<PostComment> addComment(@RequestAttribute("userId") Long userId,
            @PathVariable Long postId, @RequestBody PostComment body) {
        body.setCommentId(null);
        body.setPostId(postId); body.setUserId(userId);
        body.setStatus(1); body.setLikeCount(0); body.setCreatedAt(LocalDateTime.now());
        commentMapper.insert(body);
        CommunityPost post = postMapper.selectById(postId);
        if (post != null) {
            post.setCommentCount((post.getCommentCount() == null ? 0 : post.getCommentCount()) + 1);
            postMapper.updateById(post);
        }
        return Result.ok(body);
    }

    /** 收藏/取消收藏 */
    @PostMapping("/favorites")
    public Result<Boolean> toggleFavorite(@RequestAttribute("userId") Long userId, @RequestBody Map<String, Object> body) {
        Long targetId = Long.valueOf(body.get("targetId").toString());
        Integer targetType = Integer.valueOf(body.getOrDefault("targetType", 1).toString());
        UserFavorite exist = favoriteMapper.selectOne(new LambdaQueryWrapper<UserFavorite>()
                .eq(UserFavorite::getUserId, userId).eq(UserFavorite::getTargetId, targetId)
                .eq(UserFavorite::getTargetType, targetType));
        if (exist != null) {
            favoriteMapper.deleteById(exist);
            return Result.ok(false);
        }
        UserFavorite fav = new UserFavorite();
        fav.setUserId(userId); fav.setTargetId(targetId); fav.setTargetType(targetType);
        fav.setCreatedAt(LocalDateTime.now());
        favoriteMapper.insert(fav);
        return Result.ok(true);
    }

    /** 我的收藏 */
    @GetMapping("/favorites")
    public Result<List<UserFavorite>> myFavorites(@RequestAttribute("userId") Long userId,
            @RequestParam(required = false) Integer targetType) {
        LambdaQueryWrapper<UserFavorite> qw = new LambdaQueryWrapper<UserFavorite>()
                .eq(UserFavorite::getUserId, userId)
                .eq(targetType != null, UserFavorite::getTargetType, targetType)
                .orderByDesc(UserFavorite::getCreatedAt);
        return Result.ok(favoriteMapper.selectList(qw));
    }

    /** 点赞/取消点赞 */
    @PostMapping("/likes")
    public Result<Boolean> toggleLike(@RequestAttribute("userId") Long userId, @RequestBody Map<String, Object> body) {
        Long targetId = Long.valueOf(body.get("targetId").toString());
        Integer targetType = Integer.valueOf(body.getOrDefault("targetType", 1).toString());
        ContentLike exist = likeMapper.selectOne(new LambdaQueryWrapper<ContentLike>()
                .eq(ContentLike::getUserId, userId).eq(ContentLike::getTargetId, targetId)
                .eq(ContentLike::getTargetType, targetType));
        if (exist != null) {
            likeMapper.deleteById(exist);
            return Result.ok(false);
        }
        ContentLike like = new ContentLike();
        like.setUserId(userId); like.setTargetId(targetId); like.setTargetType(targetType);
        like.setCreatedAt(LocalDateTime.now());
        likeMapper.insert(like);
        return Result.ok(true);
    }

    /** 购物车列表 */
    @GetMapping("/cart")
    public Result<List<Map<String, Object>>> cart(@RequestAttribute("userId") Long userId) {
        List<CartItem> items = cartMapper.selectList(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId).orderByDesc(CartItem::getCreatedAt));
        List<Map<String, Object>> result = new ArrayList<>();
        for (CartItem item : items) {
            Map<String, Object> m = new HashMap<>();
            m.put("cart", item);
            m.put("product", productMapper.selectById(item.getProductId()));
            result.add(m);
        }
        return Result.ok(result);
    }

    /** 加入购物车 */
    @PostMapping("/cart")
    public Result<Void> addCart(@RequestAttribute("userId") Long userId, @RequestBody Map<String, Object> body) {
        Long productId = Long.valueOf(body.get("productId").toString());
        Integer qty = Integer.valueOf(body.getOrDefault("quantity", 1).toString());
        CartItem exist = cartMapper.selectOne(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId).eq(CartItem::getProductId, productId));
        if (exist != null) {
            exist.setQuantity(exist.getQuantity() + qty);
            cartMapper.updateById(exist);
        } else {
            CartItem item = new CartItem();
            item.setUserId(userId); item.setProductId(productId);
            item.setQuantity(qty); item.setChecked(1); item.setCreatedAt(LocalDateTime.now());
            cartMapper.insert(item);
        }
        return Result.ok();
    }

    /** 删除购物车项 */
    @DeleteMapping("/cart/{cartId}")
    public Result<Void> deleteCart(@RequestAttribute("userId") Long userId, @PathVariable Long cartId) {
        cartMapper.delete(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getCartId, cartId).eq(CartItem::getUserId, userId));
        return Result.ok();
    }

    /** 收货地址列表 */
    @GetMapping("/addresses")
    public Result<List<UserAddress>> addresses(@RequestAttribute("userId") Long userId) {
        return Result.ok(addressMapper.selectList(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId, userId).orderByDesc(UserAddress::getIsDefault)));
    }

    /** 新增/修改地址 */
    @PostMapping("/addresses")
    public Result<UserAddress> saveAddress(@RequestAttribute("userId") Long userId, @RequestBody UserAddress body) {
        body.setUserId(userId);
        if (body.getAddressId() != null) {
            addressMapper.updateById(body);
        } else {
            body.setCreatedAt(LocalDateTime.now());
            addressMapper.insert(body);
        }
        return Result.ok(body);
    }

    /** 消息列表 */
    @GetMapping("/messages")
    public Result<PageResult<MessageNotice>> messages(@RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size) {
        Page<MessageNotice> p = messageMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<MessageNotice>()
                        .eq(MessageNotice::getUserId, userId).orderByDesc(MessageNotice::getCreatedAt));
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    /** 标记消息已读 */
    @PutMapping("/messages/{id}/read")
    public Result<Void> readMessage(@RequestAttribute("userId") Long userId, @PathVariable Long id) {
        MessageNotice m = messageMapper.selectById(id);
        if (m != null && m.getUserId().equals(userId)) {
            m.setIsRead(1);
            messageMapper.updateById(m);
        }
        return Result.ok();
    }

    /** 未读消息数 */
    @GetMapping("/messages/unread-count")
    public Result<Long> unreadCount(@RequestAttribute("userId") Long userId) {
        return Result.ok(messageMapper.selectCount(new LambdaQueryWrapper<MessageNotice>()
                .eq(MessageNotice::getUserId, userId).eq(MessageNotice::getIsRead, 0)));
    }

    /** 我的活动报名 */
    @GetMapping("/enrolls")
    public Result<List<Map<String, Object>>> myEnrolls(@RequestAttribute("userId") Long userId) {
        List<ActivityEnroll> enrolls = enrollMapper.selectList(new LambdaQueryWrapper<ActivityEnroll>()
                .eq(ActivityEnroll::getUserId, userId).orderByDesc(ActivityEnroll::getCreatedAt));
        List<Map<String, Object>> result = new ArrayList<>();
        for (ActivityEnroll e : enrolls) {
            Map<String, Object> m = new HashMap<>();
            m.put("enroll", e);
            m.put("activity", activityMapper.selectById(e.getActivityId()));
            result.add(m);
        }
        return Result.ok(result);
    }

    /** 报名活动 */
    @PostMapping("/enrolls")
    public Result<ActivityEnroll> enroll(@RequestAttribute("userId") Long userId, @RequestBody ActivityEnroll body) {
        Long cnt = enrollMapper.selectCount(new LambdaQueryWrapper<ActivityEnroll>()
                .eq(ActivityEnroll::getActivityId, body.getActivityId())
                .eq(ActivityEnroll::getUserId, userId));
        if (cnt != null && cnt > 0) throw new BizException(400, "已报名该活动");
        body.setEnrollId(null);
        body.setUserId(userId);
        body.setStatus(1);
        body.setCreatedAt(LocalDateTime.now());
        enrollMapper.insert(body);
        return Result.ok(body);
    }

    /** 我的订单 */
    @GetMapping("/orders")
    public Result<PageResult<Orders>> myOrders(@RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) Integer status) {
        LambdaQueryWrapper<Orders> qw = new LambdaQueryWrapper<Orders>()
                .eq(Orders::getUserId, userId)
                .eq(status != null, Orders::getStatus, status)
                .orderByDesc(Orders::getCreatedAt);
        Page<Orders> p = ordersMapper.selectPage(new Page<>(page, size), qw);
        return Result.ok(PageResult.of(p.getRecords(), p.getTotal(), page, size));
    }

    /** 订单详情 */
    @GetMapping("/orders/{id}")
    public Result<Map<String, Object>> orderDetail(@RequestAttribute("userId") Long userId, @PathVariable Long id) {
        Orders order = ordersMapper.selectById(id);
        if (order == null || !order.getUserId().equals(userId)) throw new BizException(404, "订单不存在");
        Map<String, Object> data = new HashMap<>();
        data.put("order", order);
        data.put("items", orderService.getOrderItems(id));
        return Result.ok(data);
    }

    /** 创建订单 */
    @PostMapping("/orders")
    public Result<Orders> createOrder(@RequestAttribute("userId") Long userId, @RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
        Orders order = orderService.createOrder(userId, items,
                String.valueOf(body.getOrDefault("receiver", "")),
                String.valueOf(body.getOrDefault("phone", "")),
                String.valueOf(body.getOrDefault("address", "")),
                String.valueOf(body.getOrDefault("remark", "")));
        return Result.ok(order);
    }

    /** 支付订单 */
    @PutMapping("/orders/{id}/pay")
    public Result<Void> payOrder(@RequestAttribute("userId") Long userId, @PathVariable Long id) {
        orderService.payOrder(userId, id);
        return Result.ok();
    }

    /** 收货 */
    @PutMapping("/orders/{id}/receive")
    public Result<Void> receiveOrder(@RequestAttribute("userId") Long userId, @PathVariable Long id) {
        orderService.receiveOrder(userId, id);
        return Result.ok();
    }

    /** 取消订单 */
    @PutMapping("/orders/{id}/cancel")
    public Result<Void> cancelOrder(@RequestAttribute("userId") Long userId, @PathVariable Long id) {
        orderService.cancelOrder(userId, id);
        return Result.ok();
    }
}
