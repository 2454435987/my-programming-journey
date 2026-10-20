<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="aside">
      <div class="logo">
        <el-icon size="26" color="#74c69d"><Mug /></el-icon>
        <span class="logo-text">茶文化管理后台</span>
      </div>
      <el-menu
        :default-active="route.path"
        router
        background-color="#1b4332"
        text-color="#b7e4c7"
        active-text-color="#fff"
        class="menu"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>数据统计</span>
        </el-menu-item>
        <el-menu-item index="/users">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-sub-menu index="content">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>内容管理</span>
          </template>
          <el-menu-item index="/content/knowledge">知识管理</el-menu-item>
          <el-menu-item index="/content/courses">教程管理</el-menu-item>
          <el-menu-item index="/content/categories">分类管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="mall">
          <template #title>
            <el-icon><ShoppingCart /></el-icon>
            <span>商品与订单</span>
          </template>
          <el-menu-item index="/mall/products">商品管理</el-menu-item>
          <el-menu-item index="/mall/orders">订单管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="community">
          <template #title>
            <el-icon><ChatDotRound /></el-icon>
            <span>社区管理</span>
          </template>
          <el-menu-item index="/community/posts">帖子管理</el-menu-item>
          <el-menu-item index="/community/comments">评论管理</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/activities">
          <el-icon><Flag /></el-icon>
          <span>活动管理</span>
        </el-menu-item>
        <el-sub-menu index="operation">
          <template #title>
            <el-icon><Operation /></el-icon>
            <span>运营管理</span>
          </template>
          <el-menu-item index="/operation/banners">Banner管理</el-menu-item>
          <el-menu-item index="/operation/messages">消息通知</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/system/admins">管理员管理</el-menu-item>
          <el-menu-item index="/system/roles">角色管理</el-menu-item>
          <el-menu-item index="/system/menus">菜单管理</el-menu-item>
          <el-menu-item index="/system/logs">操作日志</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-title">{{ route.meta.title || '茶文化主题App管理后台' }}</div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" style="background:#2d6a4f">{{ adminName.charAt(0) }}</el-avatar>
              <span class="admin-name">{{ adminName }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getAdminInfo, removeToken, removeAdminInfo } from '@/utils/auth'

const route = useRoute()
const router = useRouter()
const adminName = computed(() => getAdminInfo().realName || '管理员')

function handleCommand(cmd) {
  if (cmd === 'logout') {
    removeToken()
    removeAdminInfo()
    router.push('/login')
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}
.aside {
  background: #1b4332;
  overflow-y: auto;
}
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  background: #143226;
}
.logo-text {
  color: #95d5b2;
  font-size: 16px;
  font-weight: 700;
  letter-spacing: 1px;
}
.menu {
  border-right: none;
}
.header {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}
.header-title {
  font-size: 17px;
  font-weight: 600;
  color: #303133;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #303133;
}
.admin-name {
  font-size: 14px;
}
.main {
  padding: 16px;
  overflow-y: auto;
  background: #f5f7fa;
}
</style>
