<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="90%">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item label="用户ID" prop="userId">
            <el-input v-model="formData.userId" placeholder="请输入用户ID" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用户昵称" prop="nickname">
            <el-input v-model="formData.nickname" placeholder="请输入用户昵称" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号" prop="mobile">
            <el-input v-model="formData.mobile" placeholder="请输入手机号" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="问卷ID" prop="questionId">
            <el-input v-model="formData.questionId" placeholder="请输入问卷ID" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="量表主题" prop="questionName">
            <el-input v-model="formData.questionName" placeholder="请输入量表主题" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="答题开始时间" prop="startTime">
            <el-date-picker
              v-model="formData.startTime"
              type="datetime"
              disabled
              value-format="x"
              placeholder="选择答题开始时间"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="答题结束时间" prop="endTime">
            <el-date-picker
              v-model="formData.endTime"
              type="datetime"
              disabled
              value-format="x"
              placeholder="选择答题结束时间"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="答题时间" prop="answerTime">
            <el-input v-model="formData.answerTime" placeholder="请输入答题时间" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="答案内容">
            <el-table
              :data="answerList"
              border
              class="tabNumWidth"
              max-height="600"
              size="small"
            >
              <el-table-column align="center" fixed="left" label="序号" min-width="30">
                <template #default="{ row }">
                  {{ row.indexNo }}
                </template>
              </el-table-column>
              <el-table-column align="left" fixed="left" label="题目" min-width="200">
                <template #default="{ row }">
                  {{ row.name }}
                </template>
              </el-table-column>
              <el-table-column align="left" fixed="left" label="答案" min-width="150">
                <template #default="{ row }">
                  {{ row.answerDesc }}
                </template>
              </el-table-column>
              <el-table-column align="center" fixed="left" label="分值" min-width="30">
                <template #default="{ row }">
                  {{ row.score }}
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
        </el-col>
<!--        <el-col :span="24">-->
<!--          <el-form-item label="报告标题" prop="resultTitle">-->
<!--            <el-input v-model="formData.resultTitle" placeholder="请输入报告标题" disabled />-->
<!--          </el-form-item>-->
<!--        </el-col>-->
        <el-col :span="24">
          <el-form-item label="报告结果" prop="result">
            <el-input v-model="formData.result" type="textarea" rows="12"  placeholder="请输入报告结果" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="统计结果" prop="resultStatus">
            <el-input v-model="formData.resultStatus" placeholder="请输入统计结果" disabled />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">关 闭</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import * as QuestionRecordApi from '@/api/member/questionrecord'
import {ElTable} from "element-plus";

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const answerList = ref([]) // 答案内容的数据

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  userId: undefined,
  questionId: undefined,
  answer: undefined,
  startTime: undefined,
  endTime: undefined,
  answerTime: undefined,
  resultTitle: undefined,
  result: undefined,
  resultStatus: undefined,
  resultParam: undefined
})
const formRules = reactive({
  userId: [{ required: true, message: '用户ID不能为空', trigger: 'blur' }],
  questionId: [{ required: true, message: '问卷ID不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = "查看"
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await QuestionRecordApi.getQuestionRecord(id)
      let array = []
      if (formData.value.answer){
        array = JSON.parse(formData.value.answer)
      }
      answerList.value = array
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as QuestionRecordApi.QuestionRecordVO
    if (formType.value === 'create') {
      await QuestionRecordApi.createQuestionRecord(data)
      message.success(t('common.createSuccess'))
    } else {
      await QuestionRecordApi.updateQuestionRecord(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    userId: undefined,
    questionId: undefined,
    answer: undefined,
    startTime: undefined,
    endTime: undefined,
    answerTime: undefined,
    resultTitle: undefined,
    result: undefined,
    resultStatus: undefined,
    resultParam: undefined
  }
  formRef.value?.resetFields()
}
</script>