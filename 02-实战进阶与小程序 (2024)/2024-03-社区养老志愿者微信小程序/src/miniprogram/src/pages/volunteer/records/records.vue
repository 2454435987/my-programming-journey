<template>
  <view class="page">
    <view class="head-card">
      <view class="stat">
        <view class="num">{{ points.profile.points || 0 }}</view>
        <view class="label">志愿积分</view>
      </view>
      <view class="stat">
        <view class="num">{{ hoursText }}</view>
        <view class="label">服务时长</view>
      </view>
      <view class="stat">
        <view class="num">{{ records.length }}</view>
        <view class="label">服务次数</view>
      </view>
    </view>

    <view class="tabs">
      <view class="tab" :class="{ on: tab === 'rec' }" @click="tab = 'rec'">服务记录</view>
      <view class="tab" :class="{ on: tab === 'pts' }" @click="tab = 'pts'">积分明细</view>
    </view>

    <block v-if="tab === 'rec'">
      <view class="card" v-for="r in records" :key="r.id">
        <view class="row">
          <view class="title">{{ r.title }}</view>
          <view class="tag">{{ r.category }}</view>
        </view>
        <view class="meta">服务对象: {{ r.elder_name }} · 状态: {{ st(r.status) }}</view>
        <view class="meta">签到: {{ fmt(r.check_in_time) }} · 签退: {{ fmt(r.check_out_time) }}</view>
        <view class="meta">时长: {{ r.duration_minutes || 0 }} 分钟 · 评价: {{ r.eval_score || '-' }} 分</view>
        <view class="meta" v-if="r.log_content">日志: {{ r.log_content }}</view>
      </view>
      <view v-if="!records.length" class="empty">暂无服务记录</view>
    </block>

    <block v-else>
      <view class="card p" v-for="p in points.records" :key="p.id">
        <view class="row">
          <view class="title">{{ p.reason }}</view>
          <view class="delta" :class="{ plus: p.change_val > 0 }">{{ p.change_val > 0 ? '+' : '' }}{{ p.change_val }}</view>
        </view>
        <view class="meta">{{ p.type === 'EARN' ? '服务奖励' : p.type === 'EXCHANGE' ? '积分兑换' : '积分扣减' }} · {{ fmt(p.create_time) }}</view>
      </view>
      <view v-if="!points.records || !points.records.length" class="empty">暂无积分明细</view>
    </block>
  </view>
</template>

<script>
import { get, currentUser } from '@/utils/request.js'
import { fmtTime, statusText } from '@/utils/common.js'

export default {
  data() {
    return {
      tab: 'rec',
      records: [],
      points: { profile: {}, records: [] }
    }
  },
  computed: {
    hoursText() {
      const m = Number(this.points.profile.total_minutes) || 0
      return (m / 60).toFixed(1) + 'h'
    }
  },
  onShow() {
    this.load()
  },
  methods: {
    fmt: fmtTime,
    st: statusText,
    async load() {
      const user = currentUser()
      if (!user) return uni.reLaunch({ url: '/pages/login/login' })
      this.records = await get('/api/volunteer/records', { userId: user.id })
      this.points = await get('/api/volunteer/points', { userId: user.id })
    }
  }
}
</script>

<style lang="scss" scoped>
.page {
  padding: 24rpx;
}
.head-card {
  background: linear-gradient(120deg, #2f7cf6, #6aa6ff);
  border-radius: 20rpx;
  padding: 36rpx 10rpx;
  display: flex;
  color: #fff;
  margin-bottom: 20rpx;
}
.stat {
  flex: 1;
  text-align: center;
}
.num {
  font-size: 40rpx;
  font-weight: 600;
}
.label {
  font-size: 22rpx;
  opacity: 0.92;
  margin-top: 6rpx;
}
.tabs {
  display: flex;
  background: #fff;
  border-radius: 16rpx;
  padding: 10rpx;
  margin-bottom: 20rpx;
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
  border-radius: 18rpx;
  padding: 26rpx;
  margin-bottom: 18rpx;
}
.row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.title {
  font-size: 28rpx;
  font-weight: 600;
  flex: 1;
}
.tag {
  font-size: 22rpx;
  color: #2f7cf6;
  background: #e8f1ff;
  padding: 8rpx 16rpx;
  border-radius: 20rpx;
}
.meta {
  font-size: 24rpx;
  color: #8a94a6;
  margin-top: 10rpx;
}
.delta {
  font-size: 32rpx;
  color: #f4436c;
  font-weight: 600;
  &.plus {
    color: #34c77b;
  }
}
.empty {
  text-align: center;
  color: #8a94a6;
  padding: 100rpx 0;
}
</style>
