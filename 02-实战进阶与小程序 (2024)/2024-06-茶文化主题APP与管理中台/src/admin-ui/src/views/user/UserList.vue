<template>
  <div class="page-container">
    <div class="table-card">
      <div class="page-header">
        <div class="page-title">用户管理</div>
      </div>
      <div class="filter-bar">
        <el-input v-model="keyword" placeholder="用户名/昵称" clearable style="width:220px" @keyup.enter="loadData" @clear="loadData" />
        <el-button type="primary" @click="loadData">查询</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="userId" label="ID" width="70" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column label="性别" width="70">
          <template #default="scope">
            <span>{{ scope.row.gender === 1 ? '男' : scope.row.gender === 2 ? '女' : '未知' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" show-overflow-tooltip />
        <el-table-column prop="points" label="积分" width="80" />
        <el-table-column label="等级" width="80">
          <template #default="scope">
            <el-tag>L{{ scope.row.level }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="注册时间" width="170" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="scope">
            <el-button size="small" :type="scope.row.status === 1 ? 'danger' : 'success'" link
              @click="toggleStatus(scope.row)">
              {{ scope.row.status === 1 ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total"
        layout="total, prev, pager, next" style="margin-top:12px;justify-content:flex-end" @current-change="loadData" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/api'

const list = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const keyword = ref('')

async function loadData() {
  const res = await request.get('/api/admin/users', {
    params: { page: page.value, size: size.value, keyword: keyword.value }
  })
  list.value = res.data.records
  total.value = res.data.total
}

async function toggleStatus(row) {
  const newStatus = row.status === 1 ? 0 : 1
  await ElMessageBox.confirm(newStatus === 0 ? '确认禁用该用户？' : '确认启用该用户？', '提示', { type: 'warning' })
  await request.put(`/api/admin/users/${row.userId}/status`, { status: newStatus })
  ElMessage.success('操作成功')
  loadData()
}

onMounted(loadData)
</script>
