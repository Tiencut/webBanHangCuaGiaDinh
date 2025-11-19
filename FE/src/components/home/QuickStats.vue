<template>
  <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-10">
    <!-- Tổng doanh thu -->
    <div class="bg-white rounded-xl shadow-lg p-7 border border-gray-100 transition hover:shadow-xl flex flex-col justify-between">
      <div class="flex justify-between items-center mb-2">
        <div>
          <p class="text-sm font-medium text-gray-500">Tổng doanh thu</p>
          <p class="text-3xl font-extrabold text-blue-600 mt-1">{{ formatCurrency(stats.totalRevenue) }}</p>
        </div>
        <div class="h-14 w-14 bg-blue-100 rounded-full flex items-center justify-center">
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

    <!-- Đơn hàng -->
    <div class="bg-white rounded-xl shadow-lg p-7 border border-gray-100 transition hover:shadow-xl flex flex-col justify-between">
      <div class="flex justify-between items-center mb-2">
        <div>
          <p class="text-sm font-medium text-gray-500">Đơn hàng</p>
          <p class="text-3xl font-extrabold text-gray-900 mt-1">{{ stats.totalOrders }}</p>
        </div>
        <div class="h-14 w-14 bg-green-100 rounded-full flex items-center justify-center">
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
    <div class="bg-white rounded-xl shadow-lg p-7 border border-gray-100 transition hover:shadow-xl flex flex-col justify-between">
      <div class="flex justify-between items-center mb-2">
        <div>
          <p class="text-sm font-medium text-gray-500">Sản phẩm</p>
          <p class="text-3xl font-extrabold text-gray-900 mt-1">{{ stats.totalProducts }}</p>
          <p class="text-xs text-gray-400 mt-2">Tổng số mặt hàng</p>
        </div>
        <div class="h-14 w-14 bg-purple-100 rounded-full flex items-center justify-center">
          <svg class="h-8 w-8 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/>
          </svg>
        </div>
      </div>
    </div>

    <!-- Khách hàng -->
    <div class="bg-white rounded-xl shadow-lg p-7 border border-gray-100 transition hover:shadow-xl flex flex-col justify-between">
      <div class="flex justify-between items-center mb-2">
        <div>
          <p class="text-sm font-medium text-gray-500">Khách hàng</p>
          <p class="text-3xl font-extrabold text-gray-900 mt-1">{{ stats.totalCustomers }}</p>
        </div>
        <div class="h-14 w-14 bg-orange-100 rounded-full flex items-center justify-center">
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
</template>


<script>
import { ref, onMounted } from 'vue'
import { ordersApi, productsAPI } from '@/api'

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
      customerGrowth: 0
    })

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

        // Mock data for remaining stats (will be replaced with actual API)
        stats.value.totalCustomers = 342 // Mock data
        stats.value.customerGrowth = 5.1 // Mock data

      } catch (error) {
        console.error('Error loading dashboard data:', error)
      } finally {
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

    return {
      stats,
      formatCurrency
    }
  }
}
</script>