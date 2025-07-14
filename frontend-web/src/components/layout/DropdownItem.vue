<template>
  <router-link :to="to"
    class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 transition-colors duration-200"
    :class="$route.name === routeName ? 'bg-blue-50 text-[#0070F4]' : ''">
    <div class="flex items-center">
      <slot name="icon"></slot>
      <span>{{ title }}</span>
    </div>
  </router-link>
</template>

<script>
export default {
  name: 'DropdownItem',
  props: {
    to: {
      type: String,
      required: true
    },
    title: {
      type: String,
      required: true
    },
    routeName: {
      type: String,
      required: true
    }
  }
}
</script>
