<template>
  <div class="purchase-order-page">
    <!-- Header -->
    <div class="mb-8">
      <h1 class="text-3xl font-bold text-gray-900 mb-2">Nhập hàng</h1>
      <p class="text-gray-600">Quản lý đơn nhập hàng từ nhà cung cấp</p>
    </div>

    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
      <div class="card">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium text-gray-600">Tổng đơn nhập</p>
            <p class="text-2xl font-bold text-gray-900">{{ totalOrders }}</p>
          </div>
          <div class="h-12 w-12 bg-blue-100 rounded-full flex items-center justify-center">
            <svg class="h-6 w-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium text-gray-600">Chờ xác nhận</p>
            <p class="text-2xl font-bold text-orange-600">{{ pendingOrders }}</p>
          </div>
          <div class="h-12 w-12 bg-orange-100 rounded-full flex items-center justify-center">
            <svg class="h-6 w-6 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium text-gray-600">Đã hoàn thành</p>
            <p class="text-2xl font-bold text-green-600">{{ completedOrders }}</p>
          </div>
          <div class="h-12 w-12 bg-green-100 rounded-full flex items-center justify-center">
            <svg class="h-6 w-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium text-gray-600">Tổng giá trị</p>
            <p class="text-2xl font-bold text-purple-600">₫{{ formatCurrency(totalValue) }}</p>
          </div>
          <div class="h-12 w-12 bg-purple-100 rounded-full flex items-center justify-center">
            <svg class="h-6 w-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
            </svg>
          </div>
        </div>
      </div>
    </div>

    <!-- Purchase Orders Management -->
    <div class="card">
      <div class="flex items-center justify-between mb-6">
        <h2 class="text-xl font-semibold text-gray-900">Danh sách đơn nhập hàng</h2>
        <div class="flex items-center space-x-3">
          <div class="flex items-center space-x-2">
            <input v-model="searchTerm" type="text" placeholder="Tìm đơn nhập hàng..." 
                   class="form-input w-64">
            <select v-model="statusFilter" class="form-input w-32">
              <option value="">Tất cả</option>
              <option value="PENDING">Chờ xác nhận</option>
              <option value="CONFIRMED">Đã xác nhận</option>
              <option value="RECEIVED">Đã nhận hàng</option>
              <option value="COMPLETED">Hoàn thành</option>
              <option value="CANCELLED">Đã hủy</option>
            </select>
          </div>
          <button @click="showCreateOrderModal = true" class="btn-primary">
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
            Tạo đơn nhập hàng
          </button>
        </div>
      </div>

      <!-- Purchase Orders Table -->
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Mã đơn
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Nhà cung cấp
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Ngày tạo
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Trạng thái
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Tổng tiền
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Thao tác
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="order in filteredOrders" :key="order.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm font-medium text-gray-900">{{ order.orderCode }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm text-gray-900">{{ order.supplierName }}</div>
                <div class="text-sm text-gray-500">{{ order.supplierCode }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm text-gray-900">{{ formatDate(order.orderDate) }}</div>
                <div class="text-sm text-gray-500">{{ order.expectedDate ? formatDate(order.expectedDate) : 'Chưa xác định' }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="px-2 py-1 text-xs font-medium rounded-full"
                      :class="getStatusClass(order.status)">
                  {{ getStatusText(order.status) }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                ₫{{ formatCurrency(order.totalAmount) }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <div class="flex items-center space-x-2">
                  <button @click="viewOrderDetails(order)" class="text-blue-600 hover:text-blue-800">
                    <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                    </svg>
                  </button>
                  <button @click="editOrder(order)" class="text-green-600 hover:text-green-800">
                    <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                    </svg>
                  </button>
                  <button @click="deleteOrder(order)" class="text-red-600 hover:text-red-800">
                    <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                    </svg>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Create Order Modal -->
    <div v-if="showCreateOrderModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-2xl max-h-[80vh] overflow-y-auto">
        <h3 class="text-lg font-semibold text-gray-900 mb-4">Tạo đơn nhập hàng mới</h3>
        
        <form @submit.prevent="createOrder">
          <div class="space-y-4">
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="form-label">Mã đơn hàng</label>
                <input v-model="newOrder.orderCode" type="text" class="form-input" required>
              </div>
              <div>
                <label class="form-label">Nhà cung cấp</label>
                <select v-model="newOrder.supplierId" class="form-input" required>
                  <option value="">Chọn nhà cung cấp</option>
                  <option v-for="supplier in suppliers" :key="supplier.id" :value="supplier.id">
                    {{ supplier.name }}
                  </option>
                </select>
              </div>
            </div>
            
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="form-label">Ngày đặt hàng</label>
                <input v-model="newOrder.orderDate" type="date" class="form-input" required>
              </div>
              <div>
                <label class="form-label">Ngày dự kiến nhận</label>
                <input v-model="newOrder.expectedDate" type="date" class="form-input">
              </div>
            </div>
            
            <div>
              <label class="form-label">Ghi chú</label>
              <textarea v-model="newOrder.notes" class="form-input" rows="3"></textarea>
            </div>
            
            <!-- Order Items -->
            <div>
              <div class="flex items-center justify-between mb-3">
                <label class="form-label">Sản phẩm đặt hàng</label>
                <button @click="addOrderItem" type="button" class="btn-secondary">
                  <svg class="mr-1 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                  </svg>
                  Thêm sản phẩm
                </button>
              </div>
              
              <div class="space-y-2">
                <div v-for="(item, index) in newOrder.items" :key="index" 
                     class="flex items-center space-x-2 p-3 border rounded-lg">
                  <select v-model="item.productId" class="form-input flex-1">
                    <option value="">Chọn sản phẩm</option>
                    <option v-for="product in products" :key="product.id" :value="product.id">
                      {{ product.name }}
                    </option>
                  </select>
                  <input v-model="item.quantity" type="number" placeholder="Số lượng" 
                         class="form-input w-24" min="1">
                  <input v-model="item.price" type="number" placeholder="Giá" 
                         class="form-input w-32" min="0">
                  <button @click="removeOrderItem(index)" type="button" class="text-red-600 hover:text-red-800">
                    <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          </div>
          
          <div class="flex justify-end space-x-3 mt-6">
            <button @click="showCreateOrderModal = false" type="button" class="btn-secondary">
              Hủy
            </button>
            <button type="submit" class="btn-primary">
              Tạo đơn hàng
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { purchaseOrdersApi, suppliersApi } from '@/api'

export default {
  name: 'PurchaseOrder',
  setup() {
    const orders = ref([])
    const suppliers = ref([])
    const loading = ref(false)
    const error = ref('')
    const showCreateOrderModal = ref(false)
    const newOrder = ref({
      orderCode: '',
      supplierId: '',
      orderDate: new Date().toISOString().split('T')[0], // Today's date
      expectedDate: '',
      totalAmount: 0,
      status: 'PENDING',
      notes: '',
      items: []
    })
    const searchTerm = ref('')
    const statusFilter = ref('')

    // Stats
    const totalOrders = computed(() => orders.value.length)
    const pendingOrders = computed(() => orders.value.filter(o => o.status === 'PENDING').length)
    const completedOrders = computed(() => orders.value.filter(o => o.status === 'COMPLETED').length)
    const totalValue = computed(() => orders.value.reduce((sum, o) => sum + (o.totalAmount || 0), 0))

    // Lấy danh sách đơn nhập hàng
    const loadOrders = async () => {
      try {
        loading.value = true
        error.value = ''
        const response = await purchaseOrdersApi.getAll(0, 50, searchTerm.value, statusFilter.value)
        orders.value = response.data.content || response.data || []
      } catch (err) {
        error.value = 'Lỗi khi tải danh sách đơn nhập hàng!'
        console.error('Error loading purchase orders:', err)
        // Fallback to mock data
        orders.value = [
          {
            id: 1,
            orderCode: 'PO-001',
            supplierName: 'Công ty TNHH ABC',
            orderDate: '2025-01-05',
            expectedDate: '2025-01-10',
            totalAmount: 50000000,
            status: 'PENDING',
            notes: 'Đơn hàng phụ tùng xe tải'
          },
          {
            id: 2,
            orderCode: 'PO-002',
            supplierName: 'Nhà phân phối XYZ',
            orderDate: '2025-01-04',
            expectedDate: '2025-01-08',
            totalAmount: 30000000,
            status: 'CONFIRMED',
            notes: 'Đơn hàng lốp xe'
          }
        ]
      } finally {
        loading.value = false
      }
    }

    // Lấy danh sách nhà cung cấp
    const loadSuppliers = async () => {
      try {
        const response = await suppliersApi.getAll(0, 100)
        suppliers.value = response.data.content || response.data || []
      } catch (err) {
        console.error('Error loading suppliers:', err)
        // Fallback to mock data
        suppliers.value = [
          { id: 1, name: 'Công ty TNHH ABC' },
          { id: 2, name: 'Nhà phân phối XYZ' },
          { id: 3, name: 'Công ty phụ tùng DEF' }
        ]
      }
    }

    // Tạo mới đơn nhập hàng
    const createOrder = async () => {
      // Validation
      if (!newOrder.value.orderCode || !newOrder.value.supplierId || !newOrder.value.orderDate) {
        if (window.$toast) {
          window.$toast.warning('Vui lòng nhập đầy đủ thông tin!', 'Mã đơn hàng, nhà cung cấp và ngày đặt hàng là bắt buộc')
        } else {
          alert('Vui lòng nhập đầy đủ thông tin!')
        }
        return
      }

      try {
        await purchaseOrdersApi.create(newOrder.value)
        showCreateOrderModal.value = false
        
        // Reset form
        newOrder.value = {
          orderCode: '',
          supplierId: '',
          orderDate: new Date().toISOString().split('T')[0],
          expectedDate: '',
          totalAmount: 0,
          status: 'PENDING',
          notes: '',
          items: []
        }
        
        // Reload orders
        await loadOrders()
        
        if (window.$toast) {
          window.$toast.success('Tạo đơn nhập hàng thành công!', 'Đơn hàng đã được tạo và chờ xác nhận')
        } else {
          alert('Tạo đơn nhập hàng thành công!')
        }
      } catch (err) {
        console.error('Error creating purchase order:', err)
        if (window.$toast) {
          window.$toast.error('Lỗi khi tạo đơn nhập hàng!', err.message || 'Vui lòng thử lại')
        } else {
          alert('Lỗi khi tạo đơn nhập hàng!')
        }
      }
    }

    // Thêm item vào đơn hàng
    const addOrderItem = () => {
      newOrder.value.items.push({
        productId: '',
        quantity: 1,
        price: 0,
        notes: ''
      })
    }

    // Xóa item khỏi đơn hàng
    const removeOrderItem = (index) => {
      newOrder.value.items.splice(index, 1)
    }

    // Cập nhật tổng tiền
    const updateTotalAmount = () => {
      newOrder.value.totalAmount = newOrder.value.items.reduce((sum, item) => {
        return sum + (item.quantity * item.price)
      }, 0)
    }

    // Lọc đơn hàng
    const filteredOrders = computed(() => {
      let filtered = orders.value
      if (searchTerm.value) {
        const search = searchTerm.value.toLowerCase()
        filtered = filtered.filter(o =>
          o.orderCode?.toLowerCase().includes(search) ||
          o.supplierName?.toLowerCase().includes(search) ||
          o.supplier?.name?.toLowerCase().includes(search)
        )
      }
      if (statusFilter.value) {
        filtered = filtered.filter(o => o.status === statusFilter.value)
      }
      return filtered
    })

    // Format helpers
    const formatCurrency = (v) => new Intl.NumberFormat('vi-VN').format(v)
    const formatDate = (d) => d ? new Date(d).toLocaleDateString('vi-VN') : ''
    
    const getStatusClass = (status) => {
      const map = {
        'PENDING': 'bg-orange-100 text-orange-800',
        'CONFIRMED': 'bg-blue-100 text-blue-800',
        'RECEIVED': 'bg-green-100 text-green-800',
        'COMPLETED': 'bg-purple-100 text-purple-800',
        'CANCELLED': 'bg-red-100 text-red-800'
      }
      return map[status] || 'bg-gray-100 text-gray-800'
    }
    
    const getStatusText = (status) => {
      const map = {
        'PENDING': 'Chờ xác nhận',
        'CONFIRMED': 'Đã xác nhận',
        'RECEIVED': 'Đã nhận hàng',
        'COMPLETED': 'Hoàn thành',
        'CANCELLED': 'Đã huỷ'
      }
      return map[status] || status
    }

    // Generate order code
    const generateOrderCode = () => {
      const date = new Date()
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0')
      newOrder.value.orderCode = `PO-${year}${month}${day}-${random}`
    }

    onMounted(() => {
      loadOrders()
      loadSuppliers()
    })

    return {
      orders,
      suppliers,
      loading,
      error,
      showCreateOrderModal,
      newOrder,
      searchTerm,
      statusFilter,
      totalOrders,
      pendingOrders,
      completedOrders,
      totalValue,
      loadOrders,
      createOrder,
      addOrderItem,
      removeOrderItem,
      updateTotalAmount,
      generateOrderCode,
      filteredOrders,
      formatCurrency,
      formatDate,
      getStatusClass,
      getStatusText
    }
  }
}
</script>

<style scoped>
.purchase-order-page {
  max-width: 100%;
}

.card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  border: 1px solid #e5e7eb;
  padding: 24px;
}

.btn-primary {
  background-color: #0070F4;
  color: white;
  padding: 8px 16px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s;
  display: flex;
  align-items: center;
  font-size: 14px;
  font-weight: 500;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.btn-secondary {
  background-color: #f3f4f6;
  color: #374151;
  padding: 8px 16px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s;
  display: flex;
  align-items: center;
  font-size: 14px;
  font-weight: 500;
}

.btn-secondary:hover {
  background-color: #e5e7eb;
}

.form-input {
  display: block;
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.form-input:focus {
  border-color: #0070F4;
  box-shadow: 0 0 0 2px rgba(0, 112, 244, 0.1);
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #374151;
  margin-bottom: 4px;
}
</style>
