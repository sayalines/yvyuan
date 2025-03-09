<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="90%">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item label="主题" prop="name">
            <el-input v-model="formData.name" placeholder="请输入主题" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="简介" prop="description">
            <el-input v-model="formData.description" type="textarea" rows="4" placeholder="请输入简介" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="推荐" prop="suggest">
            <el-input v-model="formData.suggest" type="textarea" rows="4" placeholder="请输入推荐" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
            <el-form-item label="选择题目" prop="topics">
              <el-select v-model="formData.topics" multiple placeholder="请选择" style="width: 100%">
                <el-option
                  v-for="item in topicList"
                  :key="item.id"
                  :label="item.calcName"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分值计算类型" prop="scoreType">
            <el-select v-model="formData.scoreType" class="w-1/1" placeholder="请选择分值计算类型" clearable>
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.QUESTION_FACTOR_SCORE_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="常量值" prop="constantValue">
            <el-input v-model="formData.constantValue" placeholder="请输入常量值" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="常模类型" prop="constantScoreType">
            <el-select v-model="formData.constantScoreType" class="w-1/1" placeholder="请选择常模类型" clearable>
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.QUESTION_FACTOR_CONSTANT_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="常模值" prop="constantScore">
            <el-input v-model="formData.constantScore" placeholder="请输入常模值" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否为总分" prop="isTotal">
            <el-select v-model="formData.isTotal" class="w-1/1" placeholder="请选择" clearable>
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
          <el-form-item label="是否显示" prop="isShow">
            <el-select v-model="formData.isShow" class="w-1/1" placeholder="请选择" clearable>
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_YES_NO)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="配置参数" prop="optContent">
            <el-table
              :data="opts"
              border
              class="tabNumWidth"
              max-height="500"
              size="small"
            >
              <el-table-column align="center" label="名称" width="150px">
                <template #default="{ row }">
                  <el-input v-model="row.name" />
                </template>
              </el-table-column>
              <el-table-column align="center" label="起始值类型" min-width="80">
                <template #default="{ row }">
                  <el-select v-model="row.scoreBeginType" placeholder="请选择">
                    <el-option
                      v-for="dict in getIntDictOptions(DICT_TYPE.QUESTION_FACTOR_BEGIN_TYPE)"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column align="center" label="起始值" min-width="80">
                <template #default="{ row }">
                  <el-input-number :precision="1" v-model="row.scoreBegin" style="width: 100%" />
                </template>
              </el-table-column>
              <el-table-column align="center" label="终止值类型" min-width="80">
                <template #default="{ row }">
                  <el-select v-model="row.scoreEndType" placeholder="请选择">
                    <el-option
                      v-for="dict in getIntDictOptions(DICT_TYPE.QUESTION_FACTOR_END_TYPE)"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column align="center" label="终止值" min-width="80">
                <template #default="{ row }">
                  <el-input-number :precision="1" v-model="row.scoreEnd" style="width: 100%"  />
                </template>
              </el-table-column>
              <el-table-column align="center" label="简介" min-width="120">
                <template #default="{ row }">
                  <el-input v-model="row.description" type="textarea" rows="4"  class="w-100%" />
                </template>
              </el-table-column>
              <el-table-column align="center" label="推荐/建议" min-width="120">
                <template #default="{ row }">
                  <el-input v-model="row.suggest" type="textarea" rows="4"  class="w-100%" />
                </template>
              </el-table-column>
              <el-table-column align="center" fixed="right" label="操作" width="60px">
                <template #default="{ row }">
                  <el-button link size="small" type="primary" @click="deleteOpt(row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-form-item>
              <el-button class="mb-10px mr-15px" style="margin-top: 10px" @click="addOption">添加</el-button>
            </el-form-item>
          </el-form-item>
        </el-col>
<!--        <el-col :span="24">-->
<!--          <el-form-item label="关键字参数" prop="keywordContent">-->
<!--            <el-table-->
<!--              :data="keywordContents"-->
<!--              border-->
<!--              class="tabNumWidth"-->
<!--              max-height="500"-->
<!--              size="small"-->
<!--            >-->
<!--              <el-table-column align="center" label="关键字" width="150px">-->
<!--                <template #default="{ row }">-->
<!--                  <el-input v-model="row.name" />-->
<!--                </template>-->
<!--              </el-table-column>-->
<!--              <el-table-column align="center" label="开始位置" width="150px">-->
<!--                <template #default="{ row }">-->
<!--                  <el-input v-model="row.startPosition" />-->
<!--                </template>-->
<!--              </el-table-column>-->
<!--              <el-table-column align="center" label="结束位置" width="150px">-->
<!--                <template #default="{ row }">-->
<!--                  <el-input v-model="row.endPosition" />-->
<!--                </template>-->
<!--              </el-table-column>-->
<!--              <el-table-column align="center" label="链接地址">-->
<!--                <template #default="{ row }">-->
<!--                  <el-input v-model="row.url" />-->
<!--                </template>-->
<!--              </el-table-column>-->
<!--              <el-table-column align="center" fixed="right" label="操作" width="60px">-->
<!--                <template #default="{ row }">-->
<!--                  <el-button link size="small" type="primary" @click="deleteKeywordContent(row)">删除</el-button>-->
<!--                </template>-->
<!--              </el-table-column>-->
<!--            </el-table>-->
<!--            <el-form-item>-->
<!--              <el-button class="mb-10px mr-15px" style="margin-top: 10px" @click="addKeywordContent">添加</el-button>-->
<!--            </el-form-item>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
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
import * as QuestionFactorApi from '@/api/system/questionfactor'
import {DICT_TYPE, getIntDictOptions} from "@/utils/dict";
import * as QuestionTopicApi from '@/api/system/questiontopic'
import {UploadImg} from "@/components/UploadFile";

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const opts = ref([]) // 选项的数据
const keywordContents = ref([]) // 选项的数据
const formData = ref({
  id: undefined,
  questionId: undefined,
  name: undefined,
  description: undefined,
  suggest: undefined,
  topics: undefined,
  scoreType: undefined,
  constantValue: undefined,
  constantScore: undefined,
  constantScoreType: undefined,
  optContent: undefined,
  keywordContent: undefined,
  isTotal: undefined,
  isShow: undefined,
  orderNo: undefined
})
const formRules = reactive({
  name: [{ required: true, message: '主题不能为空', trigger: 'blur' }],
  description: [{ required: true, message: '简介不能为空', trigger: 'blur' }],
  scoreType: [{ required: true, message: '分值计算类型不能为空', trigger: 'change' }],
  constantScore: [{ required: true, message: '常模值不能为空', trigger: 'blur' }],
  orderNo: [{ required: true, message: '排序号不能为空', trigger: 'blur' }],
  constantScoreType: [{ required: true, message: '常模类型不能为空', trigger: 'change' }]
})
const formRef = ref() // 表单 Ref
const topicList = ref([]) // 题目列表

/** 打开弹窗 */
const open = async (type: string, id?: number, questionId: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  formData.value.questionId = questionId
  topicList.value = await QuestionTopicApi.getQuestionTopicList({questionId:questionId})
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await QuestionFactorApi.getQuestionFactor(id)
      let array = []
      if (formData.value.optContent){
        array = JSON.parse(formData.value.optContent)
      }
      opts.value = array

      array = []
      if (formData.value.keywordContent){
        array = JSON.parse(formData.value.keywordContent)
      }
      keywordContents.value = array
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
    const array: any[] = []
    if (opts.value && opts.value.length>0){
      opts.value.forEach((item: any) => {
        array.push(item)
      })
    }
    formData.value.optContent = JSON.stringify(array)

    const keyArray: any[] = []
    if (keywordContents.value && keywordContents.value.length>0){
      keywordContents.value.forEach((item: any) => {
        keyArray.push(item)
      })
    }
    formData.value.keywordContent = JSON.stringify(keyArray)
    const data = formData.value as unknown as QuestionFactorApi.QuestionFactorVO
    if (formType.value === 'create') {
      await QuestionFactorApi.createQuestionFactor(data)
      message.success(t('common.createSuccess'))
    } else {
      await QuestionFactorApi.updateQuestionFactor(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

const addOption = () => {
  let item={
    name:'',
    scoreBeginType:1,
    scoreBegin:undefined,
    scoreEndType:1,
    scoreEnd:undefined,
    description:'',
    suggest:''
  }
  opts.value.push(item)
}

const deleteOpt = async(row) => {
  const index = opts.value.indexOf(row)
  opts.value.splice(index, 1)
}


const addKeywordContent = () => {
  let item={
    name:'',
    startPosition:'',
    endPosition:'',
    url:''
  }
  keywordContents.value.push(item)
}

const deleteKeywordContent = async(row) => {
  const index = keywordContents.value.indexOf(row)
  keywordContents.value.splice(index, 1)
}


/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    questionId: undefined,
    name: undefined,
    description: undefined,
    suggest: undefined,
    topics: undefined,
    scoreType: 0,
    constantValue: undefined,
    constantScore: undefined,
    constantScoreType: 1,
    isTotal: 0,
    isShow: 1,
    optContent: undefined,
    keywordContent: undefined,
    orderNo: undefined
  }
  formRef.value?.resetFields()
}


</script>