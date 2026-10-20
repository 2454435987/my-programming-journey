<template>
  <view class="page">
    <view class="bar">
      <view class="count">{{ list.length }} 条消息 · {{ unread }} 条未读</view>
      <view class="read-all" @click="readAll">全部已读</view>
    </view>

    <view v-if="list.length === 0" class="empty">暂无消息</view>

    <view
      class="card"
      :class="{ unread: !m.is_read }"
      v-for="m in list"
      :key="m.id"
      @click="readOne(m)"
    >
      <view class="row">
        <view class="type">{{ typeText(m.msg_type) }}</view>
        <view class="time">{{ fmt(m.create_time) }}</view>
      </view>
      <view class="title">{{ m.title }}</view>
      <view class="content">{{ m.content }}</view>
    </view>
  </view>
</template>

<script>
import { get, post, currentUser } from '@/utils/request.js'
import { fmtTime } from '@/utils/common.js'

export default {
  data() {
    return { list: [], unread: 0 }
  },
  onShow() {
    this.load()
  },
  methods: {
    fmt: fmtTime,
    typeText(t) {
      return {
        ACCEPT: '接单提醒',
        TIME_CHANGE: '时间变更',
        EMERGENCY: '紧急求助',
        SYSTEM: '系统通知',
        PUSH: '消息推送'
      }[t] || t
    },
    async load() {
      const user = currentUser()
      if (!user) return uni.reLaunch({ url: '/pages/login/login' })
      this.list = await get('/api/message/list', { userId: user.id })
      const n = await get('/api/message/unread', { userId: user.id })
      this.unread = Number(n) || 0
    },
    async readOne(m) {
      if (m.is_read) return
      const user = currentUser()
      await post('/api/message/read', { id: m.id, userId: user.id })
      this.load()
    },
    async readAll() {
      const user = currentUser()
      await post('/api/message/read-all', { userId: user.id })
      uni.showToast({ title: '已全部标为已读', icon: 'success' })
      this.load()
    }
  }
}
</script>

<style lang="scss" scoped>
.page {
  padding: 24rpx;
}
.bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  padding: 0 8rpx;
}
.count {
  font-size: 24rpx;
  color: #8a94a6;
}
.read-all {
  font-size: 24rpx;
  color: #2f7cf6;
}
.card {
  background: #fff;
  border-radius: 18rpx;
  padding: 26rpx;
  margin-bottom: 18rpx;
  &.unread {
    border-left: 8rpx solid #2f7cf6;
  }
}
.row {
  display: flex;
  justify-content: space-between;
}
.type {
  font-size: 22rpx;
  color: #2f7cf6;
  background: #e8f1ff;
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
}
.time {
  font-size: 22rpx;
  color: #8a94a6;
}
.title {
  font-size: 28rpx;
  font-weight: 600;
  margin: 14rpx 0 8rpx;
}
.content {
  font-size: 26rpx;
  color: #667085;
  line-height: 1.6;
}
.empty {
  text-align: center;
  color: #8a94a6;
  padding: 120rpx 0;
}
</style>
