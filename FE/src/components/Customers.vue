<template>
  <div class="container mx-auto px-4 py-6">
    <!-- Header -->
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Quản lý khách hàng</h1>
      <button 
        @click="showAddModal = true"
        class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg flex items-center"
      >
        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
        </svg>
        Thêm khách hàng
      </button>
    </div>

    <!-- Filters -->
    <div class="bg-white p-4 rounded-lg shadow-sm mb-6">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
          <input 
            v-model="searchQuery"
            type="text" 
            placeholder="Tên, số điện thoại, email..."
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
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
            @click="loadCustomers"
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

    <!-- Customers Grid -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div 
        v-for="customer in customers" 
        :key="customer.id"
        class="bg-white rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition-shadow"
      >
        <div class="p-6">
          <div class="flex items-start justify-between mb-4">
            <div class="flex items-center">
              <div class="h-12 w-12 rounded-full bg-blue-100 flex items-center justify-center mr-3">
                <svg class="h-6 w-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
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
          
          <div class="space-y-2 mb-4">
            <div class="flex items-center text-sm text-gray-600">
              <svg class="h-4 w-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"></path>
              </svg>
              {{ customer.phone }}
            </div>
            <div v-if="customer.email" class="flex items-center text-sm text-gray-600">
              <svg class="h-4 w-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"></path>
              </svg>
              {{ customer.email }}
            </div>
            <div v-if="customer.address" class="flex items-start text-sm text-gray-600">
              <svg class="h-4 w-4 mr-2 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path>
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path>
              </svg>
              <span class="line-clamp-2">{{ customer.address }}</span>
            </div>
          </div>
          
          <div class="grid grid-cols-2 gap-4 mb-4 p-3 bg-gray-50 rounded-lg">
            <div class="text-center">
              <div class="text-lg font-semibold text-gray-900">{{ customer.totalOrders || 0 }}</div>
              <div class="text-xs text-gray-500">Đơn hàng</div>
            </div>
            <div class="text-center">
              <div class="text-lg font-semibold text-green-600">{{ formatCurrency(customer.totalSpent || 0) }}</div>
              <div class="text-xs text-gray-500">Tổng chi tiêu</div>
            </div>
          </div>
          
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
    <div v-if="customers.length === 0 && !loading" class="text-center py-12">
      <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
      </svg>
      <h3 class="mt-2 text-sm font-medium text-gray-900">Không tìm thấy khách hàng</h3>
      <p class="mt-1 text-sm text-gray-500">Hãy thử thay đổi bộ lọc hoặc thêm khách hàng mới.</p>
    </div>

    <!-- Pagination -->
    <div v-if="customers.length > 0" class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6 mt-6">
      <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
        <div>
          <p class="text-sm text-gray-700">
            Hiển thị 
            <span class="font-medium">{{ (currentPage * pageSize) + 1 }}</span>
            đến 
            <span class="font-medium">{{ Math.min((currentPage + 1) * pageSize, totalElements) }}</span>
            của 
            <span class="font-medium">{{ totalElements }}</span>
            khách hàng
          </p>
        </div>
        <div>
          <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px">
            <button 
              :disabled="currentPage === 0"
              @click="changePage(currentPage - 1)"
              class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
            >
              <span class="sr-only">Previous</span>
              <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd"></path>
              </svg>
            </button>
            <button 
              v-for="page in visiblePages" 
              :key="page"
              @click="changePage(page - 1)"
              :class="[
                'relative inline-flex items-center px-4 py-2 border text-sm font-medium',
                page - 1 === currentPage 
                  ? 'z-10 bg-blue-50 border-blue-500 text-blue-600' 
                  : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50'
              ]"
            >
              {{ page }}
            </button>
            <button 
              :disabled="currentPage >= totalPages - 1"
              @click="changePage(currentPage + 1)"
              class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
            >
              <span class="sr-only">Next</span>
              <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd"></path>
              </svg>
            </button>
          </nav>
        </div>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div class="mt-3 text-center">
          <div class="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-blue-100">
            <svg class="animate-spin h-6 w-6 text-blue-600" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
          </div>
          <h3 class="text-lg leading-6 font-medium text-gray-900 mt-2">Đang tải...</h3>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue';
import { customersApi } from '../api/customers.js';

export default {
  name: 'Customers',
  setup() {
    // Reactive data
    const loading = ref(false);
    const searchQuery = ref('');
    const selectedType = ref('');
    const selectedStatus = ref('');
    const showAddModal = ref(false);
    
    // Pagination
    const currentPage = ref(0);
    const pageSize = ref(10);
    const totalElements = ref(0);
    const totalPages = ref(0);
    
    // Data
    const customers = ref([]);

    // Computed
    const visiblePages = computed(() => {
      const pages = [];
      const start = Math.max(0, currentPage.value - 2);
      const end = Math.min(totalPages.value, currentPage.value + 3);
      
      for (let i = start; i < end; i++) {
        pages.push(i + 1);
      }
      return pages;
    });

    // Methods
    const loadCustomers = async () => {
      try {
        loading.value = true;
        const response = await customersApi.getAll(
          currentPage.value,
          pageSize.value,
          searchQuery.value,
          selectedType.value || null,
          selectedStatus.value || null
        );
        
        customers.value = response.data.content || response.data;
        totalElements.value = response.data.totalElements || response.data.length;
        totalPages.value = response.data.totalPages || Math.ceil(totalElements.value / pageSize.value);
      } catch (error) {
        console.error('Error loading customers:', error);
        alert('Có lỗi khi tải danh sách khách hàng');
      } finally {
        loading.value = false;
      }
    };

    const changePage = (page) => {
      currentPage.value = page;
      loadCustomers();
    };

    const clearFilters = () => {
      searchQuery.value = '';
      selectedType.value = '';
      selectedStatus.value = '';
      currentPage.value = 0;
      loadCustomers();
    };

    const getTypeClass = (type) => {
      switch (type) {
        case 'INDIVIDUAL': return 'bg-blue-100 text-blue-800';
        case 'BUSINESS': return 'bg-purple-100 text-purple-800';
        default: return 'bg-gray-100 text-gray-800';
      }
    };

    const getTypeText = (type) => {
      switch (type) {
        case 'INDIVIDUAL': return 'Cá nhân';
        case 'BUSINESS': return 'Doanh nghiệp';
        default: return 'Không xác định';
      }
    };

    const getStatusClass = (status) => {
      switch (status) {
        case 'ACTIVE': return 'bg-green-100 text-green-800';
        case 'INACTIVE': return 'bg-yellow-100 text-yellow-800';
        case 'BLOCKED': return 'bg-red-100 text-red-800';
        default: return 'bg-gray-100 text-gray-800';
      }
    };

    const getStatusText = (status) => {
      switch (status) {
        case 'ACTIVE': return 'Hoạt động';
        case 'INACTIVE': return 'Không hoạt động';
        case 'BLOCKED': return 'Bị khóa';
        default: return 'Không xác định';
      }
    };

    const formatCurrency = (amount) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(amount);
    };

    const viewCustomer = (customer) => {
      console.log('View customer:', customer);
      // TODO: Implement view modal
    };

    const editCustomer = (customer) => {
      console.log('Edit customer:', customer);
      // TODO: Implement edit modal
    };

    // Lifecycle
    onMounted(() => {
      loadCustomers();
    });

    return {
      loading,
      searchQuery,
      selectedType,
      selectedStatus,
      showAddModal,
      currentPage,
      pageSize,
      totalElements,
      totalPages,
      customers,
      visiblePages,
      loadCustomers,
      changePage,
      clearFilters,
      getTypeClass,
      getTypeText,
      getStatusClass,
      getStatusText,
      formatCurrency,
      viewCustomer,
      editCustomer
    };
  }
};
</script> 