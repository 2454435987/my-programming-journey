<template>
  <div class="page-container">
    <div class="table-card">
      <div class="page-header">
        <div class="page-title">知识管理</div>
        <el-button type="primary" @click="openDialog()">新增知识</el-button>
      </div>
      <div class="filter-bar">
        <el-input v-model="keyword" placeholder="标题关键词" clearable style="width:220px" @keyup.enter="loadData" @clear="loadData" />
        <el-button type="primary" @click="loadData">查询</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="knowledgeId" label="ID" width="70" />
        <el-table-column label="封面" width="90">
          <template #default="scope">
            <el-image :src="scope.row.coverImage" style="width:60px;height:40px" fit="cover" v-if="scope.row.coverImage" />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" show-overflow-tooltip />
        <el-table-column prop="author" label="作者" width="110" />
        <el-table-column prop="viewCount" label="浏览量" width="80" />
        <el-table-column prop="likeCount" label="点赞" width="70" />
        <el-table-column label="置顶" width="70">
          <template #default="scope">
            <el-tag v-if="scope.row.isTop === 1" type="warning">置顶</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">{{ scope.row.status === 1 ? '发布' : '草稿' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="发布时间" width="170" />
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

    <el-dialog v-model="dialogVisible" :title="form.knowledgeId ? '编辑知识' : '新增知识'" width="640px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="摘要"><el-input v-model="form.summary" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="5" /></el-form-item>
        <el-form-item label="封面URL"><el-input v-model="form.coverImage" /></el-form-item>
        <el-form-item label="作者"><el-input v-model="form.author" /></el-form-item>
        <el-form-item label="置顶"><el-switch v-model="form.isTop" :active-value="1" :inactive-value="0" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width:160px">
            <el-option label="发布" :value="1" /><el-option label="草稿" :value="0" />
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
const form = reactive({ title: '', summary: '', content: '', coverImage: '', author: '', isTop: 0, status: 1, knowledgeId: null })

async function loadData() {
  const res = await request.get('/api/admin/knowledge', {
    params: { page: page.value, size: size.value, keyword: keyword.value }
  })
  list.value = res.data.records
  total.value = res.data.total
}

function openDialog(row) {
  if (row) Object.assign(form, row)
  else Object.assign(form, { title: '', summary: '', content: '', coverImage: '', author: '', isTop: 0, status: 1, knowledgeId: null })
  dialogVisible.value = true
}

async function handleSave() {
  if (form.knowledgeId) await request.put(`/api/admin/knowledge/${form.knowledgeId}`, form)
  else await request.post('/api/admin/knowledge', form)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确认删除该知识吗？', '提示', { type: 'warning' })
  await request.delete(`/api/admin/knowledge/${row.knowledgeId}`)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>
