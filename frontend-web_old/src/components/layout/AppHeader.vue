<template>
  <div class="bg-white shadow-lg border-b border-gray-200 fixed top-0 left-0 right-0 z-50">
    <div class="w-full px-2 sm:px-4">
      <div class="flex items-center justify-between h-16">
        <!-- Logo -->
        <AppLogo />

        <!-- Navigation Menu (Desktop) -->
        <NavigationMenu />

        <!-- Right Actions -->
        <div class="flex items-center space-x-4">
          <!-- Mobile Menu Button -->
          <button @click="toggleMobileMenu"
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

      <!-- Mobile Menu -->
      <MobileMenu :is-open="mobileMenuOpen" @close="closeMobileMenu" />
    </div>
  </div>
</template>

<script>
import AppLogo from './AppLogo.vue'
import NavigationMenu from './NavigationMenu.vue'
import MobileMenu from './MobileMenu.vue'

export default {
  name: 'AppHeader',
  components: {
    AppLogo,
    NavigationMenu,
    MobileMenu
  },
  data() {
    return {
      mobileMenuOpen: false
    }
  },
  methods: {
    toggleMobileMenu() {
      this.mobileMenuOpen = !this.mobileMenuOpen
    },
    closeMobileMenu() {
      this.mobileMenuOpen = false
    }
  },
  mounted() {
    // Close mobile menu on window resize
    window.addEventListener('resize', () => {
      if (window.innerWidth >= 1024) {
        this.mobileMenuOpen = false
      }
    })
  }
}
</script>
