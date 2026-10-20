<template>
  <view class="page">
    <view class="header">
      <view class="hello">{{ greeting }},{{ profile.vol_name || '志愿者' }}</view>
      <view class="sub">{{ profile.service_area || '幸福里社区' }} · 技能: {{ profile.skills || '待完善' }}</view>
    </view>

    <view class="stats">
      <view class="stat">
        <view class="num">{{ profile.points || 0 }}</view>
        <view class="label">志愿积分</view>
      </view>
      <view class="stat">
        <view class="num">{{ hoursText }}</view>
        <view class="label">服务时长</view>
      </view>
      <view class="stat">
        <view class="num">{{ stat.serving }}</view>
        <view class="label">服务中</view>
      </view>
      <view class="stat">
        <view class="num">{{ stat.open }}</view>
        <view class="label">可接任务</view>
      </view>
    </view>

    <view class="grid">
      <view class="item" v-for="f in features" :key="f.path" @click="go(f.path)">
        <view class="icon" :style="{ background: f.color }">{{ f.icon }}</view>
        <view class="name">{{ f.name }}</view>
        <view class="desc">{{ f.desc }}</view>
      </view>
    </view>

    <view class="card">
      <view class="sec-title">待接单需求速览</view>
      <view class="mini" v-for="d in openList" :key="d.id" @click="go('/pages/volunteer/tasks/tasks')">
        <view class="mini-title">{{ d.title }}</view>
        <view class="mini-meta">{{ d.category }} · {{ d.elder_name }} · {{ fmt(d.service_time) }}</view>
      </view>
      <view v-if="!openList.length" class="empty-mini">暂无待接单需求</view>
    </view>
  </view>
</template>

<script>
import { get, currentUser } from '@/utils/request.js'
import { fmtTime } from '@/utils/common.js'

export default {
  data() {
    return {
      profile: {},
      stat: { serving: 0, open: 0 },
      openList: [],
      features: [
        { name: '注册认证', desc: '实名与技能登记', icon: '证', color: '#2F7CF6', path: '/pages/volunteer/register/register' },
        { name: '浏览任务', desc: '报名接单', icon: '接', color: '#34C77B', path: '/pages/volunteer/tasks/tasks' },
        { name: '签到签退', desc: '服务日志', icon: '签', color: '#FF9F43', path: '/pages/volunteer/checkin/checkin' },
        { name: '服务记录', desc: '时长与积分', icon: '录', color: '#9B59F6', path: '/pages/volunteer/records/records' },
        { name: '消息通知', desc: '接单与变更', icon: '信', color: '#F4436C', path: '/pages/volunteer/messages/messages' },
        { name: '志愿任务', desc: '社区活动报名', icon: '任', color: '#17B0A6', path: '/pages/volunteer/tasks/tasks' }
      ]
    }
  },
  computed: {
    greeting() {
      const h = new Date().getHours()
      return h < 12 ? '早上好' : h < 18 ? '下午好' : '晚上好'
    },
    hoursText() {
      const m = Number(this.profile.total_minutes) || 0
      return (m / 60).toFixed(1) + 'h'
    }
  },
  onShow() {
    this.load()
  },
  methods: {
    fmt: fmtTime,
    go(path) {
      uni.navigateTo({ url: path })
    },
    async load() {
      const user = currentUser()
      if (!user) return uni.reLaunch({ url: '/pages/login/login' })
      this.profile = (await get('/api/volunteer/profile', { userId: user.id })) || {}
      try {
        const mine = await get('/api/volunteer/demand/mine', { userId: user.id })
        this.stat.serving = mine.filter((d) => d.status === 'SERVING').length
      } catch (e) {
        /* 未注册 */
      }
      const open = await get('/api/volunteer/demand/open')
      this.openList = open.slice(0, 3)
      this.stat.open = open.length
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
.sub {
  margin-top: 10rpx;
  font-size: 24rpx;
  opacity: 0.92;
}
.stats {
  margin: -50rpx 28rpx 24rpx;
  background: #fff;
  border-radius: 20rpx;
  padding: 28rpx 10rpx;
  display: flex;
  box-shadow: 0 10rpx 30rpx rgba(31, 78, 155, 0.1);
}
.stat {
  flex: 1;
  text-align: center;
}
.num {
  font-size: 36rpx;
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
  width: 31%;
  background: #fff;
  border-radius: 18rpx;
  padding: 24rpx 16rpx;
  margin-bottom: 18rpx;
  text-align: center;
  box-sizing: border-box;
}
.icon {
  width: 64rpx;
  height: 64rpx;
  border-radius: 16rpx;
  color: #fff;
  text-align: center;
  line-height: 64rpx;
  font-size: 28rpx;
  margin: 0 auto;
}
.name {
  margin-top: 12rpx;
  font-size: 24rpx;
  font-weight: 600;
}
.desc {
  margin-top: 4rpx;
  font-size: 20rpx;
  color: #8a94a6;
}
.card {
  margin: 0 28rpx;
  background: #fff;
  border-radius: 20rpx;
  padding: 24rpx;
}
.sec-title {
  font-size: 28rpx;
  font-weight: 600;
  margin-bottom: 12rpx;
}
.mini {
  padding: 16rpx 0;
  border-bottom: 1rpx solid #f0f3f8;
}
.mini-title {
  font-size: 26rpx;
}
.mini-meta {
  font-size: 22rpx;
  color: #8a94a6;
  margin-top: 6rpx;
}
.empty-mini {
  color: #8a94a6;
  font-size: 24rpx;
  padding: 20rpx 0;
  text-align: center;
}
</style>
