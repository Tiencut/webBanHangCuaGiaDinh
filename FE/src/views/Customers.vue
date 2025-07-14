<template>
  <div class="container mx-auto px-4 py-6">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Quản lý khách hàng</h1>
      <button 
        @click="showAddModal = true"
        class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg flex items-center"
      >
        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"/>
        </svg>
        Thêm khách hàng
      </button>
    </div>

    <!-- Filter & Search Section -->
    <div class="bg-white p-4 rounded-lg shadow-sm mb-6">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
          <input 
            v-model="searchQuery"
            type="text" 
            placeholder="Tên, số điện thoại, email..."
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Loại khách hàng</label>
          <select 
            v-model="selectedType"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Tất cả</option>
            <option value="INDIVIDUAL">Cá nhân</option>
            <option value="BUSINESS">Doanh nghiệp</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
          <select 
            v-model="selectedStatus"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Tất cả</option>
            <option value="ACTIVE">Hoạt động</option>
            <option value="INACTIVE">Không hoạt động</option>
            <option value="BLOCKED">Bị khóa</option>
          </select>
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

    <!-- Customers Cards -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div 
        v-for="customer in filteredCustomers" 
        :key="customer.id"
        class="bg-white rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition-shadow"
      >
        <div class="p-6">
          <!-- Customer Header -->
          <div class="flex items-start justify-between mb-4">
            <div class="flex items-center">
              <div class="h-12 w-12 rounded-full bg-blue-100 flex items-center justify-center mr-3">
                <svg class="h-6 w-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
                </svg>
              </div>
              <div>
                <h3 class="font-semibold text-gray-900">{{ customer.name }}</h3>
                <span :class="getTypeClass(customer.type)" class="text-xs px-2 py-1 rounded-full">
                  {{ getTypeText(customer.type) }}
                </span>
              </div>
            </div>
            <span :class="getStatusClass(customer.status)" class="text-xs px-2 py-1 rounded-full">
              {{ getStatusText(customer.status) }}
            </span>
          </div>

          <!-- Customer Info -->
          <div class="space-y-2 mb-4">
            <div class="flex items-center text-sm text-gray-600">
              <svg class="h-4 w-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/>
              </svg>
              {{ customer.phone }}
            </div>
            <div class="flex items-center text-sm text-gray-600" v-if="customer.email">
              <svg class="h-4 w-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
              </svg>
              {{ customer.email }}
            </div>
            <div class="flex items-start text-sm text-gray-600" v-if="customer.address">
              <svg class="h-4 w-4 mr-2 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/>
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/>
              </svg>
              <span class="line-clamp-2">{{ customer.address }}</span>
            </div>
          </div>

          <!-- Customer Stats -->
          <div class="grid grid-cols-2 gap-4 mb-4 p-3 bg-gray-50 rounded-lg">
            <div class="text-center">
              <div class="text-lg font-semibold text-gray-900">{{ customer.totalOrders }}</div>
              <div class="text-xs text-gray-500">Đơn hàng</div>
            </div>
            <div class="text-center">
              <div class="text-lg font-semibold text-green-600">{{ formatCurrency(customer.totalSpent) }}</div>
              <div class="text-xs text-gray-500">Tổng chi tiêu</div>
            </div>
          </div>

          <!-- Actions -->
          <div class="flex space-x-2">
            <button 
              @click="viewCustomer(customer)"
              class="flex-1 bg-blue-50 text-blue-600 hover:bg-blue-100 px-3 py-2 rounded-md text-sm font-medium"
            >
              Xem chi tiết
            </button>
            <button 
              @click="editCustomer(customer)"
              class="flex-1 bg-gray-50 text-gray-600 hover:bg-gray-100 px-3 py-2 rounded-md text-sm font-medium"
            >
              Chỉnh sửa
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div v-if="filteredCustomers.length === 0" class="text-center py-12">
      <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
      </svg>
      <h3 class="mt-2 text-sm font-medium text-gray-900">Không tìm thấy khách hàng</h3>
      <p class="mt-1 text-sm text-gray-500">Hãy thử thay đổi bộ lọc hoặc thêm khách hàng mới.</p>
    </div>

    <!-- Pagination -->
    <div class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6 mt-6" v-if="filteredCustomers.length > 0">
      <div class="flex-1 flex justify-between sm:hidden">
        <button class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50">
          Trước
        </button>
        <button class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50">
          Sau
        </button>
      </div>
      <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
        <div>
          <p class="text-sm text-gray-700">
            Hiển thị <span class="font-medium">{{ filteredCustomers.length }}</span> khách hàng
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'

export default {
  name: 'Customers',
  setup() {
    // Reactive data
    const searchQuery = ref('')
    const selectedType = ref('')
    const selectedStatus = ref('')
    const showAddModal = ref(false)
    
    // Mock data
    const customers = ref([
      {
        id: 1,
        name: 'Nguyễn Văn An',
        phone: '0901234567',
        email: 'an.nguyen@email.com',
        address: '123 Đường ABC, Quận 1, TP.HCM',
        type: 'INDIVIDUAL',
        status: 'ACTIVE',
        totalOrders: 15,
        totalSpent: 25000000
      },
      {
        id: 2,
        name: 'Công ty TNHH Vận tải XYZ',
        phone: '0902345678',
        email: 'contact@vantaixyz.com',
        address: '456 Đường DEF, Quận Bình Thạnh, TP.HCM',
        type: 'BUSINESS',
        status: 'ACTIVE',
        totalOrders: 45,
        totalSpent: 150000000
      },
      {
        id: 3,
        name: 'Trần Thị Bình',
        phone: '0903456789',
        email: null,
        address: '789 Đường GHI, Quận 7, TP.HCM',
        type: 'INDIVIDUAL',
        status: 'INACTIVE',
        totalOrders: 3,
        totalSpent: 2500000
      }
    ])

    // Computed properties
    const filteredCustomers = computed(() => {
      let filtered = customers.value

      if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        filtered = filtered.filter(customer => 
          customer.name.toLowerCase().includes(query) ||
          customer.phone.includes(query) ||
          (customer.email && customer.email.toLowerCase().includes(query))
        )
      }

      if (selectedType.value) {
        filtered = filtered.filter(customer => customer.type === selectedType.value)
      }

      if (selectedStatus.value) {
        filtered = filtered.filter(customer => customer.status === selectedStatus.value)
      }

      return filtered
    })

    // Methods
    const getTypeClass = (type) => {
      switch (type) {
        case 'INDIVIDUAL': return 'bg-blue-100 text-blue-800'
        case 'BUSINESS': return 'bg-purple-100 text-purple-800'
        default: return 'bg-gray-100 text-gray-800'
      }
    }

    const getTypeText = (type) => {
      switch (type) {
        case 'INDIVIDUAL': return 'Cá nhân'
        case 'BUSINESS': return 'Doanh nghiệp'
        default: return 'Không xác định'
      }
    }

    const getStatusClass = (status) => {
      switch (status) {
        case 'ACTIVE': return 'bg-green-100 text-green-800'
        case 'INACTIVE': return 'bg-yellow-100 text-yellow-800'
        case 'BLOCKED': return 'bg-red-100 text-red-800'
        default: return 'bg-gray-100 text-gray-800'
      }
    }

    const getStatusText = (status) => {
      switch (status) {
        case 'ACTIVE': return 'Hoạt động'
        case 'INACTIVE': return 'Không hoạt động'
        case 'BLOCKED': return 'Bị khóa'
        default: return 'Không xác định'
      }
    }

    const formatCurrency = (amount) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(amount)
    }

    const applyFilters = () => {
      console.log('Applying filters...')
    }

    const clearFilters = () => {
      searchQuery.value = ''
      selectedType.value = ''
      selectedStatus.value = ''
    }

    const viewCustomer = (customer) => {
      console.log('View customer:', customer)
      // Logic to view customer details
    }

    const editCustomer = (customer) => {
      console.log('Edit customer:', customer)
      // Logic to edit customer
    }

    // Lifecycle
    onMounted(() => {
      console.log('Customers component mounted')
    })

    return {
      searchQuery,
      selectedType,
      selectedStatus,
      showAddModal,
      customers,
      filteredCustomers,
      getTypeClass,
      getTypeText,
      getStatusClass,
      getStatusText,
      formatCurrency,
      applyFilters,
      clearFilters,
      viewCustomer,
      editCustomer
    }
  }
}
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
