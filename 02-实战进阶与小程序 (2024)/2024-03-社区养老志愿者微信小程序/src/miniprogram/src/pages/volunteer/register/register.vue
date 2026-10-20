<template>
  <view class="page">
    <view class="tip-card">实名认证信息将由社区管理员审核,审核通过后方可接单</view>

    <view class="card">
      <view class="label">真实姓名</view>
      <input class="input" v-model="form.volName" placeholder="请输入真实姓名" />
      <view class="label">性别</view>
      <view class="types">
        <view class="type" :class="{ on: form.gender === '男' }" @click="form.gender = '男'">男</view>
        <view class="type" :class="{ on: form.gender === '女' }" @click="form.gender = '女'">女</view>
      </view>
      <view class="label">联系电话</view>
      <input class="input" v-model="form.phone" placeholder="请输入手机号" type="number" />
      <view class="label">身份证号</view>
      <input class="input" v-model="form.idCard" placeholder="用于实名认证" />
    </view>

    <view class="card">
      <view class="label">技能标签(可多选)</view>
      <view class="skills">
        <view
          v-for="s in skillOptions"
          :key="s"
          class="skill"
          :class="{ on: form.skills.includes(s) }"
          @click="toggleSkill(s)"
        >{{ s }}</view>
      </view>
      <view class="label">可服务时段</view>
      <view class="skills">
        <view
          v-for="s in periodOptions"
          :key="s"
          class="skill"
          :class="{ on: form.servePeriod === s }"
          @click="form.servePeriod = s"
        >{{ s }}</view>
      </view>
      <view class="label">可服务区域</view>
      <picker :range="areas" @change="onArea">
        <view class="input">{{ form.serviceArea }}</view>
      </picker>
    </view>

    <button class="btn-primary" @click="submit">提交实名认证</button>
  </view>
</template>

<script>
import { post, currentUser } from '@/utils/request.js'

export default {
  data() {
    return {
      skillOptions: ['上门陪伴', '助餐', '助洁', '代购', '急救', '心理疏导'],
      periodOptions: ['工作日上午', '工作日下午', '工作日晚间', '周末全天'],
      areas: ['幸福里社区', '和谐家园社区'],
      form: {
        volName: '',
        gender: '男',
        phone: '',
        idCard: '',
        skills: ['上门陪伴'],
        servePeriod: '周末全天',
        serviceArea: '幸福里社区',
        lng: 116.398,
        lat: 39.909
      }
    }
  },
  methods: {
    toggleSkill(s) {
      const i = this.form.skills.indexOf(s)
      if (i >= 0) {
        this.form.skills.splice(i, 1)
      } else {
        this.form.skills.push(s)
      }
    },
    onArea(e) {
      this.form.serviceArea = this.areas[Number(e.detail.value)]
    },
    async submit() {
      const user = currentUser()
      if (!this.form.volName || !this.form.idCard) {
        uni.showToast({ title: '请填写姓名与身份证号', icon: 'none' })
        return
      }
      uni.showLoading({ title: '提交中...' })
      try {
        await post('/api/volunteer/register', {
          userId: user.id,
          volName: this.form.volName,
          gender: this.form.gender,
          phone: this.form.phone,
          idCard: this.form.idCard,
          skills: this.form.skills.join(','),
          servePeriod: this.form.servePeriod,
          serviceArea: this.form.serviceArea,
          lng: this.form.lng,
          lat: this.form.lat
        })
        uni.hideLoading()
        uni.showToast({ title: '已提交审核', icon: 'success' })
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
.tip-card {
  background: #eef5ff;
  color: #2f7cf6;
  border-radius: 14rpx;
  padding: 20rpx;
  font-size: 24rpx;
  margin-bottom: 20rpx;
}
.card {
  background: #fff;
  border-radius: 20rpx;
  padding: 28rpx;
  margin-bottom: 20rpx;
}
.label {
  font-size: 26rpx;
  color: #667085;
  margin: 18rpx 0 12rpx;
  &:first-child {
    margin-top: 0;
  }
}
.input {
  background: #f5f7fa;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
}
.types {
  display: flex;
}
.type {
  width: 160rpx;
  text-align: center;
  padding: 16rpx 0;
  background: #f0f4fa;
  border-radius: 12rpx;
  margin-right: 16rpx;
  color: #667085;
  &.on {
    background: #2f7cf6;
    color: #fff;
  }
}
.skills {
  display: flex;
  flex-wrap: wrap;
}
.skill {
  padding: 12rpx 26rpx;
  background: #f0f4fa;
  border-radius: 30rpx;
  margin: 0 14rpx 14rpx 0;
  font-size: 24rpx;
  color: #667085;
  &.on {
    background: #2f7cf6;
    color: #fff;
  }
}
.btn-primary {
  background: #2f7cf6;
  color: #fff;
  border-radius: 14rpx;
}
</style>
