<template>
  <div class="bg-white rounded-lg shadow-sm p-4">
    <div class="flex items-center justify-between mb-4">
      <h3 class="text-lg font-semibold">Danh sách phiếu nhập (Trạng thái dán giá)</h3>
      <div class="flex items-center space-x-2">
        <button @click="loadList" class="px-3 py-1 bg-blue-600 text-white rounded-md text-sm">Tải lại</button>
      </div>
    </div>

    <div v-if="loading" class="text-sm text-gray-500">Đang tải...</div>

    <table v-else class="w-full text-sm">
      <thead>
        <tr class="text-left text-gray-600 border-b">
          <th class="py-2">Tên công ty nhập</th>
          <th class="py-2">Ngày nhập</th>
          <th class="py-2">Đã dán giá?</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="items.length === 0">
          <td colspan="3" class="py-4 text-center text-gray-500">Không có phiếu nhập</td>
        </tr>
        <tr v-for="item in items" :key="item.id" class="border-b hover:bg-gray-50">
          <td class="py-2">{{ supplierName(item) }}</td>
          <td class="py-2">{{ formatDate(item.importDate || item.createdAt || item.orderDate) }}</td>
          <td class="py-2">
            <span v-if="isPriceApplied(item)" class="px-2 py-1 bg-green-100 text-green-800 rounded-full text-xs">Đã dán</span>
            <span v-else class="px-2 py-1 bg-red-100 text-red-800 rounded-full text-xs">Chưa</span>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { purchaseOrdersApi } from '@/api'

const items = ref([])
const loading = ref(false)

const loadList = async () => {
  loading.value = true
  try {
    // request a larger page to get recent items
    const res = await purchaseOrdersApi.getAll(0, 50)
    const data = res.data.content || res.data || []
    items.value = data
  } catch (e) {
    console.error('Error loading purchase orders', e)
    items.value = []
  } finally {
    loading.value = false
  }
}

const supplierName = (item) => {
  return item.supplierName || (item.supplier && item.supplier.name) || item.companyName || 'N/A'
}

const isPriceApplied = (item) => {
  // defensive: check common field names
  return Boolean(item.isPriceApplied || item.priceApplied || item.price_applied)
}

const formatDate = (d) => {
  if (!d) return '-'
  try {
    return new Date(d).toLocaleString('vi-VN')
  } catch (e) {
    return String(d)
  }
}

onMounted(() => {
  loadList()
})
</script>

<style scoped>
/* minimal styling kept by tailwind classes in template */
</style>
