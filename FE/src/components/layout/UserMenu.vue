<template>
  <div class="relative" ref="wrap" :class="{ 'ring-2 ring-green-300 rounded-md': open }">
    <button type="button" @click.stop="toggle" :aria-expanded="open" class="flex items-center space-x-2 text-gray-700 hover:text-gray-900 focus:outline-none focus:ring-2 focus:ring-[#0070F4] rounded-lg p-2">
      <div class="h-8 w-8 bg-[#0070F4] rounded-full flex items-center justify-center">
        <span class="text-white text-sm font-medium">{{ initials }}</span>
      </div>
      <span class="hidden lg:block text-sm font-medium">{{ displayName }}</span>
      <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
      </svg>
    </button>

    <transition name="fade">
      <div v-if="open" class="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg py-1 ring-1 ring-black ring-opacity-5 focus:outline-none z-50">
        <router-link @click="close" to="/profile" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Hồ sơ cá nhân</router-link>
        <router-link @click="close" to="/settings" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Cài đặt</router-link>
        <div class="border-t border-gray-100 my-1"></div>
        <button @click="onLogout" class="w-full text-left block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Đăng xuất</button>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const open = ref(false)
const wrap = ref(null)
const router = useRouter()
const auth = useAuthStore()

const toggle = () => { open.value = !open.value }
const close = () => { open.value = false }

const onClickOutside = (e) => {
  const el = wrap.value
  if (!el) return
  if (!el.contains(e.target)) {
    open.value = false
  }
}

onMounted(() => window.addEventListener('click', onClickOutside))
onUnmounted(() => window.removeEventListener('click', onClickOutside))

const onLogout = async () => {
  try {
    await auth.logout()
  } catch (e) {
    console.error('Logout failed', e)
  } finally {
    close()
    router.push('/login')
  }
}

const displayName = computed(() => auth.user?.name || auth.user?.username || 'Admin')
const initials = computed(() => {
  const n = auth.user?.name || auth.user?.username || 'A'
  return n.split(' ').map(s => s?.[0]).filter(Boolean).slice(0,2).join('')
})
</script>

<style scoped>
.ring-2 { box-shadow: 0 0 0 2px rgba(52,211,153,0.25); }
</style>
