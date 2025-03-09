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
      <el-form-item label="量表ID" prop="questionId">
        <el-input
          v-model="queryParams.questionId"
          placeholder="请输入量表ID"
          disabled
          class="!w-100px"
        />
      </el-form-item>
      <el-form-item label="题目编号" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入题目编号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-150px"
        />
      </el-form-item>
      <el-form-item label="题目名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入题目名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-150px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['system:question:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleImport"
          v-hasPermi="['system:question:create']"
        >
          <Icon icon="ep:upload" class="mr-5px" /> 导入
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="ID" align="center" prop="id" width="100px" />
      <el-table-column label="题目编号" align="center" prop="code" width="100px" />
      <el-table-column label="题目名称" align="left" prop="name" />
      <el-table-column align="center" label="题目类型" width="100px" prop="type">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_QUESTION_TOPIC_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="排序号" align="center" prop="orderNo" width="100px" />
      <el-table-column label="操作" align="center" width="180px">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['system:question:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="success"
            @click="handleCopy(scope.row.id)"
            v-hasPermi="['system:question:create']"
          >
            复制
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['system:question:delete']"
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
  <QuestionTopicForm ref="formRef" @success="getList" />
  <QuestionTopicImportForm ref="importFormRef" @success="getList"/>
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as QuestionTopicApi from '@/api/system/questiontopic'
import QuestionTopicForm from './QuestionTopicForm.vue'
import QuestionTopicImportForm from './QuestionTopicImportForm.vue'
import {DICT_TYPE} from "@/utils/dict";

defineOptions({ name: 'QuestionTopic' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const props = defineProps<{
  questionId: -1 // 问卷ID（主表的关联字段）
}>()

const loading = ref(true) // 列表的加载中
const list = ref([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  questionId: -1,
  code: undefined,
  name: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 监听主表的关联字段的变化，加载对应的子表数据 */
watch(
  () => props.questionId,
  (val) => {
    queryParams.questionId = val
    handleQuery()
  },
  { immediate: false }
)

/** 导入 */
const importFormRef = ref()
const handleImport = () => {
  if (props.questionId===undefined|| props.questionId === -1) {
    message.error('请选择一条量表记录')
    return
  }
  importFormRef.value.open(props.questionId)
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await QuestionTopicApi.getQuestionTopicPage(queryParams)
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
  queryParams.questionId = props.questionId
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  if (props.questionId===undefined|| props.questionId === -1) {
    message.error('请选择一条量表记录')
    return
  }
  formRef.value.open(type, id, props.questionId)
}

/** 复制按钮操作 */
const handleCopy = async (id: number) => {
  try {
    // 复制的二次确认
    await message.confirm("是否复制当前题目？")
    // 发起复制
    await QuestionTopicApi.copyQuestionTopic(id)
    message.success("复制成功")
    // 刷新列表
    await getList()
  } catch {}
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await QuestionTopicApi.deleteQuestionTopic(id)
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
    const data = await QuestionTopicApi.exportQuestionTopic(queryParams)
    download.excel(data, '量表题目.xls')
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