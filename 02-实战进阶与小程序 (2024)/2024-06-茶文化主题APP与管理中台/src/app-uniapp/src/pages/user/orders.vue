<template>
  <view class="page">
    <view class="tabs">
      <text v-for="t in tabs" :key="t.label" class="tab" :class="{active: status===t.status}" @click="select(t.status)">{{ t.label }}</text>
    </view>
    <view class="card" v-for="o in list" :key="o.orderId">
      <view class="head">
        <text class="no">订单号：{{ o.orderNo }}</text>
        <text class="st">{{ {0:'待付款',1:'待发货',2:'待收货',3:'已完成',4:'已取消'}[o.status] }}</text>
      </view>
      <text class="addr">收货：{{ o.receiver }} {{ o.receiverPhone }}</text>
      <text class="addr">{{ o.receiverAddress }}</text>
      <view class="foot">
        <text class="time">{{ fmt(o.createdAt) }}</text>
        <text class="amt">¥{{ o.payAmount }}</text>
      </view>
    </view>
    <view v-if="list.length===0" class="empty">暂无订单</view>
  </view>
</template>
<script>
import { get } from '@/utils/request.js'
export default {
  data() {
    return {
      list: [], status: null,
      tabs: [{label:'全部',status:null},{label:'待付款',status:0},{label:'待发货',status:1},{label:'待收货',status:2},{label:'已完成',status:3}]
    }
  },
  onLoad(o) { if (o.status !== undefined) this.status = Number(o.status) },
  onShow() { this.load() },
  methods: {
    async load() {
      const params = { page: 1, size: 20 }
      if (this.status !== null && this.status !== undefined) params.status = this.status
      const r = await get('/api/user/orders', params)
      this.list = r.data.records || []
    },
    select(s) { this.status = s; this.load() },
    fmt(t) { return t ? String(t).replace('T', ' ').slice(0, 16) : '' }
  }
}
</script>
<style scoped>
.page{padding:16rpx}
.tabs{display:flex;background:#fff;border-radius:16rpx;padding:16rpx;margin-bottom:16rpx}
.tab{flex:1;text-align:center;font-size:25rpx;color:#606266;padding:10rpx 0}
.tab.active{color:#2d6a4f;font-weight:700;border-bottom:4rpx solid #2d6a4f}
.card{background:#fff;border-radius:16rpx;padding:24rpx;margin-bottom:16rpx}
.head{display:flex;justify-content:space-between;margin-bottom:10rpx}
.no{font-size:24rpx;color:#606266}
.st{font-size:24rpx;color:#2d6a4f;font-weight:600}
.addr{font-size:22rpx;color:#909399;display:block;line-height:1.7}
.foot{display:flex;justify-content:space-between;margin-top:14rpx;align-items:center}
.time{font-size:20rpx;color:#b0b3b8}
.amt{font-size:30rpx;color:#e74c3c;font-weight:700}
.empty{text-align:center;padding:100rpx 0;color:#909399}
</style>
