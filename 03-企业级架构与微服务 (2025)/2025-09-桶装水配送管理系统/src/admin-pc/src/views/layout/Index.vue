<template>
  <el-container class="layout-container">
    <el-aside width="240px" class="layout-aside">
      <div class="sidebar-logo">
        <span class="logo-icon">💧</span>
        <span class="logo-text">桶装水管理系统</span>
      </div>

      <el-menu
        :default-active="activeMenu"
        class="layout-menu"
        router
        background-color="#001529"
        text-color="#c0c4cc"
        active-text-color="#409eff"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>运营监控看板</span>
        </el-menu-item>

        <el-menu-item index="/couriers">
          <el-icon><Van /></el-icon>
          <span>送水工与片区</span>
        </el-menu-item>

        <el-menu-item index="/customers">
          <el-icon><UserFilled /></el-icon>
          <span>客户档案与水票</span>
        </el-menu-item>

        <el-menu-item index="/ticket-flow">
          <el-icon><Tickets /></el-icon>
          <span>水票审计流水</span>
        </el-menu-item>

        <el-menu-item index="/history">
          <el-icon><List /></el-icon>
          <span>配送核销记录</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container class="main-container">
      <el-header class="layout-header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentRouteTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="header-right">
          <el-tag type="info" effect="plain" class="role-badge">
            {{ userInfo.roleCode === 'ADMIN' ? '总站管理员' : '调度员' }}
          </el-tag>

          <el-dropdown trigger="click" @command="handleCommand">
            <span class="user-profile">
              <el-avatar size="small" style="background-color: #409eff;">
                {{ userInfo.realName ? userInfo.realName.substring(0, 1) : '管' }}
              </el-avatar>
              <span class="username">{{ userInfo.realName || userInfo.username }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="password">修改密码</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>

    <!-- 修改密码对话框 -->
    <el-dialog v-model="pwdDialogVisible" title="修改登录密码" width="420px" destroy-on-close>
      <el-form ref="pwdFormRef" :model="pwdForm" :rules="pwdRules" label-width="90px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="pwdForm.oldPassword" type="password" show-password placeholder="请输入原密码" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="pwdForm.newPassword" type="password" show-password placeholder="请输入6位及以上新密码" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="pwdForm.confirmPassword" type="password" show-password placeholder="请再次确认新密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="pwdLoading" @click="handleUpdatePassword">确认修改</el-button>
      </template>
    </el-dialog>
  </el-container>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { updatePassword } from '@/api'

const route = useRoute()
const router = useRouter()

const activeMenu = computed(() => route.path)
const currentRouteTitle = computed(() => route.meta.title || '')

const userInfo = reactive(JSON.parse(localStorage.getItem('water_user') || '{"username":"admin","realName":"系统管理员","roleCode":"ADMIN"}'))

// 修改密码
const pwdDialogVisible = ref(false)
const pwdLoading = ref(false)
const pwdFormRef = ref(null)
const pwdForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const pwdRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== pwdForm.newPassword) {
          callback(new Error('两次输入的新密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const handleCommand = (cmd) => {
  if (cmd === 'password') {
    pwdForm.oldPassword = ''
    pwdForm.newPassword = ''
    pwdForm.confirmPassword = ''
    pwdDialogVisible.value = true
  } else if (cmd === 'logout') {
    ElMessageBox.confirm('确定要退出当前管理系统吗？', '提示', {
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    }).then(() => {
      localStorage.removeItem('water_token')
      localStorage.removeItem('water_user')
      ElMessage.success('已安全退出登录')
      router.push('/login')
    }).catch(() => {})
  }
}

const handleUpdatePassword = async () => {
  if (!pwdFormRef.value) return
  await pwdFormRef.value.validate(async (valid) => {
    if (!valid) return
    pwdLoading.value = true
    try {
      await updatePassword({
        oldPassword: pwdForm.oldPassword,
        newPassword: pwdForm.newPassword
      })
      ElMessage.success('密码修改成功，请重新登录')
      pwdDialogVisible.value = false
      localStorage.removeItem('water_token')
      localStorage.removeItem('water_user')
      router.push('/login')
    } catch (e) {
      // Handled in interceptor
    } finally {
      pwdLoading.value = false
    }
  })
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

.layout-aside {
  background-color: #001529;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
  z-index: 10;
}

.sidebar-logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  font-size: 16px;
  font-weight: bold;
  border-bottom: 1px solid #002140;
}

.logo-icon {
  font-size: 22px;
  margin-right: 8px;
}

.layout-menu {
  border-right: none;
  flex: 1;
}

.main-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

.layout-header {
  height: 60px;
  background-color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  z-index: 9;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.role-badge {
  font-size: 12px;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background 0.2s;
}

.user-profile:hover {
  background-color: #f0f2f5;
}

.username {
  font-size: 14px;
  color: #333333;
}

.layout-main {
  padding: 20px;
  background-color: #f0f2f5;
  overflow-y: auto;
  flex: 1;
}
</style>
