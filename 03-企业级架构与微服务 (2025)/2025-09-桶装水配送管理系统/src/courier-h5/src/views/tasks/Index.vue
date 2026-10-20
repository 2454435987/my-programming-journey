<template>
  <div class="tasks-page">
    <van-nav-bar title="今日待配送任务" fixed placeholder />

    <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
      <!-- 统计简报 -->
      <div class="task-summary-banner">
        <div class="summary-col">
          <span class="num">{{ taskList.length }}</span>
          <span class="label">当前任务</span>
        </div>
        <div class="summary-col">
          <span class="num">{{ totalBottles }}</span>
          <span class="label">待送桶数</span>
        </div>
        <div class="summary-col">
          <span class="num">{{ courierInfo.realName || '送水工' }}</span>
          <span class="label">{{ courierInfo.courierNo || '工号' }}</span>
        </div>
      </div>

      <!-- 任务卡片列表 -->
      <div class="task-list">
        <div v-if="taskList.length === 0 && !loading" class="empty-box">
          <van-empty description="当前片区暂无待送任务，可在“核销·AI质检”主动开单" />
        </div>

        <van-card
          v-for="item in taskList"
          :key="item.id"
          class="custom-task-card"
        >
          <template #title>
            <div class="card-header">
              <div class="customer-info">
                <span class="customer-name">{{ item.customerName }}</span>
                <van-tag :type="item.hasElevator === 1 ? 'success' : 'warning'" size="medium">
                  {{ item.hasElevator === 1 ? '🛗 有电梯' : `🏃 步梯 ${item.floorNumber || 1} 楼` }}
                </van-tag>
              </div>
              <van-tag type="primary" plain>{{ item.status === 'ASSIGNED' ? '待送达' : '配送中' }}</van-tag>
            </div>
          </template>

          <template #desc>
            <div class="card-body">
              <div class="info-row">
                <van-icon name="location-o" color="#1989fa" />
                <span class="addr-text">{{ item.address }}</span>
              </div>

              <div class="info-row">
                <van-icon name="phone-o" color="#07c160" />
                <a :href="`tel:${item.customerPhone}`" class="phone-link">
                  {{ item.customerPhone }} (点击呼叫)
                </a>
              </div>

              <div class="info-row highlight-row">
                <span class="item-badge">💧 {{ item.ticketTypeName }}</span>
                <span class="qty-badge">x {{ item.quantity }} 桶</span>
              </div>
            </div>
          </template>

          <template #footer>
            <div class="card-actions">
              <van-button
                size="small"
                type="primary"
                round
                color="#1989fa"
                @click="goToVerify(item)"
              >
                现场核销与AI质检
              </van-button>
            </div>
          </template>
        </van-card>
      </div>
    </van-pull-refresh>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getPendingTasks } from '@/api'

const router = useRouter()
const loading = ref(false)
const refreshing = ref(false)
const taskList = ref([])

const courierInfo = reactive(
  JSON.parse(localStorage.getItem('courier_info') || '{}')
)

const totalBottles = computed(() => {
  return taskList.value.reduce((sum, item) => sum + (item.totalBalance || 0), 0)
})

const fetchTasks = async () => {
  loading.value = true
  try {
    const list = await getPendingTasks(courierInfo.courierId || 1)
    taskList.value = (list || []).map(t => ({
      id: t.customerId,
      customerId: t.customerId,
      customerName: t.customerName,
      customerPhone: t.phone,
      address: t.addressDetail,
      floorNumber: t.floorNum,
      hasElevator: t.hasElevator,
      totalBalance: t.totalBalance,
      isWarning: t.isWarning,
      ticketTypeName: t.tickets && t.tickets.length > 0 ? `${t.tickets[0].brandName} ${t.tickets[0].specDesc}` : '标准19L大桶水',
      ticketTypeId: t.tickets && t.tickets.length > 0 ? t.tickets[0].typeId : 1,
      quantity: 1
    }))
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
    refreshing.value = false
  }
}

const onRefresh = () => {
  fetchTasks()
}

const goToVerify = (item) => {
  router.push({
    path: '/verify',
    query: {
      customerId: item.customerId,
      customerName: item.customerName,
      customerPhone: item.customerPhone,
      ticketTypeId: item.ticketTypeId,
      ticketTypeName: item.ticketTypeName,
      quantity: 1
    }
  })
}

onMounted(() => {
  fetchTasks()
})
</script>

<style scoped>
.tasks-page {
  padding-bottom: 20px;
}

.task-summary-banner {
  display: flex;
  justify-content: space-around;
  background: linear-gradient(135deg, #1989fa, #39a9ed);
  color: #fff;
  padding: 16px 12px;
  margin: 12px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(25, 137, 250, 0.2);
}

.summary-col {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.summary-col .num {
  font-size: 20px;
  font-weight: bold;
}

.summary-col .label {
  font-size: 12px;
  opacity: 0.9;
  margin-top: 2px;
}

.task-list {
  padding: 0 12px;
}

.custom-task-card {
  background: #ffffff;
  border-radius: 10px;
  margin-bottom: 12px;
  padding: 14px;
  border: 1px solid #ebedf0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.customer-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.customer-name {
  font-size: 16px;
  font-weight: 700;
  color: #323233;
}

.card-body {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin: 8px 0;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #646566;
}

.addr-text {
  color: #323233;
}

.phone-link {
  color: #07c160;
  font-weight: 600;
  text-decoration: underline;
}

.highlight-row {
  margin-top: 4px;
  background: #f7f8fa;
  padding: 6px 10px;
  border-radius: 6px;
  justify-content: space-between;
}

.item-badge {
  font-weight: 600;
  color: #323233;
}

.qty-badge {
  font-weight: 700;
  color: #1989fa;
  font-size: 14px;
}

.card-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 8px;
}
</style>
