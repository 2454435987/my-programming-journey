<template>
  <view class="register-page">
    <text class="page-title">注册茶文化账号</text>
    <view class="form-item">
      <input class="form-input" v-model="username" placeholder="用户名" />
    </view>
    <view class="form-item">
      <input class="form-input" v-model="nickname" placeholder="昵称" />
    </view>
    <view class="form-item">
      <input class="form-input" v-model="password" type="password" placeholder="密码" />
    </view>
    <view class="form-item">
      <input class="form-input" v-model="confirm" type="password" placeholder="确认密码" />
    </view>
    <button class="register-btn" @click="handleRegister">注 册</button>
  </view>
</template>

<script>
import { post } from '@/utils/request.js'

export default {
  data() {
    return { username: '', nickname: '', password: '', confirm: '' }
  },
  methods: {
    async handleRegister() {
      if (!this.username || !this.password) {
        uni.showToast({ title: '请填写用户名和密码', icon: 'none' })
        return
      }
      if (this.password !== this.confirm) {
        uni.showToast({ title: '两次密码不一致', icon: 'none' })
        return
      }
      const res = await post('/api/auth/app/register', {
        username: this.username, password: this.password, nickname: this.nickname
      })
      uni.setStorageSync('token', res.data.token)
      uni.setStorageSync('userInfo', res.data.user)
      uni.showToast({ title: '注册成功', icon: 'success' })
      setTimeout(() => uni.navigateBack(), 800)
    }
  }
}
</script>

<style lang="scss" scoped>
.register-page {
  padding: 60rpx 50rpx;
  min-height: 100vh;
  background: #f5f7fa;
}
.page-title {
  font-size: 40rpx;
  font-weight: 700;
  color: #2d6a4f;
  display: block;
  margin-bottom: 50rpx;
}
.form-item { margin-bottom: 24rpx; }
.form-input {
  background: #fff;
  border-radius: 12rpx;
  padding: 24rpx;
  font-size: 28rpx;
}
.register-btn {
  background: #2d6a4f;
  color: #fff;
  border-radius: 44rpx;
  font-size: 32rpx;
  margin-top: 40rpx;
  border: none;
}
</style>
