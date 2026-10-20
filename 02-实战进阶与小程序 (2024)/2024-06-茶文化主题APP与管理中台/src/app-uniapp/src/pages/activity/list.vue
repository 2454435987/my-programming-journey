<template>
  <view class="page">
    <view class="card" v-for="a in list" :key="a.activityId" @click="go(a.activityId)">
      <image :src="imgUrl(a.coverImage)" class="img" mode="aspectFill" />
      <view class="body">
        <text class="title">{{ a.title }}</text>
        <text class="time">时间：{{ fmt(a.startTime) }}</text>
        <text class="loc">地点：{{ a.location }}</text>
        <view class="tags">
          <text class="tag">{{ a.activityType }}</text>
          <text class="tag free" v-if="Number(a.price)===0">免费</text>
          <text class="tag price" v-else>¥{{ a.price }}</text>
          <text class="tag">报名 {{ a.enrolledCount }}/{{ a.maxEnroll }}</text>
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
    async load() { const r = await get('/api/public/activities', { page: 1, size: 20 }); this.list = r.data.records || [] },
    imgUrl(u) { return !u ? '' : (u.startsWith('http') ? u : BASE_URL + u) },
    fmt(t) { return t ? String(t).replace('T', ' ').slice(0, 16) : '' },
    go(id) { uni.navigateTo({ url: '/pages/activity/detail?id=' + id }) }
  }
}
</script>
<style scoped>
.page{padding:16rpx}
.card{background:#fff;border-radius:16rpx;overflow:hidden;margin-bottom:16rpx}
.img{width:100%;height:300rpx}
.body{padding:20rpx}
.title{font-size:30rpx;font-weight:700;display:block}
.time,.loc{font-size:22rpx;color:#909399;margin-top:8rpx;display:block}
.tags{display:flex;gap:12rpx;margin-top:14rpx}
.tag{font-size:20rpx;background:#e8f5e9;color:#2d6a4f;padding:4rpx 14rpx;border-radius:16rpx}
.tag.free{background:#fff3e0;color:#e67e22}
.tag.price{background:#fce4ec;color:#e74c3c}
</style>
