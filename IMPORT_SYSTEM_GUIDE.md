# Hệ thống Import CSV/Excel Tái Sử Dụng

## Tổng quan

Hệ thống import đã được chuẩn hóa để có thể tái sử dụng cho nhiều loại dữ liệu khác nhau (nhà cung cấp, sản phẩm, khách hàng, đơn hàng...) thông qua:

1. **ImportModal Component** - Component UI dùng chung
2. **useImport Composable** - Logic xử lý import
3. **importHelpers Utilities** - Các hàm tiện ích
4. **Import Configurations** - Cấu hình cho từng loại import

## Cấu trúc File

```
frontend-web/src/
├── components/
│   └── ImportModal.vue           # Component modal dùng chung
├── composables/
│   └── useImport.js             # Composable xử lý logic import
├── utils/
│   └── importHelpers.js         # Utilities và cấu hình
└── views/
    ├── Suppliers.vue            # Ví dụ sử dụng cho nhà cung cấp
    └── Products_ImportExample.vue # Ví dụ sử dụng cho sản phẩm
```

## Cách sử dụng

### 1. Trong Vue Component

```vue
<template>
  <div>
    <!-- Nút mở modal import -->
    <button @click="showImportModal = true">Import CSV</button>
    
    <!-- Import Modal -->
    <ImportModal 
      :show="showImportModal"
      :title="importConfig.title"
      :upload-message="importConfig.uploadMessage"
      :update-option-text="importConfig.updateOptionText"
      :accepted-file-types="importConfig.acceptedFileTypes"
      :status="importState.status"
      :results="importState.results"
      @close="showImportModal = false"
      @import="handleImport"
      @download-template="handleDownloadTemplate"
      @show-guide="showImportGuide = true"
    />
  </div>
</template>

<script>
import ImportModal from '@/components/ImportModal.vue'
import { useImport } from '@/composables/useImport'

export default {
  components: { ImportModal },
  setup() {
    // Chỉ cần thay đổi 'suppliers' thành loại khác: 'products', 'customers', 'orders'
    const { 
      importState, 
      config: importConfig, 
      executeImport, 
      downloadTemplate,
      setFile,
      resetImport
    } = useImport('suppliers');
    
    return {
      importState,
      importConfig,
      executeImport,
      downloadTemplate,
      setFile,
      resetImport
    };
  },
  data() {
    return {
      showImportModal: false,
      showImportGuide: false
    }
  },
  methods: {
    async handleImport(importData) {
      this.setFile(importData.file);
      this.importState.updateExisting = importData.updateExisting;
      
      const result = await this.executeImport();
      
      if (result.success) {
        // Refresh danh sách sau khi import thành công
        await this.refreshData();
      }
    },
    
    async handleDownloadTemplate() {
      const result = await this.downloadTemplate();
      if (!result.success) {
        alert(result.message);
      }
    },
    
    async refreshData() {
      // Implement logic refresh dữ liệu
    }
  }
}
</script>
```

### 2. Thêm loại import mới

Để thêm loại import mới (ví dụ: 'vehicles'), chỉ cần cập nhật `importHelpers.js`:

```javascript
// frontend-web/src/utils/importHelpers.js

export const importConfigs = {
  // ...existing configs...
  
  vehicles: {
    title: 'Import xe/dòng xe từ CSV',
    uploadMessage: 'Chọn file CSV danh sách xe',
    updateOptionText: 'Cập nhật thông tin xe đã tồn tại',
    templateFilename: 'template_xe.csv',
    apiEndpoint: '/vehicles/import',
    templateEndpoint: '/vehicles/template'
  }
};
```

Sau đó sử dụng: `useImport('vehicles')`

### 3. Backend API Requirements

Backend cần implement 2 endpoints cho mỗi loại import:

1. **POST `/api/{type}/import`** - Xử lý import file
   ```
   Request: multipart/form-data
   - file: File CSV/Excel
   - updateExisting: boolean
   
   Response: {
     success: boolean,
     message?: string,
     summary?: {
       totalRows: number,
       successCount: number,
       errorCount: number,
       duplicateCount: number,
       successRate: number,
       processingTime: number
     },
     errors?: Array<{
       row: number,
       message: string
     }>
   }
   ```

2. **GET `/api/{type}/template`** - Download file template
   ```
   Response: CSV file blob
   ```

## Tính năng chính

### ✅ Đã hoàn thành:

1. **Component tái sử dụng**: ImportModal có thể dùng cho mọi loại import
2. **Logic tái sử dụng**: useImport composable xử lý tất cả logic import
3. **Cấu hình linh hoạt**: Dễ dàng thêm loại import mới
4. **Xử lý lỗi thống nhất**: Hiển thị lỗi theo format chuẩn
5. **Upload file đa định dạng**: Hỗ trợ CSV, Excel (.xlsx, .xls)
6. **Progress tracking**: Hiển thị trạng thái import
7. **Template download**: Tải file mẫu cho từng loại
8. **Validation**: Kiểm tra file trước khi upload
9. **Error details**: Hiển thị chi tiết lỗi theo từng dòng
10. **Success summary**: Thống kê kết quả import

### 📋 Cải tiến có thể thêm:

1. **Drag & Drop**: Kéo thả file vào modal
2. **File preview**: Xem trước nội dung file
3. **Multi-sheet support**: Hỗ trợ nhiều sheet Excel
4. **Batch import**: Import nhiều file cùng lúc
5. **Import history**: Lịch sử các lần import
6. **Auto-retry**: Tự động retry khi lỗi network
7. **Import validation**: Validate dữ liệu trước khi gửi server

## Lợi ích

1. **DRY (Don't Repeat Yourself)**: Không cần viết lại code import cho mỗi màn hình
2. **Consistent UX**: Trải nghiệm người dùng nhất quán
3. **Maintainability**: Dễ bảo trì và cập nhật
4. **Scalability**: Dễ dàng mở rộng cho loại dữ liệu mới
5. **Error handling**: Xử lý lỗi thống nhất và user-friendly
6. **Performance**: Tối ưu performance với composable pattern

## Ví dụ sử dụng

Xem các file:
- `Suppliers.vue` - Import nhà cung cấp (đã cập nhật)
- `Products_ImportExample.vue` - Import sản phẩm (ví dụ)

Chỉ cần thay đổi tham số trong `useImport()` và component sẽ tự động sử dụng đúng cấu hình cho loại dữ liệu đó.
