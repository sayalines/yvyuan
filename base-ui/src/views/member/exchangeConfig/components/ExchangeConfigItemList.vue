<template>
  <!-- 列表 -->
  <ContentWrap>
    <el-button
      type="success"
      plain
      @click="handleExport"
      :loading="exportLoading"
      v-hasPermi="['member:exchange-config:export']"
    >
      <Icon icon="ep:download" class="mr-5px" /> 导出
    </el-button>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="编码" align="center" prop="code" width="300px"/>
      <el-table-column label="总次数" align="center" prop="totalCount" width="80px" />
      <el-table-column label="已使用次数" align="center" prop="useCount" width="100px" />
      <el-table-column
        label="有效开始时间"
        align="center"
        prop="validStartTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        label="有效结束时间"
        align="center"
        prop="validEndTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['member:exchange-config:delete']"
          >
            删除
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

  <!-- 表单弹窗：添加/修改 -->
  <ExchangeConfigItemForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as ExchangeConfigItemApi from '@/api/member/exchangeConfigItem'
import ExchangeConfigItemForm from './ExchangeConfigItemForm.vue'

defineOptions({ name: 'ExchangeConfigItem' })

const props = defineProps<{
  configId: undefined // 配置ID（主表的关联字段）
}>()

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  configId: undefined,
  code: undefined,
  bizType: undefined,
  bizId: undefined,
  totalCount: undefined,
  useCount: undefined,
  validStartTime: [],
  validEndTime: [],
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 监听主表的关联字段的变化，加载对应的子表数据 */
watch(
  () => props.configId,
  (val) => {
    queryParams.configId = val
    handleQuery()
  },
  { immediate: false }
)

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    if (queryParams.configId==null){
      queryParams.configId = -1
    }
    const data = await ExchangeConfigItemApi.getExchangeConfigItemPage(queryParams)
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

/** 添加/修改操作 */
const formRef = ref()

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ExchangeConfigItemApi.deleteExchangeConfigItem(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm("是否导出兑换明细")
    // 发起导出
    exportLoading.value = true
    const data = await ExchangeConfigItemApi.exportExchangeConfigItem(queryParams)
    download.excel(data, '兑换明细.xls')
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