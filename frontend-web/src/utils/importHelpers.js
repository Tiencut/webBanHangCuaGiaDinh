/**
 * Import utilities for handling common import operations
 */

/**
 * Parse and format import error response
 * @param {Error} error - The error object from API call
 * @returns {Object} Formatted error result
 */
export function parseImportError(error) {
  let errorMessage = 'Có lỗi xảy ra khi import dữ liệu';
  let errorDetails = [];
  
  if (error.response) {
    // Lỗi từ server (4xx, 5xx)
    const status = error.response.status;
    const data = error.response.data;
    
    switch (status) {
      case 400:
        errorMessage = 'Dữ liệu không hợp lệ hoặc file có lỗi định dạng';
        break;
      case 401:
        errorMessage = 'Không có quyền truy cập. Vui lòng đăng nhập lại';
        break;
      case 403:
        errorMessage = 'Không có quyền thực hiện import';
        break;
      case 404:
        errorMessage = 'Không tìm thấy API endpoint';
        break;
      case 413:
        errorMessage = 'File quá lớn. Vui lòng chọn file nhỏ hơn';
        break;
      case 415:
        errorMessage = 'Định dạng file không được hỗ trợ';
        break;
      case 422:
        errorMessage = 'Dữ liệu trong file không hợp lệ';
        break;
      case 500:
        errorMessage = 'Lỗi server nội bộ. Vui lòng thử lại sau';
        break;
      default:
        errorMessage = data?.message || `Lỗi HTTP ${status}`;
    }
    
    // Lấy chi tiết lỗi từ response
    if (data?.errors) {
      errorDetails = Array.isArray(data.errors) ? data.errors : [data.errors];
    } else if (data?.details) {
      errorDetails = Array.isArray(data.details) ? data.details : [data.details];
    } else if (data?.validationErrors) {
      errorDetails = Array.isArray(data.validationErrors) ? data.validationErrors : [data.validationErrors];
    }
    
  } else if (error.request) {
    // Lỗi network
    errorMessage = 'Không thể kết nối đến server. Kiểm tra kết nối mạng';
  } else {
    // Lỗi khác
    errorMessage = error.message || 'Lỗi không xác định';
  }
  
  return {
    success: false,
    message: errorMessage,
    errors: errorDetails.map((detail, index) => ({
      row: detail.row || detail.line || detail.rowNumber || (index + 1),
      message: detail.message || detail.error || detail
    }))
  };
}

/**
 * Validate file before upload
 * @param {File} file - File to validate
 * @param {Object} options - Validation options
 * @returns {Object} Validation result
 */
export function validateImportFile(file, options = {}) {
  const {
    allowedTypes = ['.csv', '.xlsx', '.xls'],
    maxSize = 10 * 1024 * 1024, // 10MB
    maxSizeMB = 10
  } = options;
  
  if (!file) {
    return {
      valid: false,
      message: 'Vui lòng chọn file để import'
    };
  }
  
  // Validate file type
  const fileExtension = '.' + file.name.split('.').pop().toLowerCase();
  if (!allowedTypes.includes(fileExtension)) {
    return {
      valid: false,
      message: `Định dạng file không hỗ trợ. Chỉ chấp nhận: ${allowedTypes.join(', ')}`
    };
  }
  
  // Validate file size
  if (file.size > maxSize) {
    return {
      valid: false,
      message: `File quá lớn! Kích thước tối đa ${maxSizeMB}MB`
    };
  }
  
  return {
    valid: true,
    message: 'File hợp lệ'
  };
}

/**
 * Format file size to human readable string
 * @param {number} bytes - File size in bytes
 * @returns {string} Formatted file size
 */
export function formatFileSize(bytes) {
  if (bytes === 0) return '0 Bytes';
  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

/**
 * Create FormData for import request
 * @param {File} file - File to import
 * @param {Object} options - Additional options
 * @returns {FormData} FormData object ready for API call
 */
export function createImportFormData(file, options = {}) {
  const formData = new FormData();
  formData.append('file', file);
  
  // Add additional options
  Object.keys(options).forEach(key => {
    formData.append(key, options[key]);
  });
  
  return formData;
}

/**
 * Download blob as file
 * @param {Blob} blob - Blob data
 * @param {string} filename - Filename to download
 */
export function downloadBlob(blob, filename) {
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  window.URL.revokeObjectURL(url);
}

/**
 * Import configurations for different data types
 */
export const importConfigs = {
  suppliers: {
    title: 'Import nhà cung cấp từ CSV',
    uploadMessage: 'Chọn file CSV nhà cung cấp',
    updateOptionText: 'Cập nhật nhà cung cấp đã tồn tại',
    templateFilename: 'template_nha_cung_cap.csv',
    apiEndpoint: '/suppliers/import',
    templateEndpoint: '/suppliers/template'
  },
  
  products: {
    title: 'Import sản phẩm từ CSV/Excel',
    uploadMessage: 'Chọn file CSV hoặc Excel sản phẩm',
    updateOptionText: 'Cập nhật sản phẩm đã tồn tại',
    templateFilename: 'template_san_pham.csv',
    acceptedFileTypes: '.csv,.xlsx,.xls',
    apiEndpoint: '/products/import',
    templateEndpoint: '/products/template'
  },
  
  customers: {
    title: 'Import khách hàng từ CSV',
    uploadMessage: 'Chọn file CSV khách hàng',
    updateOptionText: 'Cập nhật khách hàng đã tồn tại',
    templateFilename: 'template_khach_hang.csv',
    apiEndpoint: '/customers/import',
    templateEndpoint: '/customers/template'
  },
  
  orders: {
    title: 'Import đơn hàng từ CSV',
    uploadMessage: 'Chọn file CSV đơn hàng',
    updateOptionText: 'Cập nhật đơn hàng đã tồn tại',
    templateFilename: 'template_don_hang.csv',
    apiEndpoint: '/orders/import',
    templateEndpoint: '/orders/template'
  }
};

/**
 * Get import configuration by type
 * @param {string} type - Import type (suppliers, products, customers, orders)
 * @returns {Object} Import configuration
 */
export function getImportConfig(type) {
  return importConfigs[type] || importConfigs.suppliers;
}
