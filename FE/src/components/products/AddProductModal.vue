<template>
    <div v-if="showAddModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-2xl max-h-[80vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-semibold">Thêm sản phẩm mới</h3>
          <button @click="$emit('close')" class="text-gray-400 hover:text-gray-600">
            <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <!--  -->
        <form @submit.prevent="createProduct" class="space-y-4">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Tên sản phẩm *</label>
              <input v-model="newProduct.name" type="text" class="form-input w-full" required>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Mã SKU *</label>
              <input v-model="newProduct.sku" type="text" class="form-input w-full" required>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Thương hiệu</label>
              <input v-model="newProduct.brand" type="text" class="form-input w-full" placeholder="Isuzu, Hyundai, Hino...">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Model</label>
              <input v-model="newProduct.model" type="text" class="form-input w-full" placeholder="4JB1, HD72, 300...">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Mã phụ tùng</label>
              <input v-model="newProduct.partNumber" type="text" class="form-input w-full" placeholder="ISU-4JB1-OIL-001">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Loại xe</label>
              <select v-model="newProduct.vehicleType" class="form-input w-full">
                <option value="">Chọn loại xe</option>
                <option value="Xe tải nhẹ">Xe tải nhẹ</option>
                <option value="Xe tải trung">Xe tải trung</option>
                <option value="Xe tải nặng">Xe tải nặng</option>
                <option value="Xe bán tải">Xe bán tải</option>
                <option value="Xe van">Xe van</option>
                <option value="Xe khách">Xe khách</option>
                <option value="Xe chuyên dụng">Xe chuyên dụng</option>
              </select>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Danh mục</label>
              <select v-model="newProduct.categoryId" class="form-input w-full">
                <option value="">Chọn danh mục</option>
                <option v-for="category in categories" :key="category.id" :value="category.id">
                  {{ category.name }}
                </option>
              </select>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Nhà cung cấp</label>
              <select v-model="newProduct.supplierId" class="form-input w-full">
                <option value="">Chọn nhà cung cấp</option>
                <option v-for="supplier in suppliers" :key="supplier.id" :value="supplier.id">
                  {{ supplier.name }}
                </option>
              </select>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Giá cơ bản *</label>
              <input v-model.number="newProduct.basePrice" type="number" min="0" class="form-input w-full" required>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Giá bán</label>
              <input v-model.number="newProduct.sellingPrice" type="number" min="0" class="form-input w-full">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Giá vốn</label>
              <input v-model.number="newProduct.costPrice" type="number" min="0" class="form-input w-full">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Thời gian bảo hành (tháng)</label>
              <input v-model.number="newProduct.warrantyPeriod" type="number" min="0" class="form-input w-full">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Trọng lượng (kg)</label>
              <input v-model.number="newProduct.weight" type="number" min="0" step="0.1" class="form-input w-full">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Kích thước</label>
              <input v-model="newProduct.dimensions" type="text" class="form-input w-full" placeholder="L x W x H cm">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
              <select v-model="newProduct.status" class="form-input w-full">
                <option value="ACTIVE">Hoạt động</option>
                <option value="INACTIVE">Không hoạt động</option>
                <option value="DISCONTINUED">Ngừng kinh doanh</option>
                <option value="OUT_OF_STOCK">Hết hàng</option>
              </select>
            </div>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Mô tả</label>
            <textarea v-model="newProduct.description" rows="3" class="form-input w-full"></textarea>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Thông số kỹ thuật</label>
            <textarea v-model="newProduct.specifications" rows="3" class="form-input w-full" placeholder="Kích thước, trọng lượng, chất liệu..."></textarea>
          </div>
          
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Tồn kho tối thiểu</label>
              <input v-model.number="newProduct.minStockLevel" type="number" min="0" class="form-input w-full">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Điểm đặt hàng lại</label>
              <input v-model.number="newProduct.reorderPoint" type="number" min="0" class="form-input w-full">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Vị trí lưu trữ</label>
              <input v-model="newProduct.location" type="text" class="form-input w-full" placeholder="Kệ A, Tủ B...">
            </div>

          </div>
          
          <!-- Combo checkbox -->
          <div class="flex items-center space-x-2">
            <input 
              v-model="newProduct.isCombo" 
              type="checkbox" 
              id="isCombo" 
              class="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
            >
            <label for="isCombo" class="text-sm font-medium text-gray-700">
              Sản phẩm combo (gồm nhiều linh kiện)
            </label>
          </div>
          
          <div class="flex justify-end space-x-3 pt-4">
            <button @click="$emit('close')" type="button" class="btn-secondary">
              Hủy
            </button>
            <button type="submit" class="btn-primary">
              Thêm sản phẩm
            </button>
          </div>
        </form>
      </div>
    </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { useToast } from 'vue-toastification';
import api from '@/services/api';

const props = defineProps({
  showAddModal: Boolean,
  categories: Array,
  suppliers: Array,
});

const emit = defineEmits(['close', 'product-created']);

const toast = useToast();

const newProduct = ref({
  name: '',
  sku: '',
  brand: '',
  model: '',
  partNumber: '',
  vehicleType: '',
  categoryId: '',
  supplierId: '',
  basePrice: 0,
  sellingPrice: 0,
  costPrice: 0,
  warrantyPeriod: 0,
  weight: 0,
  dimensions: '',
  status: 'ACTIVE',
  description: '',
  specifications: '',
  minStockLevel: 0,
  reorderPoint: 0,
  location: '',
  isCombo: false,
});

watch(() => props.showAddModal, (newValue) => {
  if (newValue) {
    resetNewProduct();
  }
});

const resetNewProduct = () => {
  newProduct.value = {
    name: '',
    sku: '',
    brand: '',
    model: '',
    partNumber: '',
    vehicleType: '',
    categoryId: '',
    supplierId: '',
    basePrice: 0,
    sellingPrice: 0,
    costPrice: 0,
    warrantyPeriod: 0,
    weight: 0,
    dimensions: '',
    status: 'ACTIVE',
    description: '',
    specifications: '',
    minStockLevel: 0,
    reorderPoint: 0,
    location: '',
    isCombo: false,
  };
};

const createProduct = async () => {
  try {
    const productToCreate = { ...newProduct.value };
    if (productToCreate.categoryId === '') {
      delete productToCreate.categoryId;
    }
    if (productToCreate.supplierId === '') {
      delete productToCreate.supplierId;
    }

    await api.post('/products', productToCreate);
    toast.success('Sản phẩm đã được thêm thành công!');
    emit('product-created');
    emit('close');
  } catch (error) {
    console.error('Lỗi khi thêm sản phẩm:', error);
    toast.error('Lỗi khi thêm sản phẩm.');
  }
};
</script>

<style scoped>
/* Add any specific styles for the modal here if needed */
</style>