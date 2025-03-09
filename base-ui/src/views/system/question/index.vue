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
      <el-form-item label="量表主题" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入量表主题"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="显示名称" prop="displayName">
        <el-input
          v-model="queryParams.displayName"
          placeholder="请输入显示名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select
          v-model="queryParams.type"
          placeholder="请选择类型"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.QUESTION_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否统计" prop="isStat">
        <el-select v-model="queryParams.isStat" class="!w-240px" clearable placeholder="请选择状态">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_YES_NO)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" class="!w-240px" clearable placeholder="请选择状态">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
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
          v-hasPermi="['system:question:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
<!--        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['system:question:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>-->
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true"
              highlight-current-row
              @current-change="handleCurrentChange">
      <el-table-column label="ID" align="center" prop="id"  min-width="100px"/>
      <el-table-column label="量表主题" align="center" prop="name" min-width="250px" />
      <el-table-column label="显示名称" align="center" prop="displayName" min-width="180px" />
      <el-table-column label="类型" align="center" prop="type" min-width="100px">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.QUESTION_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="封面" min-width="80" prop="picUrl">
        <template #default="{ row }">
          <el-image :src="row.picUrl" class="h-30px w-30px" @click="imagePreview(row.picUrl)" />
        </template>
      </el-table-column>
      <el-table-column label="排序号" align="center" prop="orderNo" width="100px" />
      <el-table-column
        label="开始时间"
        align="center"
        prop="startTime"
        :formatter="dateFormatter2"
        width="120px"
      />
      <el-table-column
        label="结束时间"
        align="center"
        prop="endTime"
        :formatter="dateFormatter2"
        width="120px"
      />
      <el-table-column align="center" label="是否统计" min-width="100" prop="isStat">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_YES_NO" :value="scope.row.isStat" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" min-width="100" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="操作" align="center" width="180px" fixed="right">
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

  <ContentWrap>
    <el-tabs model-value="questionTopicItem">
      <el-tab-pane label="题目管理" name="questionTopicItem">
        <QuestionTopicList :question-id="currentRowId" />
      </el-tab-pane>
      <el-tab-pane label="因子管理" name="questionFactorItem">
        <QuestionFactorList :question-id="currentRowId" />
      </el-tab-pane>
      <el-tab-pane label="维度管理" name="questionDimensionItem">
        <QuestionDimensionList :question-id="currentRowId" />
      </el-tab-pane>
    </el-tabs>
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <QuestionForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter,dateFormatter2 } from '@/utils/formatTime'
import download from '@/utils/download'
import * as QuestionApi from '@/api/system/question'
import QuestionForm from './QuestionForm.vue'
import { createImageViewer } from '@/components/ImageViewer'
import QuestionTopicList from "./components/QuestionTopicList.vue";
import QuestionFactorList from "./components/QuestionFactorList.vue";
import QuestionDimensionList from "./components/QuestionDimensionList.vue";
import {DICT_TYPE, getIntDictOptions} from "@/utils/dict";

defineOptions({ name: 'Question' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref([]) // 列表的数据
const total = ref(0) // 列表的总页数
/** 选中行操作 */
const currentRowId = ref(-1) // 选中行记录ID
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  name: undefined,
  displayName: undefined,
  type: undefined,
  status: undefined,
  isStat: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await QuestionApi.getQuestionPage(queryParams)
    list.value = data.list
    total.value = data.total
    let row = {id:-1}
    if (data.list.length>0){
      row =data.list[data.list.length-1]
    }
    handleCurrentChange(row)
  } finally {
    loading.value = false
  }
}

/** 封面预览 */
const imagePreview = (imgUrl: string) => {
  createImageViewer({
    urlList: [imgUrl]
  })
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
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await QuestionApi.deleteQuestion(id)
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
    const data = await QuestionApi.exportQuestion(queryParams)
    download.excel(data, '量表管理.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

const handleCurrentChange = (row) => {
  let tid = -1
  if (row!=undefined && row.id!=undefined){
      tid = row.id
  }
  currentRowId.value = tid
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>