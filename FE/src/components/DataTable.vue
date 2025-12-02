<template>
  <div class="data-table-container">
    <!-- Header với Search và Filter -->
    <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between mb-6 space-y-4 lg:space-y-0">
      <div class="flex flex-col sm:flex-row sm:items-center space-y-2 sm:space-y-0 sm:space-x-4">
        <!-- Search -->
        <div class="relative">
          <input
            v-model="searchQuery"
            type="text"
            placeholder="Tìm kiếm..."
            class="pl-10 pr-4 py-2 w-full sm:w-64 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
          />
          <svg class="absolute left-3 top-2.5 h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
        </div>

        <!-- Category Multi-Select Filter -->
        <div v-if="categories?.length" class="relative category-dropdown">
          <button
            @click="toggleCategoryDropdown"
            class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 transition-colors flex items-center justify-between min-w-[200px]"
            :class="selectedCategories.length > 0 ? 'bg-blue-50 border-blue-300' : ''"
          >
            <span class="text-left">
              {{ selectedCategories.length === 0 ? 'Tất cả danh mục' : 
                 selectedCategories.length === 1 ? getCategoryName(selectedCategories[0]) :
                 `${selectedCategories.length} danh mục đã chọn` }}
            </span>
            <div class="flex items-center ml-2">
              <button
                v-if="selectedCategories.length > 0"
                @click.stop="clearCategoryFilter"
                class="text-gray-400 hover:text-gray-600 mr-1"
                title="Xóa filter danh mục"
              >
                <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
              <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
              </svg>
            </div>
          </button>
          
          <!-- Dropdown -->
          <div v-if="showCategoryDropdown" class="absolute top-full left-0 mt-1 w-full bg-white border border-gray-300 rounded-lg shadow-lg z-50 max-h-60 overflow-y-auto">
            <div class="p-2">
              <!-- Select All -->
              <label class="flex items-center p-2 hover:bg-gray-100 rounded cursor-pointer">
                <input
                  type="checkbox"
                  :checked="isAllCategoriesSelected"
                  @change="toggleAllCategories"
                  class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded mr-2"
                />
                <span class="text-sm font-medium">Tất cả danh mục</span>
              </label>
              
              <hr class="my-2">
              
              <!-- Individual Categories -->
              <label
                v-for="category in categories"
                :key="category.id"
                class="flex items-center p-2 hover:bg-gray-100 rounded cursor-pointer"
              >
                <input
                  type="checkbox"
                  :value="category.id"
                  v-model="selectedCategories"
                  class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded mr-2"
                />
                <span class="text-sm">{{ category.name }}</span>
              </label>
            </div>
          </div>
        </div>

        <!-- Status Filter -->
        <select 
          v-if="statusOptions?.length" 
          v-model="selectedStatus" 
          class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 transition-colors"
        >
          <option value="">Tất cả trạng thái</option>
          <option v-for="status in statusOptions" :key="status.value" :value="status.value">
            {{ status.label }}
          </option>
        </select>

        <!-- Date Range Filter -->
        <div class="flex items-center space-x-2">
          <span class="text-sm text-gray-600">Từ ngày:</span>
          <input
            v-model="dateFrom"
            type="date"
            placeholder="Từ ngày"
            class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 transition-colors"
          />
          <span class="text-gray-500">đến</span>
          <input
            v-model="dateTo"
            type="date"
            placeholder="Đến ngày"
            class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 transition-colors"
          />
          <button
            v-if="dateFrom || dateTo"
            @click="clearDateFilter"
            class="px-2 py-2 text-gray-500 hover:text-gray-700 transition-colors"
            title="Xóa filter ngày"
          >
            <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
      </div>

      <!-- Actions -->
      <div class="flex items-center space-x-2">
        <!-- Excel group: single button with hover/focus dropdown for Import/Export -->
        <div v-if="showImport || showExport" class="relative group">
          <button
            class="px-3 py-1 bg-gray-100 rounded-md text-sm flex items-center gap-2 hover:bg-gray-200 focus:outline-none"
            aria-haspopup="true"
            aria-expanded="false"
            title="Excel"
          >
            <svg class="h-4 w-4 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 5v14M5 12h14" />
            </svg>
            <span class="text-sm font-medium">Excel</span>
          </button>

          <div
            class="absolute right-0 mt-2 w-40 bg-white border rounded shadow-lg opacity-0 group-hover:opacity-100 group-focus-within:opacity-100 transform scale-95 group-hover:scale-100 transition-all z-50"
            role="menu"
          >
            <button v-if="showImport" @click.prevent="triggerImport" class="w-full text-left px-4 py-2 hover:bg-gray-50 text-sm" role="menuitem">Import</button>
            <button v-if="showExport" @click.prevent="exportData" class="w-full text-left px-4 py-2 hover:bg-gray-50 text-sm" role="menuitem">Export</button>
          </div>

          <input ref="importInput" type="file" accept=".csv,.xls,.xlsx" class="hidden" @change="handleImportFile" />
        </div>

        <button
          v-if="selectedRows.length > 0"
          @click="handleBulkAction"
          class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors flex items-center"
        >
          <svg class="h-4 w-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
          </svg>
          Thao tác ({{ selectedRows.length }})
        </button>
        
        <!-- Export moved into Excel dropdown above -->
        
        <button
          @click="$emit('create')"
          class="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors flex items-center"
        >
          <svg class="h-4 w-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
          </svg>
          Thêm mới
        </button>
      </div>
    </div>

    <!-- Table -->
    <div class="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
      <div class="overflow-x-auto">
        <table class="w-full">
          <!-- Header -->
          <thead class="bg-gray-50 border-b border-gray-200">
            <tr>
              <!-- Select All Checkbox -->
              <th v-if="selectable" class="px-6 py-3 text-left">
                <input
                  type="checkbox"
                  :checked="isAllSelected"
                  @change="toggleSelectAll"
                  class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                />
              </th>

              <!-- Dynamic Headers -->
              <th
                v-for="column in columns"
                :key="column.key"
                :class="[
                  'px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider',
                  column.sortable ? 'cursor-pointer hover:bg-gray-100 select-none' : '',
                  column.width ? `w-${column.width}` : ''
                ]"
                @click="column.sortable ? handleSort(column.key) : null"
              >
                <div class="flex items-center justify-between">
                  <span>{{ column.label }}</span>
                  <div v-if="column.sortable" class="flex flex-col ml-1">
                    <svg
                      :class="[
                        'h-3 w-3 transition-colors',
                        sortField === column.key && sortOrder === 'asc' ? 'text-blue-600' : 'text-gray-400'
                      ]"
                      fill="currentColor"
                      viewBox="0 0 20 20"
                    >
                      <path d="M14.707 12.707a1 1 0 01-1.414 0L10 9.414l-3.293 3.293a1 1 0 01-1.414-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 010 1.414z" />
                    </svg>
                    <svg
                      :class="[
                        'h-3 w-3 -mt-1 transition-colors',
                        sortField === column.key && sortOrder === 'desc' ? 'text-blue-600' : 'text-gray-400'
                      ]"
                      fill="currentColor"
                      viewBox="0 0 20 20"
                    >
                      <path d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" />
                    </svg>
                  </div>
                </div>
              </th>

              <!-- Actions Column -->
              <th v-if="showActions" class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                Thao tác
              </th>
            </tr>
            <!-- Filter row -->
            <tr>
              <th v-if="selectable"></th>
              <th v-for="column in columns" :key="column.key">
                <template v-if="['image','brand','model'].includes(column.key)">
                  <input
                    type="text"
                    :placeholder="'Tìm kiếm ' + (column.label || '')"
                    class="px-2 py-1 border border-gray-300 rounded w-full text-xs"
                    :value="columnFilters[column.key] || ''"
                    @input="$emit('update:columnFilters', { ...columnFilters, [column.key]: $event.target.value })"
                  />
                </template>
              </th>
              <th v-if="showActions"></th>
            </tr>
          </thead>

          <!-- Body -->
          <tbody class="bg-white divide-y divide-gray-200">
            <!-- Loading State -->
            <tr v-if="loading">
              <td :colspan="totalColumns" class="px-6 py-12 text-center">
                <div class="flex items-center justify-center">
                  <svg class="animate-spin h-8 w-8 text-blue-600" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  <span class="ml-2 text-gray-500">Đang tải...</span>
                </div>
              </td>
            </tr>

            <!-- Empty State -->
            <tr v-else-if="paginatedData.length === 0">
              <td :colspan="totalColumns" class="px-6 py-12 text-center">
                <div class="text-gray-500">
                  <svg class="mx-auto h-16 w-16 text-gray-300 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                  </svg>
                  <p class="text-lg font-medium">{{ emptyText || 'Không có dữ liệu' }}</p>
                  <p class="text-sm text-gray-400 mt-1">{{ emptySubText || 'Chưa có dữ liệu để hiển thị' }}</p>
                </div>
              </td>
            </tr>

            <!-- Data Rows -->
            <tr
              v-for="(item, index) in paginatedData"
              :key="item.id || index"
              :class="[
                'hover:bg-gray-50 transition-colors cursor-pointer',
                selectedRows.includes(item.id) ? 'bg-blue-50 border-blue-200' : ''
              ]"
              @click="handleRowClick(item)"
            >
              <!-- Select Row Checkbox -->
              <td v-if="selectable" class="px-6 py-4" @click.stop>
                <input
                  type="checkbox"
                  :checked="selectedRows.includes(item.id)"
                  @change="toggleSelectRow(item.id)"
                  class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                />
              </td>

              <!-- Dynamic Columns -->
              <td
                v-for="column in columns"
                :key="column.key"
                :class="[
                  'px-6 py-4',
                  column.align === 'center' ? 'text-center' : column.align === 'right' ? 'text-right' : 'text-left',
                  column.nowrap !== false ? 'whitespace-nowrap' : ''
                ]"
              >
                <slot :name="`cell-${column.key}`" :item="item" :value="getColumnValue(item, column.key)" :index="index">
                  <!-- Custom Formatter -->
                  <span v-if="column.formatter">
                    {{ column.formatter(getColumnValue(item, column.key)) }}
                  </span>
                  <!-- Currency -->
                  <span v-else-if="column.type === 'currency'" class="font-medium">
                    {{ formatCurrency(getColumnValue(item, column.key)) }}
                  </span>
                  <!-- Date -->
                  <span v-else-if="column.type === 'date'">
                    {{ formatDate(getColumnValue(item, column.key)) }}
                  </span>
                  <!-- DateTime -->
                  <span v-else-if="column.type === 'datetime'">
                    {{ formatDateTime(getColumnValue(item, column.key)) }}
                  </span>
                  <!-- Badge -->
                  <span v-else-if="column.type === 'badge'" :class="getBadgeClass(getColumnValue(item, column.key), column.badgeMap)">
                    {{ getBadgeText(getColumnValue(item, column.key), column.badgeMap) }}
                  </span>
                  <!-- Number -->
                  <span v-else-if="column.type === 'number'" class="font-mono">
                    {{ formatNumber(getColumnValue(item, column.key)) }}
                  </span>
                  <!-- Boolean -->
                  <span v-else-if="column.type === 'boolean'">
                    <span :class="getColumnValue(item, column.key) ? 'text-green-600' : 'text-red-600'">
                      {{ getColumnValue(item, column.key) ? 'Có' : 'Không' }}
                    </span>
                  </span>
                  <!-- Default -->
                  <span v-else class="text-gray-900">
                    {{ getColumnValue(item, column.key) || '-' }}
                  </span>
                </slot>
              </td>

              <!-- Actions Column -->
              <td v-if="showActions" class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium" @click.stop>
                <slot name="actions" :item="item" :index="index">
                  <div class="flex items-center justify-end space-x-2">
                    <button
                      @click="$emit('edit', item)"
                      class="text-blue-600 hover:text-blue-900 transition-colors"
                      title="Chỉnh sửa"
                    >
                      <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                      </svg>
                    </button>
                    <button
                      @click="$emit('delete', item)"
                      class="text-red-600 hover:text-red-900 transition-colors"
                      title="Xóa"
                    >
                      <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                      </svg>
                    </button>
                  </div>
                </slot>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <div v-if="showPagination && filteredData.length > 0" class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
        <!-- Mobile Pagination -->
        <div class="flex-1 flex justify-between sm:hidden">
          <button
            @click="prevPage"
            :disabled="currentPage === 1"
            class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
          >
            Trước
          </button>
          <span class="text-sm text-gray-700 flex items-center">
            {{ currentPage }} / {{ totalPages }}
          </span>
          <button
            @click="nextPage"
            :disabled="currentPage === totalPages"
            class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
          >
            Sau
          </button>
        </div>
        
        <!-- Desktop Pagination -->
        <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
          <div class="flex items-center space-x-4">
            <p class="text-sm text-gray-700">
              Hiển thị
              <span class="font-medium">{{ startIndex }}</span>
              đến
              <span class="font-medium">{{ endIndex }}</span>
              trong tổng số
              <span class="font-medium">{{ filteredData.length }}</span>
              kết quả
            </p>
            
            <!-- Page Size Selector -->
            <select 
              v-model="pageSize" 
              class="border border-gray-300 rounded px-2 py-1 text-sm focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option v-for="size in pageSizeOptions" :key="size" :value="size">
                {{ size }} dòng
              </option>
            </select>
          </div>

          <!-- Page Navigation -->
          <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px">
            <button
              @click="prevPage"
              :disabled="currentPage === 1"
              class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
            >
              <svg class="h-5 w-5" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
              </svg>
            </button>

            <button
              v-for="page in visiblePages"
              :key="page"
              @click="goToPage(page)"
              :class="[
                'relative inline-flex items-center px-4 py-2 border text-sm font-medium transition-colors',
                page === currentPage
                  ? 'z-10 bg-blue-50 border-blue-500 text-blue-600'
                  : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50'
              ]"
            >
              {{ page }}
            </button>

            <button
              @click="nextPage"
              :disabled="currentPage === totalPages"
              class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
            >
              <svg class="h-5 w-5" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd" />
              </svg>
            </button>
          </nav>
        </div>
      </div>
    </div>
  
  <ImportPreviewModal
    v-model="showPreviewModal"
    :expectedHeaders="expectedHeaders"
    :parsedHeaders="parsedHeaders"
    :previewRows="previewRows"
    :maxRows="10"
    @confirm="confirmImport"
    @cancel="cancelImport"
  />
  </div>
</template>

<script setup>
import { ref, computed, watch, nextTick, onMounted } from 'vue'
import ImportPreviewModal from './excel/ImportPreviewModal.vue'

// Props
const props = defineProps({
  data: {
    type: Array,
    default: () => []
  },
  columns: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  },
  categories: {
    type: Array,
    default: () => []
  },
  statusOptions: {
    type: Array,
    default: () => []
  },
  selectable: {
    type: Boolean,
    default: true
  },
  showActions: {
    type: Boolean,
    default: true
  },
  showPagination: {
    type: Boolean,
    default: true
  },
  showExport: {
    type: Boolean,
    default: false
  },
  showImport: {
    type: Boolean,
    default: false
  },
  pageSizeOptions: {
    type: Array,
    default: () => [10, 20, 50, 100]
  },
  defaultPageSize: {
    type: Number,
    default: 20
  },
  emptyText: {
    type: String,
    default: 'Không có dữ liệu'
  },
  emptySubText: {
    type: String,
    default: 'Chưa có dữ liệu để hiển thị'
  },
  columnFilters: {
    type: Object,
    default: () => ({})
  }
})

// Emits
const emit = defineEmits([
  'create', 'edit', 'delete', 'bulkAction', 'export', 'import', 'import-file', 'rowClick', 'update:columnFilters'
])

// Reactive state
const searchQuery = ref('')
const selectedCategory = ref('') // Keep for backward compatibility
const selectedCategories = ref([]) // New multi-select
const selectedStatus = ref('')
const dateFrom = ref('')
const dateTo = ref('')
const selectedRows = ref([])
const sortField = ref('')
const sortOrder = ref('asc')
const currentPage = ref(1)
const pageSize = ref(props.defaultPageSize)
const showCategoryDropdown = ref(false)

// Computed properties
const totalColumns = computed(() => {
  let count = props.columns.length
  if (props.selectable) count++
  if (props.showActions) count++
  return count
})

const isAllCategoriesSelected = computed(() => {
  return props.categories && props.categories.length > 0 && 
         selectedCategories.value.length === props.categories.length
})

const filteredData = computed(() => {
  let filtered = [...props.data]

  // Search filter
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(item => 
      props.columns.some(column => {
        const value = getColumnValue(item, column.key)
        return String(value).toLowerCase().includes(query)
      })
    )
  }

  // Category filter (multi-select)
  if (selectedCategories.value.length > 0) {
    filtered = filtered.filter(item => 
      selectedCategories.value.includes(item.categoryId) || 
      selectedCategories.value.includes(item.category?.id)
    )
  } else if (selectedCategory.value) {
    // Backward compatibility
    filtered = filtered.filter(item => 
      item.categoryId === selectedCategory.value || 
      item.category?.id === selectedCategory.value
    )
  }

  // Status filter
  if (selectedStatus.value) {
    filtered = filtered.filter(item => 
      item.status === selectedStatus.value
    )
  }

  // Date range filter
  if (dateFrom.value || dateTo.value) {
    filtered = filtered.filter(item => {
      const itemDate = item.createdAt || item.updatedAt
      if (!itemDate) return false
      
      const itemDateObj = new Date(itemDate)
      const fromDate = dateFrom.value ? new Date(dateFrom.value) : null
      const toDate = dateTo.value ? new Date(dateTo.value) : null
      
      if (fromDate && toDate) {
        return itemDateObj >= fromDate && itemDateObj <= toDate
      } else if (fromDate) {
        return itemDateObj >= fromDate
      } else if (toDate) {
        return itemDateObj <= toDate
      }
      
      return true
    })
  }

  // Sort
  if (sortField.value) {
    filtered.sort((a, b) => {
      let aVal = getColumnValue(a, sortField.value)
      let bVal = getColumnValue(b, sortField.value)
      
      // Handle null/undefined values
      if (aVal == null) aVal = ''
      if (bVal == null) bVal = ''
      
      // Convert to comparable format
      if (typeof aVal === 'string') aVal = aVal.toLowerCase()
      if (typeof bVal === 'string') bVal = bVal.toLowerCase()
      
      if (sortOrder.value === 'asc') {
        return aVal > bVal ? 1 : aVal < bVal ? -1 : 0
      } else {
        return aVal < bVal ? 1 : aVal > bVal ? -1 : 0
      }
    })
  }

  return filtered
})

const totalPages = computed(() => {
  if (!props.showPagination) return 1
  return Math.ceil(filteredData.value.length / pageSize.value)
})

const paginatedData = computed(() => {
  if (!props.showPagination) return filteredData.value
  
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredData.value.slice(start, end)
})

const startIndex = computed(() => {
  return Math.min((currentPage.value - 1) * pageSize.value + 1, filteredData.value.length)
})

const endIndex = computed(() => {
  return Math.min(currentPage.value * pageSize.value, filteredData.value.length)
})

const visiblePages = computed(() => {
  const pages = []
  const maxVisible = 7
  let start = Math.max(1, currentPage.value - Math.floor(maxVisible / 2))
  let end = Math.min(totalPages.value, start + maxVisible - 1)
  
  if (end - start + 1 < maxVisible) {
    start = Math.max(1, end - maxVisible + 1)
  }
  
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  
  return pages
})

const isAllSelected = computed(() => {
  return paginatedData.value.length > 0 && 
         paginatedData.value.every(item => selectedRows.value.includes(item.id))
})

// Methods
const handleSort = (field) => {
  if (sortField.value === field) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
  } else {
    sortField.value = field
    sortOrder.value = 'asc'
  }
}

const toggleSelectAll = () => {
  if (isAllSelected.value) {
    selectedRows.value = selectedRows.value.filter(id => 
      !paginatedData.value.some(item => item.id === id)
    )
  } else {
    const newSelections = paginatedData.value.map(item => item.id)
    selectedRows.value = [...new Set([...selectedRows.value, ...newSelections])]
  }
}

const toggleSelectRow = (id) => {
  const index = selectedRows.value.indexOf(id)
  if (index > -1) {
    selectedRows.value.splice(index, 1)
  } else {
    selectedRows.value.push(id)
  }
}

const getColumnValue = (item, key) => {
  return key.split('.').reduce((obj, k) => obj?.[k], item)
}

const formatCurrency = (value) => {
  if (!value && value !== 0) return '0đ'
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(value)
}

const formatNumber = (value) => {
  if (!value && value !== 0) return '0'
  return new Intl.NumberFormat('vi-VN').format(value)
}

const formatDate = (value) => {
  if (!value) return ''
  return new Date(value).toLocaleDateString('vi-VN')
}

const formatDateTime = (value) => {
  if (!value) return ''
  return new Date(value).toLocaleString('vi-VN')
}

const getBadgeClass = (value, badgeMap) => {
  if (badgeMap && badgeMap[value]) {
    return `px-2 py-1 text-xs rounded-full ${badgeMap[value].class}`
  }
  
  // Default badge classes
  const defaultClasses = {
    'active': 'bg-green-100 text-green-800',
    'inactive': 'bg-red-100 text-red-800',
    'pending': 'bg-yellow-100 text-yellow-800',
    'completed': 'bg-blue-100 text-blue-800',
    'cancelled': 'bg-gray-100 text-gray-800'
  }
  
  return `px-2 py-1 text-xs rounded-full ${defaultClasses[value] || 'bg-gray-100 text-gray-800'}`
}

const getBadgeText = (value, badgeMap) => {
  if (badgeMap && badgeMap[value]) {
    return badgeMap[value].text || value
  }
  return value
}

const handleBulkAction = () => {
  emit('bulkAction', selectedRows.value)
}

const handleRowClick = (item) => {
  emit('rowClick', item)
}

const exportData = () => {
  // default behaviour: export CSV and notify parent
  try {
    exportCSV(filteredData.value)
  } catch (e) {
    console.error('Export failed', e)
  }
  emit('export', filteredData.value)
}

// --- CSV Export / Import helpers ---
const importInput = ref(null)

// Import preview modal state
const showPreviewModal = ref(false)
const parsedHeaders = ref([])
const previewRows = ref([])
const expectedHeaders = computed(() => props.columns.map(c => c.label || c.key))

const triggerImport = () => {
  importInput.value?.click()
}

const handleImportFile = async (event) => {
  const file = event.target.files && event.target.files[0]
  if (!file) return
  const name = file.name.toLowerCase()
  // If CSV, parse locally and emit parsed rows
  if (name.endsWith('.csv')) {
    try {
      const text = await file.text()
      const { headers, rows } = parseCSV(text)
      parsedHeaders.value = headers
      previewRows.value = rows
      // show preview modal for user confirmation/validation
      showPreviewModal.value = true
    } catch (err) {
      console.error('Failed to parse CSV', err)
      alert('Không thể đọc file CSV')
    }
  } else {
    // For xlsx/xls, emit raw file so parent can upload or handle with sheetjs
    emit('import-file', file)
  }
  // reset
  event.target.value = ''
}

const parseCSV = (text) => {
  const lines = text.split(/\r?\n/).filter(l => l.trim() !== '')
  if (lines.length === 0) return { headers: [], rows: [] }
  const rawHeaders = lines[0].split(',').map(h => h.trim())
  const rows = []
  for (let i = 1; i < lines.length; i++) {
    const cols = lines[i].split(',')
    const obj = {}
    for (let j = 0; j < rawHeaders.length; j++) {
      obj[rawHeaders[j]] = (cols[j] || '').trim()
    }
    rows.push(obj)
  }
  return { headers: rawHeaders, rows }
}

const confirmImport = () => {
  // emit the parsed rows to parent to continue import
  emit('import', previewRows.value)
  // clear preview state
  previewRows.value = []
  parsedHeaders.value = []
  showPreviewModal.value = false
}

const cancelImport = () => {
  previewRows.value = []
  parsedHeaders.value = []
  showPreviewModal.value = false
}

const exportCSV = (data) => {
  if (!data || !Array.isArray(data)) return
  // Use columns order and labels as headers
  const headers = props.columns.map(c => c.label || c.key)
  const keys = props.columns.map(c => c.key)
  const escape = (val) => {
    if (val == null) return ''
    const s = String(val)
    if (s.includes(',') || s.includes('"') || s.includes('\n')) {
      return '"' + s.replace(/"/g, '""') + '"'
    }
    return s
  }
  const rows = [headers.join(',')]
  data.forEach(item => {
    const vals = keys.map(k => {
      const v = getColumnValue(item, k)
      return escape(v)
    })
    rows.push(vals.join(','))
  })
  const csv = rows.join('\r\n')
  const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `export_${new Date().toISOString().slice(0,10)}.csv`
  document.body.appendChild(a)
  a.click()
  a.remove()
  URL.revokeObjectURL(url)
}

const clearDateFilter = () => {
  dateFrom.value = ''
  dateTo.value = ''
}

const toggleCategoryDropdown = () => {
  showCategoryDropdown.value = !showCategoryDropdown.value
}

const toggleAllCategories = () => {
  if (isAllCategoriesSelected.value) {
    selectedCategories.value = []
  } else {
    selectedCategories.value = props.categories.map(cat => cat.id)
  }
}

const getCategoryName = (categoryId) => {
  const category = props.categories?.find(cat => cat.id === categoryId)
  return category ? category.name : 'Unknown'
}

const clearCategoryFilter = () => {
  selectedCategories.value = []
  selectedCategory.value = ''
}

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
  }
}

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
  }
}

const goToPage = (page) => {
  currentPage.value = page
}

// Public methods (exposed for external access)
const clearSelection = () => {
  selectedRows.value = []
}

const selectRows = (ids) => {
  selectedRows.value = [...ids]
}

const getSelectedRows = () => {
  return selectedRows.value
}

const refreshData = () => {
  currentPage.value = 1
  selectedRows.value = []
}

// Watch for data changes to reset pagination
watch(() => props.data, () => {
  currentPage.value = 1
  selectedRows.value = []
}, { deep: false })

watch([searchQuery, selectedCategory, selectedStatus, dateFrom, dateTo], () => {
  currentPage.value = 1
})

watch(selectedCategories, () => {
  currentPage.value = 1
})

// Close dropdown when clicking outside
onMounted(() => {
  document.addEventListener('click', (e) => {
    const dropdown = document.querySelector('.category-dropdown')
    if (dropdown && !dropdown.contains(e.target)) {
      showCategoryDropdown.value = false
    }
  })
})

watch(pageSize, () => {
  currentPage.value = 1
})

// Expose methods for parent components
defineExpose({
  clearSelection,
  selectRows,
  getSelectedRows,
  refreshData
})
</script>

<style scoped>
.data-table-container {
  width: 100%;
}

/* Custom scrollbar for table */
.overflow-x-auto::-webkit-scrollbar {
  height: 6px;
}

.overflow-x-auto::-webkit-scrollbar-track {
  background-color: #f3f4f6;
}

.overflow-x-auto::-webkit-scrollbar-thumb {
  background-color: #9ca3af;
  border-radius: 4px;
}

.overflow-x-auto::-webkit-scrollbar-thumb:hover {
  background-color: #6b7280;
}

/* Responsive table styling */
@media (max-width: 640px) {
  .data-table-container table {
    font-size: 0.875rem;
  }
  
  .data-table-container th,
  .data-table-container td {
    padding: 0.5rem 0.75rem;
  }
}
</style>
