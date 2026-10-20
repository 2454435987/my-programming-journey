<template>
  <div class="login-container">
    <div class="login-card">
      <div class="brand-header">
        <div class="logo-box">💧</div>
        <h2 class="system-title">桶装水配送管理系统</h2>
        <p class="system-subtitle">数字化运营与配送监控后台</p>
      </div>

      <el-form ref="formRef" :model="form" :rules="rules" class="login-form" size="large">
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入管理员账号 (如: admin)"
            :prefix-icon="User"
            clearable
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码 (如: 123456)"
            :prefix-icon="Lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-button
          type="primary"
          :loading="loading"
          class="submit-btn"
          @click="handleLogin"
        >
          立即登录系统
        </el-button>
      </el-form>

      <div class="login-footer">
        <div class="tip-badge">系统预设演示账号：admin / 123456</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { login } from '@/api'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: 'admin',
  password: '123456'
})

const rules = {
  username: [{ required: true, message: '请输入管理员账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入登录密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      const data = await login(form)
      localStorage.setItem('water_token', data.token)
      localStorage.setItem('water_user', JSON.stringify({
        userId: data.userId,
        username: data.username,
        realName: data.realName,
        roleCode: data.roleCode
      }))
      ElMessage.success(`欢迎回来，${data.realName}！`)
      router.push('/dashboard')
    } catch (err) {
      // Handled in interceptor
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.login-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
  position: relative;
  overflow: hidden;
}

.login-card {
  width: 440px;
  background: #ffffff;
  border-radius: 12px;
  padding: 40px 36px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
  z-index: 10;
}

.brand-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo-box {
  width: 60px;
  height: 60px;
  line-height: 60px;
  font-size: 32px;
  background: #ebf5ff;
  border-radius: 50%;
  margin: 0 auto 14px;
}

.system-title {
  font-size: 22px;
  font-weight: 700;
  color: #1a1a1a;
  margin-bottom: 6px;
}

.system-subtitle {
  font-size: 13px;
  color: #888888;
}

.login-form {
  margin-top: 10px;
}

.submit-btn {
  width: 100%;
  font-size: 15px;
  font-weight: 600;
  height: 44px;
  margin-top: 10px;
  background: #1e60d0;
  border-color: #1e60d0;
}

.submit-btn:hover {
  background: #2a70e5;
  border-color: #2a70e5;
}

.login-footer {
  margin-top: 25px;
  text-align: center;
}

.tip-badge {
  font-size: 12px;
  color: #606266;
  background: #f4f4f5;
  padding: 6px 12px;
  border-radius: 6px;
  display: inline-block;
}
</style>
