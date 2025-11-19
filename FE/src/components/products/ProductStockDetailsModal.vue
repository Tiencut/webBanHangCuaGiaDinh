<template>
    <!-- Stock Details Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-2xl max-h-[80vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-semibold">Chi tiết tồn kho</h3>
          <button @click="emit('update:showModal', false)" class="text-gray-400 hover:text-gray-600">
            <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <div v-if="selectedProduct" class="space-y-4">
          <div class="border-b pb-4">
            <h4 class="font-medium text-lg">{{ selectedProduct.name }}</h4>
            <p class="text-sm text-gray-600">SKU: {{ selectedProduct.sku }}</p>
          </div>

          <!-- Summary Cards -->
          <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div class="bg-blue-50 p-4 rounded-lg">
              <div class="text-sm text-blue-600">Tổng tồn kho</div>
              <div class="text-xl font-bold text-blue-800">{{ selectedProduct.stock || 0 }}</div>
            </div>
            <div class="bg-green-50 p-4 rounded-lg">
              <div class="text-sm text-green-600">Có sẵn</div>
              <div class="text-xl font-bold text-green-800">{{ selectedProduct.availableStock || 0 }}</div>
            </div>
            <div class="bg-yellow-50 p-4 rounded-lg">
              <div class="text-sm text-yellow-600">Đã đặt trước</div>
              <div class="text-xl font-bold text-yellow-800">{{ selectedProduct.reservedStock || 0 }}</div>
            </div>
            <div class="bg-purple-50 p-4 rounded-lg">
              <div class="text-sm text-purple-600">Nhà cung cấp</div>
              <div class="text-xl font-bold text-purple-800">{{ selectedProduct.supplierCount || 0 }}</div>
            </div>
          </div>

          <!-- Inventory Details Table -->
          <div v-if="selectedProduct.inventoryDetails && selectedProduct.inventoryDetails.length > 0">
            <h5 class="font-medium text-gray-900 mb-3">Chi tiết theo nhà cung cấp</h5>
            <div class="overflow-x-auto">
              <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                  <tr>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Nhà cung cấp
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Tồn kho
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Có sẵn
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Đã đặt
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Giá vốn
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Vị trí
                    </th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="inventory in selectedProduct.inventoryDetails" :key="inventory.id">
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ inventory.supplier?.name || 'N/A' }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ inventory.quantity || 0 }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ inventory.availableQuantity || 0 }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ inventory.reservedQuantity || 0 }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ formatCurrency(inventory.costPrice || 0) }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ inventory.location || 'N/A' }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          
          <div v-else class="text-center py-8 text-gray-500">
            <p>Chưa có dữ liệu tồn kho cho sản phẩm này</p>
          </div>
        </div>
        <div class="mt-8">
          <h4 class="font-semibold text-lg mb-2">Lịch sử giao dịch kho</h4>
          <ProductInventoryHistory :product-id="selectedProduct?.id" />
        </div>
        <div class="mb-8">
          <h4 class="font-semibold text-lg mb-2">Giá nhập/bán theo từng nhà cung cấp</h4>
          <ProductSuppliersTable :product-id="selectedProduct?.id" />
        </div>
        <div class="mb-8">
          <h4 class="font-semibold text-lg mb-2">Thông số kỹ thuật</h4>
          <ProductDynamicAttributes :product-id="selectedProduct?.id" :category-id="selectedProduct?.categoryId" />
        </div>
      </div>
    </div>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue';

// Define props for the modal
const props = defineProps({
  showModal: {
    type: Boolean,
    default: false
  },
  selectedProduct: {
    type: Object,
    default: null
  }
});

// Define emits for updating the parent component
const emit = defineEmits(['update:showModal']);

// Local state for the modal visibility
const internalShowModal = ref(props.showModal);

// Watch for changes in the prop and update internal state
watch(() => props.showModal, (newValue) => {
  internalShowModal.value = newValue;
});

// Emit update event when internal state changes
watch(internalShowModal, (newValue) => {
  emit('update:showModal', newValue);
});

// Placeholder for any methods or computed properties that will be moved
</script>

<style scoped>
/* Add any specific styles for this component here */
</style>