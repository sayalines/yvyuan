<template>
  <Dialog title="修改用户彩虹值" v-model="dialogVisible" width="600">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="用户编号" prop="id">
        <el-input v-model="formData.id" class="!w-240px" disabled />
      </el-form-item>
      <el-form-item label="用户昵称" prop="nickname">
        <el-input v-model="formData.nickname" class="!w-240px" disabled />
      </el-form-item>
      <el-form-item label="变动前彩虹值" prop="rainbow">
        <el-input-number v-model="formData.rainbow" class="!w-240px" disabled />
      </el-form-item>
      <el-form-item label="变动类型" prop="changeType">
        <el-radio-group v-model="formData.changeType">
          <el-radio :label="1">增加</el-radio>
          <el-radio :label="-1">减少</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="变动彩虹值" prop="changeRainbow">
        <el-input-number v-model="formData.changeRainbow" class="!w-240px" :min="0" :precision="0" />
      </el-form-item>
      <el-form-item label="变动后彩虹值">
        <el-input-number v-model="rainbowResult" class="!w-240px" disabled />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import * as UserApi from '@/api/member/user'

/** 修改用户彩虹值表单 */
defineOptions({ name: 'UpdateRainbowForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formData = ref({
  id: undefined,
  nickname: undefined,
  rainbow: 0,
  changeRainbow: 0,
  changeType: 1
})
const formRules = reactive({
  changeRainbow: [{ required: true, message: '变动彩虹值不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (id?: number) => {
  dialogVisible.value = true
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await UserApi.getUser(id)
      formData.value.changeType = 1 // 默认增加彩虹值
      formData.value.changeRainbow = 0 // 变动彩虹值默认0
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
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return

  if (formData.value.changeRainbow < 1) {
    message.error('变动彩虹值不能小于 1')
    return
  }
  if (rainbowResult.value < 0) {
    message.error('变动后的彩虹值不能小于 0')
    return
  }

  // 提交请求
  formLoading.value = true
  try {
    await UserApi.updateUserRainbow({
      id: formData.value.id,
      rainbow: formData.value.changeRainbow * formData.value.changeType
    })

    message.success(t('common.updateSuccess'))
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
    nickname: undefined,
    levelId: undefined,
    reason: undefined
  }
  formRef.value?.resetFields()
}

/** 变动后的彩虹值 */
const rainbowResult = computed(
  () => formData.value.rainbow + formData.value.changeRainbow * formData.value.changeType
)
</script>
