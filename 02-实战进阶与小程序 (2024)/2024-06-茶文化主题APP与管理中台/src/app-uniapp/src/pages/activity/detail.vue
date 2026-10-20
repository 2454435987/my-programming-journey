<template>
  <view class="page">
    <image :src="imgUrl(detail.coverImage)" class="cover" mode="aspectFill" />
    <view class="body">
      <text class="title">{{ detail.title }}</text>
      <view class="tags">
        <text class="tag">{{ detail.activityType }}</text>
        <text class="tag price" v-if="Number(detail.price)>0">¥{{ detail.price }}</text>
        <text class="tag free" v-else>免费</text>
      </view>
      <view class="info">
        <text class="line">时间：{{ fmt(detail.startTime) }} ~ {{ fmt(detail.endTime) }}</text>
        <text class="line">地点：{{ detail.location }}</text>
        <text class="line">报名：{{ detail.enrolledCount }}/{{ detail.maxEnroll }} 人</text>
        <text class="line">截止：{{ fmt(detail.enrollDeadline) }}</text>
      </view>
      <text class="sum">{{ detail.summary }}</text>
      <rich-text :nodes="detail.content" class="rich"></rich-text>
    </view>
    <view class="bar">
      <button class="btn" @click="enroll">立即报名</button>
    </view>
  </view>
</template>
<script>
import { get, post, BASE_URL } from '@/utils/request.js'
export default {
  data() { return { detail: {} } },
  onLoad(o) { this.load(o.id) },
  methods: {
    async load(id) { const r = await get('/api/public/activities/' + id); this.detail = r.data || {} },
    imgUrl(u) { return !u ? '' : (u.startsWith('http') ? u : BASE_URL + u) },
    fmt(t) { return t ? String(t).replace('T', ' ').slice(0, 16) : '' },
    async enroll() {
      if (!uni.getStorageSync('token')) return uni.navigateTo({ url: '/pages/user/login' })
      await post('/api/user/enrolls', { activityId: this.detail.activityId, realName: '', phone: '' })
      uni.showToast({ title: '报名成功', icon: 'success' })
    }
  }
}
</script>
<style scoped>
.page{background:#fff;min-height:100vh;padding-bottom:130rpx}
.cover{width:100%;height:420rpx}
.body{padding:30rpx}
.title{font-size:38rpx;font-weight:700;display:block;line-height:1.4}
.tags{display:flex;gap:16rpx;margin-top:16rpx}
.tag{font-size:22rpx;background:#e8f5e9;color:#2d6a4f;padding:6rpx 18rpx;border-radius:20rpx}
.tag.price{background:#fce4ec;color:#e74c3c}
.tag.free{background:#fff3e0;color:#e67e22}
.info{margin-top:24rpx;background:#f5f7fa;border-radius:12rpx;padding:20rpx}
.line{font-size:25rpx;color:#606266;display:block;line-height:2}
.sum{font-size:27rpx;color:#303133;margin-top:24rpx;line-height:1.8;display:block}
.rich{margin-top:20rpx;line-height:1.8;font-size:27rpx}
.bar{position:fixed;bottom:0;left:0;right:0;background:#fff;padding:16rpx 24rpx}
.btn{background:#2d6a4f;color:#fff;border-radius:40rpx;font-size:28rpx;height:80rpx;line-height:80rpx;border:none}
</style>
