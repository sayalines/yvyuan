import request from '@/config/axios'

export interface MessageRemindVO {
  id: number
  userId: number
  openid: string
  bizType: number
  bizId: number
  isRemind: boolean
  remindTime: Date
}

// 查询会员消息提醒分页
export const getMessageRemindPage = async (params) => {
  return await request.get({ url: `/member/message-remind/page`, params })
}

// 查询会员消息提醒详情
export const getMessageRemind = async (id: number) => {
  return await request.get({ url: `/member/message-remind/get?id=` + id })
}

// 新增会员消息提醒
export const createMessageRemind = async (data: MessageRemindVO) => {
  return await request.post({ url: `/member/message-remind/create`, data })
}

// 修改会员消息提醒
export const updateMessageRemind = async (data: MessageRemindVO) => {
  return await request.put({ url: `/member/message-remind/update`, data })
}

// 删除会员消息提醒
export const deleteMessageRemind = async (id: number) => {
  return await request.delete({ url: `/member/message-remind/delete?id=` + id })
}

// 导出会员消息提醒 Excel
export const exportMessageRemind = async (params) => {
  return await request.download({ url: `/member/message-remind/export-excel`, params })
}