<template>
  <div class="w-full min-h-screen bg-gray-50">
    <div class="px-6 py-6">
      <!-- Header Section -->
      <div class="flex justify-between items-center mb-6">
        <div>
          <h1 class="text-3xl font-bold text-gray-800">Quản lý sản phẩm</h1>
        </div>

        <!-- Training Assistant -->
        <div class="flex items-center space-x-4">
            <TrainingAssistant :selected-product="selectedProductForTraining" @search-suggestion="handleSearchSuggestion"
              @help-request="handleHelpRequest" />
            <!-- (Excel control removed — using DataTable built-in Import/Export) -->
        </div>
      </div>

      <!-- Column Visibility Controls (products-scoped inline version) -->
      <ColumnVisibilityControls :all-columns="allColumns" @update:visibleColumns="onVisibleColumnsUpdate" />

      <!-- Column Visibility Controls (products-scoped inline version) -->

      <!-- DataTable -->
      <DataTable :data="filteredProducts" :columns="visibleColumns" :loading="loading" :categories="categories"
        :status-options="statusOptions" :show-export="true" :show-import="true" :column-filters="columnFilters"
        @update:columnFilters="val => columnFilters = val" @create="handleCreate" @edit="handleEdit"
        @delete="handleDelete" @bulk-action="handleBulkAction" @export="handleExport" @import="handleImportedRows" @import-file="handleImportedFile" @row-click="handleRowClick">
        <!-- Custom cell cho hình ảnh sản phẩm -->
        <template #cell-image="{ item }">
          <div class="flex items-center">
            <div class="h-12 w-12 rounded-lg bg-gray-200 flex items-center justify-center mr-4">
              <img v-if="item.imageUrl" :src="item.imageUrl" :alt="item.name"
                class="h-12 w-12 rounded-lg object-cover" />
              <svg v-else class="h-6 w-6 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
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
            <button @click="showStockDetails(item)"
              class="px-2 py-1 rounded-full text-xs font-medium cursor-pointer hover:bg-gray-100"
              :class="getStockClass(item.stock)">
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

        <!-- Custom cell cho brand -->
        <template #cell-brand="{ item }">
          <div class="text-sm font-medium text-gray-900">{{ item.brand || '-' }}</div>
        </template>

        <!-- Custom cell cho model -->
        <template #cell-model="{ item }">
          <div class="text-sm text-gray-600">{{ item.model || '-' }}</div>
        </template>

        <!-- Custom cell cho partNumber -->
        <template #cell-partNumber="{ item }">
          <div class="text-sm font-mono text-blue-600">{{ item.partNumber || '-' }}</div>
        </template>

        <!-- Custom cell cho vehicleType -->
        <template #cell-vehicleType="{ item }">
          <div class="text-sm text-gray-700">{{ item.vehicleType || '-' }}</div>
        </template>

        <!-- Custom cell cho minStockLevel -->
        <template #cell-minStockLevel="{ item }">
          <div class="text-center">
            <span class="text-sm font-medium text-orange-600">{{ item.minStockLevel || 0 }}</span>
          </div>
        </template>

        <!-- Custom cell cho reorderPoint -->
        <template #cell-reorderPoint="{ item }">
          <div class="text-center">
            <span class="text-sm font-medium text-red-600">{{ item.reorderPoint || 0 }}</span>
          </div>
        </template>

        <!-- Custom cell cho ngày tạo -->
        <template #cell-createdAt="{ item }">
          <div class="text-center">
            <span class="text-sm text-gray-700">
              {{ item.createdAt ? new Date(item.createdAt).toLocaleDateString('vi-VN') : 'N/A' }}
            </span>
          </div>
        </template>

        <!-- Custom actions -->
        <template #cell-actions="{ item }">
          <div class="flex items-center justify-center space-x-2">
            <button @click="showStockDetails(item)" class="text-blue-600 hover:text-blue-900 transition-colors"
              title="Xem chi tiết tồn kho">
              <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </button>

            <!-- Combo management button -->
            <button @click="handleEdit(item)" class="text-blue-600 hover:text-blue-900 transition-colors"
              title="Chỉnh sửa">
              <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
              </svg>
            </button>
            <button @click="handleDelete(item)" class="text-red-600 hover:text-red-900 transition-colors" title="Xóa">
              <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
              </svg>
            </button>
          </div>
        </template>
      </DataTable>


      <!-- Product modals: components tự quản lý nội bộ, view gọi qua ref -->
      <ProductStockDetailsModal ref="productStockModalRef" />

      <!-- Product Detail Modal -->
      <ProductDetailModal ref="productDetailModalRef" :categories="categories" />

      <!-- Add Product Modal -->
      <AddProductModal ref="addProductModalRef" :categories="categories" :suppliers="suppliers" @product-created="fetchProductsWithInventory" />

      <!-- Combo Management Modal -->
      <ComboManagementModal ref="comboManagementModalRef" />
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch, onUnmounted } from 'vue'
import { useProductsStore } from '../stores/products'
import { formatCurrency, debounce } from '../utils/helpers'
import { productsAPI, inventoryAPI, categoriesAPI } from '@/services'
import TrainingAssistant from '@/components/TrainingAssistant.vue'
import DataTable from '@/components/DataTable.vue'
import ProductStockDetailsModal from '@/components/products/ProductStockDetailsModal.vue'
import ProductDetailModal from '@/components/products/ProductDetailModal.vue'
import AddProductModal from '@/components/products/AddProductModal.vue'
import ComboManagementModal from '@/components/products/ComboManagementModal.vue'
// Use the products-scoped ColumnVisibilityControls (inline controls)
import ColumnVisibilityControls from '@/components/products/ColumnVisibilityControls.vue'
import { removeVietnameseTones } from '../utils/removeVietnameseTones'


export default {
  name: 'Products',
  components: {
    TrainingAssistant,
    DataTable,

    ProductStockDetailsModal,
    ProductDetailModal,
    AddProductModal,
    ComboManagementModal,
    ColumnVisibilityControls,
  },
  setup() {
  // Reactive data
  const searchQuery = ref('')
  const selectedCategory = ref('')
  
  const selectedSupplier = ref('')
  const products = ref([])
  const categories = ref([])
    const fetchCategories = async () => {
      try {
        const res = await categoriesAPI.getAll()
        // API may return array directly or under data.content
        // categoriesAPI.getAll likely returns { data: [...] }
        categories.value = res.data || []
      } catch (err) {
        console.error('Error fetching categories:', err)
        categories.value = []
      }
    }
  const suppliers = ref([])
  const columnVisibility = ref({
    // Minimal default: show only the most important/simple columns
    image: true,
    sellingPrice: true,
    stock: true,
    availableStock: true,
    status: true,
    actions: true
  })
  const tempColumnVisibility = ref({})
  const comboComponents = ref([])
  // modal refs: components tự quản lý trạng thái nội bộ
  const productStockModalRef = ref(null)
  const productDetailModalRef = ref(null)
  const addProductModalRef = ref(null)
  const comboManagementModalRef = ref(null)
  const selectedProductForTraining = ref(null)

    const loading = ref(false)

    // Column definitions for DataTable
    const columns = ref([
      {
        key: 'image',
        label: 'Sản phẩm',
        sortable: false,
        width: '280px'
      }
      ,
      {
        key: 'brand',
        label: 'Thương hiệu',
        sortable: true,
        width: '120px'
      }
      ,
      {
        key: 'model',
        label: 'Model',
        sortable: true,
        width: '120px'
      }
      ,
      {
        key: 'partNumber',
        label: 'Mã phụ tùng',
        sortable: true,
        width: '130px'
      }
      ,
      {
        key: 'vehicleType',
        label: 'Loại xe',
        sortable: true,
        width: '120px'
      }
      ,
      {
        key: 'category.name',
        label: 'Danh mục',
        sortable: true,
        width: '150px'
      }
      ,
      {
        key: 'supplier.name',
        label: 'Nhà cung cấp',
        sortable: true,
        width: '150px'
      },
      {
        key: 'basePrice',
        label: 'Giá cơ bản',
        sortable: true,
        type: 'currency',
        align: 'right',
        width: '120px'
      },
      {
        key: 'sellingPrice',
        label: 'Giá bán',
        sortable: true,
        type: 'currency',
        align: 'right',
        width: '120px'
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
        key: 'stock',
        label: 'Tồn kho',
        sortable: true,
        align: 'center',
        width: '80px'
      },
      {
        key: 'availableStock',
        label: 'Có sẵn',
        sortable: true,
        align: 'center',
        width: '80px'
      },
      {
        key: 'reservedStock',
        label: 'Đã đặt',
        sortable: true,
        align: 'center',
        width: '80px'
      },
      {
        key: 'minStockLevel',
        label: 'Tồn tối thiểu',
        sortable: true,
        align: 'center',
        width: '100px'
      },
      {
        key: 'reorderPoint',
        label: 'Điểm đặt hàng',
        sortable: true,
        align: 'center',
        width: '100px'
      },
      {
        key: 'supplierCount',
        label: 'Nhà CC',
        sortable: true,
        align: 'center',
        width: '70px'
      },
      {
        key: 'warrantyPeriod',
        label: 'Bảo hành',
        sortable: true,
        align: 'center',
        width: '80px',
        formatter: (value) => value ? `${value} tháng` : 'N/A'
      },
      {
        key: 'weight',
        label: 'Trọng lượng',
        sortable: true,
        align: 'center',
        width: '100px',
        formatter: (value) => value ? `${value} kg` : 'N/A'
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
          'DISCONTINUED': { text: 'Ngừng bán', class: 'bg-gray-100 text-gray-800' },
          'INACTIVE': { text: 'Không hoạt động', class: 'bg-yellow-100 text-yellow-800' }
        }
      },
      {
        key: 'isCombo',
        label: 'Loại',
        sortable: true,
        type: 'badge',
        align: 'center',
        width: '80px',
        badgeMap: {
          true: { text: 'Combo', class: 'bg-purple-100 text-purple-800' },
          false: { text: 'Đơn lẻ', class: 'bg-blue-100 text-blue-800' }
        }
      },
      {
        key: 'createdAt',
        label: 'Ngày tạo',
        sortable: true,
        type: 'date',
        align: 'center',
        width: '120px',
        formatter: (value) => value ? new Date(value).toLocaleDateString('vi-VN') : 'N/A'
      },

    ])

    // All columns for reference
    const allColumns = computed(() => columns.value)

    // Visible columns based on visibility settings
    const visibleColumns = computed(() => {
      return columns.value.filter(column => columnVisibility.value[column.key])
    })

    const initializeColumnVisibility = () => {
      const savedVisibility = localStorage.getItem('productColumnVisibility')
      if (savedVisibility) {
        columnVisibility.value = JSON.parse(savedVisibility)
      } else {
        // Save default visibility if no saved settings
        saveColumnVisibility()
      }
    }

    const saveColumnVisibility = () => {
      localStorage.setItem('productColumnVisibility', JSON.stringify(columnVisibility.value))
    }

    const showColumnSelector = ref(false)

    const openColumnSelector = () => {
      tempColumnVisibility.value = { ...columnVisibility.value }
      showColumnSelector.value = true
    }

    const applyColumnVisibility = () => {
      columnVisibility.value = { ...tempColumnVisibility.value }
      saveColumnVisibility()
      showColumnSelector.value = false
    }

    const resetColumnVisibility = () => {
      // Reset to the minimal/simple default set
      columnVisibility.value = {
        image: true,
        sellingPrice: true,
        stock: true,
        availableStock: true,
        status: true,
        actions: true
      }
      saveColumnVisibility()
      showColumnSelector.value = false
    }

    const onVisibleColumnsUpdate = (visibleCols) => {
      // visibleCols is an array of column objects from the child component
      const map = {}
      columns.value.forEach(col => {
        map[col.key] = !!visibleCols.find(vc => vc && vc.key === col.key)
      })
      columnVisibility.value = map
      saveColumnVisibility()
    }

    // Initialize on component mount
    onMounted(() => {
      initializeColumnVisibility()
      fetchProductsWithInventory()
        fetchCategories()
      console.log('Products component mounted')
    })

    // Status options for filter
    const statusOptions = ref([
      { value: 'ACTIVE', label: 'Hoạt động' },
      { value: 'INACTIVE', label: 'Không hoạt động' },
      { value: 'OUT_OF_STOCK', label: 'Hết hàng' },
      { value: 'DISCONTINUED', label: 'Ngừng bán' }
    ])


    // Trong setup()
    const columnFilters = ref({ image: '', brand: '', model: '' })

    // Computed properties
    const filteredProducts = computed(() => {
      return products.value.filter(item => {
        // Tên sản phẩm (image key)
        const nameMatch = !columnFilters.value.image || (item.name && item.name.toLowerCase().includes(columnFilters.value.image.toLowerCase()))
        // Thương hiệu
        const brandMatch = !columnFilters.value.brand || (item.brand && item.brand.toLowerCase().includes(columnFilters.value.brand.toLowerCase()))
        // Model
        const modelMatch = !columnFilters.value.model || (item.model && item.model.toLowerCase().includes(columnFilters.value.model.toLowerCase()))
        return nameMatch && brandMatch && modelMatch
      })
    })

    // API Methods
    const fetchProductsWithInventory = async () => {
      loading.value = true
      try {
        const response = await productsAPI.getAll()
        products.value = response.data.content ? response.data.content.map(product => ({
          ...product,
          stock: product.totalStock || 0,
          availableStock: product.availableStock || 0,
          reservedStock: product.reservedStock || 0,
          supplierCount: product.supplierCount || 0
        })) : []
      } catch (error) {
        console.error('Error fetching products with inventory:', error)
        // Fallback to basic products API
        try {
          const basicResponse = await productsAPI.getAll()
          products.value = basicResponse.data.content ? basicResponse.data.content.map(product => ({
            ...product,
            stock: 0,
            availableStock: 0,
            reservedStock: 0,
            supplierCount: 0
          })) : []
        } catch (fallbackError) {
          console.error('Error fetching basic products:', fallbackError)
        }
      } finally {
        loading.value = false
      }
    }


    const formatPrice = (price) => {
      if (!price) return 'N/A'
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(price)
    }

    // Methods
    const handleRowClick = (product) => {
      productDetailModalRef.value?.open?.(product)
    }

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
      // mở AddProductModal qua ref
      addProductModalRef.value?.open?.()
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
      // Parent-level export handling — DataTable also supports export if used there
    }

    const handleImportedRows = async (rows) => {
      try {
        console.log('Imported rows (parsed CSV):', rows)
        alert(`Imported ${rows.length} rows (client parsed).`)
        // Optionally: send rows to backend via productsAPI if an endpoint exists
        if (productsAPI && typeof productsAPI.importRows === 'function') {
          await productsAPI.importRows(rows)
          alert('Server import successful')
        }
        await fetchProductsWithInventory()
      } catch (err) {
        console.error('handleImportedRows failed', err)
        alert('Import failed: ' + (err?.message || err))
      }
    }

    const handleImportedFile = async (file) => {
      try {
        if (productsAPI && typeof productsAPI.import === 'function') {
          const form = new FormData()
          form.append('file', file)
          await productsAPI.import(form)
          alert('Server import successful')
          await fetchProductsWithInventory()
        } else {
          console.log('Imported file (parent should handle):', file)
          alert('File received. Implement server upload in productsAPI.import to process it.')
        }
      } catch (err) {
        console.error('handleImportedFile failed', err)
        alert('Import failed: ' + (err?.message || err))
      }
    }

    const duplicateProduct = (product) => {
      console.log('Duplicate product:', product)
      // Logic to duplicate product
    }

    const viewHistory = (product) => {
      console.log('View history for product:', product)
      // Logic to view product history
    }

    const addComponentToCombo = () => {
      const newComponent = {
        id: Date.now(), // Simple unique ID
        childProduct: null,
        quantity: 1,
        bundlePrice: 0,
        isSubstitutable: false
      }
      comboComponents.value.push(newComponent)
      // Optionally, you can pre-select a product for the new component
      // newComponent.childProduct = { id: 1, name: 'Lọc dầu động cơ', sku: 'OIL-FILTER-001', price: 120000, costPrice: 100000, unit: 'Cái' }
    }

    const editComponent = (component) => {
      console.log('Edit component:', component)
      // Logic to edit component
    }

    const removeComponent = (component) => {
      if (confirm(`Bạn có chắc chắn muốn xóa linh kiện "${component.childProduct?.name || 'N/A'}" khỏi combo?`)) {
        comboComponents.value = comboComponents.value.filter(c => c.id !== component.id)
        comboTotalPrice.value = comboComponents.value.reduce((sum, c) => sum + c.bundlePrice * c.quantity, 0)
      }
    }

    const showSubstitutes = (component) => {
      console.log('Show substitutes for component:', component)
      // Logic to show substitutes for a component
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
      // mở modal tồn kho qua ref
      productStockModalRef.value?.open?.(product)
    }

    const showStockDetails = (product) => {
      productStockModalRef.value?.open?.(product)
    }

    // Lifecycle (initialization handled earlier)

    // modals now manage their own escape handling
    onUnmounted(() => {
      // ensure no leaked listeners if components didn't clean up
      try { window.removeEventListener('keydown', handleEscClose) } catch (e) {}
    })

    function handleEscClose() { /* placeholder for safety cleanup */ }

    return {
      // Data
      searchQuery,
      selectedCategory,
      selectedSupplier,
      products,
      categories,
      suppliers,
      columnVisibility,
      tempColumnVisibility,
      comboComponents,
      allColumns,
      visibleColumns,
      showColumnSelector,
      openColumnSelector,
      applyColumnVisibility,
      resetColumnVisibility,
  // modal refs
  productStockModalRef,
  productDetailModalRef,
  addProductModalRef,
  comboManagementModalRef,
  selectedProductForTraining,
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

      fetchProductsWithInventory,

      formatCurrency,
      formatPrice,


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
  selectProductForTraining,
  handleRowClick,
  showStockDetails,

      // Column filters
      columnFilters,
      filteredProducts
      ,
      // Import handlers from DataTable
      handleImportedRows,
      handleImportedFile
      ,
      // Column visibility callback from child
      onVisibleColumnsUpdate
    }
  }
}
</script>
