<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="用户昵称" prop="userName">
        <el-input v-model="formData.userName" placeholder="请输入用户昵称" />
      </el-form-item>
      <el-form-item label="手机号" prop="mobile">
        <el-input v-model="formData.mobile" placeholder="请输入手机号" />
      </el-form-item>
      <el-form-item label="问题内容" prop="content">
        <el-input v-model="formData.content" placeholder="请输入问题内容" />
      </el-form-item>
      <el-form-item label="解决状态" prop="ifSolve">
        <el-select v-model="formData.ifSolve" placeholder="请选择解决状态">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_SOLVE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import * as QuestionApi from '@/api/member/feedback'
import {DICT_TYPE, getIntDictOptions} from "@/utils/dict";

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  userName: undefined,
  mobile: undefined,
  content: undefined,
  ifSolve: undefined
})
const formRules = reactive({
  content: [{ required: true, message: '问题内容不能为空', trigger: 'blur' }],
  ifSolve: [{ required: true, message: '解决状态不能为空', trigger: 'change' }]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await QuestionApi.getQuestion(id)
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
    const data = formData.value as unknown as QuestionApi.QuestionVO
    if (formType.value === 'create') {
      await QuestionApi.createQuestion(data)
      message.success(t('common.createSuccess'))
    } else {
      await QuestionApi.updateQuestion(data)
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
    userName: undefined,
    mobile: undefined,
    content: undefined,
    ifSolve: undefined
  }
  formRef.value?.resetFields()
}
</script>