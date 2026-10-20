<template>
  <div class="page-container">
    <div class="table-card">
      <div class="page-header">
        <div class="page-title">评论管理</div>
      </div>
      <div class="filter-bar">
        <el-input v-model="keyword" placeholder="评论内容" clearable style="width:220px" @keyup.enter="loadData" @clear="loadData" />
        <el-button type="primary" @click="loadData">查询</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="commentId" label="ID" width="70" />
        <el-table-column prop="postId" label="帖子ID" width="90" />
        <el-table-column prop="userId" label="用户ID" width="90" />
        <el-table-column prop="content" label="评论内容" show-overflow-tooltip />
        <el-table-column prop="likeCount" label="点赞" width="70" />
        <el-table-column prop="createdAt" label="评论时间" width="170" />
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button size="small" type="danger" link @click="handleDelete(scope.row)">删除</el-button>
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
  const res = await request.get('/api/admin/comments', {
    params: { page: page.value, size: size.value, keyword: keyword.value }
  })
  list.value = res.data.records
  total.value = res.data.total
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确认删除该评论？', '提示', { type: 'warning' })
  await request.delete(`/api/admin/comments/${row.commentId}`)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>
