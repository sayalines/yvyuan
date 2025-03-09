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
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入标题"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="兑换类型" prop="bizType">
        <el-select
          v-model="queryParams.bizType"
          placeholder="请选择兑换类型"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.MEMBER_EXCHANGE_BIZ_TYPE)"
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
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['member:exchange-config:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['member:exchange-config:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true"
              highlight-current-row
              @current-change="handleCurrentChange">
      <el-table-column label="ID" align="center" prop="id" width="100px" />
      <el-table-column label="标题" align="center" prop="title" width="250px"  />
      <el-table-column label="兑换类型" align="center" prop="bizType" width="120px">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.MEMBER_EXCHANGE_BIZ_TYPE" :value="scope.row.bizType" />
        </template>
      </el-table-column>
      <el-table-column label="物品ID" align="center" prop="bizId" />
      <el-table-column label="编号前缀" align="center" prop="prefix"  width="100px"/>
      <el-table-column label="使用次数" align="center" prop="useCount" width="100px" />
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
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['member:exchange-config:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="primary"
            @click="createForm(scope.row.id)"
            v-hasPermi="['member:exchange-config:create']"
          >
            生成
          </el-button>
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

  <ContentWrap>
    <el-tabs model-value="exchangeConfigItem">
      <el-tab-pane label="明细信息" name="exchangeConfigItem">
        <ExchangeConfigItemList :config-id="currentRow.id" />
      </el-tab-pane>
    </el-tabs>
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <ExchangeConfigForm ref="formRef" @success="getList" />
  <!-- 表单弹窗：添加/修改 -->
  <CreateForm ref="createFormRef" @success="getList" />
</template>

<script setup lang="ts">
const { currentRoute, push } = useRouter() // 路由跳转
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as ExchangeConfigApi from '@/api/member/exchangeConfig'
import ExchangeConfigForm from './ExchangeConfigForm.vue'
import {DICT_TYPE, getIntDictOptions} from "@/utils/dict";
import {formatTime} from "@/utils";
import ExchangeConfigItemList from "./components/ExchangeConfigItemList.vue";
import CreateForm from "./components/CreateForm.vue";
/** 选中行操作 */
const currentRow = ref({}) // 选中行

defineOptions({ name: 'ExchangeConfig' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  title: undefined,
  description: undefined,
  bizType: undefined,
  prefix: undefined,
  remarks: undefined,
  bizId: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  console.log("11")

  loading.value = true
  try {
    const data = await ExchangeConfigApi.getExchangeConfigPage(queryParams)
    list.value = data.list
    total.value = data.total

    let row = {}
    if (data.total>0){
      row =data.list[0]
    }
    handleCurrentChange(row)
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  currentRow.value = {};
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  if (type == "createExchange"){
    formRef.value.open(type, id)
  }else{
    formRef.value.open(type, id)
  }
}
const createFormRef = ref()
const createForm = (id?: number) => {
  createFormRef.value.open(id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ExchangeConfigApi.deleteExchangeConfig(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ExchangeConfigApi.exportExchangeConfig(queryParams)
    download.excel(data, '兑换配置.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

const handleCurrentChange = (row) => {
  if (row!=null){
    currentRow.value = row
  }
}

// 监听路由变化更新列表，解决保存后，列表不刷新的问题。
watch(
  () => currentRoute.value,
  () => {
    getList()
  }
)

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>