<template>
  <div class="purchase-returns-page">
    <!-- Header -->
    <div class="mb-8">
      <h1 class="text-3xl font-bold text-gray-900 mb-2">Trả hàng nhập</h1>
      <p class="text-gray-600">Quản lý trả hàng cho nhà cung cấp</p>
    </div>

    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
      <div class="card">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium text-gray-600">Tổng phiếu trả</p>
            <p class="text-2xl font-bold text-gray-900">{{ totalReturns }}</p>
          </div>
          <div class="h-12 w-12 bg-red-100 rounded-full flex items-center justify-center">
            <svg class="h-6 w-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M16 15v-1a4 4 0 00-4-4H8m0 0l3 3m-3-3l3-3m9 14V5a2 2 0 00-2-2H6a2 2 0 00-2 2v16l4-2 4 2 4-2 4 2z" />
            </svg>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium text-gray-600">Chờ xử lý</p>
            <p class="text-2xl font-bold text-yellow-600">{{ pendingReturns }}</p>
          </div>
          <div class="h-12 w-12 bg-yellow-100 rounded-full flex items-center justify-center">
            <svg class="h-6 w-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
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
            <p class="text-2xl font-bold text-green-600">{{ completedReturns }}</p>
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

    <!-- Purchase Returns Management -->
    <div class="card">
      <div class="flex items-center justify-between mb-6">
        <h2 class="text-xl font-semibold text-gray-900">Danh sách phiếu trả hàng</h2>
        <div class="flex items-center space-x-3">
          <div class="flex items-center space-x-2">
            <input v-model="searchTerm" type="text" placeholder="Tìm phiếu trả hàng..." 
                   class="form-input w-64">
            <select v-model="statusFilter" class="form-input w-32">
              <option value="">Tất cả</option>
              <option value="pending">Chờ xử lý</option>
              <option value="approved">Đã duyệt</option>
              <option value="completed">Hoàn thành</option>
              <option value="rejected">Từ chối</option>
            </select>
          </div>
          <button @click="showCreateReturnModal = true" class="btn-primary">
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
            Tạo phiếu trả hàng
          </button>
        </div>
      </div>

      <!-- Returns Table -->
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Mã phiếu trả
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Nhà cung cấp
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Ngày tạo
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Lý do
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
            <tr v-for="returnOrder in filteredReturns" :key="returnOrder.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm font-medium text-gray-900">{{ returnOrder.returnCode }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm text-gray-900">{{ returnOrder.supplierName }}</div>
                <div class="text-sm text-gray-500">{{ returnOrder.originalOrderCode }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm text-gray-900">{{ formatDate(returnOrder.returnDate) }}</div>
              </td>
              <td class="px-6 py-4">
                <div class="text-sm text-gray-900">{{ returnOrder.reason }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="px-2 py-1 text-xs font-medium rounded-full"
                      :class="getStatusClass(returnOrder.status)">
                  {{ getStatusText(returnOrder.status) }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                ₫{{ formatCurrency(returnOrder.totalAmount) }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <div class="flex items-center space-x-2">
                  <button @click="viewReturnDetails(returnOrder)" class="text-blue-600 hover:text-blue-800">
                    <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                    </svg>
                  </button>
                  <button @click="editReturn(returnOrder)" class="text-green-600 hover:text-green-800">
                    <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                    </svg>
                  </button>
                  <button @click="deleteReturn(returnOrder)" class="text-red-600 hover:text-red-800">
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

    <!-- Create Return Modal -->
    <div v-if="showCreateReturnModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-2xl max-h-[80vh] overflow-y-auto">
        <h3 class="text-lg font-semibold text-gray-900 mb-4">Tạo phiếu trả hàng mới</h3>
        
        <form @submit.prevent="createReturn">
          <div class="space-y-4">
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="form-label">Mã phiếu trả</label>
                <input v-model="newReturn.returnCode" type="text" class="form-input" required>
              </div>
              <div>
                <label class="form-label">Đơn nhập gốc</label>
                <select v-model="newReturn.originalOrderId" class="form-input" required>
                  <option value="">Chọn đơn nhập hàng</option>
                  <option v-for="order in purchaseOrders" :key="order.id" :value="order.id">
                    {{ order.orderCode }} - {{ order.supplierName }}
                  </option>
                </select>
              </div>
            </div>
            
            <div>
              <label class="form-label">Ngày trả hàng</label>
              <input v-model="newReturn.returnDate" type="date" class="form-input" required>
            </div>
            
            <div>
              <label class="form-label">Lý do trả hàng</label>
              <select v-model="newReturn.reason" class="form-input" required>
                <option value="">Chọn lý do</option>
                <option value="defective">Hàng lỗi</option>
                <option value="wrong_item">Sai sản phẩm</option>
                <option value="overstock">Dư thừa</option>
                <option value="expired">Hết hạn sử dụng</option>
                <option value="damaged">Hàng hư hỏng</option>
                <option value="other">Khác</option>
              </select>
            </div>
            
            <div>
              <label class="form-label">Mô tả chi tiết</label>
              <textarea v-model="newReturn.description" class="form-input" rows="3" 
                        placeholder="Mô tả chi tiết lý do trả hàng"></textarea>
            </div>
            
            <!-- Return Items -->
            <div>
              <div class="flex items-center justify-between mb-3">
                <label class="form-label">Sản phẩm trả</label>
                <button @click="addReturnItem" type="button" class="btn-secondary">
                  <svg class="mr-1 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                  </svg>
                  Thêm sản phẩm
                </button>
              </div>
              
              <div class="space-y-2">
                <div v-for="(item, index) in newReturn.items" :key="index" 
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
                  <button @click="removeReturnItem(index)" type="button" class="text-red-600 hover:text-red-800">
                    <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          </div>
          
          <div class="flex justify-end space-x-3 mt-6">
            <button @click="showCreateReturnModal = false" type="button" class="btn-secondary">
              Hủy
            </button>
            <button type="submit" class="btn-primary">
              Tạo phiếu trả hàng
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PurchaseReturns',
  data() {
    return {
      searchTerm: '',
      statusFilter: '',
      showCreateReturnModal: false,
      totalReturns: 34,
      pendingReturns: 8,
      completedReturns: 22,
      totalValue: 145000000,
      returns: [
        {
          id: 1,
          returnCode: 'RT-2024-001',
          supplierName: 'Công ty TNHH Phụ tùng Hà Nội',
          originalOrderCode: 'PO-2024-001',
          returnDate: '2024-01-16',
          reason: 'Hàng lỗi',
          status: 'pending',
          totalAmount: 5000000,
          description: 'Phanh đĩa bị nứt'
        },
        {
          id: 2,
          returnCode: 'RT-2024-002',
          supplierName: 'Công ty Cổ phần Ô tô Thành Công',
          originalOrderCode: 'PO-2024-002',
          returnDate: '2024-01-15',
          reason: 'Sai sản phẩm',
          status: 'approved',
          totalAmount: 8000000,
          description: 'Giao nhầm mã sản phẩm'
        },
        {
          id: 3,
          returnCode: 'RT-2024-003',
          supplierName: 'Cửa hàng Phụ tùng Minh Tuấn',
          originalOrderCode: 'PO-2024-003',
          returnDate: '2024-01-14',
          reason: 'Dư thừa',
          status: 'completed',
          totalAmount: 3000000,
          description: 'Nhập quá nhiều so với nhu cầu'
        },
        {
          id: 4,
          returnCode: 'RT-2024-004',
          supplierName: 'Công ty TNHH Phụ tùng Việt Nam',
          originalOrderCode: 'PO-2024-004',
          returnDate: '2024-01-13',
          reason: 'Hàng hư hỏng',
          status: 'rejected',
          totalAmount: 4500000,
          description: 'Hư hỏng trong quá trình vận chuyển'
        }
      ],
      purchaseOrders: [
        { id: 1, orderCode: 'PO-2024-001', supplierName: 'Công ty TNHH Phụ tùng Hà Nội' },
        { id: 2, orderCode: 'PO-2024-002', supplierName: 'Công ty Cổ phần Ô tô Thành Công' },
        { id: 3, orderCode: 'PO-2024-003', supplierName: 'Cửa hàng Phụ tùng Minh Tuấn' },
        { id: 4, orderCode: 'PO-2024-004', supplierName: 'Công ty TNHH Phụ tùng Việt Nam' }
      ],
      products: [
        { id: 1, name: 'Phanh đĩa Hyundai' },
        { id: 2, name: 'Lọc dầu động cơ' },
        { id: 3, name: 'Bình acquy 12V' },
        { id: 4, name: 'Dây curoa' },
        { id: 5, name: 'Lốp xe tải 825R16' },
        { id: 6, name: 'Đèn pha LED' }
      ],
      newReturn: {
        returnCode: '',
        originalOrderId: '',
        returnDate: new Date().toISOString().substr(0, 10),
        reason: '',
        description: '',
        items: []
      }
    }
  },
  computed: {
    filteredReturns() {
      let filtered = this.returns;
      
      if (this.searchTerm) {
        filtered = filtered.filter(returnOrder => 
          returnOrder.returnCode.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
          returnOrder.supplierName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
          returnOrder.originalOrderCode.toLowerCase().includes(this.searchTerm.toLowerCase())
        );
      }
      
      if (this.statusFilter) {
        filtered = filtered.filter(returnOrder => returnOrder.status === this.statusFilter);
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
        case 'approved': return 'bg-blue-100 text-blue-800';
        case 'completed': return 'bg-green-100 text-green-800';
        case 'rejected': return 'bg-red-100 text-red-800';
        default: return 'bg-gray-100 text-gray-800';
      }
    },
    getStatusText(status) {
      switch (status) {
        case 'pending': return 'Chờ xử lý';
        case 'approved': return 'Đã duyệt';
        case 'completed': return 'Hoàn thành';
        case 'rejected': return 'Từ chối';
        default: return 'Không xác định';
      }
    },
    addReturnItem() {
      this.newReturn.items.push({
        productId: '',
        quantity: 1,
        price: 0
      });
    },
    removeReturnItem(index) {
      this.newReturn.items.splice(index, 1);
    },
    createReturn() {
      const selectedOrder = this.purchaseOrders.find(o => o.id == this.newReturn.originalOrderId);
      
      const newReturn = {
        id: Date.now(),
        returnCode: this.newReturn.returnCode,
        supplierName: selectedOrder?.supplierName || '',
        originalOrderCode: selectedOrder?.orderCode || '',
        returnDate: this.newReturn.returnDate,
        reason: this.getReasonText(this.newReturn.reason),
        status: 'pending',
        totalAmount: this.newReturn.items.reduce((sum, item) => sum + (item.quantity * item.price), 0),
        description: this.newReturn.description
      };
      
      this.returns.unshift(newReturn);
      this.showCreateReturnModal = false;
      
      // Reset form
      this.newReturn = {
        returnCode: '',
        originalOrderId: '',
        returnDate: new Date().toISOString().substr(0, 10),
        reason: '',
        description: '',
        items: []
      };
      
      console.log('Đã tạo phiếu trả hàng:', newReturn);
    },
    getReasonText(reason) {
      const reasons = {
        'defective': 'Hàng lỗi',
        'wrong_item': 'Sai sản phẩm',
        'overstock': 'Dư thừa',
        'expired': 'Hết hạn sử dụng',
        'damaged': 'Hàng hư hỏng',
        'other': 'Khác'
      };
      return reasons[reason] || reason;
    },
    viewReturnDetails(returnOrder) {
      console.log('Xem chi tiết phiếu trả hàng:', returnOrder.returnCode);
    },
    editReturn(returnOrder) {
      console.log('Chỉnh sửa phiếu trả hàng:', returnOrder.returnCode);
    },
    deleteReturn(returnOrder) {
      if (confirm('Bạn có chắc chắn muốn xóa phiếu trả hàng này?')) {
        const index = this.returns.findIndex(r => r.id === returnOrder.id);
        if (index !== -1) {
          this.returns.splice(index, 1);
          console.log('Đã xóa phiếu trả hàng:', returnOrder.returnCode);
        }
      }
    }
  }
}
</script>

<style scoped>
.purchase-returns-page {
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
