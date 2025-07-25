<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="60%">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="选择分类" prop="parentId">
        <el-tree-select
          v-model="formData.parentId"
          :data="articleCategoryTree"
          :props="defaultProps"
          check-strictly
          default-expand-all
          placeholder="请选择分类"
        />
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入名称" />
      </el-form-item>
      <el-form-item label="上传图片" prop="picUrl">
        <UploadImg v-model="formData.picUrl" height="80px"/>
      </el-form-item>
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
      <el-form-item label="排序" prop="sort">
        <el-input-number v-model="formData.sort" :min="0" clearable controls-position="right" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark"  />
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
import * as ArticleCategoryApi from '@/api/mall/promotion/articleCategory'
import { defaultProps, handleTree } from '@/utils/tree'

defineOptions({ name: 'PromotionArticleCategoryForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  parentId: undefined,
  name: undefined,
  picUrl: undefined,
  status: 0,
  sort: 1,
  remark: undefined
})
const formRules = reactive({
  parentId: [{ required: true, message: '上级分类不能为空', trigger: 'blur' }],
  name: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'blur' }],
  sort: [{ required: true, message: '排序号不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const articleCategoryTree = ref() // 树形结构

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
      formData.value = await ArticleCategoryApi.getArticleCategory(id)
    } finally {
      formLoading.value = false
    }
  }
  await getArticleCategoryTree()
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
    const data = formData.value as unknown as ArticleCategoryApi.ArticleCategoryVO
    if (formType.value === 'create') {
      await ArticleCategoryApi.createArticleCategory(data)
      message.success(t('common.createSuccess'))
    } else {
      await ArticleCategoryApi.updateArticleCategory(data)
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
    parentId: undefined,
    name: undefined,
    picUrl: undefined,
    status: 0,
    sort: 1,
    remark: undefined
  }
  formRef.value?.resetFields()
}

/** 获得文章分类树 */
const getArticleCategoryTree = async () => {
  articleCategoryTree.value = []
  const data = await ArticleCategoryApi.getArticleCategoryList()
  const root: Tree = { id: 0, name: '文章分类', children: [] }
  root.children = handleTree(data, 'id', 'parentId')
  articleCategoryTree.value.push(root)
}
</script>