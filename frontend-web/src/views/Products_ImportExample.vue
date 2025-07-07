<template>
  <div class="products-page">
    <!-- Header -->
    <div class="mb-8">
      <h1 class="text-3xl font-bold text-gray-900 mb-2">Sản phẩm</h1>
      <p class="text-gray-600">Quản lý danh mục sản phẩm và phụ tùng</p>
    </div>

    <!-- Actions -->
    <div class="card">
      <div class="flex items-center justify-between mb-6">
        <h2 class="text-xl font-semibold text-gray-900">Danh sách sản phẩm</h2>
        <div class="flex items-center space-x-3">
          <!-- Import Button -->
          <button @click="showImportModal = true" class="btn-secondary">
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
                    d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M9 19l3 3m0 0l3-3m-3 3V10" />
            </svg>
            Import Excel/CSV
          </button>
          
          <button class="btn-primary">
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
            Thêm sản phẩm
          </button>
        </div>
      </div>

      <!-- Products Table -->
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Mã sản phẩm
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Tên sản phẩm
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Danh mục
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Giá bán
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Tồn kho
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="product in products" :key="product.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                {{ product.code }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                {{ product.name }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ product.category }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                ₫{{ formatCurrency(product.price) }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                {{ product.stock }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Import Modal - Tái sử dụng component -->
    <ImportModal 
      :show="showImportModal"
      :title="importConfig.title"
      :upload-message="importConfig.uploadMessage"
      :update-option-text="importConfig.updateOptionText"
      :accepted-file-types="importConfig.acceptedFileTypes"
      :file-constraints="'Chỉ chấp nhận file .csv/.xlsx/.xls, tối đa 10MB'"
      :status="importState.status"
      :results="importState.results"
      @close="showImportModal = false"
      @import="handleImport"
      @download-template="handleDownloadTemplate"
      @show-guide="showImportGuide = true"
    />

    <!-- Import Guide Modal - Có thể tùy chỉnh cho từng loại -->
    <div v-if="showImportGuide" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto">
        <div class="p-6">
          <div class="flex justify-between items-center mb-6">
            <h3 class="text-lg font-semibold text-gray-900">Hướng dẫn import sản phẩm</h3>
            <button @click="showImportGuide = false" class="text-gray-400 hover:text-gray-600">
              <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          
          <div class="space-y-6">
            <div>
              <h4 class="font-medium text-gray-900 mb-3">1. Chuẩn bị file</h4>
              <div class="bg-gray-50 rounded-lg p-4">
                <p class="text-sm text-gray-700 mb-3">File cần có các cột sau:</p>
                <ul class="text-sm text-gray-600 space-y-1">
                  <li>• <strong>Mã sản phẩm</strong> (bắt buộc)</li>
                  <li>• <strong>Tên sản phẩm</strong> (bắt buộc)</li>
                  <li>• <strong>Danh mục</strong></li>
                  <li>• <strong>Giá nhập</strong></li>
                  <li>• <strong>Giá bán</strong></li>
                  <li>• <strong>Tồn kho</strong></li>
                  <li>• <strong>Mô tả</strong></li>
                </ul>
              </div>
            </div>
            
            <div>
              <h4 class="font-medium text-gray-900 mb-3">2. Định dạng hỗ trợ</h4>
              <div class="bg-blue-50 rounded-lg p-4">
                <ul class="text-sm text-blue-700 space-y-1">
                  <li>• CSV (.csv)</li>
                  <li>• Excel (.xlsx, .xls)</li>
                  <li>• Mã hóa: UTF-8</li>
                  <li>• Tối đa 10MB</li>
                </ul>
              </div>
            </div>
          </div>
          
          <div class="mt-6 flex justify-end">
            <button @click="showImportGuide = false" class="btn-primary">
              Đã hiểu
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/services/api'
import ImportModal from '@/components/ImportModal.vue'
import { useImport } from '@/composables/useImport'

export default {
  name: 'Products',
  components: {
    ImportModal
  },
  setup() {
    // Sử dụng composable với loại 'products'
    const { 
      importState, 
      config: importConfig, 
      executeImport, 
      downloadTemplate,
      setFile,
      resetImport
    } = useImport('products');
    
    return {
      importState,
      importConfig,
      executeImport,
      downloadTemplate,
      setFile,
      resetImport
    };
  },
  data() {
    return {
      showImportModal: false,
      showImportGuide: false,
      products: [
        {
          id: 1,
          code: 'PT001',
          name: 'Lọc dầu Toyota Vios',
          category: 'Lọc dầu',
          price: 150000,
          stock: 25
        },
        {
          id: 2,
          code: 'PT002',
          name: 'Má phanh Honda City',
          category: 'Phanh',
          price: 300000,
          stock: 15
        }
      ]
    }
  },
  methods: {
    formatCurrency(value) {
      return new Intl.NumberFormat('vi-VN').format(value);
    },
    
    // Import methods - Tương tự như Suppliers
    async handleImport(importData) {
      this.setFile(importData.file);
      this.importState.updateExisting = importData.updateExisting;
      
      const result = await this.executeImport();
      
      if (result.success && this.importState.results.summary && this.importState.results.summary.successCount > 0) {
        await this.refreshProducts();
      }
    },
    
    async handleDownloadTemplate() {
      const result = await this.downloadTemplate();
      if (!result.success) {
        alert(result.message);
      }
    },
    
    async refreshProducts() {
      try {
        const response = await api.get('/products');
        if (response.data) {
          this.products = response.data;
        }
      } catch (error) {
        console.error('Error refreshing products:', error);
      }
    }
  }
}
</script>

<style scoped>
/* Tái sử dụng CSS từ Suppliers.vue */
.products-page {
  max-width: 100%;
}

.card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  border: 1px solid #e5e7eb;
  padding: 24px;
}

.btn-primary {
  background-color: #0070F4;
  color: white;
  padding: 8px 16px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s;
  display: flex;
  align-items: center;
  font-size: 14px;
  font-weight: 500;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.btn-secondary {
  background-color: #f3f4f6;
  color: #374151;
  padding: 8px 16px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s;
  display: flex;
  align-items: center;
  font-size: 14px;
  font-weight: 500;
}

.btn-secondary:hover {
  background-color: #e5e7eb;
}
</style>
