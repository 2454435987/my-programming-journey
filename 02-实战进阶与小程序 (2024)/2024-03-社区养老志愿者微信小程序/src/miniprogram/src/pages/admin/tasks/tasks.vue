<template>
  <view class="page">
    <button class="btn-publish" @click="showForm = true">+ 发布志愿任务</button>

    <view class="card" v-for="t in tasks" :key="t.id">
      <view class="row">
        <view class="title">{{ t.title }}</view>
        <view class="tag">{{ t.status === 'OPEN' ? '招募中' : t.status === 'MATCHED' ? '已分配' : '已完成' }}</view>
      </view>
      <view class="meta">类别: {{ t.category }} · 编号: {{ t.task_no }}</view>
      <view class="meta">{{ t.description }}</view>
      <view class="meta">时间: {{ fmt(t.service_time) }} · 地点: {{ t.address }}</view>
      <view class="meta">需要{{ t.required_count }}人 / 已报名{{ t.joined_count }}人</view>
      <button class="btn-small" @click="viewSignups(t)">查看报名并匹配</button>
    </view>

    <view class="mask" v-if="showForm">
      <view class="dialog">
        <view class="d-title">发布志愿任务</view>
        <view class="label">任务标题</view>
        <input class="input" v-model="form.title" />
        <view class="label">任务类别</view>
        <picker :range="cats" @change="onCat">
          <view class="input">{{ form.category }}</view>
        </picker>
        <view class="label">任务描述</view>
        <textarea class="textarea" v-model="form.description" />
        <view class="label">服务时间</view>
        <input class="input" v-model="form.serviceTime" placeholder="2026-10-20 09:00:00" />
        <view class="label">服务地点</view>
        <input class="input" v-model="form.address" />
        <view class="label">需要人数</view>
        <input class="input" v-model="form.requiredCount" type="number" />
        <view class="btns">
          <button class="btn-plain" @click="showForm = false">取消</button>
          <button class="btn-ok" @click="publish">发布</button>
        </view>
      </view>
    </view>

    <view class="mask" v-if="signups.length">
      <view class="dialog">
        <view class="d-title">报名列表(点选匹配)</view>
        <view class="su" v-for="s in signups" :key="s.id">
          <view class="su-name">{{ s.vol_name }} · {{ s.skills }}</view>
          <view class="meta">服务{{ (s.total_minutes / 60).toFixed(1) }}h · 积分{{ s.points }} · {{ s.status === 'ACCEPTED' ? '已通过' : s.status === 'REJECTED' ? '未通过' : '待处理' }}</view>
          <view class="btns" v-if="s.status === 'PENDING'">
            <button class="btn-ok mini" @click="approve(s, 1)">通过</button>
            <button class="btn-plain mini" @click="approve(s, 2)">拒绝</button>
          </view>
        </view>
        <button class="btn-plain" @click="signups = []">关闭</button>
      </view>
    </view>
  </view>
</template>

<script>
import { get, post, currentUser } from '@/utils/request.js'
import { fmtTime } from '@/utils/common.js'

export default {
  data() {
    return {
      tasks: [],
      showForm: false,
      signups: [],
      cats: ['上门陪伴', '助餐', '助洁', '代购', '其他'],
      form: {
        title: '',
        category: '上门陪伴',
        description: '',
        serviceTime: '2026-10-25 09:00:00',
        address: '幸福里社区服务中心',
        requiredCount: 3
      }
    }
  },
  onShow() {
    this.load()
  },
  methods: {
    fmt: fmtTime,
    onCat(e) {
      this.form.category = this.cats[Number(e.detail.value)]
    },
    async load() {
      this.tasks = await get('/api/volunteer/task/list')
    },
    async publish() {
      const user = currentUser()
      await post('/api/admin/task/publish', {
        adminId: user.id,
        title: this.form.title,
        category: this.form.category,
        description: this.form.description,
        requireSkills: this.form.category,
        serviceTime: this.form.serviceTime,
        address: this.form.address,
        requiredCount: Number(this.form.requiredCount) || 1
      })
      uni.showToast({ title: '发布成功', icon: 'success' })
      this.showForm = false
      this.load()
    },
    async viewSignups(t) {
      this.signups = await get('/api/admin/task/signups', { taskId: t.id })
    },
    async approve(s, pass) {
      const user = currentUser()
      await post('/api/admin/task/signup/approve', { signupId: s.id, pass, adminId: user.id })
      uni.showToast({ title: '已处理', icon: 'success' })
      this.signups = await get('/api/admin/task/signups', { taskId: s.task_id })
      this.load()
    }
  }
}
</script>

<style lang="scss" scoped>
.page {
  padding: 24rpx;
}
.btn-publish {
  background: #245f9e;
  color: #fff;
  border-radius: 14rpx;
  margin-bottom: 20rpx;
  font-size: 28rpx;
}
.card {
  background: #fff;
  border-radius: 18rpx;
  padding: 26rpx;
  margin-bottom: 18rpx;
}
.row {
  display: flex;
  justify-content: space-between;
}
.title {
  font-size: 28rpx;
  font-weight: 600;
  flex: 1;
}
.tag {
  font-size: 22rpx;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  background: #e6f8ef;
  color: #34c77b;
}
.meta {
  font-size: 24rpx;
  color: #8a94a6;
  margin-top: 10rpx;
}
.btn-small {
  margin-top: 18rpx;
  background: #e8f1ff;
  color: #245f9e;
  border-radius: 12rpx;
  font-size: 26rpx;
  height: 72rpx;
  line-height: 72rpx;
}
.mask {
  position: fixed;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
}
.dialog {
  width: 84%;
  max-height: 80vh;
  overflow-y: auto;
  background: #fff;
  border-radius: 20rpx;
  padding: 32rpx;
}
.d-title {
  font-size: 32rpx;
  font-weight: 600;
}
.label {
  font-size: 24rpx;
  color: #8a94a6;
  margin: 14rpx 0 8rpx;
}
.input {
  background: #f5f7fa;
  border-radius: 12rpx;
  padding: 18rpx;
}
.textarea {
  background: #f5f7fa;
  border-radius: 12rpx;
  padding: 18rpx;
  height: 130rpx;
  width: 100%;
  box-sizing: border-box;
}
.btns {
  display: flex;
  margin-top: 20rpx;
}
.btn-plain {
  flex: 1;
  background: #eef2f7;
  color: #667085;
  border-radius: 12rpx;
  margin-right: 12rpx;
}
.btn-ok {
  flex: 1;
  background: #245f9e;
  color: #fff;
  border-radius: 12rpx;
}
.mini {
  flex: none;
  padding: 0 24rpx;
  height: 60rpx;
  line-height: 60rpx;
  font-size: 22rpx;
}
.su {
  border-top: 1rpx solid #f0f3f8;
  padding: 16rpx 0;
}
.su-name {
  font-size: 26rpx;
  font-weight: 600;
}
</style>
