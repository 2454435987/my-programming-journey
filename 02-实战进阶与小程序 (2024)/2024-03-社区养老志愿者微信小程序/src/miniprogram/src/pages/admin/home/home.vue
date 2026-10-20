<template>
  <view class="page">
    <view class="header">
      <view class="hello">管理员工作台</view>
      <view class="sub">社区养老志愿者服务全过程监管</view>
    </view>

    <view class="stats">
      <view class="stat" v-for="s in cards" :key="s.label">
        <view class="num">{{ s.value }}</view>
        <view class="label">{{ s.label }}</view>
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
      <view class="sec-title">待办事项</view>
      <view class="todo">待审核志愿者: {{ todo.volAudit }} 人</view>
      <view class="todo">待审核老人绑定: {{ todo.elderAudit }} 人</view>
      <view class="todo">待接单需求: {{ stat.demandPending || 0 }} 条</view>
      <view class="todo">紧急求助(未结): {{ todo.sosPending }} 条</view>
    </view>
  </view>
</template>

<script>
import { get, currentUser } from '@/utils/request.js'

export default {
  data() {
    return {
      stat: {},
      todo: { volAudit: 0, elderAudit: 0, sosPending: 0 },
      cards: [
        { label: '老人', value: 0 },
        { label: '志愿者', value: 0 },
        { label: '需求单', value: 0 },
        { label: '服务时长', value: 0 }
      ],
      features: [
        { name: '档案审核', desc: '志愿者/老人', icon: '审', color: '#2F7CF6', path: '/pages/admin/audit/audit' },
        { name: '老人管理', desc: '信息维护', icon: '管', color: '#34C77B', path: '/pages/admin/elders/elders' },
        { name: '需求监管', desc: '审核与分配', icon: '监', color: '#FF9F43', path: '/pages/admin/demands/demands' },
        { name: '任务管理', desc: '发布与匹配', icon: '任', color: '#9B59F6', path: '/pages/admin/tasks/tasks' },
        { name: '统计报表', desc: '数据可视化', icon: '统', color: '#F4436C', path: '/pages/admin/stats/stats' },
        { name: '消息推送', desc: '通知下发', icon: '推', color: '#17B0A6', path: '/pages/admin/messages/messages' }
      ]
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
      if (!user) return uni.reLaunch({ url: '/pages/login/login' })
      this.stat = await get('/api/admin/stats')
      this.cards[0].value = this.stat.elderCount
      this.cards[1].value = this.stat.volunteerCount
      this.cards[2].value = this.stat.demandTotal
      this.cards[3].value = ((this.stat.totalHours || 0) / 60).toFixed(0) + 'h'
      const vols = await get('/api/admin/volunteer/list', { status: 0 })
      const elders = await get('/api/admin/elder/list', { status: 0 })
      this.todo.volAudit = (vols || []).length
      this.todo.elderAudit = (elders || []).length
      const sos = await get('/api/admin/sos/list')
      this.todo.sosPending = (sos || []).filter((s) => s.status !== 'RESOLVED').length
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
  background: linear-gradient(180deg, #245f9e, #3d82c8);
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
  opacity: 0.9;
}
.stats {
  margin: -50rpx 28rpx 24rpx;
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
  font-size: 36rpx;
  font-weight: 600;
  color: #245f9e;
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
  padding: 24rpx 12rpx;
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
.todo {
  font-size: 26rpx;
  color: #667085;
  padding: 12rpx 0;
  border-bottom: 1rpx solid #f0f3f8;
}
</style>
