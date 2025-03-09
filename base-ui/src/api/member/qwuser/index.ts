import request from '@/config/axios'

export interface QwUserVO {
  id: number
  externalUserid: string
  name: string
  avatar: string
  type: string
  gender: string
  unionid: string
  followUserid: string
  followRemark: string
  followDescription: string
  followAddWay: number
  userId: number
  tags: string
  mobiles: string
}

// 查询企微用户分页
export const getQwUserPage = async (params) => {
  return await request.get({ url: `/member/qw-user/page`, params })
}

// 查询企微用户详情
export const getQwUser = async (id: number) => {
  return await request.get({ url: `/member/qw-user/get?id=` + id })
}

// 新增企微用户
export const createQwUser = async (data: QwUserVO) => {
  return await request.post({ url: `/member/qw-user/create`, data })
}

// 修改企微用户
export const updateQwUser = async (data: QwUserVO) => {
  return await request.put({ url: `/member/qw-user/update`, data })
}

// 删除企微用户
export const deleteQwUser = async (id: number) => {
  return await request.delete({ url: `/member/qw-user/delete?id=` + id })
}

// 导出企微用户 Excel
export const exportQwUser = async (params) => {
  return await request.download({ url: `/member/qw-user/export-excel`, params })
}