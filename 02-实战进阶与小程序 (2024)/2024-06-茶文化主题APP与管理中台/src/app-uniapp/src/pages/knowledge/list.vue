<template>
  <view class="page">
    <view class="search-bar">
      <input class="search-input" v-model="keyword" placeholder="搜索茶文化知识" confirm-type="search" @confirm="loadData" />
    </view>
    <scroll-view scroll-x class="cat-scroll">
      <view class="cat-item" :class="{ active: !categoryId }" @click="selectCat(null)">全部</view>
      <view class="cat-item" v-for="c in categories" :key="c.categoryId"
            :class="{ active: categoryId === c.categoryId }" @click="selectCat(c.categoryId)">
        {{ c.categoryName }}
      </view>
    </scroll-view>
    <view class="knowledge-card" v-for="k in list" :key="k.knowledgeId" @click="goDetail(k.knowledgeId)">
      <image :src="imgUrl(k.coverImage)" class="k-img" mode="aspectFill" />
      <view class="k-info">
        <text class="k-title">{{ k.title }}</text>
        <text class="k-summary">{{ k.summary }}</text>
        <view class="k-meta">
          <text>{{ k.author }}</text>
          <text>{{ k.viewCount }} 阅读</text>
          <text>{{ k.likeCount }} 赞</text>
        </view>
      </view>
    </view>
    <view v-if="list.length === 0" class="empty">暂无内容</view>
  </view>
</template>

<script>
import { get, BASE_URL } from '@/utils/request.js'

export default {
  data() {
    return { list: [], categories: [], keyword: '', categoryId: null }
  },
  onShow() {
    this.loadCats()
    this.loadData()
  },
  methods: {
    async loadCats() {
      const res = await get('/api/public/categories', { type: 1 })
      this.categories = res.data || []
    },
    async loadData() {
      const params = { page: 1, size: 20 }
      if (this.keyword) params.keyword = this.keyword
      if (this.categoryId) params.categoryId = this.categoryId
      const res = await get('/api/public/knowledge', params)
      this.list = res.data.records || []
    },
    selectCat(id) { this.categoryId = id; this.loadData() },
    imgUrl(u) { return !u ? '' : (u.startsWith('http') ? u : BASE_URL + u) },
    goDetail(id) { uni.navigateTo({ url: '/pages/knowledge/detail?id=' + id }) }
  }
}
</script>

<style lang="scss" scoped>
.page { padding-bottom: 30rpx; }
.search-bar { padding: 20rpx; background: #fff; }
.search-input {
  background: #f5f7fa; border-radius: 32rpx; padding: 14rpx 28rpx; font-size: 26rpx;
}
.cat-scroll { background: #fff; padding: 16rpx 20rpx; white-space: nowrap; }
.cat-item {
  display: inline-block; padding: 10rpx 26rpx; margin-right: 16rpx;
  border-radius: 30rpx; font-size: 25rpx; background: #f5f7fa; color: #606266;
}
.cat-item.active { background: #2d6a4f; color: #fff; }
.knowledge-card {
  display: flex; background: #fff; margin: 16rpx; border-radius: 16rpx; padding: 20rpx;
}
.k-img { width: 220rpx; height: 160rpx; border-radius: 12rpx; flex-shrink: 0; }
.k-info { flex: 1; margin-left: 16rpx; display: flex; flex-direction: column; justify-content: space-between; }
.k-title { font-size: 28rpx; font-weight: 600; }
.k-summary {
  font-size: 22rpx; color: #909399;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;
}
.k-meta { display: flex; gap: 20rpx; font-size: 20rpx; color: #b0b3b8; }
.empty { text-align: center; padding: 100rpx 0; color: #909399; }
</style>
