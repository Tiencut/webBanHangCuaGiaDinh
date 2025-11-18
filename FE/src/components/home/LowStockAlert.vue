<template>
  <div class="bg-white rounded-xl shadow-lg border border-gray-100 overflow-hidden">
    <!-- Header -->
    <div class="flex items-center justify-between px-6 py-5 border-b border-gray-100">
      <h2 class="text-lg font-bold text-red-600">Cảnh báo tồn kho</h2>
      <router-link
        to="/products"
        class="text-sm text-blue-600 hover:text-blue-800 font-medium"
      >
        Xem chi tiết
      </router-link>
    </div>

    <div class="px-6 py-8 bg-red-50">
      <!-- Loading -->
      <div v-if="loadingStock" class="flex flex-col items-center gap-4 py-8">
        <svg class="h-8 w-8 text-red-400 animate-spin" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
        </svg>
        <p class="text-sm text-gray-500">Đang tải dữ liệu tồn kho...</p>
      </div>

      <!-- Đủ tồn kho -->
      <div v-else-if="lowStockProducts.length === 0" class="flex flex-col items-center gap-3 py-8">
        <svg class="h-10 w-10 text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <p class="text-base text-green-600 font-semibold">Tất cả sản phẩm đều có đủ tồn kho</p>
      </div>

      <!-- Bảng cảnh báo tồn kho thấp -->
      <div v-else>
        <table class="min-w-full bg-white rounded-lg shadow-sm overflow-hidden">
          <thead>
            <tr class="bg-red-100 uppercase text-xs text-red-700">
              <th class="py-3 px-4 text-left">Sản phẩm</th>
              <th class="py-3 px-4 text-left">SKU</th>
              <th class="py-3 px-4 text-center">Tồn kho</th>
              <th class="py-3 px-4 text-right">Trạng thái</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="product in lowStockProducts" :key="product.id" class="odd:bg-red-50 even:bg-white">
              <td class="py-2 px-4 font-medium text-gray-900 flex items-center gap-2">
                <div class="h-6 w-6 bg-red-100 rounded-full flex items-center justify-center">
                  <svg class="h-4 w-4 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z" />
                  </svg>
                </div>
                {{ product.name }}
              </td>
              <td class="py-2 px-4 text-gray-500">{{ product.sku }}</td>
              <td class="py-2 px-4 text-red-700 font-bold text-center">{{ product.stock }} còn lại</td>
              <td class="py-2 px-4 text-xs text-right">
                <span class="bg-red-200 text-red-800 px-2 py-1 rounded-full font-semibold">Cần nhập thêm</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>


<script>
import { ref, onMounted } from 'vue'
import { productsAPI } from '@/api'

export default {
  name: 'LowStockAlert',
  setup() {
    const loadingStock = ref(false)
    const lowStockProducts = ref([])

    const loadLowStockProducts = async () => {
      try {
        loadingStock.value = true
        const productsResponse = await productsAPI.getProducts(0, 10, '', null, null)
        lowStockProducts.value = (productsResponse.data.content || [])
          .filter(product => product.stock < 10) // Sản phẩm có tồn kho < 10
          .slice(0, 5) // Chỉ lấy 5 sản phẩm đầu
      } catch (error) {
        console.error('Error loading low stock products:', error)
      } finally {
        loadingStock.value = false
      }
    }

    onMounted(() => {
      loadLowStockProducts()
    })

    return {
      loadingStock,
      lowStockProducts
    }
  }
}
</script>