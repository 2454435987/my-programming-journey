<template>
  <view class="page">
    <view class="card" v-for="c in list" :key="c.courseId" @click="go(c.courseId)">
      <image :src="imgUrl(c.coverImage)" class="img" mode="aspectFill" />
      <view class="info">
        <text class="title">{{ c.title }}</text>
        <text class="sum">{{ c.summary }}</text>
        <view class="meta">
          <text class="lv">{{ ['','入门','进阶','高级'][c.level] }}</text>
          <text class="t">{{ c.duration }}分钟</text>
          <text class="t">{{ c.viewCount }}人学习</text>
          <text class="price" v-if="c.isFree">免费</text>
          <text class="price" v-else>¥{{ c.price }}</text>
        </view>
      </view>
    </view>
  </view>
</template>
<script>
import { get, BASE_URL } from '@/utils/request.js'
export default {
  data() { return { list: [] } },
  onShow() { this.load() },
  methods: {
    async load() { const r = await get('/api/public/courses', { page: 1, size: 20 }); this.list = r.data.records || [] },
    imgUrl(u) { return !u ? '' : (u.startsWith('http') ? u : BASE_URL + u) },
    go(id) { uni.navigateTo({ url: '/pages/course/detail?id=' + id }) }
  }
}
</script>
<style scoped>
.page{padding:16rpx}
.card{display:flex;background:#fff;border-radius:16rpx;padding:20rpx;margin-bottom:16rpx}
.img{width:240rpx;height:170rpx;border-radius:12rpx;flex-shrink:0}
.info{flex:1;margin-left:16rpx;display:flex;flex-direction:column;justify-content:space-between}
.title{font-size:28rpx;font-weight:600}
.sum{font-size:22rpx;color:#909399;display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical;overflow:hidden}
.meta{display:flex;gap:16rpx;align-items:center}
.lv{font-size:20rpx;background:#e8f5e9;color:#2d6a4f;padding:4rpx 14rpx;border-radius:16rpx}
.t{font-size:20rpx;color:#b0b3b8}
.price{margin-left:auto;font-size:26rpx;color:#e74c3c;font-weight:600}
</style>
