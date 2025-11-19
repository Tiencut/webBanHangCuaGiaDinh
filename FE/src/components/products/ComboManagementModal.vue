<template>
    <div v-if="showComboModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-4xl max-h-[80vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-semibold">Quản lý combo: {{ props.selectedComboProduct?.name }}</h3>
          <button @click="$emit('close')" class="text-gray-400 hover:text-gray-600">
            <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <div v-if="props.selectedComboProduct" class="space-y-6">
          <!-- Combo Summary -->
          <div class="bg-gray-50 p-4 rounded-lg">
            <h4 class="font-medium text-gray-900 mb-2">Thông tin combo</h4>
            <div class="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
              <div>
                <span class="text-gray-600">Tổng giá combo:</span>
                <div class="font-medium">{{ formatCurrency(comboTotalPriceLocal) }}</div>
              </div>
              <div>
                <span class="text-gray-600">Số linh kiện:</span>
                <div class="font-medium">{{ comboComponentsLocal.length }}</div>
              </div>
              <div>
                <span class="text-gray-600">Có thể thay thế:</span>
                <div class="font-medium">{{ substitutableCount }} linh kiện</div>
              </div>
              <div>
                <span class="text-gray-600">Trạng thái:</span>
                <div class="font-medium text-green-600">Hoạt động</div>
              </div>
            </div>
          </div>

          <!-- Combo Components Table -->
          <div>
            <div class="flex justify-between items-center mb-3">
              <h5 class="font-medium text-gray-900">Linh kiện trong combo</h5>
              <button 
                @click="addComponentToCombo()"
                class="btn-primary text-sm"
              >
                Thêm linh kiện
              </button>
            </div>
            
            <div class="overflow-x-auto">
              <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                  <tr>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Linh kiện
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Số lượng
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Giá trong combo
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Có thể thay thế
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Thao tác
                    </th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="component in comboComponentsLocal" :key="component.id">
                    <td class="px-6 py-4 whitespace-nowrap">
                      <div>
                        <div class="text-sm font-medium text-gray-900">{{ component.childProduct.name }}</div>
                        <div class="text-sm text-gray-500">{{ component.childProduct.sku }}</div>
                      </div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ component.quantity }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ formatCurrency(component.bundlePrice) }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <span 
                        class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                        :class="component.isSubstitutable ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'"
                      >
                        {{ component.isSubstitutable ? 'Có' : 'Không' }}
                      </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <div class="flex space-x-2">
                        <button 
                          @click="editComponent(component)"
                          class="text-blue-600 hover:text-blue-900"
                        >
                          Sửa
                        </button>
                        <button 
                          @click="removeComponent(component)"
                          class="text-red-600 hover:text-red-900"
                        >
                          Xóa
                        </button>
                        <button 
                          v-if="component.isSubstitutable"
                          @click="showSubstitutes(component)"
                          class="text-purple-600 hover:text-purple-900"
                        >
                          Thay thế
                        </button>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            </div>
          </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, watch } from 'vue';
import { formatCurrency } from '@/utils/helpers';

const props = defineProps({
  showComboModal: Boolean,
  selectedComboProduct: Object,
});

const emit = defineEmits([
  'close',
  'update-combo-product', // Emit when combo product is updated
  'add-component-to-combo',
  'edit-component',
  'remove-component',
  'show-substitutes'
]);

const comboComponentsLocal = ref([]);
const comboTotalPriceLocal = ref(0);

const substitutableCount = computed(() => {
  return comboComponentsLocal.value.filter(component => component.isSubstitutable).length;
});

// Watch for changes in selectedComboProduct prop to initialize local state
watch(() => props.selectedComboProduct, (newProduct) => {
  if (newProduct) {
    comboComponentsLocal.value = [];
    comboTotalPriceLocal.value = 0;
    if (newProduct.isCombo && newProduct.components) {
      newProduct.components.forEach(component => {
        comboComponentsLocal.value.push({
          id: component.id,
          childProduct: component.childProduct,
          quantity: component.quantity,
          bundlePrice: component.bundlePrice,
          isSubstitutable: component.isSubstitutable
        });
        comboTotalPriceLocal.value += component.bundlePrice * component.quantity;
      });
    }
  }
}, { immediate: true }); // Run immediately on component mount

const addComponentToCombo = () => {
  const newComponent = {
    id: Date.now(), // Simple unique ID
    childProduct: null,
    quantity: 1,
    bundlePrice: 0,
    isSubstitutable: false
  };
  comboComponentsLocal.value.push(newComponent);
  // Recalculate total price
  comboTotalPriceLocal.value = comboComponentsLocal.value.reduce((sum, c) => sum + c.bundlePrice * c.quantity, 0);
  emit('add-component-to-combo', newComponent); // Emit to parent if needed
};

const editComponent = (component) => {
  console.log('Edit component:', component);
  emit('edit-component', component); // Emit to parent if needed
  // Logic to edit component - this might involve another modal or inline editing
};

const removeComponent = (component) => {
  if (confirm(`Bạn có chắc chắn muốn xóa linh kiện "${component.childProduct?.name || 'N/A'}" khỏi combo?`)) {
    comboComponentsLocal.value = comboComponentsLocal.value.filter(c => c.id !== component.id);
    // Recalculate total price
    comboTotalPriceLocal.value = comboComponentsLocal.value.reduce((sum, c) => sum + c.bundlePrice * c.quantity, 0);
    emit('remove-component', component); // Emit to parent if needed
  }
};

const showSubstitutes = (component) => {
  console.log('Show substitutes for component:', component);
  emit('show-substitutes', component); // Emit to parent if needed
  // Logic to show substitutes for a component
};
</script>

<style scoped>
/* Có thể thêm các style cụ thể cho modal tại đây nếu cần */
</style>