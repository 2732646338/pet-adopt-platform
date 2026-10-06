<template>
  <div class="detail-container">
    <el-row :gutter="20">
      <!-- 宠物图片 -->
      <el-col :span="10">
        <el-image
          :src="pet.imgUrl || defaultImg"
          fit="cover"
          style="width:100%;height:400px;border-radius:8px"
          :preview-src-list="[pet.imgUrl || defaultImg]"
        >
          <template #error>
            <img :src="defaultImg" style="width:100%;height:400px;object-fit:cover;border-radius:8px" />
          </template>
        </el-image>
      </el-col>
      <!-- 宠物基础信息 -->
      <el-col :span="14">
        <h2>{{ pet.petName }}</h2>
        <p><span>类型：</span>{{ pet.category }}</p >
        <p><span>性别：</span>{{ pet.gender }}</p >
        <p><span>年龄：</span>{{ pet.age }} 岁</p >
        <p><span>健康状况：</span>{{ pet.health }}</p >
        <p><span>所在地：</span>{{ pet.address }}</p >
        <p><span>描述：</span>{{ pet.description }}</p >
        <p>
          <span>状态：</span>
          <el-tag v-if="pet.status === 0" type="success">可领养</el-tag>
          <el-tag v-else type="info">已被领养</el-tag>
        </p >
        <el-button type="warning" @click="handleCollect">
          {{ isCollect ? '已收藏' : '收藏' }}
        </el-button>
      </el-col>
    </el-row>

    <!-- 领养申请表单 -->
    <el-card class="apply-card">
      <template #header><h3>领养申请</h3></template>
      <el-form ref="applyRef" :model="applyForm" :rules="applyRules" label-width="100px">
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="applyForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="居住环境" prop="liveEnv">
          <el-input v-model="applyForm.liveEnv" placeholder="例如：自有住房、封窗、有院子等"></el-input>
        </el-form-item>
        <el-form-item label="养宠经验" prop="petExp">
          <el-input v-model="applyForm.petExp" type="textarea" :rows="3" placeholder="请描述您的养宠经验"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitApply">提交申请</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 留言区域 -->
    <el-card class="msg-card">
      <template #header><h3>留言区</h3></template>
      <div class="msg-input">
        <el-input v-model="msgContent" placeholder="写下你的留言..." style="width:80%" />
        <el-button type="primary" @click="sendMsg">发表留言</el-button>
      </div>
      <div class="msg-list">
        <div class="msg-item" v-for="item in msgList" :key="item.id">
          <p><b>{{item.userNickname || '匿名用户'}}</b>：{{item.content}}</p >
          <p class="time">{{item.createTime}}</p>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCurrentInstance } from 'vue'
import { useUserStore } from '../stores/user'

const { proxy } = getCurrentInstance()
const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const petId = Number(route.params.id)

// 默认宠物图
const defaultImg = 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=' +
  encodeURIComponent('温馨的宠物领养照片 可爱的小猫和小狗 柔和暖色调') + '&image_size=square'

// 宠物信息
const pet = ref({})
const isCollect = ref(false)

// 领养表单（对应后端 AdoptApplyDTO）
const applyRef = ref(null)
const applyForm = ref({
  petId: petId,
  phone: '',
  liveEnv: '',
  petExp: ''
})
const applyRules = ref({
  phone: [{ required: true, message: '请填写手机号', trigger: 'blur' }],
  liveEnv: [{ required: true, message: '请填写居住环境', trigger: 'blur' }],
  petExp: [{ required: true, message: '请填写养宠经验', trigger: 'blur' }]
})

//留言
const msgContent = ref('')
const msgList = ref([])

// 未登录统一提示并跳转
const requireLogin = () => {
  if (!userStore.token) {
    proxy.$message.warning('请先登录')
    router.push('/login')
    return false
  }
  return true
}

// 获取宠物详情
const getPetDetail = async () => {
  const res = await proxy.$axios.get(`/pet/detail/${petId}`)
  if (res.code === 200) {
    pet.value = res.data
  } else {
    proxy.$message.error(res.msg || '宠物不存在')
  }
}

// 查询是否已收藏（需登录）
const checkCollected = async () => {
  if (!userStore.token) return
  const res = await proxy.$axios.post('/collect/isCollected', null, { params: { petId } })
  if (res.code === 200) {
    isCollect.value = !!res.data
  }
}

// 收藏 / 取消收藏
const handleCollect = async () => {
  if (!requireLogin()) return
  try {
    const url = isCollect.value ? '/collect/cancel' : '/collect/add'
    const res = await proxy.$axios.post(url, { petId })
    if (res.code === 200) {
      isCollect.value = !isCollect.value
      proxy.$message.success(isCollect.value ? '收藏成功' : '取消收藏成功')
    } else {
      proxy.$message.error(res.msg)
    }
  } catch (e) {
    proxy.$message.error(e.response?.data?.msg || '操作失败')
  }
}

//提交领养申请
const submitApply = async () => {
  if (!requireLogin()) return
  await applyRef.value.validate()
  try {
    const res = await proxy.$axios.post('/adopt/apply', applyForm.value)
    if (res.code === 200) {
      proxy.$message.success('申请提交成功！等待管理员审核')
      applyRef.value.resetFields()
    } else {
      proxy.$message.error(res.msg)
    }
  } catch (e) {
    proxy.$message.error(e.response?.data?.msg || '提交失败')
  }
}

// 获取留言列表（公开接口，路径参数 petId）
const getMsgList = async () => {
  const res = await proxy.$axios.get(`/message/list/${petId}`)
  if (res.code === 200) {
    msgList.value = res.data || []
  }
}

// 发送留言
const sendMsg = async () => {
  if (!msgContent.value) return
  if (!requireLogin()) return
  try {
    const res = await proxy.$axios.post('/message/add', { petId, content: msgContent.value })
    if (res.code === 200) {
      proxy.$message.success('留言成功')
      msgContent.value = ''
      getMsgList()
    } else {
      proxy.$message.error(res.msg)
    }
  } catch (e) {
    proxy.$message.error(e.response?.data?.msg || '留言失败')
  }
}

onMounted(() => {
  getPetDetail()
  getMsgList()
  checkCollected()
})
</script>

<style scoped>
.detail-container {
  width: 90%;
  margin: 30px auto;
}
.detail-container h2 {
  color: #d97c3e;
  letter-spacing: 1px;
}
.detail-container h2::before {
  content: "🐾 ";
}
.apply-card, .msg-card {
  margin-top: 30px;
  border-radius: 16px;
}
.apply-card :deep(.el-card__header),
.msg-card :deep(.el-card__header) {
  background: linear-gradient(90deg, #fff2e4, #ffe8d2);
  font-weight: bold;
  color: #d97c3e;
}
.msg-input {
  display: flex;
  gap:10px;
  align-items: center;
  margin-bottom:20px;
}
.msg-item {
  padding:12px 14px;
  border-radius: 10px;
  background: #fff6ec;
  margin-bottom: 10px;
  border-bottom: none;
}
.msg-item b {
  color: #d97c3e;
}
.time{
  color:#b09a88;
  font-size:12px;
  margin:4px 0;
}
h2{
  margin:0 0 15px;
}
p{
  font-size:16px;
  line-height:2;
  color: #5d4a3a;
}
</style>