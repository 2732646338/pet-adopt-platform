<template>
  <div>
    <!-- 顶部导航栏 -->
    <el-header class="header">
      <div class="nav">
        <div class="logo" @click="$router.push('/')">宠物领养平台</div>
        <el-menu
          mode="horizontal"
          :default-active="activeMenu"
          class="nav-menu"
          router
        >
          <el-menu-item index="/">首页</el-menu-item>
          <!-- 登录之后才显示 -->
          <el-menu-item v-if="userStore.token" index="/personal">个人中心</el-menu-item>
          <!-- 管理员才显示后台入口 -->
          <el-menu-item v-if="userStore.token && userStore.userInfo.role === 'admin'" index="/admin">管理员后台</el-menu-item>
        </el-menu>

        <div class="login-box">
          <span v-if="userStore.token">欢迎，{{ userStore.userInfo.username }}</span>
          <el-button v-else text @click="$router.push('/login')">登录</el-button>
          <el-button v-if="!userStore.token" text @click="$router.push('/register')">注册</el-button>
        </div>
      </div>
    </el-header>

    <!-- 路由页面展示位置 -->
    <el-main>
      <router-view />
    </el-main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from './stores/user'

const route = useRoute()
const userStore = useUserStore()
// 当前激活菜单
const activeMenu = computed(() => route.path)
</script>

<style>
:root {
  --warm-primary: #e8945a;
  --warm-primary-dark: #d97c3e;
  --warm-bg: #fdf6ee;
  --warm-card: #fffaf3;
  --warm-text: #5d4a3a;
  --warm-text-light: #9c8577;
}
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
body {
  background-color: var(--warm-bg);
  color: var(--warm-text);
  font-family: "PingFang SC", "Microsoft YaHei", "Helvetica Neue", sans-serif;
}
.el-main {
  background-color: var(--warm-bg);
  min-height: calc(100vh - 60px);
}
.header {
  background: linear-gradient(90deg, #fff8f0 0%, #ffefe0 100%);
  box-shadow: 0 2px 12px rgba(232, 148, 90, 0.15);
  padding: 0 40px;
  border-bottom: 1px solid #f5e0cc;
}
.nav {
  display: flex;
  height: 100%;
  align-items: center;
}
.logo {
  font-size: 20px;
  font-weight: bold;
  color: var(--warm-primary-dark);
  cursor: pointer;
  margin-right: 40px;
  letter-spacing: 1px;
}
.logo::before {
  content: "🐾 ";
}
.nav-menu {
  flex: 1;
  border-bottom: none;
  background: transparent;
}
.nav-menu .el-menu-item {
  color: var(--warm-text);
}
.nav-menu .el-menu-item:hover,
.nav-menu .el-menu-item.is-active {
  color: var(--warm-primary-dark) !important;
  background-color: rgba(232, 148, 90, 0.12) !important;
  border-bottom-color: var(--warm-primary) !important;
}
.login-box {
  display: flex;
  gap:10px;
  align-items:center;
  color: var(--warm-text-light);
}
.login-box .el-button {
  color: var(--warm-primary-dark);
}
/* 温馨风格：主要按钮统一暖橙 */
.el-button--primary {
  --el-button-bg-color: var(--warm-primary);
  --el-button-border-color: var(--warm-primary);
  --el-button-hover-bg-color: var(--warm-primary-dark);
  --el-button-hover-border-color: var(--warm-primary-dark);
  --el-button-active-bg-color: var(--warm-primary-dark);
}
/* 卡片统一柔和圆角 */
.el-card {
  border-radius: 14px;
  border: 1px solid #f3e3d3;
  background-color: var(--warm-card);
  box-shadow: 0 2px 10px rgba(232, 148, 90, 0.08);
}
</style>