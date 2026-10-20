<template>
  <div class="page-container">
    <div class="table-card">
      <div class="page-header">
        <div class="page-title">消息通知</div>
        <el-button type="primary" @click="openDialog()">发送消息</el-button>
      </div>
      <div class="filter-bar">
        <el-input v-model="keyword" placeholder="消息标题" clearable style="width:220px" @keyup.enter="loadData" @clear="loadData" />
        <el-button type="primary" @click="loadData">查询</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="messageId" label="ID" width="70" />
        <el-table-column label="接收对象" width="110">
          <template #default="scope">
            <span>{{ scope.row.userId === 0 ? '全员广播' : '用户' + scope.row.userId }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" show-overflow-tooltip />
        <el-table-column prop="content" label="内容" show-overflow-tooltip />
        <el-table-column label="类型" width="90">
          <template #default="scope">
            <el-tag>{{ {1:'系统',2:'订单',3:'活动',4:'互动'}[scope.row.msgType] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="发送时间" width="170" />
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button size="small" type="danger" link @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total"
        layout="total, prev, pager, next" style="margin-top:12px;justify-content:flex-end" @current-change="loadData" />
    </div>
    <el-dialog v-model="dialogVisible" title="发送消息" width="520px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="接收ID">
          <el-input-number v-model="form.userId" :min="0" style="width:200px" />
          <span style="margin-left:8px;color:#909399;font-size:12px">0 表示全员广播</span>
        </el-form-item>
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.msgType" style="width:160px">
            <el-option label="系统" :value="1" /><el-option label="订单" :value="2" />
            <el-option label="活动" :value="3" /><el-option label="互动" :value="4" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">发送</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/api'

const list = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const keyword = ref('')
const dialogVisible = ref(false)
const form = reactive({ userId: 0, title: '', content: '', msgType: 1 })

async function loadData() {
  const res = await request.get('/api/admin/messages', {
    params: { page: page.value, size: size.value, keyword: keyword.value }
  })
  list.value = res.data.records
  total.value = res.data.total
}

function openDialog() {
  Object.assign(form, { userId: 0, title: '', content: '', msgType: 1 })
  dialogVisible.value = true
}

async function handleSave() {
  await request.post('/api/admin/messages', form)
  ElMessage.success('发送成功')
  dialogVisible.value = false
  loadData()
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确认删除该消息？', '提示', { type: 'warning' })
  await request.delete(`/api/admin/messages/${row.messageId}`)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>
