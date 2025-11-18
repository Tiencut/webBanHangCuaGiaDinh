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

          <NavigationMenu />

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


      </div>
    </div>
    <!-- Main Content Area -->
    <div class="pt-16">
      <div class="w-full px-2 sm:px-4 py-4">
        <router-view />
      </div>
    </div>

    <MobileMenu :isOpen="isMobileMenuOpen" @close="isMobileMenuOpen = false" />
    <!-- Backdrop for Mobile Menu -->
    <div v-if="sidebarOpen" @click="sidebarOpen = false" class="fixed inset-0 bg-black bg-opacity-50 z-40 md:hidden">
    </div>
  </div>
  
  <!-- Global Components -->
  <Toast />
  <ConfirmDialog 
    :show="showConfirmDialog"
    :title="confirmDialog.title"
    :message="confirmDialog.message"
    :type="confirmDialog.type"
    :confirm-text="confirmDialog.confirmText"
    :cancel-text="confirmDialog.cancelText"
    @confirm="handleConfirm"
    @cancel="handleCancel"
  />
</template>

<script>
import { ref, onMounted, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import Toast from '@/components/Toast.vue'
import ConfirmDialog from '@/components/ConfirmDialog.vue'
import MobileMenu from './components/layout/MobileMenu.vue'
import NavigationMenu from './components/layout/NavigationMenu.vue'

export default {
  name: 'App',
  components: {
    Toast,
    ConfirmDialog,
    MobileMenu,
    NavigationMenu
  },
  setup() {
    const route = useRoute()
    const isMobileMenuOpen = ref(false)
    const isProductsDropdownOpen = ref(false)
    const isOrdersDropdownOpen = ref(false)
    const isPurchaseDropdownOpen = ref(false)

    const showConfirmDialog = ref(false)
    const confirmDialog = ref({
      title: '',
      message: '',
      type: 'warning',
      confirmText: 'Xác nhận',
      cancelText: 'Hủy',
      onConfirm: null,
      onCancel: null
    })

    const showConfirm = (options) => {
      return new Promise((resolve) => {
        confirmDialog.value = {
          title: options.title || 'Xác nhận',
          message: options.message || 'Bạn có chắc chắn muốn thực hiện hành động này?',
          type: options.type || 'warning',
          confirmText: options.confirmText || 'Xác nhận',
          cancelText: options.cancelText || 'Hủy',
          onConfirm: () => resolve(true),
          onCancel: () => resolve(false)
        }
        showConfirmDialog.value = true
      })
    }

    const handleConfirm = () => {
      showConfirmDialog.value = false
      if (confirmDialog.value.onConfirm) {
        confirmDialog.value.onConfirm()
      }
    }

    const handleCancel = () => {
      showConfirmDialog.value = false
      if (confirmDialog.value.onCancel) {
        confirmDialog.value.onCancel()
      }
    }

    onMounted(() => {
      window.$confirm = showConfirm
    })

    const sidebarOpen = ref(false)

    onMounted(() => {
      window.addEventListener('resize', () => {
        if (window.innerWidth >= 1024) {
          sidebarOpen.value = false
        }
      })

      nextTick(() => {
        const dropdownItems = document.querySelectorAll('.dropdown-menu a[href^="/"]')
        dropdownItems.forEach((item) => {
          item.addEventListener('click', (e) => {
            e.stopPropagation()
          })
        })
      })
    })

    return {
      route,
      sidebarOpen,
      isMobileMenuOpen,
      isProductsDropdownOpen,
      isOrdersDropdownOpen,
      isPurchaseDropdownOpen,
      showConfirmDialog,
      confirmDialog,
      handleConfirm,
      handleCancel
    }
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
