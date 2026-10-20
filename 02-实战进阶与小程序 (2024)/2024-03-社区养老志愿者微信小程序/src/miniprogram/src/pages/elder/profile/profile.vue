<template>
  <view class="page">
    <view class="card head-card">
      <view class="avatar">{{ (profile.elder_name || '老').charAt(0) }}</view>
      <view class="info">
        <view class="name">{{ profile.elder_name || '-' }}</view>
        <view class="sub">{{ profile.gender || '-' }} · {{ profile.community || '-' }}</view>
        <view class="badge">{{ profile.audit_status == 1 ? '已认证' : '待审核' }}</view>
      </view>
    </view>

    <view class="card">
      <view class="sec-title">基础信息</view>
      <view class="row"><view class="k">姓名</view><view class="v">{{ profile.elder_name }}</view></view>
      <view class="row"><view class="k">性别</view><view class="v">{{ profile.gender }}</view></view>
      <view class="row"><view class="k">出生日期</view><view class="v">{{ (profile.birth_date || '').substring(0, 10) }}</view></view>
      <view class="row"><view class="k">所属社区</view><view class="v">{{ profile.community }}</view></view>
      <view class="row"><view class="k">居住地址</view><view class="v">{{ profile.address }}</view></view>
    </view>

    <view class="card">
      <view class="sec-title">健康备注</view>
      <view class="health">{{ profile.health_note || '暂无' }}</view>
    </view>

    <view class="card">
      <view class="sec-title">联系方式(可更新)</view>
      <view class="label">联系电话</view>
      <input class="input" v-model="form.phone" placeholder="本人电话" />
      <view class="label">紧急联系人</view>
      <input class="input" v-model="form.emergencyContact" placeholder="如: 张伟(儿子)" />
      <view class="label">紧急联系人电话</view>
      <input class="input" v-model="form.emergencyPhone" placeholder="紧急联系人电话" />
      <button class="btn-primary" @click="save">保存更新</button>
    </view>
  </view>
</template>

<script>
import { get, post, currentUser } from '@/utils/request.js'

export default {
  data() {
    return {
      profile: {},
      form: { phone: '', emergencyContact: '', emergencyPhone: '' }
    }
  },
  onShow() {
    this.load()
  },
  methods: {
    async load() {
      const user = currentUser()
      if (!user) return uni.reLaunch({ url: '/pages/login/login' })
      this.profile = (await get('/api/elder/profile', { userId: user.id })) || {}
      this.form.phone = this.profile.phone || ''
      this.form.emergencyContact = this.profile.emergency_contact || ''
      this.form.emergencyPhone = this.profile.emergency_phone || ''
    },
    async save() {
      const user = currentUser()
      uni.showLoading({ title: '保存中...' })
      try {
        this.profile = await post('/api/auth/update-contact', {
          userId: user.id,
          phone: this.form.phone,
          emergencyContact: this.form.emergencyContact,
          emergencyPhone: this.form.emergencyPhone
        })
        uni.hideLoading()
        uni.showToast({ title: '更新成功', icon: 'success' })
      } catch (e) {
        uni.hideLoading()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.page {
  padding: 24rpx;
}
.card {
  background: #fff;
  border-radius: 20rpx;
  padding: 28rpx;
  margin-bottom: 20rpx;
}
.head-card {
  display: flex;
  align-items: center;
}
.avatar {
  width: 110rpx;
  height: 110rpx;
  background: #ff9f43;
  color: #fff;
  border-radius: 50%;
  text-align: center;
  line-height: 110rpx;
  font-size: 44rpx;
  margin-right: 24rpx;
}
.name {
  font-size: 34rpx;
  font-weight: 600;
}
.sub {
  font-size: 24rpx;
  color: #8a94a6;
  margin-top: 6rpx;
}
.badge {
  display: inline-block;
  margin-top: 10rpx;
  font-size: 22rpx;
  color: #34c77b;
  background: #e6f8ef;
  padding: 4rpx 16rpx;
  border-radius: 8rpx;
}
.sec-title {
  font-size: 28rpx;
  font-weight: 600;
  margin-bottom: 12rpx;
}
.row {
  display: flex;
  padding: 14rpx 0;
  border-bottom: 1rpx solid #f0f3f8;
}
.k {
  width: 180rpx;
  color: #8a94a6;
  font-size: 26rpx;
}
.v {
  flex: 1;
  font-size: 26rpx;
}
.health {
  background: #fff8ec;
  color: #b8813a;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 26rpx;
  line-height: 1.6;
}
.label {
  font-size: 24rpx;
  color: #8a94a6;
  margin: 16rpx 0 8rpx;
}
.input {
  background: #f5f7fa;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
}
.btn-primary {
  margin-top: 28rpx;
  background: #2f7cf6;
  color: #fff;
  border-radius: 14rpx;
}
</style>
