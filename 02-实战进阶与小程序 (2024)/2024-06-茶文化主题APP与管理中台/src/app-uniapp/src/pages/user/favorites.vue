<template>
  <view class="page">
    <view class="card" v-for="f in list" :key="f.favoriteId">
      <text class="type">{{ {1:'知识',2:'商品',3:'教程',4:'帖子',5:'活动'}[f.targetType] }}</text>
      <text class="tid">ID: {{ f.targetId }}</text>
      <text class="time">{{ fmt(f.createdAt) }}</text>
    </view>
    <view v-if="list.length===0" class="empty">暂无收藏</view>
  </view>
</template>
<script>
import { get } from '@/utils/request.js'
export default {
  data() { return { list: [] } },
  onShow() { this.load() },
  methods: {
    async load() {
      const r = await get('/api/user/favorites')
      this.list = r.data || []
    },
    fmt(t) { return t ? String(t).replace('T', ' ').slice(0, 16) : '' }
  }
}
</script>
<style scoped>
.page{padding:16rpx}
.card{display:flex;align-items:center;background:#fff;border-radius:16rpx;padding:24rpx;margin-bottom:16rpx}
.type{font-size:24rpx;background:#e8f5e9;color:#2d6a4f;padding:6rpx 18rpx;border-radius:20rpx}
.tid{flex:1;margin-left:20rpx;font-size:25rpx;color:#606266}
.time{font-size:20rpx;color:#b0b3b8}
.empty{text-align:center;padding:100rpx 0;color:#909399}
</style>
