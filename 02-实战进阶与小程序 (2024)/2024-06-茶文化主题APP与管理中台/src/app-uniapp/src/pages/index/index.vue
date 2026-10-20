<template>
  <view class="page">
    <!-- Banner 轮播 -->
    <swiper class="banner-swiper" circular autoplay :interval="4000" indicator-dots indicator-active-color="#2d6a4f">
      <swiper-item v-for="b in home.banners" :key="b.bannerId" @click="goBanner(b)">
        <image :src="imgUrl(b.imageUrl)" class="banner-img" mode="aspectFill" />
        <view class="banner-title">{{ b.title }}</view>
      </swiper-item>
    </swiper>

    <!-- 功能导航 -->
    <view class="nav-grid">
      <view class="nav-item" v-for="n in navs" :key="n.label" @click="goPage(n.path)">
        <view class="nav-icon" :style="{ background: n.color }">
          <text class="nav-emoji">{{ n.icon }}</text>
        </view>
        <text class="nav-label">{{ n.label }}</text>
      </view>
    </view>

    <!-- 热门茶品 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">热门茶品</text>
        <text class="section-more" @click="goPage('/pages/mall/index')">查看全部 ›</text>
      </view>
      <scroll-view scroll-x class="product-scroll">
        <view class="product-card" v-for="p in home.hotProducts" :key="p.productId" @click="goProduct(p.productId)">
          <image :src="imgUrl(p.coverImage)" class="product-img" mode="aspectFill" />
          <view class="product-info">
            <text class="product-name">{{ p.productName }}</text>
            <text class="product-price">¥{{ p.price }}</text>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 茶文化知识 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">茶文化知识</text>
        <text class="section-more" @click="goPage('/pages/knowledge/list')">更多 ›</text>
      </view>
      <view class="knowledge-card" v-for="k in home.newKnowledge" :key="k.knowledgeId" @click="goKnowledge(k.knowledgeId)">
        <image :src="imgUrl(k.coverImage)" class="knowledge-img" mode="aspectFill" />
        <view class="knowledge-info">
          <text class="knowledge-title">{{ k.title }}</text>
          <text class="knowledge-summary">{{ k.summary }}</text>
          <view class="knowledge-meta">
            <text class="meta-text">{{ k.author }}</text>
            <text class="meta-text">{{ k.viewCount }} 阅读</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 茶艺教程 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">茶艺教程</text>
        <text class="section-more" @click="goPage('/pages/course/list')">更多 ›</text>
      </view>
      <view class="course-row">
        <view class="course-card" v-for="c in home.hotCourses" :key="c.courseId" @click="goCourse(c.courseId)">
          <image :src="imgUrl(c.coverImage)" class="course-img" mode="aspectFill" />
          <text class="course-title">{{ c.title }}</text>
          <text class="course-meta">{{ c.duration }}分钟 · {{ ['','入门','进阶','高级'][c.level] }}</text>
        </view>
      </view>
    </view>

    <!-- 茶事活动 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">茶事活动</text>
        <text class="section-more" @click="goPage('/pages/activity/list')">更多 ›</text>
      </view>
      <view class="activity-card" v-for="a in home.activities" :key="a.activityId" @click="goActivity(a.activityId)">
        <image :src="imgUrl(a.coverImage)" class="activity-img" mode="aspectFill" />
        <view class="activity-info">
          <text class="activity-title">{{ a.title }}</text>
          <text class="activity-time">{{ formatTime(a.startTime) }} · {{ a.location }}</text>
          <view class="activity-tags">
            <text class="tag">{{ a.activityType }}</text>
            <text class="tag free" v-if="Number(a.price) === 0">免费</text>
            <text class="tag price" v-else>¥{{ a.price }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 社区热帖 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">社区热帖</text>
        <text class="section-more" @click="goPage('/pages/community/index')">更多 ›</text>
      </view>
      <view class="post-card" v-for="p in home.hotPosts" :key="p.postId" @click="goPost(p.postId)">
        <text class="post-title">{{ p.title }}</text>
        <text class="post-content">{{ p.content }}</text>
        <view class="post-meta">
          <text class="meta-text">{{ p.likeCount }} 赞</text>
          <text class="meta-text">{{ p.commentCount }} 评论</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { get, BASE_URL } from '@/utils/request.js'

export default {
  data() {
    return {
      home: { banners: [], hotProducts: [], newKnowledge: [], hotCourses: [], activities: [], hotPosts: [] },
      navs: [
        { label: '茶文化知识', icon: '📖', color: '#e8f5e9', path: '/pages/knowledge/list' },
        { label: '茶艺教程', icon: '🍵', color: '#fff3e0', path: '/pages/course/list' },
        { label: '茶事活动', icon: '🎋', color: '#e3f2fd', path: '/pages/activity/list' },
        { label: '消息通知', icon: '🔔', color: '#fce4ec', path: '/pages/message/index' }
      ]
    }
  },
  onShow() {
    this.loadData()
  },
  methods: {
    async loadData() {
      const res = await get('/api/public/home')
      this.home = res.data
    },
    imgUrl(u) {
      if (!u) return ''
      return u.startsWith('http') ? u : BASE_URL + u
    },
    formatTime(t) {
      return t ? String(t).replace('T', ' ').slice(0, 16) : ''
    },
    goPage(path) {
      uni.navigateTo({ url: path })
    },
    goBanner(b) {
      if (b.linkUrl && b.linkUrl.includes('/')) uni.navigateTo({ url: b.linkUrl })
    },
    goProduct(id) { uni.navigateTo({ url: '/pages/product/detail?id=' + id }) },
    goKnowledge(id) { uni.navigateTo({ url: '/pages/knowledge/detail?id=' + id }) },
    goCourse(id) { uni.navigateTo({ url: '/pages/course/detail?id=' + id }) },
    goActivity(id) { uni.navigateTo({ url: '/pages/activity/detail?id=' + id }) },
    goPost(id) { uni.navigateTo({ url: '/pages/community/publish?postId=' + id }) }
  }
}
</script>

<style lang="scss" scoped>
.page { padding-bottom: 30rpx; }
.banner-swiper {
  height: 340rpx;
  position: relative;
}
.banner-img {
  width: 100%;
  height: 340rpx;
}
.banner-title {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16rpx 24rpx;
  background: linear-gradient(transparent, rgba(0,0,0,0.55));
  color: #fff;
  font-size: 28rpx;
}
.nav-grid {
  display: flex;
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx 10rpx;
}
.nav-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.nav-icon {
  width: 88rpx;
  height: 88rpx;
  border-radius: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 12rpx;
}
.nav-emoji { font-size: 40rpx; }
.nav-label { font-size: 24rpx; color: #606266; }

.section {
  margin: 20rpx;
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
}
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}
.section-title {
  font-size: 32rpx;
  font-weight: 700;
  color: #303133;
}
.section-more {
  font-size: 24rpx;
  color: #2d6a4f;
}

.product-scroll {
  white-space: nowrap;
}
.product-card {
  display: inline-block;
  width: 240rpx;
  margin-right: 16rpx;
}
.product-img {
  width: 240rpx;
  height: 180rpx;
  border-radius: 12rpx;
}
.product-info {
  padding: 8rpx 4rpx;
}
.product-name {
  font-size: 24rpx;
  color: #303133;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.product-price {
  font-size: 28rpx;
  color: #e74c3c;
  font-weight: 700;
}

.knowledge-card {
  display: flex;
  padding: 16rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}
.knowledge-img {
  width: 200rpx;
  height: 140rpx;
  border-radius: 12rpx;
  flex-shrink: 0;
}
.knowledge-info {
  flex: 1;
  margin-left: 16rpx;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.knowledge-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #303133;
}
.knowledge-summary {
  font-size: 22rpx;
  color: #909399;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.knowledge-meta {
  display: flex;
  gap: 20rpx;
}
.meta-text { font-size: 20rpx; color: #b0b3b8; }

.course-row {
  display: flex;
  overflow-x: auto;
}
.course-card {
  width: 280rpx;
  margin-right: 16rpx;
  flex-shrink: 0;
}
.course-img {
  width: 280rpx;
  height: 170rpx;
  border-radius: 12rpx;
}
.course-title {
  font-size: 25rpx;
  color: #303133;
  display: block;
  margin-top: 8rpx;
}
.course-meta {
  font-size: 20rpx;
  color: #909399;
}

.activity-card {
  display: flex;
  padding: 16rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}
.activity-img {
  width: 220rpx;
  height: 150rpx;
  border-radius: 12rpx;
  flex-shrink: 0;
}
.activity-info {
  flex: 1;
  margin-left: 16rpx;
}
.activity-title {
  font-size: 27rpx;
  font-weight: 600;
  color: #303133;
}
.activity-time {
  font-size: 21rpx;
  color: #909399;
  margin-top: 8rpx;
  display: block;
}
.activity-tags {
  margin-top: 12rpx;
  display: flex;
  gap: 12rpx;
}
.tag {
  font-size: 20rpx;
  padding: 4rpx 14rpx;
  border-radius: 20rpx;
  background: #e8f5e9;
  color: #2d6a4f;
}
.tag.free { background: #fff3e0; color: #e67e22; }
.tag.price { background: #fce4ec; color: #e74c3c; }

.post-card {
  padding: 16rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}
.post-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #303133;
  display: block;
}
.post-content {
  font-size: 24rpx;
  color: #909399;
  margin-top: 8rpx;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.post-meta {
  margin-top: 10rpx;
  display: flex;
  gap: 24rpx;
}
</style>
