<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      ref="queryFormRef"
      :inline="true"
      :model="queryParams"
      class="-mb-15px"
      label-width="68px"
    >
      <el-form-item label="用户ID" prop="id">
        <el-input
          v-model="queryParams.id"
          class="!w-240px"
          clearable
          placeholder="请输入用户编号"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户昵称" prop="nickname">
        <el-input
          v-model="queryParams.nickname"
          class="!w-240px"
          clearable
          placeholder="请输入用户昵称"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="mobile">
        <el-input
          v-model="queryParams.mobile"
          class="!w-240px"
          clearable
          placeholder="请输入手机号"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="注册时间" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
          end-placeholder="结束日期"
          start-placeholder="开始日期"
          type="daterange"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item label="登录时间" prop="loginDate">
        <el-date-picker
          v-model="queryParams.loginDate"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
          end-placeholder="结束日期"
          start-placeholder="开始日期"
          type="daterange"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item label="用户类型" prop="userType">
        <el-select
          v-model="queryParams.userType"
          placeholder="请选择用户类型"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.MEMBER_USER_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择"
          clearable
          class="!w-120px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="用户等级" prop="levelId">
        <MemberLevelSelect v-model="queryParams.levelId" />
      </el-form-item>
      <el-form-item label="用户分组" prop="groupId">
        <MemberGroupSelect v-model="queryParams.groupId" />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          搜索
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          重置
        </el-button>
        <el-button v-hasPermi="['promotion:coupon:send']" @click="openCoupon">发送优惠券</el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['member:user:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table
      v-loading="loading"
      :data="list"
      :show-overflow-tooltip="true"
      :stripe="true"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column align="center" label="ID" prop="id" width="200px" />
      <el-table-column align="center" label="头像" prop="avatar" width="80px">
        <template #default="scope">
          <img :src="scope.row.avatar" style="width: 40px" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="昵称" prop="nickname" width="150px" />
      <el-table-column align="center" label="手机号" prop="mobile" width="120px" />
<!--      <el-table-column align="center" label="证件类型" prop="idCodeType" width="120px">-->
<!--        <template #default="scope">-->
<!--          <dict-tag :type="DICT_TYPE.MEMBER_CARD_TYPE" :value="scope.row.idCodeType" />-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column align="center" label="证件号码" prop="idCode" width="130px" />-->
      <el-table-column align="center" label="分组" prop="groupName" width="100px" />
<!--      <el-table-column align="center" label="用户类型" prop="userType" min-width="120px" >-->
<!--        <template #default="scope">-->
<!--          <dict-tag :type="DICT_TYPE.MEMBER_USER_TYPE" :value="scope.row.userType" />-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column align="center" label="积分" prop="point" width="100px" />
      <el-table-column align="center" label="状态" prop="status" width="100px">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="登录时间"
        prop="loginDate"
        width="180px"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="注册时间"
        prop="createTime"
        width="180px"
      />
      <el-table-column
        :show-overflow-tooltip="false"
        align="center"
        fixed="right"
        label="操作"
        width="180px"
      >
        <template #default="scope">
          <div class="flex items-center justify-center">
            <el-button link type="primary" @click="openDetail(scope.row.id)">详情</el-button>
            <el-dropdown
              v-hasPermi="[
                'member:user:update',
                'member:user:update-level',
                'member:user:update-point'
                // 'member:user:update-balance'
              ]"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button link type="primary">
                <Icon icon="ep:d-arrow-right" />
                更多
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    v-if="checkPermi(['member:user:update'])"
                    command="handleUpdate"
                  >
                    编辑
                  </el-dropdown-item>
                  <el-dropdown-item
                    v-if="checkPermi(['member:user:update-level'])"
                    command="handleUpdateLevel"
                  >
                    修改等级
                  </el-dropdown-item>
                  <el-dropdown-item
                    v-if="checkPermi(['member:user:update-point'])"
                    command="handleUpdatePoint"
                  >
                    修改积分
                  </el-dropdown-item>
                  <!--                  <el-dropdown-item-->
                  <!--                    v-if="checkPermi(['member:user:update-balance'])"-->
                  <!--                    command="handleUpdateBlance"-->
                  <!--                  >-->
                  <!--                    修改余额(WIP)-->
                  <!--                  </el-dropdown-item>-->
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      v-model:limit="queryParams.pageSize"
      v-model:page="queryParams.pageNo"
      :total="total"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <UserForm ref="formRef" @success="getList" />
  <!-- 修改用户等级弹窗 -->
  <UserLevelUpdateForm ref="updateLevelFormRef" @success="getList" />
  <!-- 修改用户积分弹窗 -->
  <UserPointUpdateForm ref="updatePointFormRef" @success="getList" />
  <!-- 修改用户彩虹值弹窗 -->
  <UserRainbowUpdateForm ref="updateRainbowFormRef" @success="getList" />
  <!-- 发送优惠券弹窗 -->
  <CouponSendForm ref="couponSendFormRef" />
</template>
<script lang="ts" setup>
import { dateFormatter } from '@/utils/formatTime'
import * as UserApi from '@/api/member/user'
import {DICT_TYPE, getIntDictOptions} from '@/utils/dict'
import UserForm from './UserForm.vue'
// import MemberTagSelect from '@/views/member/tag/components/MemberTagSelect.vue'
import MemberLevelSelect from '@/views/member/level/components/MemberLevelSelect.vue'
import MemberGroupSelect from '@/views/member/group/components/MemberGroupSelect.vue'
import UserLevelUpdateForm from './UserLevelUpdateForm.vue'
import UserPointUpdateForm from './UserPointUpdateForm.vue'
import UserRainbowUpdateForm from './UserRainbowUpdateForm.vue'
import { CouponSendForm } from '@/views/mall/promotion/coupon/components'
import { checkPermi } from '@/utils/permission'
import download from '@/utils/download'
import {exportExcel} from "@/api/member/user";

defineOptions({ name: 'MemberUser' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  id: null,
  deptId: null,
  nickname: null,
  mobile: null,
  idCodeType: null,
  idCode: null,
  userType: null,
  status: null,
  loginDate: [],
  createTime: [],
  tagIds: [],
  levelId: null,
  groupId: null,
  deptName: null
})
const queryFormRef = ref() // 搜索的表单
const updateLevelFormRef = ref() // 修改会员等级表单
const updatePointFormRef = ref() // 修改会员积分表单
const updateRainbowFormRef = ref() // 修改会员彩虹值表单
const selectedIds = ref<number[]>([]) // 表格的选中 ID 数组
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await UserApi.getUserPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 打开会员详情 */
const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'MemberUserDetail', params: { id } })
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 表格选中事件 */
const handleSelectionChange = (rows: UserApi.UserVO[]) => {
  selectedIds.value = rows.map((row) => row.id)
}

/** 发送优惠券 */
const couponSendFormRef = ref()
const openCoupon = () => {
  if (selectedIds.value.length === 0) {
    message.warning('请选择要发送优惠券的用户')
    return
  }
  couponSendFormRef.value.open(selectedIds.value)
}

/** 操作分发 */
const handleCommand = (command: string, row: UserApi.UserVO) => {
  switch (command) {
    case 'handleUpdate':
      openForm('update', row.id)
      break
    case 'handleUpdateLevel':
      updateLevelFormRef.value.open(row.id)
      break
    case 'handleUpdatePoint':
      updatePointFormRef.value.open(row.id)
      break
    case 'handleUpdateRainbow':
      updateRainbowFormRef.value.open(row.id)
      break
    case 'handleUpdateBlance':
      // todo @jason：增加一个【修改余额】
      break
    default:
      break
  }
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await UserApi.exportExcel(queryParams)
    download.excel(data, '会员记录.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}


/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
