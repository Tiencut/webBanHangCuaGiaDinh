<template>
  <div>
  <nav class="hidden md:flex items-center space-x-6 flex-1">
    <!-- Dashboard -->
    <router-link to="/"
      class="flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
      :class="$route.name === 'Home' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'">
      <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
          d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2H5a2 2 0 00-2-2z" />
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
          d="M8 5a2 2 0 012-2h4a2 2 0 012 2v6H8V5z" />
      </svg>
      Tổng quan
    </router-link>

    <!-- Products Dropdown -->
    <DropdownMenu 
      title="Hàng hóa" 
      :active-routes="['Products', 'Pricing']">
      <template #icon>
        <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
        </svg>
      </template>
      <template #items>
        <DropdownItem to="/products" title="Danh sách sản phẩm" route-name="Products">
          <template #icon>
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M4 6h16M4 10h16M4 14h16M4 18h16" />
            </svg>
          </template>
        </DropdownItem>
        <DropdownItem to="/pricing" title="Thiết lập giá" route-name="Pricing">
          <template #icon>
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
            </svg>
          </template>
        </DropdownItem>
          <DropdownItem to="/vehicles" title="Loại xe" route-name="Vehicles">
            <template #icon>
              <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M13 6H4a2 2 0 00-2 2v6a2 2 0 002 2h1m8-10V4a2 2 0 00-2-2H4m7 4v10m0 0h6a2 2 0 002-2v-6a2 2 0 00-2-2h-6z" />
              </svg>
            </template>
          </DropdownItem>
        <!-- Categories Link -->
        <DropdownItem to="/categories" title="Quản lý danh mục" route-name="Categories">
          <template #icon>
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 10h16M4 14h16M4 18h16" />
            </svg>
          </template>
        </DropdownItem>
      </template>
    </DropdownMenu>

    <!-- Orders Dropdown -->
    <DropdownMenu 
      title="Đơn hàng" 
      :active-routes="['Orders', 'OrderCreate', 'OrderReturns', 'DeliveryPartners']">
      <template #icon>
        <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            d="M9 5H7a2 2 0 00-2 2v6a2 2 0 002 2h2m0 0h2a2 2 0 002-2V7a2 2 0 00-2-2H9m0 0V3a2 2 0 012-2h2a2 2 0 012 2v2" />
        </svg>
      </template>
      <template #items>
        <DropdownItem to="/order-create" title="Đặt hàng" route-name="OrderCreate">
          <template #icon>
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
          </template>
        </DropdownItem>
        <DropdownItem to="/inventory-check" title="Kiểm kho" route-name="InventoryCheck">
          <template #icon>
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M9 5H7a2 2 0 00-2 2v6a2 2 0 002 2h2m0 0h2a2 2 0 002-2V7a2 2 0 00-2-2H9m0 0V3a2 2 0 012-2h2a2 2 0 012 2v2" />
            </svg>
          </template>
        </DropdownItem>
        <DropdownItem to="/orders" title="Đơn hàng" route-name="Orders">
          <template #icon>
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M9 5H7a2 2 0 00-2 2v6a2 2 0 002 2h2m0 0h2a2 2 0 002-2V7a2 2 0 00-2-2H9m0 0V3a2 2 0 012-2h2a2 2 0 012 2v2" />
            </svg>
          </template>
        </DropdownItem>
        <DropdownItem to="/order-returns" title="Trả hàng" route-name="OrderReturns">
          <template #icon>
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M16 15v-1a4 4 0 00-4-4H8m0 0l3 3m-3-3l3-3m9 14V5a2 2 0 00-2-2H6a2 2 0 00-2 2v16l4-2 4 2 4-2 4 2z" />
            </svg>
          </template>
        </DropdownItem>
        <hr class="my-2 border-gray-200">
        <DropdownItem to="/delivery-partners" title="Đối tác giao hàng" route-name="DeliveryPartners">
          <template #icon>
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4" />
            </svg>
          </template>
        </DropdownItem>
      </template>
    </DropdownMenu>

    <!-- Customers Dropdown (contains Khách hàng and Quản lý nợ) -->
    <DropdownMenu title="Khách hàng" :active-routes="['Customers','Debts']">
      <template #icon>
        <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1z" />
        </svg>
      </template>
      <template #items>
        <DropdownItem to="/customers" title="Danh sách khách hàng" route-name="Customers">
          <template #icon>
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1z" />
            </svg>
          </template>
        </DropdownItem>
        <DropdownItem to="/debts" title="Quản lý nợ" route-name="Debts">
          <template #icon>
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10" />
            </svg>
          </template>
        </DropdownItem>
      </template>
    </DropdownMenu>

    <!-- Regular Menu Items (rendered from data to reduce repetition) -->
    <router-link v-for="item in regularLinks" :key="item.to" :to="item.to"
      class="flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
      :class="route.name === item.routeName ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'">
      <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" v-html="item.svgInner"></svg>
      {{ item.title }}
    </router-link>

    <!-- Sales (Bán hàng) - Positioned at the far right -->
    <div class="ml-auto">
      <router-link to="/sales"
        class="flex items-center px-4 py-2 text-sm font-medium rounded-lg transition-all duration-200 shadow-md"
        :class="$route.name === 'Sales' ? 'bg-gradient-to-r from-[#FF6B35] to-[#FF8E53] text-white shadow-lg' : 'bg-gradient-to-r from-[#0070F4] to-[#0056b3] text-white hover:from-[#0056b3] hover:to-[#004085] shadow-lg'">
        <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-2.5-2.5M7 13l2.5 2.5" />
        </svg>
        Bán hàng
      </router-link>
    </div>
  </nav>
  <MobileMenu :sidebarOpen="sidebarOpen" @close-sidebar="sidebarOpen = false" />
  </div>
</template>

<script>
import { ref, onMounted, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import MobileMenu from './MobileMenu.vue'
import DropdownMenu from './DropdownMenu.vue'
import DropdownItem from './DropdownItem.vue'

export default {
  name: 'NavigationMenu',
  components: {
    MobileMenu,
    DropdownMenu,
    DropdownItem
  },
  setup() {
    const route = useRoute()
    const isMobileMenuOpen = ref(false)
    const isProductsDropdownOpen = ref(false)
    const isOrdersDropdownOpen = ref(false)
    const isPurchaseDropdownOpen = ref(false)
    const sidebarOpen = ref(false)

    const regularLinks = [
      {
        to: '/import',
        routeName: 'ImportData',
        title: 'Import',
        svgInner: `
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M9 19l3 3m0 0l3-3m-3 3V10" />
        `
      },
      {
        to: '/notes',
        routeName: 'Notes',
        title: 'Ghi chú',
        svgInner: `
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            d="M12 20h9M12 4h9M3 9h18M3 15h18" />
        `
      }
    ]

    // Mounted logic
    onMounted(() => {
    // Close sidebar on window resize
    window.addEventListener('resize', () => {
      if (window.innerWidth >= 1024) {
          sidebarOpen.value = false
      }
    })
    
    // Fix dropdown navigation by ensuring proper event handling
      nextTick(() => {
      // Add event listeners to all dropdown items
      const dropdownItems = document.querySelectorAll('.dropdown-menu a[href^="/"]')
      dropdownItems.forEach(item => {
        item.addEventListener('click', (e) => {
          // Allow normal router-link behavior
          e.stopPropagation()
          })
        })
      })
    })

    return {
      route,
      isMobileMenuOpen,
      isProductsDropdownOpen,
      isOrdersDropdownOpen,
      isPurchaseDropdownOpen,
      sidebarOpen,
      regularLinks
    }
  }
}
</script>
