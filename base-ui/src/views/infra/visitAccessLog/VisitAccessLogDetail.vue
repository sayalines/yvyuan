<template>
  <Dialog v-model="dialogVisible" :max-height="500" :scroll="true" title="详情" width="800">
    <el-descriptions :column="1" border>
      <el-descriptions-item label="日志主键" min-width="120">
        {{ detailData.id }}
      </el-descriptions-item>
      <el-descriptions-item label="链路追踪">
        {{ detailData.traceId }}
      </el-descriptions-item>
<!--      <el-descriptions-item label="应用名">-->
<!--        {{ detailData.applicationName }}-->
<!--      </el-descriptions-item>-->
      <el-descriptions-item label="用户信息">
        {{ detailData.userId }}
        <dict-tag :type="DICT_TYPE.USER_TYPE" :value="detailData.userType" />
      </el-descriptions-item>
      <el-descriptions-item label="用户 IP">
        {{ detailData.userIp }}
      </el-descriptions-item>
      <el-descriptions-item label="用户 UA">
        {{ detailData.userAgent }}
      </el-descriptions-item>
      <el-descriptions-item label="手机型号">
        {{ detailData.mobileModel }}
      </el-descriptions-item>
      <el-descriptions-item label="分组">
        {{ detailData.groupName }}
      </el-descriptions-item>
      <el-descriptions-item label="页面标题">
        {{ detailData.title }}
      </el-descriptions-item>
      <el-descriptions-item label="页面地址">
        {{ detailData.requestUrl }}
      </el-descriptions-item>
      <el-descriptions-item label="业务ID">
        {{ detailData.bizId }}
      </el-descriptions-item>
      <el-descriptions-item label="请求时间">
        {{ formatDate(detailData.beginTime) }} ~ {{ formatDate(detailData.endTime) }}
      </el-descriptions-item>
      <el-descriptions-item label="请求耗时">{{ formatPast2(detailData.duration) }}</el-descriptions-item>

    </el-descriptions>
  </Dialog>
</template>

<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import {formatDate, formatPast2} from '@/utils/formatTime'
import * as ApiAccessLog from '@/api/infra/apiAccessLog'

defineOptions({ name: 'VisitAccessLogDetail' })

const dialogVisible = ref(false) // 弹窗的是否展示
const detailLoading = ref(false) // 表单地加载中
const detailData = ref({} as ApiAccessLog.ApiAccessLogVO) // 详情数据

/** 打开弹窗 */
const open = async (data: ApiAccessLog.ApiAccessLogVO) => {
  dialogVisible.value = true
  // 设置数据
  detailLoading.value = true
  try {
    detailData.value = data
  } finally {
    detailLoading.value = false
  }
}

defineExpose({ open }) // 提供 open 方法，用于打开弹窗
</script>
