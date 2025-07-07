<template>
  <div v-if="show" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-lg p-6 w-full max-w-2xl mx-4 max-h-[90vh] overflow-y-auto">
      <div class="flex justify-between items-center mb-6">
        <h3 class="text-lg font-semibold text-gray-900">{{ title }}</h3>
        <button @click="closeModal" class="text-gray-400 hover:text-gray-600">
          <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <!-- Import Progress -->
      <div v-if="status !== 'idle'" class="mb-6">
        <div class="bg-blue-50 border border-blue-200 rounded-lg p-4">
          <div class="flex items-center">
            <div v-if="status === 'uploading'" class="flex items-center">
              <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-blue-500" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              <span class="text-blue-700">{{ loadingMessage }}</span>
            </div>
            <div v-else-if="status === 'completed'" class="flex items-center">
              <svg class="mr-3 h-5 w-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <span class="text-green-700">{{ successMessage }}</span>
            </div>
            <div v-else-if="status === 'error'" class="flex items-center">
              <svg class="mr-3 h-5 w-5 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <div class="text-red-700">
                <div class="font-medium">Có lỗi xảy ra!</div>
                <div v-if="results && results.message" class="text-sm mt-1">
                  {{ results.message }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Import Results -->
      <div v-if="results" class="mb-6">
        <!-- Error Message Display -->
        <div v-if="!results.success && results.message" class="bg-red-50 border border-red-200 rounded-lg p-4 mb-4">
          <div class="flex items-start">
            <svg class="mr-3 h-5 w-5 text-red-500 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <div>
              <h5 class="font-medium text-red-800 mb-1">Lỗi import:</h5>
              <p class="text-sm text-red-700">{{ results.message }}</p>
            </div>
          </div>
        </div>

        <!-- Success Results -->
        <div v-if="results.success || results.summary" class="bg-gray-50 rounded-lg p-4">
          <h4 class="font-medium text-gray-900 mb-3">Kết quả import:</h4>
          <div class="grid grid-cols-2 gap-4 text-sm">
            <div class="flex justify-between">
              <span class="text-gray-600">Tổng dòng:</span>
              <span class="font-medium">{{ results.summary?.totalRows || 0 }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">Thành công:</span>
              <span class="font-medium text-green-600">{{ results.summary?.successCount || results.summary?.importedRows || 0 }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">Lỗi:</span>
              <span class="font-medium text-red-600">{{ results.summary?.errorCount || results.summary?.failedRows || 0 }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">Trùng lặp:</span>
              <span class="font-medium text-yellow-600">{{ results.summary?.duplicateCount || results.summary?.duplicateRows || 0 }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">Tỷ lệ thành công:</span>
              <span class="font-medium">{{ calculateSuccessRate() }}%</span>
            </div>
            <div v-if="results.summary?.processingTime" class="flex justify-between">
              <span class="text-gray-600">Thời gian xử lý:</span>
              <span class="font-medium">{{ results.summary.processingTime }}ms</span>
            </div>
          </div>
          
          <!-- Error Details -->
          <div v-if="results.errors && results.errors.length > 0" class="mt-4">
            <h5 class="font-medium text-red-600 mb-2">Chi tiết lỗi:</h5>
            <div class="bg-red-50 rounded p-3 max-h-40 overflow-y-auto">
              <div v-for="error in results.errors" :key="error.row || error.line" class="text-sm text-red-700 mb-1">
                <span class="font-medium">{{ getErrorPrefix(error) }}:</span> {{ error.message }}
              </div>
            </div>
          </div>

          <!-- Warning Details -->
          <div v-if="results.warnings && results.warnings.length > 0" class="mt-4">
            <h5 class="font-medium text-yellow-600 mb-2">Cảnh báo:</h5>
            <div class="bg-yellow-50 rounded p-3 max-h-40 overflow-y-auto">
              <div v-for="warning in results.warnings" :key="warning.row || warning.line" class="text-sm text-yellow-700 mb-1">
                <span class="font-medium">{{ getErrorPrefix(warning) }}:</span> {{ warning.message }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- File Upload -->
      <div v-if="status === 'idle'" class="mb-6">
        <div class="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center hover:border-gray-400 transition-colors">
          <input ref="fileInput" type="file" :accept="acceptedFileTypes" @change="handleFileSelect" class="hidden">
          <div @click="$refs.fileInput.click()" class="cursor-pointer">
            <svg class="mx-auto h-12 w-12 text-gray-400 mb-4" stroke="currentColor" fill="none" viewBox="0 0 48 48">
              <path d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
            </svg>
            <p class="text-lg font-medium text-gray-900 mb-2">{{ uploadMessage }}</p>
            <p class="text-sm text-gray-500">Hoặc kéo thả file vào đây</p>
            <p class="text-xs text-gray-400 mt-2">{{ fileConstraints }}</p>
          </div>
        </div>
        
        <div v-if="selectedFile" class="mt-4 p-3 bg-blue-50 rounded-lg">
          <div class="flex items-center justify-between">
            <div class="flex items-center">
              <svg class="h-5 w-5 text-blue-500 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
              <span class="text-sm font-medium text-blue-700">{{ selectedFile.name }}</span>
              <span class="text-xs text-blue-500 ml-2">({{ formatFileSize(selectedFile.size) }})</span>
            </div>
            <button @click="clearSelectedFile" class="text-blue-400 hover:text-blue-600">
              <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>
      </div>

      <!-- Import Options -->
      <div v-if="selectedFile && status === 'idle' && showOptions" class="mb-6">
        <h4 class="font-medium text-gray-900 mb-3">Tùy chọn import:</h4>
        <slot name="options">
          <label class="flex items-center">
            <input v-model="updateExisting" type="checkbox" class="rounded border-gray-300 text-blue-600 shadow-sm focus:border-blue-300 focus:ring focus:ring-blue-200 focus:ring-opacity-50">
            <span class="ml-2 text-sm text-gray-700">{{ updateOptionText }}</span>
          </label>
        </slot>
      </div>

      <!-- Action Buttons -->
      <div class="flex justify-between">
        <div>
          <button v-if="showTemplateButton" @click="downloadTemplate" class="btn-secondary mr-3">
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
            Tải template
          </button>
          <button v-if="showGuideButton" @click="$emit('show-guide')" class="btn-secondary">
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.228 9c.549-1.165 2.03-2 3.772-2 2.21 0 4 1.343 4 3 0 1.4-1.278 2.575-3.006 2.907-.542.104-.994.54-.994 1.093m0 3h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            Hướng dẫn
          </button>
        </div>
        
        <div class="flex space-x-3">
          <button @click="closeModal" class="btn-secondary">
            Đóng
          </button>
          <button v-if="selectedFile && status === 'idle'" @click="handleImport" class="btn-primary">
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
            </svg>
            {{ importButtonText }}
          </button>
          <button v-if="status === 'completed'" @click="resetModal" class="btn-primary">
            Import file khác
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ImportModal',
  props: {
    show: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: 'Import dữ liệu từ CSV'
    },
    uploadMessage: {
      type: String,
      default: 'Chọn file CSV để import'
    },
    fileConstraints: {
      type: String,
      default: 'Chỉ chấp nhận file .csv, tối đa 10MB'
    },
    acceptedFileTypes: {
      type: String,
      default: '.csv'
    },
    maxFileSize: {
      type: Number,
      default: 10 * 1024 * 1024 // 10MB
    },
    status: {
      type: String,
      default: 'idle' // 'idle', 'uploading', 'completed', 'error'
    },
    results: {
      type: Object,
      default: null
    },
    loadingMessage: {
      type: String,
      default: 'Đang xử lý file...'
    },
    successMessage: {
      type: String,
      default: 'Import hoàn tất!'
    },
    importButtonText: {
      type: String,
      default: 'Import ngay'
    },
    updateOptionText: {
      type: String,
      default: 'Cập nhật dữ liệu đã tồn tại'
    },
    showOptions: {
      type: Boolean,
      default: true
    },
    showTemplateButton: {
      type: Boolean,
      default: true
    },
    showGuideButton: {
      type: Boolean,
      default: true
    }
  },
  emits: ['close', 'import', 'download-template', 'show-guide'],
  data() {
    return {
      selectedFile: null,
      updateExisting: false
    }
  },
  methods: {
    handleFileSelect(event) {
      const file = event.target.files[0];
      if (file) {
        // Validate file type
        const fileExtension = '.' + file.name.split('.').pop().toLowerCase();
        if (!this.acceptedFileTypes.includes(fileExtension)) {
          alert(`Vui lòng chọn file đúng định dạng: ${this.acceptedFileTypes}`);
          return;
        }
        
        // Validate file size
        if (file.size > this.maxFileSize) {
          const maxSizeMB = (this.maxFileSize / (1024 * 1024)).toFixed(1);
          alert(`File quá lớn! Kích thước tối đa ${maxSizeMB}MB`);
          return;
        }
        
        this.selectedFile = file;
      }
    },
    
    clearSelectedFile() {
      this.selectedFile = null;
      if (this.$refs.fileInput) {
        this.$refs.fileInput.value = '';
      }
    },
    
    handleImport() {
      if (!this.selectedFile) {
        alert('Vui lòng chọn file để import');
        return;
      }
      
      this.$emit('import', {
        file: this.selectedFile,
        updateExisting: this.updateExisting
      });
    },
    
    downloadTemplate() {
      this.$emit('download-template');
    },
    
    closeModal() {
      this.resetModal();
      this.$emit('close');
    },
    
    resetModal() {
      this.selectedFile = null;
      this.updateExisting = false;
      if (this.$refs.fileInput) {
        this.$refs.fileInput.value = '';
      }
    },
    
    formatFileSize(bytes) {
      if (bytes === 0) return '0 Bytes';
      const k = 1024;
      const sizes = ['Bytes', 'KB', 'MB', 'GB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    },
    
    calculateSuccessRate() {
      if (!this.results || !this.results.summary) return 0;
      
      const total = this.results.summary.totalRows || 0;
      const success = this.results.summary.successCount || this.results.summary.importedRows || 0;
      
      if (total === 0) return 0;
      return ((success / total) * 100).toFixed(1);
    },
    
    getErrorPrefix(error) {
      if (error.row) return `Dòng ${error.row}`;
      if (error.line) return `Dòng ${error.line}`;
      if (error.rowNumber) return `Dòng ${error.rowNumber}`;
      return 'Lỗi';
    }
  },
  
  watch: {
    show(newVal) {
      if (!newVal) {
        this.resetModal();
      }
    }
  }
}
</script>

<style scoped>
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
