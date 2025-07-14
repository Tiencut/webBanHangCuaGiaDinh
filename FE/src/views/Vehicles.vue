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
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'

export default {
  name: 'Vehicles',
  setup() {
    // Reactive data
    const searchQuery = ref('')
    const selectedBrand = ref('')
    const selectedType = ref('')
    const showAddModal = ref(false)
    
    // Mock data
    const vehicles = ref([
      {
        id: 1,
        name: 'Hyundai HD65',
        brand: 'Hyundai',
        type: 'LIGHT_TRUCK',
        year: 2023,
        engine: '3.9L Diesel',
        payload: 3500,
        fuelType: 'DIESEL',
        description: 'Xe tải nhẹ phổ biến, tiết kiệm nhiên liệu',
        compatiblePartsCount: 156
      },
      {
        id: 2,
        name: 'Isuzu NPR',
        brand: 'Isuzu',
        type: 'MEDIUM_TRUCK',
        year: 2023,
        engine: '5.2L Diesel',
        payload: 5500,
        fuelType: 'DIESEL',
        description: 'Xe tải trung bền bỉ, độ tin cậy cao',
        compatiblePartsCount: 234
      },
      {
        id: 3,
        name: 'Thaco Forland FD1600',
        brand: 'Thaco',
        type: 'MEDIUM_TRUCK',
        year: 2022,
        engine: '4.0L Diesel',
        payload: 7200,
        fuelType: 'DIESEL',
        description: 'Xe tải trung giá tốt, phù hợp vận chuyển trong thành phố',
        compatiblePartsCount: 189
      },
      {
        id: 4,
        name: 'Dongfeng B180',
        brand: 'Dongfeng',
        type: 'HEAVY_TRUCK',
        year: 2023,
        engine: '9.0L Diesel',
        payload: 18000,
        fuelType: 'DIESEL',
        description: 'Xe tải nặng cho vận chuyển đường dài',
        compatiblePartsCount: 312
      },
      {
        id: 5,
        name: 'Hino 300 Series',
        brand: 'Hino',
        type: 'LIGHT_TRUCK',
        year: 2023,
        engine: '4.0L Diesel',
        payload: 4200,
        fuelType: 'DIESEL',
        description: 'Dòng xe tải nhẹ chất lượng Nhật Bản',
        compatiblePartsCount: 198
      },
      {
        id: 6,
        name: 'Mitsubishi Fuso Canter',
        brand: 'Mitsubishi',
        type: 'LIGHT_TRUCK',
        year: 2022,
        engine: '3.0L Diesel',
        payload: 3000,
        fuelType: 'DIESEL',
        description: 'Xe tải nhẹ compact, linh hoạt trong thành phố',
        compatiblePartsCount: 145
      }
    ])

    // Computed properties
    const filteredVehicles = computed(() => {
      let filtered = vehicles.value

      if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        filtered = filtered.filter(vehicle => 
          vehicle.name.toLowerCase().includes(query) ||
          vehicle.brand.toLowerCase().includes(query)
        )
      }

      if (selectedBrand.value) {
        filtered = filtered.filter(vehicle => vehicle.brand === selectedBrand.value)
      }

      if (selectedType.value) {
        filtered = filtered.filter(vehicle => vehicle.type === selectedType.value)
      }

      return filtered
    })

    const brands = computed(() => {
      return [...new Set(vehicles.value.map(v => v.brand))].sort()
    })

    const popularBrands = computed(() => {
      const brandCounts = vehicles.value.reduce((acc, vehicle) => {
        acc[vehicle.brand] = (acc[vehicle.brand] || 0) + 1
        return acc
      }, {})

      return Object.entries(brandCounts)
        .map(([name, count]) => ({ name, count }))
        .sort((a, b) => b.count - a.count)
        .slice(0, 6)
    })

    // Methods
    const getTypeClass = (type) => {
      switch (type) {
        case 'LIGHT_TRUCK': return 'bg-green-100 text-green-800'
        case 'MEDIUM_TRUCK': return 'bg-blue-100 text-blue-800'
        case 'HEAVY_TRUCK': return 'bg-red-100 text-red-800'
        case 'TRAILER': return 'bg-purple-100 text-purple-800'
        default: return 'bg-gray-100 text-gray-800'
      }
    }

    const getTypeText = (type) => {
      switch (type) {
        case 'LIGHT_TRUCK': return 'Tải nhẹ'
        case 'MEDIUM_TRUCK': return 'Tải trung'
        case 'HEAVY_TRUCK': return 'Tải nặng'
        case 'TRAILER': return 'Xe kéo'
        default: return 'Không xác định'
      }
    }

    const getFuelTypeText = (fuelType) => {
      switch (fuelType) {
        case 'DIESEL': return 'Dầu diesel'
        case 'GASOLINE': return 'Xăng'
        case 'ELECTRIC': return 'Điện'
        case 'HYBRID': return 'Hybrid'
        default: return 'Không xác định'
      }
    }

    const applyFilters = () => {
      console.log('Applying filters...')
    }

    const clearFilters = () => {
      searchQuery.value = ''
      selectedBrand.value = ''
      selectedType.value = ''
    }

    const viewParts = (vehicle) => {
      console.log('View compatible parts for:', vehicle)
      // Logic to view compatible parts
    }

    const editVehicle = (vehicle) => {
      console.log('Edit vehicle:', vehicle)
      // Logic to edit vehicle
    }

    const filterByBrand = (brandName) => {
      selectedBrand.value = brandName
      // Scroll to top
      window.scrollTo({ top: 0, behavior: 'smooth' })
    }

    // Lifecycle
    onMounted(() => {
      console.log('Vehicles component mounted')
    })

    return {
      searchQuery,
      selectedBrand,
      selectedType,
      showAddModal,
      vehicles,
      filteredVehicles,
      brands,
      popularBrands,
      getTypeClass,
      getTypeText,
      getFuelTypeText,
      applyFilters,
      clearFilters,
      viewParts,
      editVehicle,
      filterByBrand
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
