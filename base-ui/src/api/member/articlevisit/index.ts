import request from '@/config/axios'

export interface ArticleVisitVO {
  id: number
  articleId: number
  articleTitle: string
  userId: number
  userName: string
  userMobile: string
}

// 查询文章访问日志分页
export const getArticleVisitPage = async (params) => {
  return await request.get({ url: `/member/article-visit/page`, params })
}

// 查询文章访问日志详情
export const getArticleVisit = async (id: number) => {
  return await request.get({ url: `/member/article-visit/get?id=` + id })
}

// 新增文章访问日志
export const createArticleVisit = async (data: ArticleVisitVO) => {
  return await request.post({ url: `/member/article-visit/create`, data })
}

// 修改文章访问日志
export const updateArticleVisit = async (data: ArticleVisitVO) => {
  return await request.put({ url: `/member/article-visit/update`, data })
}

// 删除文章访问日志
export const deleteArticleVisit = async (id: number) => {
  return await request.delete({ url: `/member/article-visit/delete?id=` + id })
}

// 导出文章访问日志 Excel
export const exportArticleVisit = async (params) => {
  return await request.download({ url: `/member/article-visit/export-excel`, params })
}