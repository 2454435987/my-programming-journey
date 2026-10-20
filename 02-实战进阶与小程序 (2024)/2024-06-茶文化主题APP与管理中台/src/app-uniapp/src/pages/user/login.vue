<template>
  <view class="login-page">
    <view class="login-header">
      <text class="logo">🍵</text>
      <text class="app-name">茶文化</text>
      <text class="app-desc">品味千年茶韵，结交天下茶友</text>
    </view>
    <view class="login-form">
      <view class="form-item">
        <text class="form-label">账号</text>
        <input class="form-input" v-model="username" placeholder="请输入用户名" />
      </view>
      <view class="form-item">
        <text class="form-label">密码</text>
        <input class="form-input" v-model="password" type="password" placeholder="请输入密码" />
      </view>
      <button class="login-btn" @click="handleLogin">登 录</button>
      <view class="login-links">
        <text class="link" @click="goRegister">注册账号</text>
        <text class="link" @click="handleDemo">体验账号登录</text>
      </view>
    </view>
  </view>
</template>

<script>
import { post } from '@/utils/request.js'

export default {
  data() {
    return { username: '', password: '' }
  },
  methods: {
    async handleLogin() {
      if (!this.username || !this.password) {
        uni.showToast({ title: '请输入账号和密码', icon: 'none' })
        return
      }
      const res = await post('/api/auth/app/login', {
        username: this.username, password: this.password
      })
      uni.setStorageSync('token', res.data.token)
      uni.setStorageSync('userInfo', res.data.user)
      uni.showToast({ title: '登录成功', icon: 'success' })
      setTimeout(() => uni.navigateBack(), 800)
    },
    handleDemo() {
      this.username = 'tealover'
      this.password = '123456'
      this.handleLogin()
    },
    goRegister() {
      uni.navigateTo({ url: '/pages/user/register' })
    }
  }
}
</script>

<style lang="scss" scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #2d6a4f 0%, #f5f7fa 45%);
  padding: 0 50rpx;
}
.login-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 140rpx;
}
.logo { font-size: 100rpx; }
.app-name {
  font-size: 48rpx;
  font-weight: 700;
  color: #fff;
  margin-top: 20rpx;
}
.app-desc {
  font-size: 24rpx;
  color: rgba(255,255,255,0.8);
  margin-top: 10rpx;
}
.login-form {
  background: #fff;
  border-radius: 24rpx;
  padding: 50rpx 40rpx;
  margin-top: 80rpx;
  box-shadow: 0 8rpx 30rpx rgba(0,0,0,0.1);
}
.form-item {
  margin-bottom: 30rpx;
}
.form-label {
  font-size: 26rpx;
  color: #606266;
  margin-bottom: 12rpx;
  display: block;
}
.form-input {
  background: #f5f7fa;
  border-radius: 12rpx;
  padding: 22rpx 24rpx;
  font-size: 28rpx;
}
.login-btn {
  background: #2d6a4f;
  color: #fff;
  border-radius: 44rpx;
  font-size: 32rpx;
  margin-top: 20rpx;
  border: none;
}
.login-links {
  display: flex;
  justify-content: space-between;
  margin-top: 30rpx;
}
.link {
  color: #2d6a4f;
  font-size: 25rpx;
}
</style>
