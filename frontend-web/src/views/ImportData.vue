<template>
  <div class="import-data-page">
    <!-- Header -->
    <div class="bg-white shadow-sm border-b border-gray-200">
      <div class="px-6 py-4">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-2xl font-bold text-gray-900">Import Dữ Liệu</h1>
            <p class="text-gray-600 mt-1">Nhập dữ liệu từ file Excel vào hệ thống</p>
          </div>
          <div class="flex space-x-3">
            <button
              @click="downloadTemplate"
              :disabled="isDownloading"
              class="bg-gray-100 hover:bg-gray-200 disabled:bg-gray-300 text-gray-700 px-4 py-2 rounded-lg font-medium transition-colors"
            >
              <span v-if="isDownloading">🔄 Đang tải...</span>
              <span v-else>📥 Tải Template</span>
            </button>
            <button
              @click="showGuide = true"
              class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg font-medium transition-colors"
            >
              📖 Hướng Dẫn
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="max-w-7xl mx-auto px-6 py-6">
      <!-- Upload Section -->
      <div class="bg-white rounded-lg shadow-sm border border-gray-200 mb-6">
        <div class="p-6">
          <h2 class="text-lg font-semibold text-gray-900 mb-4">Chọn File Excel</h2>
          
          <!-- File Drop Zone -->
          <div
            @drop="handleDrop"
            @dragover.prevent
            @dragenter.prevent
            class="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center hover:border-blue-400 transition-colors"
            :class="{ 'border-blue-400 bg-blue-50': isDragging }"
          >
            <div class="space-y-4">
              <div class="text-4xl">📂</div>
              <div>
                <p class="text-lg font-medium text-gray-900">
                  Kéo thả file Excel vào đây hoặc
                </p>
                <label class="cursor-pointer">
                  <span class="text-blue-600 hover:text-blue-500 font-medium">chọn file</span>
                  <input
                    ref="fileInput"
                    type="file"
                    accept=".xlsx"
                    @change="handleFileSelect"
                    class="hidden"
                  />
                </label>
              </div>
              <p class="text-sm text-gray-500">
                Hỗ trợ file .xlsx, tối đa 10MB
              </p>
            </div>
          </div>

          <!-- Selected File Info -->
          <div v-if="selectedFile" class="mt-4 p-4 bg-gray-50 rounded-lg">
            <div class="flex items-center justify-between">
              <div class="flex items-center space-x-3">
                <div class="text-2xl">📄</div>
                <div>
                  <p class="font-medium text-gray-900">{{ selectedFile.name }}</p>
                  <p class="text-sm text-gray-500">{{ formatFileSize(selectedFile.size) }}</p>
                </div>
              </div>
              <button
                @click="clearFile"
                class="text-red-600 hover:text-red-700 p-2"
              >
                ✕
              </button>
            </div>
          </div>

          <!-- Import Button -->
          <div v-if="selectedFile" class="mt-4 flex justify-end">
            <button
              @click="importFile"
              :disabled="isImporting"
              class="bg-green-600 hover:bg-green-700 disabled:bg-gray-400 text-white px-6 py-2 rounded-lg font-medium transition-colors"
            >
              <span v-if="isImporting">🔄 Đang Import...</span>
              <span v-else>🚀 Bắt Đầu Import</span>
            </button>
          </div>
        </div>
      </div>

      <!-- Import Progress -->
      <div v-if="importProgress.show" class="bg-white rounded-lg shadow-sm border border-gray-200 mb-6">
        <div class="p-6">
          <h2 class="text-lg font-semibold text-gray-900 mb-4">Tiến Trình Import</h2>
          
          <div class="space-y-3">
            <div class="flex items-center justify-between">
              <span class="text-sm font-medium text-gray-900">{{ importProgress.status }}</span>
              <span class="text-sm text-gray-500">{{ importProgress.progress }}%</span>
            </div>
            <div class="w-full bg-gray-200 rounded-full h-2">
              <div
                class="bg-blue-600 h-2 rounded-full transition-all duration-300"
                :style="{ width: importProgress.progress + '%' }"
              ></div>
            </div>
          </div>
        </div>
      </div>

      <!-- Import Results -->
      <div v-if="importResult" class="bg-white rounded-lg shadow-sm border border-gray-200">
        <div class="p-6">
          <div class="flex items-center justify-between mb-4">
            <h2 class="text-lg font-semibold text-gray-900">Kết Quả Import</h2>
            <div class="flex items-center space-x-2">
              <span
                class="px-3 py-1 rounded-full text-sm font-medium"
                :class="importResult.success ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
              >
                {{ importResult.success ? '✅ Thành Công' : '❌ Có Lỗi' }}
              </span>
            </div>
          </div>

          <!-- Summary -->
          <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
            <div class="text-center p-4 bg-green-50 rounded-lg">
              <div class="text-2xl font-bold text-green-600">{{ getTotalSuccesses() }}</div>
              <div class="text-sm text-green-800">Thành Công</div>
            </div>
            <div class="text-center p-4 bg-yellow-50 rounded-lg">
              <div class="text-2xl font-bold text-yellow-600">{{ getTotalWarnings() }}</div>
              <div class="text-sm text-yellow-800">Cảnh Báo</div>
            </div>
            <div class="text-center p-4 bg-red-50 rounded-lg">
              <div class="text-2xl font-bold text-red-600">{{ getTotalErrors() }}</div>
              <div class="text-sm text-red-800">Lỗi</div>
            </div>
            <div class="text-center p-4 bg-blue-50 rounded-lg">
              <div class="text-2xl font-bold text-blue-600">{{ importResult.importTime }}s</div>
              <div class="text-sm text-blue-800">Thời Gian</div>
            </div>
          </div>

          <!-- Detailed Results -->
          <div class="space-y-4">
            <!-- Successes -->
            <div v-if="importResult.successes?.length > 0">
              <h3 class="font-medium text-green-800 mb-2">✅ Thành Công</h3>
              <div class="bg-green-50 rounded-lg p-4">
                <ul class="space-y-1">
                  <li v-for="success in importResult.successes" :key="success" class="text-sm text-green-700">
                    {{ success }}
                  </li>
                </ul>
              </div>
            </div>

            <!-- Warnings -->
            <div v-if="importResult.warnings?.length > 0">
              <h3 class="font-medium text-yellow-800 mb-2">⚠️ Cảnh Báo</h3>
              <div class="bg-yellow-50 rounded-lg p-4">
                <ul class="space-y-1">
                  <li v-for="warning in importResult.warnings" :key="warning" class="text-sm text-yellow-700">
                    {{ warning }}
                  </li>
                </ul>
              </div>
            </div>

            <!-- Errors -->
            <div v-if="importResult.errors?.length > 0">
              <h3 class="font-medium text-red-800 mb-2">❌ Lỗi</h3>
              <div class="bg-red-50 rounded-lg p-4">
                <ul class="space-y-1">
                  <li v-for="error in importResult.errors" :key="error" class="text-sm text-red-700">
                    {{ error }}
                  </li>
                </ul>
              </div>
            </div>
          </div>

          <!-- Actions -->
          <div class="mt-6 flex justify-end space-x-3">
            <button
              @click="clearResults"
              class="bg-gray-100 hover:bg-gray-200 text-gray-700 px-4 py-2 rounded-lg font-medium transition-colors"
            >
              Xóa Kết Quả
            </button>
            <button
              @click="downloadLog"
              class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg font-medium transition-colors"
            >
              📥 Tải Log
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Guide Modal -->
    <div v-if="showGuide" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg max-w-4xl max-h-[90vh] overflow-y-auto">
        <div class="p-6">
          <div class="flex items-center justify-between mb-4">
            <h2 class="text-xl font-bold text-gray-900">📖 Hướng Dẫn Import Excel</h2>
            <button @click="showGuide = false" class="text-gray-400 hover:text-gray-600">
              ✕
            </button>
          </div>

          <div class="space-y-6">
            <div>
              <h3 class="font-semibold text-gray-900 mb-2">Yêu Cầu File</h3>
              <ul class="list-disc list-inside space-y-1 text-gray-700">
                <li>Định dạng: .xlsx (Excel 2007 trở lên)</li>
                <li>Kích thước tối đa: 10MB</li>
                <li>Encoding: UTF-8</li>
              </ul>
            </div>

            <div>
              <h3 class="font-semibold text-gray-900 mb-2">Cấu Trúc File Excel</h3>
              <p class="text-gray-700 mb-3">File Excel cần có các sheet sau (tên sheet phải đúng):</p>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div class="border border-gray-200 rounded-lg p-4">
                  <h4 class="font-medium text-gray-900 mb-2">Categories</h4>
                  <ul class="text-sm text-gray-600 space-y-1">
                    <li>• name (bắt buộc)</li>
                    <li>• description</li>
                    <li>• parentCategory</li>
                  </ul>
                </div>
                <div class="border border-gray-200 rounded-lg p-4">
                  <h4 class="font-medium text-gray-900 mb-2">Suppliers</h4>
                  <ul class="text-sm text-gray-600 space-y-1">
                    <li>• name (bắt buộc)</li>
                    <li>• contactInfo</li>
                    <li>• address</li>
                  </ul>
                </div>
                <div class="border border-gray-200 rounded-lg p-4">
                  <h4 class="font-medium text-gray-900 mb-2">VehicleModels</h4>
                  <ul class="text-sm text-gray-600 space-y-1">
                    <li>• brand (bắt buộc)</li>
                    <li>• model (bắt buộc)</li>
                    <li>• year</li>
                  </ul>
                </div>
                <div class="border border-gray-200 rounded-lg p-4">
                  <h4 class="font-medium text-gray-900 mb-2">Products</h4>
                  <ul class="text-sm text-gray-600 space-y-1">
                    <li>• name (bắt buộc)</li>
                    <li>• sku (bắt buộc)</li>
                    <li>• price (bắt buộc)</li>
                    <li>• category</li>
                    <li>• description</li>
                  </ul>
                </div>
                <div class="border border-gray-200 rounded-lg p-4">
                  <h4 class="font-medium text-gray-900 mb-2">Customers</h4>
                  <ul class="text-sm text-gray-600 space-y-1">
                    <li>• name (bắt buộc)</li>
                    <li>• phone</li>
                    <li>• email</li>
                    <li>• address</li>
                  </ul>
                </div>
                <div class="border border-gray-200 rounded-lg p-4">
                  <h4 class="font-medium text-gray-900 mb-2">TrainingContent</h4>
                  <ul class="text-sm text-gray-600 space-y-1">
                    <li>• title (bắt buộc)</li>
                    <li>• content (bắt buộc)</li>
                    <li>• type</li>
                    <li>• tags</li>
                  </ul>
                </div>
              </div>
            </div>

            <div>
              <h3 class="font-semibold text-gray-900 mb-2">Lưu Ý Quan Trọng</h3>
              <ul class="list-disc list-inside space-y-1 text-gray-700">
                <li>Dòng đầu tiên của mỗi sheet phải là tiêu đề cột</li>
                <li>Các trường bắt buộc không được để trống</li>
                <li>Số điện thoại và email phải đúng định dạng</li>
                <li>Giá sản phẩm phải là số dương</li>
                <li>Dữ liệu trùng lặp sẽ được ghi đè</li>
              </ul>
            </div>
          </div>

          <div class="mt-6 flex justify-end">
            <button
              @click="showGuide = false"
              class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg font-medium transition-colors"
            >
              Đã Hiểu
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Message Notification -->
    <div v-if="message.show" class="fixed top-4 right-4 z-50">
      <div 
        class="px-6 py-4 rounded-lg shadow-lg max-w-md"
        :class="{
          'bg-green-100 border border-green-400 text-green-700': message.type === 'success',
          'bg-red-100 border border-red-400 text-red-700': message.type === 'error',
          'bg-blue-100 border border-blue-400 text-blue-700': message.type === 'info'
        }"
      >
        <div class="flex items-center justify-between">
          <span>{{ message.text }}</span>
          <button @click="message.show = false" class="ml-4 text-gray-500 hover:text-gray-700">
            ✕
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ImportData',
  data() {
    return {
      selectedFile: null,
      isImporting: false,
      isDownloading: false,
      isDragging: false,
      showGuide: false,
      message: {
        show: false,
        text: '',
        type: 'info'
      },
      importProgress: {
        show: false,
        status: '',
        progress: 0
      },
      importResult: null
    }
  },
  methods: {
    handleDrop(e) {
      e.preventDefault()
      this.isDragging = false
      
      const files = e.dataTransfer.files
      if (files.length > 0) {
        this.selectFile(files[0])
      }
    },
    
    handleFileSelect(e) {
      const files = e.target.files
      if (files.length > 0) {
        this.selectFile(files[0])
      }
    },
    
    selectFile(file) {
      if (!file.name.toLowerCase().endsWith('.xlsx')) {
        alert('Chỉ hỗ trợ file .xlsx')
        return
      }
      
      if (file.size > 10 * 1024 * 1024) { // 10MB
        alert('File quá lớn (tối đa 10MB)')
        return
      }
      
      this.selectedFile = file
      this.clearResults()
    },
    
    clearFile() {
      this.selectedFile = null
      this.$refs.fileInput.value = ''
    },
    
    async importFile() {
      if (!this.selectedFile) return
      
      this.isImporting = true
      this.importProgress.show = true
      this.importProgress.status = 'Đang tải file lên...'
      this.importProgress.progress = 10
      
      try {
        const formData = new FormData()
        formData.append('file', this.selectedFile)
        
        this.importProgress.status = 'Đang xử lý dữ liệu...'
        this.importProgress.progress = 30
        
        const response = await axios.post('http://localhost:8080/api/import/excel', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          },
          onUploadProgress: (progressEvent) => {
            const progress = Math.round((progressEvent.loaded / progressEvent.total) * 50)
            this.importProgress.progress = 30 + progress
          }
        })
        
        this.importProgress.status = 'Hoàn thành!'
        this.importProgress.progress = 100
        
        setTimeout(() => {
          this.importProgress.show = false
          this.importResult = response.data
        }, 1000)
        
      } catch (error) {
        console.error('Import error:', error)
        this.importProgress.show = false
        
        let errorMessage = 'Có lỗi xảy ra khi import'
        if (error.response?.data?.message) {
          errorMessage = error.response.data.message
        }
        
        this.importResult = {
          success: false,
          errors: [errorMessage],
          warnings: [],
          successes: []
        }
      } finally {
        this.isImporting = false
      }
    },
    
    clearResults() {
      this.importResult = null
      this.importProgress.show = false
    },
    
    async downloadTemplate() {
      try {
        this.isDownloading = true
        
        const response = await axios.get('http://localhost:8080/api/import/template', {
          responseType: 'blob'
        })
        
        // Create download link
        const blob = new Blob([response.data], { 
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' 
        })
        const url = URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = `template-import-data-${new Date().toISOString().slice(0, 10)}.xlsx`
        a.click()
        URL.revokeObjectURL(url)
        
        // Show success message
        this.showMessage('✅ Template đã được tải xuống thành công', 'success')
        
      } catch (error) {
        console.error('Download template error:', error)
        this.showMessage('❌ Không thể tải template: ' + (error.response?.data?.message || error.message), 'error')
      } finally {
        this.isDownloading = false
      }
    },
    
    downloadLog() {
      if (!this.importResult) return
      
      const logContent = {
        timestamp: new Date().toISOString(),
        filename: this.selectedFile?.name,
        result: this.importResult
      }
      
      const blob = new Blob([JSON.stringify(logContent, null, 2)], { type: 'application/json' })
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = `import-log-${new Date().toISOString().slice(0, 10)}.json`
      a.click()
      URL.revokeObjectURL(url)
    },
    
    formatFileSize(bytes) {
      if (bytes === 0) return '0 Bytes'
      const k = 1024
      const sizes = ['Bytes', 'KB', 'MB', 'GB']
      const i = Math.floor(Math.log(bytes) / Math.log(k))
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
    },
    
    getTotalSuccesses() {
      return this.importResult?.successes?.length || 0
    },
    
    getTotalWarnings() {
      return this.importResult?.warnings?.length || 0
    },
    
    getTotalErrors() {
      return this.importResult?.errors?.length || 0
    },
    
    showMessage(text, type = 'info') {
      this.message = {
        show: true,
        text,
        type
      }
      
      // Auto hide after 5 seconds
      setTimeout(() => {
        this.message.show = false
      }, 5000)
    }
  }
}
</script>

<style scoped>
.import-data-page {
  min-height: 100vh;
  background-color: #f9fafb;
}
</style>
