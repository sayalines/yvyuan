<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="80%">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="120px"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item label="文章标题" prop="title">
            <el-input v-model="formData.title" placeholder="请输入文章标题" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文章分类" prop="categoryId">
            <el-cascader
              v-model="formData.categoryId"
              :options="categoryTree"
              :props="defaultProps"
              class="w-1/1"
              ref="cascaderRef"
              clearable
              placeholder="请选择文章分类"
              @change="handleCategoryChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文章作者" prop="author">
            <el-input v-model="formData.author" placeholder="请输入文章作者" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="标签" prop="tag">
            <el-input v-model="formData.tag" placeholder="请输入标签" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="文章简介" prop="introduction">
            <el-input type="textarea" :rows="4" v-model="formData.introduction" placeholder="请输入文章简介" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="文章封面" prop="picUrl">
            <UploadImg v-model="formData.picUrl" height="80px" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="文章多图" prop="pictures">
            <UploadImgs v-model:modelValue="formData.pictures" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="小程序码" prop="qrcodeUrl">
            <UploadImg v-model="formData.qrcodeUrl" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="附件" prop="files">
            <UploadFile v-model:modelValue="formData.files" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="外部链接" prop="href">
            <el-input v-model="formData.href" placeholder="请输入外部链接http(s)://" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发布时间" prop="publishDate">
            <el-date-picker
              v-model="formData.publishDate"
              type="datetime"
              value-format="x"
              placeholder="选择发布时间"
              :default-value="new Date()"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="排序" prop="sort">
            <el-input-number v-model="formData.sort" :min="0" clearable controls-position="right" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="点击数" prop="hitCount">
            <el-input-number v-model="formData.hitCount" :min="0" clearable controls-position="right" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
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
        <el-col :span="12">
          <el-form-item label="是否列表显示" prop="ifExhibition">
            <el-radio-group v-model="formData.ifExhibition">
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
          <el-form-item label="文章内容" >
            <div>
              <Ueditor v-model="formData.content"  />
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input v-model="formData.remark"/>
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
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as ArticleApi from '@/api/mall/promotion/article'
import * as ArticleCategoryApi from '@/api/mall/promotion/articleCategory'
import {defaultProps, handleTree} from "@/utils/tree";
import { cloneDeep } from 'lodash-es'

defineOptions({ name: 'PromotionArticleForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
interface FormDataType {
  id: undefined | number;
  categoryId: undefined | number;
  parentId: undefined | number;
  title: undefined | string;
  author: undefined | string;
  picUrl: undefined | string;
  pictures: string[];
  href: undefined | string;
  hitCount: undefined | number;
  files: undefined | string;
  introduction: undefined | string;
  sort: number;
  status: number;
  ifExhibition: number;
  content: undefined | string;
  publishDate: number | Date | undefined;
  tag: undefined | string;
  browseCount: undefined | string;
  qrcodeUrl: undefined;
  remark: undefined | string;
  attr01: undefined | string;
  attr02: undefined | string;
  attr03: undefined | string;
  attr04: undefined | string;
  attr05: undefined | string;
}
const formData = ref<FormDataType>({
  id: undefined,
  categoryId: undefined,
  parentId: undefined,
  title: undefined,
  author: undefined,
  picUrl: undefined,
  pictures: [],
  href: undefined,
  files: undefined,
  introduction: undefined,
  sort: 0,
  status: 0,
  hitCount: 0,
  ifExhibition: 0,
  content: undefined,
  publishDate: Date.now(),
  tag: undefined,
  browseCount: undefined,
  qrcodeUrl:undefined,
  remark: undefined,
  attr01: undefined,
  attr02: undefined,
  attr03: undefined,
  attr04: undefined,
  attr05: undefined,
})
const formRules = reactive({
  categoryId: [{ required: true, message: '分类id不能为空', trigger: 'blur' }],
  title: [{ required: true, message: '文章标题不能为空', trigger: 'blur' }],
  sort: [{ required: true, message: '排序不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'blur' }],
  ifExhibition: [{ required: true, message: '是否列表显示不能为空', trigger: 'blur' }],
  content: [{ required: true, message: '文章内容不能为空', trigger: 'blur' }],
  publishDate: [{ required: true, message: '发布时间不能为空', trigger: 'blur' }]
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
      let res = await ArticleApi.getArticle(id)
      res.pictures = res['pictures']?.map((item) => ({
        url: item
      }))
      formData.value = res
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
    let data = cloneDeep(unref(formData.value)) as ArticleApi.ArticleVO
    // // 处理轮播图列表
    const newSliderPicUrls: any[] = []
    if (data.pictures && data.pictures.length>0){
      data.pictures!.forEach((item: any) => {
        // 如果是前端选的图
        typeof item === 'object' ? newSliderPicUrls.push(item.url) : newSliderPicUrls.push(item)
      })
    }
    data.pictures = newSliderPicUrls

    if (formType.value === 'create') {
      await ArticleApi.createArticle(data)
      message.success(t('common.createSuccess'))
    } else {
      await ArticleApi.updateArticle(data)
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
    categoryId: undefined,
    parentId: undefined,
    title: undefined,
    author: undefined,
    picUrl: undefined,
    introduction: undefined,
    sort: 0,
    hitCount: 0,
    status: 0,
    ifExhibition: 0,
    content: undefined,
    publishDate: Date.now(),
    browseCount: undefined,
    qrcodeUrl: undefined,
    href: "#",
    remark: undefined,
    attr01: undefined,
    attr02: undefined,
    attr03: undefined,
    attr04: undefined,
    attr05: undefined,
  }as FormDataType;
  formRef.value?.resetFields()
}

const categoryTree = ref([]) // 树形结构
onMounted(async () => {
  const data = (await ArticleCategoryApi.getSimpleArticleCategoryList()) as ArticleCategoryApi.ArticleCategoryVO[]
  categoryTree.value = handleTree(data, 'id', 'parentId')
})

const handleCategoryChange = (categoryId) => {
  formData.value.categoryId = categoryId;
  // 从 categoryTree 中找到对应的 parentId
  const node = categoryTree.value.find((node) => node.id === categoryId)
  if (node) {
    formData.value.parentId = node.parentId
  }
};
</script>
