<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 style="text-align: center;">宠物领养平台 - 登录</h2>
      <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" label-width="80px">
        <el-form-item label="账号" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" class="login-btn">登录</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align:center">
        <span>没有账号？</span>
        <el-link type="primary" @click="$router.push('/register')">去注册</el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { getCurrentInstance } from 'vue'

const { proxy } = getCurrentInstance()
const router = useRouter()
const userStore = useUserStore()

// 表单数据
const loginFormRef = ref(null)
const loginForm = ref({
  username: '',
  password: ''
})

// 表单校验规则
const loginRules = ref({
  username: [
    { required: true, message: '账号不能为空', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' },
    { pattern: /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,20}$/, message: '密码为6-20位字母和数字组合', trigger: 'blur' }
  ]
})

// 登录提交
const handleLogin = async () => {
  await loginFormRef.value.validate()
  try {
    // 后端登录接口返回的 data 直接是 token 字符串
    const res = await proxy.$axios.post('/user/login', {
      username: loginForm.value.username,
      password: loginForm.value.password
    })
    if (res.code === 200) {
      userStore.login(res.data, {})
      // 再查询用户信息（角色等）存入 store
      const infoRes = await proxy.$axios.get('/user/info')
      if (infoRes.code === 200) {
        userStore.setUserInfo(infoRes.data)
      }
      proxy.$message.success('登录成功')
      router.push('/')
    } else {
      proxy.$message.error(res.msg || '登录失败')
    }
  } catch (e) {
    proxy.$message.error(e.response?.data?.msg || e.message || '登录失败')
  }
}
</script>

<style scoped>
.login-container {
  min-height: calc(100vh - 60px);
  background: linear-gradient(160deg, #fdf6ee 0%, #fdeedd 50%, #fce4cd 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}
.login-card {
  width: 400px;
  padding: 28px 24px;
  border-radius: 18px;
  border: 1px solid #f3e3d3;
  box-shadow: 0 10px 30px rgba(232, 148, 90, 0.16);
}
.login-card h2 {
  color: #d97c3e;
  margin-bottom: 20px;
}
.login-btn {
  width: 100%;
  border-radius: 20px;
  height: 42px;
  font-size: 16px;
  letter-spacing: 4px;
}
.login-container :deep(.el-link) {
  color: #d97c3e;
}
</style>