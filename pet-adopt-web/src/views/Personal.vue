<template>
  <div class="personal-container">
    <el-card class="info-card">
      <template #header><h3>个人资料</h3></template>
      <el-row :gutter="40">
        <el-col :span="6">
          <div class="avatar-box">
            <!-- 头像预览 -->
            <el-avatar :size="120" :src="userInfo.avatar || undefined"></el-avatar>
            <el-upload
              class="avatar-uploader"
              :action="uploadUrl"
              :headers="uploadHeader"
              :show-file-list="false"
              @success="handleAvatarSuccess"
            >
              <el-button size="small" type="primary">更换头像</el-button>
            </el-upload>
          </div>
        </el-col>
        <el-col :span="16">
          <el-form ref="userFormRef" :model="userForm" label-width="100px">
            <el-form-item label="用户名">
              <el-input v-model="userForm.username" disabled></el-input>
            </el-form-item>
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="userForm.nickname"></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="userForm.phone"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="userForm.email"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="updateInfo">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-card>

    <!-- 我的收藏 -->
    <el-card class="card" style="margin-top:20px">
      <template #header><h3>我的收藏</h3></template>
      <div class="pet-list">
        <el-card class="pet-card" v-for="item in collectList" :key="item.id" @click="goPetDetail(item.petId)">
          <el-image :src="item.imgUrl" fit="cover" style="width:100%;height:140px">
            <template #error>
              <div style="width:100%;height:140px;display:flex;align-items:center;justify-content:center;background:#f5f7fa;color:#909399;font-size:13px">暂无图片</div>
            </template>
          </el-image>
          <p style="text-align:center;margin-top:8px">{{item.petName}}</p >
        </el-card>
      </div>
    </el-card>

    <!-- 我的领养申请 -->
    <el-card class="card" style="margin-top:20px">
      <template #header><h3>我的领养申请</h3></template>
      <el-table :data="adoptList" border stripe>
        <el-table-column label="宠物名称" prop="petName"></el-table-column>
        <el-table-column label="申请时间" prop="createTime"></el-table-column>
        <el-table-column label="申请状态">
          <template #default="scope">
            <el-tag :type="getStatusTag(scope.row.applyStatus)">
              {{ getStatusText(scope.row.applyStatus) }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 退出登录 -->
    <div style="text-align:center;margin:30px 0">
      <el-button type="danger" @click="handleLogout">退出登录</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { getCurrentInstance } from 'vue'

const { proxy } = getCurrentInstance()
const router = useRouter()
const userStore = useUserStore()

// 用户信息
const userInfo = ref(userStore.userInfo)
const userFormRef = ref(null)
const userForm = ref({
  username: '',
  nickname: '',
  phone: '',
  email: ''
})

// 拉取最新用户信息
const getUserInfo = async () => {
  const res = await proxy.$axios.get('/user/info')
  if (res.code === 200) {
    userInfo.value = res.data
    userStore.setUserInfo(res.data)
    userForm.value = {
      username: res.data.username,
      nickname: res.data.nickname,
      phone: res.data.phone,
      email: res.data.email
    }
  }
}

// 上传头像地址（后端上传接口）
const uploadUrl = 'http://localhost:8080/api/user/uploadAvatar'
const uploadHeader = {
  Authorization: `Bearer ${userStore.token}`
}

// 列表数据
const collectList = ref([])
const adoptList = ref([])

// 更新个人资料（后端只接收 nickname/phone/email）
const updateInfo = async () => {
  await userFormRef.value.validate()
  const res = await proxy.$axios.put('/user/update', {
    nickname: userForm.value.nickname,
    phone: userForm.value.phone,
    email: userForm.value.email
  })
  if (res.code === 200) {
    proxy.$message.success('资料修改成功')
    await getUserInfo()
  } else {
    proxy.$message.error(res.msg)
  }
}

// 头像上传成功回调
const handleAvatarSuccess = (res) => {
  if (res.code === 200) {
    userInfo.value.avatar = res.data
    userStore.userInfo.avatar = res.data
    localStorage.setItem('user', JSON.stringify(userStore.userInfo))
    proxy.$message.success('头像更换成功')
  }
}

// 获取我的收藏（后端为 POST）
const getCollectList = async () => {
  const res = await proxy.$axios.post('/collect/myList')
  if (res.code === 200) {
    collectList.value = res.data || []
  }
}

// 获取我的领养申请
const getAdoptList = async () => {
  const res = await proxy.$axios.get('/adopt/myApply')
  if (res.code === 200) {
    adoptList.value = res.data
  }
}

// 跳转宠物详情
const goPetDetail = (petId) => {
  router.push(`/pet/${petId}`)
}

// 状态文字和tag类型
const getStatusText = (val) => {
  const map = {0:'待审核',1:'审核通过',2:'审核拒绝'}
  return map[val]
}
const getStatusTag = (val) => {
  const map = {0:'warning',1:'success',2:'danger'}
  return map[val]
}

// 退出登录
const handleLogout = async () => {
  try {
    await proxy.$axios.post('/user/logout')
  } catch (e) {
    // 即使后端退出失败，前端也清除登录态
  }
  userStore.logout()
  proxy.$message.success('已退出登录')
  router.push('/login')
}

onMounted(() => {
  getUserInfo()
  getCollectList()
  getAdoptList()
})
</script>

<style scoped>
.personal-container {
  width: 90%;
  margin: 30px auto;
}
.personal-container :deep(.el-card) {
  border-radius: 16px;
}
.personal-container :deep(.el-card__header) {
  background: linear-gradient(90deg, #fff2e4, #ffe8d2);
  color: #d97c3e;
  font-weight: bold;
}
.info-card {
  padding:10px;
}
.avatar-box {
  text-align: center;
}
.avatar-uploader {
  margin-top:10px;
}
.pet-list{
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap:16px;
}
.pet-card{
  cursor:pointer;
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}
.pet-card:hover{
  transform: translateY(-4px);
  box-shadow: 0 8px 18px rgba(232, 148, 90, 0.2);
}
</style>