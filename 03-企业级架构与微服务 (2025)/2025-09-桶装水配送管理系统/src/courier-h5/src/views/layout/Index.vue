<template>
  <div class="h5-layout-page">
    <div class="content-view">
      <router-view />
    </div>

    <van-tabbar v-model="activeTab" route fixed placeholder>
      <van-tabbar-item replace to="/tasks" icon="todo-list-o">待配送</van-tabbar-item>
      <van-tabbar-item replace to="/verify" icon="scan">核销·AI质检</van-tabbar-item>
      <van-tabbar-item replace to="/records" icon="chart-trending-o">我的业绩</van-tabbar-item>
      <van-tabbar-item replace to="/profile" icon="user-o">个人中心</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const activeTab = ref(0)
</script>

<style scoped>
.h5-layout-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.content-view {
  flex: 1;
  padding-bottom: 50px;
}
</style>
