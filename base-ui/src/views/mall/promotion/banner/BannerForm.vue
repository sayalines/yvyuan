<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="70%">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="100px"
    >
      <el-row>
        <el-col :span="24">
          <el-form-item label="标题" prop="title">
            <el-input v-model="formData.title" placeholder="请输入标题" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="图片" prop="picUrl">
            <UploadImg v-model="formData.picUrl" />
          </el-form-item>
        </el-col>
<!--        <el-col :span="24">-->
<!--          <el-form-item label="WAP图片" prop="wapPicUrl">-->
<!--            <UploadImg v-model="formData.wapPicUrl" />-->
<!--          </el-form-item>-->
<!--        </el-col>-->
        <el-col :span="24">
          <el-form-item label="视频" prop="videoUrl">
            <UploadVideo v-model="formData.videoUrl" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="跳转地址" prop="url">
            <el-input v-model="formData.url" placeholder="请输入跳转地址" />
          </el-form-item>
        </el-col>
<!--        <el-col :span="24">-->
<!--          <el-form-item label="图片尺寸类型" prop="picSize">-->
<!--            <el-radio-group v-model="formData.picSize">-->
<!--              <el-radio-->
<!--                v-for="dict in getStrDictOptions(DICT_TYPE.BANNER_PICTURE_SIZE)"-->
<!--                :key="dict.value"-->
<!--                :label="dict.value"-->
<!--              >-->
<!--                {{ dict.label }}-->
<!--              </el-radio>-->
<!--            </el-radio-group>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
        <el-col :span="24">
          <el-form-item label="排序" prop="sort">
            <el-input-number v-model="formData.sort" :min="0" clearable controls-position="right" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="状态" prop="status">
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
        </el-col>
        <el-col :span="24">
          <el-form-item label="位置" prop="position">
            <el-radio-group v-model="formData.position">
              <el-radio
                v-for="dict in getIntDictOptions(DICT_TYPE.PROMOTION_BANNER_POSITION)"
                :key="dict.value"
                :label="dict.value"
              >
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="memo">
            <el-input v-model="formData.memo" placeholder="请输入备注" type="textarea" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备用属性01" prop="attr01">
            <el-input v-model="formData.attr01" placeholder="请输入备用属性01" />
          </el-form-item>
        </el-col>
        <div></div>
        <el-col :span="12">
          <el-form-item label="备用属性02" prop="attr02">
            <el-input v-model="formData.attr02" placeholder="请输入备用属性02" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备用属性03" prop="attr03">
            <el-input v-model="formData.attr03" placeholder="请输入备用属性03" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备用属性04" prop="attr04">
            <el-input v-model="formData.attr04" placeholder="请输入备用属性04" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备用属性05" prop="attr05">
            <el-input v-model="formData.attr05" placeholder="请输入备用属性05" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
// import {DICT_TYPE, getIntDictOptions, getStrDictOptions} from '@/utils/dict'
import {DICT_TYPE, getIntDictOptions} from '@/utils/dict'
import * as BannerApi from '@/api/mall/market/banner'

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  title: undefined,
  picUrl: undefined,
  // wapPicUrl: undefined,
  videoUrl: undefined,
  picSize: undefined,
  status: 0,
  position: 1,
  url: undefined,
  sort: 0,
  memo: undefined,
  attr01: undefined,
  attr02: undefined,
  attr03: undefined,
  attr04: undefined,
  attr05: undefined,
})
const formRules = reactive({
  title: [{ required: true, message: '标题不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '活动状态不能为空', trigger: 'blur' }],
  position: [{ required: true, message: '位置不能为空', trigger: 'blur' }],
  sort: [{ required: true, message: '排序不能为空', trigger: 'blur' }]
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
      formData.value = await BannerApi.getBanner(id)
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
    const data = formData.value as unknown as BannerApi.BannerVO
    if (formType.value === 'create') {
      await BannerApi.createBanner(data)
      message.success(t('common.createSuccess'))
    } else {
      await BannerApi.updateBanner(data)
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
    // picSize: undefined,
    picUrl: undefined,
    // wapPicUrl: undefined,
    videoUrl: undefined,
    status: 0,
    position: 1,
    url:"#",
    sort: 0,
    memo: undefined,
    attr01: undefined,
    attr02: undefined,
    attr03: undefined,
    attr04: undefined,
    attr05: undefined,
  }
  formRef.value?.resetFields()
}
</script>
