<template>
  <div class="dashboard-page">
    <!-- 顶部核心指标统计卡片 -->
    <el-row :gutter="16" class="metrics-row">
      <el-col :span="6">
        <el-card shadow="hover" class="metric-card card-blue">
          <div class="metric-header">
            <span class="metric-label">今日总配送量</span>
            <el-tag size="small" type="primary" effect="dark">实时</el-tag>
          </div>
          <div class="metric-value">{{ metrics.totalDeliveredToday ?? 0 }} <span class="unit">桶</span></div>
          <div class="metric-footer">
            <span>较昨日保持稳步运行</span>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover" class="metric-card card-green">
          <div class="metric-header">
            <span class="metric-label">空桶回收率</span>
            <el-tag size="small" type="success" effect="dark">周转监控</el-tag>
          </div>
          <div class="metric-value">{{ metrics.recycleRate ?? 0 }}%</div>
          <div class="metric-footer">
            <span>回收桶数: {{ metrics.totalRecycledToday ?? 0 }} 桶</span>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover" class="metric-card card-purple">
          <div class="metric-header">
            <span class="metric-label">平均准时达标率</span>
            <el-tag size="small" type="warning" effect="dark">履约考核</el-tag>
          </div>
          <div class="metric-value">{{ metrics.onTimeRate ?? 0 }}%</div>
          <div class="metric-footer">
            <span>准时: {{ metrics.onTimeCount ?? 0 }}单 | 超时: {{ metrics.overdueCount ?? 0 }}单</span>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover" class="metric-card card-red">
          <div class="metric-header">
            <span class="metric-label">水票欠费预警客户</span>
            <el-tag size="small" type="danger" effect="dark">需催缴</el-tag>
          </div>
          <div class="metric-value">{{ metrics.warningCustomers?.length ?? 0 }} <span class="unit">户</span></div>
          <div class="metric-footer">
            <span>预警阈值：余量 ≤ 2 桶</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 中间图表区域 -->
    <el-row :gutter="16" class="charts-row">
      <el-col :span="14">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-title">
              <el-icon><Histogram /></el-icon>
              <span>送水工配送业绩排行榜 (Top 10)</span>
            </div>
          </template>
          <div ref="courierChartRef" class="echarts-box"></div>
        </el-card>
      </el-col>

      <el-col :span="10">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-title">
              <el-icon><PieChart /></el-icon>
              <span>订单履约准时分布占比</span>
            </div>
          </template>
          <div ref="pieChartRef" class="echarts-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 底部表格：告警客户预警跟进 -->
    <el-card shadow="hover" class="table-card">
      <template #header>
        <div class="card-title justify-between">
          <div class="title-left">
            <el-icon style="color: #f56c6c;"><WarningFilled /></el-icon>
            <span>水票余量不足预警列表 (实时催充)</span>
          </div>
          <el-button type="primary" size="small" @click="goToCustomers">
            前往客户管理充值
          </el-button>
        </div>
      </template>

      <el-table :data="metrics.warningCustomers || []" stripe style="width: 100%" v-loading="loading">
        <el-table-column prop="name" label="客户姓名" width="130">
          <template #default="{ row }">
            <span style="font-weight: 600;">{{ row.name || row.customerName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="联系电话" width="140" />
        <el-table-column prop="regionName" label="所属片区" width="130" />
        <el-table-column prop="addressDetail" label="详细地址" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.addressDetail || row.address }}</span>
          </template>
        </el-table-column>
        <el-table-column label="电梯房" width="90">
          <template #default="{ row }">
            <el-tag :type="row.hasElevator === 1 ? 'success' : 'info'" size="small">
              {{ row.hasElevator === 1 ? '有电梯' : `${row.floorNum || row.floorNumber || 1}楼` }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalBalance" label="剩余总水票" width="130">
          <template #default="{ row }">
            <el-tag type="danger" effect="dark" size="small">
              剩余 {{ row.totalBalance }} 张
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="unreturnedBarrels" label="未还空桶" width="110">
          <template #default="{ row }">
            <span :style="{ color: row.unreturnedBarrels > 3 ? '#e6a23c' : '#606266', fontWeight: 600 }">
              {{ row.unreturnedBarrels }} 个
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="goToRecharge(row)">
              一键充值
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import { getDashboardMetrics } from '@/api'

const router = useRouter()
const loading = ref(false)
const courierChartRef = ref(null)
const pieChartRef = ref(null)

let courierChart = null
let pieChart = null

const metrics = reactive({
  totalDeliveredToday: 0,
  totalRecycledToday: 0,
  recycleRate: 0,
  onTimeRate: 100,
  onTimeCount: 0,
  overdueCount: 0,
  warningCustomers: [],
  courierRankings: []
})

const fetchMetrics = async () => {
  loading.value = true
  try {
    const data = await getDashboardMetrics()
    if (data) {
      metrics.totalDeliveredToday = data.totalDeliveredToday || 0
      metrics.totalRecycledToday = data.totalRecycledToday || 0
      
      // 回收率容错计算
      if (data.recycleRate != null) {
        metrics.recycleRate = data.recycleRate
      } else if (metrics.totalDeliveredToday > 0) {
        metrics.recycleRate = Number(((metrics.totalRecycledToday / metrics.totalDeliveredToday) * 100).toFixed(1))
      } else {
        metrics.recycleRate = 0
      }

      // 准时与超时指标容错兼容 (兼容 timelinessDistribution 对象或根属性)
      const timeDist = data.timelinessDistribution || {}
      metrics.onTimeRate = data.onTimeRate ?? timeDist.onTimeRate ?? 100
      metrics.onTimeCount = data.onTimeCount ?? timeDist.onTimeCount ?? 0
      metrics.overdueCount = data.overdueCount ?? timeDist.overdueCount ?? 0

      metrics.warningCustomers = data.warningCustomers || []
      // 兼容 courierRankings 与 courierWorkloadRank
      metrics.courierRankings = data.courierRankings || data.courierWorkloadRank || []

      await nextTick()
      renderCourierChart()
      renderPieChart()
    }
  } catch (err) {
    console.error('Failed to load dashboard metrics:', err)
  } finally {
    loading.value = false
  }
}

const renderCourierChart = () => {
  if (!courierChartRef.value) return
  if (!courierChart) {
    courierChart = echarts.init(courierChartRef.value)
  }
  const rankings = metrics.courierRankings || []
  const names = rankings.map(r => r.courierName || r.name || r.courierNo)
  const delivers = rankings.map(r => r.deliveryQty ?? r.totalDelivered ?? 0)
  const recycles = rankings.map(r => r.recycleQty ?? r.totalRecycled ?? 0)

  courierChart.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    legend: { data: ['送达桶数', '回收空桶数'] },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { 
      type: 'category', 
      data: names.length ? names : ['无数据'],
      axisLabel: { interval: 0, rotate: names.length > 5 ? 20 : 0 }
    },
    yAxis: { type: 'value', name: '桶数' },
    series: [
      {
        name: '送达桶数',
        type: 'bar',
        data: delivers.length ? delivers : [0],
        itemStyle: { color: '#409eff', borderRadius: [4, 4, 0, 0] }
      },
      {
        name: '回收空桶数',
        type: 'bar',
        data: recycles.length ? recycles : [0],
        itemStyle: { color: '#67c23a', borderRadius: [4, 4, 0, 0] }
      }
    ]
  })
}

const renderPieChart = () => {
  if (!pieChartRef.value) return
  if (!pieChart) {
    pieChart = echarts.init(pieChartRef.value)
  }
  const onTime = Number(metrics.onTimeCount || 0)
  const overdue = Number(metrics.overdueCount || 0)
  const hasData = (onTime + overdue) > 0

  pieChart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c}单 ({d}%)' },
    legend: { bottom: '5%', left: 'center' },
    series: [
      {
        name: '履约分布',
        type: 'pie',
        radius: ['45%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 8,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: { show: false, position: 'center' },
        emphasis: {
          label: { show: true, fontSize: 16, fontWeight: 'bold' }
        },
        data: hasData ? [
          { value: onTime, name: '准时送达', itemStyle: { color: '#67c23a' } },
          { value: overdue, name: '超时送达', itemStyle: { color: '#f56c6c' } }
        ] : [
          { value: 0, name: '暂无履约数据', itemStyle: { color: '#dcdfe6' } }
        ]
      }
    ]
  })
}

const handleResize = () => {
  courierChart?.resize()
  pieChart?.resize()
}

const goToCustomers = () => {
  router.push('/customers')
}

const goToRecharge = (row) => {
  router.push({
    path: '/customers',
    query: { customerId: row.id, customerName: row.name || row.customerName }
  })
}

onMounted(() => {
  fetchMetrics()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  courierChart?.dispose()
  pieChart?.dispose()
})
</script>

<style scoped>
.dashboard-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.metrics-row {
  margin-bottom: 4px;
}

.metric-card {
  border-radius: 8px;
  border: none;
  color: #ffffff;
}

.card-blue {
  background: linear-gradient(135deg, #3a7bd5 0%, #3a6073 100%);
}

.card-green {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
}

.card-purple {
  background: linear-gradient(135deg, #654ea3 0%, #eaafc8 100%);
}

.card-red {
  background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%);
}

.metric-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  opacity: 0.9;
}

.metric-value {
  font-size: 28px;
  font-weight: 700;
  margin: 12px 0 6px;
}

.unit {
  font-size: 16px;
  font-weight: normal;
}

.metric-footer {
  font-size: 12px;
  opacity: 0.85;
}

.charts-row {
  margin-bottom: 4px;
}

.chart-card, .table-card {
  border-radius: 8px;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  font-size: 15px;
}

.justify-between {
  justify-content: space-between;
}

.title-left {
  display: flex;
  align-items: center;
  gap: 6px;
}

.echarts-box {
  width: 100%;
  height: 280px;
}
</style>
