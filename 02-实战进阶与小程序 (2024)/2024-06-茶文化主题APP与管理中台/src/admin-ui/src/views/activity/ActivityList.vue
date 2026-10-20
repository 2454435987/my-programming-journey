<template>
  <div class="page-container">
    <div class="table-card">
      <div class="page-header">
        <div class="page-title">活动管理</div>
        <el-button type="primary" @click="openDialog()">新增活动</el-button>
      </div>
      <div class="filter-bar">
        <el-input v-model="keyword" placeholder="活动标题" clearable style="width:220px" @keyup.enter="loadData" @clear="loadData" />
        <el-button type="primary" @click="loadData">查询</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="activityId" label="ID" width="70" />
        <el-table-column prop="title" label="活动标题" show-overflow-tooltip />
        <el-table-column prop="activityType" label="类型" width="80" />
        <el-table-column prop="location" label="地点" width="150" show-overflow-tooltip />
        <el-table-column prop="startTime" label="开始时间" width="160" />
        <el-table-column label="报名" width="100">
          <template #default="scope">
            <span>{{ scope.row.enrolledCount }}/{{ scope.row.maxEnroll }}</span>
          </template>
        </el-table-column>
        <el-table-column label="费用" width="80">
          <template #default="scope">
            <span>{{ Number(scope.row.price) === 0 ? '免费' : '¥' + scope.row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="scope">
            <el-tag>{{ {0:'已取消',1:'报名中',2:'进行中',3:'已结束'}[scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" link @click="openDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" link @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total"
        layout="total, prev, pager, next" style="margin-top:12px;justify-content:flex-end" @current-change="loadData" />
    </div>
    <el-dialog v-model="dialogVisible" :title="form.activityId ? '编辑活动' : '新增活动'" width="640px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="简介"><el-input v-model="form.summary" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="详情"><el-input v-model="form.content" type="textarea" :rows="4" /></el-form-item>
        <el-form-item label="封面URL"><el-input v-model="form.coverImage" /></el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.activityType" style="width:160px">
            <el-option label="线下" value="线下" /><el-option label="线上" value="线上" />
          </el-select>
        </el-form-item>
        <el-form-item label="地点"><el-input v-model="form.location" /></el-form-item>
        <el-form-item label="最大人数"><el-input-number v-model="form.maxEnroll" :min="0" /></el-form-item>
        <el-form-item label="费用"><el-input-number v-model="form.price" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width:160px">
            <el-option label="报名中" :value="1" /><el-option label="进行中" :value="2" /><el-option label="已结束" :value="3" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
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
const form = reactive({ activityId: null, title: '', summary: '', content: '', coverImage: '', activityType: '线下', location: '', maxEnroll: 50, price: 0, status: 1 })

async function loadData() {
  const res = await request.get('/api/admin/activities', {
    params: { page: page.value, size: size.value, keyword: keyword.value }
  })
  list.value = res.data.records
  total.value = res.data.total
}

function openDialog(row) {
  if (row) Object.assign(form, row)
  else Object.assign(form, { activityId: null, title: '', summary: '', content: '', coverImage: '', activityType: '线下', location: '', maxEnroll: 50, price: 0, status: 1 })
  dialogVisible.value = true
}

async function handleSave() {
  if (form.activityId) await request.put(`/api/admin/activities/${form.activityId}`, form)
  else await request.post('/api/admin/activities', form)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确认删除该活动？', '提示', { type: 'warning' })
  await request.delete(`/api/admin/activities/${row.activityId}`)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>
