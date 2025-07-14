// Format currency in VND
export const formatCurrency = (amount) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(amount)
}

// Format number with thousands separator
export const formatNumber = (number) => {
  return new Intl.NumberFormat('vi-VN').format(number)
}

// Format date
export const formatDate = (date, options = {}) => {
  const defaultOptions = {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    ...options
  }
  
  return new Intl.DateTimeFormat('vi-VN', defaultOptions).format(new Date(date))
}

// Format datetime
export const formatDateTime = (date) => {
  return new Intl.DateTimeFormat('vi-VN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  }).format(new Date(date))
}

// Get relative time (e.g., "2 hours ago")
export const getRelativeTime = (date) => {
  const now = new Date()
  const targetDate = new Date(date)
  const diffInMs = now - targetDate
  const diffInMinutes = Math.floor(diffInMs / (1000 * 60))
  const diffInHours = Math.floor(diffInMinutes / 60)
  const diffInDays = Math.floor(diffInHours / 24)
  
  if (diffInMinutes < 1) {
    return 'Vừa xong'
  } else if (diffInMinutes < 60) {
    return `${diffInMinutes} phút trước`
  } else if (diffInHours < 24) {
    return `${diffInHours} giờ trước`
  } else if (diffInDays < 7) {
    return `${diffInDays} ngày trước`
  } else {
    return formatDate(date)
  }
}

// Debounce function
export const debounce = (func, delay) => {
  let timeoutId
  return (...args) => {
    clearTimeout(timeoutId)
    timeoutId = setTimeout(() => func.apply(null, args), delay)
  }
}

// Throttle function
export const throttle = (func, limit) => {
  let inThrottle
  return (...args) => {
    if (!inThrottle) {
      func.apply(null, args)
      inThrottle = true
      setTimeout(() => inThrottle = false, limit)
    }
  }
}

// Generate unique ID
export const generateId = () => {
  return Date.now().toString(36) + Math.random().toString(36).substr(2)
}

// Validate email
export const isValidEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email)
}

// Validate phone number (Vietnamese format)
export const isValidPhone = (phone) => {
  const phoneRegex = /^(0|\+84)[0-9]{9,10}$/
  return phoneRegex.test(phone)
}

// Generate random color
export const generateColor = () => {
  const colors = [
    '#3B82F6', '#10B981', '#F59E0B', '#EF4444', '#8B5CF6',
    '#06B6D4', '#84CC16', '#F97316', '#EC4899', '#6366F1'
  ]
  return colors[Math.floor(Math.random() * colors.length)]
}

// File size formatter
export const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// Convert to slug
export const toSlug = (str) => {
  return str
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .replace(/[^a-z0-9 -]/g, '')
    .replace(/\s+/g, '-')
    .replace(/-+/g, '-')
    .trim()
}

// Copy to clipboard
export const copyToClipboard = async (text) => {
  try {
    await navigator.clipboard.writeText(text)
    return true
  } catch (err) {
    console.error('Failed to copy to clipboard:', err)
    return false
  }
}

// Download file
export const downloadFile = (blob, filename) => {
  const url = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  window.URL.revokeObjectURL(url)
}

// Get status color
export const getStatusColor = (status) => {
  const statusColors = {
    'ACTIVE': 'green',
    'INACTIVE': 'gray',
    'PENDING': 'yellow',
    'APPROVED': 'blue',
    'REJECTED': 'red',
    'COMPLETED': 'green',
    'CANCELLED': 'red',
    'DRAFT': 'gray',
    'PUBLISHED': 'green'
  }
  return statusColors[status] || 'gray'
}

// Get vehicle type text
export const getVehicleTypeText = (type) => {
  const types = {
    'LIGHT_TRUCK': 'Xe tải nhẹ',
    'MEDIUM_TRUCK': 'Xe tải trung',
    'HEAVY_TRUCK': 'Xe tải nặng',
    'TRAILER': 'Xe kéo',
    'BUS': 'Xe buýt',
    'PICKUP': 'Xe bán tải'
  }
  return types[type] || type
}

// Get customer type text
export const getCustomerTypeText = (type) => {
  const types = {
    'INDIVIDUAL': 'Cá nhân',
    'COMPANY': 'Công ty',
    'VIP': 'Khách VIP',
    'WHOLESALE': 'Khách sỉ',
    'RETAIL': 'Khách lẻ'
  }
  return types[type] || type
}

// Validate required fields
export const validateRequired = (obj, fields) => {
  const errors = {}
  
  fields.forEach(field => {
    if (!obj[field] || (typeof obj[field] === 'string' && obj[field].trim() === '')) {
      errors[field] = 'Trường này là bắt buộc'
    }
  })
  
  return errors
}

// Deep clone object
export const deepClone = (obj) => {
  return JSON.parse(JSON.stringify(obj))
}

// Check if object is empty
export const isEmpty = (obj) => {
  return Object.keys(obj).length === 0
}

// Sort array of objects by key
export const sortBy = (array, key, direction = 'asc') => {
  return [...array].sort((a, b) => {
    const aVal = a[key]
    const bVal = b[key]
    
    if (direction === 'asc') {
      return aVal > bVal ? 1 : -1
    } else {
      return aVal < bVal ? 1 : -1
    }
  })
}

// Group array by key
export const groupBy = (array, key) => {
  return array.reduce((result, item) => {
    const group = item[key]
    if (!result[group]) {
      result[group] = []
    }
    result[group].push(item)
    return result
  }, {})
}

// Calculate percentage
export const calculatePercentage = (value, total) => {
  if (total === 0) return 0
  return Math.round((value / total) * 100)
}

// Get initials from name
export const getInitials = (name) => {
  return name
    .split(' ')
    .map(word => word.charAt(0))
    .join('')
    .toUpperCase()
    .slice(0, 2)
}
