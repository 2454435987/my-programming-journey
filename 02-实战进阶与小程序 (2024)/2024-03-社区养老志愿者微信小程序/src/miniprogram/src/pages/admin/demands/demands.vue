<template>
  <view class="page">
    <view class="tabs">
      <view
        v-for="t in tabs"
        :key="t.value"
        class="tab"
        :class="{ on: status === t.value }"
        @click="status = t.value"
      >{{ t.label }}</view>
    </view>

    <view class="card" v-for="d in filtered" :key="d.id">
      <view class="row">
        <view class="title">{{ d.title }}</view>
        <view class="tag">{{ st(d.status) }}</view>
      </view>
      <view class="meta">编号: {{ d.demand_no }} · 类别: {{ d.category }}</view>
      <view class="meta">老人: {{ d.elder_name }} · 志愿者: {{ d.vol_name || '未分配' }}</view>
      <view class="meta">服务时间: {{ fmt(d.service_time) }}</view>
      <view class="meta">审核: {{ d.audit_status == 1 ? '通过' : d.audit_status == 2 ? '驳回' : '待审核' }}</view>
      <view class="btns">
        <button class="btn-a" v-if="d.status === 'PENDING'" @click="openAssign(d)">分配志愿者</button>
        <button class="btn-b" v-if="d.audit_status == 0" @click="audit(d, 1)">审核通过</button>
        <button class="btn-c" v-if="d.audit_status == 0" @click="audit(d, 2)">驳回</button>
      </view>
    </view>

    <view class="mask" v-if="assigning">
      <view class="dialog">
        <view class="d-title">分配志愿者</view>
        <view class="meta">需求: {{ assigning.title }}</view>
        <view class="label">选择志愿者</view>
        <picker :range="vols" range-key="vol_name" @change="onVol">
          <view class="input">{{ pickVol ? pickVol.vol_name : '请选择' }}</view>
        </picker>
        <view class="btns">
          <button class="btn-plain" @click="assigning = null">取消</button>
          <button class="btn-ok" @click="doAssign">确认分配</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { get, post, currentUser } from '@/utils/request.js'
import { statusText, fmtTime } from '@/utils/common.js'

export default {
  data() {
    return {
      status: '',
      list: [],
      vols: [],
      assigning: null,
      pickVol: null,
      tabs: [
        { label: '全部', value: '' },
        { label: '待接单', value: 'PENDING' },
        { label: '服务中', value: 'SERVING' },
        { label: '已完成', value: 'DONE' }
      ]
    }
  },
  computed: {
    filtered() {
      return this.status ? this.list.filter((d) => d.status === this.status) : this.list
    }
  },
  onShow() {
    this.load()
  },
  methods: {
    st: statusText,
    fmt: fmtTime,
    async load() {
      this.list = await get('/api/admin/demand/list', this.status ? { status: this.status } : {})
      this.vols = await get('/api/admin/volunteer/list', { status: 1 })
    },
    async audit(d, pass) {
      const user = currentUser()
      await post('/api/admin/demand/audit', {
        id: d.id,
        pass,
        remark: pass === 1 ? '审核通过' : '信息不完整',
        adminId: user.id
      })
      uni.showToast({ title: '已处理', icon: 'success' })
      this.load()
    },
    openAssign(d) {
      this.assigning = d
      this.pickVol = null
    },
    onVol(e) {
      this.pickVol = this.vols[Number(e.detail.value)]
    },
    async doAssign() {
      if (!this.pickVol) return uni.showToast({ title: '请选择志愿者', icon: 'none' })
      const user = currentUser()
      await post('/api/admin/demand/assign', {
        demandId: this.assigning.id,
        volunteerId: this.pickVol.id,
        adminId: user.id
      })
      uni.showToast({ title: '分配成功', icon: 'success' })
      this.assigning = null
      this.load()
    }
  }
}
</script>

<style lang="scss" scoped>
.page {
  padding: 24rpx;
}
.tabs {
  display: flex;
  background: #fff;
  border-radius: 16rpx;
  padding: 10rpx;
  margin-bottom: 20rpx;
}
.tab {
  flex: 1;
  text-align: center;
  padding: 14rpx 0;
  font-size: 24rpx;
  color: #667085;
  border-radius: 12rpx;
  &.on {
    background: #245f9e;
    color: #fff;
  }
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
  background: #e8f1ff;
  color: #2f7cf6;
}
.meta {
  font-size: 24rpx;
  color: #8a94a6;
  margin-top: 10rpx;
}
.btns {
  display: flex;
  margin-top: 18rpx;
}
.btn-a,
.btn-b,
.btn-c,
.btn-plain,
.btn-ok {
  font-size: 24rpx;
  height: 68rpx;
  line-height: 68rpx;
  margin-right: 12rpx;
  border-radius: 12rpx;
}
.btn-a {
  background: #2f7cf6;
  color: #fff;
}
.btn-b {
  background: #34c77b;
  color: #fff;
}
.btn-c {
  background: #f4436c;
  color: #fff;
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
}
.label {
  font-size: 24rpx;
  color: #8a94a6;
  margin: 16rpx 0 8rpx;
}
.input {
  background: #f5f7fa;
  border-radius: 12rpx;
  padding: 18rpx;
}
.btn-plain {
  flex: 1;
  background: #eef2f7;
  color: #667085;
}
.btn-ok {
  flex: 1;
  background: #245f9e;
  color: #fff;
}
</style>
