<template>
  <div class="container mx-auto px-4 py-6">
    <div class="mb-6">
      <h1 class="text-3xl font-bold text-gray-800">So sánh Bảng Truyền Thống vs DataTable</h1>
      <p class="text-gray-600 mt-1">Trải nghiệm sự khác biệt giữa bảng HTML thuần và DataTable component</p>
    </div>

    <!-- Tabs -->
    <div class="mb-6">
      <div class="border-b border-gray-200">
        <nav class="-mb-px flex space-x-8">
          <button
            @click="activeTab = 'old'"
            :class="[
              'py-2 px-1 border-b-2 font-medium text-sm',
              activeTab === 'old' 
                ? 'border-blue-500 text-blue-600' 
                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
            ]"
          >
            Bảng Truyền Thống
          </button>
          <button
            @click="activeTab = 'new'"
            :class="[
              'py-2 px-1 border-b-2 font-medium text-sm',
              activeTab === 'new' 
                ? 'border-blue-500 text-blue-600' 
                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
            ]"
          >
            DataTable Component
          </button>
        </nav>
      </div>
    </div>

    <!-- Old Style Table -->
    <div v-if="activeTab === 'old'" class="space-y-6">
      <div class="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
        <h2 class="text-xl font-semibold mb-4">Bảng HTML Truyền Thống</h2>
        
        <!-- Basic Search -->
        <div class="mb-4">
          <input
            v-model="searchQuery"
            type="text"
            placeholder="Tìm kiếm..."
            class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
        </div>

        <!-- Basic Table -->
        <div class="overflow-x-auto">
          <table class="w-full">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Sản phẩm
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Danh mục
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
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Thao tác
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in filteredOldData" :key="item.id">
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <div class="h-10 w-10 bg-gray-200 rounded-lg flex items-center justify-center mr-3">
                      <svg class="h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/>
                      </svg>
                    </div>
                    <div>
                      <div class="text-sm font-medium text-gray-900">{{ item.name }}</div>
                      <div class="text-sm text-gray-500">{{ item.sku }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ item.category?.name }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ item.stock }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ formatCurrency(item.salePrice) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="px-2 py-1 text-xs font-medium rounded-full bg-green-100 text-green-800">
                    {{ item.status }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <button class="text-blue-600 hover:text-blue-900 mr-3">Sửa</button>
                  <button class="text-red-600 hover:text-red-900">Xóa</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Limitations -->
        <div class="mt-6 p-4 bg-yellow-50 rounded-lg">
          <h3 class="font-medium text-yellow-800 mb-2">Hạn chế của bảng truyền thống:</h3>
          <ul class="text-sm text-yellow-700 space-y-1">
            <li>❌ Không có tính năng sorting</li>
            <li>❌ Không có pagination</li>
            <li>❌ Không có multi-select</li>
            <li>❌ Không có export Excel</li>
            <li>❌ Không có filter nâng cao</li>
            <li>❌ Không responsive tốt</li>
            <li>❌ Khó tùy biến và maintain</li>
          </ul>
        </div>
      </div>
    </div>

    <!-- New DataTable -->
    <div v-if="activeTab === 'new'" class="space-y-6">
      <div class="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
        <h2 class="text-xl font-semibold mb-4">DataTable Component Mới</h2>
        
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
                <svg class="h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
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
        </DataTable>

        <!-- Advantages -->
        <div class="mt-6 p-4 bg-green-50 rounded-lg">
          <h3 class="font-medium text-green-800 mb-2">Ưu điểm của DataTable mới:</h3>
          <ul class="text-sm text-green-700 space-y-1">
            <li>✅ Sorting đa cột (click vào header)</li>
            <li>✅ Pagination với tùy chọn số dòng</li>
            <li>✅ Multi-select với bulk actions</li>
            <li>✅ Export Excel/CSV</li>
            <li>✅ Filter nâng cao theo danh mục, trạng thái</li>
            <li>✅ Responsive hoàn toàn</li>
            <li>✅ Dễ tùy biến với slots</li>
            <li>✅ Loading states và empty states</li>
            <li>✅ Tự động format dữ liệu (currency, date, badge)</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed } from 'vue'
import DataTable from '@/components/DataTable.vue'

export default {
  name: 'TableComparison',
  components: {
    DataTable
  },
  setup() {
    const activeTab = ref('old')
    const searchQuery = ref('')
    const loading = ref(false)
    
    // Column definitions for new table
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
          'OUT_OF_STOCK': { text: 'Hết hàng', class: 'bg-red-100 text-red-800' }
        }
      }
    ])

    // Status options
    const statusOptions = ref([
      { value: 'ACTIVE', label: 'Hoạt động' },
      { value: 'OUT_OF_STOCK', label: 'Hết hàng' }
    ])

    // Categories
    const categories = ref([
      { id: 1, name: 'Hệ thống động cơ' },
      { id: 2, name: 'Hệ thống phanh' },
      { id: 3, name: 'Lốp xe' }
    ])

    // Sample data
    const products = ref([
      {
        id: 1,
        name: 'Lọc dầu động cơ Toyota',
        sku: 'OIL-FILTER-001',
        category: { id: 1, name: 'Hệ thống động cơ' },
        stock: 50,
        unit: 'cái',
        costPrice: 120000,
        salePrice: 150000,
        status: 'ACTIVE'
      },
      {
        id: 2,
        name: 'Má phanh trước Hyundai',
        sku: 'BRAKE-PAD-002',
        category: { id: 2, name: 'Hệ thống phanh' },
        stock: 5,
        unit: 'bộ',
        costPrice: 280000,
        salePrice: 350000,
        status: 'ACTIVE'
      },
      {
        id: 3,
        name: 'Lốp xe 900R20',
        sku: 'TIRE-003',
        category: { id: 3, name: 'Lốp xe' },
        stock: 0,
        unit: 'cái',
        costPrice: 2200000,
        salePrice: 2500000,
        status: 'OUT_OF_STOCK'
      }
    ])

    // Filtered data for old table
    const filteredOldData = computed(() => {
      if (!searchQuery.value) return products.value
      
      return products.value.filter(item => 
        item.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
        item.sku.toLowerCase().includes(searchQuery.value.toLowerCase())
      )
    })

    // Methods
    const getStockClass = (stock) => {
      if (stock === 0) return 'bg-red-100 text-red-800'
      if (stock < 10) return 'bg-yellow-100 text-yellow-800'
      return 'bg-green-100 text-green-800'
    }

    const formatCurrency = (amount) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(amount)
    }

    const handleCreate = () => {
      alert('Tạo sản phẩm mới')
    }

    const handleEdit = (item) => {
      alert(`Chỉnh sửa sản phẩm: ${item.name}`)
    }

    const handleDelete = (item) => {
      alert(`Xóa sản phẩm: ${item.name}`)
    }

    const handleBulkAction = (selectedIds) => {
      alert(`Thao tác với ${selectedIds.length} sản phẩm`)
    }

    const handleExport = (data) => {
      alert(`Xuất ${data.length} sản phẩm`)
    }

    const handleRowClick = (item) => {
      console.log('Row clicked:', item)
    }

    return {
      activeTab,
      searchQuery,
      loading,
      columns,
      statusOptions,
      categories,
      products,
      filteredOldData,
      getStockClass,
      formatCurrency,
      handleCreate,
      handleEdit,
      handleDelete,
      handleBulkAction,
      handleExport,
      handleRowClick
    }
  }
}
</script>
