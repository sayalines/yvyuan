<template>
  <div class="upload-file">
    <el-upload
      ref="uploadRef"
      :multiple="props.limit > 1"
      name="file"
      v-model="valueRef"
      v-model:file-list="fileList"
      :show-file-list="true"
      :auto-upload="autoUpload"
      :action="updateUrl"
      :headers="uploadHeaders"
      :limit="props.limit"
      :drag="drag"
      :disabled="props.disabled"
      :before-upload="beforeUpload"
      :on-exceed="handleExceed"
      :on-success="handleFileSuccess"
      :on-error="excelUploadError"
      :on-remove="handleRemove"
      :on-preview="handlePreview"
      class="upload-file-uploader"
    >
      <el-button type="primary"><Icon icon="ep:upload-filled" />选取文件</el-button>
      <template v-if="isShowTip" #tip>
        <div style="font-size: 8px">
          大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB</b>
        </div>
        <div style="font-size: 8px">
          格式为 <b style="color: #f56c6c">{{ fileType.join('/') }}</b> 的文件
        </div>
      </template>
    </el-upload>
  </div>
</template>
<script lang="ts" setup>
import { propTypes } from '@/utils/propTypes'
import { getAccessToken, getTenantId } from '@/utils/auth'
import type { UploadInstance, UploadUserFile, UploadProps, UploadRawFile } from 'element-plus'
import { isArray, isString } from '@/utils/is'
import * as FileApi from '@/api/infra/file'

defineOptions({ name: 'UploadFile' })

const message = useMessage() // 消息弹窗
const emit = defineEmits(['update:modelValue'])

const props = defineProps({
  modelValue: propTypes.oneOfType<string | string[]>([String, Array<String>]).isRequired,
  title: propTypes.string.def('文件上传'),
  updateUrl: propTypes.string.def(import.meta.env.VITE_UPLOAD_URL),
  fileType: propTypes.array.def(['doc','docx', 'xls', 'xlsx', 'ppt', 'txt', 'pdf','png', 'jpg', 'jpeg','zip','rar','mp4','mp3','wav','avi']), // 文件类型, 例如['png', 'jpg', 'jpeg']
  fileSize: propTypes.number.def(500), // 大小限制(MB)
  limit: propTypes.number.def(15), // 数量限制
  autoUpload: propTypes.bool.def(true), // 自动上传
  drag: propTypes.bool.def(false), // 拖拽上传
  isShowTip: propTypes.bool.def(true), // 是否显示提示
  disabled: propTypes.bool.def(false) // 是否只读
})
// ========== 上传相关 ==========
const valueRef = ref(props.modelValue)
const uploadRef = ref<UploadInstance>()
const uploadList = ref<UploadUserFile[]>([])
const fileList = ref<UploadUserFile[]>([])
const uploadNumber = ref<number>(0)
const uploadHeaders = ref({
  Authorization: 'Bearer ' + getAccessToken(),
  'tenant-id': getTenantId()
})
// 文件上传之前判断
const beforeUpload: UploadProps['beforeUpload'] = (file: UploadRawFile) => {
  if (fileList.value.length >= props.limit) {
    message.error(`上传文件数量不能超过${props.limit}个!`)
    return false
  }
  let fileExtension = ''
  if (file.name.lastIndexOf('.') > -1) {
    fileExtension = file.name.slice(file.name.lastIndexOf('.') + 1)
  }
  const isImg = props.fileType.some((type: string) => {
    if (file.type.indexOf(type) > -1) return true
    return !!(fileExtension && fileExtension.indexOf(type) > -1)
  })
  const isLimit = file.size < props.fileSize * 1024 * 1024
  if (!isImg) {
    message.error(`文件格式不正确, 请上传${props.fileType.join('/')}格式!`)
    return false
  }
  if (!isLimit) {
    message.error(`上传文件大小不能超过${props.fileSize}MB!`)
    return false
  }
  message.success('正在上传文件，请稍候...')
  uploadNumber.value++
}
// 处理上传的文件发生变化
// const handleFileChange = (uploadFile: UploadFile): void => {
//   uploadRef.value.data.path = uploadFile.name
// }
// 文件上传成功
const handleFileSuccess: UploadProps['onSuccess'] = (res: any): void => {
  message.success('上传成功')
  const fileListNew = fileList.value
  fileListNew.pop()
  fileList.value = fileListNew
  uploadList.value.push({ name: res.data, url: res.data })
  if (uploadList.value.length == uploadNumber.value) {
    fileList.value = fileList.value.concat(uploadList.value)
    uploadList.value = []
    uploadNumber.value = 0
    emitUpdateModelValue()
  }
}
// 文件数超出提示
const handleExceed: UploadProps['onExceed'] = (): void => {
  message.error(`上传文件数量不能超过${props.limit}个!`)
}
// 上传错误提示
const excelUploadError: UploadProps['onError'] = (): void => {
  message.error('导入数据失败，请您重新上传！')
}
// 删除上传文件
const handleRemove = (file) => {
  const findex = fileList.value.map((f) => f.name).indexOf(file.name)
  if (findex > -1) {
    fileList.value.splice(findex, 1)
  }
  emitUpdateModelValue()
}
const handlePreview: UploadProps['onPreview'] = (file) => {
  console.log("file", file);
  window.open(file.url,"_blank")
}

// 监听模型绑定值变动
watch(
  () => props.modelValue,
  async() => {
    let files: string[] = []
    // 情况1：字符串
    if (isString(props.modelValue)) {
      // 情况1.1：逗号分隔的多值
      if (props.modelValue.includes(',')) {
        files = files.concat(props.modelValue.split(','))
      } else if (props.modelValue.length > 0) {
        files.push(props.modelValue)
      }
    } else if (isArray(props.modelValue)) {
      // 情况2：字符串
      files = files.concat(props.modelValue)
    } else if (props.modelValue == null) {
      // 情况3：undefined 不处理
    } else {
      throw new Error('不支持的 modelValue 类型')
    }

    // fileList.value = files.map((url: string) => {
    //   return { url, name: url.substring(url.lastIndexOf('/') + 1) } as UploadUserFile
    // })

    if (files.length>0){
      fileList.value = []
      let fileUrl = files.join(',')
      let detailList = await FileApi.getFileInfo(fileUrl)

      detailList.forEach((item) => {
        fileList.value.push(item as UploadUserFile)
      })
    }
  },
  { immediate: true }
)
// 发送文件链接列表更新
const emitUpdateModelValue = () => {
  console.log("fileList:",fileList)
  // 情况1：数组结果
  let result: string | string[] = fileList.value.map((file) => file.url!)
  // 情况2：逗号分隔的字符串
  if (props.limit>0) {
    result = result.join(',')
  }
  emit('update:modelValue', result)
}
</script>
<style scoped lang="scss">
.upload-file {
  width: 100%;
}
.upload-file-uploader {
  margin-bottom: 5px;
}

:deep(.upload-file-list .el-upload-list__item) {
  position: relative;
  margin-bottom: 10px;
  line-height: 2;
  border: 1px solid #e4e7ed;
}

:deep(.el-upload-list__item-file-name) {
  max-width: 100%;
}

:deep(.upload-file-list .ele-upload-list__item-content) {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: inherit;
}

:deep(.ele-upload-list__item-content-action .el-link) {
  margin-right: 10px;
}
</style>
