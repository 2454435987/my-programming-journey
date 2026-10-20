<template>
  <div class="h5-login-container">
    <div class="login-banner">
      <div class="banner-icon">🚚</div>
      <h2>送水工移动端工作台</h2>
      <p>现场核销 · 空桶质检 · 智能路线</p>
    </div>

    <div class="form-container">
      <van-form @submit="onSubmit">
        <van-cell-group inset>
          <van-field
            v-model="username"
            name="username"
            label="送水工号"
            placeholder="请输入工号 (如 SG8001)"
            left-icon="contact"
            :rules="[{ required: true, message: '请填写送水工号' }]"
          />
          <van-field
            v-model="password"
            type="password"
            name="password"
            label="密码"
            placeholder="请输入密码 (如 123456)"
            left-icon="lock"
            :rules="[{ required: true, message: '请填写登录密码' }]"
          />
        </van-cell-group>

        <div style="margin: 24px 16px;">
          <van-button round block type="primary" native-type="submit" :loading="loading" color="#1989fa">
            立即登录工作台
          </van-button>
        </div>
      </van-form>

      <div class="demo-tip">
        <p>测试演示账号：</p>
        <p>东区负责人：<strong>SG8001</strong> / 123456 (张大力)</p>
        <p>西区负责人：<strong>SG8002</strong> / 123456 (王铁柱)</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast, showSuccessToast } from 'vant'
import { login } from '@/api'

const router = useRouter()
const username = ref('SG8001')
const password = ref('123456')
const loading = ref(false)

const onSubmit = async () => {
  loading.value = true
  try {
    const res = await login({
      username: username.value,
      password: password.value
    })
    localStorage.setItem('courier_token', res.token)
    localStorage.setItem('courier_info', JSON.stringify({
      userId: res.userId,
      username: res.username,
      realName: res.realName,
      roleCode: res.roleCode,
      courierId: res.courierId,
      regionId: res.regionId
    }))
    showSuccessToast(`欢迎您，${res.realName}！`)
    router.push('/tasks')
  } catch (err) {
    // Handled in axios interceptor
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.h5-login-container {
  min-height: 100vh;
  background: #f7f8fa;
  padding-bottom: 30px;
}

.login-banner {
  background: linear-gradient(135deg, #1989fa 0%, #0570db 100%);
  color: #fff;
  padding: 50px 20px 35px;
  text-align: center;
  border-bottom-left-radius: 24px;
  border-bottom-right-radius: 24px;
}

.banner-icon {
  font-size: 50px;
  margin-bottom: 12px;
}

.login-banner h2 {
  font-size: 22px;
  margin-bottom: 6px;
  font-weight: 600;
}

.login-banner p {
  font-size: 13px;
  opacity: 0.9;
}

.form-container {
  margin-top: -16px;
}

.demo-tip {
  margin: 20px 24px;
  background: #ffffff;
  padding: 14px 16px;
  border-radius: 10px;
  font-size: 12px;
  color: #646566;
  line-height: 1.8;
  border: 1px dashed #dcdee0;
}
</style>
