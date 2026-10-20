<template>
  <view class="page">
    <view class="card" v-for="c in list" :key="c.cart.cartId">
      <image :src="imgUrl(c.product && c.product.coverImage)" class="img" mode="aspectFill" />
      <view class="info">
        <text class="name">{{ c.product && c.product.productName }}</text>
        <view class="row">
          <text class="price">¥{{ c.product && c.product.price }}</text>
          <text class="qty">x{{ c.cart.quantity }}</text>
        </view>
      </view>
    </view>
    <view v-if="list.length===0" class="empty">购物车是空的</view>
  </view>
</template>
<script>
import { get, BASE_URL } from '@/utils/request.js'
export default {
  data() { return { list: [] } },
  onShow() { this.load() },
  methods: {
    async load() {
      const r = await get('/api/user/cart')
      this.list = r.data || []
    },
    imgUrl(u) { return !u ? '' : (u.startsWith('http') ? u : BASE_URL + u) }
  }
}
</script>
<style scoped>
.page{padding:16rpx}
.card{display:flex;background:#fff;border-radius:16rpx;padding:20rpx;margin-bottom:16rpx}
.img{width:160rpx;height:160rpx;border-radius:12rpx}
.info{flex:1;margin-left:20rpx;display:flex;flex-direction:column;justify-content:space-between}
.name{font-size:28rpx;font-weight:600}
.row{display:flex;justify-content:space-between;align-items:center}
.price{color:#e74c3c;font-size:30rpx;font-weight:700}
.qty{color:#606266;font-size:26rpx}
.empty{text-align:center;padding:100rpx 0;color:#909399}
</style>
