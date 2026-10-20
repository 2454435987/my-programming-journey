<template>
  <div class="page-container">
    <div class="table-card">
      <div class="page-header">
        <div class="page-title">教程管理</div>
        <el-button type="primary" @click="openDialog()">新增教程</el-button>
      </div>
      <div class="filter-bar">
        <el-input v-model="keyword" placeholder="标题关键词" clearable style="width:220px" @keyup.enter="loadData" @clear="loadData" />
        <el-button type="primary" @click="loadData">查询</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="courseId" label="ID" width="70" />
        <el-table-column prop="title" label="标题" show-overflow-tooltip />
        <el-table-column label="难度" width="90">
          <template #default="scope">
            <el-tag>{{ ['','入门','进阶','高级'][scope.row.level] || scope.row.level }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="duration" label="时长(分)" width="90" />
        <el-table-column prop="viewCount" label="浏览量" width="80" />
        <el-table-column label="是否免费" width="90">
          <template #default="scope">
            <el-tag :type="scope.row.isFree === 1 ? 'success' : 'warning'">{{ scope.row.isFree === 1 ? '免费' : '¥' + scope.row.price }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="170" />
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
    <el-dialog v-model="dialogVisible" :title="form.courseId ? '编辑教程' : '新增教程'" width="640px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="简介"><el-input v-model="form.summary" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="5" /></el-form-item>
        <el-form-item label="封面URL"><el-input v-model="form.coverImage" /></el-form-item>
        <el-form-item label="难度">
          <el-select v-model="form.level" style="width:160px">
            <el-option label="入门" :value="1" /><el-option label="进阶" :value="2" /><el-option label="高级" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="时长(分)"><el-input-number v-model="form.duration" :min="0" /></el-form-item>
        <el-form-item label="免费"><el-switch v-model="form.isFree" :active-value="1" :inactive-value="0" /></el-form-item>
        <el-form-item label="价格"><el-input-number v-model="form.price" :min="0" :precision="2" /></el-form-item>
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
const form = reactive({ title: '', summary: '', content: '', coverImage: '', level: 1, duration: 10, isFree: 1, price: 0, courseId: null })

async function loadData() {
  const res = await request.get('/api/admin/courses', {
    params: { page: page.value, size: size.value, keyword: keyword.value }
  })
  list.value = res.data.records
  total.value = res.data.total
}

function openDialog(row) {
  if (row) Object.assign(form, row)
  else Object.assign(form, { title: '', summary: '', content: '', coverImage: '', level: 1, duration: 10, isFree: 1, price: 0, courseId: null })
  dialogVisible.value = true
}

async function handleSave() {
  if (form.courseId) await request.put(`/api/admin/courses/${form.courseId}`, form)
  else await request.post('/api/admin/courses', form)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确认删除该教程吗？', '提示', { type: 'warning' })
  await request.delete(`/api/admin/courses/${row.courseId}`)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>
