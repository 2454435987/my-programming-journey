<template>
  <div class="page-container">
    <div class="table-card">
      <div class="page-header">
        <div class="page-title">商品管理</div>
        <el-button type="primary" @click="openDialog()">新增商品</el-button>
      </div>
      <div class="filter-bar">
        <el-input v-model="keyword" placeholder="商品名称" clearable style="width:220px" @keyup.enter="loadData" @clear="loadData" />
        <el-button type="primary" @click="loadData">查询</el-button>
      </div>
      <el-table :data="list" border stripe>
        <el-table-column prop="productId" label="ID" width="70" />
        <el-table-column label="主图" width="90">
          <template #default="scope">
            <el-image :src="scope.row.coverImage" style="width:60px;height:40px" fit="cover" v-if="scope.row.coverImage" />
          </template>
        </el-table-column>
        <el-table-column prop="productName" label="商品名称" show-overflow-tooltip />
        <el-table-column label="价格" width="110">
          <template #default="scope">
            <span style="color:#e74c3c;font-weight:600">¥{{ scope.row.price }}</span>
            <span style="color:#999;text-decoration:line-through;font-size:12px;margin-left:4px">¥{{ scope.row.originalPrice }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80" />
        <el-table-column prop="sales" label="销量" width="80" />
        <el-table-column prop="origin" label="产地" width="100" />
        <el-table-column label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">{{ scope.row.status === 1 ? '上架' : '下架' }}</el-tag>
          </template>
        </el-table-column>
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
    <el-dialog v-model="dialogVisible" :title="form.productId ? '编辑商品' : '新增商品'" width="640px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.productName" /></el-form-item>
        <el-form-item label="副标题"><el-input v-model="form.subtitle" /></el-form-item>
        <el-form-item label="主图URL"><el-input v-model="form.coverImage" /></el-form-item>
        <el-form-item label="售价"><el-input-number v-model="form.price" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="原价"><el-input-number v-model="form.originalPrice" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="库存"><el-input-number v-model="form.stock" :min="0" /></el-form-item>
        <el-form-item label="产地"><el-input v-model="form.origin" /></el-form-item>
        <el-form-item label="详情"><el-input v-model="form.description" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width:160px">
            <el-option label="上架" :value="1" /><el-option label="下架" :value="0" />
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
const form = reactive({ productId: null, productName: '', subtitle: '', coverImage: '', price: 0, originalPrice: 0, stock: 0, origin: '', description: '', status: 1 })

async function loadData() {
  const res = await request.get('/api/admin/products', {
    params: { page: page.value, size: size.value, keyword: keyword.value }
  })
  list.value = res.data.records
  total.value = res.data.total
}

function openDialog(row) {
  if (row) Object.assign(form, row)
  else Object.assign(form, { productId: null, productName: '', subtitle: '', coverImage: '', price: 0, originalPrice: 0, stock: 0, origin: '', description: '', status: 1 })
  dialogVisible.value = true
}

async function handleSave() {
  if (form.productId) await request.put(`/api/admin/products/${form.productId}`, form)
  else await request.post('/api/admin/products', form)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确认删除该商品吗？', '提示', { type: 'warning' })
  await request.delete(`/api/admin/products/${row.productId}`)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>
