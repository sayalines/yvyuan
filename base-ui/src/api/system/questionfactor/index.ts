import request from '@/config/axios'

export interface QuestionFactorVO {
  id: number
  questionId: number
  name: string
  description: string
  suggest: string
  topics: number[]
  scoreType: number
  constantValue: number
  constantScore: number
  constantScoreType: number
  optContent: string
  keywordContent: string
  orderNo: number
  isTotal: number
  isShow: number
}

// 查询量表因子分页
export const getQuestionFactorPage = async (params) => {
  return await request.get({ url: `/system/question-factor/page`, params })
}

// 查询量表因子列表
export const getQuestionFactorList = async (params) => {
  return await request.get({ url: `/system/question-factor/list`, params })
}

// 查询量表因子详情
export const getQuestionFactor = async (id: number) => {
  return await request.get({ url: `/system/question-factor/get?id=` + id })
}

// 新增量表因子
export const createQuestionFactor = async (data: QuestionFactorVO) => {
  return await request.post({ url: `/system/question-factor/create`, data })
}

// 修改量表因子
export const updateQuestionFactor = async (data: QuestionFactorVO) => {
  return await request.put({ url: `/system/question-factor/update`, data })
}

// 删除量表因子
export const deleteQuestionFactor = async (id: number) => {
  return await request.delete({ url: `/system/question-factor/delete?id=` + id })
}

// 导出量表因子 Excel
export const exportQuestionFactor = async (params) => {
  return await request.download({ url: `/system/question-factor/export-excel`, params })
}