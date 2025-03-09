<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="150px"
      v-loading="formLoading"
    >
      <el-form-item label="外部联系人ID" prop="externalUserid">
        <el-input v-model="formData.externalUserid" placeholder="请输入外部联系人ID" />
      </el-form-item>
      <el-form-item label="外部联系人名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入外部联系人名称" />
      </el-form-item>
      <el-form-item label="外部联系人头像" prop="avatar">
        <UploadImg v-model="formData.avatar" height="80px" />
      </el-form-item>
      <el-form-item label="外部联系人类型" prop="type">
        <el-input v-model="formData.type" placeholder="请输入外部联系人类型" />
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-input v-model="formData.gender" placeholder="请输入性别" />
      </el-form-item>
      <el-form-item label="微信unionid" prop="unionid">
        <el-input v-model="formData.unionid" placeholder="请输入微信unionid" />
      </el-form-item>
      <el-form-item label="跟进成员ID" prop="followUserid">
        <el-input v-model="formData.followUserid" placeholder="请输入跟进成员ID" />
      </el-form-item>
      <el-form-item label="标签" prop="tags">
        <el-input v-model="formData.tags" placeholder="请输入标签" />
      </el-form-item>
      <el-form-item label="手机号" prop="mobiles">
        <el-input v-model="formData.mobiles" placeholder="请输入手机号" />
      </el-form-item>
      <el-form-item label="备注" prop="followRemark">
        <el-input v-model="formData.followRemark" placeholder="请输入备注" />
      </el-form-item>
      <el-form-item label="描述" prop="followDescription">
        <el-input v-model="formData.followDescription" placeholder="请输入描述" />
      </el-form-item>
      <el-form-item label="客户来源" prop="followAddWay">
        <el-input v-model="formData.followAddWay" placeholder="请输入客户来源" />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input v-model="formData.userId" placeholder="请输入用户ID" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import * as QwUserApi from '@/api/member/qwuser'

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  externalUserid: undefined,
  name: undefined,
  avatar: undefined,
  type: undefined,
  gender: undefined,
  unionid: undefined,
  followUserid: undefined,
  followRemark: undefined,
  followDescription: undefined,
  followAddWay: undefined,
  userId: undefined,
  tags: undefined,
  mobiles: undefined,
})
const formRules = reactive({
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
      formData.value = await QwUserApi.getQwUser(id)
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
    const data = formData.value as unknown as QwUserApi.QwUserVO
    if (formType.value === 'create') {
      await QwUserApi.createQwUser(data)
      message.success(t('common.createSuccess'))
    } else {
      await QwUserApi.updateQwUser(data)
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
    externalUserid: undefined,
    name: undefined,
    avatar: undefined,
    type: undefined,
    gender: undefined,
    unionid: undefined,
    followUserid: undefined,
    followRemark: undefined,
    followDescription: undefined,
    followAddWay: undefined,
    userId: undefined,
    tags: undefined,
    mobiles: undefined,
  }
  formRef.value?.resetFields()
}
</script>