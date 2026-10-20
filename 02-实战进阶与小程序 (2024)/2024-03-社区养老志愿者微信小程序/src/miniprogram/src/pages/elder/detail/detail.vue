<template>
  <view class="page">
    <view class="card">
      <view class="head">
        <view class="title">{{ d.title }}</view>
        <view class="status" :class="cls(d.status)">{{ st(d.status) }}</view>
      </view>
      <view class="line">需求编号: {{ d.demand_no }}</view>
      <view class="line">服务类别: {{ d.category }}</view>
      <view class="line">服务时间: {{ fmt(d.service_time) }}</view>
      <view class="line">服务地址: {{ d.address }}</view>
      <view class="line">需求描述: {{ d.description }}</view>
    </view>

    <view class="card" v-if="d.vol_name">
      <view class="sec-title">接单志愿者</view>
      <view class="vol-row">
        <view class="avatar">{{ (d.vol_name || '').charAt(0) }}</view>
        <view class="vol-info">
          <view class="vol-name">{{ d.vol_name }}</view>
          <view class="line">联系电话: {{ d.vol_phone }}</view>
          <view class="line">技能标签: {{ d.vol_skills }}</view>
        </view>
      </view>
    </view>

    <view class="card" v-if="d.status === 'DONE'">
      <view class="sec-title">服务评价</view>
      <view v-if="d.eval_score" class="eval">
        <view class="score">{{ d.eval_score }} 分</view>
        <view class="line">{{ d.eval_content }}</view>
      </view>
      <button v-else class="btn-primary" @click="goEval">去评价</button>
    </view>
  </view>
</template>

<script>
import { get, currentUser } from '@/utils/request.js'
import { statusText, statusClass, fmtTime } from '@/utils/common.js'

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
    st: statusText,
    cls: statusClass,
    fmt: fmtTime,
    goEval() {
      uni.navigateTo({ url: '/pages/elder/evaluate/evaluate?demandId=' + this.id })
    },
    async load() {
      const user = currentUser()
      if (!user) return uni.reLaunch({ url: '/pages/login/login' })
      this.d = await get('/api/elder/demand/' + this.id)
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
  align-items: center;
  margin-bottom: 12rpx;
}
.title {
  font-size: 32rpx;
  font-weight: 600;
  flex: 1;
}
.status {
  font-size: 22rpx;
  padding: 8rpx 18rpx;
  border-radius: 20rpx;
  &.st-warn {
    background: #fff3e0;
    color: #ff9f43;
  }
  &.st-primary {
    background: #e8f1ff;
    color: #2f7cf6;
  }
  &.st-ok {
    background: #e6f8ef;
    color: #34c77b;
  }
}
.line {
  font-size: 26rpx;
  color: #667085;
  margin-top: 12rpx;
  line-height: 1.6;
}
.sec-title {
  font-size: 28rpx;
  font-weight: 600;
  margin-bottom: 12rpx;
}
.vol-row {
  display: flex;
}
.avatar {
  width: 88rpx;
  height: 88rpx;
  background: #2f7cf6;
  color: #fff;
  border-radius: 50%;
  text-align: center;
  line-height: 88rpx;
  margin-right: 20rpx;
}
.vol-name {
  font-size: 30rpx;
  font-weight: 600;
}
.eval {
  background: #f7f9fc;
  border-radius: 14rpx;
  padding: 20rpx;
}
.score {
  font-size: 36rpx;
  color: #ff9f43;
  font-weight: 600;
}
.btn-primary {
  background: #2f7cf6;
  color: #fff;
  border-radius: 14rpx;
}
</style>
