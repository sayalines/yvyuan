import request from '@/config/axios'

export interface QuestionRecordVO {
  id: number
  userId: number
  questionId: number
  answer: string
  startTime: Date
  endTime: Date
  answerTime: string
  resultTitle: string
  result: string
  resultParam: string
  resultStatus: string
}

// 查询问卷记录分页
export const getQuestionRecordPage = async (params) => {
  return await request.get({ url: `/member/question-record/page`, params })
}

// 查询问卷记录详情
export const getQuestionRecord = async (id: number) => {
  return await request.get({ url: `/member/question-record/get?id=` + id })
}

// 新增问卷记录
export const createQuestionRecord = async (data: QuestionRecordVO) => {
  return await request.post({ url: `/member/question-record/create`, data })
}

// 修改问卷记录
export const updateQuestionRecord = async (data: QuestionRecordVO) => {
  return await request.put({ url: `/member/question-record/update`, data })
}

// 删除问卷记录
export const deleteQuestionRecord = async (id: number) => {
  return await request.delete({ url: `/member/question-record/delete?id=` + id })
}

// 导出问卷记录 Excel
export const exportQuestionRecord = async (params) => {
  return await request.download({ url: `/member/question-record/export-excel`, params })
}