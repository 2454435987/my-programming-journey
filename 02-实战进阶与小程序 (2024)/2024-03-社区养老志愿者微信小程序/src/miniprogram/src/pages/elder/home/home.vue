<template>
  <view class="page">
    <view class="header">
      <view class="hello">{{ greeting }},{{ profile.elder_name || '老人' }}</view>
      <view class="community">{{ profile.community || '幸福里社区' }} · 社区养老志愿服务</view>
    </view>

    <view class="sos-bar" @click="go('/pages/elder/sos/sos')">
      <view class="sos-icon">急</view>
      <view class="sos-text">
        <view class="sos-title">一键紧急求助</view>
        <view class="sos-desc">自动推送管理员与就近志愿者,附带位置</view>
      </view>
      <view class="sos-btn">呼叫</view>
    </view>

    <view class="stats">
      <view class="stat">
        <view class="num">{{ stat.pending }}</view>
        <view class="label">待接单</view>
      </view>
      <view class="stat">
        <view class="num">{{ stat.serving }}</view>
        <view class="label">服务中</view>
      </view>
      <view class="stat">
        <view class="num">{{ stat.done }}</view>
        <view class="label">已完成</view>
      </view>
      <view class="stat">
        <view class="num">{{ stat.unread }}</view>
        <view class="label">未读消息</view>
      </view>
    </view>

    <view class="grid">
      <view class="item" v-for="f in features" :key="f.path" @click="go(f.path)">
        <view class="icon" :style="{ background: f.color }">{{ f.icon }}</view>
        <view class="name">{{ f.name }}</view>
        <view class="desc">{{ f.desc }}</view>
      </view>
    </view>
  </view>
</template>

<script>
import { get, currentUser } from '@/utils/request.js'

export default {
  data() {
    return {
      profile: {},
      stat: { pending: 0, serving: 0, done: 0, unread: 0 },
      features: [
        { name: '需求发布', desc: '上门陪伴/助餐/助洁/代购', icon: '发', color: '#2F7CF6', path: '/pages/elder/publish/publish' },
        { name: '任务查看', desc: '状态与志愿者信息', icon: '任', color: '#34C77B', path: '/pages/elder/demands/demands' },
        { name: '服务评价', desc: '打分与评价', icon: '评', color: '#FF9F43', path: '/pages/elder/evaluate/evaluate' },
        { name: '消息通知', desc: '接单与时间变更', icon: '信', color: '#9B59F6', path: '/pages/elder/messages/messages' },
        { name: '个人档案', desc: '健康备注与联系方式', icon: '档', color: '#F4436C', path: '/pages/elder/profile/profile' },
        { name: '紧急求助', desc: '一键呼叫', icon: '急', color: '#FF6B6B', path: '/pages/elder/sos/sos' }
      ]
    }
  },
  computed: {
    greeting() {
      const h = new Date().getHours()
      return h < 12 ? '早上好' : h < 18 ? '下午好' : '晚上好'
    }
  },
  onShow() {
    this.load()
  },
  methods: {
    go(path) {
      uni.navigateTo({ url: path })
    },
    async load() {
      const user = currentUser()
      if (!user) {
        uni.reLaunch({ url: '/pages/login/login' })
        return
      }
      this.profile = (await get('/api/elder/profile', { userId: user.id })) || {}
      const list = await get('/api/elder/demand/list', { userId: user.id })
      this.stat.pending = list.filter((d) => d.status === 'PENDING').length
      this.stat.serving = list.filter((d) => d.status === 'SERVING').length
      this.stat.done = list.filter((d) => d.status === 'DONE').length
      const unread = await get('/api/message/unread', { userId: user.id })
      this.stat.unread = Number(unread) || 0
    }
  }
}
</script>

<style lang="scss" scoped>
.page {
  min-height: 100vh;
  padding-bottom: 40rpx;
}
.header {
  background: linear-gradient(180deg, #2f7cf6, #4f96ff);
  padding: 40rpx 32rpx 80rpx;
  color: #fff;
}
.hello {
  font-size: 40rpx;
  font-weight: 600;
}
.community {
  margin-top: 10rpx;
  font-size: 24rpx;
  opacity: 0.9;
}
.sos-bar {
  margin: -50rpx 28rpx 24rpx;
  background: linear-gradient(90deg, #fff0f0, #ffe3e3);
  border-radius: 20rpx;
  padding: 26rpx;
  display: flex;
  align-items: center;
}
.sos-icon {
  width: 72rpx;
  height: 72rpx;
  background: #f4436c;
  color: #fff;
  border-radius: 50%;
  text-align: center;
  line-height: 72rpx;
  font-size: 32rpx;
  margin-right: 18rpx;
}
.sos-text {
  flex: 1;
}
.sos-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #d0365c;
}
.sos-desc {
  font-size: 22rpx;
  color: #b06a7c;
  margin-top: 4rpx;
}
.sos-btn {
  background: #f4436c;
  color: #fff;
  padding: 14rpx 30rpx;
  border-radius: 30rpx;
  font-size: 26rpx;
}
.stats {
  margin: 0 28rpx 24rpx;
  background: #fff;
  border-radius: 20rpx;
  padding: 28rpx 10rpx;
  display: flex;
}
.stat {
  flex: 1;
  text-align: center;
}
.num {
  font-size: 40rpx;
  font-weight: 600;
  color: #2f7cf6;
}
.label {
  font-size: 22rpx;
  color: #8a94a6;
  margin-top: 6rpx;
}
.grid {
  margin: 0 28rpx;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}
.item {
  width: 48%;
  background: #fff;
  border-radius: 20rpx;
  padding: 28rpx;
  margin-bottom: 20rpx;
  box-sizing: border-box;
}
.icon {
  width: 72rpx;
  height: 72rpx;
  border-radius: 18rpx;
  color: #fff;
  text-align: center;
  line-height: 72rpx;
  font-size: 30rpx;
}
.name {
  margin-top: 16rpx;
  font-size: 28rpx;
  font-weight: 600;
}
.desc {
  margin-top: 6rpx;
  font-size: 22rpx;
  color: #8a94a6;
}
</style>
