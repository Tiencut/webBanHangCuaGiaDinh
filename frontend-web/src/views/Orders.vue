<template>
  <div class="container mx-auto px-4 py-6">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Quản lý đơn hàng</h1>
      <button 
        @click="showCreateModal = true"
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
            v-model="fromDate"
            type="date"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Đến ngày</label>
          <input 
            v-model="toDate"
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
        v-for="order in filteredOrders" 
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
              <div class="text-sm text-gray-500">{{ formatDateTime(order.createdAt) }}</div>
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
              @click="confirmOrder(order.id)"
              class="bg-green-50 text-green-600 hover:bg-green-100 px-4 py-2 rounded-md text-sm font-medium"
            >
              Xác nhận
            </button>
            <button 
              v-if="['PENDING', 'CONFIRMED'].includes(order.status)"
              @click="cancelOrder(order.id)"
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
    <div v-if="filteredOrders.length === 0" class="text-center py-12">
      <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
      </svg>
      <h3 class="mt-2 text-sm font-medium text-gray-900">Không tìm thấy đơn hàng</h3>
      <p class="mt-1 text-sm text-gray-500">Hãy thử thay đổi bộ lọc hoặc tạo đơn hàng mới.</p>
    </div>

    <!-- Load More -->
    <div class="text-center mt-6" v-if="filteredOrders.length > 0">
      <button class="bg-gray-50 text-gray-600 hover:bg-gray-100 px-6 py-2 rounded-md text-sm font-medium">
        Tải thêm đơn hàng
      </button>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'

export default {
  name: 'Orders',
  setup() {
    // Reactive data
    const searchQuery = ref('')
    const selectedStatus = ref('')
    const fromDate = ref('')
    const toDate = ref('')
    const showCreateModal = ref(false)
    
    // Mock data
    const orders = ref([
      {
        id: 1,
        orderNumber: 'ORD001',
        customerName: 'Nguyễn Văn An',
        customerPhone: '0901234567',
        status: 'PENDING',
        totalAmount: 1500000,
        createdAt: '2024-01-15T10:30:00',
        deliveryAddress: '123 Đường ABC, Quận 1, TP.HCM',
        deliveryNotes: 'Giao hàng giờ hành chính',
        items: [
          { id: 1, productName: 'Lọc dầu động cơ', quantity: 2, subtotal: 300000 },
          { id: 2, productName: 'Má phanh trước', quantity: 1, subtotal: 350000 },
          { id: 3, productName: 'Dầu nhớt 15W40', quantity: 4, subtotal: 850000 }
        ]
      },
      {
        id: 2,
        orderNumber: 'ORD002',
        customerName: 'Công ty TNHH Vận tải XYZ',
        customerPhone: '0902345678',
        status: 'CONFIRMED',
        totalAmount: 12000000,
        createdAt: '2024-01-14T14:20:00',
        deliveryAddress: '456 Đường DEF, Quận Bình Thạnh, TP.HCM',
        deliveryNotes: null,
        items: [
          { id: 4, productName: 'Lốp xe 900R20', quantity: 4, subtotal: 10000000 },
          { id: 5, productName: 'Vỏ bánh xe', quantity: 4, subtotal: 2000000 }
        ]
      },
      {
        id: 3,
        orderNumber: 'ORD003',
        customerName: 'Trần Thị Bình',
        customerPhone: '0903456789',
        status: 'DELIVERED',
        totalAmount: 450000,
        createdAt: '2024-01-13T09:15:00',
        deliveryAddress: '789 Đường GHI, Quận 7, TP.HCM',
        deliveryNotes: 'Gọi trước khi giao',
        items: [
          { id: 6, productName: 'Lọc gió', quantity: 1, subtotal: 200000 },
          { id: 7, productName: 'Bugi', quantity: 4, subtotal: 250000 }
        ]
      }
    ])

    // Computed properties
    const filteredOrders = computed(() => {
      let filtered = orders.value

      if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        filtered = filtered.filter(order => 
          order.orderNumber.toLowerCase().includes(query) ||
          order.customerName.toLowerCase().includes(query) ||
          order.customerPhone.includes(query)
        )
      }

      if (selectedStatus.value) {
        filtered = filtered.filter(order => order.status === selectedStatus.value)
      }

      if (fromDate.value) {
        filtered = filtered.filter(order => 
          new Date(order.createdAt) >= new Date(fromDate.value)
        )
      }

      if (toDate.value) {
        filtered = filtered.filter(order => 
          new Date(order.createdAt) <= new Date(toDate.value)
        )
      }

      return filtered.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    })

    const totalOrders = computed(() => orders.value.length)
    const pendingOrders = computed(() => 
      orders.value.filter(order => order.status === 'PENDING').length
    )
    const completedOrders = computed(() => 
      orders.value.filter(order => order.status === 'DELIVERED').length
    )
    const monthlyRevenue = computed(() => 
      orders.value
        .filter(order => order.status === 'DELIVERED')
        .reduce((sum, order) => sum + order.totalAmount, 0)
    )

    // Methods
    const getStatusClass = (status) => {
      switch (status) {
        case 'PENDING': return 'bg-yellow-100 text-yellow-800'
        case 'CONFIRMED': return 'bg-blue-100 text-blue-800'
        case 'PROCESSING': return 'bg-purple-100 text-purple-800'
        case 'SHIPPING': return 'bg-indigo-100 text-indigo-800'
        case 'DELIVERED': return 'bg-green-100 text-green-800'
        case 'CANCELLED': return 'bg-red-100 text-red-800'
        default: return 'bg-gray-100 text-gray-800'
      }
    }

    const getStatusText = (status) => {
      switch (status) {
        case 'PENDING': return 'Chờ xử lý'
        case 'CONFIRMED': return 'Đã xác nhận'
        case 'PROCESSING': return 'Đang xử lý'
        case 'SHIPPING': return 'Đang giao'
        case 'DELIVERED': return 'Đã giao'
        case 'CANCELLED': return 'Đã hủy'
        default: return 'Không xác định'
      }
    }

    const formatCurrency = (amount) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(amount)
    }

    const formatDateTime = (dateTime) => {
      return new Date(dateTime).toLocaleString('vi-VN')
    }

    const applyFilters = () => {
      console.log('Applying filters...')
    }

    const clearFilters = () => {
      searchQuery.value = ''
      selectedStatus.value = ''
      fromDate.value = ''
      toDate.value = ''
    }

    const viewOrder = (order) => {
      console.log('View order:', order)
      // Logic to view order details
    }

    const confirmOrder = (orderId) => {
      console.log('Confirm order:', orderId)
      // Logic to confirm order
    }

    const cancelOrder = (orderId) => {
      if (confirm('Bạn có chắc chắn muốn hủy đơn hàng này?')) {
        console.log('Cancel order:', orderId)
        // Logic to cancel order
      }
    }

    const printOrder = (order) => {
      console.log('Print order:', order)
      // Logic to print order
    }

    // Lifecycle
    onMounted(() => {
      console.log('Orders component mounted')
    })

    return {
      searchQuery,
      selectedStatus,
      fromDate,
      toDate,
      showCreateModal,
      orders,
      filteredOrders,
      totalOrders,
      pendingOrders,
      completedOrders,
      monthlyRevenue,
      getStatusClass,
      getStatusText,
      formatCurrency,
      formatDateTime,
      applyFilters,
      clearFilters,
      viewOrder,
      confirmOrder,
      cancelOrder,
      printOrder
    }
  }
}
</script>
