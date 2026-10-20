<template>
  <div class="page-container">
    <div class="table-card">
      <div class="page-header">
        <div class="page-title">订单管理</div>
      </div>
      <div class="filter-bar">
        <el-input v-model="keyword" placeholder="订单号/收货人" clearable style="width:220px" @keyup.enter="loadData" @clear="loadData" />
        <el-select v-model="status" placeholder="订单状态" clearable style="width:140px" @change="loadData">
          <el-option v-for="(name, key) in statusMap" :key="key" :label="name" :value="Number(key)" />
        </el-select>
        <el-button type="primary" @click="loadData">查询</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="orderNo" label="订单编号" width="170" />
        <el-table-column prop="receiver" label="收货人" width="90" />
        <el-table-column prop="receiverPhone" label="电话" width="120" />
        <el-table-column label="金额" width="100">
          <template #default="scope">
            <span style="color:#e74c3c;font-weight:600">¥{{ scope.row.payAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="scope">
            <el-tag :type="statusType[scope.row.status] || 'info'">{{ statusMap[scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="支付方式" width="90">
          <template #default="scope">
            <span>{{ ['','微信','支付宝','余额'][scope.row.payType] }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="下单时间" width="170" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" link @click="viewDetail(scope.row)">详情</el-button>
            <el-button v-if="scope.row.status === 1" size="small" type="success" link @click="ship(scope.row)">发货</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total"
        layout="total, prev, pager, next" style="margin-top:12px;justify-content:flex-end" @current-change="loadData" />
    </div>

    <el-dialog v-model="detailVisible" title="订单详情" width="640px">
      <el-descriptions :column="2" border v-if="detail.order">
        <el-descriptions-item label="订单编号">{{ detail.order.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ statusMap[detail.order.status] }}</el-descriptions-item>
        <el-descriptions-item label="收货人">{{ detail.order.receiver }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ detail.order.receiverPhone }}</el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ detail.order.receiverAddress }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detail.order.remark || '无' }}</el-descriptions-item>
      </el-descriptions>
      <el-table :data="detail.items || []" border style="margin-top:12px">
        <el-table-column prop="productName" label="商品" />
        <el-table-column prop="price" label="单价" width="100" />
        <el-table-column prop="quantity" label="数量" width="80" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/api'

const statusMap = { 0: '待付款', 1: '待发货', 2: '待收货', 3: '已完成', 4: '已取消', 5: '已退款' }
const statusType = { 0: 'warning', 1: 'primary', 2: 'warning', 3: 'success', 4: 'info', 5: 'danger' }

const list = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const keyword = ref('')
const status = ref(null)
const detailVisible = ref(false)
const detail = ref({})

async function loadData() {
  const res = await request.get('/api/admin/orders', {
    params: { page: page.value, size: size.value, keyword: keyword.value, status: status.value }
  })
  list.value = res.data.records
  total.value = res.data.total
}

async function viewDetail(row) {
  const res = await request.get(`/api/admin/orders/${row.orderId}`)
  detail.value = res.data
  detailVisible.value = true
}

async function ship(row) {
  await ElMessageBox.confirm('确认发货该订单？', '提示', { type: 'warning' })
  await request.put(`/api/admin/orders/${row.orderId}/ship`)
  ElMessage.success('发货成功')
  loadData()
}

onMounted(loadData)
</script>
