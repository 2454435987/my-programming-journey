<template>
  <view class="page">
    <view class="tabs">
      <view
        v-for="t in tabs"
        :key="t.value"
        class="tab"
        :class="{ on: current === t.value }"
        @click="current = t.value"
      >{{ t.label }}</view>
    </view>

    <block v-if="current === 'DEMAND'">
      <view class="card" v-for="d in openDemands" :key="d.id">
        <view class="row">
          <view class="title">{{ d.title }}</view>
          <view class="tag">{{ d.category }}</view>
        </view>
        <view class="meta">老人: {{ d.elder_name }} · {{ d.community }}</view>
        <view class="meta">服务时间: {{ fmt(d.service_time) }}</view>
        <view class="meta">地址: {{ d.address }}</view>
        <view class="meta">健康备注: {{ d.health_note || '无' }}</view>
        <button class="btn-small" @click="accept(d)">报名接单</button>
      </view>
      <view v-if="!openDemands.length" class="empty">暂无可接需求</view>
    </block>

    <block v-else>
      <view class="card" v-for="t in tasks" :key="t.id">
        <view class="row">
          <view class="title">{{ t.title }}</view>
          <view class="tag">{{ t.category }}</view>
        </view>
        <view class="meta">{{ t.description }}</view>
        <view class="meta">时间: {{ fmt(t.service_time) }} · 地点: {{ t.address }}</view>
        <view class="meta">需要{{ t.required_count }}人 / 已报名{{ t.joined_count }}人</view>
        <button class="btn-small" @click="signup(t)">报名参加</button>
      </view>
      <view v-if="!tasks.length" class="empty">暂无志愿任务</view>
    </block>
  </view>
</template>

<script>
import { get, post, currentUser } from '@/utils/request.js'
import { fmtTime } from '@/utils/common.js'

export default {
  data() {
    return {
      current: 'DEMAND',
      tabs: [
        { label: '服务需求', value: 'DEMAND' },
        { label: '志愿任务', value: 'TASK' }
      ],
      openDemands: [],
      tasks: []
    }
  },
  onShow() {
    this.load()
  },
  methods: {
    fmt: fmtTime,
    async load() {
      this.openDemands = await get('/api/volunteer/demand/open')
      this.tasks = await get('/api/volunteer/task/list')
    },
    async accept(d) {
      const user = currentUser()
      if (!user) return uni.reLaunch({ url: '/pages/login/login' })
      uni.showLoading({ title: '接单中...' })
      try {
        await post('/api/volunteer/demand/accept', { demandId: d.id, userId: user.id })
        uni.hideLoading()
        uni.showToast({ title: '接单成功', icon: 'success' })
        this.load()
      } catch (e) {
        uni.hideLoading()
      }
    },
    async signup(t) {
      const user = currentUser()
      if (!user) return uni.reLaunch({ url: '/pages/login/login' })
      try {
        await post('/api/volunteer/task/signup', { taskId: t.id, userId: user.id, remark: '小程序报名' })
        uni.showToast({ title: '报名成功', icon: 'success' })
        this.load()
      } catch (e) {
        /* toast in request */
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.page {
  padding: 24rpx;
}
.tabs {
  display: flex;
  background: #fff;
  border-radius: 16rpx;
  padding: 10rpx;
  margin-bottom: 24rpx;
}
.tab {
  flex: 1;
  text-align: center;
  padding: 16rpx 0;
  font-size: 26rpx;
  color: #667085;
  border-radius: 12rpx;
  &.on {
    background: #2f7cf6;
    color: #fff;
  }
}
.card {
  background: #fff;
  border-radius: 20rpx;
  padding: 28rpx;
  margin-bottom: 20rpx;
}
.row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.title {
  font-size: 30rpx;
  font-weight: 600;
  flex: 1;
}
.tag {
  font-size: 22rpx;
  color: #2f7cf6;
  background: #e8f1ff;
  padding: 8rpx 18rpx;
  border-radius: 20rpx;
}
.meta {
  font-size: 24rpx;
  color: #8a94a6;
  margin-top: 10rpx;
  line-height: 1.5;
}
.btn-small {
  margin-top: 20rpx;
  background: #2f7cf6;
  color: #fff;
  border-radius: 12rpx;
  font-size: 26rpx;
  height: 72rpx;
  line-height: 72rpx;
}
.empty {
  text-align: center;
  color: #8a94a6;
  padding: 100rpx 0;
}
</style>
