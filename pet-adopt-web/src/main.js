import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from './router'
import { createPinia } from 'pinia'
import axios from 'axios'

const app = createApp(App)
const pinia = createPinia()

// axios全局配置 + 请求拦截器（自动带token）
const service = axios.create({
  baseURL: 'http://localhost:8080/api', // 后端springboot地址！改成你后端端口
  timeout: 5000
})

// 请求拦截：每次请求自动带上token
service.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if(token && typeof token === 'string'){
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
},err=>{
  return Promise.reject(err)
})

// 响应拦截：业务数据直接返回 res.data；401无权限自动跳登录
service.interceptors.response.use(res=>{
  const data = res.data
  if(data && data.code === 401){
    router.push('/login')
  }
  return data
},err=>{
  if(err.response && err.response.status === 401){
    router.push('/login')
  }
  return Promise.reject(err)
})

app.config.globalProperties.$axios = service

app.use(ElementPlus).use(router).use(pinia).mount('#app')