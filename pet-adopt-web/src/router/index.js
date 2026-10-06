import { createRouter,createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

// 页面组件
import Home from '../views/Home.vue'
import PetDetail from '../views/PetDetail.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Personal from '../views/Personal.vue'
import Admin from '../views/Admin.vue'

const routes = [
  {path:'/',name:'首页',component:Home},
  {path:'/pet/:id',name:'宠物详情',component:PetDetail},
  {path:'/login',name:'登录',component:Login},
  {path:'/register',name:'注册',component:Register},
  {path:'/personal',name:'个人中心',component:Personal,meta:{requireAuth:true}},
  {path:'/admin',name:'管理员后台',component:Admin,meta:{requireAuth:true}}
]

const router = createRouter({
  history:createWebHistory(),
  routes
})

// 路由守卫：需要登录的页面，没有token跳转登录
router.beforeEach((to,from,next)=>{
  const userStore = useUserStore()
  if(to.meta.requireAuth && !userStore.token){
    next('/login')
  }else{
    next()
  }
})
export default router