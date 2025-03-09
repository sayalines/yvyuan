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
          v-hasPermi="['statistics:trade:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column label="总成交人数" align="center" prop="totalUserCount" />
      <el-table-column label="新买家人数" align="center" prop="newUserCount" />
      <el-table-column label="退款人数" align="center" prop="refundUserCount" />
      <el-table-column align="center" label="总成交金额" prop="totalDealAmount">
        <template #default="{ row }"> {{ fenToYuan(row.totalDealAmount) }}元</template>
      </el-table-column>
      <el-table-column align="center" label="总退款金额" prop="totalRefundAmount">
        <template #default="{ row }"> {{ fenToYuan(row.totalRefundAmount) }}元</template>
      </el-table-column>
      <el-table-column align="center" label="客单价" prop="avgAmount">
        <template #default="{ row }"> {{ fenToYuan(row.avgAmount) }}元</template>
      </el-table-column>
      <el-table-column label="复购率" align="center" prop="reBuyRate" />
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
defineOptions({ name: 'TradeDelStatistics' })

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
    const data = await TradeStatisticsApi.getDealTradePage(queryParams)
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
    const data = await TradeStatisticsApi.exportDealTradeExcel(queryParams)
    download.excel(data, '成交数据.xls')
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
