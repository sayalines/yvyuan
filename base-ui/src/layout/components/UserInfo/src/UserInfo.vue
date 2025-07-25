<script lang="ts" setup>
import { ElMessageBox } from 'element-plus'

import { CACHE_KEY, useCache } from '@/hooks/web/useCache'
import { useDesign } from '@/hooks/web/useDesign'
import avatarImg from '@/assets/imgs/avatar.gif'
import { useUserStore } from '@/store/modules/user'
import { useTagsViewStore } from '@/store/modules/tagsView'

defineOptions({ name: 'UserInfo' })

const { t } = useI18n()

const { wsCache } = useCache()

const { push, replace } = useRouter()

const userStore = useUserStore()

const tagsViewStore = useTagsViewStore()

const { getPrefixCls } = useDesign()

const prefixCls = getPrefixCls('user-info')

const user = wsCache.get(CACHE_KEY.USER)

const avatar = user.user.avatar ? user.user.avatar : avatarImg

const userName = user.user.nickname ? user.user.nickname : 'Admin'

const loginOut = () => {
  ElMessageBox.confirm(t('common.loginOutMessage'), t('common.reminder'), {
    confirmButtonText: t('common.ok'),
    cancelButtonText: t('common.cancel'),
    type: 'warning'
  })
    .then(async () => {
      await userStore.loginOut()
      tagsViewStore.delAllViews()
      replace('/login?redirect=/index')
    })
    .catch(() => {})
}
const toProfile = async () => {
  push('/user/profile')
}
</script>

<template>
  <ElDropdown class="custom-hover" :class="prefixCls" trigger="click">
    <div class="flex items-center">
      <ElAvatar :src="avatar" alt="" class="w-[calc(var(--logo-height)-25px)] rounded-[50%]" />
      <span class="pl-[5px] text-14px text-[var(--top-header-text-color)] <lg:hidden">
        {{ userName }}
      </span>
    </div>
    <template #dropdown>
      <ElDropdownMenu>
        <ElDropdownItem>
          <Icon icon="ep:tools" />
          <div @click="toProfile">{{ t('common.profile') }}</div>
        </ElDropdownItem>
        <ElDropdownItem divided @click="loginOut">
          <Icon icon="ep:switch-button" />
          <div>{{ t('common.loginOut') }}</div>
        </ElDropdownItem>
      </ElDropdownMenu>
    </template>
  </ElDropdown>
</template>
