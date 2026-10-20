<template>
  <view class="page">
    <view class="hero">
      <view class="logo">养</view>
      <view class="title">社区养老志愿者平台</view>
      <view class="sub">微信小程序 · 老人 / 志愿者 / 社区管理员</view>
    </view>

    <view class="panel">
      <view class="panel-title">选择身份登录</view>
      <view
        v-for="r in roles"
        :key="r.role"
        class="role-card"
        :class="{ active: form.role === r.role }"
        @click="form.role = r.role"
      >
        <view class="role-icon" :style="{ background: r.color }">{{ r.icon }}</view>
        <view class="role-info">
          <view class="role-name">{{ r.name }}</view>
          <view class="role-desc">{{ r.desc }}</view>
        </view>
        <view class="radio" :class="{ on: form.role === r.role }"></view>
      </view>

      <button class="btn-primary" @click="doLogin">微信授权登录</button>
      <view class="tip">演示环境: H5 端模拟微信授权; 真实小程序使用 wx.login 获取 code</view>
    </view>
  </view>
</template>

<script>
import { post, setCurrentUser } from '@/utils/request.js'

export default {
  data() {
    return {
      form: { role: 'elder' },
      roles: [
        { role: 'elder', name: '老人端', desc: '发布需求 · 紧急求助 · 服务评价', icon: '老', color: '#FF9F43' },
        { role: 'volunteer', name: '志愿者端', desc: '报名接单 · 签到签退 · 志愿积分', icon: '志', color: '#2F7CF6' },
        { role: 'admin', name: '管理员端', desc: '档案审核 · 任务分配 · 统计报表', icon: '管', color: '#34C77B' }
      ]
    }
  },
  methods: {
    async doLogin() {
      const role = this.form.role
      // #ifdef H5
      await this.loginWithCode('demo_' + role, role)
      // #endif
      // #ifdef MP-WEIXIN
      uni.login({
        provider: 'weixin',
        success: async () => {
          await this.loginWithCode('demo_' + role, role)
        },
        fail: () => {
          uni.showToast({ title: '微信登录失败', icon: 'none' })
        }
      })
      // #endif
    },
    async loginWithCode(code, role) {
      uni.showLoading({ title: '登录中...' })
      try {
        const data = await post('/api/auth/login', { code, role })
        setCurrentUser(data.user)
        uni.setStorageSync('currentProfile', data.profile || {})
        uni.hideLoading()
        uni.showToast({ title: '登录成功', icon: 'success' })
        const home = {
          elder: '/pages/elder/home/home',
          volunteer: '/pages/volunteer/home/home',
          admin: '/pages/admin/home/home'
        }[role] || '/pages/elder/home/home'
        setTimeout(() => {
          uni.reLaunch({ url: home })
        }, 400)
      } catch (e) {
        uni.hideLoading()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.page {
  min-height: 100vh;
  background: linear-gradient(180deg, #2f7cf6 0%, #4f96ff 28%, #f5f7fa 28.1%);
  padding: 40rpx 32rpx 60rpx;
}
.hero {
  padding: 60rpx 0 80rpx;
  text-align: center;
  color: #fff;
}
.logo {
  width: 140rpx;
  height: 140rpx;
  line-height: 140rpx;
  margin: 0 auto 24rpx;
  background: rgba(255, 255, 255, 0.22);
  border-radius: 36rpx;
  font-size: 64rpx;
  font-weight: 600;
}
.title {
  font-size: 44rpx;
  font-weight: 600;
}
.sub {
  margin-top: 12rpx;
  font-size: 26rpx;
  opacity: 0.9;
}
.panel {
  background: #fff;
  border-radius: 24rpx;
  padding: 32rpx;
  box-shadow: 0 12rpx 40rpx rgba(31, 78, 155, 0.12);
}
.panel-title {
  font-size: 32rpx;
  font-weight: 600;
  margin-bottom: 24rpx;
}
.role-card {
  display: flex;
  align-items: center;
  padding: 24rpx;
  border: 2rpx solid #e6ebf2;
  border-radius: 18rpx;
  margin-bottom: 20rpx;
  &.active {
    border-color: #2f7cf6;
    background: #eef5ff;
  }
}
.role-icon {
  width: 84rpx;
  height: 84rpx;
  border-radius: 20rpx;
  color: #fff;
  text-align: center;
  line-height: 84rpx;
  font-size: 36rpx;
  margin-right: 20rpx;
}
.role-info {
  flex: 1;
}
.role-name {
  font-size: 30rpx;
  font-weight: 600;
}
.role-desc {
  font-size: 24rpx;
  color: #8a94a6;
  margin-top: 6rpx;
}
.radio {
  width: 36rpx;
  height: 36rpx;
  border-radius: 50%;
  border: 3rpx solid #c8d2e0;
  &.on {
    border-color: #2f7cf6;
    background: #2f7cf6;
    box-shadow: inset 0 0 0 6rpx #fff;
  }
}
.btn-primary {
  margin-top: 16rpx;
  background: #2f7cf6;
  color: #fff;
  border-radius: 16rpx;
  font-size: 32rpx;
  height: 92rpx;
  line-height: 92rpx;
}
.tip {
  margin-top: 20rpx;
  font-size: 22rpx;
  color: #8a94a6;
  text-align: center;
}
</style>
