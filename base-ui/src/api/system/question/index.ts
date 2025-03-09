import request from '@/config/axios'

export interface QuestionVO {
  id: number
  name: string
  picUrl: string
  description: string
  content: string
  score: number
  limitTime: number
  memberNum: number
  hitCount: string
  comment: string
  isAnswer: number
  isComment: number
  isMemberinfo: number
  isGraphic: number
  topicCount: number
  startTime: Date
  endTime: Date
  resurveyTime: string
  type: number
  status: number
  factorMaxAxis: number
  dimensionMaxAxis: number
  totalMaxAxis: number
  displayName: string
  orderNo: number
  isStat: number
}

// 查询量表管理分页
export const getQuestionPage = async (params) => {
  return await request.get({ url: `/system/question/page`, params })
}

// 查询量表管理列表
export const getQuestionList = async (params) => {
  return await request.get({ url: `/system/question/list`, params })
}

// 查询量表管理详情
export const getQuestion = async (id: number) => {
  return await request.get({ url: `/system/question/get?id=` + id })
}

// 新增量表管理
export const createQuestion = async (data: QuestionVO) => {
  return await request.post({ url: `/system/question/create`, data })
}

// 修改量表管理
export const updateQuestion = async (data: QuestionVO) => {
  return await request.put({ url: `/system/question/update`, data })
}

// 删除量表管理
export const deleteQuestion = async (id: number) => {
  return await request.delete({ url: `/system/question/delete?id=` + id })
}

// 导出量表管理 Excel
export const exportQuestion = async (params) => {
  return await request.download({ url: `/system/question/export-excel`, params })
}