<template>
  <div class="container mx-auto px-4 py-6">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-6">
      <div>
        <h1 class="text-3xl font-bold text-gray-800">Đối tác giao hàng</h1>
        <p class="text-gray-600 mt-1">Quản lý các đối tác vận chuyển và giao hàng</p>
      </div>
      
      <div class="flex items-center space-x-4">
        <button class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors">
          Thêm đối tác
        </button>
      </div>
    </div>

    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-6">
      <div class="bg-white rounded-lg shadow-md p-6">
        <div class="flex items-center">
          <div class="p-3 bg-blue-100 rounded-full">
            <svg class="h-6 w-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4"></path>
            </svg>
          </div>
          <div class="ml-4">
            <p class="text-sm font-medium text-gray-600">Tổng đối tác</p>
            <p class="text-2xl font-bold text-gray-900">8</p>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow-md p-6">
        <div class="flex items-center">
          <div class="p-3 bg-green-100 rounded-full">
            <svg class="h-6 w-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
            </svg>
          </div>
          <div class="ml-4">
            <p class="text-sm font-medium text-gray-600">Đang hoạt động</p>
            <p class="text-2xl font-bold text-gray-900">6</p>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow-md p-6">
        <div class="flex items-center">
          <div class="p-3 bg-yellow-100 rounded-full">
            <svg class="h-6 w-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
            </svg>
          </div>
          <div class="ml-4">
            <p class="text-sm font-medium text-gray-600">Đơn hàng hôm nay</p>
            <p class="text-2xl font-bold text-gray-900">24</p>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow-md p-6">
        <div class="flex items-center">
          <div class="p-3 bg-purple-100 rounded-full">
            <svg class="h-6 w-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"></path>
            </svg>
          </div>
          <div class="ml-4">
            <p class="text-sm font-medium text-gray-600">Chi phí tháng này</p>
            <p class="text-2xl font-bold text-gray-900">15.2M</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Filters -->
    <div class="bg-white rounded-lg shadow-md p-4 mb-6">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
          <input type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" placeholder="Tên đối tác, số điện thoại...">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
          <select class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
            <option value="">Tất cả</option>
            <option value="active">Đang hoạt động</option>
            <option value="inactive">Tạm dừng</option>
            <option value="suspended">Đình chỉ</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Loại dịch vụ</label>
          <select class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
            <option value="">Tất cả</option>
            <option value="motorbike">Xe máy</option>
            <option value="car">Ô tô</option>
            <option value="truck">Xe tải</option>
          </select>
        </div>
      </div>
    </div>

    <!-- Partners List -->
    <div class="bg-white rounded-lg shadow-md overflow-hidden">
      <table class="w-full">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Đối tác</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Liên hệ</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Dịch vụ</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Đánh giá</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Đơn hoàn thành</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Trạng thái</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Thao tác</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <tr>
            <td class="px-6 py-4">
              <div class="flex items-center">
                <div class="h-10 w-10 bg-blue-100 rounded-full flex items-center justify-center">
                  <span class="text-blue-600 font-medium">GH</span>
                </div>
                <div class="ml-4">
                  <div class="text-sm font-medium text-gray-900">Giao Hàng Nhanh</div>
                  <div class="text-sm text-gray-500">Mã: GHN001</div>
                </div>
              </div>
            </td>
            <td class="px-6 py-4 text-sm text-gray-900">
              <div>0901234567</div>
              <div class="text-gray-500">contact@ghn.vn</div>
            </td>
            <td class="px-6 py-4">
              <span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-green-100 text-green-800">
                Xe máy, Ô tô
              </span>
            </td>
            <td class="px-6 py-4 text-sm">
              <div class="flex items-center">
                <span class="text-yellow-400">★★★★☆</span>
                <span class="ml-1 text-gray-600">4.2</span>
              </div>
            </td>
            <td class="px-6 py-4 text-sm text-gray-900">1,245</td>
            <td class="px-6 py-4">
              <span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-green-100 text-green-800">
                Hoạt động
              </span>
            </td>
            <td class="px-6 py-4 text-sm">
              <div class="flex space-x-2">
                <button class="text-blue-500 hover:text-blue-700">Xem</button>
                <button class="text-green-500 hover:text-green-700">Sửa</button>
                <button class="text-red-500 hover:text-red-700">Tạm dừng</button>
              </div>
            </td>
          </tr>
          <tr>
            <td class="px-6 py-4">
              <div class="flex items-center">
                <div class="h-10 w-10 bg-red-100 rounded-full flex items-center justify-center">
                  <span class="text-red-600 font-medium">VT</span>
                </div>
                <div class="ml-4">
                  <div class="text-sm font-medium text-gray-900">Viettel Post</div>
                  <div class="text-sm text-gray-500">Mã: VTP002</div>
                </div>
              </div>
            </td>
            <td class="px-6 py-4 text-sm text-gray-900">
              <div>0987654321</div>
              <div class="text-gray-500">support@viettelpost.vn</div>
            </td>
            <td class="px-6 py-4">
              <span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-blue-100 text-blue-800">
                Xe tải
              </span>
            </td>
            <td class="px-6 py-4 text-sm">
              <div class="flex items-center">
                <span class="text-yellow-400">★★★★★</span>
                <span class="ml-1 text-gray-600">4.8</span>
              </div>
            </td>
            <td class="px-6 py-4 text-sm text-gray-900">987</td>
            <td class="px-6 py-4">
              <span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-green-100 text-green-800">
                Hoạt động
              </span>
            </td>
            <td class="px-6 py-4 text-sm">
              <div class="flex space-x-2">
                <button class="text-blue-500 hover:text-blue-700">Xem</button>
                <button class="text-green-500 hover:text-green-700">Sửa</button>
                <button class="text-red-500 hover:text-red-700">Tạm dừng</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="mt-6 flex justify-between items-center">
      <div class="text-sm text-gray-700">
        Hiển thị 1-2 trong 8 kết quả
      </div>
      <div class="flex space-x-2">
        <button class="px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 cursor-not-allowed">Trước</button>
        <button class="px-3 py-1 bg-blue-500 text-white rounded-lg text-sm">1</button>
        <button class="px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-700 hover:bg-gray-100">2</button>
        <button class="px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-700 hover:bg-gray-100">Sau</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DeliveryPartners',
  data() {
    return {
      // Component data
    }
  },
  methods: {
    // Component methods
  }
}
</script>
