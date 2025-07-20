<template>
  <div>
    <div v-if="loading" class="text-center py-8 text-gray-500">Đang tải lịch sử kho...</div>
    <div v-else>
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">Chứng từ</th>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">Thời gian</th>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">Loại giao dịch</th>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">Đối tác</th>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">Giá GD</th>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">Giá vốn</th>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">Số lượng</th>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">Tồn cuối</th>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">Ghi chú</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in history" :key="item.id" class="hover:bg-gray-50">
            <td class="px-3 py-2 text-sm">{{ item.documentCode }}</td>
            <td class="px-3 py-2 text-sm">{{ formatDateTime(item.transDate) }}</td>
            <td class="px-3 py-2 text-sm">{{ item.transactionType }}</td>
            <td class="px-3 py-2 text-sm">{{ item.partnerName }}</td>
            <td class="px-3 py-2 text-sm text-right">{{ formatCurrency(item.transPrice) }}</td>
            <td class="px-3 py-2 text-sm text-right">{{ formatCurrency(item.cost) }}</td>
            <td class="px-3 py-2 text-sm text-center">{{ item.quantity }}</td>
            <td class="px-3 py-2 text-sm text-center">{{ item.endingStocks }}</td>
            <td class="px-3 py-2 text-sm">{{ item.note }}</td>
          </tr>
          <tr v-if="history.length === 0">
            <td colspan="9" class="text-center text-gray-400 py-6">Không có dữ liệu lịch sử kho</td>
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

const history = ref([])
const loading = ref(false)

const fetchHistory = async () => {
  if (!props.productId) return
  loading.value = true
  try {
    const res = await api.get(`/api/inventory-transactions/product/${props.productId}`)
    history.value = res.data
  } catch (e) {
    history.value = []
  } finally {
    loading.value = false
  }
}

onMounted(fetchHistory)
watch(() => props.productId, fetchHistory)

function formatDateTime(dt) {
  if (!dt) return ''
  const d = new Date(dt)
  return d.toLocaleString('vi-VN')
}
function formatCurrency(val) {
  if (val == null) return ''
  return Number(val).toLocaleString('vi-VN')
}
</script> 