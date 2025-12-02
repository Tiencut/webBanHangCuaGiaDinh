<template>
  <div>
    <div v-if="loading" class="text-gray-500">Đang tải nhà cung cấp...</div>
    <div v-else>
      <table class="min-w-full divide-y divide-gray-200 text-sm">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-3 py-2 text-left">Nhà cung cấp</th>
            <th class="px-3 py-2 text-left">Giá nhập</th>
            <th class="px-3 py-2 text-left">Giá bán</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="suppliers.length === 0">
            <td colspan="3" class="text-center text-gray-400 py-4">Không có dữ liệu</td>
          </tr>
          <tr v-for="s in suppliers" :key="s.id">
            <td class="px-3 py-2">{{ s.name }}</td>
            <td class="px-3 py-2">{{ formatCurrency(s.costPrice) }}</td>
            <td class="px-3 py-2">{{ formatCurrency(s.sellingPrice) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { formatCurrency } from '@/utils/helpers'
import api from '@/services/api'

const props = defineProps({
  'product-id': [String, Number],
  'producta_id': [String, Number]
})

const loading = ref(false)
const suppliers = ref([])

const productId = computed(() => props['product-id'] || props.producta_id)

const fetchSuppliers = async () => {
  if (!productId.value) return
  loading.value = true
  try {
    // const res = await api.get(`/products/${productId.value}/suppliers`)
    // suppliers.value = res.data || []
    suppliers.value = []
  } catch (e) {
    suppliers.value = []
  } finally {
    loading.value = false
  }
}

onMounted(fetchSuppliers)
watch(() => productId.value, fetchSuppliers)
</script>

<style scoped>
</style>
