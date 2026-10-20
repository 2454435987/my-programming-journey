<template>
  <div class="page-container">
    <div class="table-card">
      <div class="page-header">
        <div class="page-title">角色管理</div>
        <el-button type="primary" @click="openDialog()">新增角色</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="roleId" label="ID" width="70" />
        <el-table-column prop="roleCode" label="角色编码" width="150" />
        <el-table-column prop="roleName" label="角色名称" width="130" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column label="状态" width="90">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">{{ scope.row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="170" />
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button size="small" type="primary" link @click="openDialog(scope.row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog v-model="dialogVisible" :title="form.roleId ? '编辑角色' : '新增角色'" width="480px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="编码"><el-input v-model="form.roleCode" /></el-form-item>
        <el-form-item label="名称"><el-input v-model="form.roleName" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" /></el-form-item>
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
import { ElMessage } from 'element-plus'
import request from '@/api'

const list = ref([])
const dialogVisible = ref(false)
const form = reactive({ roleId: null, roleCode: '', roleName: '', description: '', status: 1 })

async function loadData() {
  const res = await request.get('/api/admin/roles')
  list.value = res.data
}

function openDialog(row) {
  if (row) Object.assign(form, row)
  else Object.assign(form, { roleId: null, roleCode: '', roleName: '', description: '', status: 1 })
  dialogVisible.value = true
}

async function handleSave() {
  if (form.roleId) await request.put(`/api/admin/roles/${form.roleId}`, form)
  else await request.post('/api/admin/roles', form)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

onMounted(loadData)
</script>
