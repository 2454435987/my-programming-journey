<template>
  <div class="couriers-page">
    <!-- 顶部片区分布概览卡片 -->
    <el-row :gutter="16" class="region-cards-row">
      <el-col :span="8" v-for="reg in regionList" :key="reg.id">
        <el-card shadow="hover" class="region-box-card">
          <div class="region-box-header">
            <span class="region-name">📍 {{ reg.regionName || reg.name }} ({{ reg.regionCode || reg.code }})</span>
            <el-tag size="small" type="info">覆盖范围</el-tag>
          </div>
          <div class="region-coverage">{{ reg.coverageDesc || reg.coverage }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索筛选栏 -->
    <el-card shadow="never" class="filter-card">
      <el-form :inline="true" :model="queryParam" class="filter-form">
        <el-form-item label="关键字">
          <el-input
            v-model="queryParam.keyword"
            placeholder="搜索送水工工号 / 姓名 / 手机号"
            clearable
            style="width: 240px;"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="所属片区">
          <el-select v-model="queryParam.regionId" placeholder="全部片区" clearable style="width: 180px;">
            <el-option
              v-for="r in regionList"
              :key="r.id"
              :label="r.regionName || r.name"
              :value="r.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="在职状态">
          <el-select v-model="queryParam.status" placeholder="全部状态" clearable style="width: 140px;">
            <el-option label="在职" :value="1" />
            <el-option label="离职/停用" :value="0" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" v-loading="loading" stripe border style="width: 100%">
        <el-table-column prop="courierNo" label="员工工号" width="120" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="phone" label="手机号码" width="140" />
        <el-table-column prop="regionName" label="所属片区" width="160">
          <template #default="{ row }">
            <el-tag :type="row.regionName ? 'success' : 'warning'" effect="plain">
              {{ row.regionName || '未分配' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalDelivered" label="累计送达(桶)" width="130" sortable align="right" />
        <el-table-column prop="unreturnedBarrels" label="手头欠空桶" width="120" align="right">
          <template #default="{ row }">
            <span :style="{ color: row.unreturnedBarrels > 5 ? '#f56c6c' : '#606266', fontWeight: 600 }">
              {{ row.unreturnedBarrels }} 个
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="在职状态" width="120" align="center">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              active-text="在职"
              inactive-text="停职"
              inline-prompt
              @change="(val) => handleStatusChange(row, val)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="录入时间" width="180">
          <template #default="{ row }">
            <span>{{ formatTime(row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="openAssignDialog(row)">
              调整片区
            </el-button>
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

    <!-- 调整片区弹窗 -->
    <el-dialog v-model="assignDialogVisible" title="调整送水工网格片区" width="450px" destroy-on-close>
      <el-form :model="assignForm" label-width="100px">
        <el-form-item label="送水工姓名">
          <span>{{ currentCourier.name }} ({{ currentCourier.courierNo }})</span>
        </el-form-item>
        <el-form-item label="当前片区">
          <span>{{ currentCourier.regionName || '暂未分配' }}</span>
        </el-form-item>
        <el-form-item label="目标片区" required>
          <el-select v-model="assignForm.regionId" placeholder="请选择新分配片区" style="width: 100%;">
            <el-option
              v-for="r in regionList"
              :key="r.id"
              :label="`${r.regionName || r.name} (${r.coverageDesc || r.coverage || ''})`"
              :value="r.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="assignLoading" @click="confirmAssignRegion">确认调整</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCourierPage, getRegionList, assignCourierRegion, updateCourierStatus } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const regionList = ref([])

const queryParam = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  regionId: null,
  status: null
})

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

// 片区调整弹窗
const assignDialogVisible = ref(false)
const assignLoading = ref(false)
const currentCourier = ref({})
const assignForm = reactive({
  courierId: null,
  regionId: null
})

const fetchRegions = async () => {
  try {
    const list = await getRegionList()
    regionList.value = list || []
  } catch (err) {
    console.error(err)
  }
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getCourierPage(queryParam)
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
  queryParam.regionId = null
  queryParam.status = null
  fetchData()
}

const handleStatusChange = async (row, val) => {
  const actionText = val === 1 ? '恢复在职' : '停用该送水工'
  try {
    await ElMessageBox.confirm(`确定要将送水工【${row.name}】设置为${actionText}状态吗？`, '状态变更确认', {
      type: 'warning'
    })
    await updateCourierStatus({
      courierId: row.id,
      status: val
    })
    row.status = val
    ElMessage.success(`已成功更新【${row.name}】为${val === 1 ? '在职' : '停用'}`)
  } catch (e) {
    if (e !== 'cancel') {
      console.error(e)
    }
    // revert
    row.status = val === 1 ? 0 : 1
  }
}

const openAssignDialog = (row) => {
  currentCourier.value = row
  assignForm.courierId = row.id
  assignForm.regionId = row.regionId
  assignDialogVisible.value = true
}

const confirmAssignRegion = async () => {
  if (!assignForm.regionId) {
    ElMessage.warning('请选择目标片区')
    return
  }
  assignLoading.value = true
  try {
    await assignCourierRegion(assignForm)
    ElMessage.success('片区划分调整成功！')
    assignDialogVisible.value = false
    fetchData()
  } catch (e) {
    console.error(e)
  } finally {
    assignLoading.value = false
  }
}

onMounted(() => {
  fetchRegions()
  fetchData()
})
</script>

<style scoped>
.couriers-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.region-cards-row {
  margin-bottom: 4px;
}

.region-box-card {
  border-radius: 8px;
  background: #ffffff;
}

.region-box-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.region-name {
  font-weight: 600;
  font-size: 15px;
  color: #1f2d3d;
}

.region-coverage {
  font-size: 13px;
  color: #71767a;
  line-height: 1.4;
}

.filter-card, .table-card {
  border-radius: 8px;
}

.filter-form {
  margin-bottom: -18px;
}

.pagination-box {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
