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
      <el-form-item label="图标" prop="logo">
        <UploadImg v-model="formData.logo" />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="formData.type" placeholder="请选择类型">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.MEMBER_TASK_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="业务类型" prop="bizType">
        <el-select v-model="formData.bizType" placeholder="请选择业务类型">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.MEMBER_TASK_BIZ_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="奖励积分" prop="point">
        <el-input v-model="formData.point" placeholder="请输入奖励积分" />
      </el-form-item>
      <el-form-item label="奖励彩虹值" prop="rainbow">
        <el-input v-model="formData.rainbow" placeholder="请输入奖励彩虹值" />
      </el-form-item>
      <el-form-item label="限制类型" prop="limitType">
        <el-select v-model="formData.limitType" placeholder="请选择限制类型">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.MEMBER_TASK_LIMIT_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="限制次数" prop="limitCount">
        <el-input v-model="formData.limitCount" placeholder="请输入限制次数" />
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
import * as TaskConfigApi from '@/api/member/task'
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
  logo: undefined,
  type: undefined,
  bizType: undefined,
  point: undefined,
  rainbow: undefined,
  limitType: undefined,
  limitCount: undefined,
  remarks: undefined
})
const formRules = reactive({
  title: [{ required: true, message: '标题不能为空', trigger: 'blur' }],
  type: [{ required: true, message: '类型不能为空', trigger: 'change' }],
  bizType: [{ required: true, message: '业务类型不能为空', trigger: 'change' }],
  point: [{ required: true, message: '奖励积分不能为空', trigger: 'blur' }],
  rainbow: [{ required: true, message: '奖励彩虹值不能为空', trigger: 'blur' }],
  limitType: [{ required: true, message: '限制类型不能为空', trigger: 'change' }],
  limitCount: [{ required: true, message: '限制次数不能为空', trigger: 'blur' }]
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
      formData.value = await TaskConfigApi.getTaskConfig(id)
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
    const data = formData.value as unknown as TaskConfigApi.TaskConfigVO
    if (formType.value === 'create') {
      await TaskConfigApi.createTaskConfig(data)
      message.success(t('common.createSuccess'))
    } else {
      await TaskConfigApi.updateTaskConfig(data)
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
    logo: undefined,
    type: undefined,
    bizType: undefined,
    point: 0,
    rainbow: 0,
    limitType: undefined,
    limitCount: 0,
    remarks: undefined
  }
  formRef.value?.resetFields()
}
</script>