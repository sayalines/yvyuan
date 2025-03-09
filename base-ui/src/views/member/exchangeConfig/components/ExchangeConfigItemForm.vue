<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="配置ID" prop="configId">
        <el-input v-model="formData.configId" placeholder="请输入配置ID" />
      </el-form-item>
      <el-form-item label="编码" prop="code">
        <el-input v-model="formData.code" placeholder="请输入编码" />
      </el-form-item>
      <el-form-item label="业务类型" prop="bizType">
        <el-select v-model="formData.bizType" placeholder="请选择业务类型">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="物品ID" prop="bizId">
        <el-input v-model="formData.bizId" placeholder="请输入物品ID" />
      </el-form-item>
      <el-form-item label="总次数" prop="totalCount">
        <el-input v-model="formData.totalCount" placeholder="请输入总次数" />
      </el-form-item>
      <el-form-item label="已使用次数" prop="useCount">
        <el-input v-model="formData.useCount" placeholder="请输入已使用次数" />
      </el-form-item>
      <el-form-item label="有效开始时间" prop="validStartTime">
        <el-date-picker
          v-model="formData.validStartTime"
          type="date"
          value-format="x"
          placeholder="选择有效开始时间"
        />
      </el-form-item>
      <el-form-item label="有效结束时间" prop="validEndTime">
        <el-date-picker
          v-model="formData.validEndTime"
          type="date"
          value-format="x"
          placeholder="选择有效结束时间"
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
import * as ExchangeConfigItemApi from '@/api/member/exchangeConfigItem'

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  configId: undefined,
  code: undefined,
  bizType: undefined,
  bizId: undefined,
  totalCount: undefined,
  useCount: undefined,
  validStartTime: undefined,
  validEndTime: undefined
})
const formRules = reactive({
  bizType: [{ required: true, message: '业务类型不能为空', trigger: 'change' }]
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
      formData.value = await ExchangeConfigItemApi.getExchangeConfigItem(id)
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
    const data = formData.value as unknown as ExchangeConfigItemApi.ExchangeConfigItemVO
    if (formType.value === 'create') {
      await ExchangeConfigItemApi.createExchangeConfigItem(data)
      message.success(t('common.createSuccess'))
    } else {
      await ExchangeConfigItemApi.updateExchangeConfigItem(data)
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
    configId: undefined,
    code: undefined,
    bizType: undefined,
    bizId: undefined,
    totalCount: undefined,
    useCount: undefined,
    validStartTime: undefined,
    validEndTime: undefined
  }
  formRef.value?.resetFields()
}
</script>