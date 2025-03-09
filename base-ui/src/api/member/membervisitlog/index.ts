import request from '@/config/axios'

export interface VisitLogVO {
  id: number
  statTime: Date
  userId: number
  userIp: string
  mobileModel: string
  minTime: Date
  maxTime: Date
}

// 查询访客信息日志分页
export const getVisitLogPage = async (params) => {
  return await request.get({ url: `/member/visit-log/page`, params })
}

// 查询访客信息日志详情
export const getVisitLog = async (id: number) => {
  return await request.get({ url: `/member/visit-log/get?id=` + id })
}

// 新增访客信息日志
export const createVisitLog = async (data: VisitLogVO) => {
  return await request.post({ url: `/member/visit-log/create`, data })
}

// 修改访客信息日志
export const updateVisitLog = async (data: VisitLogVO) => {
  return await request.put({ url: `/member/visit-log/update`, data })
}

// 删除访客信息日志
export const deleteVisitLog = async (id: number) => {
  return await request.delete({ url: `/member/visit-log/delete?id=` + id })
}

// 导出访客信息日志 Excel
export const exportVisitLog = async (params) => {
  return await request.download({ url: `/member/visit-log/export-excel`, params })
}