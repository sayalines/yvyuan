import request from '@/config/axios'

export interface ApiAccessLogVO {
  id: number
  traceId: string
  userId: number
  userType: number
  applicationName: string
  requestMethod: string
  requestParams: string
  requestUrl: string
  userIp: string
  userAgent: string
  beginTime: Date
  endTIme: Date
  duration: number
  resultCode: number
  resultMsg: string
  createTime: Date
  title: string
  mobileModel: string
}

// 查询列表API 访问日志
export const getApiAccessLogPage = (params: PageParam) => {
  return request.get({ url: '/infra/api-access-log/page', params })
}

// 导出API 访问日志
export const exportApiAccessLog = (params) => {
  return request.download({ url: '/infra/api-access-log/export-excel', params })
}
