<template>
  <div class="bg-white rounded-xl shadow-lg p-8 border border-gray-100 mb-10">
    <h2 class="text-lg font-bold text-gray-900 mb-6">Thao tác nhanh</h2>
    <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
      <!-- Thêm sản phẩm -->
      <router-link
        to="/products"
        class="flex flex-col items-center justify-center gap-2 bg-gradient-to-br from-green-50 via-white to-green-100 p-5 rounded-xl border border-green-100 hover:shadow-lg transition duration-200"
      >
        <div class="h-14 w-14 bg-green-200 rounded-full flex items-center justify-center mb-2">
          <svg class="h-7 w-7 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
          </svg>
        </div>
        <span class="text-base font-semibold text-gray-900">Thêm sản phẩm</span>
      </router-link>

      <!-- Thêm khách hàng -->
      <router-link
        to="/customers"
        class="flex flex-col items-center justify-center gap-2 bg-gradient-to-br from-purple-50 via-white to-purple-100 p-5 rounded-xl border border-purple-100 hover:shadow-lg transition duration-200"
      >
        <div class="h-14 w-14 bg-purple-200 rounded-full flex items-center justify-center mb-2">
          <svg class="h-7 w-7 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
          </svg>
        </div>
        <span class="text-base font-semibold text-gray-900">Thêm khách hàng</span>
      </router-link>

      <!-- Quản lý xe -->
      <router-link
        to="/vehicles"
        class="flex flex-col items-center justify-center gap-2 bg-gradient-to-br from-orange-50 via-white to-orange-100 p-5 rounded-xl border border-orange-100 hover:shadow-lg transition duration-200"
      >
        <div class="h-14 w-14 bg-orange-200 rounded-full flex items-center justify-center mb-2">
          <svg class="h-7 w-7 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0z" />
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M13 6H4a2 2 0 00-2 2v6a2 2 0 002 2h1m8-10V4a2 2 0 00-2-2H4m7 4v10m0 0h6a2 2 0 002-2v-6a2 2 0 00-2-2h-6z" />
          </svg>
        </div>
        <span class="text-base font-semibold text-gray-900">Quản lý xe</span>
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'QuickActions',
}
</script>
