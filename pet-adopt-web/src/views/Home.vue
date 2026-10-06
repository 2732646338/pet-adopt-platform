<template>
  <div class="home">
    <!-- 轮播图 -->
    <el-carousel height="300px" class="carousel">
      <el-carousel-item>
        <div class="banner banner1">关爱流浪宠物，给它一个家</div>
      </el-carousel-item>
      <el-carousel-item>
        <div class="banner banner2">领养代替购买</div>
      </el-carousel-item>
      <el-carousel-item>
        <div class="banner banner3">用爱陪伴小动物</div>
      </el-carousel-item>
    </el-carousel>

    <div class="container">
      <!-- 筛选区域 -->
      <div class="filter">
        <span>品种筛选：</span>
        <el-select v-model="petType" placeholder="全部" clearable style="width:180px">
          <el-option label="全部" value=""></el-option>
          <el-option v-for="c in categoryOptions" :key="c" :label="c" :value="c"></el-option>
        </el-select>
      </div>

      <!-- 宠物卡片列表 -->
      <div class="pet-list">
        <el-card class="pet-card" v-for="pet in petList" :key="pet.id" @click="goDetail(pet.id)">
          <template #header>
            <div class="card-header">{{ pet.petName }}</div>
          </template>
          <el-image
            :src="pet.imgUrl || defaultImg"
            fit="cover"
            style="width:100%;height:180px"
            :preview-src-list="[pet.imgUrl || defaultImg]"
          >
            <template #error>
              <img :src="defaultImg" style="width:100%;height:180px;object-fit:cover" />
            </template>
          </el-image>
          <div class="info">
            <p>类型：{{pet.category}}</p >
            <p>年龄：{{pet.age}}岁</p >
            <p>状态：<el-tag v-if="pet.status === 0" type="success">可领养</el-tag><el-tag v-else type="info">已领养</el-tag></p >
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCurrentInstance } from 'vue'

const { proxy } = getCurrentInstance()
const router = useRouter()

const petType = ref('')
const allPets = ref([])
// 图片为空时的默认图
const defaultImg = 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=' +
  encodeURIComponent('温馨的宠物领养照片 可爱的小猫和小狗 柔和暖色调') + '&image_size=square'

// 筛选项由数据中的实际品种动态生成
const categoryOptions = computed(() => {
  return [...new Set(allPets.value.map(p => p.category).filter(Boolean))]
})
// 按品种前端筛选
const petList = computed(() => {
  if (!petType.value) return allPets.value
  return allPets.value.filter(p => p.category === petType.value)
})

// 获取宠物列表接口（后端为 POST 分页查询，数据在 records 中）
const getPetList = async () => {
  const res = await proxy.$axios.post('/pet/list', {
    pageNo: 1,
    pageSize: 100
  })
  if(res.code === 200){
    allPets.value = res.data.records || []
  }
}

// 跳转到宠物详情
const goDetail = (id) => {
  router.push(`/pet/${id}`)
}

//页面加载自动查询
onMounted(()=>{
  getPetList()
})
</script>

<style scoped>
.home{
  padding-bottom:40px;
}
.carousel{
  width: 90%;
  margin: 20px auto;
  border-radius:16px;
  overflow: hidden;
  box-shadow: 0 6px 20px rgba(232, 148, 90, 0.18);
}
.banner{
  width:100%;
  height:300px;
  display:flex;
  flex-direction: column;
  align-items:center;
  justify-content:center;
  color:#fff;
  font-size:30px;
  letter-spacing: 4px;
  text-shadow: 0 2px 8px rgba(0,0,0,0.15);
}
.banner::after{
  font-size: 16px;
  letter-spacing: 2px;
  margin-top: 12px;
  opacity: 0.9;
}
.banner1{
  background: linear-gradient(135deg, #f6b078 0%, #e8945a 100%);
}
.banner1::after{
  content: "每一只流浪的小可爱，都在等一个温暖的家";
}
.banner2{
  background: linear-gradient(135deg, #f2a98a 0%, #e07856 100%);
}
.banner2::after{
  content: "给它一个家，它会用一生陪伴你";
}
.banner3{
  background: linear-gradient(135deg, #f7c59f 0%, #efa06b 100%);
}
.banner3::after{
  content: "爱与被爱，从一次领养开始";
}
.container{
  width:90%;
  margin:0 auto;
}
.filter{
  margin:24px 0;
  font-size:16px;
  color: #5d4a3a;
}
.pet-list{
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap:20px;
}
.pet-card{
  cursor:pointer;
  border-radius: 16px;
  overflow: hidden;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}
.pet-card:hover{
  transform: translateY(-6px);
  box-shadow: 0 10px 24px rgba(232, 148, 90, 0.22);
}
.pet-card :deep(.el-card__header){
  background: linear-gradient(90deg, #fff2e4, #ffe8d2);
  border-bottom: none;
  padding: 12px 16px;
}
.card-header{
  font-weight:bold;
  color: #d97c3e;
  font-size: 16px;
}
.card-header::before{
  content: "🐶 ";
}
.pet-card:nth-child(2n) .card-header::before{
  content: "🐱 ";
}
.info{
  padding: 4px 2px;
  color: #7a6555;
}
.info p{
  margin:6px 0;
  font-size: 14px;
}
.info :deep(.el-tag){
  border-radius: 10px;
}
</style>