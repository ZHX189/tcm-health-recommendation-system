<!--
  SVG图标组件
  @author Ti
  @since 2026-02-03
-->
<template>
  <svg
    class="tcm-icon"
    :class="[`tcm-icon-${name}`, className]"
    :style="iconStyle"
    aria-hidden="true"
  >
    <use :xlink:href="`#icon-${name}`" />
  </svg>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  // 图标名称
  name: {
    type: String,
    required: true
  },
  // 图标大小
  size: {
    type: [Number, String],
    default: 16
  },
  // 图标颜色
  color: {
    type: String,
    default: ''
  },
  // 自定义类名
  className: {
    type: String,
    default: ''
  }
})

const iconStyle = computed(() => {
  const size = typeof props.size === 'number' ? `${props.size}px` : props.size
  return {
    width: size,
    height: size,
    color: props.color || 'inherit'
  }
})
</script>

<style scoped>
.tcm-icon {
  display: inline-block;
  vertical-align: middle;
  fill: currentColor;
  overflow: hidden;
}
</style>
