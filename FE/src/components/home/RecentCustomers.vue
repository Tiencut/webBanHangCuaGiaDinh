<template>
  <div class="bg-white rounded-xl shadow-lg border border-gray-100 overflow-hidden">
    <!-- Header -->
    <div class="flex items-center justify-between px-6 py-5 border-b border-gray-100">
      <h2 class="text-lg font-semibold text-gray-900 tracking-tight">Khách hàng gần đây</h2>
      <router-link 
        to="/customers" 
        class="text-sm text-blue-600 hover:text-blue-800 font-medium"
      >
        Xem tất cả
      </router-link>
    </div>

    <!-- Content (Empty State) -->
    <div class="p-10 flex flex-col items-center justify-center bg-gray-50">
      <svg class="h-10 w-10 text-blue-200 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
          d="M17 20h5v-2a4 4 0 00-5-4H6a4 4 0 00-5 4v2h5" />
        <circle cx="12" cy="7" r="4" stroke="currentColor" stroke-width="2" fill="none"></circle>
      </svg>
      <p class="mt-2 text-base text-gray-500">Chưa có khách hàng nào.</p>
    </div>
  </div>
</template>

<script>
import { defineComponent } from 'vue';
export default defineComponent({
  name: 'RecentCustomers',
});
</script>
