<template>
  <view class="page">
    <view class="card">
      <view class="label">服务类别</view>
      <view class="cats">
        <view
          v-for="c in categories"
          :key="c"
          class="cat"
          :class="{ on: form.category === c }"
          @click="form.category = c"
        >{{ c }}</view>
      </view>
    </view>

    <view class="card">
      <view class="label">需求标题</view>
      <input class="input" v-model="form.title" placeholder="如: 希望有人陪我聊天" maxlength="30" />
      <view class="label">需求描述</view>
      <textarea class="textarea" v-model="form.description" placeholder="请描述具体需求、注意事项等" maxlength="200" />
      <view class="label">期望服务时间</view>
      <picker mode="date" :start="today" @change="onDate">
        <view class="input picker">{{ form.date || '请选择日期' }}</view>
      </picker>
      <picker mode="time" @change="onTime">
        <view class="input picker">{{ form.time || '请选择时间' }}</view>
      </picker>
      <view class="label">服务地址</view>
      <input class="input" v-model="form.address" placeholder="如: 幸福里3号楼2单元501" />
      <view class="label">需求描述模板</view>
      <view class="tpls">
        <view class="tpl" v-for="t in templates" :key="t" @click="form.description = t">{{ t }}</view>
      </view>
    </view>

    <button class="btn-primary" @click="submit">提交服务申请</button>
  </view>
</template>

<script>
import { post, currentUser } from '@/utils/request.js'

export default {
  data() {
    return {
      categories: ['上门陪伴', '助餐', '助洁', '代购'],
      templates: [
        '子女不在身边,希望志愿者上门陪伴一小时左右',
        '腿脚不便,希望帮忙取餐并陪伴用餐',
        '家里需要打扫卫生、整理居室',
        '需要代购生活用品或常用药品'
      ],
      form: {
        category: '上门陪伴',
        title: '',
        description: '',
        date: '',
        time: '',
        address: '幸福里社区',
        lng: 116.397428,
        lat: 39.90923
      }
    }
  },
  computed: {
    today() {
      return new Date().toISOString().substring(0, 10)
    }
  },
  methods: {
    onDate(e) {
      this.form.date = e.detail.value
    },
    onTime(e) {
      this.form.time = e.detail.value
    },
    async submit() {
      const user = currentUser()
      if (!this.form.title) {
        uni.showToast({ title: '请填写需求标题', icon: 'none' })
        return
      }
      if (!this.form.date || !this.form.time) {
        uni.showToast({ title: '请选择服务时间', icon: 'none' })
        return
      }
      uni.showLoading({ title: '提交中...' })
      try {
        await post('/api/elder/demand/publish', {
          userId: user.id,
          category: this.form.category,
          title: this.form.title,
          description: this.form.description,
          serviceTime: this.form.date + ' ' + this.form.time + ':00',
          address: this.form.address,
          lng: this.form.lng,
          lat: this.form.lat
        })
        uni.hideLoading()
        uni.showToast({ title: '提交成功', icon: 'success' })
        setTimeout(() => uni.redirectTo({ url: '/pages/elder/demands/demands' }), 500)
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
  margin-bottom: 24rpx;
}
.label {
  font-size: 26rpx;
  color: #667085;
  margin: 20rpx 0 12rpx;
  &:first-child {
    margin-top: 0;
  }
}
.cats {
  display: flex;
  flex-wrap: wrap;
}
.cat {
  padding: 14rpx 32rpx;
  background: #f0f4fa;
  border-radius: 30rpx;
  margin: 0 16rpx 16rpx 0;
  font-size: 26rpx;
  color: #667085;
  &.on {
    background: #2f7cf6;
    color: #fff;
  }
}
.input {
  background: #f5f7fa;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  margin-bottom: 8rpx;
}
.picker {
  color: #333;
}
.textarea {
  background: #f5f7fa;
  border-radius: 12rpx;
  padding: 20rpx;
  width: 100%;
  height: 160rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}
.tpls {
  display: flex;
  flex-wrap: wrap;
}
.tpl {
  font-size: 22rpx;
  color: #2f7cf6;
  background: #eef5ff;
  border-radius: 10rpx;
  padding: 12rpx 16rpx;
  margin: 0 12rpx 12rpx 0;
}
.btn-primary {
  background: #2f7cf6;
  color: #fff;
  border-radius: 16rpx;
  font-size: 30rpx;
}
</style>
