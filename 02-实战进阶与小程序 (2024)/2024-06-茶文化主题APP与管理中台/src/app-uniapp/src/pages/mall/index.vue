<template>
  <view class="page">
    <!-- 搜索 -->
    <view class="search-bar">
      <input class="search-input" v-model="keyword" placeholder="搜索茶品" confirm-type="search" @confirm="loadData" />
      <text class="search-btn" @click="loadData">搜索</text>
    </view>
    <!-- 分类 -->
    <scroll-view scroll-x class="cat-scroll">
      <view class="cat-item" :class="{ active: categoryId === 0 }" @click="selectCat(0)">全部</view>
      <view class="cat-item" v-for="c in categories" :key="c.categoryId"
            :class="{ active: categoryId === c.categoryId }" @click="selectCat(c.categoryId)">
        {{ c.categoryName }}
      </view>
    </scroll-view>
    <!-- 排序 -->
    <view class="sort-bar">
      <text class="sort-item" :class="{ active: sort === '' }" @click="selectSort('')">综合</text>
      <text class="sort-item" :class="{ active: sort === 'sales' }" @click="selectSort('sales')">销量</text>
      <text class="sort-item" :class="{ active: sort === 'price_asc' }" @click="selectSort('price_asc')">价格↑</text>
      <text class="sort-item" :class="{ active: sort === 'price_desc' }" @click="selectSort('price_desc')">价格↓</text>
    </view>
    <!-- 商品列表 -->
    <view class="product-grid">
      <view class="product-item" v-for="p in list" :key="p.productId" @click="goDetail(p.productId)">
        <image :src="imgUrl(p.coverImage)" class="product-img" mode="aspectFill" />
        <view class="product-info">
          <text class="product-name">{{ p.productName }}</text>
          <text class="product-subtitle">{{ p.subtitle }}</text>
          <view class="product-bottom">
            <text class="product-price">¥{{ p.price }}</text>
            <text class="product-sales">已售{{ p.sales }}</text>
          </view>
        </view>
      </view>
    </view>
    <view v-if="list.length === 0" class="empty">暂无商品</view>
  </view>
</template>

<script>
import { get, BASE_URL } from '@/utils/request.js'

export default {
  data() {
    return { list: [], categories: [], keyword: '', categoryId: 0, sort: '', page: 1 }
  },
  onShow() {
    this.loadCats()
    this.loadData()
  },
  methods: {
    async loadCats() {
      const res = await get('/api/public/categories', { type: 2 })
      this.categories = res.data || []
    },
    async loadData() {
      const params = { page: this.page, size: 20 }
      if (this.keyword) params.keyword = this.keyword
      if (this.categoryId) params.categoryId = this.categoryId
      if (this.sort) params.sort = this.sort
      const res = await get('/api/public/products', params)
      this.list = res.data.records || []
    },
    selectCat(id) { this.categoryId = id; this.loadData() },
    selectSort(s) { this.sort = s; this.loadData() },
    imgUrl(u) { return !u ? '' : (u.startsWith('http') ? u : BASE_URL + u) },
    goDetail(id) { uni.navigateTo({ url: '/pages/product/detail?id=' + id }) }
  }
}
</script>

<style lang="scss" scoped>
.page { padding-bottom: 30rpx; }
.search-bar {
  display: flex;
  padding: 20rpx;
  background: #fff;
}
.search-input {
  flex: 1;
  background: #f5f7fa;
  border-radius: 32rpx;
  padding: 14rpx 28rpx;
  font-size: 26rpx;
}
.search-btn {
  margin-left: 16rpx;
  color: #2d6a4f;
  font-size: 28rpx;
  line-height: 64rpx;
}
.cat-scroll {
  background: #fff;
  padding: 16rpx 20rpx;
  white-space: nowrap;
  margin-top: 2rpx;
}
.cat-item {
  display: inline-block;
  padding: 10rpx 28rpx;
  margin-right: 16rpx;
  border-radius: 30rpx;
  font-size: 25rpx;
  background: #f5f7fa;
  color: #606266;
}
.cat-item.active {
  background: #2d6a4f;
  color: #fff;
}
.sort-bar {
  display: flex;
  background: #fff;
  padding: 16rpx 20rpx;
  margin-top: 2rpx;
}
.sort-item {
  margin-right: 40rpx;
  font-size: 26rpx;
  color: #606266;
}
.sort-item.active {
  color: #2d6a4f;
  font-weight: 700;
}
.product-grid {
  display: flex;
  flex-wrap: wrap;
  padding: 16rpx;
}
.product-item {
  width: calc(50% - 12rpx);
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  margin: 0 6rpx 16rpx;
}
.product-img {
  width: 100%;
  height: 300rpx;
}
.product-info {
  padding: 16rpx;
}
.product-name {
  font-size: 26rpx;
  font-weight: 600;
  color: #303133;
  display: block;
}
.product-subtitle {
  font-size: 21rpx;
  color: #909399;
  display: block;
  margin-top: 6rpx;
}
.product-bottom {
  display: flex;
  justify-content: space-between;
  margin-top: 12rpx;
  align-items: center;
}
.product-price {
  color: #e74c3c;
  font-size: 30rpx;
  font-weight: 700;
}
.product-sales {
  font-size: 20rpx;
  color: #b0b3b8;
}
.empty {
  text-align: center;
  padding: 100rpx 0;
  color: #909399;
}
</style>
