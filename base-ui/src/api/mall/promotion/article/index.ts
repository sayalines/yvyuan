import request from '@/config/axios'

export interface ArticleVO {
  id: number
  categoryId: number
  parentId:number
  title: string
  author: string
  picUrl: string
  introduction: string
  browseCount: string
  sort: number
  status: number
  // spuId: number
  ifExhibition: number
  content: string
  publishDate: Date
  tag: string
  pictures?: string[]
  files: string
  href: string
  attr01: string
  attr02: string
  hitCount: number
  remark: string
  attr03: string
  attr04: string
  attr05: string
}

// 查询文章管理列表
export const getArticlePage = async (params: any) => {
  return await request.get({ url: `/promotion/article/page`, params })
}

// 查询文章管理详情
export const getArticle = async (id: number) => {
  return await request.get({ url: `/promotion/article/get?id=` + id })
}

// 新增文章管理
export const createArticle = async (data: ArticleVO) => {
  return await request.post({ url: `/promotion/article/create`, data })
}

// 修改文章管理
export const updateArticle = async (data: ArticleVO) => {
  return await request.put({ url: `/promotion/article/update`, data })
}

// 删除文章管理
export const deleteArticle = async (id: number) => {
  return await request.delete({ url: `/promotion/article/delete?id=` + id })
}

// 生成链接（设备介绍）
export const createLinkEq = async (id: number) => {
  return await request.put({ url: `/promotion/article/createLinkEq?id=` + id })
}

// 生成链接（拓展资料）
export const createLinkEx = async (id: number) => {
  return await request.put({url: `/promotion/article/createLinkEx?id=` + id})
}

// 生成链接（资讯）
export const createLinkIn = async (id: number) => {
  return await request.put({url: `/promotion/article/createLinkIn?id=` + id})
}

// 生成链接（科普知识）
export const createLinkPo = async (id: number) => {
  return await request.put({url: `/promotion/article/createLinkPo?id=` + id})
}