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

    <view v-if="filtered.length === 0" class="empty">暂无相关需求</view>

    <view class="card" v-for="d in filtered" :key="d.id" @click="goDetail(d.id)">
      <view class="row">
        <view class="title">{{ d.title }}</view>
        <view class="status" :class="cls(d.status)">{{ st(d.status) }}</view>
      </view>
      <view class="meta">类别: {{ d.category }} · 编号: {{ d.demand_no }}</view>
      <view class="meta">服务时间: {{ fmt(d.service_time) }}</view>
      <view class="meta">地址: {{ d.address }}</view>
      <view class="vol" v-if="d.vol_name">
        <view class="avatar">{{ (d.vol_name || '').charAt(0) }}</view>
        <view>
          <view class="vol-name">接单志愿者: {{ d.vol_name }}</view>
          <view class="meta">电话: {{ d.vol_phone || '-' }} · 技能: {{ d.vol_skills || '-' }}</view>
        </view>
      </view>
      <view class="vol empty-vol" v-else>暂未有志愿者接单</view>
    </view>
  </view>
</template>

<script>
import { get, currentUser } from '@/utils/request.js'
import { statusText, statusClass, fmtTime } from '@/utils/common.js'

export default {
  data() {
    return {
      current: 'ALL',
      list: [],
      tabs: [
        { label: '全部', value: 'ALL' },
        { label: '待接单', value: 'PENDING' },
        { label: '服务中', value: 'SERVING' },
        { label: '已完成', value: 'DONE' }
      ]
    }
  },
  computed: {
    filtered() {
      return this.current === 'ALL' ? this.list : this.list.filter((d) => d.status === this.current)
    }
  },
  onShow() {
    this.load()
  },
  methods: {
    st: statusText,
    cls: statusClass,
    fmt: fmtTime,
    goDetail(id) {
      uni.navigateTo({ url: '/pages/elder/detail/detail?id=' + id })
    },
    async load() {
      const user = currentUser()
      if (!user) return uni.reLaunch({ url: '/pages/login/login' })
      this.list = await get('/api/elder/demand/list', { userId: user.id })
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
    font-weight: 600;
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
  margin-right: 16rpx;
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
.meta {
  font-size: 24rpx;
  color: #8a94a6;
  margin-top: 10rpx;
}
.vol {
  margin-top: 18rpx;
  background: #f7f9fc;
  border-radius: 14rpx;
  padding: 18rpx;
  display: flex;
  align-items: center;
}
.empty-vol {
  color: #8a94a6;
  font-size: 24rpx;
}
.avatar {
  width: 72rpx;
  height: 72rpx;
  background: #2f7cf6;
  color: #fff;
  border-radius: 50%;
  text-align: center;
  line-height: 72rpx;
  margin-right: 16rpx;
  font-size: 28rpx;
}
.vol-name {
  font-size: 26rpx;
  font-weight: 600;
}
.empty {
  text-align: center;
  color: #8a94a6;
  padding: 120rpx 0;
}
</style>
