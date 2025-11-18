<template>
  <div class="bg-white rounded-xl shadow-lg border border-gray-100 overflow-hidden">
    <!-- Header -->
    <div class="flex items-center justify-between px-6 py-5 border-b border-gray-100">
      <h2 class="text-lg font-semibold text-gray-900 tracking-tight">Đơn hàng gần đây</h2>
      <div class="flex space-x-2 items-center">
        <router-link 
          to="/orders"
          class="inline-flex items-center px-3 py-2 rounded-lg border border-gray-200 hover:bg-blue-50 transition"
        >
          <svg class="h-5 w-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
          </svg>
        </router-link>
        <router-link
          to="/orders"
          class="text-sm text-blue-600 hover:text-blue-800 font-medium"
        >
          Xem tất cả
        </router-link>
      </div>
    </div>

    <!-- Main Content Area -->
    <div class="p-6 bg-gray-50">
      <!-- Loading State -->
      <div v-if="loading" class="flex flex-col items-center gap-4 py-10">
        <svg class="h-8 w-8 text-blue-400 animate-spin" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <circle class="opacity-20" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-70" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"></path>
        </svg>
        <span class="text-gray-500 text-sm">Đang tải đơn hàng...</span>
      </div>
      
      <!-- No Orders State -->
      <div v-else-if="recentOrders.length === 0" class="flex flex-col items-center gap-4 py-10">
        <svg class="h-8 w-8 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
        </svg>
        <span class="text-gray-500 text-sm">Chưa có đơn hàng nào</span>
      </div>
      
      <!-- Orders List -->
      <div v-else class="divide-y divide-gray-100">
        <div v-for="order in recentOrders" :key="order.id" class="flex flex-wrap items-center justify-between py-4">
          <div class="flex items-center gap-3 shrink-0">
            <div class="h-12 w-12 bg-blue-100 rounded-full flex items-center justify-center">
              <svg class="h-6 w-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
            </div>
            <div>
              <div class="font-semibold text-gray-900">#{{ order.orderNumber }}</div>
              <div class="text-xs text-gray-500">{{ order.customerName }}</div>
            </div>
          </div>
          <div class="flex flex-col items-end gap-2 min-w-[120px]">
            <span :class="getStatusClass(order.status)" class="px-2 py-1 text-xs font-semibold rounded-full capitalize">
              {{ getStatusText(order.status) }}
            </span>
            <div class="text-right">
              <div class="font-medium text-gray-900">{{ formatCurrency(order.totalAmount) }}</div>
              <div class="text-xs text-gray-500">{{ formatDate(order.createdAt) }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import { ref, onMounted } from 'vue'
import { ordersApi } from '@/api'

export default {
  name: 'RecentOrders',
  props: {
    formatCurrency: {
      type: Function,
      required: true
    }
  },
  setup(props) {
    const loading = ref(false)
    const recentOrders = ref([])

    const loadRecentOrders = async () => {
      try {
        loading.value = true
        const ordersResponse = await ordersApi.getAll(0, 5)
        recentOrders.value = ordersResponse.data.content || []
      } catch (error) {
        console.error('Error loading recent orders:', error)
      } finally {
        loading.value = false
      }
    }

    const formatDate = (dateString) => {
      return new Date(dateString).toLocaleDateString('vi-VN')
    }

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
      loadRecentOrders()
    })

    return {
      loading,
      recentOrders,
      formatDate,
      getStatusClass,
      getStatusText,
      formatCurrency: props.formatCurrency // Pass formatCurrency from props
    }
  }
}
</script>
