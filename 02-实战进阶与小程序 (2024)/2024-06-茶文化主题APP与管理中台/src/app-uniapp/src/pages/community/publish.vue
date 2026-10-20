<template>
  <view class="page">
    <!-- 发帖表单 -->
    <view v-if="!postId">
      <view class="form-item">
        <input class="input" v-model="form.title" placeholder="标题" />
      </view>
      <view class="form-item">
        <textarea class="textarea" v-model="form.content" placeholder="分享你的茶生活..." :maxlength="500" />
      </view>
      <view class="form-item">
        <input class="input" v-model="form.topic" placeholder="话题标签（可选）" />
      </view>
      <button class="btn" @click="publish">发布帖子</button>
    </view>
    <!-- 帖子详情 -->
    <view v-else>
      <text class="title">{{ post.title }}</text>
      <view class="meta">
        <text class="topic">{{ post.topic }}</text>
        <text class="time">{{ fmt(post.createdAt) }}</text>
      </view>
      <text class="content">{{ post.content }}</text>
      <view class="images" v-if="post.images">
        <image v-for="(img, i) in post.images.split(',')" :key="i" :src="imgUrl(img.trim())" class="img" mode="aspectFill" />
      </view>
      <view class="stats">
        <text class="stat">{{ post.viewCount }} 浏览</text>
        <text class="stat">{{ post.likeCount }} 点赞</text>
        <text class="stat">{{ post.commentCount }} 评论</text>
      </view>
      <view class="comment-title">评论</view>
      <view class="comment" v-for="c in comments" :key="c.commentId">
        <text class="c-user">茶友 {{ c.userId }}</text>
        <text class="c-content">{{ c.content }}</text>
        <text class="c-time">{{ fmt(c.createdAt) }}</text>
      </view>
      <view class="comment-form">
        <input class="input" v-model="newComment" placeholder="写下你的评论..." />
        <button class="btn small" @click="addComment">评论</button>
      </view>
    </view>
  </view>
</template>
<script>
import { get, post, BASE_URL } from '@/utils/request.js'
export default {
  data() {
    return { postId: null, form: { title: '', content: '', topic: '' }, post: {}, comments: [], newComment: '' }
  },
  onLoad(o) {
    if (o.postId) { this.postId = o.postId; this.loadPost() }
  },
  methods: {
    async loadPost() {
      const r = await get('/api/public/posts/' + this.postId)
      this.post = r.data || {}
      const cr = await get('/api/user/posts/' + this.postId + '/comments')
      this.comments = cr.data || []
    },
    async publish() {
      if (!uni.getStorageSync('token')) return uni.navigateTo({ url: '/pages/user/login' })
      if (!this.form.title || !this.form.content) return uni.showToast({ title: '请填写标题和内容', icon: 'none' })
      await post('/api/user/posts', this.form)
      uni.showToast({ title: '发布成功', icon: 'success' })
      setTimeout(() => uni.navigateBack(), 800)
    },
    async addComment() {
      if (!uni.getStorageSync('token')) return uni.navigateTo({ url: '/pages/user/login' })
      if (!this.newComment) return
      await post('/api/user/posts/' + this.postId + '/comments', { content: this.newComment })
      this.newComment = ''
      this.loadPost()
    },
    imgUrl(u) { return !u ? '' : (u.startsWith('http') ? u : BASE_URL + u) },
    fmt(t) { return t ? String(t).replace('T', ' ').slice(0, 16) : '' }
  }
}
</script>
<style scoped>
.page{padding:24rpx;background:#f5f7fa;min-height:100vh}
.form-item{background:#fff;border-radius:16rpx;padding:16rpx;margin-bottom:20rpx}
.input{font-size:28rpx;padding:12rpx 8rpx}
.textarea{width:100%;min-height:260rpx;font-size:28rpx;padding:12rpx 8rpx}
.btn{background:#2d6a4f;color:#fff;border-radius:44rpx;font-size:30rpx;border:none;margin-top:20rpx}
.btn.small{flex:1;margin:0 0 0 16rpx;font-size:25rpx;padding:0;height:68rpx;line-height:68rpx}
.title{font-size:38rpx;font-weight:700;display:block}
.meta{display:flex;gap:20rpx;margin-top:12rpx}
.topic{font-size:22rpx;color:#2d6a4f}
.time{font-size:22rpx;color:#b0b3b8}
.content{font-size:28rpx;line-height:1.8;margin-top:20rpx;display:block}
.images{display:flex;flex-wrap:wrap;gap:12rpx;margin-top:20rpx}
.img{width:220rpx;height:180rpx;border-radius:12rpx}
.stats{display:flex;gap:32rpx;margin-top:24rpx;padding:16rpx 0;border-top:1rpx solid #eee}
.stat{font-size:22rpx;color:#b0b3b8}
.comment-title{font-size:30rpx;font-weight:700;margin-top:24rpx}
.comment{background:#fff;border-radius:12rpx;padding:20rpx;margin-top:16rpx}
.c-user{font-size:24rpx;font-weight:600;color:#2d6a4f;display:block}
.c-content{font-size:26rpx;margin-top:8rpx;display:block}
.c-time{font-size:20rpx;color:#b0b3b8;margin-top:8rpx;display:block}
.comment-form{display:flex;align-items:center;margin-top:24rpx;background:#fff;border-radius:16rpx;padding:16rpx}
.comment-form .input{flex:1}
</style>
