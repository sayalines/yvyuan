<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="标题" prop="title">
        <el-input v-model="formData.title" placeholder="请输入标题" />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input v-model="formData.description" placeholder="请输入描述" />
      </el-form-item>
      <el-form-item label="兑换类型" prop="bizType">
        <el-select v-model="formData.bizType" placeholder="请选择兑换类型">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.MEMBER_EXCHANGE_BIZ_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="编号前缀" prop="prefix">
        <el-input v-model="formData.prefix" placeholder="请输入编号前缀" />
      </el-form-item>
      <el-form-item label="物品ID" prop="bizId">
        <el-input v-model="formData.bizId" placeholder="请输入物品ID" />
      </el-form-item>
      <el-form-item label="使用次数" prop="useCount">
        <el-input v-model="formData.useCount" placeholder="请输入使用次数" />
      </el-form-item>
      <el-form-item label="有效开始时间" prop="validStartTime">
        <el-date-picker
          v-model="formData.validStartTime"
          type="datetime"
          value-format="x"
          placeholder="选择有效开始时间"
        />
      </el-form-item>
      <el-form-item label="有效结束时间" prop="validEndTime">
        <el-date-picker
          v-model="formData.validEndTime"
          type="datetime"
          value-format="x"
          placeholder="选择有效结束时间"
        />
      </el-form-item>
      <el-form-item label="兑换码长度" prop="codeLength">
        <el-input v-model="formData.codeLength" placeholder="请输入兑换码长度" />
      </el-form-item>
      <el-form-item label="兑换上限" prop="dayCount">
        <el-input v-model="formData.dayCount" placeholder="请输入兑换上限" />
      </el-form-item>
      <el-form-item label="白名单用户" prop="whiteUsers">
        <el-input v-model="formData.whiteUsers" rows="3" placeholder="请输入白名单用户,多个用户用逗号隔开" type="textarea" />
      </el-form-item>
      <el-form-item label="黑名单用户" prop="blackUsers">
        <el-input v-model="formData.blackUsers" rows="3" placeholder="请输入黑名单用户,多个用户用逗号隔开" type="textarea" />
      </el-form-item>
      <el-form-item label="备注" prop="remarks">
        <el-input v-model="formData.remarks" placeholder="请输入备注" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import * as ExchangeConfigApi from '@/api/member/exchangeConfig'
import {DICT_TYPE, getIntDictOptions} from "@/utils/dict";

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  title: undefined,
  description: undefined,
  bizType: undefined,
  useCount: undefined,
  prefix: undefined,
  remarks: undefined,
  bizId: undefined,
  validStartTime: undefined,
  validEndTime: undefined,
  codeLength: undefined,
  dayCount: undefined,
  blackUsers: undefined,
  whiteUsers: undefined,
})
const formRules = reactive({
  title: [{ required: true, message: '标题不能为空', trigger: 'blur' }],
  bizType: [{ required: true, message: '业务类型不能为空', trigger: 'change' }],
  codeLength: [{ required: true, message: '兑换码长度不能为空', trigger: 'change' }],
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
      formData.value = await ExchangeConfigApi.getExchangeConfig(id)
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
    const data = formData.value as unknown as ExchangeConfigApi.ExchangeConfigVO
    if (formType.value === 'create') {
      await ExchangeConfigApi.createExchangeConfig(data)
      message.success(t('common.createSuccess'))
    } else {
      await ExchangeConfigApi.updateExchangeConfig(data)
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
    title: undefined,
    description: undefined,
    bizType: undefined,
    useCount: 1,
    prefix: undefined,
    remarks: undefined,
    bizId: undefined,
    validStartTime: undefined,
    validEndTime: undefined,
    codeLength: 10,
    dayCount: 0,
    blackUsers: undefined,
    whiteUsers: undefined,
  }
  formRef.value?.resetFields()
}
</script>