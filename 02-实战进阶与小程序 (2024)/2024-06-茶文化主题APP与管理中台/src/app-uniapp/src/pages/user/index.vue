<template>
  <view class="page">
    <!-- 用户信息 -->
    <view class="user-header">
      <view class="user-bg"></view>
      <view class="user-card" v-if="userInfo.userId">
        <view class="avatar">{{ (userInfo.nickname || '茶').charAt(0) }}</view>
        <view class="user-info">
          <text class="nickname">{{ userInfo.nickname }}</text>
          <text class="signature">{{ userInfo.signature || '这个人很懒，什么都没写' }}</text>
          <view class="user-level">
            <text class="level-tag">L{{ userInfo.level }}</text>
            <text class="points">{{ userInfo.points }} 积分</text>
          </view>
        </view>
      </view>
      <view class="user-card" v-else @click="goLogin">
        <view class="avatar">?</view>
        <view class="user-info">
          <text class="nickname">点击登录</text>
          <text class="signature">登录后享受更多茶文化服务</text>
        </view>
      </view>
    </view>

    <!-- 订单快捷 -->
    <view class="order-section">
      <view class="section-header">
        <text class="section-title">我的订单</text>
        <text class="section-more" @click="goPage('/pages/user/orders')">全部订单 ›</text>
      </view>
      <view class="order-nav">
        <view class="order-item" v-for="o in orderNavs" :key="o.label" @click="goOrders(o.status)">
          <text class="order-icon">{{ o.icon }}</text>
          <text class="order-label">{{ o.label }}</text>
        </view>
      </view>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-section">
      <view class="menu-item" v-for="m in menus" :key="m.label" @click="goPage(m.path)">
        <text class="menu-icon">{{ m.icon }}</text>
        <text class="menu-label">{{ m.label }}</text>
        <text class="menu-arrow">›</text>
      </view>
    </view>

    <view class="logout-btn" v-if="userInfo.userId" @click="handleLogout">退出登录</view>
  </view>
</template>

<script>
import { get } from '@/utils/request.js'

export default {
  data() {
    return {
      userInfo: {},
      orderNavs: [
        { label: '待付款', icon: '💰', status: 0 },
        { label: '待发货', icon: '📦', status: 1 },
        { label: '待收货', icon: '🚚', status: 2 },
        { label: '已完成', icon: '✅', status: 3 }
      ],
      menus: [
        { label: '我的收藏', icon: '⭐', path: '/pages/user/favorites' },
        { label: '购物车', icon: '🛒', path: '/pages/user/cart' },
        { label: '我的帖子', icon: '📝', path: '/pages/community/index' },
        { label: '消息通知', icon: '🔔', path: '/pages/message/index' },
        { label: '茶事活动', icon: '🎋', path: '/pages/activity/list' },
        { label: '茶艺教程', icon: '🍵', path: '/pages/course/list' }
      ]
    }
  },
  onShow() {
    this.loadProfile()
  },
  methods: {
    async loadProfile() {
      const token = uni.getStorageSync('token')
      if (!token) { this.userInfo = {}; return }
      try {
        const res = await get('/api/user/profile')
        this.userInfo = res.data || {}
        uni.setStorageSync('userInfo', this.userInfo)
      } catch (e) {
        this.userInfo = uni.getStorageSync('userInfo') || {}
      }
    },
    goLogin() { uni.navigateTo({ url: '/pages/user/login' }) },
    goPage(path) { uni.navigateTo({ url: path }) },
    goOrders(status) { uni.navigateTo({ url: '/pages/user/orders?status=' + status }) },
    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确认退出登录？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            this.userInfo = {}
            uni.showToast({ title: '已退出', icon: 'success' })
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.page { padding-bottom: 40rpx; }
.user-header {
  position: relative;
}
.user-bg {
  height: 280rpx;
  background: linear-gradient(135deg, #2d6a4f, #52b788);
}
.user-card {
  display: flex;
  align-items: center;
  background: #fff;
  margin: -100rpx 24rpx 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.08);
}
.avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: #2d6a4f;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48rpx;
  font-weight: 700;
}
.user-info {
  margin-left: 24rpx;
  flex: 1;
}
.nickname {
  font-size: 34rpx;
  font-weight: 700;
  display: block;
}
.signature {
  font-size: 22rpx;
  color: #909399;
  margin-top: 6rpx;
  display: block;
}
.user-level {
  display: flex;
  align-items: center;
  gap: 16rpx;
  margin-top: 10rpx;
}
.level-tag {
  background: linear-gradient(90deg, #e9c46a, #e76f51);
  color: #fff;
  font-size: 20rpx;
  padding: 4rpx 16rpx;
  border-radius: 20rpx;
}
.points {
  font-size: 22rpx;
  color: #2d6a4f;
}

.order-section {
  background: #fff;
  margin: 20rpx 24rpx;
  border-radius: 16rpx;
  padding: 24rpx;
}
.section-header {
  display: flex;
  justify-content: space-between;
}
.section-title {
  font-size: 30rpx;
  font-weight: 700;
}
.section-more {
  font-size: 24rpx;
  color: #909399;
}
.order-nav {
  display: flex;
  margin-top: 24rpx;
}
.order-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.order-icon { font-size: 44rpx; }
.order-label { font-size: 22rpx; color: #606266; margin-top: 8rpx; }

.menu-section {
  background: #fff;
  margin: 20rpx 24rpx;
  border-radius: 16rpx;
}
.menu-item {
  display: flex;
  align-items: center;
  padding: 28rpx 24rpx;
  border-bottom: 1rpx solid #f5f7fa;
}
.menu-icon { font-size: 36rpx; margin-right: 20rpx; }
.menu-label { flex: 1; font-size: 28rpx; }
.menu-arrow { color: #c0c4cc; font-size: 32rpx; }

.logout-btn {
  margin: 40rpx 24rpx;
  background: #fff;
  color: #e74c3c;
  text-align: center;
  padding: 24rpx;
  border-radius: 16rpx;
  font-size: 28rpx;
}
</style>
