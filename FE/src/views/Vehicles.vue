<template>
  <div class="container mx-auto px-4 py-6">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Quản lý mẫu xe</h1>
      <button 
        @click="showAddModal = true"
        class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg flex items-center"
      >
        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"/>
        </svg>
        Thêm mẫu xe
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
            placeholder="Tên xe, hãng xe..."
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Hãng xe</label>
          <select 
            v-model="selectedBrand"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Tất cả</option>
            <option v-for="brand in brands" :key="brand" :value="brand">
              {{ brand }}
            </option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Loại xe</label>
          <select 
            v-model="selectedType"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Tất cả</option>
            <option value="LIGHT_TRUCK">Xe tải nhẹ</option>
            <option value="MEDIUM_TRUCK">Xe tải trung</option>
            <option value="HEAVY_TRUCK">Xe tải nặng</option>
            <option value="TRAILER">Xe kéo</option>
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

    <!-- Vehicle Models Grid -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      <div 
        v-for="vehicle in filteredVehicles" 
        :key="vehicle.id"
        class="bg-white rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition-shadow overflow-hidden"
      >
        <!-- Vehicle Image -->
        <div class="h-48 bg-gradient-to-br from-blue-50 to-blue-100 flex items-center justify-center">
          <svg class="h-16 w-16 text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0z"/>
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M13 6H4a2 2 0 00-2 2v6a2 2 0 002 2h1m8-10V4a2 2 0 00-2-2H4m7 4v10m0 0h6a2 2 0 002-2v-6a2 2 0 00-2-2h-6z"/>
          </svg>
        </div>

        <div class="p-4">
          <!-- Vehicle Header -->
          <div class="mb-3">
            <h3 class="font-semibold text-lg text-gray-900 mb-1">{{ vehicle.name }}</h3>
            <div class="flex items-center justify-between">
              <span class="text-sm font-medium text-blue-600">{{ vehicle.brand }}</span>
              <span :class="getTypeClass(vehicle.type)" class="text-xs px-2 py-1 rounded-full">
                {{ getTypeText(vehicle.type) }}
              </span>
            </div>
          </div>

          <!-- Vehicle Specs -->
          <div class="space-y-2 mb-4">
            <div class="flex justify-between text-sm">
              <span class="text-gray-600">Năm sản xuất:</span>
              <span class="font-medium">{{ vehicle.year }}</span>
            </div>
            <div class="flex justify-between text-sm" v-if="vehicle.engine">
              <span class="text-gray-600">Động cơ:</span>
              <span class="font-medium">{{ vehicle.engine }}</span>
            </div>
            <div class="flex justify-between text-sm" v-if="vehicle.payload">
              <span class="text-gray-600">Tải trọng:</span>
              <span class="font-medium">{{ vehicle.payload }}kg</span>
            </div>
            <div class="flex justify-between text-sm" v-if="vehicle.fuelType">
              <span class="text-gray-600">Nhiên liệu:</span>
              <span class="font-medium">{{ getFuelTypeText(vehicle.fuelType) }}</span>
            </div>
          </div>

          <!-- Compatible Parts Count -->
          <div class="bg-gray-50 rounded-lg p-3 mb-4">
            <div class="text-center">
              <div class="text-lg font-semibold text-gray-900">{{ vehicle.compatiblePartsCount || 0 }}</div>
              <div class="text-xs text-gray-500">Phụ tùng tương thích</div>
            </div>
          </div>

          <!-- Description -->
          <div class="mb-4" v-if="vehicle.description">
            <p class="text-sm text-gray-600 line-clamp-2">{{ vehicle.description }}</p>
          </div>

          <!-- Actions -->
          <div class="flex space-x-2">
            <button 
              @click="viewParts(vehicle)"
              class="flex-1 bg-blue-50 text-blue-600 hover:bg-blue-100 px-3 py-2 rounded-md text-sm font-medium"
            >
              Xem phụ tùng
            </button>
            <button 
              @click="editVehicle(vehicle)"
              class="bg-gray-50 text-gray-600 hover:bg-gray-100 px-3 py-2 rounded-md text-sm font-medium"
            >
              Sửa
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div v-if="filteredVehicles.length === 0" class="text-center py-12">
      <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0z"/>
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 6H4a2 2 0 00-2 2v6a2 2 0 002 2h1m8-10V4a2 2 0 00-2-2H4m7 4v10m0 0h6a2 2 0 002-2v-6a2 2 0 00-2-2h-6z"/>
      </svg>
      <h3 class="mt-2 text-sm font-medium text-gray-900">Không tìm thấy mẫu xe</h3>
      <p class="mt-1 text-sm text-gray-500">Hãy thử thay đổi bộ lọc hoặc thêm mẫu xe mới.</p>
    </div>

    <!-- Load More -->
    <div class="text-center mt-8" v-if="filteredVehicles.length > 0 && filteredVehicles.length >= 12">
      <button class="bg-gray-50 text-gray-600 hover:bg-gray-100 px-6 py-2 rounded-md text-sm font-medium">
        Tải thêm mẫu xe
      </button>
    </div>

    <!-- Popular Brands Section -->
    <div class="mt-12">
      <h2 class="text-xl font-semibold text-gray-900 mb-4">Hãng xe phổ biến</h2>
      <div class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4">
        <div 
          v-for="brand in popularBrands" 
          :key="brand.name"
          @click="filterByBrand(brand.name)"
          class="bg-white rounded-lg shadow-sm border border-gray-200 p-4 text-center cursor-pointer hover:shadow-md transition-shadow"
        >
          <div class="h-12 w-12 mx-auto mb-2 bg-gray-100 rounded-lg flex items-center justify-center">
            <span class="text-lg font-bold text-gray-600">{{ brand.name.charAt(0) }}</span>
          </div>
          <div class="text-sm font-medium text-gray-900">{{ brand.name }}</div>
          <div class="text-xs text-gray-500">{{ brand.count }} mẫu xe</div>
        </div>
      </div>
    </div>

    <!-- Add Vehicle Modal -->
    <div v-if="showAddModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-2xl max-h-[80vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-semibold">Thêm mẫu xe mới</h3>
          <button @click="showAddModal = false" class="text-gray-400 hover:text-gray-600">
            <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <form @submit.prevent="createVehicle" class="space-y-4">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Tên mẫu xe *</label>
              <input v-model="newVehicle.name" type="text" class="form-input w-full" required>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Mã mẫu xe</label>
              <input v-model="newVehicle.code" type="text" class="form-input w-full">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Hãng xe *</label>
              <select v-model="newVehicle.brand" class="form-input w-full" required>
                <option value="">Chọn hãng xe</option>
                <option value="HINO">Hino</option>
                <option value="HYUNDAI">Hyundai</option>
                <option value="THACO">Thaco</option>
                <option value="DONGFENG">Dongfeng</option>
                <option value="ISUZU">Isuzu</option>
                <option value="MITSUBISHI">Mitsubishi</option>
              </select>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Dòng xe</label>
              <input v-model="newVehicle.model" type="text" class="form-input w-full">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Động cơ</label>
              <input v-model="newVehicle.engine" type="text" class="form-input w-full" placeholder="VD: 4.0L Diesel">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Năm sản xuất</label>
              <input v-model="newVehicle.year" type="number" min="1900" max="2030" class="form-input w-full">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Loại xe</label>
              <select v-model="newVehicle.type" class="form-input w-full">
                <option value="">Chọn loại xe</option>
                <option value="LIGHT_TRUCK">Xe tải nhẹ</option>
                <option value="MEDIUM_TRUCK">Xe tải trung</option>
                <option value="HEAVY_TRUCK">Xe tải nặng</option>
                <option value="TRAILER">Xe kéo</option>
              </select>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
              <select v-model="newVehicle.status" class="form-input w-full">
                <option value="ACTIVE">Hoạt động</option>
                <option value="INACTIVE">Không hoạt động</option>
              </select>
            </div>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Mô tả</label>
            <textarea v-model="newVehicle.description" rows="3" class="form-input w-full"></textarea>
          </div>
          
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Tải trọng (kg)</label>
              <input v-model.number="newVehicle.payload" type="number" min="0" class="form-input w-full">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Loại nhiên liệu</label>
              <select v-model="newVehicle.fuelType" class="form-input w-full">
                <option value="">Chọn nhiên liệu</option>
                <option value="DIESEL">Diesel</option>
                <option value="GASOLINE">Xăng</option>
                <option value="ELECTRIC">Điện</option>
                <option value="HYBRID">Hybrid</option>
              </select>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Số chỗ ngồi</label>
              <input v-model.number="newVehicle.seats" type="number" min="1" class="form-input w-full">
            </div>
          </div>
          
          <div class="flex justify-end space-x-3 pt-4">
            <button @click="showAddModal = false" type="button" class="btn-secondary">
              Hủy
            </button>
            <button type="submit" class="btn-primary">
              Thêm mẫu xe
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { vehiclesApi } from '@/api'

export default {
  name: 'Vehicles',
  setup() {
    const loading = ref(false)
    const vehicles = ref([])
    const currentPage = ref(0)
    const totalPages = ref(0)
    const totalElements = ref(0)
    const pageSize = ref(10)

    // Filters
    const searchQuery = ref('')
    const selectedBrand = ref('')
    const selectedYear = ref('')
    const brands = ref([]) // Khởi tạo brands
    const selectedType = ref('') // Khởi tạo selectedType
    const popularBrands = ref([]) // Khai báo popularBrands

    // Computed property for filtered vehicles
    const filteredVehicles = computed(() => {
      let filtered = vehicles.value

      if (selectedBrand.value) {
        filtered = filtered.filter(vehicle => vehicle.brand === selectedBrand.value)
      }
      if (selectedType.value) {
        filtered = filtered.filter(vehicle => vehicle.type === selectedType.value)
      }
      if (selectedYear.value) {
        filtered = filtered.filter(vehicle => vehicle.year === parseInt(selectedYear.value))
      }
      if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        filtered = filtered.filter(vehicle => 
          vehicle.name.toLowerCase().includes(query) ||
          vehicle.brand.toLowerCase().includes(query)
        )
      }
      return filtered
    })

    // Helper functions for display
    const getFuelTypeText = (fuelType) => {
      const types = {
        GASOLINE: 'Xăng',
        DIESEL: 'Dầu',
        ELECTRIC: 'Điện',
        HYBRID: 'Hybrid'
      }
      return types[fuelType] || fuelType
    }

    const getTypeText = (type) => {
      const types = {
        LIGHT_TRUCK: 'Xe tải nhẹ',
        MEDIUM_TRUCK: 'Xe tải trung',
        HEAVY_TRUCK: 'Xe tải nặng',
        TRAILER: 'Xe kéo'
      }
      return types[type] || type
    }

    const getTypeClass = (type) => {
      const classes = {
        LIGHT_TRUCK: 'bg-blue-100 text-blue-800',
        MEDIUM_TRUCK: 'bg-green-100 text-green-800',
        HEAVY_TRUCK: 'bg-red-100 text-red-800',
        TRAILER: 'bg-purple-100 text-purple-800'
      }
      return classes[type] || 'bg-gray-100 text-gray-800'
    }

    // Modal states
    const showAddModal = ref(false)
    const showEditModal = ref(false)
    const selectedVehicle = ref(null)

    // New vehicle form
    const newVehicle = ref({
      name: '',
      code: '',
      brand: '',
      model: '',
      engine: '',
      year: '',
      description: '',
      status: 'ACTIVE'
    })

    // Load vehicles
    const loadVehicles = async () => {
      try {
        loading.value = true
        const response = await vehiclesApi.getAll(
          currentPage.value,
          pageSize.value,
          searchQuery.value,
          selectedBrand.value || null,
          selectedYear.value || null
        )
        
        vehicles.value = response.data.content || []
        totalPages.value = response.data.totalPages || 0
        totalElements.value = response.data.totalElements || 0

        // Populate brands
        const uniqueBrands = [...new Set(vehicles.value.map(v => v.brand))].filter(Boolean)
        brands.value = uniqueBrands

        // Populate popularBrands (example, you might fetch this from API)
        popularBrands.value = [
          { name: 'HINO', count: 10 },
          { name: 'HYUNDAI', count: 8 },
          { name: 'THACO', count: 12 },
          { name: 'ISUZU', count: 5 },
        ];

      } catch (error) {
        console.error('Error loading vehicles:', error)
      } finally {
        loading.value = false
      }
    }

    // Create new vehicle
    const createVehicle = async () => {
      try {
        await vehiclesApi.create(newVehicle.value)
        showAddModal.value = false
        resetNewVehicle()
        loadVehicles()
      } catch (error) {
        console.error('Error creating vehicle:', error)
      }
    }

    // Update vehicle
    const updateVehicle = async () => {
      try {
        await vehiclesApi.update(selectedVehicle.value.id, selectedVehicle.value)
        showEditModal.value = false
        selectedVehicle.value = null
        loadVehicles()
      } catch (error) {
        console.error('Error updating vehicle:', error)
      }
    }

    // Delete vehicle
    const deleteVehicle = async (vehicleId) => {
      if (confirm('Bạn có chắc chắn muốn xóa mẫu xe này?')) {
        try {
          await vehiclesApi.delete(vehicleId)
          loadVehicles()
        } catch (error) {
          console.error('Error deleting vehicle:', error)
        }
      }
    }

    // View parts for vehicle
    const viewParts = (vehicle) => {
      console.log('View parts for:', vehicle)
      // Navigate to parts page with vehicle filter
    }

    // Edit vehicle
    const editVehicle = (vehicle) => {
      selectedVehicle.value = { ...vehicle }
      showEditModal.value = true
    }

    // View vehicle details
    const viewVehicle = (vehicle) => {
      selectedVehicle.value = { ...vehicle }
      // Có thể mở modal chi tiết hoặc navigate đến trang chi tiết
    }

    // Get compatible products
    const getCompatibleProducts = async (vehicleId) => {
      try {
        const response = await vehiclesApi.getCompatibleProducts(vehicleId)
        return response.data || []
      } catch (error) {
        console.error('Error getting compatible products:', error)
        return []
      }
    }

    // Suggest products for vehicle
    const suggestProducts = async (vehicleId, keyword) => {
      try {
        const response = await vehiclesApi.suggest(vehicleId, keyword)
        return response.data || []
      } catch (error) {
        console.error('Error suggesting products:', error)
        return []
      }
    }

    // Reset new vehicle form
    const resetNewVehicle = () => {
      newVehicle.value = {
        name: '',
        code: '',
        brand: '',
        model: '',
        engine: '',
        year: '',
        description: '',
        status: 'ACTIVE'
      }
    }

    // Apply filters
    const applyFilters = () => {
      currentPage.value = 0
      loadVehicles()
    }

    // Clear filters
    const clearFilters = () => {
      searchQuery.value = ''
      selectedBrand.value = ''
      selectedYear.value = ''
      currentPage.value = 0
      loadVehicles()
    }

    const filterByBrand = (brandName) => {
      selectedBrand.value = brandName
      applyFilters()
    }

    // Change page
    const changePage = (page) => {
      currentPage.value = page
      loadVehicles()
    }

    // Format date
    const formatDate = (dateString) => {
      return new Date(dateString).toLocaleDateString('vi-VN')
    }

    // Get status class
    const getStatusClass = (status) => {
      const classes = {
        'ACTIVE': 'bg-green-100 text-green-800',
        'INACTIVE': 'bg-gray-100 text-gray-800'
      }
      return classes[status] || 'bg-gray-100 text-gray-800'
    }

    // Get status text
    const getStatusText = (status) => {
      const texts = {
        'ACTIVE': 'Hoạt động',
        'INACTIVE': 'Không hoạt động'
      }
      return texts[status] || status
    }

    // Computed properties
    const paginationInfo = computed(() => {
      const start = currentPage.value * pageSize.value + 1
      const end = Math.min((currentPage.value + 1) * pageSize.value, totalElements.value)
      return `${start}-${end} của ${totalElements.value} mẫu xe`
    })

    // Load data on mount
    onMounted(() => {
      loadVehicles()
    })

    return {
      // Data
      loading,
      vehicles,
      currentPage,
      totalPages,
      totalElements,
      pageSize,
      
      // Filters
      searchQuery,
      selectedBrand,
      selectedYear,
      brands, // Thêm brands vào đây
      selectedType, // Thêm selectedType vào đây
      
      // Modals
      showAddModal,
      showEditModal,
      selectedVehicle,
      newVehicle,
      
      // Methods
      loadVehicles,
      createVehicle,
      updateVehicle,
      deleteVehicle,
      editVehicle,
      viewVehicle,
      viewParts,
      getCompatibleProducts,
      suggestProducts,
      applyFilters,
      clearFilters,
      changePage,
      formatDate,
      getStatusClass,
      getStatusText,
      getFuelTypeText,
      getTypeText,
      getTypeClass,
      filterByBrand,
      
      // Computed
      filteredVehicles, // Thêm filteredVehicles vào đây
      paginationInfo,
      popularBrands // Thêm popularBrands vào đây
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
