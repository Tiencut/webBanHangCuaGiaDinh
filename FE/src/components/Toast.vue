<template>
  <div class="fixed top-4 right-4 z-50 space-y-2">
    <TransitionGroup name="toast">
      <div
        v-for="toast in toasts"
        :key="toast.id"
        class="max-w-sm w-full bg-white shadow-lg rounded-lg pointer-events-auto ring-1 ring-black ring-opacity-5 overflow-hidden"
        :class="getToastClass(toast.type)"
      >
        <div class="p-4">
          <div class="flex items-start">
            <div class="flex-shrink-0">
              <component :is="getIcon(toast.type)" class="h-6 w-6" />
            </div>
            <div class="ml-3 w-0 flex-1 pt-0.5">
              <p class="text-sm font-medium text-gray-900">
                {{ toast.title }}
              </p>
              <p v-if="toast.message" class="mt-1 text-sm text-gray-500">
                {{ toast.message }}
              </p>
            </div>
            <div class="ml-4 flex-shrink-0 flex">
              <button
                @click="removeToast(toast.id)"
                class="bg-white rounded-md inline-flex text-gray-400 hover:text-gray-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                <span class="sr-only">Close</span>
                <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>
    </TransitionGroup>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { 
  CheckCircleIcon, 
  ExclamationTriangleIcon, 
  InformationCircleIcon,
  XCircleIcon 
} from '@heroicons/vue/24/outline'

export default {
  name: 'Toast',
  setup() {
    const toasts = ref([])
    let nextId = 1

    const addToast = (toast) => {
      const id = nextId++
      const newToast = {
        id,
        type: toast.type || 'info',
        title: toast.title,
        message: toast.message,
        duration: toast.duration || 5000
      }
      
      toasts.value.push(newToast)
      
      // Auto remove after duration
      setTimeout(() => {
        removeToast(id)
      }, newToast.duration)
    }

    const removeToast = (id) => {
      const index = toasts.value.findIndex(toast => toast.id === id)
      if (index > -1) {
        toasts.value.splice(index, 1)
      }
    }

    const getToastClass = (type) => {
      const classes = {
        success: 'border-l-4 border-green-400',
        error: 'border-l-4 border-red-400',
        warning: 'border-l-4 border-yellow-400',
        info: 'border-l-4 border-blue-400'
      }
      return classes[type] || classes.info
    }

    const getIcon = (type) => {
      const icons = {
        success: CheckCircleIcon,
        error: XCircleIcon,
        warning: ExclamationTriangleIcon,
        info: InformationCircleIcon
      }
      return icons[type] || icons.info
    }

    // Expose methods globally
    onMounted(() => {
      window.$toast = {
        success: (title, message) => addToast({ type: 'success', title, message }),
        error: (title, message) => addToast({ type: 'error', title, message }),
        warning: (title, message) => addToast({ type: 'warning', title, message }),
        info: (title, message) => addToast({ type: 'info', title, message })
      }
    })

    return {
      toasts,
      addToast,
      removeToast,
      getToastClass,
      getIcon
    }
  }
}
</script>

<style scoped>
.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s ease;
}

.toast-enter-from {
  opacity: 0;
  transform: translateX(100%);
}

.toast-leave-to {
  opacity: 0;
  transform: translateX(100%);
}

.toast-move {
  transition: transform 0.3s ease;
}
</style> 