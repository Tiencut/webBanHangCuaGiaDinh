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

          <!-- User Menu -->
          <UserMenu />
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
import UserMenu from './UserMenu.vue'

export default {
  name: 'AppHeader',
  components: {
    AppLogo,
    NavigationMenu,
    MobileMenu,
    UserMenu
  },
  data() {
    return {
      mobileMenuOpen: false,
      // profile menu moved to UserMenu component
    }
  },
  methods: {
    toggleMobileMenu() {
      this.mobileMenuOpen = !this.mobileMenuOpen
    },
    closeMobileMenu() {
      this.mobileMenuOpen = false
    },
    // profile/menu logic handled inside UserMenu component
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
