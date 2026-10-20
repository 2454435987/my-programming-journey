<template>
  <view class="page">
    <image :src="imgUrl(detail.coverImage)" class="cover" mode="aspectFill" v-if="detail.coverImage" />
    <view class="content">
      <text class="title">{{ detail.title }}</text>
      <view class="meta">
        <text class="author">{{ detail.author }}</text>
        <text class="time">{{ formatTime(detail.createdAt) }}</text>
      </view>
      <view class="stats">
        <text class="stat">{{ detail.viewCount }} 阅读</text>
        <text class="stat">{{ detail.likeCount }} 点赞</text>
        <text class="stat">来源：{{ detail.source || '原创' }}</text>
      </view>
      <view class="summary-box">
        <text class="summary">{{ detail.summary }}</text>
      </view>
      <rich-text :nodes="detail.content" class="article"></rich-text>
    </view>
  </view>
</template>

<script>
import { get, BASE_URL } from '@/utils/request.js'

export default {
  data() { return { detail: {} } },
  onLoad(options) {
    this.loadDetail(options.id)
  },
  methods: {
    async loadDetail(id) {
      const res = await get('/api/public/knowledge/' + id)
      this.detail = res.data || {}
    },
    imgUrl(u) { return !u ? '' : (u.startsWith('http') ? u : BASE_URL + u) },
    formatTime(t) { return t ? String(t).replace('T', ' ').slice(0, 10) : '' }
  }
}
</script>

<style lang="scss" scoped>
.page { background: #fff; min-height: 100vh; }
.cover { width: 100%; height: 420rpx; }
.content { padding: 30rpx; }
.title { font-size: 40rpx; font-weight: 700; line-height: 1.5; display: block; }
.meta {
  display: flex; gap: 24rpx; margin-top: 16rpx;
}
.author { font-size: 24rpx; color: #2d6a4f; }
.time { font-size: 24rpx; color: #909399; }
.stats {
  display: flex; gap: 32rpx; margin-top: 16rpx; padding-bottom: 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
}
.stat { font-size: 22rpx; color: #b0b3b8; }
.summary-box {
  background: #f0f7f2; border-radius: 12rpx; padding: 20rpx; margin-top: 24rpx;
}
.summary { font-size: 26rpx; color: #2d6a4f; line-height: 1.7; }
.article { margin-top: 24rpx; line-height: 1.8; font-size: 28rpx; }
</style>
