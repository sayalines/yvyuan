<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="统计时间" prop="statTime">
        <el-date-picker
          v-model="formData.statTime"
          type="date"
          value-format="x"
          placeholder="选择统计时间"
        />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input v-model="formData.userId" placeholder="请输入用户ID" />
      </el-form-item>
      <el-form-item label="访问页面" prop="title">
        <el-input v-model="formData.title" placeholder="请输入访问页面" />
      </el-form-item>
      <el-form-item label="访问数" prop="visitCount">
        <el-input v-model="formData.visitCount" placeholder="请输入访问数" />
      </el-form-item>
      <el-form-item label="点击数" prop="clickCount">
        <el-input v-model="formData.clickCount" placeholder="请输入点击数" />
      </el-form-item>
      <el-form-item label="第一次访问时间" prop="minTime">
        <el-date-picker
          v-model="formData.minTime"
          type="date"
          value-format="x"
          placeholder="选择第一次访问时间"
        />
      </el-form-item>
      <el-form-item label="最后一次访问时间" prop="maxTime">
        <el-date-picker
          v-model="formData.maxTime"
          type="date"
          value-format="x"
          placeholder="选择最后一次访问时间"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import * as ActionLogApi from '@/api/member/memberactionlog'

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  statTime: undefined,
  userId: undefined,
  title: undefined,
  visitCount: undefined,
  clickCount: undefined,
  minTime: undefined,
  maxTime: undefined
})
const formRules = reactive({
  statTime: [{ required: true, message: '统计时间不能为空', trigger: 'blur' }]
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
      formData.value = await ActionLogApi.getActionLog(id)
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
    const data = formData.value as unknown as ActionLogApi.ActionLogVO
    if (formType.value === 'create') {
      await ActionLogApi.createActionLog(data)
      message.success(t('common.createSuccess'))
    } else {
      await ActionLogApi.updateActionLog(data)
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
    statTime: undefined,
    userId: undefined,
    title: undefined,
    visitCount: undefined,
    clickCount: undefined,
    minTime: undefined,
    maxTime: undefined
  }
  formRef.value?.resetFields()
}
</script>