import request from '@/config/axios'


// 获得商品数据为分页
export const getProductPage = async (params) => {
  return await request.get({ url: `/statistics/product/page`, params })
}

// 导出商品数据
export const exportProductExcel = async (params) => {
  return await request.download({ url: `/statistics/product/export-excel`, params })
}


// 获得商品明细数据为分页
export const getProductDetailPage = async (params) => {
  return await request.get({ url: `/statistics/product/detail/page`, params })
}

// 导出商品明细数据
export const exportProductDetailExcel = async (params) => {
  return await request.download({ url: `/statistics/product/detail/export-excel`, params })
}