<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="品牌" prop="brandId">
        <el-select v-model="queryParams.brandId" placeholder="请选择" clearable>
          <el-option
            v-for="item in brandList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入标题"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="是否热门" prop="isHot">
        <el-select
          v-model="queryParams.isHot"
          placeholder="请选择是否热门"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions('system_yes_no')"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否精选" prop="isChoice">
        <el-select
          v-model="queryParams.isChoice"
          placeholder="请选择是否精选"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions('system_yes_no')"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择状态"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions('ext_community_status')"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['community:community:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="ID" align="center" prop="id" width="200px" />
      <el-table-column label="用户ID" align="center" prop="userId" width="200px"  />
      <el-table-column label="用户昵称" align="center" prop="nickname" width="150px"   />
      <el-table-column label="手机号" align="center" prop="mobile" width="150px"   />
      <el-table-column label="品牌" align="center" prop="brand" width="150px"   />
      <el-table-column label="标题" align="center" prop="title" width="150px"  />
      <el-table-column label="封面图" align="center" width="120px" prop="picUrl">
        <template #default="scope">
          <img v-if="scope.row.picUrl" :src="scope.row.picUrl" @click="imagePreview(row.picUrl)" alt="移动端分类图" class="h-36px" />
        </template>
      </el-table-column>
      <el-table-column label="是否热门" align="center" prop="isHot" width="120px">
        <template #default="scope">
          <dict-tag type="system_yes_no" :value="scope.row.isHot" />
        </template>
      </el-table-column>
      <el-table-column label="是否精选" align="center" prop="isChoice" width="120px">
        <template #default="scope">
          <dict-tag type="system_yes_no" :value="scope.row.isChoice" />
        </template>
      </el-table-column>
      <el-table-column label="收藏数" align="center" prop="collectCount" width="120px" />
      <el-table-column label="点赞数" align="center" prop="likeCount" width="120px" />
      <el-table-column label="评论数" align="center" prop="reviewCount" width="120px" />
      <el-table-column label="状态" align="center" prop="status" width="120px">
        <template #default="scope">
          <dict-tag type="ext_community_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        label="审核时间"
        align="center"
        prop="auditTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="操作" align="center" width="180px" fixed="right">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('view', scope.row.id)"
          >
            详细
          </el-button>
          <el-button
            link
            type="primary"
            v-if="scope.row.status == 0"
            @click="auditForm(scope.row.id)"
            v-hasPermi="['community:community:update']"
          >
            审核
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['community:community:delete']"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <CommunityForm ref="formRef" @success="getList" />
  <!--  审核表单弹窗-->
  <Dialog title="审核" v-model="dialogVisible">
    <el-form
      ref="ruleFormRef"
      :model="auditData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="状态" prop="status">
          <el-radio label="1" v-model="auditData.status" >审核通过</el-radio>
          <el-radio label="2" v-model="auditData.status">拒绝</el-radio>
      </el-form-item>
      <el-form-item label="拒绝原因" prop="reason" v-if="auditData.status ==2">
        <el-input v-model="auditData.reason" type="textarea" placeholder="请输入拒绝原因"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { getIntDictOptions, getBoolDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as CommunityApi from '@/api/member/community'
import CommunityForm from './CommunityForm.vue'
import { createImageViewer } from '@/components/ImageViewer'
import * as ProductBrandApi from "@/api/mall/product/brand";

defineOptions({ name: 'Community' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const brandList = ref([]) // 精简商品品牌列表

const loading = ref(true) // 列表的加载中
const list = ref([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  userId: undefined,
  brandId: undefined,
  title: undefined,
  isHot: undefined,
  isChoice: undefined,
  status: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const dialogVisible = ref(false) // 审核弹窗的是否展示

const auditData = ref({
  id: undefined,
  status: '1',
  reason: undefined,
})

const formRules = reactive({
  status: [{required: true, message: '审核状态不能为空', trigger: 'blur'}]
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await CommunityApi.getCommunityPage(queryParams)
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

/** 图片预览 */
const imagePreview = (imgUrl: string) => {
  createImageViewer({
    urlList: [imgUrl]
  })
}

/** 详细操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/**打开审核操作弹窗*/
const formLoading = ref(false)
const ruleFormRef=ref()
const emit = defineEmits(['success']) // 定义 success 事件，用于审核操作成功后的回调
const auditForm = (id) => {
  auditData.value.id = id
  dialogVisible.value = true
}

/** 审核操作 */
const submitForm = async () => {
  // 校验表单
  await ruleFormRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = auditData.value
    //打印数据
    await CommunityApi.auditCommunity(data)
    message.success('操作成功')
    dialogVisible.value = false
    emit('success')
    getList()
  } finally {
    formLoading.value = false
  }
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await CommunityApi.deleteCommunity(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await CommunityApi.exportCommunity(queryParams)
    download.excel(data, '论坛文章.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(async () => {
  getList()
  // 获取商品品牌列表
  brandList.value = await ProductBrandApi.getSimpleBrandList()
})
</script>