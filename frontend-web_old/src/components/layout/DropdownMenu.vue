<template>
  <div class="relative group">
    <a href="#"
      class="flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200 text-gray-700 hover:bg-gray-100"
      :class="activeRoutes.includes($route.name) ? 'bg-[#0070F4] text-white' : ''">
      <slot name="icon"></slot>
      <span>{{ title }}</span>
      <svg class="ml-1 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
      </svg>
    </a>
    
    <!-- Dropdown Menu -->
    <div class="absolute top-full left-0 mt-1 w-48 bg-white rounded-lg shadow-lg border border-gray-200 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-300 z-50">
      <div class="py-2">
        <slot name="items"></slot>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DropdownMenu',
  props: {
    title: {
      type: String,
      required: true
    },
    activeRoutes: {
      type: Array,
      default: () => []
    }
  }
}
</script>

<style scoped>
/* Improved dropdown hover */
.group {
  position: relative;
}

.group:hover > div:last-child {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.group > div:last-child {
  transform: translateY(-10px);
  transition: all 0.3s ease;
}

/* Keep dropdown visible when hovering over it */
.group > div:last-child:hover {
  opacity: 1;
  visibility: visible;
}
</style>
