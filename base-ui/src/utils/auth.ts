import { useCache } from '@/hooks/web/useCache'
import { TokenType } from '@/api/login/types'
import { decrypt, encrypt } from '@/utils/jsencrypt'
import  WebPageCacheManager  from '@/hooks/web/useCache'

const { wsCache } = useCache()
const cacheManager = new WebPageCacheManager()

const AccessTokenKey = 'ACCESS_TOKEN'
const RefreshTokenKey = 'REFRESH_TOKEN'

// 获取token
export const getAccessToken = () => {
  // 此处与TokenKey相同，此写法解决初始化时Cookies中不存在TokenKey报错
  return wsCache.get(AccessTokenKey) ? wsCache.get(AccessTokenKey) : wsCache.get('ACCESS_TOKEN')
}

// 刷新token
export const getRefreshToken = () => {
  return wsCache.get(RefreshTokenKey)
}

// 设置token
export const setToken = (token: TokenType) => {
  wsCache.set(RefreshTokenKey, token.refreshToken)
  wsCache.set(AccessTokenKey, token.accessToken)
}

// 删除token
export const removeToken = () => {
  wsCache.delete(AccessTokenKey)
  wsCache.delete(RefreshTokenKey)
}

/** 格式化token（jwt格式） */
export const formatToken = (token: string): string => {
  return 'Bearer ' + token
}
// ========== 账号相关 ==========



export type LoginFormType = {
  tenantName: string
  username: string
  password: string
  rememberMe: boolean
}

// 设置登录表单
export const setLoginForm = (loginForm: LoginFormType): void => {
  loginForm.password = encrypt(loginForm.password) as string;
  cacheManager.setItem('LOGINFORM', loginForm, 2592000); // 设置为30天过期
  console.log("setform:",loginForm)
};

// 获取登录表单
export const getLoginForm = (): LoginFormType | null => {
  const loginForm: LoginFormType | null = cacheManager.getItem('LOGINFORM');
  console.log("getform:",loginForm)
  if (loginForm && loginForm.password) {
    loginForm.password = decrypt(loginForm.password); // 解密密码字段
  }
  return loginForm;
};

export const removeLoginForm = (): void => {
  cacheManager.removeItem('LOGINFORM');
};
// ========== 租户相关 ==========

const TenantIdKey = 'TENANT_ID'
const TenantNameKey = 'TENANT_NAME'

export const getTenantName = () => {
  return wsCache.get(TenantNameKey)
}

export const setTenantName = (username: string) => {
  wsCache.set(TenantNameKey, username, { exp: 30 * 24 * 60 * 60 })
}

export const removeTenantName = () => {
  wsCache.delete(TenantNameKey)
}

export const getTenantId = () => {
  return wsCache.get(TenantIdKey)
}

export const setTenantId = (username: string) => {
  wsCache.set(TenantIdKey, username)
}

export const removeTenantId = () => {
  wsCache.delete(TenantIdKey)
}
