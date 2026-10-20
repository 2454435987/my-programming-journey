<template>
  <view class="page">
    <view class="card">
      <view class="label">选择服务中需求</view>
      <picker :range="servingList" range-key="title" @change="onPick">
        <view class="input">{{ current ? current.title : '请选择需求' }}</view>
      </picker>
    </view>

    <view class="card center">
      <view class="circle" @click="doCheckin">
        <view class="c-title">签 到</view>
        <view class="c-desc">开始服务并记录位置</view>
      </view>
      <view class="arrow">→</view>
      <view class="circle out" @click="doCheckout">
        <view class="c-title">签 退</view>
        <view class="c-desc">结束服务并填写日志</view>
      </view>
    </view>

    <view class="card" v-if="needLog">
      <view class="label">服务日志</view>
      <textarea class="textarea" v-model="logContent" placeholder="请填写本次服务内容、老人情况等" />
      <button class="btn-primary" @click="doCheckout">提交签退与服务日志</button>
    </view>

    <view class="card">
      <view class="sec-title">今日服务日志</view>
      <view class="log" v-for="l in logs" :key="l.id">
        <view class="log-title">{{ l.title }} · {{ l.status === 'IN' ? '服务中' : '已签退' }}</view>
        <view class="log-meta">签到: {{ fmt(l.check_in_time) }} · 时长: {{ l.duration_minutes || 0 }} 分钟</view>
        <view class="log-meta">{{ l.log_content || '暂无日志' }}</view>
      </view>
      <view v-if="!logs.length" class="empty-mini">暂无服务日志</view>
    </view>
  </view>
</template>

<script>
import { get, post, currentUser } from '@/utils/request.js'
import { fmtTime } from '@/utils/common.js'

export default {
  data() {
    return {
      all: [],
      current: null,
      needLog: false,
      logContent: '',
      logs: []
    }
  },
  computed: {
    servingList() {
      return this.all.filter((d) => d.status === 'SERVING' || d.status === 'DONE')
    }
  },
  onLoad(q) {
    if (q && q.demandId) {
      this.pendingId = Number(q.demandId)
    }
  },
  onShow() {
    this.load()
  },
  methods: {
    fmt: fmtTime,
    onPick(e) {
      this.current = this.servingList[Number(e.detail.value)]
    },
    async load() {
      const user = currentUser()
      if (!user) return uni.reLaunch({ url: '/pages/login/login' })
      this.all = await get('/api/volunteer/demand/mine', { userId: user.id })
      if (this.pendingId) {
        this.current = this.all.find((d) => d.id === this.pendingId) || this.current
      }
      if (!this.current && this.servingList.length) {
        this.current = this.servingList[0]
      }
      this.logs = this.all
        .filter((d) => d.check_in_time)
        .map((d) => ({
          id: d.id,
          title: d.title,
          status: d.status === 'DONE' ? 'OUT' : 'IN',
          check_in_time: d.check_in_time,
          duration_minutes: d.duration_minutes,
          log_content: d.log_content
        }))
    },
    async doCheckin() {
      const user = currentUser()
      if (!this.current) return uni.showToast({ title: '请选择需求', icon: 'none' })
      try {
        await post('/api/volunteer/checkin', {
          demandId: this.current.id,
          userId: user.id,
          lng: 116.397,
          lat: 39.909
        })
        uni.showToast({ title: '签到成功', icon: 'success' })
        this.needLog = true
        this.load()
      } catch (e) {
        /* toast */
      }
    },
    async doCheckout() {
      const user = currentUser()
      if (!this.current) return uni.showToast({ title: '请选择需求', icon: 'none' })
      try {
        await post('/api/volunteer/checkout', {
          demandId: this.current.id,
          userId: user.id,
          lng: 116.397,
          lat: 39.909,
          logContent: this.logContent || '已完成本次服务'
        })
        uni.showToast({ title: '签退成功', icon: 'success' })
        this.needLog = false
        this.logContent = ''
        this.load()
      } catch (e) {
        /* toast */
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.page {
  padding: 24rpx;
}
.card {
  background: #fff;
  border-radius: 20rpx;
  padding: 28rpx;
  margin-bottom: 20rpx;
}
.center {
  display: flex;
  align-items: center;
  justify-content: space-around;
}
.circle {
  width: 240rpx;
  height: 240rpx;
  border-radius: 50%;
  background: linear-gradient(180deg, #34c77b, #5ed99a);
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  &.out {
    background: linear-gradient(180deg, #2f7cf6, #6aa6ff);
  }
}
.c-title {
  font-size: 40rpx;
  font-weight: 600;
}
.c-desc {
  font-size: 22rpx;
  margin-top: 8rpx;
  opacity: 0.92;
}
.arrow {
  font-size: 48rpx;
  color: #c8d2e0;
}
.label {
  font-size: 26rpx;
  color: #667085;
  margin-bottom: 12rpx;
}
.input {
  background: #f5f7fa;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
}
.textarea {
  background: #f5f7fa;
  border-radius: 12rpx;
  padding: 20rpx;
  width: 100%;
  height: 160rpx;
  box-sizing: border-box;
  margin-bottom: 20rpx;
}
.btn-primary {
  background: #2f7cf6;
  color: #fff;
  border-radius: 14rpx;
}
.sec-title {
  font-size: 28rpx;
  font-weight: 600;
  margin-bottom: 12rpx;
}
.log {
  border-top: 1rpx solid #f0f3f8;
  padding: 16rpx 0;
}
.log-title {
  font-size: 26rpx;
  font-weight: 600;
}
.log-meta {
  font-size: 22rpx;
  color: #8a94a6;
  margin-top: 6rpx;
}
.empty-mini {
  color: #8a94a6;
  font-size: 24rpx;
  text-align: center;
  padding: 20rpx 0;
}
</style>
