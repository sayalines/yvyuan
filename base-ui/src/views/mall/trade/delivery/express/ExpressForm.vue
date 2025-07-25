<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="公司编码" prop="code">
        <el-input v-model="formData.code" placeholder="请输入快递编码" />
      </el-form-item>
      <el-form-item label="公司名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入快递名称" />
      </el-form-item>
      <el-form-item label="公司 logo" prop="logo">
        <UploadImg v-model="formData.logo" :limit="1" :is-show-tip="false" />
        <div style="font-size: 10px" class="pl-10px">推荐 180x180 图片分辨率</div>
      </el-form-item>
      <el-form-item label="快递接口代码" prop="apiCode">
        <el-input v-model="formData.apiCode" placeholder="请输入快递接口代码" />
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input-number v-model="formData.sort" controls-position="right" :min="0" />
      </el-form-item>
      <el-form-item label="开启状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
            :key="dict.value"
            :label="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { CommonStatusEnum } from '@/utils/constants'
import * as DeliveryExpressApi from '@/api/mall/trade/delivery/express'

defineOptions({ name: 'ExpressForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  code: '',
  name: '',
  logo: '',
  apiCode: '',
  sort: 0,
  status: CommonStatusEnum.ENABLE
})
const formRules = reactive({
  code: [{ required: true, message: '快递编码不能为空', trigger: 'blur' }],
  name: [{ required: true, message: '分类名称不能为空', trigger: 'blur' }],
  logo: [{ required: true, message: '分类图片不能为空', trigger: 'blur' }],
  sort: [{ required: true, message: '分类排序不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '开启状态不能为空', trigger: 'blur' }]
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
      formData.value = await DeliveryExpressApi.getDeliveryExpress(id)
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
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as DeliveryExpressApi.DeliveryExpressVO
    if (formType.value === 'create') {
      await DeliveryExpressApi.createDeliveryExpress(data)
      message.success(t('common.createSuccess'))
    } else {
      await DeliveryExpressApi.updateDeliveryExpress(data)
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
    name: '',
    picUrl: '',
    apiCode: '',
    bigPicUrl: '',
    status: CommonStatusEnum.ENABLE
  }
  formRef.value?.resetFields()
}
</script>
