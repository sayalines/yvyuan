import request from '@/config/axios'

export interface QuestionDimensionVO {
  id: number
  questionId: number
  name: string
  description: string
  comment: string
  factors: string
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

// 查询量表维度分页
export const getQuestionDimensionPage = async (params) => {
  return await request.get({ url: `/system/question-dimension/page`, params })
}

// 查询量表维度详情
export const getQuestionDimension = async (id: number) => {
  return await request.get({ url: `/system/question-dimension/get?id=` + id })
}

// 新增量表维度
export const createQuestionDimension = async (data: QuestionDimensionVO) => {
  return await request.post({ url: `/system/question-dimension/create`, data })
}

// 修改量表维度
export const updateQuestionDimension = async (data: QuestionDimensionVO) => {
  return await request.put({ url: `/system/question-dimension/update`, data })
}

// 删除量表维度
export const deleteQuestionDimension = async (id: number) => {
  return await request.delete({ url: `/system/question-dimension/delete?id=` + id })
}

// 导出量表维度 Excel
export const exportQuestionDimension = async (params) => {
  return await request.download({ url: `/system/question-dimension/export-excel`, params })
}