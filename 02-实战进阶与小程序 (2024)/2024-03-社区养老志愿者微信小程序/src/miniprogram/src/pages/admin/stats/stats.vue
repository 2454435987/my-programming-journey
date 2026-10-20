<template>
  <view class="page">
    <view class="kpis">
      <view class="kpi" v-for="k in kpis" :key="k.label">
        <view class="k-num">{{ k.value }}</view>
        <view class="k-label">{{ k.label }}</view>
      </view>
    </view>

    <view class="card">
      <view class="sec-title">服务需求类别分布</view>
      <view class="bar-row" v-for="c in stats.categoryStat" :key="c.category">
        <view class="bar-label">{{ c.category }}</view>
        <view class="bar-track">
          <view class="bar-fill" :style="{ width: pct(c.cnt, maxCategory) }"></view>
        </view>
        <view class="bar-val">{{ c.cnt }}</view>
      </view>
    </view>

    <view class="card">
      <view class="sec-title">需求状态分布</view>
      <view class="bar-row" v-for="s in statusList" :key="s.label">
        <view class="bar-label">{{ s.label }}</view>
        <view class="bar-track">
          <view class="bar-fill" :class="s.cls" :style="{ width: pct(s.cnt, maxStatus) }"></view>
        </view>
        <view class="bar-val">{{ s.cnt }}</view>
      </view>
    </view>

    <view class="card">
      <view class="sec-title">月度服务时长(分钟)</view>
      <view class="bar-row" v-for="m in stats.monthlyHours" :key="m.month">
        <view class="bar-label">{{ m.month }}</view>
        <view class="bar-track">
          <view class="bar-fill fill-green" :style="{ width: pct(m.minutes, maxMinutes) }"></view>
        </view>
        <view class="bar-val">{{ m.minutes }}</view>
      </view>
    </view>

    <view class="card">
      <view class="sec-title">志愿者积分排行</view>
      <view class="rank" v-for="(v, i) in stats.volunteerRank" :key="v.id">
        <view class="rank-no" :class="'no' + (i + 1)">{{ i + 1 }}</view>
        <view class="rank-name">{{ v.vol_name }}</view>
        <view class="rank-score">{{ v.points }} 分 / {{ (v.total_minutes / 60).toFixed(1) }}h</view>
      </view>
    </view>

    <view class="card">
      <view class="sec-title">评价与社区分布</view>
      <view class="bar-row">
        <view class="bar-label">平均评分</view>
        <view class="bar-track">
          <view class="bar-fill fill-orange" :style="{ width: (Number(stats.avgScore) / 5 * 100) + '%' }"></view>
        </view>
        <view class="bar-val">{{ Number(stats.avgScore).toFixed(2) }}</view>
      </view>
      <view class="bar-row" v-for="c in stats.communityStat" :key="c.community">
        <view class="bar-label">{{ c.community }}</view>
        <view class="bar-track">
          <view class="bar-fill" :style="{ width: pct(c.cnt, maxCommunity) }"></view>
        </view>
        <view class="bar-val">{{ c.cnt }}</view>
      </view>
    </view>
  </view>
</template>

<script>
import { get } from '@/utils/request.js'

export default {
  data() {
    return {
      stats: {
        categoryStat: [],
        statusStat: [],
        monthlyHours: [],
        volunteerRank: [],
        communityStat: []
      }
    }
  },
  computed: {
    kpis() {
      return [
        { label: '需求总数', value: this.stats.demandTotal },
        { label: '已完成', value: this.stats.demandDone },
        { label: '服务时长', value: ((this.stats.totalHours || 0) / 60).toFixed(1) + 'h' },
        { label: '评价数', value: this.stats.evalCount },
        { label: '紧急求助', value: this.stats.sosTotal },
        { label: '志愿者', value: this.stats.volunteerCount }
      ]
    },
    statusList() {
      const map = { PENDING: '待接单', SERVING: '服务中', DONE: '已完成', CANCELLED: '已取消' }
      const cls = { PENDING: 'fill-orange', SERVING: 'fill-blue', DONE: 'fill-green' }
      return (this.stats.statusStat || []).map((s) => ({
        label: map[s.status] || s.status,
        cnt: s.cnt,
        cls: cls[s.status] || ''
      }))
    },
    maxCategory() {
      return Math.max(...(this.stats.categoryStat || []).map((c) => c.cnt), 1)
    },
    maxStatus() {
      return Math.max(...(this.stats.statusStat || []).map((c) => c.cnt), 1)
    },
    maxMinutes() {
      return Math.max(...(this.stats.monthlyHours || []).map((c) => c.minutes), 1)
    },
    maxCommunity() {
      return Math.max(...(this.stats.communityStat || []).map((c) => c.cnt), 1)
    }
  },
  onShow() {
    this.load()
  },
  methods: {
    pct(v, max) {
      return Math.round((Number(v) / max) * 100) + '%'
    },
    async load() {
      this.stats = await get('/api/admin/stats')
    }
  }
}
</script>

<style lang="scss" scoped>
.page {
  padding: 24rpx;
}
.kpis {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}
.kpi {
  width: 31%;
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx 8rpx;
  text-align: center;
  margin-bottom: 16rpx;
  box-sizing: border-box;
}
.k-num {
  font-size: 34rpx;
  font-weight: 600;
  color: #245f9e;
}
.k-label {
  font-size: 22rpx;
  color: #8a94a6;
  margin-top: 6rpx;
}
.card {
  background: #fff;
  border-radius: 18rpx;
  padding: 26rpx;
  margin-bottom: 18rpx;
}
.sec-title {
  font-size: 28rpx;
  font-weight: 600;
  margin-bottom: 18rpx;
}
.bar-row {
  display: flex;
  align-items: center;
  margin-bottom: 16rpx;
}
.bar-label {
  width: 150rpx;
  font-size: 24rpx;
  color: #667085;
}
.bar-track {
  flex: 1;
  height: 22rpx;
  background: #eef2f7;
  border-radius: 11rpx;
  overflow: hidden;
}
.bar-fill {
  height: 100%;
  background: #2f7cf6;
  border-radius: 11rpx;
}
.fill-green {
  background: #34c77b;
}
.fill-orange {
  background: #ff9f43;
}
.fill-blue {
  background: #2f7cf6;
}
.bar-val {
  width: 80rpx;
  text-align: right;
  font-size: 24rpx;
  color: #667085;
}
.rank {
  display: flex;
  align-items: center;
  padding: 14rpx 0;
  border-bottom: 1rpx solid #f0f3f8;
}
.rank-no {
  width: 48rpx;
  height: 48rpx;
  border-radius: 50%;
  background: #eef2f7;
  color: #667085;
  text-align: center;
  line-height: 48rpx;
  font-size: 22rpx;
  margin-right: 16rpx;
  &.no1 {
    background: #ff9f43;
    color: #fff;
  }
  &.no2 {
    background: #8bb4e8;
    color: #fff;
  }
  &.no3 {
    background: #c58b5a;
    color: #fff;
  }
}
.rank-name {
  flex: 1;
  font-size: 26rpx;
}
.rank-score {
  font-size: 24rpx;
  color: #8a94a6;
}
</style>
