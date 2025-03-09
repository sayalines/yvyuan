import request from '@/config/axios'

export interface ProductVisitLogVO {
  id: number
  statTime: Date
  userId: number
  spuId: number
  spuName: string
  hitCount: number
  cartCount: number
  minTime: Date
  maxTime: Date
}

// 查询商品访问日志分页
export const getProductVisitLogPage = async (params) => {
  return await request.get({ url: `/member/product-visit-log/page`, params })
}

// 查询商品访问日志详情
export const getProductVisitLog = async (id: number) => {
  return await request.get({ url: `/member/product-visit-log/get?id=` + id })
}

// 新增商品访问日志
export const createProductVisitLog = async (data: ProductVisitLogVO) => {
  return await request.post({ url: `/member/product-visit-log/create`, data })
}

// 修改商品访问日志
export const updateProductVisitLog = async (data: ProductVisitLogVO) => {
  return await request.put({ url: `/member/product-visit-log/update`, data })
}

// 删除商品访问日志
export const deleteProductVisitLog = async (id: number) => {
  return await request.delete({ url: `/member/product-visit-log/delete?id=` + id })
}

// 导出商品访问日志 Excel
export const exportProductVisitLog = async (params) => {
  return await request.download({ url: `/member/product-visit-log/export-excel`, params })
}