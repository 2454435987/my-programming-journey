<template>
  <div class="page-container">
    <div class="table-card">
      <div class="page-header">
        <div class="page-title">菜单管理</div>
      </div>
      <el-table :data="list" border stripe row-key="menuId" default-expand-all>
        <el-table-column prop="menuId" label="ID" width="70" />
        <el-table-column prop="menuName" label="菜单名称" width="150" />
        <el-table-column prop="path" label="路由路径" width="180" />
        <el-table-column prop="perms" label="权限标识" width="170" />
        <el-table-column label="类型" width="90">
          <template #default="scope">
            <el-tag>{{ {1:'目录',2:'菜单',3:'按钮'}[scope.row.menuType] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="icon" label="图标" width="110" />
        <el-table-column label="可见" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.visible === 1 ? 'success' : 'info'">{{ scope.row.visible === 1 ? '是' : '否' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/api'

const list = ref([])

async function loadData() {
  const res = await request.get('/api/admin/menus')
  list.value = res.data
}

onMounted(loadData)
</script>
