<template>
  <view class="page">
    <view class="card">
      <view class="head">
        <view class="title">{{ d.title }}</view>
        <view class="tag">{{ d.category }}</view>
      </view>
      <view class="line">需求编号: {{ d.demand_no }}</view>
      <view class="line">服务时间: {{ fmt(d.service_time) }}</view>
      <view class="line">服务地址: {{ d.address }}</view>
      <view class="line">需求描述: {{ d.description }}</view>
    </view>

    <view class="card">
      <view class="sec-title">服务对象</view>
      <view class="line">老人姓名: {{ d.elder_name }}</view>
      <view class="line">联系电话: {{ d.elder_phone }}</view>
      <view class="line">健康备注: {{ d.health_note || '无' }}</view>
    </view>

    <view class="btns">
      <button class="btn-primary" v-if="d.status === 'PENDING'" @click="accept">报名接单</button>
      <button class="btn-primary" v-if="d.status === 'SERVING'" @click="goCheckin">去签到/签退</button>
      <button class="btn-plain" v-if="d.status === 'DONE'" disabled>服务已完成</button>
    </view>
  </view>
</template>

<script>
import { get, post, currentUser } from '@/utils/request.js'
import { fmtTime } from '@/utils/common.js'

export default {
  data() {
    return { id: 0, d: {} }
  },
  onLoad(q) {
    this.id = Number(q.id)
  },
  onShow() {
    this.load()
  },
  methods: {
    fmt: fmtTime,
    async load() {
      this.d = await get('/api/elder/demand/' + this.id)
    },
    async accept() {
      const user = currentUser()
      if (!user) return uni.reLaunch({ url: '/pages/login/login' })
      try {
        await post('/api/volunteer/demand/accept', { demandId: this.id, userId: user.id })
        uni.showToast({ title: '接单成功', icon: 'success' })
        this.load()
      } catch (e) {
        /* toast */
      }
    },
    goCheckin() {
      uni.navigateTo({ url: '/pages/volunteer/checkin/checkin?demandId=' + this.id })
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
.head {
  display: flex;
  justify-content: space-between;
}
.title {
  font-size: 32rpx;
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
.line {
  font-size: 26rpx;
  color: #667085;
  margin-top: 14rpx;
  line-height: 1.5;
}
.sec-title {
  font-size: 28rpx;
  font-weight: 600;
}
.btns {
  margin-top: 32rpx;
}
.btn-primary {
  background: #2f7cf6;
  color: #fff;
  border-radius: 14rpx;
  margin-bottom: 16rpx;
}
.btn-plain {
  background: #eef2f7;
  color: #8a94a6;
  border-radius: 14rpx;
}
</style>
