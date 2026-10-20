package com.tea.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.common.BizException;
import com.tea.entity.CartItem;
import com.tea.entity.MessageNotice;
import com.tea.entity.OrderItem;
import com.tea.entity.Orders;
import com.tea.entity.TeaProduct;
import com.tea.mapper.CartItemMapper;
import com.tea.mapper.MessageNoticeMapper;
import com.tea.mapper.OrderItemMapper;
import com.tea.mapper.OrdersMapper;
import com.tea.mapper.TeaProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单服务：下单、支付、发货、收货
 */
@Service
public class OrderService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private TeaProductMapper productMapper;
    @Autowired
    private CartItemMapper cartItemMapper;
    @Autowired
    private MessageNoticeMapper messageNoticeMapper;

    /**
     * 从购物车/立即购买创建订单
     * @param userId 用户ID
     * @param items  [{productId, quantity}]
     */
    @Transactional(rollbackFor = Exception.class)
    public Orders createOrder(Long userId, List<Map<String, Object>> items,
                              String receiver, String phone, String address, String remark) {
        if (items == null || items.isEmpty()) {
            throw new BizException(400, "订单商品不能为空");
        }
        Orders order = new Orders();
        String orderNo = "TC" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%02d", (int) (Math.random() * 100));
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setStatus(0);
        order.setPayType(1);
        order.setFreight(new BigDecimal("0.00"));
        order.setReceiver(receiver);
        order.setReceiverPhone(phone);
        order.setReceiverAddress(address);
        order.setRemark(remark);
        order.setCreatedAt(LocalDateTime.now());
        ordersMapper.insert(order);

        BigDecimal total = BigDecimal.ZERO;
        for (Map<String, Object> item : items) {
            Long productId = Long.valueOf(item.get("productId").toString());
            Integer qty = Integer.valueOf(item.get("quantity").toString());
            TeaProduct product = productMapper.selectById(productId);
            if (product == null || product.getStatus() != 1) {
                throw new BizException(400, "商品不存在或已下架: " + productId);
            }
            if (product.getStock() < qty) {
                throw new BizException(400, "商品库存不足: " + product.getProductName());
            }
            // 扣库存 + 销量
            product.setStock(product.getStock() - qty);
            product.setSales(product.getSales() + qty);
            productMapper.updateById(product);

            OrderItem oi = new OrderItem();
            oi.setOrderId(order.getOrderId());
            oi.setOrderNo(orderNo);
            oi.setProductId(productId);
            oi.setProductName(product.getProductName());
            oi.setProductImage(product.getCoverImage());
            oi.setPrice(product.getPrice());
            oi.setQuantity(qty);
            orderItemMapper.insert(oi);

            total = total.add(product.getPrice().multiply(new BigDecimal(qty)));
        }
        order.setTotalAmount(total);
        order.setPayAmount(total);
        ordersMapper.updateById(order);

        // 清除已购购物车
        cartItemMapper.delete(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .in(CartItem::getProductId, items.stream()
                        .map(i -> Long.valueOf(i.get("productId").toString()))
                        .toArray()));
        return order;
    }

    /**
     * 支付订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void payOrder(Long userId, Long orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BizException(404, "订单不存在");
        }
        if (order.getStatus() != 0) {
            throw new BizException(400, "当前订单状态不允许支付");
        }
        order.setStatus(1);
        order.setPayTime(LocalDateTime.now());
        ordersMapper.updateById(order);
        MessageNotice msg = new MessageNotice();
        msg.setUserId(userId);
        msg.setTitle("支付成功通知");
        msg.setContent("您的订单 " + order.getOrderNo() + " 已支付成功，商家将尽快发货。");
        msg.setMsgType(2);
        msg.setIsRead(0);
        msg.setCreatedAt(LocalDateTime.now());
        messageNoticeMapper.insert(msg);
    }

    /**
     * 发货（管理员）
     */
    @Transactional(rollbackFor = Exception.class)
    public void shipOrder(Long orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) throw new BizException(404, "订单不存在");
        if (order.getStatus() != 1) throw new BizException(400, "当前订单状态不允许发货");
        order.setStatus(2);
        order.setShipTime(LocalDateTime.now());
        ordersMapper.updateById(order);
        MessageNotice msg = new MessageNotice();
        msg.setUserId(order.getUserId());
        msg.setTitle("订单发货通知");
        msg.setContent("您的订单 " + order.getOrderNo() + " 已发货，请注意查收。");
        msg.setMsgType(2);
        msg.setIsRead(0);
        msg.setCreatedAt(LocalDateTime.now());
        messageNoticeMapper.insert(msg);
    }

    /**
     * 收货
     */
    @Transactional(rollbackFor = Exception.class)
    public void receiveOrder(Long userId, Long orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BizException(404, "订单不存在");
        }
        if (order.getStatus() != 2) {
            throw new BizException(400, "当前订单状态不允许收货");
        }
        order.setStatus(3);
        order.setReceiveTime(LocalDateTime.now());
        ordersMapper.updateById(order);
    }

    /**
     * 取消订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long userId, Long orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BizException(404, "订单不存在");
        }
        if (order.getStatus() != 0) {
            throw new BizException(400, "仅待付款订单可取消");
        }
        order.setStatus(4);
        ordersMapper.updateById(order);
    }

    /**
     * 查询订单明细
     */
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
    }
}
