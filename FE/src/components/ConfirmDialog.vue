<template>
  <div v-if="show" class="fixed inset-0 z-50 overflow-y-auto">
    <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
      <!-- Background overlay -->
      <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" @click="handleCancel"></div>

      <!-- Modal panel -->
      <div class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
        <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
          <div class="sm:flex sm:items-start">
            <div class="mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full sm:mx-0 sm:h-10 sm:w-10"
                 :class="getIconClass()">
              <component :is="getIcon()" class="h-6 w-6" :class="getIconColor()" />
            </div>
            <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
              <h3 class="text-lg leading-6 font-medium text-gray-900">
                {{ title }}
              </h3>
              <div class="mt-2">
                <p class="text-sm text-gray-500">
                  {{ message }}
                </p>
              </div>
            </div>
          </div>
        </div>
        <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
          <button
            type="button"
            @click="handleConfirm"
            class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 text-base font-medium text-white focus:outline-none focus:ring-2 focus:ring-offset-2 sm:ml-3 sm:w-auto sm:text-sm"
            :class="getConfirmButtonClass()"
          >
            {{ confirmText }}
          </button>
          <button
            type="button"
            @click="handleCancel"
            class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
          >
            {{ cancelText }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { 
  ExclamationTriangleIcon,
  InformationCircleIcon,
  XCircleIcon,
  CheckCircleIcon
} from '@heroicons/vue/24/outline'

export default {
  name: 'ConfirmDialog',
  props: {
    show: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: 'Xác nhận'
    },
    message: {
      type: String,
      default: 'Bạn có chắc chắn muốn thực hiện hành động này?'
    },
    type: {
      type: String,
      default: 'warning', // warning, info, error, success
      validator: value => ['warning', 'info', 'error', 'success'].includes(value)
    },
    confirmText: {
      type: String,
      default: 'Xác nhận'
    },
    cancelText: {
      type: String,
      default: 'Hủy'
    }
  },
  emits: ['confirm', 'cancel'],
  setup(props, { emit }) {
    const handleConfirm = () => {
      emit('confirm')
    }

    const handleCancel = () => {
      emit('cancel')
    }

    const getIcon = () => {
      const icons = {
        warning: ExclamationTriangleIcon,
        info: InformationCircleIcon,
        error: XCircleIcon,
        success: CheckCircleIcon
      }
      return icons[props.type] || icons.warning
    }

    const getIconClass = () => {
      const classes = {
        warning: 'bg-yellow-100',
        info: 'bg-blue-100',
        error: 'bg-red-100',
        success: 'bg-green-100'
      }
      return classes[props.type] || classes.warning
    }

    const getIconColor = () => {
      const colors = {
        warning: 'text-yellow-600',
        info: 'text-blue-600',
        error: 'text-red-600',
        success: 'text-green-600'
      }
      return colors[props.type] || colors.warning
    }

    const getConfirmButtonClass = () => {
      const classes = {
        warning: 'bg-yellow-600 hover:bg-yellow-700 focus:ring-yellow-500',
        info: 'bg-blue-600 hover:bg-blue-700 focus:ring-blue-500',
        error: 'bg-red-600 hover:bg-red-700 focus:ring-red-500',
        success: 'bg-green-600 hover:bg-green-700 focus:ring-green-500'
      }
      return classes[props.type] || classes.warning
    }

    return {
      handleConfirm,
      handleCancel,
      getIcon,
      getIconClass,
      getIconColor,
      getConfirmButtonClass
    }
  }
}
</script> 