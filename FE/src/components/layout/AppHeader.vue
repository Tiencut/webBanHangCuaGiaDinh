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
            <div class="relative" ref="profileWrap" :class="{ 'ring-2 ring-green-300 rounded-md': showProfileMenu }">
              <button
                type="button"
                @click.stop="toggleProfileMenu"
                :aria-expanded="showProfileMenu"
                class="flex items-center space-x-2 text-gray-700 hover:text-gray-900 focus:outline-none focus:ring-2 focus:ring-[#0070F4] rounded-lg p-2">
                <div class="h-8 w-8 bg-[#0070F4] rounded-full flex items-center justify-center">
                  <span class="text-white text-sm font-medium">A</span>
                </div>
                <span class="hidden lg:block text-sm font-medium">Admin</span>
                <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
                </svg>
              </button>

              <!-- Profile Dropdown -->
              <transition name="fade">
                <div v-if="showProfileMenu" class="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg py-1 ring-1 ring-black ring-opacity-5 focus:outline-none z-50">
                  <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Hồ sơ cá nhân</a>
                  <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Cài đặt</a>
                  <div class="border-t border-gray-100 my-1"></div>
                  <button @click="handleLogout" class="w-full text-left block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Đăng xuất</button>
                </div>
              </transition>
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
import { useAuthStore } from '@/stores/auth'

export default {
  name: 'AppHeader',
  components: {
    AppLogo,
    NavigationMenu,
    MobileMenu
  },
  data() {
    return {
      mobileMenuOpen: false,
      showProfileMenu: false
    }
  },
  methods: {
    toggleMobileMenu() {
      this.mobileMenuOpen = !this.mobileMenuOpen
    },
    closeMobileMenu() {
      this.mobileMenuOpen = false
    },
    toggleProfileMenu() {
      this.showProfileMenu = !this.showProfileMenu
      // debug logs to verify click handler runs (helpful if console filters hide logs)
      // eslint-disable-next-line no-console
      console.log('AppHeader.toggleProfileMenu ->', this.showProfileMenu)
      // eslint-disable-next-line no-console
      console.warn('AppHeader: profile button clicked', { open: this.showProfileMenu })
      // eslint-disable-next-line no-console
      console.trace('AppHeader.toggleProfileMenu trace')
      try {
        if (window && window.console) {
          // extra info to help when DevTools swallowing logs
          window.console.info('profile click registered at', new Date().toISOString())
        }
      } catch (e) {
        // ignore
      }
    },
    async handleLogout() {
      try {
        const auth = useAuthStore()
        await auth.logout()
        this.showProfileMenu = false
        this.$router.push('/login')
      } catch (err) {
        console.error('Logout failed', err)
      }
    }
  },
    mounted() {
      // Close mobile menu on window resize
      window.addEventListener('resize', () => {
        if (window.innerWidth >= 1024) {
          this.mobileMenuOpen = false
        }
      })

      // Better outside-click handling: use the wrapper ref so we only close when clicking outside the profile area
      const onClickOutside = (e) => {
        const wrap = this.$refs.profileWrap
        if (!wrap) return
        if (!wrap.contains(e.target)) {
          this.showProfileMenu = false
        }
      }

      window.addEventListener('click', onClickOutside)

      // store listener so it can be removed if component destroyed
      this._onClickOutside = onClickOutside
    },
    unmounted() {
      if (this._onClickOutside) {
        window.removeEventListener('click', this._onClickOutside)
      }
    }
}
</script>
