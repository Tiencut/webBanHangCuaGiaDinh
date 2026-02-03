<template>
  <div class="orders-container">
    <div class="header">
      <h2>Quản lý đơn hàng</h2>
      <button @click="showAddForm = true" class="btn-primary">Tạo đơn hàng mới</button>
    </div>

    <!-- Search and Filter -->
    <div class="search-filter">
      <input 
        v-model="searchTerm" 
        @input="searchOrders"
        placeholder="Tìm kiếm đơn hàng..." 
        class="search-input"
      />
      <select v-model="selectedStatus" @change="filterOrders" class="filter-select">
        <option value="">Tất cả trạng thái</option>
        <option value="PENDING">Chờ xử lý</option>
        <option value="PROCESSING">Đang xử lý</option>
        <option value="COMPLETED">Hoàn thành</option>
        <option value="CANCELLED">Đã hủy</option>
      </select>
    </div>

    <!-- Orders Table -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>Mã đơn hàng</th>
            <th>Khách hàng</th>
            <th>Ngày đặt</th>
            <th>Tổng tiền</th>
            <th>Trạng thái</th>
            <th>Thao tác</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in filteredOrders" :key="order.id">
            <td>{{ order.orderCode }}</td>
            <td>{{ order.customerName }}</td>
            <td>{{ formatDate(order.orderDate) }}</td>
            <td>{{ formatCurrency(order.totalAmount) }}</td>
            <td>
              <span :class="getStatusClass(order.status)">
                {{ getStatusText(order.status) }}
              </span>
            </td>
            <td>
              <button @click="viewOrder(order)" class="btn-view">Xem</button>
              <button @click="editOrder(order)" class="btn-edit">Sửa</button>
              <button @click="deleteOrder(order.id)" class="btn-delete">Xóa</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">Trước</button>
      <span>Trang {{ currentPage }} / {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage === totalPages">Sau</button>
    </div>

    <!-- Add/Edit Modal -->
    <div v-if="showAddForm || showEditForm" class="modal-overlay">
      <div class="modal">
        <h3>{{ showEditForm ? 'Sửa đơn hàng' : 'Tạo đơn hàng mới' }}</h3>
        <form @submit.prevent="showEditForm ? updateOrder() : addOrder()">
          <div class="form-group">
            <label>Khách hàng:</label>
            <select v-model="formData.customerId" required>
              <option value="">Chọn khách hàng</option>
              <option v-for="customer in customers" :key="customer.id" :value="customer.id">
                {{ customer.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>Trạng thái:</label>
            <select v-model="formData.status" required>
              <option value="PENDING">Chờ xử lý</option>
              <option value="PROCESSING">Đang xử lý</option>
              <option value="COMPLETED">Hoàn thành</option>
              <option value="CANCELLED">Đã hủy</option>
            </select>
          </div>
          <div class="form-group">
            <label>Ghi chú:</label>
            <textarea v-model="formData.notes" rows="3"></textarea>
          </div>
          <div class="form-actions">
            <button type="submit" class="btn-primary">
              {{ showEditForm ? 'Cập nhật' : 'Tạo' }}
            </button>
            <button type="button" @click="closeForm" class="btn-secondary">Hủy</button>
          </div>
        </form>
      </div>
    </div>

    <!-- View Order Details Modal -->
    <div v-if="showViewModal" class="modal-overlay">
      <div class="modal large-modal">
        <h3>Chi tiết đơn hàng #{{ selectedOrder?.orderCode }}</h3>
        <div v-if="selectedOrder" class="order-details">
          <div class="order-info">
            <p><strong>Khách hàng:</strong> {{ selectedOrder.customerName }}</p>
            <p><strong>Ngày đặt:</strong> {{ formatDate(selectedOrder.orderDate) }}</p>
            <p><strong>Trạng thái:</strong> 
              <span :class="getStatusClass(selectedOrder.status)">
                {{ getStatusText(selectedOrder.status) }}
              </span>
            </p>
            <p><strong>Tổng tiền:</strong> {{ formatCurrency(selectedOrder.totalAmount) }}</p>
          </div>
          
          <div class="order-items">
            <h4>Chi tiết sản phẩm</h4>
            <table class="items-table">
              <thead>
                <tr>
                  <th>Sản phẩm</th>
                  <th>Số lượng</th>
                  <th>Đơn giá</th>
                  <th>Thành tiền</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in selectedOrder.orderDetails" :key="item.id">
                  <td>
                    <div>{{ item.productName }}</div>
                    <div v-if="selectedOrder._allocations">
                      <div v-for="allocItem in (selectedOrder._allocations || [])" :key="allocItem.orderItemId">
                        <template v-if="allocItem.orderItemId === item.id">
                          <div v-for="a in allocItem.allocations" :key="a.inventoryId">
                            <span class="chip">Lấy từ: {{ a.supplierName }} ({{ a.suggestedQuantity }})</span>
                          </div>
                          <button @click.prevent="openSupplierPicker(item)" class="btn-link">Chọn khác</button>
                        </template>
                      </div>
                    </div>
                  </td>
                  <td>{{ item.quantity }}</td>
                  <td>{{ formatCurrency(item.unitPrice) }}</td>
                  <td>{{ formatCurrency(item.quantity * item.unitPrice) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="form-actions">
          <button @click="showViewModal = false" class="btn-secondary">Đóng</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ordersApi } from '../api/orders.js'
import { customersApi } from '../api/customers.js'

export default {
  name: 'Orders',
  data() {
    return {
      orders: [],
      filteredOrders: [],
      customers: [],
      searchTerm: '',
      selectedStatus: '',
      currentPage: 1,
      pageSize: 10,
      totalPages: 1,
      showAddForm: false,
      showEditForm: false,
      showViewModal: false,
      formData: {
        customerId: '',
        status: 'PENDING',
        notes: ''
      },
      editingId: null,
      selectedOrder: null
    }
  },
  async mounted() {
    await Promise.all([
      this.loadOrders(),
      this.loadCustomers()
    ])
  },
  methods: {
    async loadOrders() {
      try {
        const response = await ordersApi.getAll()
        this.orders = response.data || []
        this.filteredOrders = [...this.orders]
        this.updatePagination()
      } catch (error) {
        console.error('Lỗi khi tải danh sách đơn hàng:', error)
        // Fallback to mock data
        this.orders = [
          {
            id: 1,
            orderCode: 'DH001',
            customerName: 'Nguyễn Văn A',
            orderDate: '2024-01-15',
            totalAmount: 1500000,
            status: 'COMPLETED',
            orderDetails: [
              { id: 1, productName: 'Lốp xe tải', quantity: 4, unitPrice: 375000 }
            ]
          },
          {
            id: 2,
            orderCode: 'DH002',
            customerName: 'Trần Thị B',
            orderDate: '2024-01-16',
            totalAmount: 800000,
            status: 'PENDING',
            orderDetails: [
              { id: 2, productName: 'Phanh xe', quantity: 2, unitPrice: 400000 }
            ]
          }
        ]
        this.filteredOrders = [...this.orders]
        this.updatePagination()
      }
    },
    async loadCustomers() {
      try {
        const response = await customersApi.getAll()
        this.customers = response.data || []
      } catch (error) {
        console.error('Lỗi khi tải danh sách khách hàng:', error)
        this.customers = [
          { id: 1, name: 'Nguyễn Văn A' },
          { id: 2, name: 'Trần Thị B' }
        ]
      }
    },
    searchOrders() {
      this.filterOrders()
    },
    filterOrders() {
      let filtered = this.orders

      if (this.searchTerm) {
        const term = this.searchTerm.toLowerCase()
        filtered = filtered.filter(o => 
          o.orderCode.toLowerCase().includes(term) ||
          o.customerName.toLowerCase().includes(term)
        )
      }

      if (this.selectedStatus) {
        filtered = filtered.filter(o => o.status === this.selectedStatus)
      }

      this.filteredOrders = filtered
      this.currentPage = 1
      this.updatePagination()
    },
    updatePagination() {
      this.totalPages = Math.ceil(this.filteredOrders.length / this.pageSize)
      if (this.currentPage > this.totalPages) {
        this.currentPage = this.totalPages || 1
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++
      }
    },
    async viewOrder(order) {
      this.selectedOrder = order
      this.showViewModal = true
      // request allocation suggestion
      try {
        const res = await fetch(`/api/orders/${order.id}/allocate`, { method: 'POST' })
        const body = await res.json()
        this.selectedOrder._allocations = body.allocations || []
      } catch (e) {
        console.error('Failed to fetch allocation suggestion', e)
        this.selectedOrder._allocations = []
      }
    },
    editOrder(order) {
      this.formData = {
        customerId: order.customerId || '',
        status: order.status,
        notes: order.notes || ''
      }
      this.editingId = order.id
      this.showEditForm = true
    },
    async addOrder() {
      try {
        await ordersApi.create(this.formData)
        await this.loadOrders()
        this.closeForm()
      } catch (error) {
        console.error('Lỗi khi tạo đơn hàng:', error)
      }
    },
    async updateOrder() {
      try {
        await ordersApi.update(this.editingId, this.formData)
        await this.loadOrders()
        this.closeForm()
      } catch (error) {
        console.error('Lỗi khi cập nhật đơn hàng:', error)
      }
    },
    async deleteOrder(id) {
      if (confirm('Bạn có chắc muốn xóa đơn hàng này?')) {
        try {
          await ordersApi.delete(id)
          await this.loadOrders()
        } catch (error) {
          console.error('Lỗi khi xóa đơn hàng:', error)
        }
      }
    },
    closeForm() {
      this.showAddForm = false
      this.showEditForm = false
      this.formData = {
        customerId: '',
        status: 'PENDING',
        notes: ''
      }
      this.editingId = null
    },
    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleDateString('vi-VN')
    },
    formatCurrency(amount) {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(amount)
    },
    getStatusClass(status) {
      const classes = {
        'PENDING': 'status-pending',
        'PROCESSING': 'status-processing',
        'COMPLETED': 'status-completed',
        'CANCELLED': 'status-cancelled'
      }
      return classes[status] || 'status-pending'
    },
    getStatusText(status) {
      const texts = {
        'PENDING': 'Chờ xử lý',
        'PROCESSING': 'Đang xử lý',
        'COMPLETED': 'Hoàn thành',
        'CANCELLED': 'Đã hủy'
      }
      return texts[status] || status
    }
  }
}
</script>

<style scoped>
.orders-container {
  background: white;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.header h2 {
  color: #333;
  margin: 0;
}

.search-filter {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
}

.search-input, .filter-select {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  flex: 1;
}

.table-container {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 1rem;
}

.data-table th,
.data-table td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.data-table th {
  background-color: #f8f9fa;
  font-weight: 600;
}

.status-pending {
  background: #fff3cd;
  color: #856404;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.875rem;
}

.status-processing {
  background: #cce5ff;
  color: #004085;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.875rem;
}

.status-completed {
  background: #d4edda;
  color: #155724;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.875rem;
}

.status-cancelled {
  background: #f8d7da;
  color: #721c24;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.875rem;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-top: 1rem;
}

.pagination button {
  padding: 0.5rem 1rem;
  border: 1px solid #ddd;
  background: white;
  cursor: pointer;
  border-radius: 4px;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  min-width: 400px;
  max-height: 80vh;
  overflow-y: auto;
}

.large-modal {
  min-width: 600px;
  max-width: 800px;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.form-group textarea {
  resize: vertical;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 1rem;
}

.btn-primary {
  background: #007bff;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.btn-secondary {
  background: #6c757d;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.btn-view {
  background: #17a2b8;
  color: white;
  border: none;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 0.5rem;
}

.btn-edit {
  background: #ffc107;
  color: #212529;
  border: none;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 0.5rem;
}

.btn-delete {
  background: #dc3545;
  color: white;
  border: none;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  cursor: pointer;
}

.order-details {
  margin-bottom: 1rem;
}

.order-info {
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 4px;
  margin-bottom: 1rem;
}

.order-info p {
  margin: 0.5rem 0;
}

.order-items h4 {
  margin-bottom: 1rem;
}

.items-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 1rem;
}

.items-table th,
.items-table td {
  padding: 0.5rem;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.items-table th {
  background-color: #f8f9fa;
  font-weight: 600;
}
</style> 