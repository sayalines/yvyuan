<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" style="width:70%">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item label="手机号" prop="mobile">
            <el-input v-model="formData.mobile" placeholder="请输入手机号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用户昵称" prop="nickname">
            <el-input v-model="formData.nickname" placeholder="请输入用户昵称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="formData.email" placeholder="请输入邮箱" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="来源" prop="origins">
            <el-input v-model="formData.origins" placeholder="请输入来源" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="头像" prop="avatar">
            <UploadImg v-model="formData.avatar" :limit="1" :is-show-tip="false" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="formData.name" placeholder="请输入姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="出生日期" prop="birthday">
            <el-date-picker
              v-model="formData.birthday"
              type="date"
              value-format="x"
              placeholder="选择出生日期"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用户性别" prop="sex">
            <el-radio-group v-model="formData.sex">
              <el-radio
                v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_USER_SEX)"
                :key="dict.value"
                :label="dict.value"
              >
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
<!--        <el-col :span="12">-->
<!--          <el-form-item label="用户类型" prop="userType">-->
<!--            <el-select-->
<!--              v-model="formData.userType"-->
<!--              placeholder="请选择用户类型"-->
<!--              clearable-->
<!--              class="!w-240px"-->
<!--            >-->
<!--              <el-option-->
<!--                v-for="dict in getIntDictOptions(DICT_TYPE.MEMBER_USER_TYPE)"-->
<!--                :key="dict.value"-->
<!--                :label="dict.label"-->
<!--                :value="dict.value"-->
<!--              />-->
<!--            </el-select>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="12">-->
<!--          <el-form-item label="证件类型" prop="idCodeType">-->
<!--            <el-select-->
<!--              v-model="formData.idCodeType"-->
<!--              placeholder="请选择证件类型"-->
<!--              clearable-->
<!--              class="!w-240px"-->
<!--            >-->
<!--              <el-option-->
<!--                v-for="dict in getIntDictOptions(DICT_TYPE.MEMBER_CARD_TYPE)"-->
<!--                :key="dict.value"-->
<!--                :label="dict.label"-->
<!--                :value="dict.value"-->
<!--              />-->
<!--            </el-select>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="12">-->
<!--          <el-form-item label="证件号码" prop="idCode">-->
<!--            <el-input v-model="formData.idCode" placeholder="请输入证件号码" />-->
<!--          </el-form-item>-->
<!--        </el-col>-->
        <el-col :span="12">
          <el-form-item label="所在地" prop="areaId">
            <el-tree-select
              style="width:100%"
              v-model="formData.areaName"
              :data="areaList"
              :props="defaultProps"
              @node-click='areaNodeClick'
              :render-after-expand="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用户标签" prop="tagIds">
            <MemberTagSelect v-model="formData.tagIds" show-add />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用户分组" prop="groupId">
            <MemberGroupSelect v-model="formData.groupId" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="推荐码" prop="referralCode">
            <el-input v-model="formData.referralCode" placeholder="请输入推荐码" />
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
        <el-col :span="24">
          <el-form-item label="会员备注" prop="mark">
            <el-input type="textarea" v-model="formData.mark" placeholder="请输入会员备注" />
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
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as UserApi from '@/api/member/user'
import * as AreaApi from '@/api/system/area'
import {defaultProps, handleTree} from "@/utils/tree";
import * as DeptApi from "@/api/system/dept";
import MemberTagSelect from '@/views/member/tag/components/MemberTagSelect.vue'
import MemberGroupSelect from '@/views/member/group/components/MemberGroupSelect.vue'

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  mobile: undefined,
  email: undefined,
  origins: undefined,
  password: undefined,
  status: undefined,
  nickname: undefined,
  avatar: undefined,
  isRegister: undefined,
  isTakeNewGiftPack: undefined,
  name: undefined,
  userType: undefined,
  sex: undefined,
  areaId: undefined,
  areaName: undefined,
  birthday: undefined,
  mark: undefined,
  idCodeType: undefined,
  idCode: undefined,
  age: undefined,
  tagIds: [],
  deptId: undefined,
  groupId: undefined,
  referralCode: undefined
})
const formRules = reactive({
  // mobile: [{ required: true, message: '手机号不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const areaList = ref([]) // 地区列表
const deptList = ref<Tree[]>([]) // 树形结构

async function areaNodeClick(node,treeNode){
  console.log(node)
  formData.value.areaId = node.id
  formData.value.areaName = await AreaApi.getAreaName(node.id)
}

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
      formData.value = await UserApi.getUser(id)
      // if (formData.value.areaId){
      //   formData.value.areaName = await AreaApi.getAreaName(formData.value.areaId)
      // }
    } finally {
      formLoading.value = false
    }
  }
  // 获得地区列表
  areaList.value = await AreaApi.getAreaTree()
  // 加载部门树
  deptList.value = handleTree(await DeptApi.getSimpleDeptList())
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
    const data = formData.value as unknown as UserApi.UserVO
    if (formType.value === 'create') {
      // 说明：目前暂时没有新增操作。如果自己业务需要，可以进行扩展
      // await UserApi.createUser(data)
      message.success(t('common.createSuccess'))
    } else {
      await UserApi.updateUser(data)
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
    mobile: undefined,
    email: undefined,
    origins: undefined,
    password: undefined,
    status: undefined,
    nickname: undefined,
    avatar: undefined,
    isRegister: undefined,
    isTakeNewGiftPack: undefined,
    name: undefined,
    sex: undefined,
    areaId: undefined,
    areaName: undefined,
    birthday: undefined,
    mark: undefined,
    tagIds: [],
    groupId: undefined,
    referralCode: undefined
  }
  formRef.value?.resetFields()
}
</script>
