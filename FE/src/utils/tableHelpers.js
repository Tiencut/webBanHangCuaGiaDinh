// utils/tableHelpers.js
export const exportToExcel = (data, filename = 'export.xlsx') => {
  // Simple CSV export (có thể mở rộng với library xlsx)
  const headers = Object.keys(data[0] || {})
  const csvContent = [
    headers.join(','),
    ...data.map(row => headers.map(header => {
      const value = row[header]
      return typeof value === 'string' ? `"${value}"` : value
    }).join(','))
  ].join('\n')
  
  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  const url = URL.createObjectURL(blob)
  link.setAttribute('href', url)
  link.setAttribute('download', filename.replace('.xlsx', '.csv'))
  link.style.visibility = 'hidden'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

export const formatVietnameseNumber = (number) => {
  return new Intl.NumberFormat('vi-VN').format(number)
}

export const formatVietnameseCurrency = (amount) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(amount)
}

export const formatVietnameseDate = (date) => {
  return new Date(date).toLocaleDateString('vi-VN')
}

export const formatVietnameseDateTime = (date) => {
  return new Date(date).toLocaleString('vi-VN')
}

export const getRandomColor = () => {
  const colors = [
    'bg-red-100 text-red-800',
    'bg-green-100 text-green-800',
    'bg-blue-100 text-blue-800',
    'bg-yellow-100 text-yellow-800',
    'bg-purple-100 text-purple-800',
    'bg-pink-100 text-pink-800',
    'bg-indigo-100 text-indigo-800'
  ]
  return colors[Math.floor(Math.random() * colors.length)]
}

export const debounce = (func, wait) => {
  let timeout
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout)
      func(...args)
    }
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
  }
}
