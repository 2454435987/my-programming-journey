<template>
  <div class="profile-page">
    <van-nav-bar title="个人中心" fixed placeholder />

    <!-- 个人基本卡片 -->
    <div class="user-card">
      <div class="avatar-box">👷‍♂️</div>
      <div class="user-info">
        <div class="name-line">
          <span class="user-name">{{ courierInfo.realName || '送水员' }}</span>
          <van-tag type="primary">送水工</van-tag>
        </div>
        <div class="courier-no">工号：{{ courierInfo.username || 'SG8001' }}</div>
      </div>
    </div>

    <!-- 业务功能单元格 -->
    <div class="menu-cells">
      <van-cell-group inset>
        <van-cell title="当前负责片区" :value="regionName" icon="location-o" />
        <van-cell title="系统状态" value="正常在职履约" icon="passed" />
        <van-cell title="修改登录密码" is-link icon="edit" @click="showPwdDialog = true" />
      </van-cell-group>
    </div>

    <!-- 退出登录 -->
    <div style="margin: 28px 16px;">
      <van-button round block type="danger" plain @click="handleLogout">
        退出登录
      </van-button>
    </div>

    <!-- 修改密码弹窗 -->
    <van-dialog
      v-model:show="showPwdDialog"
      title="修改登录密码"
      show-cancel-button
      :before-close="onBeforeClosePwd"
    >
      <van-form ref="pwdFormRef" style="padding: 14px 10px;">
        <van-field
          v-model="oldPassword"
          type="password"
          label="原密码"
          placeholder="请输入当前密码"
          :rules="[{ required: true, message: '请输入原密码' }]"
        />
        <van-field
          v-model="newPassword"
          type="password"
          label="新密码"
          placeholder="请输入新密码"
          :rules="[{ required: true, message: '请输入新密码' }]"
        />
      </van-form>
    </van-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showConfirmDialog, showSuccessToast, showToast } from 'vant'
import { getUserInfo, updatePassword } from '@/api'

const router = useRouter()
const showPwdDialog = ref(false)
const oldPassword = ref('')
const newPassword = ref('')
const regionName = ref('阳光花园片区')

const courierInfo = reactive(
  JSON.parse(localStorage.getItem('courier_info') || '{}')
)

const loadInfo = async () => {
  try {
    const info = await getUserInfo()
    if (info) {
      courierInfo.realName = info.realName
      courierInfo.username = info.username
      if (info.regionName) {
        regionName.value = info.regionName
      }
    }
  } catch (e) {
    console.error(e)
  }
}

const onBeforeClosePwd = async (action) => {
  if (action === 'confirm') {
    if (!oldPassword.value || !newPassword.value) {
      showToast('请完整填写新旧密码')
      return false
    }
    if (newPassword.value.length < 6) {
      showToast('新密码长度不能少于6位')
      return false
    }
    try {
      await updatePassword({
        oldPassword: oldPassword.value,
        newPassword: newPassword.value
      })
      showSuccessToast('密码修改成功，请重新登录')
      localStorage.removeItem('courier_token')
      localStorage.removeItem('courier_info')
      router.push('/login')
      return true
    } catch (e) {
      return false
    }
  }
  return true
}

const handleLogout = () => {
  showConfirmDialog({
    title: '退出提示',
    message: '确定退出送水工工作台吗？'
  }).then(() => {
    localStorage.removeItem('courier_token')
    localStorage.removeItem('courier_info')
    showSuccessToast('已退出')
    router.push('/login')
  }).catch(() => {})
}

onMounted(() => {
  loadInfo()
})
</script>

<style scoped>
.profile-page {
  padding-bottom: 20px;
}

.user-card {
  background: #ffffff;
  margin: 12px;
  padding: 20px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.avatar-box {
  width: 60px;
  height: 60px;
  line-height: 60px;
  font-size: 34px;
  text-align: center;
  background: #e8f4ff;
  border-radius: 50%;
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.name-line {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-name {
  font-size: 18px;
  font-weight: 700;
  color: #323233;
}

.courier-no {
  font-size: 13px;
  color: #969799;
}

.menu-cells {
  margin-top: 14px;
}
</style>
