import request from '@/config/axios'

export interface CommunityVO {
  id: number
  userId: number
  brandId: number
  title: string
  content: string
  picUrl: string
  fileUrl: string
  isHot: number
  isChoice: number
  collectCount: number
  likeCount: number
  reviewCount: number
  status: number
  auditTime: Date
  reason: string
}

// 查询论坛文章分页
export const getCommunityPage = async (params) => {
  return await request.get({ url: `/community/community/page`, params })
}

// 查询论坛文章详情
export const getCommunity = async (id: number) => {
  return await request.get({ url: `/community/community/get?id=` + id })
}

// 新增论坛文章
export const createCommunity = async (data: CommunityVO) => {
  return await request.post({ url: `/community/community/create`, data })
}

// 修改论坛文章
export const updateCommunity = async (data: CommunityVO) => {
  return await request.put({ url: `/community/community/update`, data })
}

// 审核论坛文章
export const auditCommunity = async (params) => {
  return await request.put({ url: `/community/community/audit`, params })
}


// 删除论坛文章
export const deleteCommunity = async (id: number) => {
  return await request.delete({ url: `/community/community/delete?id=` + id })
}

// 导出论坛文章 Excel
export const exportCommunity = async (params) => {
  return await request.download({ url: `/community/community/export-excel`, params })
}