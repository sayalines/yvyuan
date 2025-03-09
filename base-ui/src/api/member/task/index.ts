import request from '@/config/axios'

export interface TaskConfigVO {
  id: number
  title: string
  description: string
  logo: string
  type: number
  bizType: number
  point: number
  rainbow: number
  limitType: number
  limitCount: number
  remarks: string
}

// 查询任务配置分页
export const getTaskConfigPage = async (params) => {
  return await request.get({ url: `/member/task-config/page`, params })
}

// 查询任务配置详情
export const getTaskConfig = async (id: number) => {
  return await request.get({ url: `/member/task-config/get?id=` + id })
}

// 新增任务配置
export const createTaskConfig = async (data: TaskConfigVO) => {
  return await request.post({ url: `/member/task-config/create`, data })
}

// 修改任务配置
export const updateTaskConfig = async (data: TaskConfigVO) => {
  return await request.put({ url: `/member/task-config/update`, data })
}

// 删除任务配置
export const deleteTaskConfig = async (id: number) => {
  return await request.delete({ url: `/member/task-config/delete?id=` + id })
}

// 导出任务配置 Excel
export const exportTaskConfig = async (params) => {
  return await request.download({ url: `/member/task-config/export-excel`, params })
}