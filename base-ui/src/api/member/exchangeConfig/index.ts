import request from '@/config/axios'

export interface ExchangeConfigVO {
  id: number
  title: string
  description: string
  bizType: number
  useCount: number
  prefix: string
  remarks: string
  bizId: number
  validStartTime: Date
  validEndTime: Date
  codeLength: number
  dayCount: number
  blackUsers: string
  whiteUsers: string
}

// 查询兑换配置分页
export const getExchangeConfigPage = async (params) => {
  return await request.get({ url: `/member/exchange-config/page`, params })
}

// 查询兑换配置详情
export const getExchangeConfig = async (id: number) => {
  return await request.get({ url: `/member/exchange-config/get?id=` + id })
}

// 新增兑换配置
export const createExchangeConfig = async (data: ExchangeConfigVO) => {
  return await request.post({ url: `/member/exchange-config/create`, data })
}

// 修改兑换配置
export const updateExchangeConfig = async (data: ExchangeConfigVO) => {
  return await request.put({ url: `/member/exchange-config/update`, data })
}

// 删除兑换配置
export const deleteExchangeConfig = async (id: number) => {
  return await request.delete({ url: `/member/exchange-config/delete?id=` + id })
}

// 导出兑换配置 Excel
export const exportExchangeConfig = async (params) => {
  return await request.download({ url: `/member/exchange-config/export-excel`, params })
}


// 生成兑换码
export const createExchange = async (params) => {
  return await request.post({ url: `/member/exchange-config/create-exchange`, params })
}