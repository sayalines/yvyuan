<template>
  <vue-ueditor-wrap
    v-model="valueHtml"
    :config="editorConfig"
    :editor-id="`editor-${Math.random().toString(36).substr(2, 9)}`"
    :editorDependencies="['ueditor.config.js', 'ueditor.all.js']"
    :readonly="props.readonly"
    v-show="props.show"
    :style="editorStyle"
  />
</template>

<script lang="ts" setup>
import {propTypes} from "@/utils/propTypes";
import {isNumber} from "@/utils/is";

const valueHtml = ref('<p><br/></p>')

const props = defineProps({
  height: propTypes.oneOfType([Number, String]).def('450px'),
  readonly: propTypes.bool.def(false),
  modelValue: propTypes.string.def('<p><br/></p>'),
  show: propTypes.bool.def(true)
})

const emit = defineEmits(['change', 'update:modelValue'])

const editorStyle = computed(() => {
  return {
    height: isNumber(props.height) ? `${props.height}px` : props.height
  }
})

function initEditorHeight(str: string) {
  if (isNumber(str)){
    return parseInt(str)
  }else{
    return parseInt(str.replace("px",""))
  }
}

watch(
  () => props.modelValue,
  (val: string) => {
    if (val === unref(valueHtml)) return
    valueHtml.value = val
  },
  {
    immediate: true
  }
)

// 监听
watch(
  () => valueHtml.value,
  (val: string) => {
    emit('update:modelValue', val)
  }
)

const editorConfig = ref({
  zIndex: 3000, // 设置层级
  initialFrameWidth: '100%', // 设置编辑器宽度
  initialFrameHeight: initEditorHeight(`${props.height}`)-175,
  serverUrl: import.meta.env.VITE_BASE_URL+import.meta.env.VITE_API_URL+"/infra/file/upload_editor_file",
  UEDITOR_HOME_URL: '/UEditor/', // 配置 UEditor 静态资源根路径
});
</script>