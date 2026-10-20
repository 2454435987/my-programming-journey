<template>
  <div class="verify-page">
    <van-nav-bar title="现场核销与AI空桶质检" left-arrow @click-left="router.back()" fixed placeholder />

    <div class="verify-body">
      <!-- 1. 客户信息选择与余额卡片 -->
      <div class="section-card customer-card">
        <div class="section-title">
          <span>👤 配送客户信息</span>
          <van-button size="mini" plain type="primary" @click="showCustomerPicker = true">切换/查找客户</van-button>
        </div>

        <div v-if="selectedCustomer.id" class="customer-detail">
          <div class="row">
            <span class="name">{{ selectedCustomer.customerName }}</span>
            <span class="phone">{{ selectedCustomer.phone }}</span>
          </div>
          <div class="addr">{{ selectedCustomer.address }}</div>
          <div class="ticket-balance-box">
            <span>当前水票余量：</span>
            <van-tag :type="currentBalance > 0 ? 'success' : 'danger'" size="medium">
              剩余 {{ currentBalance }} 张
            </van-tag>
            <span v-if="currentBalance <= 0" class="warn-tip">（水票不足，请提醒充值）</span>
          </div>
        </div>
        <div v-else class="no-customer" @click="showCustomerPicker = true">
          <span>点击快速选择或搜索现场客户 🔍</span>
        </div>
      </div>

      <!-- 2. 本次配送水种与数量 -->
      <div class="section-card">
        <div class="section-title">💧 配送与回收数量</div>
        <van-cell-group inset>
          <van-field
            v-model="ticketTypeName"
            is-link
            readonly
            label="水种规格"
            placeholder="请选择送达的水种"
            @click="showWaterPicker = true"
          />
          <van-cell title="本次送水 (桶)">
            <template #right-icon>
              <van-stepper v-model="deliveryQty" :min="1" :max="20" />
            </template>
          </van-cell>
          <van-cell title="现场回收空桶 (个)">
            <template #right-icon>
              <van-stepper v-model="recycleQty" :min="0" :max="20" />
            </template>
          </van-cell>
        </van-cell-group>
      </div>

      <!-- 3. AI 空桶多模态视觉大模型智能质检 (核心特色模块) -->
      <div class="section-card ai-inspect-card">
        <div class="section-title">
          <span>🤖 AI 视觉空桶智能质检</span>
          <van-tag type="primary" effect="dark">通义千问VL驱动</van-tag>
        </div>
        <div class="ai-desc">
          对回收的空桶进行视觉缺陷检测，秒级判定是否有凹陷破损或青苔污渍。
        </div>

        <!-- 演示场景选择标签 (方便快速答辩/实测) -->
        <div class="sample-picker-title">✨ 一键快速测试典型样本（免找实物）：</div>
        <div class="sample-buttons">
          <van-button
            size="small"
            :type="sampleType === 'PASSED' ? 'success' : 'default'"
            round
            @click="selectSample('PASSED')"
          >
            🟢 样本1: 完好合格桶
          </van-button>
          <van-button
            size="small"
            :type="sampleType === 'NEED_CLEAN' ? 'warning' : 'default'"
            round
            @click="selectSample('NEED_CLEAN')"
          >
            🟡 样本2: 内壁青苔桶
          </van-button>
          <van-button
            size="small"
            :type="sampleType === 'SCRAP' ? 'danger' : 'default'"
            round
            @click="selectSample('SCRAP')"
          >
            🔴 样本3: 破裂凹陷桶
          </van-button>
        </div>

        <!-- 或者现场拍照上传 -->
        <div class="upload-box">
          <div class="upload-label">或送水工手机现场拍照：</div>
          <van-uploader
            v-model="fileList"
            :max-count="1"
            :after-read="afterPhotoRead"
            upload-text="拍照/上传空桶照片"
          />
        </div>

        <!-- 质检触发按钮 -->
        <div style="margin-top: 14px;">
          <van-button
            block
            round
            type="primary"
            color="linear-gradient(to right, #4bb0ff, #6149f6)"
            :loading="aiAnalyzing"
            loading-text="大模型多模态视觉分析中..."
            @click="runAiInspection"
          >
            📸 启动 AI 视觉多模态质检分析
          </van-button>
        </div>

        <!-- 质检分析结果展示卡片 -->
        <div v-if="aiResult" class="ai-result-panel" :class="getAiResultClass()">
          <div class="result-header">
            <span class="result-title">质检判定：{{ aiResult.statusLabel || getAiStatusText(aiResult.status) }}</span>
            <van-tag :type="getAiTagType(aiResult.status)" effect="dark" size="medium">
              {{ aiResult.status }}
            </van-tag>
          </div>

          <div class="result-detail">
            <p><strong>缺陷诊断：</strong>{{ aiResult.description || aiResult.damageReason }}</p>
            <p v-if="(aiResult.suggestDeductDeposit || aiResult.deductDeposit) > 0" class="deduct-text">
              <strong>扣除押金建议：</strong>¥{{ aiResult.suggestDeductDeposit || aiResult.deductDeposit }} 元
            </p>
          </div>
        </div>
      </div>

      <!-- 4. 底部最终提交核销按钮 -->
      <div class="submit-section">
        <van-button
          block
          round
          type="success"
          size="large"
          :loading="submitting"
          color="#07c160"
          @click="submitVerification"
        >
          ✓ 确认现场核销扣票 (送水{{ deliveryQty }}桶)
        </van-button>
      </div>
    </div>

    <!-- 客户选择弹窗 -->
    <van-popup v-model:show="showCustomerPicker" position="bottom" round :style="{ height: '65%' }">
      <div class="popup-container">
        <van-search v-model="customerSearchKeyword" placeholder="搜索客户姓名或手机号" @update:model-value="onSearchCustomer" />
        <div class="customer-picker-list">
          <van-cell
            v-for="c in candidateCustomers"
            :key="c.id"
            :title="`${c.customerName} (${c.phone})`"
            :label="`${c.address} · 剩余水票: ${c.totalBalance}张`"
            is-link
            @click="onPickCustomer(c)"
          />
        </div>
      </div>
    </van-popup>

    <!-- 水种规格选择弹窗 -->
    <van-action-sheet
      v-model:show="showWaterPicker"
      :actions="waterActions"
      cancel-text="取消"
      description="请选择本次核销配送的水种"
      close-on-click-action
      @select="onPickWater"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast, showSuccessToast, showDialog } from 'vant'
import {
  searchCustomer,
  getCustomerTickets,
  inspectBarrel,
  verifyAndDeliver
} from '@/api'

const router = useRouter()
const route = useRoute()

// 选中的客户与水种
const selectedCustomer = ref({})
const currentBalance = ref(0)
const ticketTypeId = ref(1)
const ticketTypeName = ref('农夫山泉 19L大桶水')
const deliveryQty = ref(1)
const recycleQty = ref(1)
const currentOrderId = ref(null)

// 客户选择弹层
const showCustomerPicker = ref(false)
const customerSearchKeyword = ref('')
const candidateCustomers = ref([])

// 水种选择弹层
const showWaterPicker = ref(false)
const waterActions = ref([
  { id: 1, name: '农夫山泉 19L天然水' },
  { id: 2, name: '怡宝 18.9L纯净水' },
  { id: 3, name: '娃哈哈 19L纯净水' },
  { id: 4, name: '景田 18.9L矿泉水' }
])

// AI质检状态
const sampleType = ref('PASSED')
const fileList = ref([])
const photoFile = ref(null)
const aiAnalyzing = ref(false)
const aiResult = ref(null)

// 提交状态
const submitting = ref(false)

const courierInfo = reactive(
  JSON.parse(localStorage.getItem('courier_info') || '{}')
)

const selectSample = (type) => {
  sampleType.value = type
  fileList.value = []
  photoFile.value = null
  showToast({ message: `已选用测试样本：${type === 'PASSED' ? '完好桶' : (type === 'NEED_CLEAN' ? '青苔桶' : '破裂桶')}`, duration: 1000 })
}

const afterPhotoRead = (file) => {
  photoFile.value = file.file
  sampleType.value = ''
}

const runAiInspection = async () => {
  aiAnalyzing.value = true
  try {
    const formData = new FormData()
    if (photoFile.value) {
      formData.append('file', photoFile.value)
    }
    if (sampleType.value) {
      formData.append('sampleType', sampleType.value)
    }
    const res = await inspectBarrel(formData)
    aiResult.value = res
    showSuccessToast('AI视觉分析完成！')
  } catch (err) {
    console.error(err)
  } finally {
    aiAnalyzing.value = false
  }
}

const getAiStatusText = (st) => {
  switch (st) {
    case 'PASSED': return '合格 (直接回收)'
    case 'NEED_CLEAN': return '需高温清洗消毒'
    case 'SCRAP': return '报废 (建议扣除押金)'
    default: return st || '合格'
  }
}

const getAiTagType = (st) => {
  switch (st) {
    case 'PASSED': return 'success'
    case 'NEED_CLEAN': return 'warning'
    case 'SCRAP': return 'danger'
    default: return 'primary'
  }
}

const getAiResultClass = () => {
  if (!aiResult.value) return ''
  const s = aiResult.value.status || aiResult.value.qualityStatus
  switch (s) {
    case 'PASSED': return 'result-passed'
    case 'NEED_CLEAN': return 'result-clean'
    case 'SCRAP': return 'result-scrap'
    default: return ''
  }
}

const onSearchCustomer = async (keyword) => {
  try {
    const list = await searchCustomer({ keyword: keyword || '' })
    candidateCustomers.value = (list || []).map(c => ({
      id: c.id,
      customerName: c.name || c.customerName,
      phone: c.phone,
      address: c.addressDetail || c.address,
      totalBalance: c.totalBalance ?? 0
    }))
  } catch (e) {
    console.error(e)
  }
}

const onPickCustomer = async (cust) => {
  selectedCustomer.value = cust
  showCustomerPicker.value = false
  await refreshCustomerBalance(cust.id)
}

const refreshCustomerBalance = async (custId) => {
  try {
    const tickets = await getCustomerTickets(custId)
    const match = (tickets || []).find(t => t.typeId === ticketTypeId.value || t.ticketTypeId === ticketTypeId.value)
    currentBalance.value = match ? match.balance : 0
  } catch (e) {
    console.error(e)
  }
}

const onPickWater = async (action) => {
  ticketTypeId.value = action.id
  ticketTypeName.value = action.name
  if (selectedCustomer.value.id) {
    await refreshCustomerBalance(selectedCustomer.value.id)
  }
}

const submitVerification = async () => {
  if (!selectedCustomer.value.id) {
    showToast('请先选择核销客户')
    return
  }

  if (currentBalance.value < deliveryQty.value) {
    showDialog({
      title: '水票不足提醒',
      message: `客户当前【${ticketTypeName.value}】水票仅剩 ${currentBalance.value} 张，不足以核销本次送水的 ${deliveryQty.value} 桶。请提醒客户先行充值！`
    })
    return
  }

  // 整理AI质检数据
  let finalStatus = aiResult.value ? (aiResult.value.status || aiResult.value.qualityStatus) : 'PASSED'
  let finalDesc = aiResult.value ? (aiResult.value.description || aiResult.value.damageReason) : '未上传异常照片，现场目测合格'
  let finalImg = aiResult.value ? (aiResult.value.inspectImageUrl || aiResult.value.photoUrl) : ''
  let finalDeduct = aiResult.value ? (aiResult.value.suggestDeductDeposit || aiResult.value.deductDeposit || 0) : 0

  submitting.value = true
  try {
    await verifyAndDeliver({
      orderId: currentOrderId.value,
      customerId: selectedCustomer.value.id,
      courierId: courierInfo.courierId || 1,
      typeId: ticketTypeId.value,
      deliveryQty: deliveryQty.value,
      recycleQty: recycleQty.value,
      aiInspectStatus: finalStatus,
      aiInspectDesc: finalDesc,
      inspectImageUrl: finalImg,
      suggestDeductDeposit: finalDeduct
    })

    showSuccessToast('核销成功！水票已扣减，空桶已入账。')
    setTimeout(() => {
      router.push('/records')
    }, 1200)
  } catch (e) {
    console.error(e)
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  // 预载路由传参
  if (route.query.customerId) {
    selectedCustomer.value = {
      id: Number(route.query.customerId),
      customerName: route.query.customerName,
      phone: route.query.customerPhone,
      address: '指定待配任务客户'
    }
    if (route.query.orderId) {
      currentOrderId.value = Number(route.query.orderId)
    }
    if (route.query.ticketTypeId) {
      ticketTypeId.value = Number(route.query.ticketTypeId)
    }
    if (route.query.ticketTypeName) {
      ticketTypeName.value = route.query.ticketTypeName
    }
    if (route.query.quantity) {
      deliveryQty.value = Number(route.query.quantity)
    }
    await refreshCustomerBalance(selectedCustomer.value.id)
  } else {
    // 默认加载首位客户
    await onSearchCustomer('')
    if (candidateCustomers.value.length > 0) {
      await onPickCustomer(candidateCustomers.value[0])
    }
  }
})
</script>

<style scoped>
.verify-page {
  padding-bottom: 30px;
}

.verify-body {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.section-card {
  background: #ffffff;
  border-radius: 12px;
  padding: 14px 12px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.section-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 15px;
  font-weight: 700;
  color: #323233;
  margin-bottom: 10px;
}

.customer-detail .row {
  display: flex;
  justify-content: space-between;
  font-size: 16px;
  font-weight: 600;
  color: #1f2d3d;
  margin-bottom: 4px;
}

.customer-detail .addr {
  font-size: 13px;
  color: #646566;
  margin-bottom: 8px;
}

.ticket-balance-box {
  background: #f0f9eb;
  padding: 8px 12px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #333;
}

.warn-tip {
  color: #f56c6c;
  font-size: 12px;
}

.no-customer {
  padding: 18px;
  text-align: center;
  color: #1989fa;
  font-size: 14px;
  border: 1px dashed #1989fa;
  border-radius: 8px;
}

.ai-inspect-card {
  border: 1px solid #d4e8fe;
}

.ai-desc {
  font-size: 12px;
  color: #7d7e80;
  margin-bottom: 12px;
}

.sample-picker-title {
  font-size: 13px;
  font-weight: 600;
  color: #323233;
  margin-bottom: 8px;
}

.sample-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 14px;
}

.upload-box {
  margin-top: 10px;
}

.upload-label {
  font-size: 12px;
  color: #646566;
  margin-bottom: 6px;
}

.ai-result-panel {
  margin-top: 14px;
  padding: 12px 14px;
  border-radius: 8px;
  border: 1px solid #ebedf0;
}

.result-passed {
  background: #f0f9eb;
  border-color: #e1f3d8;
}

.result-clean {
  background: #fdf6ec;
  border-color: #faecd8;
}

.result-scrap {
  background: #fef0f0;
  border-color: #fde2e2;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.result-title {
  font-size: 15px;
  font-weight: 700;
  color: #323233;
}

.result-detail p {
  font-size: 13px;
  line-height: 1.6;
  color: #4f5358;
}

.deduct-text {
  color: #ee0a24;
  font-size: 14px;
}

.submit-section {
  margin-top: 10px;
}

.popup-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.customer-picker-list {
  flex: 1;
  overflow-y: auto;
}
</style>
