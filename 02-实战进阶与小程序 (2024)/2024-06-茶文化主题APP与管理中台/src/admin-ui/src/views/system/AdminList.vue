<template>
  <div class="page-container">
    <div class="table-card">
      <div class="page-header">
        <div class="page-title">管理员管理</div>
        <el-button type="primary" @click="openDialog()">新增管理员</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="adminId" label="ID" width="70" />
        <el-table-column prop="username" label="账号" width="120" />
        <el-table-column prop="realName" label="姓名" width="120" />
        <el-table-column prop="email" label="邮箱" show-overflow-tooltip />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">{{ scope.row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastLogin" label="最后登录" width="170" />
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button size="small" type="primary" link @click="openDialog(scope.row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total"
        layout="total, prev, pager, next" style="margin-top:12px;justify-content:flex-end" @current-change="loadData" />
    </div>
    <el-dialog v-model="dialogVisible" :title="form.adminId ? '编辑管理员' : '新增管理员'" width="520px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="账号"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="姓名"><el-input v-model="form.realName" /></el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password
            :placeholder="form.adminId ? '不修改请留空' : '请输入密码'" />
        </el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
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
const page = ref(1)
const size = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const form = reactive({ adminId: null, username: '', realName: '', password: '', email: '', phone: '', status: 1 })

async function loadData() {
  const res = await request.get('/api/admin/admins', {
    params: { page: page.value, size: size.value }
  })
  list.value = res.data.records
  total.value = res.data.total
}

function openDialog(row) {
  if (row) Object.assign(form, { ...row, password: '' })
  else Object.assign(form, { adminId: null, username: '', realName: '', password: '', email: '', phone: '', status: 1 })
  dialogVisible.value = true
}

async function handleSave() {
  if (form.adminId) await request.put(`/api/admin/admins/${form.adminId}`, form)
  else await request.post('/api/admin/admins', form)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

onMounted(loadData)
</script>
