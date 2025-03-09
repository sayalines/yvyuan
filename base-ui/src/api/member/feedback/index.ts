import request from '@/config/axios'

export interface QuestionVO {
  userName: string
  mobile: string
  content: string
  ifSolve: number
}

// 查询问题反馈分页
export const getQuestionPage = async (params) => {
  return await request.get({ url: `/member/question/page`, params })
}

// 查询问题反馈详情
export const getQuestion = async (id: number) => {
  return await request.get({ url: `/member/question/get?id=` + id })
}

// 新增问题反馈
export const createQuestion = async (data: QuestionVO) => {
  return await request.post({ url: `/member/question/create`, data })
}

// 修改问题反馈
export const updateQuestion = async (data: QuestionVO) => {
  return await request.put({ url: `/member/question/update`, data })
}

// 删除问题反馈
export const deleteQuestion = async (id: number) => {
  return await request.delete({ url: `/member/question/delete?id=` + id })
}

//改变解决状态
export const auditQuestion = async (id: number, result: string) => {
  return await request.put({ url: `/member/question/audit/${id}`, data: { id,result } });
};

// 导出问题反馈 Excel
export const exportQuestion = async (params) => {
  return await request.download({ url: `/member/question/export-excel`, params })
}