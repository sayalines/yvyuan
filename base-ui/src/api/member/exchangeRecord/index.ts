import request from '@/config/axios'

export interface ExchangeRecordVO {
  id: number
  userId: number
  nickname: string
  configId: number
  code: string
  bizType: number
  isUse: boolean
  useTime: Date
  bizId: number
  activeTime: Date
  validStartTime: Date
  validEndTime: Date
}

// 查询会员兑换记录分页
export const getExchangeRecordPage = async (params) => {
  return await request.get({ url: `/member/exchange-record/page`, params })
}

// 查询会员兑换记录详情
export const getExchangeRecord = async (id: number) => {
  return await request.get({ url: `/member/exchange-record/get?id=` + id })
}

// 新增会员兑换记录
export const createExchangeRecord = async (data: ExchangeRecordVO) => {
  return await request.post({ url: `/member/exchange-record/create`, data })
}

// 修改会员兑换记录
export const updateExchangeRecord = async (data: ExchangeRecordVO) => {
  return await request.put({ url: `/member/exchange-record/update`, data })
}

// 删除会员兑换记录
export const deleteExchangeRecord = async (id: number) => {
  return await request.delete({ url: `/member/exchange-record/delete?id=` + id })
}

// 导出会员兑换记录 Excel
export const exportExchangeRecord = async (params) => {
  return await request.download({ url: `/member/exchange-record/export-excel`, params })
}