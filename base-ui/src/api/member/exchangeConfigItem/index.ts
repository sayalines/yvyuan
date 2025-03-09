import request from '@/config/axios'

export interface ExchangeConfigItemVO {
  id: number
  configId: number
  code: string
  bizType: number
  bizId: number
  totalCount: number
  useCount: number
  validStartTime: Date
  validEndTime: Date
}

// 查询兑换配置明细分页
export const getExchangeConfigItemPage = async (params) => {
  return await request.get({ url: `/member/exchange-config-item/page`, params })
}

// 查询兑换配置明细详情
export const getExchangeConfigItem = async (id: number) => {
  return await request.get({ url: `/member/exchange-config-item/get?id=` + id })
}

// 新增兑换配置明细
export const createExchangeConfigItem = async (data: ExchangeConfigItemVO) => {
  return await request.post({ url: `/member/exchange-config-item/create`, data })
}

// 修改兑换配置明细
export const updateExchangeConfigItem = async (data: ExchangeConfigItemVO) => {
  return await request.put({ url: `/member/exchange-config-item/update`, data })
}

// 删除兑换配置明细
export const deleteExchangeConfigItem = async (id: number) => {
  return await request.delete({ url: `/member/exchange-config-item/delete?id=` + id })
}

// 导出兑换配置明细 Excel
export const exportExchangeConfigItem = async (params) => {
  return await request.download({ url: `/member/exchange-config-item/export-excel`, params })
}