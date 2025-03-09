<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="80%">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="题目编号" prop="code">
        <el-input v-model="formData.code" placeholder="请输入题目编号" />
      </el-form-item>
      <el-form-item label="题目名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入题目名称" />
      </el-form-item>
      <el-form-item label="题目类型" prop="type">
        <el-select v-model="formData.type" placeholder="请选择题目类型">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_QUESTION_TOPIC_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="选项内容">
        <el-table
          :data="opts"
          border
          class="tabNumWidth"
          max-height="500"
          size="small"
        >
          <el-table-column align="center" fixed="left" label="图标" min-width="50">
            <template #default="{ row }">
              <UploadImg v-model="row.picUrl" height="50px" width="100%" />
            </template>
          </el-table-column>
          <el-table-column align="center" label="选项值" min-width="80">
            <template #default="{ row }">
              <el-input v-model="row.value" class="w-100%" />
            </template>
          </el-table-column>
          <el-table-column align="center" label="选项内容" min-width="200">
            <template #default="{ row }">
              <el-input v-model="row.name" class="w-100%" />
            </template>
          </el-table-column>
          <el-table-column align="center" label="分值" min-width="80">
            <template #default="{ row }">
              <el-input-number v-model="row.score" class="w-100%" />
            </template>
          </el-table-column>
<!--          <el-table-column align="center" label="跳转题目编号" min-width="80">-->
<!--            <template #default="{ row }">-->
<!--              <el-input v-model="row.toCode" class="w-100%" />-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column align="center" label="是否选填" min-width="100">-->
<!--            <template #default="{ row }">-->
<!--              <el-select v-model="row.isInput" placeholder="请选择">-->
<!--                <el-option-->
<!--                  v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_YES_NO)"-->
<!--                  :key="dict.value"-->
<!--                  :label="dict.label"-->
<!--                  :value="dict.value"-->
<!--                />-->
<!--              </el-select>-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column align="center" fixed="right" label="操作" width="80">
            <template #default="{ row }">
              <el-button link size="small" type="primary" @click="deleteOpt(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-form-item>
          <el-button class="mb-10px mr-15px" style="margin-top: 10px" @click="addOption">添加</el-button>
        </el-form-item>
      </el-form-item>

<!--      <el-form-item label="选项内容" prop="optContent">-->
<!--        <el-input v-model="formData.optContent" type="textarea" placeholder="请输入选项内容" />-->
<!--      </el-form-item>-->
      <el-form-item label="排序号" prop="orderNo">
        <el-input v-model="formData.orderNo" placeholder="请输入排序号" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import * as QuestionTopicApi from '@/api/system/questiontopic'
import {DICT_TYPE, getBoolDictOptions, getIntDictOptions, getStrDictOptions} from "@/utils/dict";
import {ElTable} from "element-plus";
import {UploadImg} from "@/components/UploadFile";

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const opts = ref([]) // 选项的数据
const formData = ref({
  id: undefined,
  questionId: undefined,
  code: undefined,
  name: undefined,
  type: undefined,
  optContent: undefined,
  orderNo: undefined
})
const formRules = reactive({
  code: [{ required: true, message: '题目编号不能为空', trigger: 'blur' }],
  name: [{ required: true, message: '题目名称不能为空', trigger: 'blur' }],
  type: [{ required: true, message: '题目类型不能为空', trigger: 'blur' }],
  optContent: [{ required: true, message: '选项内容不能为空', trigger: 'blur' }],
  orderNo: [{ required: true, message: '排序号不能为空', trigger: 'blur' }],
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number, questionId: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  formData.value.questionId = questionId
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await QuestionTopicApi.getQuestionTopic(id)
      let array = []
      if (formData.value.optContent){
        array = JSON.parse(formData.value.optContent)
      }
      opts.value = array
    } finally {
      formLoading.value = false
    }
  }else{
    opts.value = []
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
    const array: any[] = []
    if (opts.value && opts.value.length>0){
      opts.value.forEach((item: any) => {
        array.push(item)
      })
    }
    formData.value.optContent = JSON.stringify(array)
    const data = formData.value as unknown as QuestionTopicApi.QuestionTopicVO
    if (formType.value === 'create') {
      await QuestionTopicApi.createQuestionTopic(data)
      message.success(t('common.createSuccess'))
    } else {
      await QuestionTopicApi.updateQuestionTopic(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

const addOption = () => {
  let item={
    picUrl:'',
    value:'',
    name:'',
    score:0
    // toCode:'',
    // isInput:0,
    // isExclusive:0
  }
  opts.value.push(item)
}

const deleteOpt = async(row) => {
  const index = opts.value.indexOf(row)
  opts.value.splice(index, 1)
}



/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    questionId: undefined,
    code: undefined,
    name: undefined,
    type: 1,
    optContent: undefined,
    orderNo: 0
  }
  formRef.value?.resetFields()
}
</script>