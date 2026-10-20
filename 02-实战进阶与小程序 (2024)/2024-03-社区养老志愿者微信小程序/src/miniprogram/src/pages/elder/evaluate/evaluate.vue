<template>
  <view class="page">
    <view class="card">
      <view class="label">选择已完成的服务</view>
      <picker :range="doneList" range-key="title" @change="onPick">
        <view class="input">{{ current ? current.title + ' (' + current.vol_name + ')' : '请选择服务' }}</view>
      </picker>
    </view>

    <view class="card">
      <view class="label">服务评分</view>
      <view class="stars">
        <view
          v-for="i in 5"
          :key="i"
          class="star"
          :class="{ on: i <= form.score }"
          @click="form.score = i"
        >★</view>
        <view class="score-text">{{ form.score }} 分</view>
      </view>
      <view class="label">评价内容</view>
      <textarea class="textarea" v-model="form.content" placeholder="请填写本次服务的简单评价" />
    </view>

    <button class="btn-primary" @click="submit">提交评价</button>

    <view class="card" v-if="myEvals.length">
      <view class="sec-title">我的历史评价</view>
      <view class="ev" v-for="e in myEvals" :key="e.id">
        <view class="ev-row">
          <view class="ev-title">{{ e.title }}</view>
          <view class="ev-score">{{ e.score }} 分</view>
        </view>
        <view class="ev-content">{{ e.content }}</view>
      </view>
    </view>
  </view>
</template>

<script>
import { get, post, currentUser } from '@/utils/request.js'

export default {
  data() {
    return {
      list: [],
      form: { score: 5, content: '' },
      current: null,
      demandId: 0
    }
  },
  computed: {
    doneList() {
      return this.list.filter((d) => d.status === 'DONE' && !d.eval_score)
    },
    myEvals() {
      return this.list.filter((d) => d.eval_score)
    }
  },
  onLoad(q) {
    if (q && q.id) {
      this.demandId = Number(q.id)
    }
  },
  onShow() {
    this.load()
  },
  methods: {
    onPick(e) {
      this.current = this.doneList[Number(e.detail.value)]
    },
    async load() {
      const user = currentUser()
      if (!user) return uni.reLaunch({ url: '/pages/login/login' })
      this.list = await get('/api/elder/demand/list', { userId: user.id })
      if (this.demandId) {
        this.current = this.doneList.find((d) => d.id === this.demandId) || this.current
      }
      if (!this.current && this.doneList.length) {
        this.current = this.doneList[0]
      }
    },
    async submit() {
      const user = currentUser()
      if (!this.current) {
        uni.showToast({ title: '请选择服务', icon: 'none' })
        return
      }
      uni.showLoading({ title: '提交中...' })
      try {
        await post('/api/elder/eval', {
          demandId: this.current.id,
          userId: user.id,
          score: this.form.score,
          content: this.form.content
        })
        uni.hideLoading()
        uni.showToast({ title: '评价成功', icon: 'success' })
        this.form.content = ''
        this.current = null
        this.load()
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
.label {
  font-size: 26rpx;
  color: #667085;
  margin-bottom: 12rpx;
}
.input {
  background: #f5f7fa;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
}
.stars {
  display: flex;
  align-items: center;
  margin: 10rpx 0 20rpx;
}
.star {
  font-size: 56rpx;
  color: #d8dee8;
  margin-right: 10rpx;
  &.on {
    color: #ff9f43;
  }
}
.score-text {
  margin-left: 16rpx;
  color: #ff9f43;
  font-size: 28rpx;
}
.textarea {
  background: #f5f7fa;
  border-radius: 12rpx;
  padding: 20rpx;
  width: 100%;
  height: 160rpx;
  box-sizing: border-box;
}
.btn-primary {
  background: #2f7cf6;
  color: #fff;
  border-radius: 14rpx;
}
.sec-title {
  font-size: 28rpx;
  font-weight: 600;
  margin-bottom: 12rpx;
}
.ev {
  border-top: 1rpx solid #eef2f7;
  padding: 16rpx 0;
}
.ev-row {
  display: flex;
  justify-content: space-between;
}
.ev-title {
  font-size: 26rpx;
}
.ev-score {
  color: #ff9f43;
  font-size: 26rpx;
}
.ev-content {
  font-size: 24rpx;
  color: #8a94a6;
  margin-top: 6rpx;
}
</style>
