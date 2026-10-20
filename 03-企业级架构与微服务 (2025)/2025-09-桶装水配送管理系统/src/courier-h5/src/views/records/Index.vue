<template>
  <div class="records-page">
    <van-nav-bar title="我的送水业绩与核销明细" fixed placeholder />

    <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
      <!-- 汇总卡片 -->
      <div class="summary-card">
        <div class="stat-item">
          <span class="num">{{ totalDelivered }}</span>
          <span class="label">累计完成配送(桶)</span>
        </div>
        <div class="divider"></div>
        <div class="stat-item">
          <span class="num">{{ totalRecycled }}</span>
          <span class="label">已回收空桶(个)</span>
        </div>
      </div>

      <!-- 历史流水列表 -->
      <div class="record-list">
        <van-empty v-if="records.length === 0 && !loading" description="暂无配送核销记录" />

        <div v-for="item in records" :key="item.id" class="record-item">
          <div class="item-top">
            <span class="order-no">{{ item.orderNo }}</span>
            <van-tag type="success" size="medium">已完成送达</van-tag>
          </div>

          <div class="item-main">
            <div class="client-name">{{ item.customerName }} ({{ item.customerPhone }})</div>
            <div class="client-addr">{{ item.addressDetail || item.address }}</div>
            <div class="detail-tags">
              <van-tag plain type="primary">💧 {{ item.brandName ? `${item.brandName} ${item.specDesc || ''}` : (item.ticketTypeName || '标准大桶水') }} x {{ item.deliveryQty ?? item.quantity ?? 1 }}桶</van-tag>
              <van-tag plain type="warning">♻️ 回收空桶: {{ item.recycleQty ?? item.recycledBarrels ?? 0 }}个</van-tag>
              <van-tag
                v-if="item.aiInspectStatus || item.recycleDetail"
                :type="(item.aiInspectStatus || item.recycleDetail?.qualityStatus) === 'PASSED' ? 'success' : ((item.aiInspectStatus || item.recycleDetail?.qualityStatus) === 'NEED_CLEAN' ? 'warning' : 'danger')"
              >
                AI质检: {{ getQualityText(item.aiInspectStatus || item.recycleDetail?.qualityStatus) }}
              </van-tag>
            </div>
          </div>

          <div class="item-time">送达时间：{{ formatTime(item.deliveryTime || item.finishTime || item.createTime) }}</div>
        </div>
      </div>
    </van-pull-refresh>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { getMyDeliveryTasks } from '@/api'

const loading = ref(false)
const refreshing = ref(false)
const records = ref([])

const courierInfo = reactive(
  JSON.parse(localStorage.getItem('courier_info') || '{}')
)

const formatTime = (timeStr) => {
  if (!timeStr) return '-'
  if (typeof timeStr === 'string' && (timeStr.includes('T') || timeStr.includes('+') || timeStr.includes('.'))) {
    const d = new Date(timeStr)
    if (!isNaN(d.getTime())) {
      const pad = (n) => String(n).padStart(2, '0')
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
    }
  }
  return timeStr
}

const totalDelivered = computed(() => {
  return records.value.reduce((acc, cur) => acc + (cur.deliveryQty ?? cur.quantity ?? 0), 0)
})

const totalRecycled = computed(() => {
  return records.value.reduce((acc, cur) => acc + (cur.recycleQty ?? cur.recycledBarrels ?? 0), 0)
})

const getQualityText = (st) => {
  switch (st) {
    case 'PASSED': return '合格'
    case 'NEED_CLEAN': return '需清洗'
    case 'SCRAP': return '报废扣押金'
    default: return '正常'
  }
}

const fetchRecords = async () => {
  loading.value = true
  try {
    const res = await getMyDeliveryTasks({
      pageNum: 1,
      pageSize: 50,
      courierId: courierInfo.courierId,
      status: 'COMPLETED'
    })
    records.value = res?.records || []
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
    refreshing.value = false
  }
}

const onRefresh = () => {
  fetchRecords()
}

onMounted(() => {
  fetchRecords()
})
</script>

<style scoped>
.records-page {
  padding-bottom: 20px;
}

.summary-card {
  background: linear-gradient(135deg, #11998e, #38ef7d);
  color: #fff;
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 18px 12px;
  margin: 12px;
  border-radius: 12px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-item .num {
  font-size: 24px;
  font-weight: bold;
}

.stat-item .label {
  font-size: 12px;
  opacity: 0.9;
  margin-top: 4px;
}

.divider {
  width: 1px;
  height: 30px;
  background: rgba(255, 255, 255, 0.4);
}

.record-list {
  padding: 0 12px;
}

.record-item {
  background: #ffffff;
  border-radius: 10px;
  padding: 14px;
  margin-bottom: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
}

.item-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #f2f3f5;
  padding-bottom: 8px;
  margin-bottom: 8px;
}

.order-no {
  font-size: 13px;
  color: #909399;
}

.client-name {
  font-size: 15px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 4px;
}

.client-addr {
  font-size: 13px;
  color: #606266;
  margin-bottom: 8px;
}

.detail-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 8px;
}

.item-time {
  font-size: 12px;
  color: #909399;
  text-align: right;
}
</style>
