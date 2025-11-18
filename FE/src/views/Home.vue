<template>
  <div class="w-full space-y-6">
    <!-- Quick Stats Cards (KiotViet Style) -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
      <div class="bg-white rounded-lg shadow-sm p-6 border border-gray-100">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium text-gray-600 mb-1">Tổng doanh thu</p>
            <p class="text-2xl font-bold text-blue-600">{{ formatCurrency(stats.totalRevenue) }}</p>
            <p class="text-xs text-green-600 mt-1" v-if="stats.revenueGrowth > 0">
              <span class="inline-flex items-center">
                <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M13 7h8m0 0v8m0-8l-8 8-4-4-4 4" />
                </svg>
                +{{ stats.revenueGrowth }}%
              </span>
              so với tháng trước
            </p>
          </div>
          <div class="h-12 w-12 bg-blue-100 rounded-full flex items-center justify-center">
            <svg class="h-6 w-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
            </svg>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow-sm p-6 border border-gray-100">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium text-gray-600 mb-1">Đơn hàng</p>
            <p class="text-2xl font-bold text-gray-900">{{ stats.totalOrders }}</p>
            <p class="text-xs text-green-600 mt-1" v-if="stats.orderGrowth > 0">
              <span class="inline-flex items-center">
                <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M13 7h8m0 0v8m0-8l-8 8-4-4-4 4" />
                </svg>
                +{{ stats.orderGrowth }}%
              </span>
              so với tháng trước
            </p>
          </div>
          <div class="h-12 w-12 bg-green-100 rounded-full flex items-center justify-center">
            <svg class="h-6 w-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow-sm p-6 border border-gray-100">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium text-gray-600 mb-1">Sản phẩm</p>
            <p class="text-2xl font-bold text-gray-900">{{ stats.totalProducts }}</p>
            <p class="text-xs text-gray-500 mt-1">Tổng số mặt hàng</p>
          </div>
          <div class="h-12 w-12 bg-purple-100 rounded-full flex items-center justify-center">
            <svg class="h-6 w-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
            </svg>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow-sm p-6 border border-gray-100">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium text-gray-600 mb-1">Khách hàng</p>
            <p class="text-2xl font-bold text-gray-900">{{ stats.totalCustomers }}</p>
            <p class="text-xs text-blue-600 mt-1" v-if="stats.customerGrowth > 0">
              <span class="inline-flex items-center">
                <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M13 7h8m0 0v8m0-8l-8 8-4-4-4 4" />
                </svg>
                +{{ stats.customerGrowth }}%
              </span>
              khách hàng mới
            </p>
          </div>
          <div class="h-12 w-12 bg-orange-100 rounded-full flex items-center justify-center">
            <svg class="h-6 w-6 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
            </svg>
          </div>
        </div>
      </div>
    </div>

    <!-- Quick Actions -->
    <div class="bg-white rounded-lg shadow-sm p-6 border border-gray-100 mb-8">
      <h2 class="text-lg font-semibold text-gray-900 mb-4">Thao tác nhanh</h2>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
        <router-link to="/products"
          class="flex flex-col items-center p-4 rounded-lg border border-gray-200 hover:border-green-300 hover:bg-green-50 transition-colors">
          <div class="h-12 w-12 bg-green-100 rounded-full flex items-center justify-center mb-3">
            <svg class="h-6 w-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
            </svg>
          </div>
          <span class="text-sm font-medium text-gray-900">Thêm sản phẩm</span>
        </router-link>

        <router-link to="/customers"
          class="flex flex-col items-center p-4 rounded-lg border border-gray-200 hover:border-purple-300 hover:bg-purple-50 transition-colors">
          <div class="h-12 w-12 bg-purple-100 rounded-full flex items-center justify-center mb-3">
            <svg class="h-6 w-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
          </div>
          <span class="text-sm font-medium text-gray-900">Thêm khách hàng</span>
        </router-link>

        <router-link to="/vehicles"
          class="flex flex-col items-center p-4 rounded-lg border border-gray-200 hover:border-orange-300 hover:bg-orange-50 transition-colors">
          <div class="h-12 w-12 bg-orange-100 rounded-full flex items-center justify-center mb-3">
            <svg class="h-6 w-6 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0z" />
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M13 6H4a2 2 0 00-2 2v6a2 2 0 002 2h1m8-10V4a2 2 0 00-2-2H4m7 4v10m0 0h6a2 2 0 002-2v-6a2 2 0 00-2-2h-6z" />
            </svg>
          </div>
          <span class="text-sm font-medium text-gray-900">Quản lý xe</span>
        </router-link>
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <!-- Recent Orders -->
      <div class="bg-white rounded-lg shadow-sm border border-gray-100">
        <div class="p-6 border-b border-gray-200">
          <div class="flex items-center justify-between">
            <h2 class="text-lg font-semibold text-gray-900">Đơn hàng gần đây</h2>
            <router-link to="/orders"
              class="flex flex-col items-center p-4 rounded-lg border border-gray-200 hover:border-blue-300 hover:bg-blue-50 transition-colors">
              <div class="h-12 w-12 bg-blue-100 rounded-full flex items-center justify-center mb-3">
                <svg class="h-6 w-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                </svg>
              </div>
            </router-link>
            <router-link to="/orders" class="text-sm text-blue-600 hover:text-blue-800 font-medium">
              Xem tất cả
            </router-link>
          </div>
        </div>
        <div class="p-6">
          <div v-if="loading" class="text-center py-8">
            <svg class="mx-auto h-8 w-8 text-gray-400 animate-spin" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            <p class="mt-2 text-sm text-gray-500">Đang tải đơn hàng...</p>
          </div>
          <div v-else-if="recentOrders.length === 0" class="text-center py-8">
            <svg class="mx-auto h-8 w-8 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
            <p class="mt-2 text-sm text-gray-500">Chưa có đơn hàng nào</p>
          </div>
          <div v-else class="space-y-4">
            <div v-for="order in recentOrders" :key="order.id"
              class="flex items-center justify-between p-4 border border-gray-100 rounded-lg hover:bg-gray-50 transition-colors">
              <div class="flex items-center space-x-4">
                <div class="h-10 w-10 bg-blue-100 rounded-full flex items-center justify-center">
                  <svg class="h-5 w-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                  </svg>
                </div>
                <div>
                  <div class="font-medium text-gray-900">#{{ order.orderNumber }}</div>
                  <div class="text-sm text-gray-500">{{ order.customerName }}</div>
                </div>
              </div>
              <div class="flex items-center space-x-4">
                <span :class="getStatusClass(order.status)" class="px-2 py-1 text-xs font-medium rounded-full">
                  {{ getStatusText(order.status) }}
                </span>
                <div class="text-right">
                  <div class="font-medium text-gray-900">{{ formatCurrency(order.totalAmount) }}</div>
                  <div class="text-sm text-gray-500">{{ formatDate(order.createdAt) }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Low Stock Alert -->
      <div class="bg-white rounded-lg shadow-sm border border-gray-100">
        <div class="p-6 border-b border-gray-200">
          <div class="flex items-center justify-between">
            <h2 class="text-lg font-semibold text-gray-900">Cảnh báo tồn kho</h2>
            <router-link to="/products" class="text-sm text-blue-600 hover:text-blue-800 font-medium">
              Xem chi tiết
            </router-link>
          </div>
        </div>
        <div class="p-6">
          <div v-if="loadingStock" class="text-center py-8">
            <svg class="mx-auto h-8 w-8 text-gray-400 animate-spin" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            <p class="mt-2 text-sm text-gray-500">Đang tải dữ liệu tồn kho...</p>
          </div>
          <div v-else-if="lowStockProducts.length === 0" class="text-center py-8">
            <svg class="mx-auto h-8 w-8 text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <p class="mt-2 text-sm text-gray-500">Tất cả sản phẩm đều có đủ tồn kho</p>
          </div>
          <div v-else class="space-y-3">
            <div v-for="product in lowStockProducts" :key="product.id"
              class="flex items-center justify-between p-3 border border-red-100 rounded-lg bg-red-50">
              <div class="flex items-center space-x-3">
                <div class="h-8 w-8 bg-red-100 rounded-full flex items-center justify-center">
                  <svg class="h-4 w-4 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z" />
                  </svg>
                </div>
                <div>
                  <div class="font-medium text-gray-900">{{ product.name }}</div>
                  <div class="text-sm text-gray-500">SKU: {{ product.sku }}</div>
                </div>
              </div>
              <div class="text-right">
                <div class="text-sm font-medium text-red-600">{{ product.stock }} còn lại</div>
                <div class="text-xs text-gray-500">Cần nhập thêm</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <NotesWidget />

      <!-- Khách hàng -->
      <div class="bg-white rounded-lg shadow-sm border border-gray-100">
        <div class="p-6 border-b border-gray-200">
          <div class="flex items-center justify-between">
            <h2 class="text-lg font-semibold text-gray-900">Khách hàng gần đây</h2>
            <router-link to="/customers" class="text-sm text-blue-600 hover:text-blue-800 font-medium">
              Xem tất cả
            </router-link>
          </div>
        </div>
        <div class="p-6">
          <div class="text-center py-8">
            <p class="mt-2 text-sm text-gray-500">Chưa có khách hàng nào.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { ordersApi, productsAPI } from '@/api'
import NotesWidget from '@/components/notes/NotesWidget.vue'

export default {
  name: 'Home',
  components: { NotesWidget },
  setup() {
    const loading = ref(false)
    const loadingStock = ref(false)
    const recentOrders = ref([])
    const lowStockProducts = ref([])
    const stats = ref({
      totalRevenue: 0,
      revenueGrowth: 0,
      totalOrders: 0,
      orderGrowth: 0,
      totalProducts: 0,
      totalCustomers: 0,
      customerGrowth: 0
    })

    // Load dashboard data
    const loadDashboardData = async () => {
      try {
        loading.value = true
        loadingStock.value = true

        // Load recent orders
        const ordersResponse = await ordersApi.getAll(0, 5)
        recentOrders.value = ordersResponse.data.content || []

        // Load order stats
        const statsResponse = await ordersApi.getOrderStats()
        if (statsResponse.data) {
          stats.value = {
            ...stats.value,
            totalOrders: statsResponse.data.totalOrders || 0,
            orderGrowth: statsResponse.data.orderGrowth || 0,
            totalRevenue: statsResponse.data.totalRevenue || 0,
            revenueGrowth: statsResponse.data.revenueGrowth || 0
          }
        }

        // Load low stock products
        const productsResponse = await productsAPI.getProducts(0, 10, '', null, null)
        lowStockProducts.value = (productsResponse.data.content || [])
          .filter(product => product.stock < 10) // Sản phẩm có tồn kho < 10
          .slice(0, 5) // Chỉ lấy 5 sản phẩm đầu

        // Mock data cho stats còn lại (sẽ thay bằng API thực tế)
        stats.value.totalProducts = productsResponse.data.totalElements || 0
        stats.value.totalCustomers = 342 // Mock data
        stats.value.customerGrowth = 5.1 // Mock data

      } catch (error) {
        console.error('Error loading dashboard data:', error)
      } finally {
        loading.value = false
        loadingStock.value = false
      }
    }

    // Format currency
    const formatCurrency = (amount) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(amount)
    }

    // Format date
    const formatDate = (dateString) => {
      return new Date(dateString).toLocaleDateString('vi-VN')
    }

    // Get status class
    const getStatusClass = (status) => {
      const classes = {
        'PENDING': 'bg-yellow-100 text-yellow-800',
        'CONFIRMED': 'bg-blue-100 text-blue-800',
        'PROCESSING': 'bg-purple-100 text-purple-800',
        'SHIPPED': 'bg-indigo-100 text-indigo-800',
        'DELIVERED': 'bg-green-100 text-green-800',
        'CANCELLED': 'bg-red-100 text-red-800'
      }
      return classes[status] || 'bg-gray-100 text-gray-800'
    }

    // Get status text
    const getStatusText = (status) => {
      const texts = {
        'PENDING': 'Chờ xác nhận',
        'CONFIRMED': 'Đã xác nhận',
        'PROCESSING': 'Đang xử lý',
        'SHIPPED': 'Đã giao hàng',
        'DELIVERED': 'Đã nhận',
        'CANCELLED': 'Đã hủy'
      }
      return texts[status] || status
    }

    onMounted(() => {
      loadDashboardData()
    })

    return {
      loading,
      loadingStock,
      recentOrders,
      lowStockProducts,
      stats,
      formatCurrency,
      formatDate,
      getStatusClass,
      getStatusText
    }
  }
}
</script>
