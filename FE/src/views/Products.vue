<template>
  <div class="container mx-auto px-4 py-6">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-6">
      <div>
        <h1 class="text-3xl font-bold text-gray-800">Quản lý sản phẩm</h1>
        <p class="text-gray-600 mt-1">Quản lý sản phẩm đa nguồn cung ứng</p>
      </div>
      
      <!-- Training Assistant -->
      <div class="flex items-center space-x-4">
        <TrainingAssistant 
          :selected-product="selectedProductForTraining"
          @search-suggestion="handleSearchSuggestion"
          @help-request="handleHelpRequest"
        />
      </div>
    </div>

    <!-- DataTable -->
    <DataTable
      :data="products"
      :columns="columns"
      :loading="loading"
      :categories="categories"
      :status-options="statusOptions"
      :show-export="true"
      @create="handleCreate"
      @edit="handleEdit"
      @delete="handleDelete"
      @bulk-action="handleBulkAction"
      @export="handleExport"
      @row-click="selectProductForTraining"
    >
      <!-- Custom cell cho hình ảnh sản phẩm -->
      <template #cell-image="{ item }">
        <div class="flex items-center">
          <div class="h-12 w-12 rounded-lg bg-gray-200 flex items-center justify-center mr-4">
            <img 
              v-if="item.imageUrl" 
              :src="item.imageUrl" 
              :alt="item.name"
              class="h-12 w-12 rounded-lg object-cover"
            />
            <svg v-else class="h-6 w-6 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/>
            </svg>
          </div>
          <div>
            <div class="font-medium text-gray-900">{{ item.name }}</div>
            <div class="text-sm text-gray-500">{{ item.sku }}</div>
          </div>
        </div>
      </template>

      <!-- Custom cell cho tồn kho -->
      <template #cell-stock="{ item }">
        <div class="flex items-center justify-center">
          <button 
            @click="showStockDetails(item)"
            class="px-2 py-1 rounded-full text-xs font-medium cursor-pointer hover:bg-gray-100"
            :class="getStockClass(item.stock)"
          >
            {{ item.stock || 0 }}
          </button>
        </div>
      </template>

      <!-- Custom cell cho available stock -->
      <template #cell-availableStock="{ item }">
        <div class="text-center">
          <span class="text-sm font-medium text-green-600">{{ item.availableStock || 0 }}</span>
        </div>
      </template>

      <!-- Custom cell cho reserved stock -->
      <template #cell-reservedStock="{ item }">
        <div class="text-center">
          <span class="text-sm font-medium text-yellow-600">{{ item.reservedStock || 0 }}</span>
        </div>
      </template>

      <!-- Custom cell cho supplier count -->
      <template #cell-supplierCount="{ item }">
        <div class="text-center">
          <span class="text-sm font-medium text-purple-600">{{ item.supplierCount || 0 }}</span>
        </div>
      </template>

      <!-- Custom actions -->
      <template #cell-actions="{ item }">
        <div class="flex items-center justify-center space-x-2">
          <button
            @click="showStockDetails(item)"
            class="text-blue-600 hover:text-blue-900 transition-colors"
            title="Xem chi tiết tồn kho"
          >
            <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </button>
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

    <!-- Stock Details Modal -->
    <div v-if="showStockModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-2xl max-h-[80vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-semibold">Chi tiết tồn kho</h3>
          <button @click="showStockModal = false" class="text-gray-400 hover:text-gray-600">
            <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <div v-if="selectedProductForStock" class="space-y-4">
          <div class="border-b pb-4">
            <h4 class="font-medium text-lg">{{ selectedProductForStock.name }}</h4>
            <p class="text-sm text-gray-600">SKU: {{ selectedProductForStock.sku }}</p>
          </div>

          <!-- Summary Cards -->
          <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div class="bg-blue-50 p-4 rounded-lg">
              <div class="text-sm text-blue-600">Tổng tồn kho</div>
              <div class="text-xl font-bold text-blue-800">{{ selectedProductForStock.stock || 0 }}</div>
            </div>
            <div class="bg-green-50 p-4 rounded-lg">
              <div class="text-sm text-green-600">Có sẵn</div>
              <div class="text-xl font-bold text-green-800">{{ selectedProductForStock.availableStock || 0 }}</div>
            </div>
            <div class="bg-yellow-50 p-4 rounded-lg">
              <div class="text-sm text-yellow-600">Đã đặt trước</div>
              <div class="text-xl font-bold text-yellow-800">{{ selectedProductForStock.reservedStock || 0 }}</div>
            </div>
            <div class="bg-purple-50 p-4 rounded-lg">
              <div class="text-sm text-purple-600">Nhà cung cấp</div>
              <div class="text-xl font-bold text-purple-800">{{ selectedProductForStock.supplierCount || 0 }}</div>
            </div>
          </div>

          <!-- Inventory Details Table -->
          <div v-if="selectedProductForStock.inventoryDetails && selectedProductForStock.inventoryDetails.length > 0">
            <h5 class="font-medium text-gray-900 mb-3">Chi tiết theo nhà cung cấp</h5>
            <div class="overflow-x-auto">
              <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                  <tr>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Nhà cung cấp
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Tồn kho
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Có sẵn
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Đã đặt
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Giá vốn
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Vị trí
                    </th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="inventory in selectedProductForStock.inventoryDetails" :key="inventory.id">
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ inventory.supplier?.name || 'N/A' }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ inventory.quantity || 0 }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ inventory.availableQuantity || 0 }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ inventory.reservedQuantity || 0 }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ formatCurrency(inventory.costPrice) }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ inventory.location || 'N/A' }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          
          <div v-else class="text-center py-8 text-gray-500">
            <p>Chưa có dữ liệu tồn kho cho sản phẩm này</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useProductsStore } from '../stores/products'
import { formatCurrency, debounce } from '../utils/helpers'
import { productsAPI, inventoryAPI } from '@/services'
import TrainingAssistant from '@/components/TrainingAssistant.vue'
import DataTable from '@/components/DataTable.vue'

export default {
  name: 'Products',
  components: {
    TrainingAssistant,
    DataTable
  },
  setup() {
    // Reactive data
    const searchQuery = ref('')
    const selectedCategory = ref('')
    const selectedSupplier = ref('')
    const showAddModal = ref(false)
    const selectedProductForTraining = ref(null)
    const showStockModal = ref(false)
    const selectedProductForStock = ref(null)
    const loading = ref(false)
    
    // Column definitions for DataTable
    const columns = ref([
      {
        key: 'image',
        label: 'Sản phẩm',
        sortable: false,
        width: '280px'
      },
      {
        key: 'category.name',
        label: 'Danh mục',
        sortable: true,
        width: '150px'
      },
      {
        key: 'supplier.name',
        label: 'Nhà cung cấp',
        sortable: true,
        width: '150px'
      },
      {
        key: 'stock',
        label: 'Tồn kho',
        sortable: true,
        align: 'center',
        width: '100px'
      },
      {
        key: 'availableStock',
        label: 'Có sẵn',
        sortable: true,
        align: 'center',
        width: '100px'
      },
      {
        key: 'reservedStock',
        label: 'Đã đặt',
        sortable: true,
        align: 'center',
        width: '100px'
      },
      {
        key: 'supplierCount',
        label: 'Nhà CC',
        sortable: true,
        align: 'center',
        width: '80px'
      },
      {
        key: 'costPrice',
        label: 'Giá vốn',
        sortable: true,
        type: 'currency',
        align: 'right',
        width: '120px'
      },
      {
        key: 'salePrice',
        label: 'Giá bán',
        sortable: true,
        align: 'right',
        width: '120px'
      },
      {
        key: 'status',
        label: 'Trạng thái',
        sortable: true,
        type: 'badge',
        align: 'center',
        width: '100px',
        badgeMap: {
          'ACTIVE': { text: 'Hoạt động', class: 'bg-green-100 text-green-800' },
          'OUT_OF_STOCK': { text: 'Hết hàng', class: 'bg-red-100 text-red-800' },
          'DISCONTINUED': { text: 'Ngừng bán', class: 'bg-gray-100 text-gray-800' }
        }
      },
      {
        key: 'actions',
        label: 'Thao tác',
        sortable: false,
        align: 'center',
        width: '120px'
      }
    ])

    // Status options for filter
    const statusOptions = ref([
      { value: 'ACTIVE', label: 'Hoạt động' },
      { value: 'OUT_OF_STOCK', label: 'Hết hàng' },
      { value: 'DISCONTINUED', label: 'Ngừng bán' }
    ])
    
    // Mock data - sẽ thay thế bằng API calls
    const products = ref([
      {
        id: 1,
        name: 'Lọc dầu động cơ',
        sku: 'OIL-FILTER-001',
        categoryId: 1,
        category: { id: 1, name: 'Hệ thống động cơ' },
        supplierId: 1,
        supplier: { id: 1, name: 'Công ty TNHH ABC' },
        totalStock: 50,
        costPrice: 120000,
        salePrice: 150000,
        status: 'ACTIVE',
        imageUrl: null,
        supplierStocks: [
          { supplierId: 1, quantity: 30 },
          { supplierId: 2, quantity: 20 }
        ]
      },
      {
        id: 2,
        name: 'Má phanh trước',
        sku: 'BRAKE-PAD-002',
        categoryId: 2,
        category: { id: 2, name: 'Hệ thống phanh' },
        supplierId: 2,
        supplier: { id: 2, name: 'Nhà phân phối XYZ' },
        totalStock: 5,
        costPrice: 280000,
        salePrice: 350000,
        status: 'ACTIVE',
        imageUrl: null,
        supplierStocks: [
          { supplierId: 2, quantity: 5 }
        ]
      },
      {
        id: 3,
        name: 'Lốp xe 900R20',
        sku: 'TIRE-003',
        categoryId: 3,
        category: { id: 3, name: 'Lốp xe' },
        supplierId: 1,
        supplier: { id: 1, name: 'Công ty TNHH ABC' },
        totalStock: 0,
        costPrice: 2200000,
        salePrice: 2500000,
        status: 'OUT_OF_STOCK',
        imageUrl: null,
        supplierStocks: []
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

    // API Methods
    const fetchProductsWithInventory = async () => {
      loading.value = true
      try {
        const response = await productsAPI.getAllWithInventory()
        products.value = response.data.map(product => ({
          ...product,
          stock: product.totalStock || 0,
          availableStock: product.availableStock || 0,
          reservedStock: product.reservedStock || 0,
          supplierCount: product.supplierCount || 0
        }))
      } catch (error) {
        console.error('Error fetching products with inventory:', error)
        // Fallback to basic products API
        try {
          const basicResponse = await productsAPI.getAll()
          products.value = basicResponse.data.map(product => ({
            ...product,
            stock: 0,
            availableStock: 0,
            reservedStock: 0,
            supplierCount: 0
          }))
        } catch (fallbackError) {
          console.error('Error fetching basic products:', fallbackError)
        }
      } finally {
        loading.value = false
      }
    }

    const fetchProductInventory = async (productId) => {
      try {
        const response = await inventoryAPI.getProductDetails(productId)
        return response.data
      } catch (error) {
        console.error('Error fetching product inventory:', error)
        return null
      }
    }

    const showStockDetails = async (product) => {
      const inventoryDetails = await fetchProductInventory(product.id)
      if (inventoryDetails) {
        selectedProductForStock.value = {
          ...product,
          inventoryDetails
        }
        showStockModal.value = true
      }
    }

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

    // DataTable event handlers
    const handleCreate = () => {
      console.log('Create new product')
      // Logic to create new product
      showAddModal.value = true
    }

    const handleEdit = (product) => {
      console.log('Edit product:', product)
      // Logic to edit product
    }

    const handleDelete = (product) => {
      if (confirm(`Bạn có chắc chắn muốn xóa sản phẩm "${product.name}"?`)) {
        console.log('Delete product:', product)
        // Logic to delete product
      }
    }

    const handleBulkAction = (selectedIds) => {
      console.log('Bulk action for products:', selectedIds)
      // Logic for bulk actions
    }

    const handleExport = (data) => {
      console.log('Export products:', data)
      // Logic to export data
    }

    const duplicateProduct = (product) => {
      console.log('Duplicate product:', product)
      // Logic to duplicate product
    }

    const viewHistory = (product) => {
      console.log('View history for product:', product)
      // Logic to view product history
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
      selectedProductForTraining.value = product
      console.log('Selected product for training:', product)
    }

    // Lifecycle
    onMounted(() => {
      // Load data when component mounts
      fetchProductsWithInventory()
      console.log('Products component mounted')
    })

    return {
      // Data
      searchQuery,
      selectedCategory,
      selectedSupplier,
      showAddModal,
      selectedProductForTraining,
      showStockModal,
      selectedProductForStock,
      loading,
      
      // Table config
      columns,
      statusOptions,
      
      // Mock data
      products,
      categories,
      suppliers,
      
      // Methods
      getCategoryName,
      getSupplierName,
      getStockClass,
      showStockDetails,
      fetchProductsWithInventory,
      fetchProductInventory,
      formatCurrency,
      
      // DataTable handlers
      handleCreate,
      handleEdit,
      handleDelete,
      handleBulkAction,
      handleExport,
      duplicateProduct,
      viewHistory,
      
      // Training Assistant
      handleSearchSuggestion,
      handleHelpRequest,
      selectProductForTraining
    }
  }
}
</script>
