<template>
  <div class="container mx-auto px-4 py-6">
    <div v-if="loading" class="text-center py-12">
      <svg class="mx-auto h-12 w-12 text-gray-400 animate-spin" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
      </svg>
      <h3 class="mt-2 text-sm font-medium text-gray-900">Đang tải dữ liệu khách hàng...</h3>
    </div>
    <div v-else>
      <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
        <h1 class="text-2xl font-bold text-gray-800 mb-2">{{ customer.name }}</h1>
        <div class="flex flex-wrap gap-4 mb-2">
          <div><span class="font-medium">SĐT:</span> {{ customer.phone }}</div>
          <div v-if="customer.email"><span class="font-medium">Email:</span> {{ customer.email }}</div>
          <div v-if="customer.address"><span class="font-medium">Địa chỉ:</span> {{ customer.address }}</div>
          <div><span class="font-medium">Loại:</span> {{ getTypeText(customer.type) }}</div>
          <div><span class="font-medium">Trạng thái:</span> {{ getStatusText(customer.status) }}</div>
        </div>
        <div class="flex flex-wrap gap-4 mb-2">
          <div><span class="font-medium">Công nợ hiện tại:</span> <span class="text-red-600">{{ formatCurrency(customer.currentDebt) }}</span></div>
          <div><span class="font-medium">Hạn mức công nợ:</span> {{ formatCurrency(customer.creditLimit) }}</div>
          <div><span class="font-medium">Chiết khấu:</span> {{ customer.discountPercentage }}%</div>
          <div><span class="font-medium">Điểm tích lũy:</span> {{ customer.loyaltyPoints }}</div>
        </div>
        <div v-if="customer.notes" class="mt-2 text-gray-600">{{ customer.notes }}</div>
      </div>
      <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
        <h2 class="text-xl font-semibold mb-4">Lịch sử mua hàng</h2>
        <div v-if="orders.length === 0" class="text-gray-500">Khách hàng chưa có đơn hàng nào.</div>
        <table v-else class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Mã đơn</th>
              <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Ngày</th>
              <th class="px-4 py-2 text-right text-xs font-medium text-gray-500 uppercase">Tổng tiền</th>
              <th class="px-4 py-2 text-center text-xs font-medium text-gray-500 uppercase">Trạng thái</th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="order in orders" :key="order.id">
              <td class="px-4 py-2">{{ order.orderCode || order.id }}</td>
              <td class="px-4 py-2">{{ formatDate(order.orderDate) }}</td>
              <td class="px-4 py-2 text-right">{{ formatCurrency(order.totalAmount) }}</td>
              <td class="px-4 py-2 text-center">
                <span :class="getOrderStatusClass(order.status)">{{ getOrderStatusText(order.status) }}</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { customersApi } from '../api/customers.js'
import { ordersApi } from '../api/orders.js'

const route = useRoute()
const customerId = route.params.id
const customer = ref({})
const orders = ref([])
const loading = ref(true)

const fetchCustomer = async () => {
  try {
    const res = await customersApi.getById(customerId)
    customer.value = res.data
  } catch (e) {
    customer.value = {}
  }
}

const fetchOrders = async () => {
  try {
    const res = await ordersApi.getAll(0, 100, '', customerId)
    orders.value = res.data.content || res.data
  } catch (e) {
    orders.value = []
  }
}

onMounted(async () => {
  loading.value = true
  await fetchCustomer()
  await fetchOrders()
  loading.value = false
})

const formatCurrency = (amount) => {
  if (amount == null) return '-'
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount)
}
const formatDate = (dateString) => {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleDateString('vi-VN')
}
const getTypeText = (type) => {
  switch (type) {
    case 'INDIVIDUAL': return 'Cá nhân'
    case 'BUSINESS': return 'Doanh nghiệp'
    default: return 'Không xác định'
  }
}
const getStatusText = (status) => {
  switch (status) {
    case 'ACTIVE': return 'Hoạt động'
    case 'INACTIVE': return 'Không hoạt động'
    case 'BLOCKED': return 'Bị khóa'
    default: return 'Không xác định'
  }
}
const getOrderStatusText = (status) => {
  switch (status) {
    case 'PENDING': return 'Chờ xử lý'
    case 'CONFIRMED': return 'Đã xác nhận'
    case 'COMPLETED': return 'Hoàn thành'
    case 'CANCELLED': return 'Đã hủy'
    default: return status
  }
}
const getOrderStatusClass = (status) => {
  switch (status) {
    case 'COMPLETED': return 'bg-green-100 text-green-800 px-2 py-1 rounded-full text-xs'
    case 'CANCELLED': return 'bg-red-100 text-red-800 px-2 py-1 rounded-full text-xs'
    case 'PENDING': return 'bg-yellow-100 text-yellow-800 px-2 py-1 rounded-full text-xs'
    default: return 'bg-gray-100 text-gray-800 px-2 py-1 rounded-full text-xs'
  }
}
</script>

<style scoped>
</style> 