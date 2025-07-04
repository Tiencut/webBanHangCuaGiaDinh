<template>
  <div class="container mx-auto px-4 py-6">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Quản lý sản phẩm</h1>
      <button 
        @click="showAddModal = true"
        class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg flex items-center"
      >
        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"/>
        </svg>
        Thêm sản phẩm
      </button>
    </div>

    <!-- Filter & Search Section -->
    <div class="bg-white p-4 rounded-lg shadow-sm mb-6">
      <div class="flex justify-between items-start mb-4">
        <h2 class="text-lg font-semibold text-gray-900">Tìm kiếm & Lọc</h2>
        <!-- Training Assistant -->
        <TrainingAssistant 
          :current-search-query="searchQuery"
          :selected-product-id="selectedProductForTraining"
          @search-suggestion="handleSearchSuggestion"
          @help-request="handleHelpRequest"
        />
      </div>
      
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
          <input 
            v-model="searchQuery"
            @input="handleSearchInput"
            type="text" 
            placeholder="Tên sản phẩm, mã sản phẩm..."
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Danh mục</label>
          <select 
            v-model="selectedCategory"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Tất cả</option>
            <option v-for="category in categories" :key="category.id" :value="category.id">
              {{ category.name }}
            </option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Nhà cung cấp</label>
          <select 
            v-model="selectedSupplier"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Tất cả</option>
            <option v-for="supplier in suppliers" :key="supplier.id" :value="supplier.id">
              {{ supplier.name }}
            </option>
          </select>
        </div>
        <div class="flex items-end">
          <button 
            @click="applyFilters"
            class="bg-gray-600 hover:bg-gray-700 text-white px-4 py-2 rounded-md mr-2"
          >
            Lọc
          </button>
          <button 
            @click="clearFilters"
            class="border border-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-50"
          >
            Xóa
          </button>
        </div>
      </div>
    </div>

    <!-- Products Table -->
    <div class="bg-white rounded-lg shadow-sm overflow-hidden">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Sản phẩm
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Danh mục
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Nhà cung cấp
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Tồn kho
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Giá bán
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Trạng thái
              </th>
              <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                Thao tác
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="product in filteredProducts" :key="product.id" class="hover:bg-gray-50">
              <td class="px-6 py-4">
                <div class="flex items-center">
                  <div class="h-12 w-12 rounded-lg bg-gray-200 flex items-center justify-center mr-4">
                    <svg class="h-6 w-6 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/>
                    </svg>
                  </div>
                  <div>
                    <div class="font-medium text-gray-900">{{ product.name }}</div>
                    <div class="text-sm text-gray-500">{{ product.sku }}</div>
                  </div>
                </div>
              </td>
              <td class="px-6 py-4 text-sm text-gray-900">
                {{ getCategoryName(product.categoryId) }}
              </td>
              <td class="px-6 py-4 text-sm text-gray-900">
                {{ getSupplierName(product.supplierId) }}
              </td>
              <td class="px-6 py-4">
                <span :class="getStockClass(product.quantity)" class="px-2 py-1 text-xs font-medium rounded-full">
                  {{ product.quantity }}
                </span>
              </td>
              <td class="px-6 py-4 text-sm text-gray-900">
                {{ formatCurrency(product.salePrice) }}
              </td>
              <td class="px-6 py-4">
                <span :class="getStatusClass(product.status)" class="px-2 py-1 text-xs font-medium rounded-full">
                  {{ getStatusText(product.status) }}
                </span>
              </td>
              <td class="px-6 py-4 text-right text-sm font-medium">
                <button 
                  @click="editProduct(product)"
                  class="text-blue-600 hover:text-blue-900 mr-3"
                >
                  Sửa
                </button>
                <button 
                  @click="deleteProduct(product.id)"
                  class="text-red-600 hover:text-red-900"
                >
                  Xóa
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Pagination -->
    <div class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6 mt-4">
      <div class="flex-1 flex justify-between sm:hidden">
        <button class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50">
          Trước
        </button>
        <button class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50">
          Sau
        </button>
      </div>
      <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
        <div>
          <p class="text-sm text-gray-700">
            Hiển thị <span class="font-medium">1</span> đến <span class="font-medium">10</span> của
            <span class="font-medium">{{ filteredProducts.length }}</span> kết quả
          </p>
        </div>
        <div>
          <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px">
            <button class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
              <span class="sr-only">Previous</span>
              <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
              </svg>
            </button>
            <button class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50">
              1
            </button>
            <button class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
              <span class="sr-only">Next</span>
              <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd" />
              </svg>
            </button>
          </nav>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useProductsStore } from '../stores/products'
import { formatCurrency, debounce } from '../utils/helpers'
import TrainingAssistant from '@/components/TrainingAssistant.vue'

export default {
  name: 'Products',
  components: {
    TrainingAssistant
  },
  setup() {
    // Reactive data
    const searchQuery = ref('')
    const selectedCategory = ref('')
    const selectedSupplier = ref('')
    const showAddModal = ref(false)
    const selectedProductForTraining = ref(null)
    
    // Mock data - sẽ thay thế bằng API calls
    const products = ref([
      {
        id: 1,
        name: 'Lọc dầu động cơ',
        sku: 'OIL-FILTER-001',
        categoryId: 1,
        supplierId: 1,
        quantity: 50,
        salePrice: 150000,
        status: 'ACTIVE'
      },
      {
        id: 2,
        name: 'Má phanh trước',
        sku: 'BRAKE-PAD-002',
        categoryId: 2,
        supplierId: 2,
        quantity: 5,
        salePrice: 350000,
        status: 'ACTIVE'
      },
      {
        id: 3,
        name: 'Lốp xe 900R20',
        sku: 'TIRE-003',
        categoryId: 3,
        supplierId: 1,
        quantity: 0,
        salePrice: 2500000,
        status: 'OUT_OF_STOCK'
      }
    ])

    const categories = ref([
      { id: 1, name: 'Hệ thống động cơ' },
      { id: 2, name: 'Hệ thống phanh' },
      { id: 3, name: 'Lốp xe' }
    ])

    const suppliers = ref([
      { id: 1, name: 'Công ty TNHH ABC' },
      { id: 2, name: 'Nhà phân phối XYZ' }
    ])

    // Computed properties
    const filteredProducts = computed(() => {
      let filtered = products.value

      if (searchQuery.value) {
        filtered = filtered.filter(product => 
          product.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
          product.sku.toLowerCase().includes(searchQuery.value.toLowerCase())
        )
      }

      if (selectedCategory.value) {
        filtered = filtered.filter(product => product.categoryId == selectedCategory.value)
      }

      if (selectedSupplier.value) {
        filtered = filtered.filter(product => product.supplierId == selectedSupplier.value)
      }

      return filtered
    })

    // Methods
    const getCategoryName = (categoryId) => {
      const category = categories.value.find(c => c.id === categoryId)
      return category ? category.name : 'N/A'
    }

    const getSupplierName = (supplierId) => {
      const supplier = suppliers.value.find(s => s.id === supplierId)
      return supplier ? supplier.name : 'N/A'
    }

    const getStockClass = (quantity) => {
      if (quantity === 0) return 'bg-red-100 text-red-800'
      if (quantity < 10) return 'bg-yellow-100 text-yellow-800'
      return 'bg-green-100 text-green-800'
    }

    const getStatusClass = (status) => {
      switch (status) {
        case 'ACTIVE': return 'bg-green-100 text-green-800'
        case 'OUT_OF_STOCK': return 'bg-red-100 text-red-800'
        case 'DISCONTINUED': return 'bg-gray-100 text-gray-800'
        default: return 'bg-gray-100 text-gray-800'
      }
    }

    const getStatusText = (status) => {
      switch (status) {
        case 'ACTIVE': return 'Hoạt động'
        case 'OUT_OF_STOCK': return 'Hết hàng'
        case 'DISCONTINUED': return 'Ngừng bán'
        default: return 'Không xác định'
      }
    }

    const formatCurrency = (amount) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(amount)
    }

    const applyFilters = () => {
      // Logic to apply filters
      console.log('Applying filters...')
    }

    const clearFilters = () => {
      searchQuery.value = ''
      selectedCategory.value = ''
      selectedSupplier.value = ''
    }

    const editProduct = (product) => {
      console.log('Edit product:', product)
      // Logic to edit product
    }

    const deleteProduct = (productId) => {
      if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')) {
        console.log('Delete product:', productId)
        // Logic to delete product
      }
    }

    // Training Assistant Methods
    const handleSearchSuggestion = (suggestion) => {
      searchQuery.value = suggestion.query || suggestion
      console.log('Applied search suggestion:', suggestion)
    }

    const handleHelpRequest = (request) => {
      console.log('Help request:', request)
      // Show help modal or send to support
      alert('Yêu cầu hỗ trợ đã được ghi nhận. Nhân viên senior sẽ hỗ trợ bạn sớm nhất.')
    }

    const selectProductForTraining = (product) => {
      selectedProductForTraining.value = product.id
      console.log('Selected product for training:', product)
    }

    // Lifecycle
    onMounted(() => {
      // Load data when component mounts
      console.log('Products component mounted')
    })

    return {
      searchQuery,
      selectedCategory,
      selectedSupplier,
      showAddModal,
      selectedProductForTraining,
      products,
      categories,
      suppliers,
      filteredProducts,
      getCategoryName,
      getSupplierName,
      getStockClass,
      getStatusClass,
      getStatusText,
      formatCurrency,
      applyFilters,
      clearFilters,
      editProduct,
      deleteProduct,
      handleSearchSuggestion,
      handleHelpRequest,
      selectProductForTraining
    }
  }
}
</script>
