<template>
  <view class="page">
    <view class="tabs">
      <view class="tab" :class="{ on: tab === 'vol' }" @click="switchTab('vol')">志愿者审核</view>
      <view class="tab" :class="{ on: tab === 'elder' }" @click="switchTab('elder')">老人绑定审核</view>
    </view>

    <block v-if="tab === 'vol'">
      <view class="card" v-for="v in vols" :key="v.id">
        <view class="row">
          <view class="title">{{ v.vol_name }}</view>
          <view class="tag" :class="v.audit_status == 1 ? 'ok' : v.audit_status == 2 ? 'no' : ''">
            {{ v.audit_status == 1 ? '已通过' : v.audit_status == 2 ? '已驳回' : '待审核' }}
          </view>
        </view>
        <view class="meta">性别: {{ v.gender }} · 电话: {{ v.phone }}</view>
        <view class="meta">身份证: {{ v.id_card }}</view>
        <view class="meta">技能: {{ v.skills }}</view>
        <view class="meta">可服务时段: {{ v.serve_period }} · 区域: {{ v.service_area }}</view>
        <view class="btns" v-if="v.audit_status == 0">
          <button class="btn-ok" @click="auditVol(v, 1)">通过</button>
          <button class="btn-no" @click="auditVol(v, 2)">驳回</button>
        </view>
      </view>
      <view v-if="!vols.length" class="empty">暂无待审核志愿者</view>
    </block>

    <block v-else>
      <view class="card" v-for="e in elders" :key="e.id">
        <view class="row">
          <view class="title">{{ e.elder_name }}</view>
          <view class="tag" :class="e.audit_status == 1 ? 'ok' : e.audit_status == 2 ? 'no' : ''">
            {{ e.audit_status == 1 ? '已通过' : e.audit_status == 2 ? '已驳回' : '待审核' }}
          </view>
        </view>
        <view class="meta">性别: {{ e.gender }} · 电话: {{ e.phone }}</view>
        <view class="meta">社区: {{ e.community }} · 地址: {{ e.address }}</view>
        <view class="meta">健康备注: {{ e.health_note }}</view>
        <view class="btns" v-if="e.audit_status == 0">
          <button class="btn-ok" @click="auditElder(e, 1)">通过</button>
          <button class="btn-no" @click="auditElder(e, 2)">驳回</button>
        </view>
      </view>
      <view v-if="!elders.length" class="empty">暂无待审核老人</view>
    </block>
  </view>
</template>

<script>
import { get, post, currentUser } from '@/utils/request.js'

export default {
  data() {
    return {
      tab: 'vol',
      vols: [],
      elders: []
    }
  },
  onShow() {
    this.load()
  },
  methods: {
    switchTab(t) {
      this.tab = t
      this.load()
    },
    async load() {
      const user = currentUser()
      if (!user) return uni.reLaunch({ url: '/pages/login/login' })
      if (this.tab === 'vol') {
        this.vols = (await get('/api/admin/volunteer/list', { status: 0 })).concat(
          await get('/api/admin/volunteer/list', { status: 1 })
        )
      } else {
        this.elders = (await get('/api/admin/elder/list', { status: 0 })).concat(
          await get('/api/admin/elder/list', { status: 1 })
        )
      }
    },
    async auditVol(v, pass) {
      const user = currentUser()
      await post('/api/admin/volunteer/audit', {
        id: v.id,
        pass,
        remark: pass === 1 ? '实名认证通过' : '信息不完整',
        adminId: user.id
      })
      uni.showToast({ title: pass === 1 ? '已通过' : '已驳回', icon: 'success' })
      this.load()
    },
    async auditElder(e, pass) {
      const user = currentUser()
      await post('/api/admin/elder/audit', {
        id: e.id,
        pass,
        remark: pass === 1 ? '信息核实无误' : '信息不符',
        adminId: user.id
      })
      uni.showToast({ title: pass === 1 ? '已通过' : '已驳回', icon: 'success' })
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
  padding: 16rpx 0;
  font-size: 26rpx;
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
  align-items: center;
}
.title {
  font-size: 30rpx;
  font-weight: 600;
}
.tag {
  font-size: 22rpx;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  background: #fff3e0;
  color: #ff9f43;
  &.ok {
    background: #e6f8ef;
    color: #34c77b;
  }
  &.no {
    background: #fde8ee;
    color: #f4436c;
  }
}
.meta {
  font-size: 24rpx;
  color: #8a94a6;
  margin-top: 10rpx;
}
.btns {
  display: flex;
  margin-top: 20rpx;
}
.btn-ok {
  flex: 1;
  background: #34c77b;
  color: #fff;
  border-radius: 12rpx;
  margin-right: 16rpx;
  font-size: 26rpx;
  height: 72rpx;
  line-height: 72rpx;
}
.btn-no {
  flex: 1;
  background: #f4436c;
  color: #fff;
  border-radius: 12rpx;
  font-size: 26rpx;
  height: 72rpx;
  line-height: 72rpx;
}
.empty {
  text-align: center;
  color: #8a94a6;
  padding: 100rpx 0;
}
</style>
