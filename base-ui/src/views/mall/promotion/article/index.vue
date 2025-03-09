<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      ref="queryFormRef"
      :inline="true"
      :model="queryParams"
      class="-mb-15px"
      label-width="80px"
    >
      <el-form-item label="文章分类" prop="categoryId">
        <el-cascader
          v-model="queryParams.categoryId"
          :options="categoryTree"
          :props="defaultProps"
          class="w-1/1"
          ref="cascaderRef"
          clearable
          placeholder="请选择文章分类"
        />
      </el-form-item>
      <el-form-item label="文章标题" prop="title">
        <el-input
          v-model="queryParams.title"
          class="!w-240px"
          clearable
          placeholder="请输入文章标题"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" class="!w-240px" clearable placeholder="请选择状态">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <div></div>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          搜索
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          重置
        </el-button>
        <el-button
          v-hasPermi="['promotion:article:create']"
          plain
          type="primary"
          @click="openForm('create')"
        >
          <Icon class="mr-5px" icon="ep:plus" />
          新增
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column align="center" label="ID" prop="id" width="200px" />
      <el-table-column align="center" label="标题" width="180px"  prop="title" />
      <el-table-column align="center" label="封面图片" width="120px"  prop="picUrl">
        <template #default="{ row }">
          <el-image :src="row.picUrl" class="w-80px" @click="imagePreview(row.picUrl)" />
        </template>
      </el-table-column>
<!--      <el-table-column align="center" label="小程序码" width="100px"  prop="qrcodeUrl">-->
<!--        <template #default="{ row }">-->
<!--          <el-image :src="row.qrcodeUrl" class="h-30px w-30px" @click="imagePreview(row.qrcodeUrl)" />-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column align="center" label="分类" width="150px" prop="categoryId">
        <template #default="scope">
          {{ findCategoryName(categoryTree, scope.row.categoryId) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" width="80px" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="发布时间"
        prop="publishDate"
        width="180px"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="创建时间"
        prop="createTime"
        width="180px"
      />
      <el-table-column align="center" fixed="right" label="操作" width="200px">
        <template #default="scope">
          <el-button
            v-hasPermi="['promotion:article:update']"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            编辑
          </el-button>
          <el-button
            v-hasPermi="['promotion:article:delete']"
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      v-model:limit="queryParams.pageSize"
      v-model:page="queryParams.pageNo"
      :total="total"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <ArticleForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as AppointActiveApi from '@/api/system/appointactive'
import { dateFormatter } from '@/utils/formatTime'
import * as ArticleApi from '@/api/mall/promotion/article'
import ArticleForm from './ArticleForm.vue'
import * as ArticleCategoryApi from '@/api/mall/promotion/articleCategory'
import { createImageViewer } from '@/components/ImageViewer'
import {defaultProps, handleTree} from "@/utils/tree";
import { reactive,computed } from 'vue';

defineOptions({ name: 'PromotionArticle' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据

interface QueryParams {
  pageNo: number;
  pageSize: number;
  categoryId: number | undefined; // 指定数组中元素的类型
  orderBy: number;
  title: string | null;
  status: string | undefined;
  picUrl: undefined | string;
  qrcodeUrl: undefined | string;
  createTime: string[];
}

const queryParams = reactive<QueryParams>({
  pageNo: 1,
  pageSize: 10,
  categoryId: undefined,
  orderBy: 3,
  title: null,
  status: undefined,
  picUrl: undefined,
  qrcodeUrl:undefined,
  createTime: []
})

const queryFormRef = ref() // 搜索的表单

/** 文章封面预览 */
const imagePreview = (imgUrl: string) => {
  createImageViewer({
    urlList: [imgUrl]
  })
}
/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ArticleApi.getArticlePage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

// /** 生成二维码 */
// const createLink = async (id: number) => {
//   try {
//     // 二次确认
//     await message.confirm("是否生成二维码")
//     // 生成链接
//     await ArticleApi.createLink(id)
//     message.success("操作成功")
//     // 刷新列表
//     await getList()
//   } catch {}
// }

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ArticleApi.deleteArticle(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

const categoryTree = ref([]) // 树形结构
onMounted(async () => {
  await getList()
  // 加载分类、商品列表
  const data = (await ArticleCategoryApi.getSimpleArticleCategoryList()) as ArticleCategoryApi.ArticleCategoryVO[]
  categoryTree.value = handleTree(data, 'id', 'parentId')
})

function findCategoryName(categories, categoryId) {
  // 递归函数查找分类名称
  function traverse(nodes) {
    for (const node of nodes) {
      if (node.id === categoryId) {
        return node.name; // 找到匹配的分类，返回名称
      }
      if (node.children) {
        const name = traverse(node.children);
        if (name) return name; // 如果在子节点中找到了，返回名称
      }
    }
    return null;
  }

  // 调用递归函数，开始查找
  return traverse(categories) || '';
}
</script>
