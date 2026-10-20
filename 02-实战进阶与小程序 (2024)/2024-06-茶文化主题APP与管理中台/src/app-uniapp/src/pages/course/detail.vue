<template>
  <view class="page">
    <image :src="imgUrl(detail.coverImage)" class="cover" mode="aspectFill" />
    <view class="body">
      <text class="title">{{ detail.title }}</text>
      <view class="meta">
        <text class="lv">{{ ['','入门','进阶','高级'][detail.level] }}</text>
        <text class="t">{{ detail.duration }}分钟 · {{ detail.viewCount }}次学习</text>
      </view>
      <text class="sum">{{ detail.summary }}</text>
      <view class="rich"><rich-text :nodes="detail.content"></rich-text></view>
    </view>
  </view>
</template>
<script>
import { get, BASE_URL } from '@/utils/request.js'
export default {
  data() { return { detail: {} } },
  onLoad(o) { this.load(o.id) },
  methods: {
    async load(id) { const r = await get('/api/public/courses/' + id); this.detail = r.data || {} },
    imgUrl(u) { return !u ? '' : (u.startsWith('http') ? u : BASE_URL + u) }
  }
}
</script>
<style scoped>
.page{background:#fff;min-height:100vh}
.cover{width:100%;height:400rpx}
.body{padding:30rpx}
.title{font-size:36rpx;font-weight:700;display:block}
.meta{display:flex;gap:20rpx;margin-top:16rpx;align-items:center}
.lv{font-size:22rpx;background:#e8f5e9;color:#2d6a4f;padding:6rpx 18rpx;border-radius:20rpx}
.t{font-size:22rpx;color:#909399}
.sum{font-size:26rpx;color:#606266;margin-top:24rpx;display:block;line-height:1.7}
.rich{margin-top:24rpx;line-height:1.8;font-size:28rpx}
</style>
