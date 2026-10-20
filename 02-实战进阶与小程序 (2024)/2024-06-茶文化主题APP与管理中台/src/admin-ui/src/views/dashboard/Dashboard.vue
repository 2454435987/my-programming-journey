<template>
  <div class="page-container">
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6" v-for="item in statCards" :key="item.label">
        <div class="stat-card">
          <div class="stat-icon" :style="{ background: item.color }">
            <el-icon size="28"><component :is="item.icon" /></el-icon>
          </div>
          <div class="stat-info">
            <div class="num">{{ item.value }}</div>
            <div class="label">{{ item.label }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="14">
        <div class="table-card">
          <div class="page-title" style="margin-bottom:12px">近7天订单趋势</div>
          <div ref="trendChart" style="height:320px"></div>
        </div>
      </el-col>
      <el-col :span="10">
        <div class="table-card">
          <div class="page-title" style="margin-bottom:12px">订单状态分布</div>
          <div ref="pieChart" style="height:320px"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="12">
        <div class="table-card">
          <div class="page-title" style="margin-bottom:12px">商品销量TOP8</div>
          <div ref="barChart" style="height:300px"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="table-card">
          <div class="page-title" style="margin-bottom:12px">快捷操作</div>
          <el-space wrap :size="12">
            <el-button type="primary" @click="$router.push('/content/knowledge')">发布知识</el-button>
            <el-button type="success" @click="$router.push('/mall/products')">管理商品</el-button>
            <el-button type="warning" @click="$router.push('/activities')">创建活动</el-button>
            <el-button type="info" @click="$router.push('/community/posts')">审核帖子</el-button>
          </el-space>
          <el-descriptions title="系统信息" :column="1" border style="margin-top:20px">
            <el-descriptions-item label="系统名称">茶文化主题App</el-descriptions-item>
            <el-descriptions-item label="后端框架">Spring Boot 2.7 + MyBatis-Plus</el-descriptions-item>
            <el-descriptions-item label="前端框架">Vue 3 + Element Plus</el-descriptions-item>
            <el-descriptions-item label="数据库">MySQL / MariaDB 10.11</el-descriptions-item>
          </el-descriptions>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import request from '@/api'

const trendChart = ref()
const pieChart = ref()
const barChart = ref()
const statCards = reactive([
  { label: '注册用户', value: 0, icon: 'User', color: '#2d6a4f' },
  { label: '订单总数', value: 0, icon: 'List', color: '#40916c' },
  { label: '销售总额', value: '¥0', icon: 'Money', color: '#52b788' },
  { label: '社区帖子', value: 0, icon: 'ChatDotRound', color: '#74c69d' }
])

onMounted(async () => {
  const res = await request.get('/api/admin/stats/overview')
  const d = res.data
  statCards[0].value = d.userCount
  statCards[1].value = d.orderCount
  statCards[2].value = '¥' + Number(d.totalSales).toFixed(0)
  statCards[3].value = d.postCount

  await nextTick()
  drawTrend()
  drawPie()
  drawBar()
})

async function drawTrend() {
  const res = await request.get('/api/admin/stats/order-trend')
  const rows = res.data || []
  const chart = echarts.init(trendChart.value)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['订单数', '销售额'] },
    grid: { left: 40, right: 20, top: 40, bottom: 30 },
    xAxis: {
      type: 'category',
      data: rows.map(r => {
        const v = r.d
        return typeof v === 'string' ? v.slice(5) : v
      })
    },
    yAxis: [
      { type: 'value', name: '订单数' },
      { type: 'value', name: '销售额', axisLabel: { formatter: '¥{value}' } }
    ],
    series: [
      {
        name: '订单数',
        type: 'bar',
        barWidth: 22,
        itemStyle: { color: '#40916c', borderRadius: [4, 4, 0, 0] },
        data: rows.map(r => r.cnt)
      },
      {
        name: '销售额',
        type: 'line',
        smooth: true,
        itemStyle: { color: '#e9c46a' },
        data: rows.map(r => Number(r.amount))
      }
    ]
  })
  window.addEventListener('resize', () => chart.resize())
}

async function drawPie() {
  const res = await request.get('/api/admin/stats/order-status')
  const names = { 0: '待付款', 1: '待发货', 2: '待收货', 3: '已完成', 4: '已取消', 5: '已退款' }
  const rows = (res.data || []).map(r => ({ name: names[r.status] || ('状态' + r.status), value: r.cnt }))
  const chart = echarts.init(pieChart.value)
  chart.setOption({
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [{
      type: 'pie',
      radius: ['38%', '65%'],
      center: ['50%', '45%'],
      color: ['#40916c', '#e9c46a', '#f4a261', '#2a9d8f', '#c44536', '#8d99ae'],
      data: rows,
      label: { formatter: '{b}: {c}' }
    }]
  })
  window.addEventListener('resize', () => chart.resize())
}

async function drawBar() {
  const res = await request.get('/api/admin/stats/product-top')
  const rows = (res.data || []).slice().reverse()
  const chart = echarts.init(barChart.value)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: 130, right: 30, top: 10, bottom: 30 },
    xAxis: { type: 'value' },
    yAxis: {
      type: 'category',
      data: rows.map(r => (r.productName || '').slice(0, 8))
    },
    series: [{
      type: 'bar',
      barWidth: 16,
      itemStyle: { color: '#2d6a4f', borderRadius: [0, 4, 4, 0] },
      data: rows.map(r => r.sales)
    }]
  })
  window.addEventListener('resize', () => chart.resize())
}
</script>
