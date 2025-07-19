<template>
  <div class="container mx-auto px-4 py-6">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Quản lý đơn hàng</h1>
      <button 
        @click="showAddModal = true"
        class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg flex items-center"
      >
        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"/>
        </svg>
        Tạo đơn hàng
      </button>
    </div>

    <!-- Statistics Cards -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-6">
      <div class="bg-white rounded-lg shadow-sm p-6">
        <div class="flex items-center">
          <div class="flex-shrink-0">
            <div class="w-8 h-8 bg-blue-500 rounded-md flex items-center justify-center">
              <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
              </svg>
            </div>
          </div>
          <div class="ml-5 w-0 flex-1">
            <dl>
              <dt class="text-sm font-medium text-gray-500 truncate">Tổng đơn hàng</dt>
              <dd class="text-lg font-medium text-gray-900">{{ totalOrders }}</dd>
            </dl>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow-sm p-6">
        <div class="flex items-center">
          <div class="flex-shrink-0">
            <div class="w-8 h-8 bg-yellow-500 rounded-md flex items-center justify-center">
              <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
              </svg>
            </div>
          </div>
          <div class="ml-5 w-0 flex-1">
            <dl>
              <dt class="text-sm font-medium text-gray-500 truncate">Chờ xử lý</dt>
              <dd class="text-lg font-medium text-gray-900">{{ pendingOrders }}</dd>
            </dl>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow-sm p-6">
        <div class="flex items-center">
          <div class="flex-shrink-0">
            <div class="w-8 h-8 bg-green-500 rounded-md flex items-center justify-center">
              <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
              </svg>
            </div>
          </div>
          <div class="ml-5 w-0 flex-1">
            <dl>
              <dt class="text-sm font-medium text-gray-500 truncate">Hoàn thành</dt>
              <dd class="text-lg font-medium text-gray-900">{{ completedOrders }}</dd>
            </dl>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow-sm p-6">
        <div class="flex items-center">
          <div class="flex-shrink-0">
            <div class="w-8 h-8 bg-green-600 rounded-md flex items-center justify-center">
              <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"/>
              </svg>
            </div>
          </div>
          <div class="ml-5 w-0 flex-1">
            <dl>
              <dt class="text-sm font-medium text-gray-500 truncate">Doanh thu tháng</dt>
              <dd class="text-lg font-medium text-gray-900">{{ formatCurrency(monthlyRevenue) }}</dd>
            </dl>
          </div>
        </div>
      </div>
    </div>

    <!-- Filter & Search Section -->
    <div class="bg-white p-4 rounded-lg shadow-sm mb-6">
      <div class="grid grid-cols-1 md:grid-cols-5 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
          <input 
            v-model="searchQuery"
            type="text" 
            placeholder="Mã đơn hàng, khách hàng..."
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
          <select 
            v-model="selectedStatus"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Tất cả</option>
            <option value="PENDING">Chờ xử lý</option>
            <option value="CONFIRMED">Đã xác nhận</option>
            <option value="PROCESSING">Đang xử lý</option>
            <option value="SHIPPING">Đang giao</option>
            <option value="DELIVERED">Đã giao</option>
            <option value="CANCELLED">Đã hủy</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Từ ngày</label>
          <input 
            v-model="dateRange.startDate"
            type="date"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Đến ngày</label>
          <input 
            v-model="dateRange.endDate"
            type="date"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
        </div>
        <div class="flex items-end">
          <button 
            @click="applyFilters"
            class="bg-gray-600 hover:bg-gray-700 text-white px-4 py-2 rounded-md mr-2"
          >
            Lọc
          </button>
          <button 
            @click="clearFilters"
            class="border border-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-50"
          >
            Xóa
          </button>
        </div>
      </div>
    </div>

    <!-- Orders List -->
    <div class="space-y-4">
      <div 
        v-for="order in orders" 
        :key="order.id"
        class="bg-white rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition-shadow"
      >
        <div class="p-6">
          <!-- Order Header -->
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center space-x-4">
              <h3 class="text-lg font-semibold text-gray-900">#{{ order.orderNumber }}</h3>
              <span :class="getStatusClass(order.status)" class="px-3 py-1 text-sm font-medium rounded-full">
                {{ getStatusText(order.status) }}
              </span>
            </div>
            <div class="text-right">
              <div class="text-lg font-semibold text-gray-900">{{ formatCurrency(order.totalAmount) }}</div>
              <div class="text-sm text-gray-500">{{ formatDate(order.createdAt) }}</div>
            </div>
          </div>

          <!-- Customer Info -->
          <div class="flex items-center mb-4">
            <div class="h-10 w-10 rounded-full bg-blue-100 flex items-center justify-center mr-3">
              <svg class="h-5 w-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
              </svg>
            </div>
            <div>
              <div class="font-medium text-gray-900">{{ order.customerName }}</div>
              <div class="text-sm text-gray-500">{{ order.customerPhone }}</div>
            </div>
          </div>

          <!-- Order Items Summary -->
          <div class="mb-4">
            <div class="text-sm text-gray-700 mb-2">
              <span class="font-medium">{{ order.items.length }}</span> sản phẩm:
            </div>
            <div class="space-y-1">
              <div 
                v-for="item in order.items.slice(0, 3)" 
                :key="item.id"
                class="flex justify-between text-sm"
              >
                <span class="text-gray-600">{{ item.productName }} x{{ item.quantity }}</span>
                <span class="text-gray-900">{{ formatCurrency(item.subtotal) }}</span>
              </div>
              <div v-if="order.items.length > 3" class="text-sm text-gray-500">
                ... và {{ order.items.length - 3 }} sản phẩm khác
              </div>
            </div>
          </div>

          <!-- Delivery Info -->
          <div class="mb-4 p-3 bg-gray-50 rounded-lg" v-if="order.deliveryAddress">
            <div class="text-sm text-gray-700">
              <span class="font-medium">Giao hàng:</span> {{ order.deliveryAddress }}
            </div>
            <div class="text-sm text-gray-500" v-if="order.deliveryNotes">
              {{ order.deliveryNotes }}
            </div>
          </div>

          <!-- Actions -->
          <div class="flex space-x-3">
            <button 
              @click="viewOrder(order)"
              class="bg-blue-50 text-blue-600 hover:bg-blue-100 px-4 py-2 rounded-md text-sm font-medium"
            >
              Xem chi tiết
            </button>
            <button 
              v-if="order.status === 'PENDING'"
              @click="updateOrderStatus(order.id, 'CONFIRMED')"
              class="bg-green-50 text-green-600 hover:bg-green-100 px-4 py-2 rounded-md text-sm font-medium"
            >
              Xác nhận
            </button>
            <button 
              v-if="['PENDING', 'CONFIRMED'].includes(order.status)"
              @click="updateOrderStatus(order.id, 'CANCELLED')"
              class="bg-red-50 text-red-600 hover:bg-red-100 px-4 py-2 rounded-md text-sm font-medium"
            >
              Hủy đơn
            </button>
            <button 
              @click="printOrder(order)"
              class="bg-gray-50 text-gray-600 hover:bg-gray-100 px-4 py-2 rounded-md text-sm font-medium"
            >
              In đơn hàng
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div v-if="orders.length === 0" class="text-center py-12">
      <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
      </svg>
      <h3 class="mt-2 text-sm font-medium text-gray-900">Không tìm thấy đơn hàng</h3>
      <p class="mt-1 text-sm text-gray-500">Hãy thử thay đổi bộ lọc hoặc tạo đơn hàng mới.</p>
    </div>

    <!-- Load More -->
    <div class="text-center mt-6" v-if="orders.length > 0">
      <button class="bg-gray-50 text-gray-600 hover:bg-gray-100 px-6 py-2 rounded-md text-sm font-medium">
        Tải thêm đơn hàng
      </button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { ordersApi, customersApi } from '@/api'
import { removeVietnameseTones } from '../utils/removeVietnameseTones'

export default {
  name: 'Orders',
  setup() {
    const loading = ref(false)
    const orders = ref([])
    const customers = ref([])
    const currentPage = ref(0)
    const totalPages = ref(0)
    const totalElements = ref(0)
    const pageSize = ref(10)

    // Filters
    const searchQuery = ref('')
    const selectedStatus = ref('')
    const selectedCustomer = ref('')
    const dateRange = ref({
      startDate: '',
      endDate: ''
    })

    // Modal states
    const showAddModal = ref(false)
    const showEditModal = ref(false)
    const selectedOrder = ref(null)

    // New order form
    const newOrder = ref({
      customerId: '',
      items: [],
      totalAmount: 0,
        status: 'PENDING',
      notes: '',
      deliveryMethod: 'SELF_PICKUP'
    })

    // Load orders
    const loadOrders = async () => {
      try {
        loading.value = true
        const response = await ordersApi.getAll(
          currentPage.value,
          pageSize.value,
          searchQuery.value,
          selectedStatus.value || null,
          selectedCustomer.value || null
        )
        
        orders.value = response.data.content || []
        totalPages.value = response.data.totalPages || 0
        totalElements.value = response.data.totalElements || 0
      } catch (error) {
        console.error('Error loading orders:', error)
      } finally {
        loading.value = false
      }
    }

    // Load customers for dropdown
    const loadCustomers = async () => {
      try {
        const response = await customersApi.getAll(0, 1000)
        customers.value = response.data.content || []
      } catch (error) {
        console.error('Error loading customers:', error)
      }
    }

    // Create new order
    const createOrder = async () => {
      try {
        await ordersApi.create(newOrder.value)
        showAddModal.value = false
        resetNewOrder()
        loadOrders()
      } catch (error) {
        console.error('Error creating order:', error)
      }
    }

    // Update order
    const updateOrder = async () => {
      try {
        await ordersApi.update(selectedOrder.value.id, selectedOrder.value)
        showEditModal.value = false
        selectedOrder.value = null
        loadOrders()
      } catch (error) {
        console.error('Error updating order:', error)
      }
    }

    // Delete order
    const deleteOrder = async (orderId) => {
      if (confirm('Bạn có chắc chắn muốn xóa đơn hàng này?')) {
        try {
          await ordersApi.delete(orderId)
          loadOrders()
        } catch (error) {
          console.error('Error deleting order:', error)
        }
      }
    }

    // Update order status
    const updateOrderStatus = async (orderId, newStatus) => {
      try {
        await ordersApi.updateOrderStatus(orderId, newStatus)
        loadOrders()
      } catch (error) {
        console.error('Error updating order status:', error)
      }
    }

    // Edit order
    const editOrder = (order) => {
      selectedOrder.value = { ...order }
      showEditModal.value = true
    }

    // View order details
    const viewOrder = (order) => {
      selectedOrder.value = { ...order }
      // Có thể mở modal chi tiết hoặc navigate đến trang chi tiết
    }

    // Reset new order form
    const resetNewOrder = () => {
      newOrder.value = {
        customerId: '',
        items: [],
        totalAmount: 0,
        status: 'PENDING',
        notes: '',
        deliveryMethod: 'SELF_PICKUP'
      }
    }

    // Apply filters
    const applyFilters = () => {
      currentPage.value = 0
      loadOrders()
    }

    // Clear filters
    const clearFilters = () => {
      searchQuery.value = ''
      selectedStatus.value = ''
      selectedCustomer.value = ''
      dateRange.value = { startDate: '', endDate: '' }
      currentPage.value = 0
      loadOrders()
    }

    // Change page
    const changePage = (page) => {
      currentPage.value = page
      loadOrders()
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

    // Get delivery method text
    const getDeliveryMethodText = (method) => {
      const texts = {
        'SELF_PICKUP': 'Tự đến lấy',
        'MOTORBIKE': 'Xe ôm',
        'BUS': 'Xe khách',
        'TRUCK': 'Xe tải',
        'EXPRESS': 'Chuyển phát nhanh'
      }
      return texts[method] || method
    }

    // Get customer name by ID
    const getCustomerName = (customerId) => {
      const customer = customers.value.find(c => c.id === customerId)
      return customer ? customer.name : 'N/A'
    }

    // Computed properties
    const paginationInfo = computed(() => {
      const start = currentPage.value * pageSize.value + 1
      const end = Math.min((currentPage.value + 1) * pageSize.value, totalElements.value)
      return `${start}-${end} của ${totalElements.value} đơn hàng`
    })

    const filteredOrders = computed(() => {
      let filtered = orders.value
      if (searchQuery.value) {
        const search = removeVietnameseTones(searchQuery.value.toLowerCase())
        filtered = filtered.filter(order =>
          removeVietnameseTones(order.orderNumber?.toLowerCase() || '').includes(search) ||
          removeVietnameseTones(order.customerName?.toLowerCase() || '').includes(search)
        )
      }
      return filtered
    })

    // Load data on mount
    onMounted(() => {
      loadOrders()
      loadCustomers()
    })

    return {
      // Data
      loading,
      orders,
      customers,
      currentPage,
      totalPages,
      totalElements,
      pageSize,
      
      // Filters
      searchQuery,
      selectedStatus,
      selectedCustomer,
      dateRange,
      
      // Modals
      showAddModal,
      showEditModal,
      selectedOrder,
      newOrder,
      
      // Methods
      loadOrders,
      createOrder,
      updateOrder,
      deleteOrder,
      updateOrderStatus,
      editOrder,
      viewOrder,
      applyFilters,
      clearFilters,
      changePage,
      formatCurrency,
      formatDate,
      getStatusClass,
      getStatusText,
      getDeliveryMethodText,
      getCustomerName,
      
      // Computed
      paginationInfo,
      filteredOrders
    }
  }
}
</script>
