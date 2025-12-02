<template>
  <div ref="rootRef" class="mb-4 relative flex items-center justify-between">
    <div class="flex items-center space-x-4">
      <button
        @click="openColumnSelector"
        class="px-4 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition-colors flex items-center"
      >
        <svg class="h-4 w-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 10h16M4 14h16M4 18h16" />
        </svg>
        Hiển thị cột
      </button>
      
      <button
        @click="resetColumnVisibility"
        class="px-3 py-2 text-gray-600 hover:text-gray-800 transition-colors text-sm"
      >
        Đặt lại
      </button>
    </div>
    
    <div class="text-sm text-gray-500">
      {{ visibleColumns.length }}/{{ allColumns.length }} cột đang hiển thị
    </div>
    
    <!-- Dropdown / Panel for selecting columns -->
    <transition name="cv-panel">
      <div v-if="showColumnSelector" class="absolute right-0 mt-12 w-64 bg-white border rounded shadow-lg z-50 p-3">
        <div class="flex items-center justify-between mb-3">
          <div class="flex items-center gap-3">
            <div class="text-sm font-semibold">Chọn cột</div>
            <div class="text-xs text-gray-500">{{ visibleColumns.length }}/{{ allColumns.length }} đã chọn</div>
          </div>
          <button @click="cancel" aria-label="Đóng" class="text-gray-400 hover:text-gray-600">
            <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>
          </button>
        </div>

        <div class="mb-2">
          <input v-model="filterQuery" placeholder="Tìm cột..." class="w-full px-3 py-2 border rounded text-sm focus:outline-none focus:ring-2 focus:ring-blue-200" />
        </div>

        <div class="max-h-64 overflow-y-auto">
          <div v-for="col in filteredColumns" :key="col.key" class="flex items-center justify-between p-2 hover:bg-gray-50 rounded cursor-pointer"
               @click="toggleColumnVisibility(col.key)" role="button" :aria-pressed="!!tempColumnVisibility[col.key]">
            <div class="flex items-center gap-2">
              <svg class="h-4 w-4 text-gray-400" viewBox="0 0 24 24" fill="none" stroke="currentColor"><rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/></svg>
              <div class="text-sm text-gray-800">{{ col.label }}</div>
            </div>
            <div @click.stop>
              <label class="inline-flex items-center cursor-pointer">
                <input type="checkbox" class="sr-only" v-model="tempColumnVisibility[col.key]" @click.stop />
                <span :class="['w-10 h-6 flex items-center rounded-full p-1 transition-colors', tempColumnVisibility[col.key] ? 'bg-blue-600' : 'bg-gray-200']">
                  <span :class="['bg-white w-4 h-4 rounded-full shadow transform transition-transform', tempColumnVisibility[col.key] ? 'translate-x-4' : 'translate-x-0']"></span>
                </span>
              </label>
            </div>
          </div>
        </div>

        <div class="flex items-center justify-between mt-3">
          <div class="flex items-center space-x-2">
            <button @click="selectAllColumns" class="text-xs text-gray-600 hover:underline">Chọn tất cả</button>
            <button @click="deselectAllColumns" class="text-xs text-gray-600 hover:underline">Bỏ chọn</button>
          </div>
          <div class="space-x-2">
            <button @click="cancel" class="px-3 py-1 bg-white border rounded text-sm">Hủy</button>
            <button @click="applyColumnVisibility" class="px-3 py-1 bg-blue-600 text-white rounded text-sm">Áp dụng</button>
          </div>
        </div>
    </div>
    </transition>
    </div>
</template>

<script>
import { ref, computed, onMounted, onUnmounted } from 'vue'

export default {
  name: 'ColumnVisibilityControls',
  props: {
    allColumns: {
      type: Array,
      required: true
    }
  },
  emits: ['update:visibleColumns', 'update:showColumnSelector'],
  setup(props, { emit }) {
    const rootRef = ref(null)
    const showColumnSelector = ref(false)
    const columnVisibility = ref({})
    const tempColumnVisibility = ref({})

    const visibleColumns = computed(() => {
      return props.allColumns.filter(column => columnVisibility.value[column.key] !== false)
    })

    const initializeColumnVisibility = () => {
      const defaultVisibleKeys = [
        'image',
        'brand',
        'model',
        'category.name',
        'sellingPrice',
        'basePrice',
        'stock',
        'actions'
      ];
      props.allColumns.forEach(column => {
        if (columnVisibility.value[column.key] === undefined) {
          columnVisibility.value[column.key] = defaultVisibleKeys.includes(column.key);
        }
      });
      emit('update:visibleColumns', visibleColumns.value)
    }

    const isColumnVisible = (columnKey) => {
      return tempColumnVisibility.value[columnKey] !== false
    }

    const toggleColumnVisibility = (columnKey) => {
      tempColumnVisibility.value[columnKey] = !tempColumnVisibility.value[columnKey]
    }

    const selectAllColumns = () => {
      props.allColumns.forEach(column => {
        tempColumnVisibility.value[column.key] = true
      })
    }

    const deselectAllColumns = () => {
      props.allColumns.forEach(column => {
        tempColumnVisibility.value[column.key] = false
      })
    }

    const applyColumnVisibility = () => {
      columnVisibility.value = { ...tempColumnVisibility.value }
      showColumnSelector.value = false
      emit('update:visibleColumns', visibleColumns.value)
    }

    const openColumnSelector = () => {
      tempColumnVisibility.value = { ...columnVisibility.value }
      showColumnSelector.value = true
    }

    const cancel = () => {
      // reset temp to current visibility and close
      tempColumnVisibility.value = { ...columnVisibility.value }
      showColumnSelector.value = false
    }

    const onDocumentClick = (e) => {
      const el = rootRef.value
      if (!el) return
      if (showColumnSelector.value && !el.contains(e.target)) {
        cancel()
      }
    }

    const onKeyDown = (e) => {
      if (e.key === 'Escape') cancel()
    }

    const resetColumnVisibility = () => {
      props.allColumns.forEach(column => {
        columnVisibility.value[column.key] = true
        tempColumnVisibility.value[column.key] = true
      })
      emit('update:visibleColumns', visibleColumns.value)
    }

    const filterQuery = ref('')
    const filteredColumns = computed(() => {
      const q = String(filterQuery.value || '').trim().toLowerCase()
      if (!q) return props.allColumns
      return props.allColumns.filter(c => (c.label || c.key).toLowerCase().includes(q))
    })

    onMounted(() => {
      initializeColumnVisibility()
      document.addEventListener('click', onDocumentClick)
      document.addEventListener('keydown', onKeyDown)
    })

    onUnmounted(() => {
      document.removeEventListener('click', onDocumentClick)
      document.removeEventListener('keydown', onKeyDown)
    })

    return {
      rootRef,
      showColumnSelector,
      visibleColumns,
      isColumnVisible,
      toggleColumnVisibility,
      selectAllColumns,
      deselectAllColumns,
      applyColumnVisibility,
      openColumnSelector,
      resetColumnVisibility,
      tempColumnVisibility,
      filterQuery,
      filteredColumns,
      cancel
    }
  }
}
</script>

<style scoped>
.cv-panel-enter-active, .cv-panel-leave-active { transition: transform .12s ease, opacity .12s ease }
.cv-panel-enter-from, .cv-panel-leave-to { transform: scale(.98); opacity: 0 }
.cv-panel-enter-to, .cv-panel-leave-from { transform: scale(1); opacity: 1 }
</style>