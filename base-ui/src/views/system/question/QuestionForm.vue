<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="80%">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="180px"
      v-loading="formLoading"
    >
      <el-row>
        <el-col :span="24">
          <el-form-item label="量表主题" prop="name">
            <el-input v-model="formData.name" placeholder="请输入量表主题" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="显示名称" prop="displayName">
            <el-input v-model="formData.displayName" placeholder="请输入显示名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="类型" prop="type">
            <el-select v-model="formData.type" class="w-1/1" placeholder="请选择" clearable>
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.QUESTION_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="封面图" prop="picUrl">
            <UploadImg v-model="formData.picUrl" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="简介" prop="description">
            <el-input v-model="formData.description" type="textarea" rows="8" placeholder="请输入简介" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="默认评分" prop="score">
            <el-input-number v-model="formData.score" :precision="1" placeholder="请输入默认评分" style="width:200px" />
            <el-text  size="small" type="info">
              &nbsp;评分1-5.0范围内
            </el-text>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="限制时间" prop="limitTime">
            <el-input-number v-model="formData.limitTime" placeholder="请输入限制时间" style="width:200px"/>&nbsp;分钟
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="题目数" prop="topicCount">
            <el-input-number v-model="formData.topicCount" placeholder="请输入题目数" style="width:200px"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="使用人数" prop="memberNum">
            <el-input-number v-model="formData.memberNum" placeholder="请输入使用人数" style="width:200px"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="重测时间" prop="resurveyTime">
            <el-input v-model="formData.resurveyTime" placeholder="请输入重测时间"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="热度" prop="hitCount">
            <el-input v-model="formData.hitCount" placeholder="请输入热度"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开始时间" prop="startTime">
            <el-date-picker
              v-model="formData.startTime"
              type="date"
              value-format="x"
              placeholder="选择开始时间"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束时间" prop="endTime">
            <el-date-picker
              v-model="formData.endTime"
              type="date"
              value-format="x"
              placeholder="选择结束时间"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否允许查看结果" prop="isAnswer">
            <el-select v-model="formData.isAnswer" class="w-1/1" placeholder="请选择" clearable>
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.QUESTION_RESULT_STATUS)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否允许查看指导建议" prop="isComment">
            <el-select v-model="formData.isComment" class="w-1/1" placeholder="请选择" clearable>
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.QUESTION_RESULT_STATUS)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否需要完善会员资料" prop="isMemberinfo">
            <el-select v-model="formData.isMemberinfo" class="w-1/1" placeholder="请选择">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_YES_NO)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否需要图形报表" prop="isGraphic">
            <el-select v-model="formData.isGraphic" class="w-1/1" placeholder="请选择">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_YES_NO)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否统计" prop="isStat">
            <el-select v-model="formData.isStat" class="w-1/1" placeholder="请选择">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_YES_NO)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="因子Y轴最大值" prop="factorMaxAxis">
            <el-input-number v-model="formData.factorMaxAxis" placeholder="请输入因子Y轴最大值" style="width:200px"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="维度Y轴最大值" prop="dimensionMaxAxis">
            <el-input-number v-model="formData.dimensionMaxAxis" placeholder="请输入维度Y轴最大值" style="width:200px"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="总分Y轴最大值" prop="totalMaxAxis">
            <el-input-number v-model="formData.totalMaxAxis" placeholder="请输入总分Y轴最大值" style="width:200px"/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="指导语" prop="comment">
            <el-input v-model="formData.comment" type="textarea" rows="4" placeholder="请输入指导语" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="报告说明" prop="content">
            <Ueditor v-model="formData.content" height="350px"/>
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
          <el-form-item label="排序号" prop="orderNo">
            <el-input-number v-model="formData.orderNo" placeholder="请输入排序号" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import * as QuestionApi from '@/api/system/question'
import {DICT_TYPE, getIntDictOptions} from "@/utils/dict";

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: undefined,
  picUrl: undefined,
  description: undefined,
  comment: undefined,
  isAnswer: undefined,
  isComment: undefined,
  isMemberinfo: undefined,
  isGraphic: undefined,
  hitCount: undefined,
  memberNum: undefined,
  topicCount: undefined,
  limitTime: undefined,
  score: undefined,
  startTime: undefined,
  endTime: undefined,
  resurveyTime: undefined,
  type: undefined,
  status: undefined,
  factorMaxAxis: undefined,
  dimensionMaxAxis: undefined,
  totalMaxAxis: undefined,
  displayName: undefined,
  content: undefined,
  isStat: undefined,
  orderNo: undefined
})
const formRules = reactive({
  name: [{ required: true, message: '量表主题不能为空', trigger: 'blur' }],
  displayName: [{ required: true, message: '显示名称不能为空', trigger: 'blur' }],
  score: [{ required: true, message: '默认评分不能为空', trigger: 'blur' }],
  limitTime: [{ required: true, message: '限制时间不能为空', trigger: 'blur' }],
  isAnswer: [{ required: true, message: '是否允许查看结果不能为空', trigger: 'blur' }],
  isComment: [{ required: true, message: '是否允许查看指导建议不能为空', trigger: 'blur' }],
  isMemberinfo: [{ required: true, message: '是否需要完善会员资料不能为空', trigger: 'blur' }],
  isGraphic: [{ required: true, message: '是否需要图形报表不能为空', trigger: 'blur' }],
  isStat: [{ required: true, message: '是否统计不能为空', trigger: 'blur' }],
  topicCount: [{ required: true, message: '题目数不能为空', trigger: 'blur' }],
  orderNo: [{ required: true, message: '排序号不能为空', trigger: 'blur' }],
  type: [{ required: true, message: '类型不能为空', trigger: 'blur' }],
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
      formData.value = await QuestionApi.getQuestion(id)
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
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as QuestionApi.QuestionVO
    if (formType.value === 'create') {
      await QuestionApi.createQuestion(data)
      message.success(t('common.createSuccess'))
    } else {
      await QuestionApi.updateQuestion(data)
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
    name: undefined,
    picUrl: undefined,
    description: undefined,
    comment: undefined,
    isAnswer: undefined,
    isComment: undefined,
    topicCount: 0,
    isMemberinfo: 0,
    isGraphic: 0,
    isStat: 0,
    hitCount: undefined,
    memberNum: undefined,
    limitTime: 0,
    score: 5.0,
    content: undefined,
    startTime: undefined,
    endTime: undefined,
    resurveyTime: undefined,
    type: undefined,
    status: 0,
    factorMaxAxis: undefined,
    dimensionMaxAxis: undefined,
    totalMaxAxis: undefined,
    displayName: undefined,
    content: undefined,
    orderNo: undefined
  }
  formRef.value?.resetFields()
}
</script>