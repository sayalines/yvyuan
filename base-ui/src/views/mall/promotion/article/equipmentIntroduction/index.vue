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
      <el-table-column align="center" label="ID" prop="id" />
      <el-table-column align="center" label="标题" min-width="180" prop="title" />
      <el-table-column align="center" label="封面图片" min-width="100" prop="picUrl">
        <template #default="{ row }">
          <el-image :src="row.picUrl" class="w-80px" @click="imagePreview(row.picUrl)" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="小程序码" min-width="100" prop="qrcodeUrl">
        <template #default="{ row }">
          <el-image :src="row.qrcodeUrl" class="h-30px w-30px" @click="imagePreview(row.qrcodeUrl)" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="分类" min-width="80" prop="categoryId">
        <template #default="scope">
          {{ categoryTree.find((item) => item.id === scope.row.categoryId)?.name }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" min-width="60" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="发布时间"
        prop="publishDate"
        width="120px"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="创建时间"
        prop="createTime"
        width="120px"
      />
      <el-table-column align="center" fixed="right" label="操作" width="200">
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
            v-hasPermi="['promotion:article:create']"
            link
            type="primary"
            @click="createLinkEq(scope.row.id)"
          >
            生成二维码
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
  <EquipmentIntroduction ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as ArticleApi from '@/api/mall/promotion/article'
import EquipmentIntroduction from './EquipmentIntroductionForm.vue'
import * as ArticleCategoryApi from '@/api/mall/promotion/articleCategory'
import { createImageViewer } from '@/components/ImageViewer'
import {defaultProps, handleTree} from "@/utils/tree";
import { reactive,computed } from 'vue';

defineOptions({ name: 'EquipmentIntroduction' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据

interface QueryParams {
  orderBy: number;
  pageNo: undefined | number;
  pageSize: number;
  categoryId: undefined | number; // 指定数组中元素的类型
  parentId: undefined | number;
  title: string | null;
  picUrl: undefined | string;
  status: string | undefined;
  createTime: string[];
}

const queryParams = reactive<QueryParams>({
  parentId: 6,
  orderBy: 3,
  pageNo: 1,
  pageSize: 10,
  categoryId: undefined,
  title: null,
  picUrl: undefined,
  status: undefined,
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

/** 生成二维码 */
const createLinkEq = async (id: number) => {
  try {
    // 二次确认
    await message.confirm("是否生成二维码")
    // 生成链接
    await ArticleApi.createLinkEq(id)
    message.success("操作成功")
    // 刷新列表
    await getList()
  } catch {}
}

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
const treeData = ref([])
onMounted(async () => {
  await getList()
  // 加载分类、商品列表
  const data = (await ArticleCategoryApi.getSimpleArticleCategoryList()) as ArticleCategoryApi.ArticleCategoryVO[]
  treeData.value = handleTree(data, 'id', 'parentId')
  if (Array.isArray(categoryTree.value)) {
    // 查找 id 为 6 的节点及其子树
    const subtree = treeData.value.find(node => node.id === 6) || {
      id: 6,
      children: [] // 如果没有找到 id 为 6 的节点，返回一个空的子树
    };
    // categoryTree.value 现在包含以 id 为 6 的节点及其子节点
    categoryTree.value = subtree.children; // 如果只需要子节点
  } else {
    console.error('categoryTree.value is not an array');
  }
})
</script>
