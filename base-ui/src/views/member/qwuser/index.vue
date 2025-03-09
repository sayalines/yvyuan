<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="120px"
    >
      <el-form-item label="外部联系人ID" prop="externalUserid">
        <el-input
          v-model="queryParams.externalUserid"
          placeholder="请输入外部联系人ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="外部联系人名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入外部联系人名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="微信unionid" prop="unionid">
        <el-input
          v-model="queryParams.unionid"
          placeholder="请输入微信unionid"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="跟进成员ID" prop="followUserid">
        <el-input
          v-model="queryParams.followUserid"
          placeholder="请输入跟进成员ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="标签" prop="tags">
        <el-input
          v-model="queryParams.tags"
          placeholder="标签"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="mobiles">
        <el-input
          v-model="queryParams.mobiles"
          placeholder="手机号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="是否关联用户" prop="isRelateUser">
        <el-select
          v-model="queryParams.isRelateUser"
          placeholder="请选择是否关联用户"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getBoolDictOptions(DICT_TYPE.INFRA_BOOLEAN_STRING)"
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
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          @click="openForm('create')"-->
<!--          v-hasPermi="['member:qw-user:create']"-->
<!--        >-->
<!--          <Icon icon="ep:plus" class="mr-5px" /> 新增-->
<!--        </el-button>-->
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['member:qw-user:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="ID" align="center" prop="id" width="100" />
      <el-table-column label="外部联系人ID" align="center" prop="externalUserid" width="150"  />
      <el-table-column label="外部联系人名称" align="center" prop="name" width="150"  />
      <el-table-column label="外部联系人头像"  prop="avatar"  width="100">
        <template #default="{ row }">
          <el-image :src="row.avatar" class="h-40px w-60px" @click="imagePreview(row.avatar)" />
        </template>
      </el-table-column>
      <el-table-column label="外部联系人的类型" align="center" prop="type"  width="100" />
      <el-table-column label="性别" align="center" prop="gender"   width="80"/>
      <el-table-column label="微信unionid" align="center" prop="unionid" width="180" />
      <el-table-column label="跟进成员ID" align="center" prop="followUserid" width="150" />
      <el-table-column label="标签" align="center" prop="tags" width="200"  />
      <el-table-column label="手机号" align="center" prop="mobiles"  width="120" />
      <el-table-column label="备注" align="center" prop="followRemark"  width="100" />
      <el-table-column label="描述" align="center" prop="followDescription"   width="100"/>
      <el-table-column label="客户来源" align="center" prop="followAddWay"  width="100" />
      <el-table-column label="用户ID" align="center" prop="userId"  width="100" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="操作" align="center" width="180px">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['member:qw-user:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['member:qw-user:delete']"
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
  <QwUserForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as QwUserApi from '@/api/member/qwuser'
import QwUserForm from './QwUserForm.vue'
import {createImageViewer} from "@/components/ImageViewer";
import {DICT_TYPE, getBoolDictOptions} from "@/utils/dict";

defineOptions({ name: 'QwUser' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
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
  isRelateUser: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中


/** 商品图预览 */
const imagePreview = (imgUrl: string) => {
  createImageViewer({
    urlList: [imgUrl]
  })
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await QwUserApi.getQwUserPage(queryParams)
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

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await QwUserApi.deleteQwUser(id)
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
    const data = await QwUserApi.exportQwUser(queryParams)
    download.excel(data, '企微用户.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>