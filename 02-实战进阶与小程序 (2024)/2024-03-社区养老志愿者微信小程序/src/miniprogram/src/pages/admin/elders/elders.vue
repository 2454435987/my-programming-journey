<template>
  <view class="page">
    <view class="card" v-for="e in elders" :key="e.id">
      <view class="row">
        <view class="title">{{ e.elder_name }}</view>
        <view class="tag">{{ e.community }}</view>
      </view>
      <view class="meta">性别: {{ e.gender }} · 电话: {{ e.phone }}</view>
      <view class="meta">地址: {{ e.address }}</view>
      <view class="meta">健康备注: {{ e.health_note || '无' }}</view>
      <view class="meta">紧急联系人: {{ e.emergency_contact }} / {{ e.emergency_phone }}</view>
      <view class="meta">绑定状态: {{ e.bind_status == 1 ? '已绑定' : '未绑定' }} · 审核: {{ e.audit_status == 1 ? '通过' : e.audit_status == 2 ? '驳回' : '待审核' }}</view>
      <button class="btn-edit" @click="openEdit(e)">编辑健康备注</button>
    </view>

    <view class="mask" v-if="editing">
      <view class="dialog">
        <view class="d-title">编辑老人档案</view>
        <view class="label">健康备注</view>
        <textarea class="textarea" v-model="form.healthNote" />
        <view class="label">联系电话</view>
        <input class="input" v-model="form.phone" />
        <view class="btns">
          <button class="btn-plain" @click="editing = false">取消</button>
          <button class="btn-ok" @click="save">保存</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { get, post, currentUser } from '@/utils/request.js'

export default {
  data() {
    return {
      elders: [],
      editing: false,
      current: null,
      form: { healthNote: '', phone: '' }
    }
  },
  onShow() {
    this.load()
  },
  methods: {
    async load() {
      this.elders = await get('/api/admin/elder/list')
    },
    openEdit(e) {
      this.current = e
      this.form.healthNote = e.health_note || ''
      this.form.phone = e.phone || ''
      this.editing = true
    },
    async save() {
      const user = currentUser()
      await post('/api/admin/elder/update', {
        id: this.current.id,
        healthNote: this.form.healthNote,
        phone: this.form.phone,
        emergencyContact: this.current.emergency_contact,
        emergencyPhone: this.current.emergency_phone,
        address: this.current.address,
        adminId: user.id
      })
      uni.showToast({ title: '保存成功', icon: 'success' })
      this.editing = false
      this.load()
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
  border-radius: 18rpx;
  padding: 26rpx;
  margin-bottom: 18rpx;
}
.row {
  display: flex;
  justify-content: space-between;
}
.title {
  font-size: 30rpx;
  font-weight: 600;
}
.tag {
  font-size: 22rpx;
  color: #245f9e;
  background: #e8f1ff;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}
.meta {
  font-size: 24rpx;
  color: #8a94a6;
  margin-top: 10rpx;
}
.btn-edit {
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
  width: 82%;
  background: #fff;
  border-radius: 20rpx;
  padding: 32rpx;
}
.d-title {
  font-size: 32rpx;
  font-weight: 600;
  margin-bottom: 12rpx;
}
.label {
  font-size: 24rpx;
  color: #8a94a6;
  margin: 16rpx 0 8rpx;
}
.textarea {
  background: #f5f7fa;
  border-radius: 12rpx;
  padding: 16rpx;
  height: 140rpx;
  width: 100%;
  box-sizing: border-box;
}
.input {
  background: #f5f7fa;
  border-radius: 12rpx;
  padding: 16rpx;
}
.btns {
  display: flex;
  margin-top: 28rpx;
}
.btn-plain {
  flex: 1;
  background: #eef2f7;
  color: #667085;
  border-radius: 12rpx;
  margin-right: 16rpx;
}
.btn-ok {
  flex: 1;
  background: #245f9e;
  color: #fff;
  border-radius: 12rpx;
}
</style>
