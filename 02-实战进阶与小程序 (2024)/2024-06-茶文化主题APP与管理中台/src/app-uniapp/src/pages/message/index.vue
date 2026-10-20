<template>
  <view class="page">
    <view class="card" v-for="m in list" :key="m.messageId" :class="{ unread: m.isRead === 0 }">
      <view class="head">
        <text class="type">{{ {1:'系统',2:'订单',3:'活动',4:'互动'}[m.msgType] }}</text>
        <text class="time">{{ fmt(m.createdAt) }}</text>
      </view>
      <text class="title">{{ m.title }}</text>
      <text class="content">{{ m.content }}</text>
    </view>
    <view v-if="list.length===0" class="empty">暂无消息</view>
  </view>
</template>
<script>
import { get } from '@/utils/request.js'
export default {
  data() { return { list: [] } },
  onShow() { this.load() },
  methods: {
    async load() {
      if (!uni.getStorageSync('token')) return
      const r = await get('/api/user/messages', { page: 1, size: 20 })
      this.list = r.data.records || []
    },
    fmt(t) { return t ? String(t).replace('T', ' ').slice(0, 16) : '' }
  }
}
</script>
<style scoped>
.page{padding:16rpx}
.card{background:#fff;border-radius:16rpx;padding:24rpx;margin-bottom:16rpx}
.card.unread{border-left:6rpx solid #2d6a4f}
.head{display:flex;justify-content:space-between;margin-bottom:10rpx}
.type{font-size:20rpx;background:#e8f5e9;color:#2d6a4f;padding:4rpx 14rpx;border-radius:16rpx}
.time{font-size:20rpx;color:#b0b3b8}
.title{font-size:28rpx;font-weight:600;display:block}
.content{font-size:24rpx;color:#606266;margin-top:8rpx;display:block;line-height:1.6}
.empty{text-align:center;padding:100rpx 0;color:#909399}
</style>
