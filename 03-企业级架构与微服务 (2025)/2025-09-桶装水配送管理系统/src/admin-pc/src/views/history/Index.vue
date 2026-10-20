<template>
  <div class="history-page">
    <!-- 筛选区域 -->
    <el-card shadow="never" class="filter-card">
      <el-form :inline="true" :model="queryParam" class="filter-form">
        <el-form-item label="客户搜索">
          <el-input
            v-model="queryParam.customerKeyword"
            placeholder="客户姓名 / 手机号"
            clearable
            style="width: 170px;"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="所属片区">
          <el-select v-model="queryParam.regionId" placeholder="全部片区" clearable style="width: 150px;">
            <el-option
              v-for="r in regionList"
              :key="r.id"
              :label="r.regionName || r.name"
              :value="r.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="送水工">
          <el-select v-model="queryParam.courierId" placeholder="全部送水工" clearable style="width: 150px;">
            <el-option
              v-for="c in courierList"
              :key="c.id"
              :label="`${c.name} (${c.courierNo})`"
              :value="c.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="配送状态">
          <el-select v-model="queryParam.status" placeholder="全部状态" clearable style="width: 130px;">
            <el-option label="已完成" :value="1" />
            <el-option label="配送中" :value="2" />
            <el-option label="待派工" :value="3" />
          </el-select>
        </el-form-item>

        <el-form-item label="日期范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始"
            end-placeholder="结束"
            value-format="YYYY-MM-DD"
            style="width: 220px;"
            @change="handleDateChange"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 动态汇总统计横幅 -->
    <div class="summary-stat-bar">
      <div class="stat-item">
        <span class="label">当前筛选累计送达：</span>
        <span class="num text-primary">{{ summary.totalDelivered ?? summary.totalQuantity ?? 0 }}</span> 桶
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="label">回收合格及待洗空桶：</span>
        <span class="num text-success">{{ summary.totalRecycled ?? summary.totalRecycledBarrels ?? 0 }}</span> 个
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="label">空桶综合周转回收率：</span>
        <span class="num text-warning">{{ summary.recycleRate ?? 0 }}%</span>
      </div>
    </div>

    <!-- 历史记录列表 -->
    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" v-loading="loading" stripe border style="width: 100%">
        <el-table-column prop="orderNo" label="配送单号" width="170" />
        <el-table-column prop="customerName" label="客户姓名" width="120">
          <template #default="{ row }">
            <span style="font-weight: 600;">{{ row.customerName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="customerPhone" label="客户电话" width="130" />
        <el-table-column prop="addressDetail" label="配送地址" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.addressDetail || row.address }}</span>
          </template>
        </el-table-column>
        <el-table-column label="楼层/电梯" width="110">
          <template #default="{ row }">
            <span>{{ row.floorNum || row.floorNumber || 1 }}层 {{ row.hasElevator === 1 ? '(电梯)' : '(步梯)' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="courierName" label="送水工" width="110">
          <template #default="{ row }">
            <el-tag size="small" type="info">{{ row.courierName || '未指定' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="水种规格" width="140">
          <template #default="{ row }">
            <span>{{ (row.brandName ? `${row.brandName} ${row.specDesc || ''}` : row.ticketTypeName) || '大桶水' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="送水(桶)" width="95" align="right">
          <template #default="{ row }">
            <span style="font-weight: 700; color: #409eff;">{{ row.deliveryQty ?? row.quantity ?? 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="回收(桶)" width="95" align="right">
          <template #default="{ row }">
            <span style="font-weight: 700; color: #67c23a;">{{ row.recycleQty ?? row.recycledBarrels ?? 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.orderStatus === 1 ? 'success' : (row.orderStatus === 2 ? 'warning' : 'info')">
              {{ row.orderStatus === 1 ? '已完成' : (row.orderStatus === 2 ? '配送中' : '待派工') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="deliveryTime" label="送达完成时间" width="170">
          <template #default="{ row }">
            <span>{{ formatTime(row.deliveryTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="AI空桶质检" width="130" align="center" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.aiInspectStatus || row.recycleDetail"
              link
              :type="(row.aiInspectStatus || row.recycleDetail?.qualityStatus) === 'SCRAP' ? 'danger' : ((row.aiInspectStatus || row.recycleDetail?.qualityStatus) === 'NEED_CLEAN' ? 'warning' : 'success')"
              size="small"
              @click="openAiInspectDialog(row)"
            >
              {{ getQualityText(row.aiInspectStatus || row.recycleDetail?.qualityStatus) }} 🔍
            </el-button>
            <span v-else style="color: #909399; font-size: 12px;">无回收/未质检</span>
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

    <!-- AI 视觉质检结果对话框 -->
    <el-dialog v-model="aiDialogVisible" title="大模型多模态视觉质检详情报告" width="520px" destroy-on-close>
      <div v-if="currentRecycle" class="ai-report-box">
        <div class="report-status-banner" :class="getQualityBannerClass(currentRecycle.qualityStatus)">
          <div class="banner-title">
            {{ getQualityText(currentRecycle.qualityStatus) }}
          </div>
          <div class="banner-code">判定时间：{{ formatTime(currentRecycle.createTime) }}</div>
        </div>

        <el-descriptions :column="1" border style="margin-top: 14px;">
          <el-descriptions-item label="现场照片">
            <div class="photo-preview">
              <el-image
                v-if="currentRecycle.photoUrl"
                :src="currentRecycle.photoUrl"
                fit="cover"
                style="width: 140px; height: 140px; border-radius: 6px; border: 1px solid #dcdfe6;"
                :preview-src-list="[currentRecycle.photoUrl]"
              />
              <span v-else style="color: #999;">无实物现场抓拍</span>
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="质检结论">
            <el-tag :type="getQualityTagType(currentRecycle.qualityStatus)" effect="dark">
              {{ getQualityText(currentRecycle.qualityStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="AI缺陷诊断原因">
            <div style="line-height: 1.6; color: #333;">
              {{ currentRecycle.damageReason || '外观完好无损伤，桶口密封及内壁洁净度合格。' }}
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="系统处理建议">
            <span v-if="currentRecycle.qualityStatus === 'SCRAP'" style="color: #f56c6c; font-weight: 700;">
              扣除客户押金 ¥{{ currentRecycle.deductDeposit || 30 }}元，空桶直接进入报废破裂处置流。
            </span>
            <span v-else-if="currentRecycle.qualityStatus === 'NEED_CLEAN'" style="color: #e6a23c; font-weight: 600;">
              空桶送入二次高温蒸汽清洗消毒车间，不扣除押金。
            </span>
            <span v-else style="color: #67c23a; font-weight: 600;">
              合格空桶直接归库，可快速周转再灌装。
            </span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh } from '@element-plus/icons-vue'
import {
  getDeliveryHistoryPage,
  getDeliveryHistorySummary,
  getRegionList,
  getCourierPage
} from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dateRange = ref([])
const regionList = ref([])
const courierList = ref([])

const summary = reactive({
  totalQuantity: 0,
  totalRecycledBarrels: 0,
  recycleRate: 0
})

const queryParam = reactive({
  pageNum: 1,
  pageSize: 10,
  regionId: null,
  courierId: null,
  customerKeyword: '',
  status: null,
  startDate: '',
  endDate: ''
})

// AI 质检弹窗
const aiDialogVisible = ref(false)
const currentRecycle = ref(null)

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

const getStatusTagType = (st) => {
  switch (st) {
    case 'ASSIGNED': return 'info'
    case 'DELIVERING': return 'warning'
    case 'COMPLETED': return 'success'
    case 'CANCELED': return 'danger'
    default: return 'info'
  }
}

const getStatusText = (st) => {
  switch (st) {
    case 'ASSIGNED': return '已派工'
    case 'DELIVERING': return '配送中'
    case 'COMPLETED': return '已完成'
    case 'CANCELED': return '已取消'
    default: return st
  }
}

const getQualityTagType = (st) => {
  switch (st) {
    case 'PASSED': return 'success'
    case 'NEED_CLEAN': return 'warning'
    case 'SCRAP': return 'danger'
    default: return 'info'
  }
}

const getQualityText = (st) => {
  switch (st) {
    case 'PASSED': return '合格'
    case 'NEED_CLEAN': return '需清洗'
    case 'SCRAP': return '报废/破损'
    default: return st || '正常'
  }
}

const getQualityBannerClass = (st) => {
  switch (st) {
    case 'PASSED': return 'banner-passed'
    case 'NEED_CLEAN': return 'banner-clean'
    case 'SCRAP': return 'banner-scrap'
    default: return 'banner-passed'
  }
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

const fetchBaseData = async () => {
  try {
    const [regions, couriersRes] = await Promise.all([
      getRegionList(),
      getCourierPage({ pageNum: 1, pageSize: 100 })
    ])
    regionList.value = regions || []
    courierList.value = couriersRes?.records || []
  } catch (err) {
    console.error(err)
  }
}

const fetchData = async () => {
  loading.value = true
  try {
    const [pageRes, sumRes] = await Promise.all([
      getDeliveryHistoryPage(queryParam),
      getDeliveryHistorySummary(queryParam)
    ])
    if (pageRes) {
      tableData.value = pageRes.records || []
      total.value = pageRes.total || 0
    }
    if (sumRes) {
      Object.assign(summary, sumRes)
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
  queryParam.customerKeyword = ''
  queryParam.regionId = null
  queryParam.courierId = null
  queryParam.status = null
  queryParam.startDate = ''
  queryParam.endDate = ''
  dateRange.value = []
  fetchData()
}

const openAiInspectDialog = (row) => {
  currentRecycle.value = {
    qualityStatus: row.aiInspectStatus || row.recycleDetail?.qualityStatus || 'PASSED',
    damageReason: row.aiInspectDesc || row.recycleDetail?.damageReason || '外观完好无破损，符合再周转标准',
    deductDeposit: row.suggestDeductDeposit ?? row.recycleDetail?.deductDeposit ?? 0,
    photoUrl: row.inspectImageUrl || row.recycleDetail?.photoUrl || '',
    createTime: row.deliveryTime || row.createTime || '刚刚'
  }
  aiDialogVisible.value = true
}

onMounted(async () => {
  await fetchBaseData()
  fetchData()
})
</script>

<style scoped>
.history-page {
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

.summary-stat-bar {
  display: flex;
  align-items: center;
  background: #ffffff;
  padding: 14px 24px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.stat-item {
  display: flex;
  align-items: baseline;
  gap: 6px;
  font-size: 14px;
  color: #606266;
}

.stat-item .num {
  font-size: 22px;
  font-weight: 700;
}

.text-primary { color: #409eff; }
.text-success { color: #67c23a; }
.text-warning { color: #e6a23c; }

.stat-divider {
  width: 1px;
  height: 24px;
  background: #ebeef5;
  margin: 0 28px;
}

.pagination-box {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}

.report-status-banner {
  padding: 12px 16px;
  border-radius: 6px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #ffffff;
}

.banner-title {
  font-size: 18px;
  font-weight: 700;
}

.banner-code {
  font-size: 12px;
  opacity: 0.9;
}

.banner-passed {
  background: linear-gradient(135deg, #11998e, #38ef7d);
}

.banner-clean {
  background: linear-gradient(135deg, #f2994a, #f2c94c);
}

.banner-scrap {
  background: linear-gradient(135deg, #eb3349, #f45c43);
}
</style>
