<template>
  <view class="page">
    <!-- 话题标签 -->
    <scroll-view scroll-x class="topic-scroll">
      <view class="topic-item" :class="{ active: topic === '' }" @click="selectTopic('')">全部</view>
      <view class="topic-item" v-for="t in topics" :key="t" :class="{ active: topic === t }" @click="selectTopic(t)">
        #{{ t }}
      </view>
    </scroll-view>

    <!-- 发帖入口 -->
    <view class="publish-entry" @click="goPublish">
      <text class="publish-text">分享你的茶生活...</text>
      <text class="publish-btn">发帖</text>
    </view>

    <!-- 帖子列表 -->
    <view class="post-card" v-for="p in list" :key="p.postId" @click="goDetail(p.postId)">
      <view class="post-header">
        <view class="avatar">{{ (p.title || '茶').charAt(0) }}</view>
        <view class="post-user">
          <text class="username">茶友 {{ p.userId }}</text>
          <text class="post-time">{{ formatTime(p.createdAt) }}</text>
        </view>
        <text class="essence" v-if="p.isEssence === 1">精华</text>
      </view>
      <text class="post-title">{{ p.title }}</text>
      <text class="post-content">{{ p.content }}</text>
      <view class="post-images" v-if="p.images">
        <image v-for="(img, idx) in p.images.split(',').slice(0, 3)" :key="idx"
               :src="imgUrl(img.trim())" class="post-img" mode="aspectFill" />
      </view>
      <view class="post-footer">
        <text class="footer-item">{{ p.viewCount }} 浏览</text>
        <text class="footer-item">{{ p.likeCount }} 点赞</text>
        <text class="footer-item">{{ p.commentCount }} 评论</text>
      </view>
    </view>
  </view>
</template>

<script>
import { get, BASE_URL } from '@/utils/request.js'

export default {
  data() {
    return {
      list: [], topic: '',
      topics: ['茶席分享', '普洱仓储', '武夷岩茶', '新手入门', '茶点搭配', '岩茶体验']
    }
  },
  onShow() { this.loadData() },
  methods: {
    async loadData() {
      const params = { page: 1, size: 20 }
      if (this.topic) params.topic = this.topic
      const res = await get('/api/public/posts', params)
      this.list = res.data.records || []
    },
    selectTopic(t) { this.topic = t; this.loadData() },
    imgUrl(u) { return !u ? '' : (u.startsWith('http') ? u : BASE_URL + u) },
    formatTime(t) { return t ? String(t).replace('T', ' ').slice(0, 16) : '' },
    goDetail(id) { uni.navigateTo({ url: '/pages/community/publish?postId=' + id }) },
    goPublish() { uni.navigateTo({ url: '/pages/community/publish' }) }
  }
}
</script>

<style lang="scss" scoped>
.page { padding-bottom: 30rpx; }
.topic-scroll {
  background: #fff;
  padding: 20rpx;
  white-space: nowrap;
}
.topic-item {
  display: inline-block;
  padding: 10rpx 26rpx;
  margin-right: 16rpx;
  border-radius: 30rpx;
  font-size: 24rpx;
  background: #f5f7fa;
  color: #606266;
}
.topic-item.active {
  background: #2d6a4f;
  color: #fff;
}
.publish-entry {
  display: flex;
  align-items: center;
  background: #fff;
  margin: 16rpx;
  padding: 24rpx;
  border-radius: 16rpx;
}
.publish-text {
  flex: 1;
  color: #b0b3b8;
  font-size: 26rpx;
}
.publish-btn {
  background: #2d6a4f;
  color: #fff;
  padding: 10rpx 32rpx;
  border-radius: 30rpx;
  font-size: 24rpx;
}
.post-card {
  background: #fff;
  margin: 0 16rpx 16rpx;
  border-radius: 16rpx;
  padding: 24rpx;
}
.post-header {
  display: flex;
  align-items: center;
}
.avatar {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  background: #2d6a4f;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
}
.post-user {
  flex: 1;
  margin-left: 16rpx;
}
.username {
  font-size: 26rpx;
  font-weight: 600;
  display: block;
}
.post-time {
  font-size: 20rpx;
  color: #b0b3b8;
}
.essence {
  background: #e74c3c;
  color: #fff;
  font-size: 20rpx;
  padding: 4rpx 16rpx;
  border-radius: 20rpx;
}
.post-title {
  font-size: 30rpx;
  font-weight: 700;
  margin-top: 16rpx;
  display: block;
}
.post-content {
  font-size: 26rpx;
  color: #606266;
  margin-top: 10rpx;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.post-images {
  display: flex;
  gap: 12rpx;
  margin-top: 16rpx;
}
.post-img {
  width: 210rpx;
  height: 160rpx;
  border-radius: 10rpx;
}
.post-footer {
  display: flex;
  gap: 32rpx;
  margin-top: 16rpx;
}
.footer-item {
  font-size: 22rpx;
  color: #b0b3b8;
}
</style>
