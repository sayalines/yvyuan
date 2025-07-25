<template>
  <Dialog v-model="dialogVisible" title="题目导入" width="500">
    <el-upload
      ref="uploadRef"
      v-model:file-list="fileList"
      :action="importUrl + questionId + '?updateSupport=' + updateSupport"
      :auto-upload="false"
      :disabled="formLoading"
      :headers="uploadHeaders"
      :limit="1"
      :on-error="submitFormError"
      :on-exceed="handleExceed"
      :on-success="submitFormSuccess"
      accept=".xlsx, .xls"
      drag
    >
      <Icon icon="ep:upload" />
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <template #tip>
        <div class="el-upload__tip text-center">
          <div class="el-upload__tip">
            <el-checkbox v-model="updateSupport" />
            是否更新已经存在的相同题目编号的数据
          </div>
          <span>仅允许导入 xls、xlsx 格式文件。</span>
          <el-link
            :underline="false"
            style="font-size: 12px; vertical-align: baseline"
            type="primary"
            @click="importTemplate"
          >
            下载模板
          </el-link>
        </div>
      </template>
    </el-upload>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as QuestionTopicApi from '@/api/system/questiontopic'
import { getAccessToken, getTenantId } from '@/utils/auth'
import download from '@/utils/download'

defineOptions({ name: 'QuestionTopicImportForm.vue' })
const message = useMessage() // 消息弹窗
const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中
const uploadRef = ref()
const importUrl = import.meta.env.VITE_BASE_URL + import.meta.env.VITE_API_URL + '/system/question-topic/excel/'
const uploadHeaders = ref() // 上传 Header 头
const fileList = ref([]) // 文件列表
const updateSupport = ref(0) // 是否更新已经存在的用户数据
const questionId = ref(0)

/** 打开弹窗 */
const open = (id) => {
  dialogVisible.value = true
  questionId.value = id
  resetForm()
  fileList.value = []
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const submitForm = async () => {
  if (fileList.value.length == 0) {
    message.error('请上传文件')
    return
  }
  // 提交请求
  uploadHeaders.value = {
    Authorization: 'Bearer ' + getAccessToken(),
    'tenant-id': getTenantId()
  }
  formLoading.value = true
  uploadRef.value!.submit()
}

/** 文件上传成功 */
const emits = defineEmits(['success'])
const submitFormSuccess = (response: any) => {
  if (response.code !== 0) {
    message.error(response.msg)
    formLoading.value = false
    return
  }
  // 拼接提示语
  /*const data = response.data
  let text = '上传成功数量：' + data.createProducts.length + ';'
  for (let product of data.createProducts) {
    text += '< ' + product + ' >'
  }
  text += '更新成功数量：' + data.updateProducts.length + ';'
  for (const product of data.updateProducts) {
    text += '< ' + product + ' >'
  }
  text += '更新失败数量：' + Object.keys(data.failureProducts).length + ';'
  for (const product in data.failureProducts) {
    text += '< ' + product + ': ' + data.failureProducts[product] + ' >'
  }*/
  fileList.value = [];
  dialogVisible.value = false;
  message.alert(response.msg)
  // 发送操作成功的事件
  emits('success')
}

/** 上传错误提示 */
const submitFormError = (): void => {
  message.error('上传失败，请您重新上传！')
  formLoading.value = false
}

/** 重置表单 */
const resetForm = () => {
  // 重置上传状态和文件
  formLoading.value = false
  uploadRef.value?.clearFiles()
}

/** 文件数超出提示 */
const handleExceed = (): void => {
  message.error('最多只能上传一个文件！')
}

/** 下载模板操作 */
const importTemplate = async () => {
  const res = await QuestionTopicApi.importQuestionTopicMoudle()
  download.excel(res, '题目导入模版.xls')
}
</script>
