<template>
  <div class="page-container">
    <div class="table-card">
      <div class="page-header">
        <div class="page-title">分类管理</div>
        <el-button type="primary" @click="openDialog()">新增分类</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="categoryId" label="ID" width="70" />
        <el-table-column prop="categoryName" label="分类名称" width="140" />
        <el-table-column label="类型" width="100">
          <template #default="scope">
            <el-tag>{{ {1:'知识',2:'商品',3:'教程'}[scope.row.categoryType] || scope.row.categoryType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" type="primary" link @click="openDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" link @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog v-model="dialogVisible" :title="form.categoryId ? '编辑分类' : '新增分类'" width="480px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.categoryName" /></el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.categoryType" style="width:160px">
            <el-option label="知识" :value="1" /><el-option label="商品" :value="2" /><el-option label="教程" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="0" /></el-form-item>
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
const dialogVisible = ref(false)
const form = reactive({ categoryId: null, categoryName: '', categoryType: 1, description: '', sortOrder: 0 })

async function loadData() {
  const res = await request.get('/api/admin/categories')
  list.value = res.data
}

function openDialog(row) {
  if (row) Object.assign(form, row)
  else Object.assign(form, { categoryId: null, categoryName: '', categoryType: 1, description: '', sortOrder: 0 })
  dialogVisible.value = true
}

async function handleSave() {
  if (form.categoryId) await request.put(`/api/admin/categories/${form.categoryId}`, form)
  else await request.post('/api/admin/categories', form)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确认删除该分类吗？', '提示', { type: 'warning' })
  await request.delete(`/api/admin/categories/${row.categoryId}`)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>
