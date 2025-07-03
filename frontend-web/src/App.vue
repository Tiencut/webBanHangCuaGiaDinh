<template>
  <div id="app" class="min-h-screen bg-gray-50 flex">
    <!-- Sidebar Navigation (KiotViet Style) -->
    <div class="fixed inset-y-0 left-0 z-50 w-64 bg-white shadow-lg transform transition-transform duration-300 ease-in-out lg:translate-x-0 lg:static lg:inset-0"
         :class="sidebarOpen ? 'translate-x-0' : '-translate-x-full'">
      
      <!-- Logo -->
      <div class="flex items-center justify-center h-16 px-4 bg-[#0070F4] text-white">
        <div class="flex items-center">
          <div class="h-8 w-8 bg-white rounded-lg flex items-center justify-center mr-3">
            <svg class="h-5 w-5 text-[#0070F4]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0z"/>
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 6H4a2 2 0 00-2 2v6a2 2 0 002 2h1m8-10V4a2 2 0 00-2-2H4m7 4v10m0 0h6a2 2 0 002-2v-6a2 2 0 00-2-2h-6z"/>
            </svg>
          </div>
          <span class="font-bold text-lg">AutoParts</span>
        </div>
      </div>

      <!-- Navigation Menu -->
      <nav class="mt-8 px-4">
        <div class="space-y-2">
          <!-- Dashboard -->
          <router-link 
            to="/" 
            class="group flex items-center px-3 py-2.5 text-sm font-medium rounded-lg transition-colors duration-200"
            :class="$route.name === 'Home' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
            @click="closeSidebarOnMobile"
          >
            <svg class="mr-3 h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2H5a2 2 0 00-2-2z"/>
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 5a2 2 0 012-2h4a2 2 0 012 2v6H8V5z"/>
            </svg>
            Tổng quan
          </router-link>

          <!-- Products -->
          <router-link 
            to="/products" 
            class="group flex items-center px-3 py-2.5 text-sm font-medium rounded-lg transition-colors duration-200"
            :class="$route.name === 'Products' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
            @click="closeSidebarOnMobile"
          >
            <svg class="mr-3 h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/>
            </svg>
            Hàng hóa
          </router-link>

          <!-- Orders -->
          <router-link 
            to="/orders" 
            class="group flex items-center px-3 py-2.5 text-sm font-medium rounded-lg transition-colors duration-200"
            :class="$route.name === 'Orders' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
            @click="closeSidebarOnMobile"
          >
            <svg class="mr-3 h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
            </svg>
            Hóa đơn
          </router-link>

          <!-- Customers -->
          <router-link 
            to="/customers" 
            class="group flex items-center px-3 py-2.5 text-sm font-medium rounded-lg transition-colors duration-200"
            :class="$route.name === 'Customers' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
            @click="closeSidebarOnMobile"
          >
            <svg class="mr-3 h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
            </svg>
            Khách hàng
          </router-link>

          <!-- Vehicles -->
          <router-link 
            to="/vehicles" 
            class="group flex items-center px-3 py-2.5 text-sm font-medium rounded-lg transition-colors duration-200"
            :class="$route.name === 'Vehicles' ? 'bg-[#0070F4] text-white' : 'text-gray-700 hover:bg-gray-100'"
            @click="closeSidebarOnMobile"
          >
            <svg class="mr-3 h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0z"/>
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 6H4a2 2 0 00-2 2v6a2 2 0 002 2h1m8-10V4a2 2 0 00-2-2H4m7 4v10m0 0h6a2 2 0 002-2v-6a2 2 0 00-2-2h-6z"/>
            </svg>
            Mẫu xe
          </router-link>
        </div>

        <!-- Divider -->
        <div class="my-6 border-t border-gray-200"></div>

        <!-- Additional Menu Items -->
        <div class="space-y-2">
          <a href="#" class="group flex items-center px-3 py-2.5 text-sm font-medium text-gray-700 rounded-lg hover:bg-gray-100 transition-colors duration-200">
            <svg class="mr-3 h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"/>
            </svg>
            Báo cáo
          </a>

          <a href="#" class="group flex items-center px-3 py-2.5 text-sm font-medium text-gray-700 rounded-lg hover:bg-gray-100 transition-colors duration-200">
            <svg class="mr-3 h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"/>
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
            </svg>
            Cài đặt
          </a>
        </div>
      </nav>
    </div>

    <!-- Main Content Area -->
    <div class="flex-1 lg:ml-0">
      <!-- Top Header -->
      <header class="bg-white shadow-sm border-b border-gray-200 h-16 fixed top-0 right-0 left-0 lg:left-64 z-40">
        <div class="flex items-center justify-between h-full px-4">
          <!-- Mobile Menu Button -->
          <button 
            @click="sidebarOpen = !sidebarOpen"
            class="lg:hidden p-2 rounded-md text-gray-500 hover:text-gray-600 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-[#0070F4]"
          >
            <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/>
            </svg>
          </button>

          <!-- Page Title -->
          <div class="hidden lg:block">
            <h1 class="text-lg font-semibold text-gray-900">{{ getPageTitle() }}</h1>
          </div>

          <!-- Right Actions -->
          <div class="flex items-center space-x-4">
            <!-- Quick Actions -->
            <button class="p-2 text-gray-500 hover:text-gray-600 hover:bg-gray-100 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#0070F4]">
              <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-5 5-5-5h5v-13h0z"/>
              </svg>
            </button>

            <!-- Notifications -->
            <button class="relative p-2 text-gray-500 hover:text-gray-600 hover:bg-gray-100 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#0070F4]">
              <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-5 5-5-5h5v-13h0z"/>
              </svg>
              <span class="absolute top-0 right-0 h-2 w-2 bg-red-500 rounded-full"></span>
            </button>

            <!-- Profile Menu -->
            <div class="relative">
              <button 
                @click="showProfileMenu = !showProfileMenu"
                class="flex items-center space-x-2 p-2 text-gray-700 hover:bg-gray-100 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#0070F4]"
              >
                <div class="h-8 w-8 bg-[#0070F4] rounded-full flex items-center justify-center">
                  <span class="text-sm font-medium text-white">A</span>
                </div>
                <span class="hidden md:block text-sm font-medium">Admin</span>
                <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
                </svg>
              </button>

              <!-- Profile Dropdown -->
              <transition
                enter-active-class="transition ease-out duration-200"
                enter-from-class="transform opacity-0 scale-95"
                enter-to-class="transform opacity-100 scale-100"
                leave-active-class="transition ease-in duration-75"
                leave-from-class="transform opacity-100 scale-100"
                leave-to-class="transform opacity-0 scale-95"
              >
                <div 
                  v-show="showProfileMenu"
                  class="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg py-1 ring-1 ring-black ring-opacity-5 focus:outline-none z-50"
                >
                  <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Hồ sơ cá nhân</a>
                  <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Cài đặt</a>
                  <div class="border-t border-gray-100 my-1"></div>
                  <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Đăng xuất</a>
                </div>
              </transition>
            </div>
          </div>
        </div>
      </header>

      <!-- Main Content -->
      <main class="pt-16 min-h-screen bg-gray-50">
        <router-view />
      </main>
    </div>

    <!-- Mobile Sidebar Overlay -->
    <div 
      v-if="sidebarOpen" 
      @click="sidebarOpen = false"
      class="fixed inset-0 z-40 bg-gray-600 bg-opacity-75 lg:hidden"
    ></div>

    <!-- Click outside to close profile menu -->
    <div 
      v-if="showProfileMenu" 
      @click="showProfileMenu = false"
      class="fixed inset-0 z-30"
    ></div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRoute } from 'vue-router'

export default {
  name: 'App',
  setup() {
    const route = useRoute()
    const sidebarOpen = ref(false)
    const showProfileMenu = ref(false)

    const getPageTitle = () => {
      const titles = {
        'Home': 'Tổng quan',
        'Products': 'Quản lý hàng hóa',
        'Orders': 'Quản lý hóa đơn',
        'Customers': 'Quản lý khách hàng',
        'Vehicles': 'Quản lý mẫu xe'
      }
      return titles[route.name] || 'AutoParts'
    }

    const closeSidebarOnMobile = () => {
      if (window.innerWidth < 1024) {
        sidebarOpen.value = false
      }
    }

    return {
      sidebarOpen,
      showProfileMenu,
      getPageTitle,
      closeSidebarOnMobile
    }
  }
}
</script>

<style>
/* Global styles sẽ được load từ main.css */
</style>
