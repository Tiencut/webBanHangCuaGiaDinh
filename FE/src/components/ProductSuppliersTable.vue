<template>
  <div>
    <div v-if="loading" class="text-center py-8 text-gray-500">Đang tải danh sách nhà cung cấp...</div>
    <div v-else>
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">Nhà cung cấp</th>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">Giá nhập</th>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">Giá bán</th>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">Tồn kho</th>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">Lần nhập gần nhất</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in suppliers" :key="item.id" class="hover:bg-gray-50">
            <td class="px-3 py-2 text-sm">{{ item.supplier?.name }}</td>
            <td class="px-3 py-2 text-sm text-right">{{ formatCurrency(item.costPrice) }}</td>
            <td class="px-3 py-2 text-sm text-right">{{ formatCurrency(item.sellingPrice) }}</td>
            <td class="px-3 py-2 text-sm text-center">{{ item.stockQuantity }}</td>
            <td class="px-3 py-2 text-sm text-center">{{ formatDateTime(item.lastPurchaseDate) }}</td>
          </tr>
          <tr v-if="suppliers.length === 0">
            <td colspan="5" class="text-center text-gray-400 py-6">Không có dữ liệu nhà cung cấp</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { api } from '../api'

const props = defineProps({
  productId: { type: [Number, String], required: true }
})

const suppliers = ref([])
const loading = ref(false)

const fetchSuppliers = async () => {
  if (!props.productId) return
  loading.value = true
  try {
    const res = await api.get(`/api/product-suppliers/product/${props.productId}`)
    suppliers.value = res.data
  } catch (e) {
    suppliers.value = []
  } finally {
    loading.value = false
  }
}

onMounted(fetchSuppliers)
watch(() => props.productId, fetchSuppliers)

function formatDateTime(dt) {
  if (!dt) return ''
  const d = new Date(dt)
  return d.toLocaleDateString('vi-VN')
}
function formatCurrency(val) {
  if (val == null) return ''
  return Number(val).toLocaleString('vi-VN')
}
</script> 