import { DiyComponent } from '@/components/DiyEditor/util'

/** 公告栏属性 */
export interface NoticeBarProperty {
  // 图标地址
  iconUrl: string
  // 公告内容列表
  contents: NoticeContentProperty[]
  // 背景颜色
  backgroundColor: string
  // 文字颜色
  textColor: string
}

/** 内容属性 */
export interface NoticeContentProperty {
  // 内容文字
  text: string
  // 链接地址
  url: string
}

// 定义组件
export const component = {
  id: 'NoticeBar',
  name: '公告栏',
  icon: 'ep:bell',
  property: {
    iconUrl: 'http://mall.base.iocoder.cn/static/images/xinjian.png',
    contents: [
      {
        text: '',
        url: ''
      }
    ],
    backgroundColor: '#fff',
    textColor: '#333'
  }
} as DiyComponent<NoticeBarProperty>
