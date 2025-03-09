import request from '@/config/axios'

export interface QuestionTopicVO {
  id: number
  questionId: number
  code: string
  name: string
  type: number
  optContent: string
  orderNo: number
}

// 查询量表题目分页
export const getQuestionTopicPage = async (params) => {
  return await request.get({ url: `/system/question-topic/page`, params })
}

// 查询量表题目列表
export const getQuestionTopicList = async (params) => {
  return await request.get({ url: `/system/question-topic/list`, params })
}


// 查询量表题目详情
export const getQuestionTopic = async (id: number) => {
  return await request.get({ url: `/system/question-topic/get?id=` + id })
}

// 新增量表题目
export const createQuestionTopic = async (data: QuestionTopicVO) => {
  return await request.post({ url: `/system/question-topic/create`, data })
}

// 修改量表题目
export const updateQuestionTopic = async (data: QuestionTopicVO) => {
  return await request.put({ url: `/system/question-topic/update`, data })
}

// 复制量表题目
export const copyQuestionTopic = async (id: number) => {
  return await request.delete({ url: `/system/question-topic/copy?id=` + id })
}

// 删除量表题目
export const deleteQuestionTopic = async (id: number) => {
  return await request.delete({ url: `/system/question-topic/delete?id=` + id })
}

// 导出量表题目 Excel
export const exportQuestionTopic = async (params) => {
  return await request.download({ url: `/system/question-topic/export-excel`, params })
}

export const importQuestionTopicMoudle = async () => {
  return await request.download({ url: `/system/question-topic/import-excel-moudle` })
}