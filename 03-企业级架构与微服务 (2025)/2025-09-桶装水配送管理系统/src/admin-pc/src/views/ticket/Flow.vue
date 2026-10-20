<template>
  <div class="ticket-flow-page">
    <!-- 筛选条件 -->
    <el-card shadow="never" class="filter-card">
      <el-form :inline="true" :model="queryParam" class="filter-form">
        <el-form-item label="客户查询">
          <el-input
            v-model="queryParam.keyword"
            placeholder="客户姓名 / 手机号"
            clearable
            style="width: 200px;"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="流水类型">
          <el-select v-model="queryParam.bizType" placeholder="全部类型" clearable style="width: 170px;">
            <el-option label="全部类型" :value="null" />
            <el-option label="水票充值 (RECHARGE)" :value="1" />
            <el-option label="配送核销 (DEDUCT)" :value="2" />
            <el-option label="水票作废 (VOID)" :value="3" />
            <el-option label="退票回收 (REFUND)" :value="4" />
          </el-select>
        </el-form-item>

        <el-form-item label="时间范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            @change="handleDateChange"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询流水</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 审计台账列表 -->
    <el-card shadow="never" class="table-card">
      <div class="audit-banner">
        <el-icon color="#409eff"><InfoFilled /></el-icon>
        <span>水票资产电子台账具有严格审计约束，记录生成后不可伪造或篡改，所有充值、配送核销、退废操作均可全链路追溯。</span>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe border style="width: 100%">
        <el-table-column prop="flowNo" label="流水单号" width="180" />
        <el-table-column label="客户姓名 / 手机号" width="160">
          <template #default="{ row }">
            <div style="font-weight: 600; color: #303133;">{{ row.customerName }}</div>
            <div v-if="row.customerPhone" style="font-size: 12px; color: #909399; margin-top: 2px;">
              {{ row.customerPhone }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="brandSpecName" label="水票品类" width="160" />
        <el-table-column prop="bizType" label="业务类型" width="130" align="center">
          <template #default="{ row }">
            <el-tag :type="getFlowTagType(row.bizType)" effect="dark">
              {{ getFlowTypeText(row.bizType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="changeAmount" label="变动张数" width="120" align="center">
          <template #default="{ row }">
            <span :style="{ color: row.changeAmount > 0 ? '#67c23a' : '#f56c6c', fontWeight: 700, fontSize: '15px' }">
              {{ row.changeAmount > 0 ? `+${row.changeAmount}` : row.changeAmount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="beforeBalance" label="变动前余量" width="110" align="right" />
        <el-table-column prop="afterBalance" label="变动后余量" width="110" align="right">
          <template #default="{ row }">
            <span style="font-weight: 600;">{{ row.afterBalance }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="refOrderNo" label="关联业务单号" width="180" />
        <el-table-column prop="remark" label="操作说明 / 备注" min-width="160" show-overflow-tooltip />
        <el-table-column prop="createTime" label="发生时间" width="180">
          <template #default="{ row }">
            <span>{{ formatTime(row.createTime) }}</span>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-box">
        <el-pagination
          v-model:current-page="queryParam.pageNum"
          v-model:page-size="queryParam.pageSize"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="fetchData"
          @current-change="fetchData"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh, InfoFilled } from '@element-plus/icons-vue'
import { getTicketFlowPage } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dateRange = ref([])

const queryParam = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  bizType: null,
  startDate: '',
  endDate: ''
})

const getFlowTagType = (type) => {
  switch (Number(type)) {
    case 1: return 'success'
    case 2: return 'primary'
    case 3: return 'danger'
    case 4: return 'warning'
    default: return 'info'
  }
}

const getFlowTypeText = (type) => {
  switch (Number(type)) {
    case 1: return '水票充值'
    case 2: return '配送核销'
    case 3: return '水票作废'
    case 4: return '退票回收'
    default: return '变动'
  }
}

const formatTime = (timeStr) => {
  if (!timeStr) return '-'
  if (typeof timeStr === 'string' && (timeStr.includes('T') || timeStr.includes('+') || timeStr.includes('.'))) {
    const d = new Date(timeStr)
    if (!isNaN(d.getTime())) {
      const pad = (n) => String(n).padStart(2, '0')
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
    }
  }
  return timeStr
}

const handleDateChange = (val) => {
  if (val && val.length === 2) {
    queryParam.startDate = val[0]
    queryParam.endDate = val[1]
  } else {
    queryParam.startDate = ''
    queryParam.endDate = ''
  }
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getTicketFlowPage(queryParam)
    if (res) {
      tableData.value = res.records || []
      total.value = res.total || 0
    }
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  queryParam.pageNum = 1
  fetchData()
}

const handleReset = () => {
  queryParam.pageNum = 1
  queryParam.keyword = ''
  queryParam.bizType = null
  queryParam.startDate = ''
  queryParam.endDate = ''
  dateRange.value = []
  fetchData()
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.ticket-flow-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.filter-card, .table-card {
  border-radius: 8px;
}

.filter-form {
  margin-bottom: -18px;
}

.audit-banner {
  background: #ecf5ff;
  border-radius: 6px;
  padding: 10px 14px;
  margin-bottom: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
  color: #409eff;
  font-size: 13px;
}

.pagination-box {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
