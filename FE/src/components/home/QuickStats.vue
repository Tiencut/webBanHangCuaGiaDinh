<template>
  <div class="flex justify-center mb-6">
    <div class="w-full max-w-md aspect-square grid grid-cols-2 gap-4">
    <!-- Tổng doanh thu -->
    <div class="bg-white rounded-xl shadow-lg p-4 border border-gray-100 transition hover:shadow-xl flex flex-col justify-between h-full">
      <div class="flex justify-between items-center mb-2">
        <div>
          <p class="text-sm font-medium text-gray-500">Tổng doanh thu</p>
          <p class="text-3xl font-extrabold text-blue-600 mt-1">{{ formatCurrency(stats.totalRevenue) }}</p>
        </div>
        <div
          class="h-14 w-14 bg-blue-100 rounded-full flex items-center justify-center cursor-pointer hover:shadow-md hover:scale-105 transform transition duration-150 focus:outline-none focus:ring-2 focus:ring-blue-200"
          role="button"
          tabindex="0"
          @click="onQuickAction('revenue')"
          @keyup.enter="onQuickAction('revenue')"
          title="Thao tác nhanh: Tổng doanh thu"
        >
          <svg class="h-8 w-8 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
          </svg>
        </div>
      </div>
      <p class="flex items-center text-xs text-green-600 mt-2" v-if="stats.revenueGrowth > 0">
        <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            d="M13 7h8m0 0v8m0-8l-8 8-4-4-4 4"/>
        </svg>
        +{{ stats.revenueGrowth }}% so với tháng trước
      </p>
    </div>
    <!-- Quản lý xe (card giống các stat khác) -->
    <div class="bg-white rounded-xl shadow-lg p-4 border border-gray-100 transition hover:shadow-xl flex flex-col justify-between h-full">
      <div class="flex justify-between items-center mb-2">
        <div>
          <p class="text-sm font-medium text-gray-500">Quản lý xe</p>
          <p class="text-3xl font-extrabold text-gray-900 mt-1">{{ stats.totalVehicles }}</p>
          <p class="text-xs text-gray-400 mt-2">Tổng số xe</p>
        </div>
        <div
          class="h-14 w-14 bg-orange-100 rounded-full flex items-center justify-center cursor-pointer hover:shadow-md hover:scale-105 transform transition duration-150 focus:outline-none focus:ring-2 focus:ring-orange-200"
          role="button"
          tabindex="0"
          @click="onQuickAction('vehicles')"
          @keyup.enter="onQuickAction('vehicles')"
          title="Thao tác nhanh: Quản lý xe"
        >
          <svg class="h-8 w-8 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0z" />
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M13 6H4a2 2 0 00-2 2v6a2 2 0 002 2h1m8-10V4a2 2 0 00-2-2H4m7 4v10m0 0h6a2 2 0 002-2v-6a2 2 0 00-2-2h-6z" />
          </svg>
        </div>
      </div>
      <p class="flex items-center text-xs text-gray-500 mt-2">
        <span>{{ vehiclesCount }} xe</span>
      </p>
    </div>

    <!-- Đơn hàng -->
    <div class="bg-white rounded-xl shadow-lg p-4 border border-gray-100 transition hover:shadow-xl flex flex-col justify-between h-full">
      <div class="flex justify-between items-center mb-2">
        <div>
          <p class="text-sm font-medium text-gray-500">Đơn hàng</p>
          <p class="text-3xl font-extrabold text-gray-900 mt-1">{{ stats.totalOrders }}</p>
        </div>
        <div
          class="h-14 w-14 bg-green-100 rounded-full flex items-center justify-center cursor-pointer hover:shadow-md hover:scale-105 transform transition duration-150 focus:outline-none focus:ring-2 focus:ring-green-200"
          role="button"
          tabindex="0"
          @click="onQuickAction('orders')"
          @keyup.enter="onQuickAction('orders')"
          title="Thao tác nhanh: Đơn hàng"
        >
          <svg class="h-8 w-8 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
          </svg>
        </div>
      </div>
      <p class="flex items-center text-xs text-green-600 mt-2" v-if="stats.orderGrowth > 0">
        <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            d="M13 7h8m0 0v8m0-8l-8 8-4-4-4 4"/>
        </svg>
        +{{ stats.orderGrowth }}% so với tháng trước
      </p>
    </div>

    <!-- Sản phẩm -->
    <div class="bg-white rounded-xl shadow-lg p-4 border border-gray-100 transition hover:shadow-xl flex flex-col justify-between h-full">
      <div class="flex justify-between items-center mb-2">
        <div>
          <p class="text-sm font-medium text-gray-500">Sản phẩm</p>
          <p class="text-3xl font-extrabold text-gray-900 mt-1">{{ stats.totalProducts }}</p>
          <p class="text-xs text-gray-400 mt-2">Tổng số mặt hàng</p>
        </div>
        <div
          class="h-14 w-14 bg-purple-100 rounded-full flex items-center justify-center cursor-pointer hover:shadow-md hover:scale-105 transform transition duration-150 focus:outline-none focus:ring-2 focus:ring-purple-200"
          role="button"
          tabindex="0"
          @click="onQuickAction('products')"
          @keyup.enter="onQuickAction('products')"
          title="Thao tác nhanh: Sản phẩm"
        >
          <svg class="h-8 w-8 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/>
          </svg>
        </div>
      </div>
    </div>

    <!-- Khách hàng -->
    <div class="bg-white rounded-xl shadow-lg p-4 border border-gray-100 transition hover:shadow-xl flex flex-col justify-between h-full">
      <div class="flex justify-between items-center mb-2">
        <div>
          <p class="text-sm font-medium text-gray-500">Khách hàng</p>
          <p class="text-3xl font-extrabold text-gray-900 mt-1">{{ stats.totalCustomers }}</p>
        </div>
        <div
          class="h-14 w-14 bg-orange-100 rounded-full flex items-center justify-center cursor-pointer hover:shadow-md hover:scale-105 transform transition duration-150 focus:outline-none focus:ring-2 focus:ring-orange-200"
          role="button"
          tabindex="0"
          @click="onQuickAction('customers')"
          @keyup.enter="onQuickAction('customers')"
          title="Thao tác nhanh: Khách hàng"
        >
          <svg class="h-8 w-8 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
          </svg>
        </div>
      </div>
      <p class="flex items-center text-xs text-blue-600 mt-2" v-if="stats.customerGrowth > 0">
        <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            d="M13 7h8m0 0v8m0-8l-8 8-4-4-4 4"/>
        </svg>
        +{{ stats.customerGrowth }}% khách hàng mới
      </p>
    </div>
    </div>
  </div>
</template>


<script>
import { ref, onMounted } from 'vue'
import { ordersApi, productsAPI, vehiclesApi } from '@/api'
import { useRouter } from 'vue-router'

export default {
  name: 'QuickStats',
  setup() {
    const stats = ref({
      totalRevenue: 0,
      revenueGrowth: 0,
      totalOrders: 0,
      orderGrowth: 0,
      totalProducts: 0,
      totalCustomers: 0,
      totalVehicles: 0,
      customerGrowth: 0
    })

    const vehiclesCount = ref(0)

    // Load dashboard data
    const loadDashboardData = async () => {
      try {
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

        // Load total products
        const productsResponse = await productsAPI.getProducts(0, 1) // Get 1 product to get totalElements
        if (productsResponse.data && productsResponse.data.totalElements !== undefined) {
          stats.value.totalProducts = productsResponse.data.totalElements
        }

        // Load vehicles count
        try {
          const vehiclesResponse = await vehiclesApi.getAll(0, 1)
          if (vehiclesResponse.data && vehiclesResponse.data.totalElements !== undefined) {
            stats.value.totalVehicles = vehiclesResponse.data.totalElements
            vehiclesCount.value = vehiclesResponse.data.totalElements
          }
        } catch (e) {
          // ignore
        }

        // Mock data for remaining stats (will be replaced with actual API)
        stats.value.totalCustomers = 342 // Mock data
        stats.value.customerGrowth = 5.1 // Mock data

      } catch (error) {
        console.error('Error loading dashboard data:', error)
      }
    }

    // Format currency
    const formatCurrency = (amount) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(amount)
    }

    onMounted(() => {
      loadDashboardData()
    })

    const router = useRouter()

    const onQuickAction = (type) => {
      // Navigate based on type
      switch (type) {
        case 'revenue':
          router.push('/orders/stats').catch(() => {})
          break
        case 'vehicles':
          router.push('/vehicles').catch(() => {})
          break
        case 'orders':
          router.push('/orders').catch(() => {})
          break
        case 'products':
          router.push('/products').catch(() => {})
          break
        case 'customers':
          router.push('/customers').catch(() => {})
          break
        default:
          console.log('Unknown quick action:', type)
      }
    }

    const navigateTo = (path) => {
      router.push(path).catch(() => {})
    }

    return {
      stats,
      formatCurrency,
      onQuickAction,
      navigateTo,
      vehiclesCount
    }
  }
}
</script>