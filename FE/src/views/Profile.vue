<template>
  <div class="max-w-3xl mx-auto py-6">
    <div class="card">
      <h2 class="text-xl font-semibold mb-4">Hồ sơ cá nhân</h2>

      <div v-if="loading" class="text-sm text-gray-500">Đang tải...</div>

      <div v-else>
        <div class="space-y-3">
          <div>
            <div class="text-sm text-gray-600">Tên</div>
            <div class="font-medium">{{ user?.name || user?.username || '—' }}</div>
          </div>

          <div>
            <div class="text-sm text-gray-600">Vai trò</div>
            <div class="font-medium">{{ user?.role || '—' }}</div>
          </div>

          <div>
            <div class="text-sm text-gray-600">Email</div>
            <div class="font-medium">{{ user?.email || '—' }}</div>
          </div>

          <div>
            <div class="text-sm text-gray-600">Số điện thoại</div>
            <div class="font-medium">{{ user?.phone || '—' }}</div>
          </div>
        </div>

        <div class="mt-6 flex space-x-3">
          <button @click="refresh" class="btn-secondary">Làm mới</button>
          <router-link to="/change-password" class="btn-primary">Đổi mật khẩu</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'

export default {
  name: 'ProfileView',
  setup() {
    const auth = useAuthStore()
    const loading = ref(true)

    const load = async () => {
      loading.value = true
      try {
        if (!auth.user) {
          await auth.getCurrentUser()
        }
      } catch (e) {
        console.error('Error loading user', e)
      } finally {
        loading.value = false
      }
    }

    onMounted(load)

    return {
      user: auth.user,
      loading,
      refresh: load
    }
  }
}
</script>

<style scoped>
.card { padding: 20px; }
</style>
