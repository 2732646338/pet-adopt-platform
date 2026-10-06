<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2 style="text-align: center;">宠物领养平台 - 注册</h2>
      <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" label-width="80px">
        <el-form-item label="账号" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="repassword">
          <el-input v-model="registerForm.repassword" type="password" placeholder="再次输入密码"></el-input>
        </el-form-item>
      </el-form>
      <el-form-item>
        <el-button type="primary" @click="handleRegister" class="register-btn">注册</el-button>
      </el-form-item>
      <div style="text-align:center">
        <span>已有账号？</span>
        <el-link type="primary" @click="$router.push('/login')">去登录</el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { getCurrentInstance } from 'vue'

const { proxy } = getCurrentInstance()
const router = useRouter()

const registerFormRef = ref(null)
const registerForm = ref({
  username: '',
  password: '',
  repassword: ''
})

// 自定义校验：两次密码一致
const validatePass2 = (rule, value, callback) => {
  if (value !== registerForm.value.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const registerRules = ref({
  username: [
    { required: true, message: '账号不能为空', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' },
    { pattern: /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,20}$/, message: '密码为6-20位字母和数字组合', trigger: 'blur' }
  ],
  repassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validatePass2, trigger: 'blur' }
  ]
})

const handleRegister = async () => {
  await registerFormRef.value.validate()
  try {
    // 只提交后端需要的字段（repassword 不传），昵称默认用账号
    const res = await proxy.$axios.post('/user/register', {
      username: registerForm.value.username,
      password: registerForm.value.password,
      nickname: registerForm.value.username
    })
    if (res.code === 200) {
      proxy.$message.success('注册成功，请登录')
      router.push('/login')
    } else {
      proxy.$message.error(res.msg || '注册失败')
    }
  } catch (e) {
    proxy.$message.error(e.response?.data?.msg || e.message || '注册失败')
  }
}
</script>

<style scoped>
.register-container {
  min-height: calc(100vh - 60px);
  background: linear-gradient(160deg, #fdf6ee 0%, #fdeedd 50%, #fce4cd 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}
.register-card {
  width: 440px;
  padding: 28px 24px;
  border-radius: 18px;
  border: 1px solid #f3e3d3;
  box-shadow: 0 10px 30px rgba(232, 148, 90, 0.16);
}
.register-card h2 {
  color: #d97c3e;
  margin-bottom: 20px;
}
.register-btn {
  width: 100%;
  border-radius: 20px;
  height: 42px;
  font-size: 16px;
  letter-spacing: 4px;
}
</style>