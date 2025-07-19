<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
    <div class="py-6">
      <!-- Header -->
      <div class="md:flex md:items-center md:justify-between">
        <div class="flex-1 min-w-0">
          <h2 class="text-2xl font-bold leading-7 text-gray-900 sm:text-3xl sm:truncate">
            📦 Kiểm kho
          </h2>
          <p class="mt-1 text-sm text-gray-500">
            Quản lý và kiểm tra tồn kho sản phẩm
          </p>
        </div>
        <div class="mt-4 flex md:mt-0 md:ml-4">
          <button
            class="bg-[#0070F4] hover:bg-[#0056b3] text-white px-4 py-2 rounded-lg shadow-sm transition-colors duration-200 flex items-center">
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            Tạo phiếu kiểm kho
          </button>
        </div>
      </div>

      <!-- Quick Stats -->
      <div class="mt-8 grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
        <div class="bg-white overflow-hidden shadow rounded-lg">
          <div class="p-5">
            <div class="flex items-center">
              <div class="flex-shrink-0">
                <svg class="h-6 w-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                </svg>
              </div>
              <div class="ml-5 w-0 flex-1">
                <dl>
                  <dt class="text-sm font-medium text-gray-500 truncate">Tổng sản phẩm</dt>
                  <dd class="text-lg font-medium text-gray-900">1,247</dd>
                </dl>
              </div>
            </div>
          </div>
        </div>

        <div class="bg-white overflow-hidden shadow rounded-lg">
          <div class="p-5">
            <div class="flex items-center">
              <div class="flex-shrink-0">
                <svg class="h-6 w-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z" />
                </svg>
              </div>
              <div class="ml-5 w-0 flex-1">
                <dl>
                  <dt class="text-sm font-medium text-gray-500 truncate">Sắp hết hàng</dt>
                  <dd class="text-lg font-medium text-gray-900">23</dd>
                </dl>
              </div>
            </div>
          </div>
        </div>

        <div class="bg-white overflow-hidden shadow rounded-lg">
          <div class="p-5">
            <div class="flex items-center">
              <div class="flex-shrink-0">
                <svg class="h-6 w-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M6 18L18 6M6 6l12 12" />
                </svg>
              </div>
              <div class="ml-5 w-0 flex-1">
                <dl>
                  <dt class="text-sm font-medium text-gray-500 truncate">Hết hàng</dt>
                  <dd class="text-lg font-medium text-gray-900">5</dd>
                </dl>
              </div>
            </div>
          </div>
        </div>

        <div class="bg-white overflow-hidden shadow rounded-lg">
          <div class="p-5">
            <div class="flex items-center">
              <div class="flex-shrink-0">
                <svg class="h-6 w-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
              <div class="ml-5 w-0 flex-1">
                <dl>
                  <dt class="text-sm font-medium text-gray-500 truncate">Kiểm tra hôm nay</dt>
                  <dd class="text-lg font-medium text-gray-900">12</dd>
                </dl>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Recent Inventory Checks -->
      <div class="mt-8">
        <div class="bg-white shadow overflow-hidden sm:rounded-md">
          <div class="px-4 py-5 sm:px-6">
            <h3 class="text-lg leading-6 font-medium text-gray-900">
              Phiếu kiểm kho gần đây
            </h3>
            <p class="mt-1 max-w-2xl text-sm text-gray-500">
              Danh sách các phiếu kiểm kho đã thực hiện
            </p>
          </div>
          <ul class="divide-y divide-gray-200">
            <li class="px-4 py-4 sm:px-6 hover:bg-gray-50">
              <div class="flex items-center justify-between">
                <div class="flex items-center">
                  <div class="flex-shrink-0">
                    <div class="h-10 w-10 bg-blue-100 rounded-full flex items-center justify-center">
                      <svg class="h-5 w-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M9 5H7a2 2 0 00-2 2v6a2 2 0 002 2h2m0 0h2a2 2 0 002-2V7a2 2 0 00-2-2H9m0 0V3a2 2 0 012-2h2a2 2 0 012 2v2" />
                      </svg>
                    </div>
                  </div>
                  <div class="ml-4">
                    <div class="text-sm font-medium text-gray-900">
                      Phiếu kiểm kho #KK001
                    </div>
                    <div class="text-sm text-gray-500">
                      Kiểm tra kho A - Phụ tùng xe tải
                    </div>
                  </div>
                </div>
                <div class="flex items-center">
                  <div class="text-sm text-gray-500 mr-4">
                    <div>2025-01-05</div>
                    <div class="text-xs">09:30</div>
                  </div>
                  <span class="px-2 py-1 text-xs font-semibold bg-green-100 text-green-800 rounded-full">
                    Hoàn thành
                  </span>
                </div>
              </div>
            </li>
            <li class="px-4 py-4 sm:px-6 hover:bg-gray-50">
              <div class="flex items-center justify-between">
                <div class="flex items-center">
                  <div class="flex-shrink-0">
                    <div class="h-10 w-10 bg-yellow-100 rounded-full flex items-center justify-center">
                      <svg class="h-5 w-5 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                      </svg>
                    </div>
                  </div>
                  <div class="ml-4">
                    <div class="text-sm font-medium text-gray-900">
                      Phiếu kiểm kho #KK002
                    </div>
                    <div class="text-sm text-gray-500">
                      Kiểm tra kho B - Phụ tùng xe con
                    </div>
                  </div>
                </div>
                <div class="flex items-center">
                  <div class="text-sm text-gray-500 mr-4">
                    <div>2025-01-05</div>
                    <div class="text-xs">14:15</div>
                  </div>
                  <span class="px-2 py-1 text-xs font-semibold bg-yellow-100 text-yellow-800 rounded-full">
                    Đang kiểm tra
                  </span>
                </div>
              </div>
            </li>
            <li class="px-4 py-4 sm:px-6 hover:bg-gray-50">
              <div class="flex items-center justify-between">
                <div class="flex items-center">
                  <div class="flex-shrink-0">
                    <div class="h-10 w-10 bg-red-100 rounded-full flex items-center justify-center">
                      <svg class="h-5 w-5 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z" />
                      </svg>
                    </div>
                  </div>
                  <div class="ml-4">
                    <div class="text-sm font-medium text-gray-900">
                      Phiếu kiểm kho #KK003
                    </div>
                    <div class="text-sm text-gray-500">
                      Kiểm tra kho C - Phụ tùng xe máy
                    </div>
                  </div>
                </div>
                <div class="flex items-center">
                  <div class="text-sm text-gray-500 mr-4">
                    <div>2025-01-04</div>
                    <div class="text-xs">16:45</div>
                  </div>
                  <span class="px-2 py-1 text-xs font-semibold bg-red-100 text-red-800 rounded-full">
                    Có sai lệch
                  </span>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </div>

      <!-- Quick Actions -->
      <div class="mt-8 grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3">
        <div class="bg-white overflow-hidden shadow rounded-lg hover:shadow-md transition-shadow duration-200">
          <div class="p-5">
            <div class="flex items-center">
              <div class="flex-shrink-0">
                <svg class="h-6 w-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M9 5H7a2 2 0 00-2 2v6a2 2 0 002 2h2m0 0h2a2 2 0 002-2V7a2 2 0 00-2-2H9m0 0V3a2 2 0 012-2h2a2 2 0 012 2v2" />
                </svg>
              </div>
              <div class="ml-5 w-0 flex-1">
                <h3 class="text-lg font-medium text-gray-900">Kiểm kho định kỳ</h3>
                <p class="mt-1 text-sm text-gray-500">
                  Tạo phiếu kiểm kho theo lịch định kỳ
                </p>
              </div>
            </div>
            <div class="mt-4">
              <button class="w-full bg-blue-50 hover:bg-blue-100 text-blue-700 px-4 py-2 rounded-lg transition-colors duration-200">
                Tạo phiếu kiểm kho
              </button>
            </div>
          </div>
        </div>

        <div class="bg-white overflow-hidden shadow rounded-lg hover:shadow-md transition-shadow duration-200">
          <div class="p-5">
            <div class="flex items-center">
              <div class="flex-shrink-0">
                <svg class="h-6 w-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z" />
                </svg>
              </div>
              <div class="ml-5 w-0 flex-1">
                <h3 class="text-lg font-medium text-gray-900">Cảnh báo tồn kho</h3>
                <p class="mt-1 text-sm text-gray-500">
                  Xem danh sách sản phẩm sắp hết hàng
                </p>
              </div>
            </div>
            <div class="mt-4">
              <button class="w-full bg-yellow-50 hover:bg-yellow-100 text-yellow-700 px-4 py-2 rounded-lg transition-colors duration-200">
                Xem cảnh báo
              </button>
            </div>
          </div>
        </div>

        <div class="bg-white overflow-hidden shadow rounded-lg hover:shadow-md transition-shadow duration-200">
          <div class="p-5">
            <div class="flex items-center">
              <div class="flex-shrink-0">
                <svg class="h-6 w-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M9 17v-2m3 2v-4m3 4v-6m2 10H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                </svg>
              </div>
              <div class="ml-5 w-0 flex-1">
                <h3 class="text-lg font-medium text-gray-900">Báo cáo tồn kho</h3>
                <p class="mt-1 text-sm text-gray-500">
                  Tạo báo cáo chi tiết về tình trạng kho
                </p>
              </div>
            </div>
            <div class="mt-4">
              <button class="w-full bg-green-50 hover:bg-green-100 text-green-700 px-4 py-2 rounded-lg transition-colors duration-200">
                Tạo báo cáo
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Bảng kiểm kê tồn kho -->
      <div class="mt-10 bg-white shadow rounded-lg p-6">
        <h3 class="text-lg font-semibold mb-4">Danh sách sản phẩm & tồn kho hiện tại</h3>
        <div v-if="loading" class="text-center py-8">
          <svg class="mx-auto h-8 w-8 text-gray-400 animate-spin" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          <p class="mt-2 text-sm text-gray-500">Đang tải dữ liệu sản phẩm...</p>
        </div>
        <div v-else-if="error" class="text-center text-red-600 py-8">{{ error }}</div>
        <div v-else>
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Tên sản phẩm</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Mã sản phẩm</th>
                <th class="px-4 py-2 text-center text-xs font-medium text-gray-500 uppercase">Tồn kho</th>
                <th class="px-4 py-2 text-center text-xs font-medium text-gray-500 uppercase">Trạng thái</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="product in products" :key="product.id">
                <td class="px-4 py-2">{{ product.name }}</td>
                <td class="px-4 py-2">{{ product.code || '-' }}</td>
                <td class="px-4 py-2 text-center">{{ product.stock ?? product.quantity ?? 0 }}</td>
                <td class="px-4 py-2 text-center">
                  <span :class="'px-2 py-1 rounded-full text-xs font-semibold ' + getStockStatus(product.stock ?? product.quantity ?? 0).color">
                    {{ getStockStatus(product.stock ?? product.quantity ?? 0).text }}
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
          <div v-if="products.length === 0" class="text-center text-gray-500 py-8">Không có sản phẩm nào.</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { inventoryApi } from '@/api'

export default {
  name: 'InventoryCheck',
  setup() {
    const loading = ref(false)
    const error = ref('')
    const stats = ref({
      totalProducts: 0,
      lowStock: 0,
      outOfStock: 0,
      checksToday: 0
    })
    const recentChecks = ref([])
    const showCreateCheckModal = ref(false)
    const newCheck = ref({
      name: '',
      description: '',
      warehouse: '',
      items: []
    })

    // Load inventory stats
    const loadStats = async () => {
      try {
        loading.value = true
        error.value = ''
        const response = await inventoryApi.getStats()
        stats.value = response.data
      } catch (err) {
        error.value = 'Lỗi khi tải thống kê tồn kho'
        console.error('Error loading inventory stats:', err)
      } finally {
        loading.value = false
      }
    }

    // Load recent inventory checks
    const loadRecentChecks = async () => {
      try {
        const response = await inventoryApi.getChecks(0, 10)
        recentChecks.value = response.data.content || []
      } catch (err) {
        console.error('Error loading recent checks:', err)
      }
    }

    // Create new inventory check
    const createCheck = async () => {
      try {
        await inventoryApi.createCheck(newCheck.value)
        showCreateCheckModal.value = false
        resetNewCheck()
        loadRecentChecks() // reload list
      } catch (err) {
        alert('Lỗi khi tạo phiếu kiểm kho')
        console.error('Error creating check:', err)
      }
    }

    // Reset new check form
    const resetNewCheck = () => {
      newCheck.value = {
        name: '',
        description: '',
        warehouse: '',
        items: []
      }
    }

    // Get status class
    const getStatusClass = (status) => {
      const classes = {
        'COMPLETED': 'bg-green-100 text-green-800',
        'IN_PROGRESS': 'bg-yellow-100 text-yellow-800',
        'DISCREPANCY': 'bg-red-100 text-red-800'
      }
      return classes[status] || 'bg-gray-100 text-gray-800'
    }

    // Get status text
    const getStatusText = (status) => {
      const texts = {
        'COMPLETED': 'Hoàn thành',
        'IN_PROGRESS': 'Đang kiểm tra',
        'DISCREPANCY': 'Có sai lệch'
      }
      return texts[status] || status
    }

    // Format date
    const formatDate = (dateString) => {
      return new Date(dateString).toLocaleDateString('vi-VN')
    }

    // Lifecycle
    onMounted(() => {
      loadStats()
      loadRecentChecks()
    })

    return {
      loading,
      error,
      stats,
      recentChecks,
      showCreateCheckModal,
      newCheck,
      createCheck,
      resetNewCheck,
      getStatusClass,
      getStatusText,
      formatDate
    }
  }
}
</script>

<style scoped>
/* Component specific styles */
</style>
