<template>
  <div class="w-full min-h-screen bg-gray-50">
    <div class="px-6 py-6">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-6">
      <div>
        <h1 class="text-3xl font-bold text-gray-800">Quản lý danh mục</h1>
        <p class="text-gray-600 mt-1">Quản lý danh mục sản phẩm phụ tùng xe tải</p>
      </div>
    </div>

    <!-- Advanced Search Panel -->
    <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
      <div class="flex items-center justify-between mb-4">
        <h3 class="text-lg font-semibold text-gray-800">Tìm kiếm nâng cao</h3>
        <button 
          @click="toggleAdvancedSearch"
          class="text-blue-600 hover:text-blue-800 transition-colors flex items-center"
        >
          <svg class="h-4 w-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
          </svg>
          {{ showAdvancedSearch ? 'Thu gọn' : 'Mở rộng' }}
        </button>
      </div>

      <!-- Basic Search -->
      <div class="flex flex-col sm:flex-row gap-4 mb-4">
        <div class="flex-1">
          <label class="block text-sm font-medium text-gray-700 mb-1">Từ khóa tìm kiếm</label>
          <input
            v-model="searchFilters.keyword"
            type="text"
            placeholder="Tìm theo tên, mã, mô tả..."
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
        </div>
        <div class="flex items-end">
          <button
            @click="performSearch"
            class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors flex items-center"
          >
            <svg class="h-4 w-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
            Tìm kiếm
          </button>
        </div>
      </div>

      <!-- Advanced Search Options -->
      <div v-if="showAdvancedSearch" class="space-y-4">
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Danh mục cha</label>
            <select v-model="searchFilters.parentId" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500">
              <option value="">Tất cả</option>
              <option value="null">Danh mục gốc</option>
              <option v-for="cat in availableParents" :key="cat.id" :value="cat.id">
                {{ cat.name }}
              </option>
            </select>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Cấp độ</label>
            <select v-model="searchFilters.level" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500">
              <option value="">Tất cả</option>
              <option value="1">Cấp 1 (Gốc)</option>
              <option value="2">Cấp 2 (Con)</option>
              <option value="3">Cấp 3 (Cháu)</option>
            </select>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
            <select v-model="searchFilters.isActive" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500">
              <option value="">Tất cả</option>
              <option value="true">Hoạt động</option>
              <option value="false">Không hoạt động</option>
            </select>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Số sản phẩm</label>
            <select v-model="searchFilters.productCount" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500">
              <option value="">Tất cả</option>
              <option value="0">Không có sản phẩm</option>
              <option value="1-5">1-5 sản phẩm</option>
              <option value="6-10">6-10 sản phẩm</option>
              <option value="10+">Trên 10 sản phẩm</option>
            </select>
          </div>
        </div>

        <!-- Advanced Search Buttons -->
        <div class="flex items-center justify-center space-x-4 pt-2">
          <button
            @click="performSearch"
            class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors flex items-center"
          >
            <svg class="h-4 w-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
            Tìm kiếm nâng cao
          </button>
          
          <button
            @click="performQuickSearch"
            class="px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors flex items-center"
          >
            <svg class="h-4 w-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
            Tìm nhanh
          </button>
        </div>
      </div>

      <!-- Search Actions -->
      <div v-if="showAdvancedSearch" class="flex items-center justify-between mt-4 pt-4 border-t border-gray-200">
        <div class="flex items-center space-x-4">
          <button
            @click="clearAllFilters"
            class="px-4 py-2 text-gray-600 hover:text-gray-800 transition-colors flex items-center"
          >
            <svg class="h-4 w-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
            Xóa tất cả
          </button>
          <button
            @click="saveSearchPreset"
            class="px-4 py-2 text-blue-600 hover:text-blue-800 transition-colors flex items-center"
          >
            <svg class="h-4 w-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z" />
            </svg>
            Lưu bộ lọc
          </button>
        </div>
        
        <div class="text-sm text-gray-500">
          <span v-if="hasActiveFilters()" class="text-blue-600 font-medium">
            Tìm thấy {{ filteredCategories.length }} danh mục
          </span>
          <span v-else>
            Hiển thị tất cả {{ filteredCategories.length }} danh mục
          </span>
        </div>
      </div>
    </div>

    <!-- DataTable -->
    <DataTable
      :data="filteredCategories"
      :columns="columns"
      :loading="loading"
      :show-export="true"
      @create="handleCreate"
      @edit="handleEdit"
      @delete="handleDelete"
      @bulk-action="handleBulkAction"
      @export="handleExport"
    >
      <!-- Custom cell cho mô tả -->
      <template #cell-description="{ item }">
        <div class="text-sm text-gray-600 max-w-xs truncate" :title="item.description">
          {{ item.description || 'Không có mô tả' }}
        </div>
      </template>

      <!-- Custom cell cho số sản phẩm -->
      <template #cell-productCount="{ item }">
        <div class="text-center">
          <span class="text-sm font-medium text-blue-600">{{ item.productCount || 0 }}</span>
        </div>
      </template>

      <!-- Custom cell cho cấp độ -->
      <template #cell-level="{ item }">
        <div class="text-center">
          <span class="text-sm font-medium text-purple-600">{{ item.level || 1 }}</span>
        </div>
      </template>

      <!-- Custom cell cho danh mục cha -->
      <template #cell-parent="{ item }">
        <div class="text-sm text-gray-700">
          {{ item.parent?.name || 'Danh mục gốc' }}
        </div>
      </template>

      <!-- Custom cell cho trạng thái -->
      <template #cell-status="{ item }">
        <span class="px-2 py-1 text-xs rounded-full font-medium"
              :class="item.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'">
          {{ item.isActive ? 'Hoạt động' : 'Không hoạt động' }}
        </span>
      </template>

      <!-- Custom actions -->
      <template #cell-actions="{ item }">
        <div class="flex items-center justify-center space-x-2">
          <button
            @click="handleEdit(item)"
            class="text-blue-600 hover:text-blue-900 transition-colors"
            title="Chỉnh sửa"
          >
            <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
            </svg>
          </button>
          <button
            @click="handleDelete(item)"
            class="text-red-600 hover:text-red-900 transition-colors"
            title="Xóa"
          >
            <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
            </svg>
          </button>
        </div>
      </template>
    </DataTable>

    <!-- Add/Edit Category Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-2xl max-h-[80vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-semibold">{{ isEditing ? 'Chỉnh sửa danh mục' : 'Thêm danh mục mới' }}</h3>
          <button @click="closeModal" class="text-gray-400 hover:text-gray-600">
            <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <form @submit.prevent="saveCategory" class="space-y-4">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Tên danh mục *</label>
              <input v-model="categoryForm.name" type="text" class="form-input w-full" required>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Mã danh mục</label>
              <input v-model="categoryForm.code" type="text" class="form-input w-full" placeholder="ENGINE, BRAKE, TIRE...">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Danh mục cha</label>
              <select v-model="categoryForm.parentId" class="form-input w-full">
                <option value="">Không có (danh mục gốc)</option>
                <option v-for="cat in availableParents" :key="cat.id" :value="cat.id">
                  {{ cat.name }}
                </option>
              </select>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Thứ tự sắp xếp</label>
              <input v-model.number="categoryForm.sortOrder" type="number" min="0" class="form-input w-full">
            </div>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Mô tả</label>
            <textarea v-model="categoryForm.description" rows="3" class="form-input w-full" 
                      placeholder="Mô tả chi tiết về danh mục này..."></textarea>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
            <select v-model="categoryForm.isActive" class="form-input w-full">
              <option :value="true">Hoạt động</option>
              <option :value="false">Không hoạt động</option>
            </select>
          </div>
          
          <div class="flex justify-end space-x-3 pt-4">
            <button @click="closeModal" type="button" class="btn-secondary">
              Hủy
            </button>
            <button type="submit" class="btn-primary">
              {{ isEditing ? 'Cập nhật' : 'Thêm danh mục' }}
            </button>
          </div>
        </form>
      </div>
    </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch } from 'vue'
import DataTable from '@/components/DataTable.vue'
import { categoriesApi } from '@/api/categories.js'

export default {
  name: 'Categories',
  components: {
    DataTable
  },
  setup() {
    const loading = ref(false)
    const showModal = ref(false)
    const isEditing = ref(false)
    const selectedCategory = ref(null)
    const showAdvancedSearch = ref(false)
    
    // Search filters
    const searchFilters = ref({
      keyword: '',
      parentId: '',
      level: '',
      isActive: '',
      productCount: ''
    })

    // Column definitions for DataTable
    const columns = ref([
      {
        key: 'name',
        label: 'Tên danh mục',
        sortable: true,
        width: '200px'
      },
      {
        key: 'code',
        label: 'Mã danh mục',
        sortable: true,
        width: '120px'
      },
      {
        key: 'parent',
        label: 'Danh mục cha',
        sortable: true,
        width: '150px'
      },
      {
        key: 'level',
        label: 'Cấp độ',
        sortable: true,
        align: 'center',
        width: '80px'
      },
      {
        key: 'productCount',
        label: 'Số SP',
        sortable: true,
        align: 'center',
        width: '80px'
      },
      {
        key: 'sortOrder',
        label: 'Thứ tự',
        sortable: true,
        align: 'center',
        width: '80px'
      },
      {
        key: 'description',
        label: 'Mô tả',
        sortable: false,
        width: '200px'
      },
      {
        key: 'status',
        label: 'Trạng thái',
        sortable: true,
        type: 'badge',
        align: 'center',
        width: '120px'
      },
      {
        key: 'actions',
        label: 'Thao tác',
        sortable: false,
        align: 'center',
        width: '100px'
      }
    ])

    // Mock data - sẽ thay thế bằng API calls
    const categories = ref([
      {
        id: 1,
        name: 'Hệ thống động cơ',
        code: 'ENGINE',
        description: 'Các phụ tùng liên quan đến động cơ xe tải',
        parentId: null,
        parent: null,
        level: 1,
        sortOrder: 1,
        productCount: 15,
        isActive: true,
        createdAt: '2024-01-15T10:00:00',
        updatedAt: '2024-01-15T10:00:00'
      },
      {
        id: 2,
        name: 'Hệ thống phanh',
        code: 'BRAKE',
        description: 'Các phụ tùng liên quan đến hệ thống phanh',
        parentId: null,
        parent: null,
        level: 1,
        sortOrder: 2,
        productCount: 8,
        isActive: true,
        createdAt: '2024-01-15T10:00:00',
        updatedAt: '2024-01-15T10:00:00'
      },
      {
        id: 3,
        name: 'Lốp xe',
        code: 'TIRE',
        description: 'Các loại lốp xe tải',
        parentId: null,
        parent: null,
        level: 1,
        sortOrder: 3,
        productCount: 12,
        isActive: true,
        createdAt: '2024-01-15T10:00:00',
        updatedAt: '2024-01-15T10:00:00'
      },
      {
        id: 4,
        name: 'Hệ thống truyền động',
        code: 'TRANSMISSION',
        description: 'Hộp số, cầu xe, các phụ tùng truyền động',
        parentId: null,
        parent: null,
        level: 1,
        sortOrder: 4,
        productCount: 6,
        isActive: true,
        createdAt: '2024-01-15T10:00:00',
        updatedAt: '2024-01-15T10:00:00'
      },
      {
        id: 5,
        name: 'Hệ thống điện',
        code: 'ELECTRICAL',
        description: 'Các phụ tùng điện, đèn, ắc quy',
        parentId: null,
        parent: null,
        level: 1,
        sortOrder: 5,
        productCount: 10,
        isActive: true,
        createdAt: '2024-01-15T10:00:00',
        updatedAt: '2024-01-15T10:00:00'
      },
      {
        id: 6,
        name: 'Hệ thống làm mát',
        code: 'COOLING',
        description: 'Bơm nước, két nước, quạt gió',
        parentId: null,
        parent: null,
        level: 1,
        sortOrder: 6,
        productCount: 4,
        isActive: true,
        createdAt: '2024-01-15T10:00:00',
        updatedAt: '2024-01-15T10:00:00'
      },
      {
        id: 7,
        name: 'Hệ thống nhiên liệu',
        code: 'FUEL',
        description: 'Bơm nhiên liệu, bộ lọc nhiên liệu',
        parentId: null,
        parent: null,
        level: 1,
        sortOrder: 7,
        productCount: 3,
        isActive: true,
        createdAt: '2024-01-15T10:00:00',
        updatedAt: '2024-01-15T10:00:00'
      },
      {
        id: 8,
        name: 'Phụ kiện',
        code: 'ACCESSORIES',
        description: 'Các phụ kiện bổ sung cho xe tải',
        parentId: null,
        parent: null,
        level: 1,
        sortOrder: 8,
        productCount: 20,
        isActive: true,
        createdAt: '2024-01-15T10:00:00',
        updatedAt: '2024-01-15T10:00:00'
      },
      {
        id: 9,
        name: 'Lọc dầu động cơ',
        code: 'ENGINE_OIL_FILTER',
        description: 'Các loại lọc dầu động cơ',
        parentId: 1,
        parent: { id: 1, name: 'Hệ thống động cơ' },
        level: 2,
        sortOrder: 1,
        productCount: 5,
        isActive: true,
        createdAt: '2024-01-15T10:00:00',
        updatedAt: '2024-01-15T10:00:00'
      },
      {
        id: 10,
        name: 'Lọc gió động cơ',
        code: 'ENGINE_AIR_FILTER',
        description: 'Các loại lọc gió động cơ',
        parentId: 1,
        parent: { id: 1, name: 'Hệ thống động cơ' },
        level: 2,
        sortOrder: 2,
        productCount: 4,
        isActive: true,
        createdAt: '2024-01-15T10:00:00',
        updatedAt: '2024-01-15T10:00:00'
      }
    ])

    // Form data
    const categoryForm = ref({
      name: '',
      code: '',
      description: '',
      parentId: '',
      sortOrder: 0,
      isActive: true
    })

    // Computed properties
    const availableParents = computed(() => {
      return categories.value.filter(cat => cat.id !== selectedCategory.value?.id)
    })

    // Filtered categories - will be updated by API calls
    const filteredCategories = ref([])

    // Methods
    const handleCreate = () => {
      isEditing.value = false
      selectedCategory.value = null
      resetForm()
      showModal.value = true
    }

    const handleEdit = (category) => {
      isEditing.value = true
      selectedCategory.value = category
      categoryForm.value = {
        name: category.name,
        code: category.code,
        description: category.description,
        parentId: category.parentId || '',
        sortOrder: category.sortOrder,
        isActive: category.isActive
      }
      showModal.value = true
    }

    const handleDelete = async (category) => {
      const confirmed = await window.$confirm({
        title: 'Xác nhận xóa',
        message: `Bạn có chắc chắn muốn xóa danh mục "${category.name}"?`,
        type: 'warning'
      })

      if (confirmed) {
        try {
          await categoriesApi.deleteCategory(category.id)
          await loadCategories() // Reload categories
        } catch (error) {
          console.error('Error deleting category:', error)
          // TODO: Show error message to user
        }
      }
    }

    const handleBulkAction = (selectedIds) => {
      console.log('Bulk action for categories:', selectedIds)
    }

    const handleExport = (data) => {
      console.log('Export categories:', data)
    }

    const saveCategory = async () => {
      try {
        if (isEditing.value) {
          // Update existing category
          await categoriesApi.updateCategory(selectedCategory.value.id, categoryForm.value)
        } else {
          // Create new category
          await categoriesApi.createCategory(categoryForm.value)
        }
        
        await loadCategories() // Reload categories
        closeModal()
      } catch (error) {
        console.error('Error saving category:', error)
        // TODO: Show error message to user
      }
    }

    // Advanced search methods
    const toggleAdvancedSearch = () => {
      showAdvancedSearch.value = !showAdvancedSearch.value
    }

    const performSearch = async () => {
      try {
        loading.value = true
        
        // Prepare search request
        const searchRequest = {
          keyword: searchFilters.value.keyword || null,
          parentId: searchFilters.value.parentId === 'null' ? null : 
                   searchFilters.value.parentId ? parseInt(searchFilters.value.parentId) : null,
          level: searchFilters.value.level ? parseInt(searchFilters.value.level) : null,
          isActive: searchFilters.value.isActive !== '' ? searchFilters.value.isActive === 'true' : null,
          productCountRange: searchFilters.value.productCount || null,
          sortBy: 'name',
          sortDirection: 'asc'
        }
        
        // Call API
        const response = await categoriesApi.advancedSearch(searchRequest)
        filteredCategories.value = response.data
        
        console.log('Advanced search completed, found:', filteredCategories.value.length, 'categories')
      } catch (error) {
        console.error('Error performing advanced search:', error)
        // Fallback to basic search or show error
        filteredCategories.value = []
      } finally {
        loading.value = false
      }
    }

    const performQuickSearch = async () => {
      try {
        loading.value = true
        
        // Quick search chỉ dùng keyword
        const searchRequest = {
          keyword: searchFilters.value.keyword || null,
          parentId: null,
          level: null,
          isActive: null,
          productCountRange: null,
          sortBy: 'name',
          sortDirection: 'asc'
        }
        
        // Call API
        const response = await categoriesApi.advancedSearch(searchRequest)
        filteredCategories.value = response.data
        
        console.log('Quick search completed, found:', filteredCategories.value.length, 'categories')
      } catch (error) {
        console.error('Error performing quick search:', error)
        filteredCategories.value = []
      } finally {
        loading.value = false
      }
    }

    const clearAllFilters = () => {
      searchFilters.value = {
        keyword: '',
        parentId: '',
        level: '',
        isActive: '',
        productCount: ''
      }
      // Reset to show all categories
      filteredCategories.value = [...categories.value]
    }

    // Auto-search when filters change (debounced)
    let searchTimeout = null
    const autoSearch = () => {
      if (searchTimeout) {
        clearTimeout(searchTimeout)
      }
      searchTimeout = setTimeout(() => {
        if (hasActiveFilters()) {
          performSearch()
        } else {
          filteredCategories.value = [...categories.value]
        }
      }, 500) // 500ms delay
    }

    const hasActiveFilters = () => {
      return searchFilters.value.keyword || 
             searchFilters.value.parentId || 
             searchFilters.value.level || 
             searchFilters.value.isActive !== '' || 
             searchFilters.value.productCount
    }

    const saveSearchPreset = () => {
      const presetName = prompt('Nhập tên cho bộ lọc này:')
      if (presetName) {
        const presets = JSON.parse(localStorage.getItem('categorySearchPresets') || '{}')
        presets[presetName] = { ...searchFilters.value }
        localStorage.setItem('categorySearchPresets', JSON.stringify(presets))
        alert(`Đã lưu bộ lọc "${presetName}"`)
      }
    }

    const closeModal = () => {
      showModal.value = false
      resetForm()
    }

    const resetForm = () => {
      categoryForm.value = {
        name: '',
        code: '',
        description: '',
        parentId: '',
        sortOrder: 0,
        isActive: true
      }
    }

    onMounted(async () => {
      await loadCategories()
    })

    // Watch for filter changes to auto-search
    watch(searchFilters, () => {
      autoSearch()
    }, { deep: true })

    const loadCategories = async () => {
      try {
        loading.value = true
        const response = await categoriesApi.getAllCategories()
        categories.value = response.data
        filteredCategories.value = [...response.data] // Initialize with all categories
      } catch (error) {
        console.error('Error loading categories:', error)
        // Fallback to mock data if API fails
        filteredCategories.value = [...categories.value]
      } finally {
        loading.value = false
      }
    }

          return {
        loading,
        showModal,
        isEditing,
        selectedCategory,
        showAdvancedSearch,
        searchFilters,
        filteredCategories,
        columns,
        categories,
        categoryForm,
        availableParents,
        handleCreate,
        handleEdit,
        handleDelete,
        handleBulkAction,
        handleExport,
        saveCategory,
        closeModal,
        loadCategories,
        toggleAdvancedSearch,
        performSearch,
        performQuickSearch,
        clearAllFilters,
        saveSearchPreset
      }
  }
}
</script> 