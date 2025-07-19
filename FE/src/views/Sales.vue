<template>
  <div class="sales-page">
    <!-- Quick Stats -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
      <div class="card">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium text-gray-600">Doanh thu hôm nay</p>
            <p class="text-2xl font-bold text-green-600">₫{{ formatCurrency(todayRevenue) }}</p>
          </div>
          <div class="h-12 w-12 bg-green-100 rounded-full flex items-center justify-center">
            <svg class="h-6 w-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
            </svg>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium text-gray-600">Đơn hàng hôm nay</p>
            <p class="text-2xl font-bold text-blue-600">{{ todayOrders }}</p>
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
            <p class="text-sm font-medium text-gray-600">Sản phẩm bán chạy</p>
            <p class="text-2xl font-bold text-purple-600">{{ topSellingItems }}</p>
          </div>
          <div class="h-12 w-12 bg-purple-100 rounded-full flex items-center justify-center">
            <svg class="h-6 w-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M13 7h8m0 0v8m0-8l-8 8-4-4-4 4" />
            </svg>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium text-gray-600">Khách hàng mới</p>
            <p class="text-2xl font-bold text-orange-600">{{ newCustomers }}</p>
          </div>
          <div class="h-12 w-12 bg-orange-100 rounded-full flex items-center justify-center">
            <svg class="h-6 w-6 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Sales Interface -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Product Selection -->
      <div class="lg:col-span-2">
        <div class="card">
          <div class="flex items-center justify-between mb-6">
            <h2 class="text-xl font-semibold text-gray-900">Chọn sản phẩm</h2>
            <div class="flex items-center space-x-3">
              <input v-model="productSearch" type="text" placeholder="Tìm sản phẩm..." 
                     class="form-input w-64">
              <select v-model="categoryFilter" class="form-input w-40">
                <option value="">Tất cả danh mục</option>
                <option v-for="category in categories" :key="category" :value="category">
                  {{ category }}
                </option>
              </select>
            </div>
          </div>

          <!-- Product Grid -->
          <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 max-h-96 overflow-y-auto">
            <div v-for="product in filteredProducts" :key="product.id" 
                 @click="addToCart(product)"
                 class="border rounded-lg p-4 hover:border-[#0070F4] hover:shadow-md cursor-pointer transition-all duration-200">
              <div class="text-center">
                <div class="h-16 w-16 bg-gray-200 rounded-lg mx-auto mb-3 flex items-center justify-center">
                  <svg class="h-8 w-8 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                  </svg>
                </div>
                <h3 class="font-medium text-gray-900 text-sm mb-1">{{ product.name }}</h3>
                <p class="text-xs text-gray-600 mb-2">{{ product.category }}</p>
                <p class="text-lg font-bold text-[#0070F4]">₫{{ formatCurrency(product.price) }}</p>
                <p class="text-xs text-gray-500">Kho: {{ product.stock }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Shopping Cart -->
      <div class="lg:col-span-1">
        <div class="card sticky top-20">
          <h2 class="text-xl font-semibold text-gray-900 mb-6">Giỏ hàng</h2>
          
          <!-- Customer Selection -->
          <div class="mb-6">
            <label class="form-label">Khách hàng</label>
            <div class="flex items-center space-x-2">
              <select v-model="selectedCustomer" class="form-input flex-1">
                <option value="">Khách lẻ</option>
                <option v-for="customer in customers" :key="customer.id" :value="customer">
                  {{ customer.name }}
                </option>
              </select>
              <button @click="showAddCustomerModal = true" class="btn-secondary">
                <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                </svg>
              </button>
            </div>
          </div>

          <!-- Cart Items -->
          <div class="space-y-3 mb-6 max-h-60 overflow-y-auto">
            <div v-if="cartItems.length === 0" class="text-center text-gray-500 py-8">
              <svg class="h-12 w-12 mx-auto mb-3 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-2.5-2.5M7 13l2.5 2.5" />
              </svg>
              <p>Chưa có sản phẩm nào</p>
            </div>
            
            <div v-for="item in cartItems" :key="item.id" 
                 class="flex items-center justify-between p-3 border rounded-lg">
              <div class="flex-1">
                <h4 class="font-medium text-gray-900 text-sm">{{ item.name }}</h4>
                <p class="text-xs text-gray-600">₫{{ formatCurrency(item.price) }} x {{ item.quantity }}</p>
              </div>
              <div class="flex items-center space-x-2">
                <button @click="updateQuantity(item, item.quantity - 1)" 
                        class="w-6 h-6 rounded-full bg-gray-200 flex items-center justify-center">
                  <svg class="h-3 w-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 12H4" />
                  </svg>
                </button>
                <span class="w-8 text-center text-sm">{{ item.quantity }}</span>
                <button @click="updateQuantity(item, item.quantity + 1)" 
                        class="w-6 h-6 rounded-full bg-gray-200 flex items-center justify-center">
                  <svg class="h-3 w-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                  </svg>
                </button>
                <button @click="removeFromCart(item)" class="text-red-600 hover:text-red-800">
                  <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </div>
            </div>
          </div>

          <!-- Total and Payment -->
          <div class="border-t pt-4">
            <div class="space-y-2 mb-4">
              <div class="flex justify-between text-sm">
                <span>Tạm tính:</span>
                <span>₫{{ formatCurrency(subtotal) }}</span>
              </div>
              <div class="flex justify-between text-sm">
                <span>Giảm giá:</span>
                <span>₫{{ formatCurrency(discount) }}</span>
              </div>
              <div class="flex justify-between font-bold text-lg border-t pt-2">
                <span>Tổng cộng:</span>
                <span class="text-[#0070F4]">₫{{ formatCurrency(total) }}</span>
              </div>
            </div>

            <!-- Payment Method -->
            <div class="mb-4">
              <label class="form-label">Phương thức thanh toán</label>
              <select v-model="paymentMethod" class="form-input">
                <option value="cash">Tiền mặt</option>
                <option value="card">Thẻ</option>
                <option value="transfer">Chuyển khoản</option>
              </select>
            </div>

            <!-- Action Buttons -->
            <div class="space-y-2">
              <button @click="processPayment" 
                      :disabled="cartItems.length === 0"
                      class="w-full btn-primary"
                      :class="{ 'opacity-50 cursor-not-allowed': cartItems.length === 0 }">
                <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z" />
                </svg>
                Thanh toán
              </button>
              <button @click="clearCart" 
                      :disabled="cartItems.length === 0"
                      class="w-full btn-secondary"
                      :class="{ 'opacity-50 cursor-not-allowed': cartItems.length === 0 }">
                Xóa giỏ hàng
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Add Customer Modal -->
    <div v-if="showAddCustomerModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-md">
        <h3 class="text-lg font-semibold text-gray-900 mb-4">Thêm khách hàng mới</h3>
        
        <form @submit.prevent="addCustomer">
          <div class="space-y-4">
            <div>
              <label class="form-label">Tên khách hàng</label>
              <input v-model="newCustomer.name" type="text" class="form-input" required>
            </div>
            <div>
              <label class="form-label">Số điện thoại</label>
              <input v-model="newCustomer.phone" type="tel" class="form-input">
            </div>
            <div>
              <label class="form-label">Email</label>
              <input v-model="newCustomer.email" type="email" class="form-input">
            </div>
          </div>
          
          <div class="flex justify-end space-x-3 mt-6">
            <button @click="showAddCustomerModal = false" type="button" class="btn-secondary">
              Hủy
            </button>
            <button type="submit" class="btn-primary">
              Thêm khách hàng
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { productsAPI, customersApi, ordersApi, salesApi } from '@/api'

export default {
  name: 'Sales',
  setup() {
    // Stats data
    const todayRevenue = ref(0)
    const todayOrders = ref(0)
    const topSellingItems = ref(0)
    const newCustomers = ref(0)

    // Filters
    const productSearch = ref('')
    const categoryFilter = ref('')

    // Cart data
    const cartItems = ref([])
    const selectedCustomer = ref(null)
    const paymentMethod = ref('cash')
    const discount = ref(0)

    // Modals
    const showAddCustomerModal = ref(false)

    // Data
    const categories = ref([])
    const products = ref([])
    const customers = ref([])
    const newCustomer = ref({
      name: '',
      phone: '',
      email: '',
      address: '',
      customerType: 'RETAIL'
    })

    // Loading states
    const loadingProducts = ref(false)
    const loadingCustomers = ref(false)
    const loadingOrder = ref(false)
    const loadingStats = ref(false)

    // Load sales stats
    const loadStats = async () => {
      try {
        loadingStats.value = true
        const response = await salesApi.getStats('today')
        const stats = response.data
        todayRevenue.value = stats.revenue || 0
        todayOrders.value = stats.orders || 0
        topSellingItems.value = stats.topSelling || 0
        newCustomers.value = stats.newCustomers || 0
      } catch (error) {
        console.error('Error loading sales stats:', error)
        // Fallback to mock data
        todayRevenue.value = 12500000
        todayOrders.value = 8
        topSellingItems.value = 15
        newCustomers.value = 3
      } finally {
        loadingStats.value = false
      }
    }

    // Load products
    const loadProducts = async () => {
      loadingProducts.value = true
      try {
        const response = await productsAPI.getProducts(0, 100, productSearch.value, categoryFilter.value || null, null)
        products.value = response.data.content || []
        
        // Extract unique categories
        const uniqueCategories = [...new Set(products.value.map(p => p.category?.name || p.category))]
        categories.value = uniqueCategories.filter(Boolean)
      } catch (error) {
        console.error('Error loading products:', error)
        // Fallback to mock data
        products.value = [
          { id: 1, name: 'Lốp xe 900R20', category: 'Lốp xe', price: 2500000, stock: 50 },
          { id: 2, name: 'Phanh tay', category: 'Hệ thống phanh', price: 150000, stock: 100 },
          { id: 3, name: 'Bộ lọc dầu', category: 'Hệ thống động cơ', price: 80000, stock: 200 }
        ]
        categories.value = ['Lốp xe', 'Hệ thống phanh', 'Hệ thống động cơ']
      } finally {
        loadingProducts.value = false
      }
    }

    // Load customers
    const loadCustomers = async () => {
      loadingCustomers.value = true
      try {
        const response = await customersApi.getAll(0, 100)
        customers.value = response.data.content || response.data
      } catch (error) {
        console.error('Error loading customers:', error)
        // Fallback to mock data
        customers.value = [
          { id: 1, name: 'Nguyễn Văn A', phone: '0123456789' },
          { id: 2, name: 'Trần Thị B', phone: '0987654321' }
        ]
      } finally {
        loadingCustomers.value = false
      }
    }

    // Add to cart
    const addToCart = (product) => {
      const existingItem = cartItems.value.find(item => item.id === product.id)
      if (existingItem) {
        existingItem.quantity += 1
      } else {
        cartItems.value.push({
          ...product,
          quantity: 1
        })
      }
    }

    // Update quantity
    const updateQuantity = (item, newQuantity) => {
      if (newQuantity <= 0) {
        removeFromCart(item)
      } else {
        item.quantity = newQuantity
      }
    }

    // Remove from cart
    const removeFromCart = (item) => {
      const index = cartItems.value.findIndex(cartItem => cartItem.id === item.id)
      if (index > -1) {
        cartItems.value.splice(index, 1)
      }
    }

    // Clear cart
    const clearCart = () => {
      cartItems.value = []
      selectedCustomer.value = null
      discount.value = 0
    }

    // Add customer
    const addCustomer = async () => {
      if (!newCustomer.value.name || !newCustomer.value.phone) {
        alert('Vui lòng nhập tên và số điện thoại!')
        return
      }
      try {
        await customersApi.create(newCustomer.value)
        showAddCustomerModal.value = false
        newCustomer.value = { name: '', phone: '', email: '', address: '', customerType: 'RETAIL' }
        loadCustomers() // reload list
        alert('Thêm khách hàng thành công!')
      } catch (e) {
        alert('Lỗi khi thêm khách hàng!')
        console.error(e)
      }
    }

    // Process payment (tạo đơn hàng)
    const processPayment = async () => {
      if (cartItems.value.length === 0) return
      loadingOrder.value = true
      try {
        const orderPayload = {
          customerId: selectedCustomer.value?.id || null,
          items: cartItems.value.map(item => ({
            productId: item.id,
            quantity: item.quantity,
            price: item.price
          })),
          totalAmount: total.value,
          discount: discount.value,
          paymentMethod: paymentMethod.value,
          status: 'CONFIRMED',
          notes: ''
        }
        await ordersApi.create(orderPayload)
        
        // Show success toast
        if (window.$toast) {
          window.$toast.success('Thanh toán thành công!', `Tổng tiền: ₫${formatCurrency(total.value)}`)
        } else {
          alert(`Thanh toán thành công! Tổng tiền: ₫${formatCurrency(total.value)}`)
        }
        
        clearCart()
        loadStats() // Reload stats after successful order
      } catch (e) {
        if (window.$toast) {
          window.$toast.error('Lỗi khi tạo đơn hàng!', e.message || 'Vui lòng thử lại')
        } else {
          alert('Lỗi khi tạo đơn hàng!')
        }
        console.error(e)
      } finally {
        loadingOrder.value = false
      }
    }

    // Computed
    const filteredProducts = computed(() => {
      let filtered = products.value
      if (productSearch.value) {
        filtered = filtered.filter(product =>
          product.name.toLowerCase().includes(productSearch.value.toLowerCase())
        )
      }
      if (categoryFilter.value) {
        filtered = filtered.filter(product => 
          product.category?.name === categoryFilter.value || product.category === categoryFilter.value
        )
      }
      return filtered
    })
    const subtotal = computed(() => cartItems.value.reduce((sum, item) => sum + (item.price * item.quantity), 0))
    const total = computed(() => subtotal.value - discount.value)

    // Format currency
    const formatCurrency = (value) => {
      return new Intl.NumberFormat('vi-VN').format(value)
    }

    onMounted(() => {
      loadStats()
      loadProducts()
      loadCustomers()
    })

    return {
      // Stats
      todayRevenue,
      todayOrders,
      topSellingItems,
      newCustomers,
      // Filters
      productSearch,
      categoryFilter,
      // Cart
      cartItems,
      selectedCustomer,
      paymentMethod,
      discount,
      // Modals
      showAddCustomerModal,
      // Data
      categories,
      products,
      customers,
      newCustomer,
      // Loading
      loadingProducts,
      loadingCustomers,
      loadingOrder,
      loadingStats,
      // Methods
      addToCart,
      updateQuantity,
      removeFromCart,
      clearCart,
      addCustomer,
      processPayment,
      formatCurrency,
      // Computed
      filteredProducts,
      subtotal,
      total
    }
  }
}
</script>

<style scoped>
.sales-page {
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
  justify-content: center;
  font-size: 14px;
  font-weight: 500;
}

.btn-primary:hover:not(:disabled) {
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
  justify-content: center;
  font-size: 14px;
  font-weight: 500;
}

.btn-secondary:hover:not(:disabled) {
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

/* Custom scrollbar */
.max-h-96::-webkit-scrollbar,
.max-h-60::-webkit-scrollbar {
  width: 6px;
}

.max-h-96::-webkit-scrollbar-track,
.max-h-60::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.max-h-96::-webkit-scrollbar-thumb,
.max-h-60::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.max-h-96::-webkit-scrollbar-thumb:hover,
.max-h-60::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
