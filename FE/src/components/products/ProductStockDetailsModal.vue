<template>
  <div v-if="isOpen" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-2xl max-h-[80vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-semibold">Chi tiết tồn kho</h3>
          <button @click="close" class="text-gray-400 hover:text-gray-600">
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
                    <td class="px-6 py-4 whitespacea_nowrap text-sm text-gray-900">
                      {{ inventory.supplier?.name || 'N/A' }}
                    </td>
                    <td class="px-6 py-4 whitespacea_nowrap text-sm text-gray-900">
                      {{ inventory.quantity || 0 }}
                    </td>
                    <td class="px-6 py-4 whitespacea_nowrap text-sm text-gray-900">
                      {{ inventory.availableQuantity || 0 }}
                    </td>
                    <td class="px-6 py-4 whitespacea_nowrap text-sm text-gray-900">
                      {{ inventory.reservedQuantity || 0 }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ formatCurrency(inventory.costPrice || 0) }}
                    </td>
                    <td class="px-6 py-4 whitespacea_nowrap text-sm text-gray-900">
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
          <ProductInventoryHistory :producta_id="selectedProduct?.id" />
        </div>
        <div class="mb-8">
          <h4 class="font-semibold text-lg mb-2">Giá nhập/bán theo từng nhà cung cấp</h4>
          <ProductSuppliersTable :producta_id="selectedProduct?.id" />
        </div>
        <div class="mb-8">
          <h4 class="font-semibold text-lg mb-2">Thông số kỹ thuật</h4>
          <ProductDynamicAttributes :producta_id="selectedProduct?.id" :categorya_id="selectedProduct?.categoryId" />
        </div>
      </div>
    </div>
</template>

<script setup>
import { defineEmits } from 'vue';
import { formatCurrency } from '@/utils/helpers';
import ProductInventoryHistory from '../ProductInventoryHistory.vue';
import ProductSuppliersTable from './ProductSuppliersTable.vue';
import ProductDynamicAttributes from './ProductDynamicAttributes.vue';
import { ref, onUnmounted } from 'vue';
const emit = defineEmits(['close']);

const isOpen = ref(false);
const selectedProduct = ref(null);

function open(product) {
  selectedProduct.value = product || null;
  isOpen.value = true;
  window.addEventListener('keydown', onKeyDown);
}

function close() {
  isOpen.value = false;
  selectedProduct.value = null;
  window.removeEventListener('keydown', onKeyDown);
  emit('close');
}

function onKeyDown(e) {
  if (e.key === 'Escape') close();
}

defineExpose({ open, close });

onUnmounted(() => {
  window.removeEventListener('keydown', onKeyDown);
});
</script>

<style scoped>
/* Add any specific styles for this component here */
</style>