<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="统计时间" prop="createTime">
        <ShortcutDateRangePicker @change="handleTimeRangeChange" />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['statistics:tradeTotal:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column label="订单ID" align="center" prop="id" />
      <el-table-column label="订单编号" align="center" prop="no" width="180"/>
<!--      <el-table-column label="订单类型" align="center" prop="type" />-->
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
      <el-table-column label="支付状态" align="center" prop="payStatus" />
      <el-table-column label="订单状态" align="center" prop="status" />
<!--      <el-table-column label="快递公司" align="center" prop="deliveryName" />-->
<!--      <el-table-column label="快递单号" align="center" prop="logisticsNo" />-->
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="手机号" align="center" prop="mobile" width="120" />
<!--      <el-table-column label="openid" align="center" prop="openid" />-->
<!--      <el-table-column label="unionid" align="center" prop="unionid" />-->
      <el-table-column label="商品数量" align="center" prop="productCount" />
      <el-table-column label="总金额" align="center" prop="totalPrice" />
      <el-table-column label="来源" align="center" prop="origins" />
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
import * as TradeStatisticsApi from '@/api/mall/statistics/trade'
import dayjs from 'dayjs'
import {formatDate} from "@/utils/formatTime";
import download from '@/utils/download'
import {fenToYuan} from "@/utils";
import {exportDealTotalExcel, getDealTotalPage} from "@/api/mall/statistics/trade";
defineOptions({ name: 'TradeDealTotalStatistics' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询数据 */
const handleTimeRangeChange = async (times: [dayjs.ConfigType, dayjs.ConfigType]) => {
  // 查询数据
  let params = {times}
  queryParams.createTime = [formatDate(params.times[0]), formatDate(params.times[1])]
  getList()
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await TradeStatisticsApi.getDealTotalPage(queryParams)
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
    const data = await TradeStatisticsApi.exportDealTotalExcel(queryParams)
    download.excel(data, '交易汇总.xls')
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
