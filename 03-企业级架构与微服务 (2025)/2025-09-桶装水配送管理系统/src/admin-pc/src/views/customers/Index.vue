<template>
  <div class="customers-page">
    <!-- 筛选卡片 -->
    <el-card shadow="never" class="filter-card">
      <el-form :inline="true" :model="queryParam" class="filter-form">
        <el-form-item label="客户搜索">
          <el-input
            v-model="queryParam.keyword"
            placeholder="姓名 / 手机号 / 楼栋地址"
            clearable
            style="width: 220px;"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="所属片区">
          <el-select v-model="queryParam.regionId" placeholder="全部片区" clearable style="width: 160px;">
            <el-option
              v-for="r in regionList"
              :key="r.id"
              :label="r.regionName || r.name"
              :value="r.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="电梯情况">
          <el-select v-model="queryParam.hasElevator" placeholder="全部" clearable style="width: 120px;">
            <el-option label="有电梯" :value="1" />
            <el-option label="无电梯(楼梯房)" :value="0" />
          </el-select>
        </el-form-item>

        <el-form-item label="预警过滤">
          <el-checkbox v-model="queryParam.onlyWarning" border @change="handleSearch">
            <span style="color: #f56c6c; font-weight: 600;">仅看欠费告警 (≤2桶)</span>
          </el-checkbox>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
          <el-button type="success" :icon="Plus" @click="openCustomerDialog(null)">新建客户档案</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 客户列表 -->
    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" v-loading="loading" stripe border style="width: 100%">
        <el-table-column prop="name" label="客户姓名" width="130">
          <template #default="{ row }">
            <span style="font-weight: 600;">{{ row.name || row.customerName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="联系电话" width="140" />
        <el-table-column prop="regionName" label="片区" width="130">
          <template #default="{ row }">
            <el-tag size="small" effect="plain">{{ row.regionName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="addressDetail" label="详细地址" min-width="220" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.addressDetail || row.address }}</span>
          </template>
        </el-table-column>
        <el-table-column label="楼层" width="100">
          <template #default="{ row }">
            <span>{{ row.floorNum || 1 }} 层</span>
          </template>
        </el-table-column>
        <el-table-column label="电梯" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.hasElevator === 1 ? 'success' : 'warning'" size="small">
              {{ row.hasElevator === 1 ? '有电梯' : '步梯' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="unreturnedBarrels" label="手头空桶" width="100" align="center">
          <template #default="{ row }">
            <span :style="{ color: row.unreturnedBarrels > 3 ? '#e6a23c' : '#606266' }">
              {{ row.unreturnedBarrels || 0 }} 个
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="totalBalance" label="剩余总水票" width="140" align="center">
          <template #default="{ row }">
            <el-badge :is-dot="row.totalBalance <= 2" type="danger">
              <el-tag :type="row.totalBalance <= 2 ? 'danger' : 'success'" effect="dark" size="small">
                {{ row.totalBalance }} 张
              </el-tag>
            </el-badge>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="220" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="openRechargeDialog(row)">
              充值水票
            </el-button>
            <el-button link type="success" size="small" @click="openBalanceDrawer(row)">
              余量明细
            </el-button>
            <el-button link type="warning" size="small" @click="openCustomerDialog(row)">
              编辑
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

    <!-- 新增 / 编辑客户档案弹窗 -->
    <el-dialog
      v-model="customerDialogVisible"
      :title="isEditCustomer ? '编辑客户档案' : '新建客户档案'"
      width="540px"
      destroy-on-close
    >
      <el-form ref="customerFormRef" :model="customerForm" :rules="customerRules" label-width="100px">
        <el-form-item label="客户姓名" prop="customerName">
          <el-input v-model="customerForm.customerName" placeholder="如：李明" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="customerForm.phone" placeholder="如：13800138001" />
        </el-form-item>
        <el-form-item label="所属片区" prop="regionId">
          <el-select v-model="customerForm.regionId" placeholder="请选择片区" style="width: 100%;">
            <el-option
              v-for="r in regionList"
              :key="r.id"
              :label="r.regionName || r.name"
              :value="r.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="楼栋编号" prop="buildingNo">
          <el-input v-model="customerForm.buildingNo" placeholder="如：3号楼" />
        </el-form-item>
        <el-form-item label="楼层与门牌" prop="floorNumber">
          <el-row :gutter="10">
            <el-col :span="12">
              <el-input-number v-model="customerForm.floorNumber" :min="1" :max="99" style="width: 100%;" placeholder="所在楼层" />
            </el-col>
            <el-col :span="12">
              <el-input v-model="customerForm.roomNumber" placeholder="门牌号(如: 802室)" />
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="电梯配置" prop="hasElevator">
          <el-radio-group v-model="customerForm.hasElevator">
            <el-radio :label="1">有电梯 (便捷配送)</el-radio>
            <el-radio :label="0">无电梯 (步梯爬楼)</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="完整地址" prop="address">
          <el-input v-model="customerForm.address" placeholder="如：锦绣花园3栋802室" />
        </el-form-item>
        <el-form-item label="押金金额" prop="barrelDeposit">
          <el-input-number v-model="customerForm.barrelDeposit" :min="0" :step="50" style="width: 100%;" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="customerDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="customerSubmitting" @click="submitCustomer">保存档案</el-button>
      </template>
    </el-dialog>

    <!-- 水票充值弹窗 -->
    <el-dialog v-model="rechargeDialogVisible" title="客户水票充值 / 售卡" width="500px" destroy-on-close>
      <el-form ref="rechargeFormRef" :model="rechargeForm" :rules="rechargeRules" label-width="100px">
        <el-form-item label="充值客户">
          <span style="font-weight: 600;">{{ currentCustomer.customerName }} ({{ currentCustomer.phone }})</span>
        </el-form-item>
        <el-form-item label="水票品类" prop="ticketTypeId">
          <el-select v-model="rechargeForm.ticketTypeId" placeholder="请选择水种规格" style="width: 100%;" @change="handleTicketTypeChange">
            <el-option
              v-for="t in ticketTypes"
              :key="t.id"
              :label="`${t.brandName || t.name} (${t.specDesc || t.spec || ''}) - 单价 ¥${t.unitPrice ?? t.price ?? 0}`"
              :value="t.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="充值张数" prop="quantity">
          <el-input-number v-model="rechargeForm.quantity" :min="1" :max="1000" style="width: 100%;" @change="handleRechargeQtyChange" />
        </el-form-item>
        <el-form-item label="实收金额" prop="paidAmount">
          <el-input-number v-model="rechargeForm.paidAmount" :min="0" :precision="2" :step="10" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="业务备注">
          <el-input v-model="rechargeForm.remark" placeholder="如：店庆买10送2优惠充值" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rechargeDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="rechargeSubmitting" @click="submitRecharge">确认充值入账</el-button>
      </template>
    </el-dialog>

    <!-- 客户各品类水票余量抽屉 -->
    <el-drawer v-model="balanceDrawerVisible" title="客户水票账户明细" size="460px">
      <div v-if="currentCustomer.id" class="drawer-content">
        <div class="customer-summary-card">
          <h4>{{ currentCustomer.customerName }}</h4>
          <p>电话：{{ currentCustomer.phone }}</p>
          <p>地址：{{ currentCustomer.address }}</p>
          <p>当前押金：¥{{ currentCustomer.barrelDeposit }} | 欠桶：{{ currentCustomer.unreturnedBarrels }} 个</p>
        </div>

        <el-divider content-position="left">各水种余量列表</el-divider>

        <el-table :data="customerTicketAccounts" stripe border style="width: 100%">
          <el-table-column label="水票品类">
            <template #default="{ row }">
              <span>{{ (row.brandName ? `${row.brandName} (${row.specDesc || ''})` : row.ticketTypeName) || '桶装水' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="balance" label="剩余水票" width="120" align="center">
            <template #default="{ row }">
              <el-tag :type="row.balance <= 2 ? 'danger' : 'success'" effect="dark">
                {{ row.balance }} 张
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" align="center">
            <template #default="{ row }">
              <el-button link type="danger" size="small" @click="openRefundDialog(row)">
                退换票
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-drawer>

    <!-- 退票弹窗 -->
    <el-dialog v-model="refundDialogVisible" title="水票退款 / 作废处理" width="420px" destroy-on-close>
      <el-form :model="refundForm" label-width="90px">
        <el-form-item label="退票品类">
          <span>{{ (currentRefundAccount.brandName ? `${currentRefundAccount.brandName} (${currentRefundAccount.specDesc || ''})` : currentRefundAccount.ticketTypeName) || '水票' }} (当前余量: {{ currentRefundAccount.balance }}张)</span>
        </el-form-item>
        <el-form-item label="退票张数" required>
          <el-input-number v-model="refundForm.quantity" :min="1" :max="currentRefundAccount.balance || 1" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="退款金额">
          <el-input-number v-model="refundForm.refundAmount" :min="0" :precision="2" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="退款原因">
          <el-input v-model="refundForm.remark" placeholder="如：客户搬迁注销水票退款" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="refundDialogVisible = false">取消</el-button>
        <el-button type="danger" :loading="refundSubmitting" @click="submitRefund">确认退款作废</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import {
  getCustomerPage,
  getRegionList,
  saveCustomer,
  getTicketTypes,
  rechargeTicket,
  getCustomerTickets,
  cancelRefundTicket
} from '@/api'

const route = useRoute()
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const regionList = ref([])
const ticketTypes = ref([])

const queryParam = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  regionId: null,
  hasElevator: null,
  onlyWarning: false
})

// 客户弹窗
const customerDialogVisible = ref(false)
const customerSubmitting = ref(false)
const isEditCustomer = ref(false)
const customerFormRef = ref(null)
const customerForm = reactive({
  id: null,
  customerName: '',
  phone: '',
  regionId: null,
  buildingNo: '',
  floorNumber: 1,
  roomNumber: '',
  hasElevator: 1,
  address: '',
  barrelDeposit: 100
})

const customerRules = {
  customerName: [{ required: true, message: '请输入客户姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号码', trigger: 'blur' }],
  regionId: [{ required: true, message: '请选择所属片区', trigger: 'change' }],
  address: [{ required: true, message: '请输入完整地址', trigger: 'blur' }]
}

// 充值水票弹窗
const rechargeDialogVisible = ref(false)
const rechargeSubmitting = ref(false)
const rechargeFormRef = ref(null)
const currentCustomer = ref({})
const rechargeForm = reactive({
  customerId: null,
  ticketTypeId: null,
  quantity: 10,
  paidAmount: 200,
  remark: ''
})

const rechargeRules = {
  ticketTypeId: [{ required: true, message: '请选择水票品类', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入充值张数', trigger: 'blur' }]
}

// 账户余量抽屉
const balanceDrawerVisible = ref(false)
const customerTicketAccounts = ref([])

// 退票弹窗
const refundDialogVisible = ref(false)
const refundSubmitting = ref(false)
const currentRefundAccount = ref({})
const refundForm = reactive({
  customerId: null,
  ticketTypeId: null,
  quantity: 1,
  refundAmount: 0,
  remark: ''
})

const fetchBaseData = async () => {
  try {
    const [regions, types] = await Promise.all([getRegionList(), getTicketTypes()])
    regionList.value = regions || []
    ticketTypes.value = types || []
  } catch (err) {
    console.error(err)
  }
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getCustomerPage(queryParam)
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
  queryParam.hasElevator = null
  queryParam.onlyWarning = false
  fetchData()
}

// 打开客户编辑/新增弹窗
const openCustomerDialog = (row) => {
  if (row) {
    isEditCustomer.value = true
    Object.assign(customerForm, {
      id: row.id,
      customerName: row.name || row.customerName,
      phone: row.phone,
      regionId: row.regionId,
      floorNumber: row.floorNum || 1,
      hasElevator: row.hasElevator ?? 1,
      address: row.addressDetail || row.address
    })
  } else {
    isEditCustomer.value = false
    Object.assign(customerForm, {
      id: null,
      customerName: '',
      phone: '',
      regionId: regionList.value[0]?.id || null,
      floorNumber: 1,
      hasElevator: 1,
      address: ''
    })
  }
  customerDialogVisible.value = true
}

const submitCustomer = async () => {
  if (!customerFormRef.value) return
  await customerFormRef.value.validate(async (valid) => {
    if (!valid) return
    customerSubmitting.value = true
    try {
      await saveCustomer(customerForm)
      ElMessage.success(isEditCustomer.value ? '客户档案修改成功' : '新增客户档案成功')
      customerDialogVisible.value = false
      fetchData()
    } catch (e) {
      console.error(e)
    } finally {
      customerSubmitting.value = false
    }
  })
}

// 打开充值水票
const openRechargeDialog = (row) => {
  currentCustomer.value = {
    id: row.id,
    customerName: row.name || row.customerName,
    phone: row.phone
  }
  rechargeForm.customerId = row.id
  const defaultType = ticketTypes.value[0]
  rechargeForm.ticketTypeId = defaultType?.id || null
  rechargeForm.quantity = 10
  rechargeForm.paidAmount = defaultType ? Number(defaultType.unitPrice || 20) * 10 : 200
  rechargeForm.remark = '前台柜台充值'
  rechargeDialogVisible.value = true
}

const handleTicketTypeChange = () => {
  const selected = ticketTypes.value.find(t => t.id === rechargeForm.ticketTypeId)
  if (selected && selected.unitPrice != null) {
    rechargeForm.paidAmount = Number(selected.unitPrice) * (rechargeForm.quantity || 1)
  }
}

const handleRechargeQtyChange = () => {
  const selected = ticketTypes.value.find(t => t.id === rechargeForm.ticketTypeId)
  if (selected && selected.unitPrice != null) {
    rechargeForm.paidAmount = Number(selected.unitPrice) * (rechargeForm.quantity || 1)
  }
}

const submitRecharge = async () => {
  if (!rechargeFormRef.value) return
  await rechargeFormRef.value.validate(async (valid) => {
    if (!valid) return
    rechargeSubmitting.value = true
    try {
      await rechargeTicket(rechargeForm)
      ElMessage.success('水票充值成功！台账已记录流水。')
      rechargeDialogVisible.value = false
      fetchData()
    } catch (e) {
      console.error(e)
    } finally {
      rechargeSubmitting.value = false
    }
  })
}

// 打开余量明细抽屉
const openBalanceDrawer = async (row) => {
  currentCustomer.value = {
    id: row.id,
    customerName: row.name || row.customerName,
    phone: row.phone,
    address: row.addressDetail || row.address,
    unreturnedBarrels: row.unreturnedBarrels || 0
  }
  balanceDrawerVisible.value = true
  try {
    const list = await getCustomerTickets(row.id)
    customerTicketAccounts.value = list || []
  } catch (err) {
    console.error(err)
  }
}

// 退票操作
const openRefundDialog = (account) => {
  currentRefundAccount.value = account
  refundForm.customerId = currentCustomer.value.id
  refundForm.ticketTypeId = account.typeId || account.ticketTypeId
  refundForm.quantity = 1
  refundForm.refundAmount = Number(account.unitPrice || 20)
  refundForm.remark = '客户办理退票'
  refundDialogVisible.value = true
}

const submitRefund = async () => {
  refundSubmitting.value = true
  try {
    await cancelRefundTicket(refundForm)
    ElMessage.success('退票处理成功，已核减水票并生成退款流水！')
    refundDialogVisible.value = false
    // 刷新抽屉列表与外部表格
    const list = await getCustomerTickets(currentCustomer.value.id)
    customerTicketAccounts.value = list || []
    fetchData()
  } catch (e) {
    console.error(e)
  } finally {
    refundSubmitting.value = false
  }
}

onMounted(async () => {
  await fetchBaseData()
  if (route.query.customerId) {
    queryParam.keyword = route.query.customerName || ''
  }
  fetchData()
})
</script>

<style scoped>
.customers-page {
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

.pagination-box {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}

.customer-summary-card {
  background: #f8fafc;
  padding: 14px;
  border-radius: 6px;
  line-height: 1.8;
  color: #334155;
  margin-bottom: 12px;
}
</style>
