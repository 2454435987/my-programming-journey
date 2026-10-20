<template>
  <view class="page">
    <view class="panel">
      <view class="big-btn" @click="sendSOS">
        <view class="big-icon">急</view>
        <view class="big-title">一键紧急求助</view>
        <view class="big-desc">点击后自动推送管理员与就近志愿者</view>
      </view>

      <view class="label">求助类型</view>
      <view class="types">
        <view
          v-for="t in types"
          :key="t.value"
          class="type"
          :class="{ on: form.helpType === t.value }"
          @click="form.helpType = t.value"
        >{{ t.label }}</view>
      </view>

      <view class="label">情况描述</view>
      <textarea class="textarea" v-model="form.description" placeholder="请简要描述当前情况" />

      <view class="loc">
        <view class="line">当前位置: 幸福里社区(演示)</view>
        <view class="line">经度: {{ form.lng }} 纬度: {{ form.lat }}</view>
      </view>
    </view>

    <view class="card" v-if="last">
      <view class="sec-title">最近求助记录</view>
      <view class="line">状态: {{ last.status }} · 类型: {{ last.help_type }}</view>
      <view class="line">时间: {{ fmt(last.create_time) }}</view>
      <view class="line">通知就近志愿者ID: {{ last.nearby_vol_ids }}</view>
      <view class="line" v-if="last.resolve_note">处理结果: {{ last.resolve_note }}</view>
    </view>
  </view>
</template>

<script>
import { post, get, currentUser } from '@/utils/request.js'
import { fmtTime } from '@/utils/common.js'

export default {
  data() {
    return {
      types: [
        { label: '一键求助', value: 'SOS' },
        { label: '跌倒', value: 'FALL' },
        { label: '身体不适', value: 'MEDICAL' },
        { label: '其他', value: 'OTHER' }
      ],
      form: {
        helpType: 'SOS',
        description: '',
        address: '幸福里3号楼2单元501',
        lng: 116.397428,
        lat: 39.90923
      },
      last: null
    }
  },
  onShow() {
    this.loadLast()
  },
  methods: {
    fmt: fmtTime,
    async loadLast() {
      try {
        const list = await get('/api/elder/sos/list')
        this.last = list && list.length ? list[0] : null
      } catch (e) {
        /* ignore */
      }
    },
    async sendSOS() {
      const user = currentUser()
      if (!user) return uni.reLaunch({ url: '/pages/login/login' })
      uni.showLoading({ title: '发送中...' })
      try {
        const r = await post('/api/elder/sos', {
          userId: user.id,
          helpType: this.form.helpType,
          description: this.form.description || '一键紧急求助',
          address: this.form.address,
          lng: this.form.lng,
          lat: this.form.lat
        })
        uni.hideLoading()
        uni.showModal({
          title: '求助已发出',
          content: '已推送管理员与就近志愿者(ID: ' + (r.nearby_vol_ids || '') + '),请保持电话畅通',
          showCancel: false
        })
        this.loadLast()
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
.panel {
  background: #fff;
  border-radius: 20rpx;
  padding: 28rpx;
}
.big-btn {
  background: linear-gradient(180deg, #f4436c, #ff6b6b);
  border-radius: 24rpx;
  padding: 56rpx 24rpx;
  text-align: center;
  color: #fff;
}
.big-icon {
  width: 120rpx;
  height: 120rpx;
  line-height: 120rpx;
  background: rgba(255, 255, 255, 0.25);
  border-radius: 50%;
  margin: 0 auto 20rpx;
  font-size: 52rpx;
}
.big-title {
  font-size: 36rpx;
  font-weight: 600;
}
.big-desc {
  font-size: 24rpx;
  opacity: 0.9;
  margin-top: 8rpx;
}
.label {
  font-size: 26rpx;
  color: #667085;
  margin: 24rpx 0 12rpx;
}
.types {
  display: flex;
}
.type {
  flex: 1;
  text-align: center;
  padding: 16rpx 0;
  background: #f0f4fa;
  margin-right: 12rpx;
  border-radius: 12rpx;
  font-size: 24rpx;
  color: #667085;
  &:last-child {
    margin-right: 0;
  }
  &.on {
    background: #f4436c;
    color: #fff;
  }
}
.textarea {
  background: #f5f7fa;
  border-radius: 12rpx;
  padding: 20rpx;
  width: 100%;
  height: 140rpx;
  box-sizing: border-box;
}
.loc {
  margin-top: 20rpx;
  background: #f7f9fc;
  border-radius: 12rpx;
  padding: 18rpx;
}
.line {
  font-size: 24rpx;
  color: #8a94a6;
  margin-top: 8rpx;
}
.card {
  margin-top: 24rpx;
  background: #fff;
  border-radius: 20rpx;
  padding: 28rpx;
}
.sec-title {
  font-size: 28rpx;
  font-weight: 600;
  margin-bottom: 8rpx;
}
</style>
