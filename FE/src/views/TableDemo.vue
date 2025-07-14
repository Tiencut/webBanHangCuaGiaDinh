<template>
  <div class="container mx-auto px-4 py-6">
    <div class="mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Demo Bảng Sản Phẩm</h1>
      <p class="text-gray-600 mt-1">Mẫu bảng với tính năng sorting theo phong cách KiotViet</p>
    </div>

    <!-- Demo DataTable -->
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
      @row-click="handleRowClick"
    >
      <!-- Custom cell cho hình ảnh sản phẩm -->
      <template #cell-image="{ item }">
        <div class="flex items-center">
          <div class="h-10 w-10 rounded-lg bg-gray-200 flex items-center justify-center mr-3">
            <img 
              v-if="item.imageUrl" 
              :src="item.imageUrl" 
              :alt="item.name"
              class="h-10 w-10 rounded-lg object-cover"
            />
            <svg v-else class="h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
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
        <div class="flex flex-col items-center">
          <span :class="getStockClass(item.stock)" class="px-2 py-1 text-xs font-medium rounded-full">
            {{ item.stock }}
          </span>
          <span class="text-xs text-gray-500 mt-1">{{ item.unit }}</span>
        </div>
      </template>

      <!-- Custom cell cho giá -->
      <template #cell-price="{ item }">
        <div class="text-right">
          <div class="font-medium text-gray-900">{{ formatCurrency(item.salePrice) }}</div>
          <div class="text-sm text-gray-500">{{ formatCurrency(item.costPrice) }}</div>
        </div>
      </template>

      <!-- Custom actions -->
      <template #actions="{ item }">
        <div class="flex items-center justify-end space-x-2">
          <button
            @click="duplicateItem(item)"
            class="text-green-600 hover:text-green-900 transition-colors"
            title="Nhân bản"
          >
            <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
            </svg>
          </button>
          <button
            @click="viewDetails(item)"
            class="text-blue-600 hover:text-blue-900 transition-colors"
            title="Xem chi tiết"
          >
            <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
            </svg>
          </button>
          <button
            @click="handleEdit(item)"
            class="text-orange-600 hover:text-orange-900 transition-colors"
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

    <!-- Action Panel -->
    <div class="mt-6 flex justify-between items-center">
      <div class="text-sm text-gray-600">
        <p>✅ Sorting: Click vào tiêu đề cột để sắp xếp</p>
        <p>✅ Filter: Tìm kiếm và lọc theo danh mục, trạng thái</p>
        <p>✅ Pagination: Phân trang và chọn số dòng hiển thị</p>
        <p>✅ Export: Xuất dữ liệu Excel</p>
        <p>✅ Bulk Action: Chọn nhiều dòng để thao tác</p>
      </div>
      <div class="flex space-x-2">
        <button
          @click="generateMoreData"
          class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
        >
          Tạo thêm dữ liệu
        </button>
        <button
          @click="clearData"
          class="px-4 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition-colors"
        >
          Xóa dữ liệu
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import DataTable from '@/components/DataTable.vue'
import { exportToExcel, formatVietnameseCurrency } from '@/utils/tableHelpers'

export default {
  name: 'TableDemo',
  components: {
    DataTable
  },
  setup() {
    const loading = ref(false)
    
    // Column definitions
    const columns = ref([
      {
        key: 'image',
        label: 'Sản phẩm',
        sortable: false,
        width: '250px'
      },
      {
        key: 'category.name',
        label: 'Danh mục',
        sortable: true,
        width: '140px'
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
        key: 'price',
        label: 'Giá bán / Giá vốn',
        sortable: true,
        align: 'right',
        width: '140px'
      },
      {
        key: 'status',
        label: 'Trạng thái',
        sortable: true,
        type: 'badge',
        align: 'center',
        width: '120px',
        badgeMap: {
          'ACTIVE': { text: 'Hoạt động', class: 'bg-green-100 text-green-800' },
          'OUT_OF_STOCK': { text: 'Hết hàng', class: 'bg-red-100 text-red-800' },
          'DISCONTINUED': { text: 'Ngừng bán', class: 'bg-gray-100 text-gray-800' }
        }
      },
      {
        key: 'createdAt',
        label: 'Ngày tạo',
        sortable: true,
        type: 'date',
        width: '120px'
      }
    ])

    // Status options
    const statusOptions = ref([
      { value: 'ACTIVE', label: 'Hoạt động' },
      { value: 'OUT_OF_STOCK', label: 'Hết hàng' },
      { value: 'DISCONTINUED', label: 'Ngừng bán' }
    ])

    // Categories
    const categories = ref([
      { id: 1, name: 'Hệ thống động cơ' },
      { id: 2, name: 'Hệ thống phanh' },
      { id: 3, name: 'Lốp xe' },
      { id: 4, name: 'Đèn chiếu sáng' },
      { id: 5, name: 'Phụ tùng khác' }
    ])

    // Sample data
    const products = ref([
      {
        id: 1,
        name: 'Lọc dầu động cơ Toyota',
        sku: 'OIL-FILTER-001',
        category: { id: 1, name: 'Hệ thống động cơ' },
        supplier: { id: 1, name: 'Công ty TNHH ABC' },
        stock: 50,
        unit: 'cái',
        costPrice: 120000,
        salePrice: 150000,
        status: 'ACTIVE',
        createdAt: '2024-01-15',
        imageUrl: null
      },
      {
        id: 2,
        name: 'Má phanh trước Hyundai',
        sku: 'BRAKE-PAD-002',
        category: { id: 2, name: 'Hệ thống phanh' },
        supplier: { id: 2, name: 'Nhà phân phối XYZ' },
        stock: 5,
        unit: 'bộ',
        costPrice: 280000,
        salePrice: 350000,
        status: 'ACTIVE',
        createdAt: '2024-01-20',
        imageUrl: null
      },
      {
        id: 3,
        name: 'Lốp xe 900R20 Bridgestone',
        sku: 'TIRE-003',
        category: { id: 3, name: 'Lốp xe' },
        supplier: { id: 1, name: 'Công ty TNHH ABC' },
        stock: 0,
        unit: 'cái',
        costPrice: 2200000,
        salePrice: 2500000,
        status: 'OUT_OF_STOCK',
        createdAt: '2024-01-10',
        imageUrl: null
      },
      {
        id: 4,
        name: 'Đèn pha LED 24V',
        sku: 'LED-LIGHT-004',
        category: { id: 4, name: 'Đèn chiếu sáng' },
        supplier: { id: 2, name: 'Nhà phân phối XYZ' },
        stock: 25,
        unit: 'cái',
        costPrice: 450000,
        salePrice: 580000,
        status: 'ACTIVE',
        createdAt: '2024-01-25',
        imageUrl: null
      },
      {
        id: 5,
        name: 'Gương chiếu hậu trái',
        sku: 'MIRROR-005',
        category: { id: 5, name: 'Phụ tùng khác' },
        supplier: { id: 1, name: 'Công ty TNHH ABC' },
        stock: 15,
        unit: 'cái',
        costPrice: 320000,
        salePrice: 420000,
        status: 'ACTIVE',
        createdAt: '2024-01-30',
        imageUrl: null
      }
    ])

    // Methods
    const getStockClass = (stock) => {
      if (stock === 0) return 'bg-red-100 text-red-800'
      if (stock < 10) return 'bg-yellow-100 text-yellow-800'
      return 'bg-green-100 text-green-800'
    }

    const formatCurrency = (amount) => {
      return formatVietnameseCurrency(amount)
    }

    const handleCreate = () => {
      alert('Tạo sản phẩm mới')
    }

    const handleEdit = (item) => {
      alert(`Chỉnh sửa sản phẩm: ${item.name}`)
    }

    const handleDelete = (item) => {
      if (confirm(`Bạn có chắc muốn xóa sản phẩm "${item.name}"?`)) {
        const index = products.value.findIndex(p => p.id === item.id)
        if (index > -1) {
          products.value.splice(index, 1)
        }
      }
    }

    const handleBulkAction = (selectedIds) => {
      alert(`Thao tác với ${selectedIds.length} sản phẩm đã chọn`)
    }

    const handleExport = (data) => {
      // Chuẩn bị dữ liệu export
      const exportData = data.map(item => ({
        'Tên sản phẩm': item.name,
        'Mã SKU': item.sku,
        'Danh mục': item.category?.name || '',
        'Nhà cung cấp': item.supplier?.name || '',
        'Tồn kho': item.stock,
        'Đơn vị': item.unit,
        'Giá vốn': item.costPrice,
        'Giá bán': item.salePrice,
        'Trạng thái': item.status,
        'Ngày tạo': item.createdAt
      }))
      
      exportToExcel(exportData, 'danh-sach-san-pham.xlsx')
      alert(`Đã xuất ${data.length} sản phẩm ra file Excel`)
    }

    const handleRowClick = (item) => {
      console.log('Row clicked:', item)
    }

    const duplicateItem = (item) => {
      const newItem = {
        ...item,
        id: Date.now(),
        name: `${item.name} (Copy)`,
        sku: `${item.sku}-COPY`
      }
      products.value.push(newItem)
      alert(`Đã nhân bản: ${item.name}`)
    }

    const viewDetails = (item) => {
      alert(`Xem chi tiết: ${item.name}`)
    }

    const generateMoreData = () => {
      const newProducts = [
        {
          id: Date.now() + 1,
          name: 'Lọc khí Toyota Hilux',
          sku: 'AIR-FILTER-006',
          category: { id: 1, name: 'Hệ thống động cơ' },
          supplier: { id: 1, name: 'Công ty TNHH ABC' },
          stock: 30,
          unit: 'cái',
          costPrice: 80000,
          salePrice: 120000,
          status: 'ACTIVE',
          createdAt: '2024-02-01',
          imageUrl: null
        },
        {
          id: Date.now() + 2,
          name: 'Đĩa phanh sau Mercedes',
          sku: 'BRAKE-DISC-007',
          category: { id: 2, name: 'Hệ thống phanh' },
          supplier: { id: 2, name: 'Nhà phân phối XYZ' },
          stock: 8,
          unit: 'cái',
          costPrice: 1200000,
          salePrice: 1500000,
          status: 'ACTIVE',
          createdAt: '2024-02-02',
          imageUrl: null
        },
        {
          id: Date.now() + 3,
          name: 'Lốp xe 11R22.5 Michelin',
          sku: 'TIRE-008',
          category: { id: 3, name: 'Lốp xe' },
          supplier: { id: 1, name: 'Công ty TNHH ABC' },
          stock: 12,
          unit: 'cái',
          costPrice: 3200000,
          salePrice: 3800000,
          status: 'ACTIVE',
          createdAt: '2024-02-03',
          imageUrl: null
        }
      ]
      
      products.value.push(...newProducts)
      alert('Đã tạo thêm 3 sản phẩm mới!')
    }

    const clearData = () => {
      if (confirm('Bạn có chắc muốn xóa tất cả dữ liệu?')) {
        products.value = []
        alert('Đã xóa tất cả dữ liệu!')
      }
    }

    onMounted(() => {
      console.log('Table Demo mounted')
    })

    return {
      loading,
      columns,
      statusOptions,
      categories,
      products,
      getStockClass,
      formatCurrency,
      handleCreate,
      handleEdit,
      handleDelete,
      handleBulkAction,
      handleExport,
      handleRowClick,
      duplicateItem,
      viewDetails,
      generateMoreData,
      clearData
    }
  }
}
</script>
