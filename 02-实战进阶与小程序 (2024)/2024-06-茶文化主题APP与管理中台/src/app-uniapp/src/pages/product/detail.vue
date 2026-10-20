<template>
  <view class="page">
    <image :src="imgUrl(detail.coverImage)" class="cover" mode="aspectFill" />
    <view class="card">
      <view class="row"><text class="price">¥{{ detail.price }}</text>
        <text class="orig">¥{{ detail.originalPrice }}</text>
        <text class="sales">已售{{ detail.sales }}</text></view>
      <text class="name">{{ detail.productName }}</text>
      <text class="sub">{{ detail.subtitle }}</text>
      <view class="tags">
        <text class="tag">{{ detail.origin }}</text>
        <text class="tag">{{ detail.unit }}</text>
        <text class="tag">库存{{ detail.stock }}</text>
      </view>
    </view>
    <view class="card">
      <text class="ctitle">商品详情</text>
      <text class="desc">{{ detail.description }}</text>
    </view>
    <view class="bar">
      <button class="btn1" @click="addCart">加入购物车</button>
      <button class="btn2" @click="buyNow">立即购买</button>
    </view>
  </view>
</template>
<script>
import { get, post, BASE_URL } from '@/utils/request.js'
export default {
  data() { return { detail: {} } },
  onLoad(o) { this.id = o.id; this.load() },
  methods: {
    async load() { const r = await get('/api/public/products/' + this.id); this.detail = r.data || {} },
    imgUrl(u) { return !u ? '' : (u.startsWith('http') ? u : BASE_URL + u) },
    async addCart() {
      if (!uni.getStorageSync('token')) return uni.navigateTo({ url: '/pages/user/login' })
      await post('/api/user/cart', { productId: this.id, quantity: 1 })
      uni.showToast({ title: '已加入购物车', icon: 'success' })
    },
    buyNow() {
      if (!uni.getStorageSync('token')) return uni.navigateTo({ url: '/pages/user/login' })
      uni.showToast({ title: '下单成功', icon: 'success' })
    }
  }
}
</script>
<style scoped>
.page{padding-bottom:130rpx;background:#f5f7fa}
.cover{width:100%;height:560rpx;background:#fff}
.card{background:#fff;margin:16rpx;border-radius:16rpx;padding:24rpx}
.row{display:flex;align-items:baseline;gap:16rpx}
.price{font-size:44rpx;color:#e74c3c;font-weight:700}
.orig{font-size:24rpx;color:#b0b3b8;text-decoration:line-through}
.sales{margin-left:auto;font-size:22rpx;color:#909399}
.name{font-size:32rpx;font-weight:700;margin-top:12rpx;display:block}
.sub{font-size:24rpx;color:#909399;margin-top:8rpx;display:block}
.tags{display:flex;gap:16rpx;margin-top:16rpx}
.tag{font-size:20rpx;background:#f5f7fa;color:#606266;padding:6rpx 18rpx;border-radius:20rpx}
.ctitle{font-size:30rpx;font-weight:700;display:block;margin-bottom:12rpx}
.desc{font-size:26rpx;color:#606266;line-height:1.8}
.bar{position:fixed;bottom:0;left:0;right:0;background:#fff;display:flex;gap:16rpx;padding:16rpx 24rpx}
.btn1,.btn2{flex:1;border-radius:40rpx;font-size:26rpx;height:72rpx;line-height:72rpx;border:none;margin:0}
.btn1{background:#ffe0b2;color:#e67e22}
.btn2{background:#e74c3c;color:#fff}
</style>
