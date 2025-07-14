<template>
  <div class="md:hidden" :class="isOpen ? 'block' : 'hidden'">
    <div class="px-4 py-2 space-y-1 bg-gray-50 border-t border-gray-200">
      <router-link to="/" 
        class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
        :class="$route.name === 'Home' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
        @click="$emit('close')">
        Tổng quan
      </router-link>
      
      <router-link to="/products"
        class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
        :class="$route.name === 'Products' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
        @click="$emit('close')">
        Danh sách sản phẩm
      </router-link>
      
      <router-link to="/pricing"
        class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
        :class="$route.name === 'Pricing' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
        @click="$emit('close')">
        Thiết lập giá
      </router-link>
      
      <router-link to="/suppliers"
        class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
        :class="$route.name === 'Suppliers' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
        @click="$emit('close')">
        Nhà cung cấp
      </router-link>
      
      <router-link to="/purchase-order"
        class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
        :class="$route.name === 'PurchaseOrder' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
        @click="$emit('close')">
        Nhập hàng
      </router-link>
      
      <router-link to="/purchase-returns"
        class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
        :class="$route.name === 'PurchaseReturns' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
        @click="$emit('close')">
        Trả hàng nhập
      </router-link>
      
      <router-link to="/order-create"
        class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
        :class="$route.name === 'OrderCreate' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
        @click="$emit('close')">
        Đặt hàng
      </router-link>
      
      <router-link to="/orders"
        class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
        :class="$route.name === 'Orders' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
        @click="$emit('close')">
        Đơn hàng
      </router-link>
      
      <router-link to="/order-returns"
        class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
        :class="$route.name === 'OrderReturns' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
        @click="$emit('close')">
        Trả hàng
      </router-link>
      
      <router-link to="/delivery-partners"
        class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
        :class="$route.name === 'DeliveryPartners' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
        @click="$emit('close')">
        Đối tác giao hàng
      </router-link>
      
      <router-link to="/customers"
        class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
        :class="$route.name === 'Customers' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
        @click="$emit('close')">
        Khách hàng
      </router-link>
      
      <router-link to="/vehicles"
        class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
        :class="$route.name === 'Vehicles' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
        @click="$emit('close')">
        Loại xe
      </router-link>
      
      <router-link to="/inventory-check"
        class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
        :class="$route.name === 'InventoryCheck' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
        @click="$emit('close')">
        Kiểm kho
      </router-link>
      
      <router-link to="/import"
        class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
        :class="$route.name === 'ImportData' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
        @click="$emit('close')">
        Import dữ liệu
      </router-link>
      
      <router-link to="/sales"
        class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
        :class="$route.name === 'Sales' ? 'bg-gradient-to-r from-[#FF6B35] to-[#FF8E53] text-white' : 'bg-gradient-to-r from-[#0070F4] to-[#0056b3] text-white hover:from-[#0056b3] hover:to-[#004085]'"
        @click="$emit('close')">
        <div class="flex items-center">
          <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-2.5-2.5M7 13l2.5 2.5" />
          </svg>
          Bán hàng
        </div>
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MobileMenu',
  props: {
    isOpen: {
      type: Boolean,
      default: false
    }
  },
  emits: ['close']
}
</script>
