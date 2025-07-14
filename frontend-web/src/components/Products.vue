<template>
  <div class="container mx-auto px-4 py-6">
    <!-- Header -->
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Quản lý sản phẩm</h1>
      <button 
        @click="showAddModal = true"
        class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg flex items-center"
      >
        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
        </svg>
        Thêm sản phẩm
      </button>
    </div>

    <!-- Training Assistant -->
    <div class="bg-white p-4 rounded-lg shadow-sm mb-6">
      <div class="flex justify-between items-start mb-4">
        <h2 class="text-lg font-semibold text-gray-900">Tìm kiếm & Lọc</h2>
        <!-- <TrainingAssistant 
          :current-search-query="searchQuery"
          :selected-product-id="selectedProductForTraining"
          @search-suggestion="handleSearchSuggestion"
          @help-request="handleHelpRequest"
        /> -->
      </div>
      
      <!-- Filters -->
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
          <input 
            v-model="searchQuery"
            type="text" 
            placeholder="Tên sản phẩm, mã sản phẩm..."
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Danh mục</label>
          <select 
            v-model="selectedCategory"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Tất cả</option>
            <option v-for="category in categories" :key="category.id" :value="category.id">
              {{ category.name }}
            </option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Nhà cung cấp</label>
          <select 
            v-model="selectedSupplier"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Tất cả</option>
            <option v-for="supplier in suppliers" :key="supplier.id" :value="supplier.id">
              {{ supplier.name }}
            </option>
          </select>
        </div>
        <div class="flex items-end">
          <button 
            @click="loadProducts"
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

    <!-- Products Table -->
    <div class="bg-white rounded-lg shadow-sm overflow-hidden">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Sản phẩm
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Danh mục
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Nhà cung cấp
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
              <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                Thao tác
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="product in products" :key="product.id" class="hover:bg-gray-50">
              <td class="px-6 py-4">
                <div class="flex items-center">
                  <div class="h-12 w-12 rounded-lg bg-gray-200 flex items-center justify-center mr-4">
                    <svg class="h-6 w-6 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"></path>
                    </svg>
                  </div>
                  <div>
                    <div class="font-medium text-gray-900">{{ product.name }}</div>
                    <div class="text-sm text-gray-500">{{ product.code }}</div>
                  </div>
                </div>
              </td>
              <td class="px-6 py-4 text-sm text-gray-900">
                {{ getCategoryName(product.category?.id) }}
              </td>
              <td class="px-6 py-4 text-sm text-gray-900">
                {{ getSupplierName(product.supplier?.id) }}
              </td>
              <td class="px-6 py-4">
                <span :class="getStockClass(product.quantity)" class="px-2 py-1 text-xs font-medium rounded-full">
                  {{ product.quantity }}
                </span>
              </td>
              <td class="px-6 py-4 text-sm text-gray-900">
                {{ formatCurrency(product.sellingPrice) }}
              </td>
              <td class="px-6 py-4">
                <span :class="getStatusClass(product.status)" class="px-2 py-1 text-xs font-medium rounded-full">
                  {{ getStatusText(product.status) }}
                </span>
              </td>
              <td class="px-6 py-4 text-right text-sm font-medium">
                <button 
                  @click="editProduct(product)"
                  class="text-blue-600 hover:text-blue-900 mr-3"
                >
                  Sửa
                </button>
                <button 
                  @click="deleteProduct(product.id)"
                  class="text-red-600 hover:text-red-900"
                >
                  Xóa
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Pagination -->
    <div class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6 mt-4">
      <div class="flex-1 flex justify-between sm:hidden">
        <button 
          :disabled="currentPage === 0"
          @click="changePage(currentPage - 1)"
          class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50"
        >
          Trước
        </button>
        <button 
          :disabled="currentPage >= totalPages - 1"
          @click="changePage(currentPage + 1)"
          class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50"
        >
          Sau
        </button>
      </div>
      <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
        <div>
          <p class="text-sm text-gray-700">
            Hiển thị 
            <span class="font-medium">{{ (currentPage * pageSize) + 1 }}</span>
            đến 
            <span class="font-medium">{{ Math.min((currentPage + 1) * pageSize, totalElements) }}</span>
            của 
            <span class="font-medium">{{ totalElements }}</span>
            kết quả
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
import { ref, reactive, onMounted, computed } from 'vue';
import { productsAPI } from '../api/products.js';
// import TrainingAssistant from './TrainingAssistant.vue';

export default {
  name: 'Products',
  components: {
    // TrainingAssistant
  },
  setup() {
    // Reactive data
    const loading = ref(false);
    const searchQuery = ref('');
    const selectedCategory = ref('');
    const selectedSupplier = ref('');
    const showAddModal = ref(false);
    const selectedProductForTraining = ref(null);
    
    // Pagination
    const currentPage = ref(0);
    const pageSize = ref(10);
    const totalElements = ref(0);
    const totalPages = ref(0);
    
    // Data
    const products = ref([]);
    const categories = ref([]);
    const suppliers = ref([]);

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
    const loadProducts = async () => {
      try {
        loading.value = true;
        const response = await productsAPI.getProducts(
          currentPage.value,
          pageSize.value,
          searchQuery.value,
          selectedCategory.value || null,
          selectedSupplier.value || null
        );
        
        products.value = response.data.content || response.data;
        totalElements.value = response.data.totalElements || response.data.length;
        totalPages.value = response.data.totalPages || Math.ceil(totalElements.value / pageSize.value);
      } catch (error) {
        console.error('Error loading products:', error);
        alert('Có lỗi khi tải danh sách sản phẩm');
      } finally {
        loading.value = false;
      }
    };

    const loadCategories = async () => {
      try {
        const response = await productsAPI.getCategories();
        categories.value = response.data;
      } catch (error) {
        console.error('Error loading categories:', error);
      }
    };

    const loadSuppliers = async () => {
      try {
        const response = await productsAPI.getSuppliers();
        suppliers.value = response.data;
      } catch (error) {
        console.error('Error loading suppliers:', error);
      }
    };

    const changePage = (page) => {
      currentPage.value = page;
      loadProducts();
    };

    const clearFilters = () => {
      searchQuery.value = '';
      selectedCategory.value = '';
      selectedSupplier.value = '';
      currentPage.value = 0;
      loadProducts();
    };

    const getCategoryName = (categoryId) => {
      const category = categories.value.find(c => c.id === categoryId);
      return category ? category.name : 'N/A';
    };

    const getSupplierName = (supplierId) => {
      if (!suppliers.value || !Array.isArray(suppliers.value)) return 'N/A';
      const supplier = suppliers.value.find(s => s.id === supplierId);
      return supplier ? supplier.name : 'N/A';
    };

    const getStockClass = (quantity) => {
      if (quantity === 0) return 'bg-red-100 text-red-800';
      if (quantity < 10) return 'bg-yellow-100 text-yellow-800';
      return 'bg-green-100 text-green-800';
    };

    const getStatusClass = (status) => {
      switch (status) {
        case 'ACTIVE': return 'bg-green-100 text-green-800';
        case 'OUT_OF_STOCK': return 'bg-red-100 text-red-800';
        case 'DISCONTINUED': return 'bg-gray-100 text-gray-800';
        default: return 'bg-gray-100 text-gray-800';
      }
    };

    const getStatusText = (status) => {
      switch (status) {
        case 'ACTIVE': return 'Hoạt động';
        case 'OUT_OF_STOCK': return 'Hết hàng';
        case 'DISCONTINUED': return 'Ngừng bán';
        default: return 'Không xác định';
      }
    };

    const formatCurrency = (amount) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(amount);
    };

    const editProduct = (product) => {
      console.log('Edit product:', product);
      // TODO: Implement edit modal
    };

    const deleteProduct = async (id) => {
      if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')) {
        try {
          await productsAPI.deleteProduct(id);
          loadProducts();
          alert('Xóa sản phẩm thành công');
        } catch (error) {
          console.error('Error deleting product:', error);
          alert('Có lỗi khi xóa sản phẩm');
        }
      }
    };

    const handleSearchSuggestion = (suggestion) => {
      searchQuery.value = suggestion.query || suggestion;
      loadProducts();
    };

    const handleHelpRequest = (request) => {
      console.log('Help request:', request);
      alert('Yêu cầu hỗ trợ đã được ghi nhận. Nhân viên senior sẽ hỗ trợ bạn sớm nhất.');
    };

    const selectProductForTraining = (product) => {
      selectedProductForTraining.value = product.id;
      console.log('Selected product for training:', product);
    };

    // Lifecycle
    onMounted(() => {
      loadProducts();
      loadCategories();
      loadSuppliers();
    });

    return {
      loading,
      searchQuery,
      selectedCategory,
      selectedSupplier,
      showAddModal,
      selectedProductForTraining,
      currentPage,
      pageSize,
      totalElements,
      totalPages,
      products,
      categories,
      suppliers,
      visiblePages,
      loadProducts,
      changePage,
      clearFilters,
      getCategoryName,
      getSupplierName,
      getStockClass,
      getStatusClass,
      getStatusText,
      formatCurrency,
      editProduct,
      deleteProduct,
      handleSearchSuggestion,
      handleHelpRequest,
      selectProductForTraining
    };
  }
};
</script>