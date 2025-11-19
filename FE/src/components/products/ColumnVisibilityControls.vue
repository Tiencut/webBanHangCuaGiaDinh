<template>
  <div class="mb-4 flex items-center justify-between">
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
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'

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

    const resetColumnVisibility = () => {
      props.allColumns.forEach(column => {
        columnVisibility.value[column.key] = true
        tempColumnVisibility.value[column.key] = true
      })
      emit('update:visibleColumns', visibleColumns.value)
    }

    onMounted(() => {
      initializeColumnVisibility()
    })

    return {
      showColumnSelector,
      visibleColumns,
      isColumnVisible,
      toggleColumnVisibility,
      selectAllColumns,
      deselectAllColumns,
      applyColumnVisibility,
      openColumnSelector,
      resetColumnVisibility,
      tempColumnVisibility
    }
  }
}
</script>