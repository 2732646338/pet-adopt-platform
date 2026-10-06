<template>
  <div class="admin-container">
    <el-card>
      <template #header>
        <div class="header-title">
          <span>管理员后台</span>
        </div>
      </template>

      <!-- 标签页切换：宠物管理 / 领养审核 -->
      <el-tabs v-model="activeTab">
        <!-- 宠物管理 -->
        <el-tab-pane label="宠物管理" name="pet">
          <el-button type="primary" @click="openPetDialog">新增宠物</el-button>
          <el-table :data="petList" border stripe style="margin-top:15px">
            <el-table-column label="ID" prop="id" width="80"></el-table-column>
            <el-table-column label="宠物名称" prop="petName"></el-table-column>
            <el-table-column label="类型" prop="category"></el-table-column>
            <el-table-column label="性别" prop="gender"></el-table-column>
            <el-table-column label="年龄" prop="age"></el-table-column>
            <el-table-column label="状态">
              <template #default="scope">
                <el-tag :type="scope.row.status === 0 ? 'success' : 'info'">
                  {{ scope.row.status === 0 ? '可领养' : '已领养' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180">
              <template #default="scope">
                <el-button size="small" type="primary" @click="openPetDialog(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deletePet(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 新增/编辑宠物弹窗 -->
          <el-dialog v-model="petDialogVisible" title="宠物信息">
            <el-form ref="petFormRef" :model="petForm" :rules="petRules" label-width="100px">
              <el-form-item label="宠物名称" prop="petName">
                <el-input v-model="petForm.petName"></el-input>
              </el-form-item>
              <el-form-item label="类型" prop="category">
                <el-select v-model="petForm.category">
                  <el-option label="猫" value="猫"></el-option>
                  <el-option label="狗" value="狗"></el-option>
                  <el-option label="其他" value="其他"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="性别" prop="gender">
                <el-select v-model="petForm.gender">
                  <el-option label="公" value="公"></el-option>
                  <el-option label="母" value="母"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="年龄" prop="age">
                <el-input v-model.number="petForm.age"></el-input>
              </el-form-item>
              <el-form-item label="健康状况" prop="health">
                <el-input v-model="petForm.health"></el-input>
              </el-form-item>
              <el-form-item label="所在地" prop="address">
                <el-input v-model="petForm.address"></el-input>
              </el-form-item>
              <el-form-item label="图片地址" prop="imgUrl">
                <el-input v-model="petForm.imgUrl"></el-input>
              </el-form-item>
              <el-form-item label="描述" prop="description">
                <el-input v-model="petForm.description" type="textarea" :rows="3"></el-input>
              </el-form-item>
              <el-form-item label="状态" prop="status">
                <el-select v-model="petForm.status">
                  <el-option label="可领养" :value="0"></el-option>
                  <el-option label="已领养" :value="1"></el-option>
                </el-select>
              </el-form-item>
            </el-form>
            <template #footer>
              <el-button @click="petDialogVisible = false">取消</el-button>
              <el-button type="primary" @click="savePet">保存</el-button>
            </template>
          </el-dialog>
        </el-tab-pane>

        <!-- 领养申请审核 -->
        <el-tab-pane label="领养申请审核" name="adopt">
          <el-table :data="adoptApplyList" border stripe style="margin-top:15px">
            <el-table-column label="申请ID" prop="id" width="80"></el-table-column>
            <el-table-column label="宠物名称" prop="petName"></el-table-column>
            <el-table-column label="申请人" prop="userNickName"></el-table-column>
            <el-table-column label="联系电话" prop="phone"></el-table-column>
            <el-table-column label="居住环境" prop="liveEnv" show-overflow-tooltip></el-table-column>
            <el-table-column label="养宠经验" prop="petExp" show-overflow-tooltip></el-table-column>
            <el-table-column label="申请状态">
              <template #default="scope">
                <el-tag :type="getStatusTag(scope.row.applyStatus)">
                  {{ getStatusText(scope.row.applyStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <template v-if="scope.row.applyStatus === 0">
                  <el-button size="small" type="success" @click="auditApply(scope.row,1)">通过</el-button>
                  <el-button size="small" type="danger" @click="auditApply(scope.row,2)">驳回</el-button>
                </template>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCurrentInstance } from 'vue'
const { proxy } = getCurrentInstance()

const activeTab = ref('pet')

// ======宠物管理相关======
const petList = ref([])
const petDialogVisible = ref(false)
const petFormRef = ref(null)
const defaultPetForm = () => ({
  id: null,
  petName: '',
  category: '',
  gender: '',
  age: null,
  health: '',
  address: '',
  imgUrl: '',
  description: '',
  status: 0
})
const petForm = ref(defaultPetForm())
const petRules = ref({
  petName: [{ required: true, message: '请输入宠物名称', trigger: 'blur' }],
  category: [{ required: true, message: '请选择类型', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
})

// 打开弹窗，编辑/新增
const openPetDialog = (row) => {
  petDialogVisible.value = true
  if (row) {
    petForm.value = { ...defaultPetForm(), ...row }
  } else {
    petForm.value = defaultPetForm()
  }
}

// 保存宠物（后端新增/编辑统一走 POST /pet/add，按 id 区分）
const savePet = async () => {
  await petFormRef.value.validate()
  const res = await proxy.$axios.post('/pet/add', petForm.value)
  if (res.code === 200) {
    proxy.$message.success('保存成功')
    petDialogVisible.value = false
    getPetList()
  } else {
    proxy.$message.error(res.msg)
  }
}

// 删除宠物
const deletePet = async (row) => {
  await proxy.$confirm('确定删除这条宠物数据?', '提示', { type: 'warning' })
  const res = await proxy.$axios.delete(`/pet/delete/${row.id}`)
  if (res.code === 200) {
    proxy.$message.success('删除成功')
    getPetList()
  }
}

const getPetList = async () => {
  const res = await proxy.$axios.post('/pet/adminList', {
    pageNo: 1,
    pageSize: 1000
  })
  if (res.code === 200) {
    petList.value = res.data.records || []
  }
}

// =====领养审核相关=====
const adoptApplyList = ref([])
const getAdoptApplyList = async () => {
  // 后端为 GET，默认返回待审核(status=0)
  const res = await proxy.$axios.get('/adopt/adminList')
  if (res.code === 200) {
    adoptApplyList.value = res.data || []
  }
}

const auditApply = async (row, status) => {
  // 后端为 PUT，审核状态放在 body
  const res = await proxy.$axios.put(`/adopt/audit/${row.id}`, {
    applyStatus: status
  })
  if (res.code === 200) {
    proxy.$message.success('审核完成')
    getAdoptApplyList()
  } else {
    proxy.$message.error(res.msg)
  }
}

const getStatusText = (val) => {
  const map = { 0: '待审核', 1: '审核通过', 2: '审核拒绝' }
  return map[val]
}
const getStatusTag = (val) => {
  const map = { 0: 'warning', 1: 'success', 2: 'danger' }
  return map[val]
}

onMounted(() => {
  getPetList()
  getAdoptApplyList()
})
</script>

<style scoped>
.admin-container {
  width: 94%;
  margin: 30px auto;
}
.admin-container :deep(.el-card) {
  border-radius: 16px;
}
.admin-container :deep(.el-card__header) {
  background: linear-gradient(90deg, #fff2e4, #ffe8d2);
}
.header-title {
  font-size: 18px;
  font-weight: bold;
  color: #d97c3e;
}
.header-title::before {
  content: "🛠️ ";
}
.admin-container :deep(.el-table) {
  border-radius: 10px;
  overflow: hidden;
}
.admin-container :deep(.el-table__header th) {
  background-color: #fff4e8;
  color: #7a5a44;
}
</style>