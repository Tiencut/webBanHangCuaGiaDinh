<template>
  <div class="container mx-auto px-4 py-6">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-6">
      <div>
        <h1 class="text-3xl font-bold text-gray-800">Trả hàng</h1>
        <p class="text-gray-600 mt-1">Quản lý đơn hàng trả về</p>
      </div>
      
      <div class="flex items-center space-x-4">
        <button class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors">
          Tạo đơn trả hàng
        </button>
      </div>
    </div>

    <!-- Filters -->
    <div class="bg-white rounded-lg shadow-md p-4 mb-6">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
          <input type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" placeholder="Mã đơn hàng, tên khách hàng...">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
          <select class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
            <option value="">Tất cả</option>
            <option value="pending">Chờ xử lý</option>
            <option value="approved">Đã duyệt</option>
            <option value="rejected">Từ chối</option>
            <option value="completed">Hoàn thành</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Từ ngày</label>
          <input type="date" class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Đến ngày</label>
          <input type="date" class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
        </div>
      </div>
    </div>

    <!-- Returns List -->
    <div class="bg-white rounded-lg shadow-md overflow-hidden">
      <table class="w-full">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Mã đơn trả</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Đơn hàng gốc</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Khách hàng</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Ngày trả</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Lý do</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Giá trị</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Trạng thái</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Thao tác</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <tr>
            <td class="px-6 py-4 text-sm font-medium text-gray-900">RT001</td>
            <td class="px-6 py-4 text-sm text-blue-600 hover:text-blue-800 cursor-pointer">DH001</td>
            <td class="px-6 py-4 text-sm text-gray-900">Nguyễn Văn A</td>
            <td class="px-6 py-4 text-sm text-gray-900">15/12/2024</td>
            <td class="px-6 py-4 text-sm text-gray-900">Sản phẩm lỗi</td>
            <td class="px-6 py-4 text-sm text-gray-900">1,200,000 VND</td>
            <td class="px-6 py-4">
              <span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-yellow-100 text-yellow-800">
                Chờ xử lý
              </span>
            </td>
            <td class="px-6 py-4 text-sm">
              <div class="flex space-x-2">
                <button class="text-blue-500 hover:text-blue-700">Xem</button>
                <button class="text-green-500 hover:text-green-700">Duyệt</button>
                <button class="text-red-500 hover:text-red-700">Từ chối</button>
              </div>
            </td>
          </tr>
          <tr>
            <td class="px-6 py-4 text-sm font-medium text-gray-900">RT002</td>
            <td class="px-6 py-4 text-sm text-blue-600 hover:text-blue-800 cursor-pointer">DH005</td>
            <td class="px-6 py-4 text-sm text-gray-900">Trần Thị B</td>
            <td class="px-6 py-4 text-sm text-gray-900">14/12/2024</td>
            <td class="px-6 py-4 text-sm text-gray-900">Không đúng mô tả</td>
            <td class="px-6 py-4 text-sm text-gray-900">850,000 VND</td>
            <td class="px-6 py-4">
              <span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-green-100 text-green-800">
                Đã duyệt
              </span>
            </td>
            <td class="px-6 py-4 text-sm">
              <div class="flex space-x-2">
                <button class="text-blue-500 hover:text-blue-700">Xem</button>
                <button class="text-gray-400 cursor-not-allowed">Đã xử lý</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="mt-6 flex justify-between items-center">
      <div class="text-sm text-gray-700">
        Hiển thị 1-2 trong 2 kết quả
      </div>
      <div class="flex space-x-2">
        <button class="px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 cursor-not-allowed">Trước</button>
        <button class="px-3 py-1 bg-blue-500 text-white rounded-lg text-sm">1</button>
        <button class="px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 cursor-not-allowed">Sau</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'OrderReturns',
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
