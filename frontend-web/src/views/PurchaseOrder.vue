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
              <option value="pending">Chờ xác nhận</option>
              <option value="confirmed">Đã xác nhận</option>
              <option value="received">Đã nhận hàng</option>
              <option value="completed">Hoàn thành</option>
              <option value="cancelled">Đã hủy</option>
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
export default {
  name: 'PurchaseOrder',
  data() {
    return {
      searchTerm: '',
      statusFilter: '',
      showCreateOrderModal: false,
      totalOrders: 156,
      pendingOrders: 23,
      completedOrders: 118,
      totalValue: 850000000,
      orders: [
        {
          id: 1,
          orderCode: 'PO-2024-001',
          supplierName: 'Công ty TNHH Phụ tùng Hà Nội',
          supplierCode: 'SUP001',
          orderDate: '2024-01-15',
          expectedDate: '2024-01-20',
          status: 'pending',
          totalAmount: 15000000,
          notes: 'Đơn hàng khẩn cấp'
        },
        {
          id: 2,
          orderCode: 'PO-2024-002',
          supplierName: 'Công ty Cổ phần Ô tô Thành Công',
          supplierCode: 'SUP002',
          orderDate: '2024-01-14',
          expectedDate: '2024-01-19',
          status: 'confirmed',
          totalAmount: 25000000,
          notes: 'Đơn hàng thường xuyên'
        },
        {
          id: 3,
          orderCode: 'PO-2024-003',
          supplierName: 'Cửa hàng Phụ tùng Minh Tuấn',
          supplierCode: 'SUP003',
          orderDate: '2024-01-13',
          expectedDate: '2024-01-18',
          status: 'received',
          totalAmount: 8000000,
          notes: ''
        },
        {
          id: 4,
          orderCode: 'PO-2024-004',
          supplierName: 'Công ty TNHH Phụ tùng Việt Nam',
          supplierCode: 'SUP004',
          orderDate: '2024-01-12',
          expectedDate: '2024-01-17',
          status: 'completed',
          totalAmount: 18000000,
          notes: 'Đã hoàn thành'
        },
        {
          id: 5,
          orderCode: 'PO-2024-005',
          supplierName: 'Cửa hàng Phụ tùng Đại Phát',
          supplierCode: 'SUP005',
          orderDate: '2024-01-11',
          expectedDate: '2024-01-16',
          status: 'cancelled',
          totalAmount: 12000000,
          notes: 'Hủy do không đúng yêu cầu'
        }
      ],
      suppliers: [
        { id: 1, name: 'Công ty TNHH Phụ tùng Hà Nội' },
        { id: 2, name: 'Công ty Cổ phần Ô tô Thành Công' },
        { id: 3, name: 'Cửa hàng Phụ tùng Minh Tuấn' },
        { id: 4, name: 'Công ty TNHH Phụ tùng Việt Nam' },
        { id: 5, name: 'Cửa hàng Phụ tùng Đại Phát' }
      ],
      products: [
        { id: 1, name: 'Phanh đĩa Hyundai' },
        { id: 2, name: 'Lọc dầu động cơ' },
        { id: 3, name: 'Bình acquy 12V' },
        { id: 4, name: 'Dây curoa' },
        { id: 5, name: 'Lốp xe tải 825R16' },
        { id: 6, name: 'Đèn pha LED' }
      ],
      newOrder: {
        orderCode: '',
        supplierId: '',
        orderDate: new Date().toISOString().substr(0, 10),
        expectedDate: '',
        notes: '',
        items: []
      }
    }
  },
  computed: {
    filteredOrders() {
      let filtered = this.orders;
      
      if (this.searchTerm) {
        filtered = filtered.filter(order => 
          order.orderCode.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
          order.supplierName.toLowerCase().includes(this.searchTerm.toLowerCase())
        );
      }
      
      if (this.statusFilter) {
        filtered = filtered.filter(order => order.status === this.statusFilter);
      }
      
      return filtered;
    }
  },
  methods: {
    formatCurrency(value) {
      return new Intl.NumberFormat('vi-VN').format(value);
    },
    formatDate(date) {
      return new Date(date).toLocaleDateString('vi-VN');
    },
    getStatusClass(status) {
      switch (status) {
        case 'pending': return 'bg-yellow-100 text-yellow-800';
        case 'confirmed': return 'bg-blue-100 text-blue-800';
        case 'received': return 'bg-purple-100 text-purple-800';
        case 'completed': return 'bg-green-100 text-green-800';
        case 'cancelled': return 'bg-red-100 text-red-800';
        default: return 'bg-gray-100 text-gray-800';
      }
    },
    getStatusText(status) {
      switch (status) {
        case 'pending': return 'Chờ xác nhận';
        case 'confirmed': return 'Đã xác nhận';
        case 'received': return 'Đã nhận hàng';
        case 'completed': return 'Hoàn thành';
        case 'cancelled': return 'Đã hủy';
        default: return 'Không xác định';
      }
    },
    addOrderItem() {
      this.newOrder.items.push({
        productId: '',
        quantity: 1,
        price: 0
      });
    },
    removeOrderItem(index) {
      this.newOrder.items.splice(index, 1);
    },
    createOrder() {
      const newOrder = {
        id: Date.now(),
        orderCode: this.newOrder.orderCode,
        supplierName: this.suppliers.find(s => s.id == this.newOrder.supplierId)?.name || '',
        supplierCode: `SUP${this.newOrder.supplierId.toString().padStart(3, '0')}`,
        orderDate: this.newOrder.orderDate,
        expectedDate: this.newOrder.expectedDate,
        status: 'pending',
        totalAmount: this.newOrder.items.reduce((sum, item) => sum + (item.quantity * item.price), 0),
        notes: this.newOrder.notes
      };
      
      this.orders.unshift(newOrder);
      this.showCreateOrderModal = false;
      
      // Reset form
      this.newOrder = {
        orderCode: '',
        supplierId: '',
        orderDate: new Date().toISOString().substr(0, 10),
        expectedDate: '',
        notes: '',
        items: []
      };
      
      console.log('Đã tạo đơn nhập hàng:', newOrder);
    },
    viewOrderDetails(order) {
      console.log('Xem chi tiết đơn hàng:', order.orderCode);
    },
    editOrder(order) {
      console.log('Chỉnh sửa đơn hàng:', order.orderCode);
    },
    deleteOrder(order) {
      if (confirm('Bạn có chắc chắn muốn xóa đơn hàng này?')) {
        const index = this.orders.findIndex(o => o.id === order.id);
        if (index !== -1) {
          this.orders.splice(index, 1);
          console.log('Đã xóa đơn hàng:', order.orderCode);
        }
      }
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
