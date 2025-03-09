import request from '@/config/axios'

/** 数据对照 Response VO */
export interface DataComparisonRespVO<T> {
  value: T
  reference: T
}

// 获得其他数据为分页
export const getOtherPage = async (params) => {
  return await request.get({ url: `/statistics/other/page`, params })
}

// 导出其他数据
export const exportOtherExcel = async (params) => {
  return await request.download({ url: `/statistics/other/export-excel`, params })
}
