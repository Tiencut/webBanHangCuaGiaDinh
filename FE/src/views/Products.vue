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
          <TrainingAssistant 
            :selected-product="selectedProductForTraining"
            @search-suggestion="handleSearchSuggestion"
            @help-request="handleHelpRequest"
          />
        </div>
      </div>

      <!-- Column Visibility Controls -->
      <ColumnVisibilityControls
        :visible-columns="visibleColumns"
        :all-columns="allColumns"
        :show-column-selector="showColumnSelector"
        @open-column-selector="openColumnSelector"
        @reset-column-visibility="resetColumnVisibility"
      />

      <!-- DataTable -->
      <DataTable
        :data="filteredProducts"
        :columns="visibleColumns"
        :loading="loading"
        :categories="categories"
        :status-options="statusOptions"
        :show-export="true"
        :column-filters="columnFilters"
        @update:columnFilters="val => columnFilters = val"
        @create="handleCreate"
        @edit="handleEdit"
        @delete="handleDelete"
        @bulk-action="handleBulkAction"
        @export="handleExport"
        @row-click="showProductDetail"
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
            <button
              @click="showStockDetails(item)"
              class="text-blue-600 hover:text-blue-900 transition-colors"
              title="Xem chi tiết tồn kho"
            >
              <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </button>
            
            <!-- Combo management button -->
            <button
              v-if="item.isCombo"
              @click="manageCombo(item)"
              class="text-purple-600 hover:text-purple-900 transition-colors"
              title="Quản lý combo"
            >
              <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
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
                        {{ formatCurrency(inventory.costPrice || 0) }}
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
          <div class="mt-8">
            <h4 class="font-semibold text-lg mb-2">Lịch sử giao dịch kho</h4>
            <ProductInventoryHistory :product-id="selectedProductForStock?.id" />
          </div>
          <div class="mb-8">
            <h4 class="font-semibold text-lg mb-2">Giá nhập/bán theo từng nhà cung cấp</h4>
            <ProductSuppliersTable :product-id="selectedProductForStock?.id" />
          </div>
          <div class="mb-8">
            <h4 class="font-semibold text-lg mb-2">Thông số kỹ thuật</h4>
            <ProductDynamicAttributes :product-id="selectedProductForStock?.id" :category-id="selectedProductForStock?.categoryId" />
          </div>
        </div>
      </div>

      <!-- Product Detail Modal -->
      <ProductDetailModal
        ref="productDetailModalRef"
        @close="showProductDetailModal = false"
      />

      <!-- Add Product Modal -->
      <AddProductModal
        ref="addProductModalRef"
        @close="showAddProductModal = false"
        @success="handleAddProductSuccess"
      />

      <!-- Combo Management Modal -->
      <ComboManagementModal
        ref="comboManagementModalRef"
        @close="showComboModal = false"
        @success="handleComboManagementSuccess"
      />
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch, onUnmounted } from 'vue'
import { useProductsStore } from '../stores/products'
import { formatCurrency, debounce } from '../utils/helpers'
import { productsAPI, inventoryAPI } from '@/services'
import TrainingAssistant from '@/components/TrainingAssistant.vue'
import DataTable from '@/components/DataTable.vue'
import { removeVietnameseTones } from '../utils/removeVietnameseTones'
import ProductInventoryHistory from '../components/ProductInventoryHistory.vue'
import ProductSuppliersTable from '../components/ProductSuppliersTable.vue'
import ProductDynamicAttributes from '../components/ProductDynamicAttributes.vue'

export default {
  name: 'Products',
  components: {
    TrainingAssistant,
    DataTable,
    ProductInventoryHistory,
    ProductSuppliersTable,
    ProductDynamicAttributes
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
    const showProductDetailModal = ref(false)
    const selectedProductDetail = ref(null)
    const activeTab = ref('info')
    const loading = ref(false)
    
    
    // Column visibility management

    const newProduct = ref({
      name: '',
      sku: '',
      brand: '',
      model: '',
      partNumber: '',
      vehicleType: '',
      categoryId: '',
      supplierId: '',
      basePrice: 0,
      sellingPrice: 0,
      costPrice: 0,
      warrantyPeriod: 0,
      weight: 0,
      dimensions: '',
      status: 'ACTIVE',
      description: '',
      specifications: '',
      minStockLevel: 0,
      reorderPoint: 10,
      location: '',
      isCombo: false
)
    
    // Column definitions for DataTable
    const columns = ref([
      {
        key: 'image',
        label: 'Sản phẩm',
        sortable: false,
        width: '280px'
  ,
      {
        key: 'brand',
        label: 'Thương hiệu',
        sortable: true,
        width: '120px'
  ,
      {
        key: 'model',
        label: 'Model',
        sortable: true,
        width: '120px'
  ,
      {
        key: 'partNumber',
        label: 'Mã phụ tùng',
        sortable: true,
        width: '130px'
  ,
      {
        key: 'vehicleType',
        label: 'Loại xe',
        sortable: true,
        width: '120px'
  ,
      {
        key: 'category.name',
        label: 'Danh mục',
        sortable: true,
        width: '150px'
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
        key: 
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
    
    











    



    }
    


    }
    




    }
    




    }
    



    }
    




    }
    





    }
    
    // Initialize on component mount
    onMounted(() => {
      initializeColumnVisibility()
      // Copy current visibility to temp for editing
      tempColumnVisibility.value = { ...columnVisibility.value }
    })

    // Status options for filter
    const statusOptions = ref([
      { value: 'ACTIVE', label: 'Hoạt động' },
      { value: 'INACTIVE', label: 'Không hoạt động' },
      { value: 'OUT_OF_STOCK', label: 'Hết hàng' },
      { value: 'DISCONTINUED', label: 'Ngừng bán' }
    ])
    
    // Mock data - sẽ thay thế bằng API calls
    const products = ref([
      {
        id: 1,
        name: 'Lọc dầu động cơ Isuzu 4JB1',
        sku: 'OIL-FILTER-001',
        brand: 'Isuzu',
        model: '4JB1',
        partNumber: 'ISU-4JB1-OIL-001',
        vehicleType: 'Xe tải nhẹ',
        categoryId: 1,
        category: { id: 1, name: 'Hệ thống động cơ' },
        supplierId: 1,
        supplier: { id: 1, name: 'Công ty TNHH ABC' },
        basePrice: 140000,
        sellingPrice: 150000,
        costPrice: 120000,
        stock: 50,
        availableStock: 45,
        reservedStock: 5,
        minStockLevel: 10,
        reorderPoint: 15,
        supplierCount: 2,
        warrantyPeriod: 12,
        weight: 0.8,
        dimensions: '12x8x6 cm',
        status: 'ACTIVE',
        imageUrl: null,
        supplierStocks: [
          { supplierId: 1, quantity: 30 },
          { supplierId: 2, quantity: 20 }
        ],
        isCombo: false,
        createdAt: '2024-01-15T10:00:00'
      },
      {
        id: 2,
        name: 'Má phanh trước Hyundai HD72',
        sku: 'BRAKE-PAD-002',
        brand: 'Hyundai',
        model: 'HD72',
        partNumber: 'HYU-HD72-BRAKE-002',
        vehicleType: 'Xe tải trung',
        categoryId: 2,
        category: { id: 2, name: 'Hệ thống phanh' },
        supplierId: 2,
        supplier: { id: 2, name: 'Nhà phân phối XYZ' },
        basePrice: 320000,
        sellingPrice: 350000,
        costPrice: 280000,
        stock: 5,
        availableStock: 3,
        reservedStock: 2,
        minStockLevel: 5,
        reorderPoint: 8,
        supplierCount: 1,
        warrantyPeriod: 6,
        weight: 2.5,
        dimensions: '15x12x3 cm',
        status: 'ACTIVE',
        imageUrl: null,
        supplierStocks: [
          { supplierId: 2, quantity: 5 }
        ],
        isCombo: false,
        createdAt: '2024-01-20T14:30:00'
      },
      {
        id: 3,
        name: 'Lốp xe 900R20 Michelin',
        sku: 'TIRE-003',
        brand: 'Michelin',
        model: '900R20',
        partNumber: 'MIC-900R20-TIRE-003',
        vehicleType: 'Xe tải nặng',
        categoryId: 3,
        category: { id: 3, name: 'Lốp xe' },
        supplierId: 1,
        supplier: { id: 1, name: 'Công ty TNHH ABC' },
        basePrice: 2400000,
        sellingPrice: 2500000,
        costPrice: 2200000,
        stock: 0,
        availableStock: 0,
        reservedStock: 0,
        minStockLevel: 4,
        reorderPoint: 6,
        supplierCount: 1,
        warrantyPeriod: 24,
        weight: 45.0,
        dimensions: '90x20 cm',
        status: 'OUT_OF_STOCK',
        imageUrl: null,
        supplierStocks: [],
        isCombo: false,
        createdAt: '2024-01-10T09:15:00'
      },
      {
        id: 4,
        name: 'Combo lọc dầu + má phanh Thaco Ollin',
        sku: 'OIL-BRAKE-004',
        brand: 'Thaco',
        model: 'Ollin',
        partNumber: 'THA-OLLIN-COMBO-004',
        vehicleType: 'Xe tải nhẹ',
        categoryId: 1,
        category: { id: 1, name: 'Hệ thống động cơ' },
        supplierId: 1,
        supplier: { id: 1, name: 'Công ty TNHH ABC' },
        basePrice: 200000,
        sellingPrice: 220000,
        costPrice: 180000,
        stock: 10,
        availableStock: 8,
        reservedStock: 2,
        minStockLevel: 5,
        reorderPoint: 8,
        supplierCount: 1,
        warrantyPeriod: 12,
        weight: 3.2,
        dimensions: '20x15x8 cm',
        status: 'ACTIVE',
        imageUrl: null,
        supplierStocks: [
          { supplierId: 1, quantity: 10 }
        ],
        isCombo: true,
        createdAt: '2024-01-25T16:45:00'
      },
      {
        id: 5,
        name: 'Hộp số 5 cấp Isuzu',
        sku: 'GEARBOX-005',
        brand: 'Isuzu',
        model: '5-Speed',
        partNumber: 'ISU-5SPD-GEAR-005',
        vehicleType: 'Xe tải nhẹ',
        categoryId: 4,
        category: { id: 4, name: 'Hệ thống truyền động' },
        supplierId: 2,
        supplier: { id: 2, name: 'Nhà phân phối XYZ' },
        basePrice: 8500000,
        sellingPrice: 9000000,
        costPrice: 7500000,
        stock: 2,
        availableStock: 1,
        reservedStock: 1,
        minStockLevel: 1,
        reorderPoint: 2,
        supplierCount: 1,
        warrantyPeriod: 36,
        weight: 85.0,
        dimensions: '60x40x35 cm',
        status: 'ACTIVE',
        imageUrl: null,
        supplierStocks: [
          { supplierId: 2, quantity: 2 }
        ],
        isCombo: false,
        createdAt: '2024-02-01T11:20:00'
      },
      {
        id: 6,
        name: 'Bộ lọc gió động cơ',
        sku: 'AIR-FILTER-006',
        brand: 'Mann',
        model: 'C25114',
        partNumber: 'MAN-C25114-AIR-006',
        vehicleType: 'Xe tải nhẹ',
        categoryId: 1,
        category: { id: 1, name: 'Hệ thống động cơ' },
        supplierId: 1,
        supplier: { id: 1, name: 'Công ty TNHH ABC' },
        basePrice: 85000,
        sellingPrice: 95000,
        costPrice: 70000,
        stock: 25,
        availableStock: 22,
        reservedStock: 3,
        minStockLevel: 5,
        reorderPoint: 8,
        supplierCount: 1,
        warrantyPeriod: 6,
        weight: 0.5,
        dimensions: '15x12x8 cm',
        status: 'ACTIVE',
        imageUrl: null,
        supplierStocks: [
          { supplierId: 1, quantity: 25 }
        ],
        isCombo: false,
        createdAt: '2024-02-05T13:30:00'
      },
      {
        id: 7,
        name: 'Dây đai động cơ',
        sku: 'BELT-007',
        brand: 'Gates',
        model: '13x1000',
        partNumber: 'GAT-13X1000-BELT-007',
        vehicleType: 'Xe tải trung',
        categoryId: 1,
        category: { id: 1, name: 'Hệ thống động cơ' },
        supplierId: 2,
        supplier: { id: 2, name: 'Nhà phân phối XYZ' },
        basePrice: 120000,
        sellingPrice: 135000,
        costPrice: 100000,
        stock: 15,
        availableStock: 12,
        reservedStock: 3,
        minStockLevel: 3,
        reorderPoint: 5,
        supplierCount: 1,
        warrantyPeriod: 12,
        weight: 0.3,
        dimensions: '100x2 cm',
        status: 'ACTIVE',
        imageUrl: null,
        supplierStocks: [
          { supplierId: 2, quantity: 15 }
        ],
        isCombo: false,
        createdAt: '2024-02-10T08:45:00'
      },
      {
        id: 8,
        name: 'Bugi đánh lửa',
        sku: 'SPARK-PLUG-008',
        brand: 'NGK',
        model: 'BPR6ES',
        partNumber: 'NGK-BPR6ES-SPARK-008',
        vehicleType: 'Xe tải nhẹ',
        categoryId: 1,
        category: { id: 1, name: 'Hệ thống động cơ' },
        supplierId: 1,
        supplier: { id: 1, name: 'Công ty TNHH ABC' },
        basePrice: 45000,
        sellingPrice: 55000,
        costPrice: 35000,
        stock: 100,
        availableStock: 85,
        reservedStock: 15,
        minStockLevel: 20,
        reorderPoint: 30,
        supplierCount: 3,
        warrantyPeriod: 12,
        weight: 0.1,
        dimensions: '2x1 cm',
        status: 'ACTIVE',
        imageUrl: null,
        supplierStocks: [
          { supplierId: 1, quantity: 50 },
          { supplierId: 3, quantity: 30 },
          { supplierId: 4, quantity: 20 }
        ],
        isCombo: false,
        createdAt: '2024-02-15T15:10:00'
      },
      {
        id: 9,
        name: 'Bộ lọc nhiên liệu cũ',
        sku: 'FUEL-FILTER-009',
        brand: 'Bosch',
        model: 'F026402001',
        partNumber: 'BOS-F026402001-FUEL-009',
        vehicleType: 'Xe tải trung',
        categoryId: 7,
        category: { id: 7, name: 'Hệ thống nhiên liệu' },
        supplierId: 2,
        supplier: { id: 2, name: 'Nhà phân phối XYZ' },
        basePrice: 180000,
        sellingPrice: 200000,
        costPrice: 150000,
        stock: 0,
        availableStock: 0,
        reservedStock: 0,
        minStockLevel: 5,
        reorderPoint: 8,
        supplierCount: 1,
        warrantyPeriod: 12,
        weight: 0.8,
        dimensions: '10x8x6 cm',
        status: 'INACTIVE',
        imageUrl: null,
        supplierStocks: [],
        isCombo: false,
        createdAt: '2024-01-05T12:00:00'
      }
    ])

    const categories = ref([
      { id: 1, name: 'Hệ thống động cơ' },
      { id: 2, name: 'Hệ thống phanh' },
      { id: 3, name: 'Lốp xe' },
      { id: 4, name: 'Hệ thống truyền động' },
      { id: 5, name: 'Hệ thống điện' },
      { id: 6, name: 'Hệ thống làm mát' },
      { id: 7, name: 'Hệ thống nhiên liệu' },
      { id: 8, name: 'Phụ kiện' }
    ])

    const suppliers = ref([
      { id: 1, name: 'Công ty TNHH ABC' },
      { id: 2, name: 'Nhà phân phối XYZ' },
      { id: 3, name: 'Công ty Phụ tùng Đông Nam' },
      { id: 4, name: 'Nhà phân phối Phương Nam' }
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
        products.value = response.data.content.map(product => ({
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
          products.value = basicResponse.data.content.map(product => ({
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

    const showProductDetail = (product) => {
      selectedProductDetail.value = product
      activeTab.value = 'info'
      showProductDetailModal.value = true
    }

    const formatPrice = (price) => {
      if (!price) return 'N/A'
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(price)
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

    const createProduct = async () => {
      try {
        await productsAPI.create(newProduct.value)
        showAddModal.value = false
        resetNewProduct()
        fetchProductsWithInventory() // reload list
      } catch (error) {
        console.error('Error creating product:', error)
        alert('Lỗi khi thêm sản phẩm')
      }
    }

    const resetNewProduct = () => {
      newProduct.value = {
        name: '',
        sku: '',
        categoryId: '',
        supplierId: '',
        price: 0,
        costPrice: 0,
        unit: '',
        status: 'ACTIVE',
        description: '',
        specifications: '',
        minStock: 0,
        maxStock: 0,
        location: '',
        isCombo: false
      }
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

    const manageCombo = (product) => {
      console.log('Manage combo for product:', product)
      selectedComboProduct.value = product
      comboComponents.value = []
      comboTotalPrice.value = 0
      if (product.isCombo) {
        product.components.forEach(component => {
          comboComponents.value.push({
            id: component.id,
            childProduct: component.childProduct,
            quantity: component.quantity,
            bundlePrice: component.bundlePrice,
            isSubstitutable: component.isSubstitutable
          })
          comboTotalPrice.value += component.bundlePrice * component.quantity
        })
      }
      showComboModal.value = true
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
      selectedProductForStock.value = product;
      showStockModal.value = true;
    }

    // Lifecycle
    onMounted(() => {
      // Load data when component mounts
      fetchProductsWithInventory()
      console.log('Products component mounted')
    })

    watch(showStockModal, (val) => {
      if (val) {
        window.addEventListener('keydown', handleEscClose)
      } else {
        window.removeEventListener('keydown', handleEscClose)
      }
    })

    watch(showProductDetailModal, (val) => {
      if (val) {
        window.addEventListener('keydown', handleEscClose)
      } else {
        window.removeEventListener('keydown', handleEscClose)
      }
    })

    function handleEscClose(e) {
      if (e.key === 'Escape') {
        showStockModal.value = false
        showProductDetailModal.value = false
      }
    }

    onUnmounted(() => {
      window.removeEventListener('keydown', handleEscClose)
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
      showProductDetailModal,
      selectedProductDetail,
      activeTab,
      loading,
      newProduct,
      showComboModal,
      selectedComboProduct,
      comboComponents,
      comboTotalPrice,
      
      // Column visibility
      showColumnSelector,
      allColumns,
      visibleColumns,
      
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
      showProductDetail,
      fetchProductsWithInventory,
      fetchProductInventory,
      formatCurrency,
      formatPrice,
      createProduct,
      resetNewProduct,
      
      // DataTable handlers
      handleCreate,
      handleEdit,
      handleDelete,
      handleBulkAction,
      handleExport,
      duplicateProduct,
      viewHistory,
      manageCombo,
      addComponentToCombo,
      editComponent,
      removeComponent,
      showSubstitutes,
      
      // Training Assistant
      handleSearchSuggestion,
      handleHelpRequest,
      selectProductForTraining,
      
      // Column visibility methods
      isColumnVisible,
      toggleColumnVisibility,
      selectAllColumns,
      deselectAllColumns,
      applyColumnVisibility,
      openColumnSelector,
      resetColumnVisibility,

      // Column filters
      columnFilters,
      filteredProducts
    }
  }
}
</script>
