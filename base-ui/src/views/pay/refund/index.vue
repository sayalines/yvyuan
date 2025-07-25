<template>

  <!-- 搜索工作栏 -->
  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="120px"
    >
      <el-form-item label="应用编号" prop="appId">
        <el-select
          v-model="queryParams.appId"
          clearable
          placeholder="请选择应用信息"
          class="!w-240px"
        >
          <el-option v-for="item in appList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="退款渠道" prop="channelCode">
        <el-select
          v-model="queryParams.channelCode"
          placeholder="请选择退款渠道"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getStrDictOptions(DICT_TYPE.PAY_CHANNEL_CODE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="商户支付单号" prop="merchantOrderId">
        <el-input
          v-model="queryParams.merchantOrderId"
          placeholder="请输入商户支付单号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="商户退款单号" prop="merchantRefundId">
        <el-input
          v-model="queryParams.merchantRefundId"
          placeholder="请输入商户退款单号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="渠道支付单号" prop="channelOrderNo">
        <el-input
          v-model="queryParams.channelOrderNo"
          placeholder="请输入渠道支付单号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="渠道退款单号" prop="channelRefundNo">
        <el-input
          v-model="queryParams.channelRefundNo"
          placeholder="请输入渠道退款单号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="退款状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择退款状态"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.PAY_REFUND_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"> <Icon icon="ep:search" class="mr-5px" /> 搜索 </el-button>
        <el-button @click="resetQuery"> <Icon icon="ep:refresh" class="mr-5px" /> 重置 </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['system:tenant:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column label="ID" align="center" prop="id" width="200px" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column label="支付金额" align="center" prop="payPrice" width="100">
        <template #default="scope">
          ￥{{ parseFloat(scope.row.payPrice / 100).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="退款金额" align="center" prop="refundPrice" width="100">
        <template #default="scope">
          ￥{{ parseFloat(scope.row.refundPrice / 100).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="退款订单号" align="left" width="300">
        <template #default="scope">
          <p class="order-font">
            <el-tag size="small">商户</el-tag> {{ scope.row.merchantRefundId }}
          </p>
          <p class="order-font">
            <el-tag size="small" type="warning">退款</el-tag> {{ scope.row.no }}
          </p>
          <p class="order-font" v-if="scope.row.channelRefundNo">
            <el-tag size="small" type="success">渠道</el-tag> {{ scope.row.channelRefundNo }}
          </p>
        </template>
      </el-table-column>
      <el-table-column label="支付订单号" align="left" width="300">
        <template #default="scope">
          <p class="order-font">
            <el-tag size="small">商户</el-tag> {{ scope.row.merchantOrderId }}
          </p>
          <p class="order-font">
            <el-tag size="small" type="success">渠道</el-tag> {{ scope.row.channelOrderNo }}
          </p>
        </template>
      </el-table-column>
      <el-table-column label="退款状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PAY_REFUND_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="退款渠道" align="center" width="140">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PAY_CHANNEL_CODE" :value="scope.row.channelCode" />
        </template>
      </el-table-column>
      <el-table-column
        label="成功时间"
        align="center"
        prop="successTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column label="支付应用" align="center" prop="successTime" width="100">
        <template #default="scope">
          <span>{{ scope.row.appName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right">
        <template #default="scope">
          <el-button
            type="primary"
            link
            @click="openDetail(scope.row.id)"
            v-hasPermi="['pay:order:query']"
          >
            详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：预览 -->
  <RefundDetail ref="detailRef" @success="getList" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions, getStrDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as RefundApi from '@/api/pay/refund'
import * as AppApi from '@/api/pay/app'
import RefundDetail from './RefundDetail.vue'
import download from '@/utils/download'

defineOptions({ name: 'PayRefund' })

const message = useMessage() // 消息弹窗

const loading = ref(false) // 列表遮罩层
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  merchantId: undefined,
  appId: undefined,
  channelCode: undefined,
  merchantOrderId: undefined,
  merchantRefundId: undefined,
  status: undefined,
  payPrice: undefined,
  refundPrice: undefined,
  channelOrderNo: undefined,
  channelRefundNo: undefined,
  createTime: [],
  successTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出等待
const appList = ref([]) // 支付应用列表集合

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await RefundApi.getRefundPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields()
  handleQuery()
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await RefundApi.exportRefund(queryParams)
    download.excel(data, '支付订单.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 预览详情 */
const detailRef = ref()
const openDetail = (id: number) => {
  detailRef.value.open(id)
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  appList.value = await AppApi.getAppList()
})
</script>
<style>
.order-font {
  padding: 2px 0;
  font-size: 12px;
}
</style>
