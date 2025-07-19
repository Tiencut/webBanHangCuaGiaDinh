<template>
  <div class="container mx-auto px-4 py-6">
    <h1 class="text-2xl font-bold mb-4">Debug: Danh sách sản phẩm</h1>
    <div v-if="loading" class="text-gray-500">Đang tải...</div>
    <div v-if="error" class="text-red-600">Lỗi: {{ error }}</div>
    <table v-if="products.length" class="min-w-full divide-y divide-gray-200 mt-4">
      <thead class="bg-gray-50">
        <tr>
          <th class="px-4 py-2 text-left">ID</th>
          <th class="px-4 py-2 text-left">Tên</th>
          <th class="px-4 py-2 text-left">SKU</th>
          <th class="px-4 py-2 text-left">Giá bán</th>
          <th class="px-4 py-2 text-left">Tồn kho</th>
        </tr>
      </thead>
      <tbody class="bg-white divide-y divide-gray-200">
        <tr v-for="p in products" :key="p.id">
          <td class="px-4 py-2">{{ p.id }}</td>
          <td class="px-4 py-2">{{ p.name }}</td>
          <td class="px-4 py-2">{{ p.sku }}</td>
          <td class="px-4 py-2">{{ p.salePrice }}</td>
          <td class="px-4 py-2">{{ p.stock ?? p.totalStock ?? 0 }}</td>
        </tr>
      </tbody>
    </table>
    <div v-if="!loading && !products.length && !error" class="text-gray-500 mt-4">Không có sản phẩm nào.</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { productsAPI } from '@/services'

const products = ref([])
const loading = ref(false)
const error = ref('')

const loadProducts = async () => {
  loading.value = true
  error.value = ''
  try {
    const res = await productsAPI.getAll()
    products.value = res.data.content || res.data
    console.log('Sản phẩm:', products.value)
  } catch (e) {
    error.value = e.message || 'Lỗi không xác định'
    console.error('Lỗi khi tải sản phẩm:', e)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadProducts()
})
</script> 