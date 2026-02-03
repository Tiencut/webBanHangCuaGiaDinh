<template>
    <div v-if="isOpen" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-8 w-full max-w-5xl max-h-[95vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-xl font-semibold">Chi tiết sản phẩm</h3>
          <button @click="close" class="text-gray-400 hover:text-gray-600">
            <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
  <div v-if="selectedProduct" class="space-y-8">
          <!-- Product Info Header -->
          <div class="bg-gray-50 p-4 rounded-lg">
            <div class="flex items-center space-x-4">
              <div class="h-20 w-20 rounded-lg bg-gray-200 flex items-center justify-center">
                <img 
                  v-if="selectedProduct && selectedProduct.imageUrl" 
                  :src="selectedProduct.imageUrl" 
                  :alt="selectedProduct.name"
                  class="h-20 w-20 rounded-lg object-cover"
                />
                <svg v-else class="h-10 w-10 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/>
                </svg>
              </div>
              <div>
                <h4 class="text-2xl font-bold text-gray-900">{{ selectedProduct.name }}</h4>
                <p class="text-sm text-gray-600">SKU: {{ selectedProduct.sku }}</p>
                <p class="text-sm text-gray-600">Danh mục: {{ getCategoryName(selectedProduct.categoryId) }}</p>
              </div>
            </div>
            <div class="grid grid-cols-2 gap-4 mt-4">
              <div>
                <label class="block text-sm font-medium text-gray-700">Thương hiệu</label>
                <p class="mt-1 text-sm text-gray-900">{{ selectedProduct.brand || 'N/A' }}</p>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Model</label>
                <p class="mt-1 text-sm text-gray-900">{{ selectedProduct.model || 'N/A' }}</p>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Mã phụ tùng</label>
                <p class="mt-1 text-sm text-gray-900">{{ selectedProduct.partNumber || 'N/A' }}</p>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Loại xe</label>
                <p class="mt-1 text-sm text-gray-900">{{ selectedProduct.vehicleType || 'N/A' }}</p>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Tồn kho</label>
                <p class="mt-1 text-sm text-gray-900">{{ selectedProduct.stock || 0 }}</p>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Giá bán</label>
                <p class="mt-1 text-sm text-gray-900">{{ formatCurrency(selectedProduct.sellingPrice) }}</p>
              </div>
            </div>
          </div>

          <!-- Product Dynamic Attributes -->
          <div>
            <h4 class="font-semibold text-lg mb-2">Thông số kỹ thuật</h4>
            <ProductDynamicAttributes 
              :product-id="selectedProduct?.id" 
              :category-id="selectedProduct?.categoryId" 
            />
          </div>

          <!-- Product Suppliers Table -->
          <div>
            <h4 class="font-semibold text-lg mb-2">Giá nhập/bán theo từng nhà cung cấp</h4>
            <ProductSuppliersTable :product-id="selectedProduct?.id" />
          </div>
        </div>
      </div>
    </div>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue';
import ProductDynamicAttributes from '@/components/products/ProductDynamicAttributes.vue';
import ProductSuppliersTable from '@/components/products/ProductSuppliersTable.vue';
import { formatCurrency } from '@/utils/helpers';

const props = defineProps({
  categories: Array, // Cần để hiển thị tên danh mục
});

const emit = defineEmits(['close']);

const isOpen = ref(false);
const selectedProduct = ref(null);
const activeTab = ref('info'); // Default active tab

const getCategoryName = (categoryId) => {
  const category = props.categories?.find(cat => cat.id === categoryId) || null;
  return category ? category.name : 'N/A';
};

function open(product) {
  selectedProduct.value = product || null;
  activeTab.value = 'info';
  isOpen.value = true;
  window.addEventListener('keydown', handleEscClose);
}

function close() {
  isOpen.value = false;
  selectedProduct.value = null;
  window.removeEventListener('keydown', handleEscClose);
  emit('close');
}

function handleEscClose(e) {
  if (e.key === 'Escape') {
    close();
  }
}

defineExpose({ open, close });
</script>
 