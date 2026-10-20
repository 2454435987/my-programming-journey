<template>
  <div class="page-container">
    <div class="table-card">
      <div class="page-header">
        <div class="page-title">Banner管理</div>
        <el-button type="primary" @click="openDialog()">新增Banner</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="bannerId" label="ID" width="70" />
        <el-table-column label="图片" width="140">
          <template #default="scope">
            <el-image :src="scope.row.imageUrl" style="width:110px;height:50px" fit="cover" v-if="scope.row.imageUrl" />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" show-overflow-tooltip />
        <el-table-column prop="linkUrl" label="跳转链接" show-overflow-tooltip />
        <el-table-column prop="position" label="位置" width="90" />
        <el-table-column prop="sortOrder" label="排序" width="70" />
        <el-table-column label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">{{ scope.row.status === 1 ? '启用' : '停用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" type="primary" link @click="openDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" link @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total"
        layout="total, prev, pager, next" style="margin-top:12px;justify-content:flex-end" @current-change="loadData" />
    </div>
    <el-dialog v-model="dialogVisible" :title="form.bannerId ? '编辑Banner' : '新增Banner'" width="560px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="图片URL"><el-input v-model="form.imageUrl" /></el-form-item>
        <el-form-item label="跳转链接"><el-input v-model="form.linkUrl" /></el-form-item>
        <el-form-item label="位置">
          <el-select v-model="form.position" style="width:160px">
            <el-option label="首页" value="home" /><el-option label="商城" value="mall" /><el-option label="活动" value="activity" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="0" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="form.status" :active-value="1" :inactive-value="0" /></el-form-item>
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
const dialogVisible = ref(false)
const form = reactive({ bannerId: null, title: '', imageUrl: '', linkUrl: '', position: 'home', sortOrder: 0, status: 1 })

async function loadData() {
  const res = await request.get('/api/admin/banners', {
    params: { page: page.value, size: size.value }
  })
  list.value = res.data.records
  total.value = res.data.total
}

function openDialog(row) {
  if (row) Object.assign(form, row)
  else Object.assign(form, { bannerId: null, title: '', imageUrl: '', linkUrl: '', position: 'home', sortOrder: 0, status: 1 })
  dialogVisible.value = true
}

async function handleSave() {
  if (form.bannerId) await request.put(`/api/admin/banners/${form.bannerId}`, form)
  else await request.post('/api/admin/banners', form)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确认删除该Banner？', '提示', { type: 'warning' })
  await request.delete(`/api/admin/banners/${row.bannerId}`)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>
