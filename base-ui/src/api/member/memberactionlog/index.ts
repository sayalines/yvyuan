import request from '@/config/axios'

export interface ActionLogVO {
  id: number
  statTime: Date
  userId: number
  title: string
  visitCount: number
  clickCount: number
  minTime: Date
  maxTime: Date
}

// 查询访客行为日志分页
export const getActionLogPage = async (params) => {
  return await request.get({ url: `/member/action-log/page`, params })
}

// 查询访客行为日志详情
export const getActionLog = async (id: number) => {
  return await request.get({ url: `/member/action-log/get?id=` + id })
}

// 新增访客行为日志
export const createActionLog = async (data: ActionLogVO) => {
  return await request.post({ url: `/member/action-log/create`, data })
}

// 修改访客行为日志
export const updateActionLog = async (data: ActionLogVO) => {
  return await request.put({ url: `/member/action-log/update`, data })
}

// 删除访客行为日志
export const deleteActionLog = async (id: number) => {
  return await request.delete({ url: `/member/action-log/delete?id=` + id })
}

// 导出访客行为日志 Excel
export const exportActionLog = async (params) => {
  return await request.download({ url: `/member/action-log/export-excel`, params })
}