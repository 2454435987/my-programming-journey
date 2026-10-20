<template>
  <div class="page-container">
    <div class="table-card">
      <div class="page-header">
        <div class="page-title">帖子管理</div>
      </div>
      <div class="filter-bar">
        <el-input v-model="keyword" placeholder="标题/内容关键词" clearable style="width:220px" @keyup.enter="loadData" @clear="loadData" />
        <el-button type="primary" @click="loadData">查询</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="postId" label="ID" width="70" />
        <el-table-column prop="title" label="标题" show-overflow-tooltip />
        <el-table-column prop="topic" label="话题" width="100" />
        <el-table-column prop="viewCount" label="浏览" width="70" />
        <el-table-column prop="likeCount" label="点赞" width="70" />
        <el-table-column prop="commentCount" label="评论" width="70" />
        <el-table-column label="置顶" width="70">
          <template #default="scope">
            <el-tag v-if="scope.row.isTop === 1" type="warning">置顶</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="精华" width="70">
          <template #default="scope">
            <el-tag v-if="scope.row.isEssence === 1" type="danger">精华</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">{{ scope.row.status === 1 ? '可见' : '隐藏' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="发布时间" width="170" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" type="warning" link @click="toggleTop(scope.row)">{{ scope.row.isTop === 1 ? '取消置顶' : '置顶' }}</el-button>
            <el-button size="small" type="success" link @click="toggleEssence(scope.row)">{{ scope.row.isEssence === 1 ? '取消精华' : '精华' }}</el-button>
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
  const res = await request.get('/api/admin/posts', {
    params: { page: page.value, size: size.value, keyword: keyword.value }
  })
  list.value = res.data.records
  total.value = res.data.total
}

async function toggleTop(row) {
  await request.put(`/api/admin/posts/${row.postId}/audit`, { isTop: row.isTop === 1 ? 0 : 1 })
  ElMessage.success('操作成功')
  loadData()
}

async function toggleEssence(row) {
  await request.put(`/api/admin/posts/${row.postId}/audit`, { isEssence: row.isEssence === 1 ? 0 : 1 })
  ElMessage.success('操作成功')
  loadData()
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确认删除该帖子？', '提示', { type: 'warning' })
  await request.delete(`/api/admin/posts/${row.postId}`)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>
