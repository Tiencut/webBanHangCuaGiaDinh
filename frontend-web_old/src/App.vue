<template>
  <div id="app" class="min-h-screen bg-gray-50">
    <!-- Top Navigation Bar (Horizontal) -->
    <div class="bg-white shadow-lg border-b border-gray-200 fixed top-0 left-0 right-0 z-50">
      <div class="w-full px-2 sm:px-4">
        <div class="flex items-center justify-between h-16">
          <!-- Logo -->
          <div class="flex items-center">
            <div class="h-8 w-8 bg-[#0070F4] rounded-lg flex items-center justify-center mr-3">
              <svg class="h-5 w-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M13 6H4a2 2 0 00-2 2v6a2 2 0 002 2h1m8-10V4a2 2 0 00-2-2H4m7 4v10m0 0h6a2 2 0 002-2v-6a2 2 0 00-2-2h-6z" />
              </svg>
            </div>
            <span class="font-bold text-lg text-gray-900">AutoParts</span>
          </div>

          <!-- Navigation Menu (Horizontal) -->
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

            <!-- Products (Hàng hóa) -->
            <div class="relative group">
              <button
                class="flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200 text-gray-700 hover:bg-gray-100"
                :class="['Products', 'Pricing'].includes($route.name) ? 'bg-[#0070F4] text-white' : ''">
                <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                </svg>
                Hàng hóa
                <svg class="ml-1 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                </svg>
              </button>
              
              <!-- Products Dropdown Menu -->
              <div class="dropdown-menu absolute top-full left-0 mt-1 w-48 bg-white rounded-lg shadow-lg border border-gray-200 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-300 z-50">
                <div class="py-2">
                  <router-link to="/products"
                    class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 transition-colors duration-200"
                    :class="$route.name === 'Products' ? 'bg-blue-50 text-[#0070F4]' : ''"
                    @click.stop>
                    <div class="flex items-center">
                      <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M4 6h16M4 10h16M4 14h16M4 18h16" />
                      </svg>
                      Danh sách sản phẩm
                    </div>
                  </router-link>
                  <router-link to="/pricing"
                    class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 transition-colors duration-200"
                    :class="$route.name === 'Pricing' ? 'bg-blue-50 text-[#0070F4]' : ''"
                    @click.stop>
                    <div class="flex items-center">
                      <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
                      </svg>
                      Thiết lập giá
                    </div>
                  </router-link>
                </div>
              </div>
            </div>

            <!-- Purchase/Import -->
            <div class="relative group">
              <a href="#"
                class="flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200 text-gray-700 hover:bg-gray-100"
                :class="['Suppliers', 'PurchaseOrder', 'PurchaseReturns'].includes($route.name) ? 'bg-[#0070F4] text-white' : ''">
                <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
                </svg>
                Nhập hàng
                <svg class="ml-1 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                </svg>
              </a>

              <!-- Purchase Dropdown Menu -->
              <div
                class="dropdown-menu absolute top-full left-0 mt-1 w-48 bg-white rounded-lg shadow-lg border border-gray-200 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-50">
                <div class="py-2">
                  <router-link to="/suppliers"
                    class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 transition-colors duration-200"
                    :class="$route.name === 'Suppliers' ? 'bg-blue-50 text-[#0070F4]' : ''">
                    <div class="flex items-center">
                      <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                      </svg>
                      Nhà cung cấp
                    </div>
                  </router-link>
                  <router-link to="/purchase-order"
                    class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 transition-colors duration-200"
                    :class="$route.name === 'PurchaseOrder' ? 'bg-blue-50 text-[#0070F4]' : ''">
                    <div class="flex items-center">
                      <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M16 11V7a4 4 0 00-8 0v4M8 11v6a2 2 0 002 2h4a2 2 0 002-2v-6M8 11h8" />
                      </svg>
                      Nhập hàng
                    </div>
                  </router-link>
                  <router-link to="/purchase-returns"
                    class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 transition-colors duration-200"
                    :class="$route.name === 'PurchaseReturns' ? 'bg-blue-50 text-[#0070F4]' : ''">
                    <div class="flex items-center">
                      <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M16 15v-1a4 4 0 00-4-4H8m0 0l3 3m-3-3l3-3m9 14V5a2 2 0 00-2-2H6a2 2 0 00-2 2v16l4-2 4 2 4-2 4 2z" />
                      </svg>
                      Trả hàng nhập
                    </div>
                  </router-link>
                </div>
              </div>
            </div>

            <!-- Orders -->
            <div class="relative group">
              <a href="#"
                class="flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200 text-gray-700 hover:bg-gray-100"
                :class="['Orders', 'OrderCreate', 'OrderReturns', 'DeliveryPartners'].includes($route.name) ? 'bg-[#0070F4] text-white' : ''">
                <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M9 5H7a2 2 0 00-2 2v6a2 2 0 002 2h2m0 0h2a2 2 0 002-2V7a2 2 0 00-2-2H9m0 0V3a2 2 0 012-2h2a2 2 0 012 2v2" />
                </svg>
                Đơn hàng
                <svg class="ml-1 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                </svg>
              </a>

              <!-- Orders Dropdown Menu -->
              <div
                class="dropdown-menu absolute top-full left-0 mt-1 w-48 bg-white rounded-lg shadow-lg border border-gray-200 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-50">
                <div class="py-2">
                  <router-link to="/order-create"
                    class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 transition-colors duration-200"
                    :class="$route.name === 'OrderCreate' ? 'bg-blue-50 text-[#0070F4]' : ''">
                    <div class="flex items-center">
                      <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                      </svg>
                      Đặt hàng
                    </div>
                  </router-link>
                  <router-link to="/orders"
                    class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 transition-colors duration-200"
                    :class="$route.name === 'Orders' ? 'bg-blue-50 text-[#0070F4]' : ''">
                    <div class="flex items-center">
                      <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M9 5H7a2 2 0 00-2 2v6a2 2 0 002 2h2m0 0h2a2 2 0 002-2V7a2 2 0 00-2-2H9m0 0V3a2 2 0 012-2h2a2 2 0 012 2v2" />
                      </svg>
                      Đơn hàng
                    </div>
                  </router-link>
                  <router-link to="/order-returns"
                    class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 transition-colors duration-200"
                    :class="$route.name === 'OrderReturns' ? 'bg-blue-50 text-[#0070F4]' : ''">
                    <div class="flex items-center">
                      <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M16 15v-1a4 4 0 00-4-4H8m0 0l3 3m-3-3l3-3m9 14V5a2 2 0 00-2-2H6a2 2 0 00-2 2v16l4-2 4 2 4-2 4 2z" />
                      </svg>
                      Trả hàng
                    </div>
                  </router-link>
                  <!-- Divider -->
                  <hr class="my-2 border-gray-200">
                  <router-link to="/delivery-partners"
                    class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 transition-colors duration-200"
                    :class="$route.name === 'DeliveryPartners' ? 'bg-blue-50 text-[#0070F4]' : ''">
                    <div class="flex items-center">
                      <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4" />
                      </svg>
                      Đối tác giao hàng
                    </div>
                  </router-link>
                </div>
              </div>
            </div>

            <!-- Customers -->
            <router-link to="/customers"
              class="flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'Customers' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'">
              <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z" />
              </svg>
              Khách hàng
            </router-link>

            <!-- Vehicles -->
            <router-link to="/vehicles"
              class="flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'Vehicles' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'">
              <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M13 6H4a2 2 0 00-2 2v6a2 2 0 002 2h1m8-10V4a2 2 0 00-2-2H4m7 4v10m0 0h6a2 2 0 002-2v-6a2 2 0 00-2-2h-6z" />
              </svg>
              Loại xe
            </router-link>

            <!-- Inventory Check -->
            <router-link to="/inventory-check"
              class="flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'InventoryCheck' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'">
              <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M9 5H7a2 2 0 00-2 2v6a2 2 0 002 2h2m0 0h2a2 2 0 002-2V7a2 2 0 00-2-2H9m0 0V3a2 2 0 012-2h2a2 2 0 012 2v2M9 5a2 2 0 012 2v2a2 2 0 01-2 2m6 0a2 2 0 002-2V7a2 2 0 00-2-2M9 5a2 2 0 012 2v2a2 2 0 01-2 2m6 0a2 2 0 002-2V7a2 2 0 00-2-2" />
              </svg>
              Kiểm kho
            </router-link>

            <!-- Import Data -->
            <router-link to="/import"
              class="flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'ImportData' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'">
              <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M9 19l3 3m0 0l3-3m-3 3V10" />
              </svg>
              Import
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

          <!-- Right Actions -->
          <div class="flex items-center space-x-4">
            <!-- Mobile Menu Button -->
            <button @click="sidebarOpen = !sidebarOpen"
              class="md:hidden p-2 rounded-md text-gray-500 hover:text-gray-600 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-[#0070F4]">
              <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
              </svg>
            </button>

            <!-- Quick Actions -->
            <button
              class="p-2 text-gray-500 hover:text-gray-600 hover:bg-gray-100 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#0070F4]">
              <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </button>

            <!-- User Menu -->
            <div class="relative">
              <button
                class="flex items-center space-x-2 text-gray-700 hover:text-gray-900 focus:outline-none focus:ring-2 focus:ring-[#0070F4] rounded-lg p-2">
                <div class="h-8 w-8 bg-[#0070F4] rounded-full flex items-center justify-center">
                  <span class="text-white text-sm font-medium">A</span>
                </div>
                <span class="hidden lg:block text-sm font-medium">Admin</span>
              </button>
            </div>
          </div>
        </div>

        <!-- Mobile Menu (Dropdown) -->
        <div class="md:hidden" :class="sidebarOpen ? 'block' : 'hidden'">
          <div class="px-4 py-2 space-y-1 bg-gray-50 border-t border-gray-200">
            <router-link to="/" class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'Home' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
              @click="sidebarOpen = false">
              Tổng quan
            </router-link>
            <router-link to="/products"
              class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'Products' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
              @click="sidebarOpen = false">
              Danh sách sản phẩm
            </router-link>
            <router-link to="/pricing"
              class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'Pricing' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
              @click="sidebarOpen = false">
              Thiết lập giá
            </router-link>
            <router-link to="/suppliers"
              class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'Suppliers' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
              @click="sidebarOpen = false">
              Nhà cung cấp
            </router-link>
            <router-link to="/purchase-order"
              class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'PurchaseOrder' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
              @click="sidebarOpen = false">
              Nhập hàng
            </router-link>
            <router-link to="/purchase-returns"
              class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'PurchaseReturns' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
              @click="sidebarOpen = false">
              Trả hàng nhập
            </router-link>
            <router-link to="/order-create"
              class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'OrderCreate' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
              @click="sidebarOpen = false">
              Đặt hàng
            </router-link>
            <router-link to="/orders"
              class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'Orders' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
              @click="sidebarOpen = false">
              Đơn hàng
            </router-link>
            <router-link to="/order-returns"
              class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'OrderReturns' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
              @click="sidebarOpen = false">
              Trả hàng
            </router-link>
            <router-link to="/delivery-partners"
              class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'DeliveryPartners' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
              @click="sidebarOpen = false">
              Đối tác giao hàng
            </router-link>
            <router-link to="/customers"
              class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'Customers' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
              @click="sidebarOpen = false">
              Khách hàng
            </router-link>
            <router-link to="/vehicles"
              class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'Vehicles' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
              @click="sidebarOpen = false">
              Loại xe
            </router-link>
            <router-link to="/inventory-check"
              class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'InventoryCheck' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
              @click="sidebarOpen = false">
              Kiểm kho
            </router-link>
            <router-link to="/import"
              class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'ImportData' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
              @click="sidebarOpen = false">
              Import dữ liệu
            </router-link>
            <router-link to="/sales"
              class="block px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200"
              :class="$route.name === 'Sales' ? 'bg-gradient-to-r from-[#FF6B35] to-[#FF8E53] text-white' : 'bg-gradient-to-r from-[#0070F4] to-[#0056b3] text-white hover:from-[#0056b3] hover:to-[#004085]'"
              @click="sidebarOpen = false">
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
      </div>
    </div>
    <!-- Main Content Area -->
    <div class="pt-16">
      <div class="w-full px-2 sm:px-4 py-4">
        <router-view />
      </div>
    </div>

    <!-- Backdrop for Mobile Menu -->
    <div v-if="sidebarOpen" @click="sidebarOpen = false" class="fixed inset-0 bg-black bg-opacity-50 z-40 md:hidden">
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      sidebarOpen: false
    }
  },
  methods: {
    getPageTitle() {
      const routeNames = {
        'Home': 'Tổng quan',
        'Products': 'Hàng hóa',
        'Pricing': 'Thiết lập giá',
        'Suppliers': 'Nhà cung cấp',
        'PurchaseOrder': 'Nhập hàng',
        'PurchaseReturns': 'Trả hàng nhập',
        'Orders': 'Đơn hàng',
        'OrderCreate': 'Đặt hàng',
        'OrderReturns': 'Trả hàng',
        'DeliveryPartners': 'Đối tác giao hàng',
        'Customers': 'Khách hàng',
        'Vehicles': 'Loại xe',
        'InventoryCheck': 'Kiểm kho',
        'ImportData': 'Import dữ liệu',
        'Sales': 'Bán hàng'
      }
      return routeNames[this.$route.name] || 'Trang chủ'
    },
    closeSidebarOnMobile() {
      if (window.innerWidth < 1024) {
        this.sidebarOpen = false
      }
    }
  },
  mounted() {
    // Close sidebar on window resize
    window.addEventListener('resize', () => {
      if (window.innerWidth >= 1024) {
        this.sidebarOpen = false
      }
    })
    
    // Fix dropdown navigation by ensuring proper event handling
    this.$nextTick(() => {
      // Add event listeners to all dropdown items
      const dropdownItems = document.querySelectorAll('.dropdown-menu a[href^="/"]')
      dropdownItems.forEach(item => {
        item.addEventListener('click', (e) => {
          // Allow normal router-link behavior
          e.stopPropagation()
        })
      })
    })
  }
}
</script>

<style>
/* Global styles */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background-color: #f9fafb;
}

#app {
  width: 100%;
  min-height: 100vh;
}

/* Custom scrollbar */
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* Dropdown Menu Styles */
.group:hover .group-hover\:opacity-100 {
  opacity: 1;
}

.group:hover .group-hover\:visible {
  visibility: visible;
}

/* Improved dropdown hover */
.group {
  position: relative;
}

.group:hover > div:last-child {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.group > div:last-child {
  transform: translateY(-10px);
  transition: all 0.3s ease;
}

/* Keep dropdown visible when hovering over it */
.group > div:last-child:hover {
  opacity: 1;
  visibility: visible;
}

/* Smooth transitions */
.transition-all {
  transition: all 0.3s ease;
}

/* Focus styles */
.focus-ring {
  outline: none;
  box-shadow: 0 0 0 2px #0070F4;
}

/* Button styles */
.btn-primary {
  background-color: #0070F4;
  color: white;
  padding: 8px 16px;
  border-radius: 8px;
  transition: background-color 0.2s;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.btn-secondary {
  background-color: #f3f4f6;
  color: #374151;
  padding: 8px 16px;
  border-radius: 8px;
  transition: background-color 0.2s;
}

.btn-secondary:hover {
  background-color: #e5e7eb;
}

/* Card styles */
.card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  border: 1px solid #e5e7eb;
  padding: 24px;
}

/* Table styles */
.table {
  min-width: 100%;
  border-collapse: collapse;
}

.table th {
  padding: 12px 24px;
  text-align: left;
  font-size: 12px;
  font-weight: 500;
  color: #6b7280;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.table td {
  padding: 16px 24px;
  font-size: 14px;
  color: #111827;
}

/* Form styles */
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

/* Animation classes */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s ease;
}

.slide-enter-from {
  transform: translateX(-100%);
}

.slide-leave-to {
  transform: translateX(-100%);
}

/* Responsive utilities */
@media (max-width: 640px) {
  .mobile-hidden {
    display: none !important;
  }
}

@media (min-width: 1024px) {
  .desktop-hidden {
    display: none !important;
  }
}

/* Dropdown improvements */
.group:hover .group-hover\:opacity-100 {
  opacity: 1 !important;
}

.group:hover .group-hover\:visible {
  visibility: visible !important;
}

/* Ensure dropdown menu stays visible when hovering over it */
.group {
  position: relative;
}

.group .dropdown-menu {
  opacity: 0;
  visibility: hidden;
  transition: opacity 0.3s ease, visibility 0.3s ease;
  pointer-events: none;
}

.group:hover .dropdown-menu {
  opacity: 1;
  visibility: visible;
  pointer-events: auto;
}

/* Keep dropdown visible when hovering over the dropdown itself */
.dropdown-menu:hover {
  opacity: 1;
  visibility: visible;
  pointer-events: auto;
}

/* Improve click area for dropdown items */
.dropdown-menu a {
  cursor: pointer;
  pointer-events: auto;
  text-decoration: none;
  display: block;
  position: relative;
  z-index: 60;
}

.dropdown-menu a:hover {
  text-decoration: none;
}

/* Add a small delay to prevent flicker */
.group:hover .dropdown-menu {
  transition-delay: 0.1s;
}

.group:not(:hover) .dropdown-menu {
  transition-delay: 0.3s;
}
</style>
