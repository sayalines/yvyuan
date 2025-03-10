<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="80%">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="160px"
      v-loading="formLoading"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item label="用户ID" prop="userId">
            <el-input v-model="formData.userId" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用户昵称" prop="nickname">
            <el-input v-model="formData.nickname" disabled  />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="品牌" prop="brand">
            <el-input v-model="formData.brand" disabled  />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="标题" prop="title">
            <el-input v-model="formData.title" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="内容" prop="content">
            <Ueditor v-model="formData.content" disabled height="350px"/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="封面图" prop="picUrl">
            <UploadImg v-model="formData.picUrl" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="附件" prop="fileUrl">
            <UploadFile v-model="formData.fileUrl" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否热门" prop="isHot">
            <el-select v-model="formData.isHot" placeholder="请选择是否热门" disabled>
                  <el-option
                      v-for="dict in getIntDictOptions('system_yes_no')"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                  />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否精选" prop="isChoice">
            <el-select v-model="formData.isChoice" placeholder="请选择是否精选" disabled>
                  <el-option
                      v-for="dict in getIntDictOptions('system_yes_no')"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                  />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收藏数" prop="collectCount">
            <el-input v-model="formData.collectCount" placeholder="请输入收藏数" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="点赞数" prop="likeCount">
            <el-input v-model="formData.likeCount" placeholder="请输入点赞数" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评论数" prop="reviewCount">
            <el-input v-model="formData.reviewCount" placeholder="请输入评论数" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-select v-model="formData.status" placeholder="请选择状态" disabled>
                  <el-option
                      v-for="dict in getIntDictOptions('ext_community_status')"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                  />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审核时间" prop="auditTime">
            <el-date-picker
              disabled
              v-model="formData.auditTime"
              type="datetime"
              value-format="x"
              placeholder="选择审核时间"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="拒绝原因" prop="reason">
            <el-input v-model="formData.reason"  type="textarea" rows="4" disabled placeholder="请输入拒绝原因" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
<!--      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>-->
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { getIntDictOptions, getBoolDictOptions } from '@/utils/dict'
import * as CommunityApi from '@/api/member/community'

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  userId: undefined,
  brandId: undefined,
  title: undefined,
  content: undefined,
  picUrl: undefined,
  fileUrl: undefined,
  isHot: undefined,
  isChoice: undefined,
  collectCount: undefined,
  likeCount: undefined,
  reviewCount: undefined,
  status: undefined,
  auditTime: undefined,
  reason: undefined
})
const formRules = reactive({
  userId: [{ required: true, message: '用户ID不能为空', trigger: 'blur' }],
  title: [{ required: true, message: '标题不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = '详细'
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await CommunityApi.getCommunity(id)
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
    const data = formData.value as unknown as CommunityApi.CommunityVO
    if (formType.value === 'create') {
      await CommunityApi.createCommunity(data)
      message.success(t('common.createSuccess'))
    } else {
      await CommunityApi.updateCommunity(data)
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
    brandId: undefined,
    title: undefined,
    content: undefined,
    picUrl: undefined,
    fileUrl: undefined,
    isHot: undefined,
    isChoice: undefined,
    collectCount: undefined,
    likeCount: undefined,
    reviewCount: undefined,
    status: undefined,
    auditTime: undefined,
    reason: undefined
  }
  formRef.value?.resetFields()
}
</script>