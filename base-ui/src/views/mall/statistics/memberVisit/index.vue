<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="120px"
    >
      <el-form-item label="统计时间" prop="createTime">
        <ShortcutDateRangePicker @change="handleTimeRangeChange" />
<!--        <el-date-picker-->
<!--          v-model="queryParams.createTime"-->
<!--          value-format="YYYY-MM-DD HH:mm:ss"-->
<!--          type="daterange"-->
<!--          start-placeholder="开始日期"-->
<!--          end-placeholder="结束日期"-->
<!--          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"-->
<!--          class="!w-240px"-->
<!--        />-->
      </el-form-item>

      <el-form-item label="是否实时数据" prop="isRealTime">
        <el-select
          v-model="queryParams.isRealTime"
          placeholder="请选择是否实时数据"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getBoolDictOptions(DICT_TYPE.INFRA_BOOLEAN_STRING)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['statistics:member:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column label="访客ID" align="center" prop="userId" width="200px" />
      <el-table-column label="访客IP" align="center" prop="userIp" />
      <el-table-column label="手机号" align="center" prop="mobile" />
<!--      <el-table-column align="center" label="微信头像" prop="avatar" width="80px">-->
<!--        <template #default="scope">-->
<!--          <img :src="scope.row.avatar" style="width: 40px" />-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="邮箱" align="center" prop="email" />
      <el-table-column label="昵称" align="center" prop="nickname" />
<!--      <el-table-column label="性别" align="center" prop="sex" width="120px">-->
<!--        <template #default="scope">-->
<!--          <dict-tag :type="DICT_TYPE.SYSTEM_USER_SEX" :value="scope.row.sex" />-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="年龄" align="center" prop="age" />-->
      <el-table-column label="来源" align="center" prop="origins" />
      <el-table-column label="浏览器型号" align="center" prop="browser" />
      <el-table-column label="手机型号" align="center" prop="mobileModel" />
      <el-table-column
        label="访问时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
    </el-table>
    <!-- 分页组件 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as MemberStatisticsApi from '@/api/mall/statistics/member'
import {DICT_TYPE, getBoolDictOptions} from "@/utils/dict";
import dayjs from 'dayjs'
import {dateFormatter, formatDate, isTodyDay} from "@/utils/formatTime";
import download from '@/utils/download'
defineOptions({ name: 'MemberVisitStatistics' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  createTime: [],
  isRealTime:false
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询数据 */
const handleTimeRangeChange = async (times: [dayjs.ConfigType, dayjs.ConfigType]) => {
  // 查询数据
  let params = {times}
  queryParams.createTime = [formatDate(params.times[0]), formatDate(params.times[1])]
  if (isTodyDay(params.times[0],params.times[1])){
    queryParams.isRealTime = true
  }else{
    queryParams.isRealTime = false
  }
  getList()
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await MemberStatisticsApi.getMemberVisitPage(queryParams)
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

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await MemberStatisticsApi.exportMemberVisitExcel(queryParams)
    download.excel(data, '访客信息.xls')
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
