/**
 * 配置浏览器本地存储的方式，可直接存储对象数组。
 */

import WebStorageCache from 'web-storage-cache'

type CacheType = 'localStorage' | 'sessionStorage'

export const CACHE_KEY = {
  IS_DARK: 'isDark',
  USER: 'user',
  LANG: 'lang',
  THEME: 'theme',
  LAYOUT: 'layout',
  ROLE_ROUTERS: 'roleRouters',
  DICT_CACHE: 'dictCache'
}

export const useCache = (type: CacheType = 'localStorage') => {
  const wsCache: WebStorageCache = new WebStorageCache({
    storage: type
  })

  return {
    wsCache
  }
}

class WebPageCacheManager {
  private wsCache: WebStorageCache;

  constructor(type: CacheType = 'localStorage') {
    this.wsCache = new WebStorageCache({
      storage: type,
    });
  }

  // 设置缓存项，包括过期时间
  public setItem(key: string, value: any, expireInSeconds: number = 30*24*60*60): void {
    this.wsCache.set(key, value, { exp: expireInSeconds });
  }

  // 获取缓存项
  public getItem(key: string): any {
    return this.wsCache.get(key);
  }

  // 删除缓存项
  public removeItem(key: string): void {
    this.wsCache.delete(key);
  }

  // 清除所有过期缓存项
  public clearExpiredItems(): void {
    this.wsCache.deleteAllExpires();
  }

  // 清空所有缓存项
  public clearAllItems(): void {
    this.wsCache.clear();
  }
}

export default WebPageCacheManager;
