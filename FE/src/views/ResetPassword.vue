<template>
  <div class="reset-password-page">
    <h2>Đặt lại mật khẩu</h2>

    <div v-if="checking" class="notice">Đang kiểm tra mã...</div>

    <div v-else>
      <div v-if="!token || !valid" class="notice error">
        Liên kết đặt lại mật khẩu không hợp lệ hoặc đã hết hạn. Vui lòng yêu cầu lại từ trang "Quên mật khẩu".
      </div>

      <form v-else @submit.prevent="onSubmit" class="form">
        <div class="field">
          <label for="password">Mật khẩu mới</label>
          <input id="password" type="password" v-model="password" autocomplete="new-password" />
        </div>

        <div class="field">
          <label for="confirm">Xác nhận mật khẩu</label>
          <input id="confirm" type="password" v-model="confirm" autocomplete="new-password" />
        </div>

        <div v-if="error" class="notice error">{{ error }}</div>
        <div v-if="success" class="notice success">{{ success }}</div>

        <div class="actions">
          <button type="submit" :disabled="loading">Đặt lại mật khẩu</button>
          <router-link to="/login" class="link">Quay về đăng nhập</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { authAPI } from '../services/index'

const route = useRoute()
const router = useRouter()

const token = route.query.token || ''
const password = ref('')
const confirm = ref('')
const loading = ref(false)
const error = ref('')
const success = ref('')
const checking = ref(true)
const valid = ref(false)

function validateForm() {
  error.value = ''
  if (!password.value || password.value.length < 8) {
    error.value = 'Mật khẩu phải có ít nhất 8 ký tự.'
    return false
  }
  if (password.value !== confirm.value) {
    error.value = 'Mật khẩu xác nhận không khớp.'
    return false
  }
  return true
}

onMounted(async () => {
  if (!token) {
    checking.value = false
    valid.value = false
    return
  }
  try {
    await authAPI.validateResetToken(token)
    valid.value = true
  } catch (e) {
    valid.value = false
  } finally {
    checking.value = false
  }
})

async function onSubmit() {
  if (!validateForm()) return
  if (!token) {
    error.value = 'Mã không hợp lệ.'
    return
  }

  loading.value = true
  error.value = ''
  success.value = ''
  try {
    await authAPI.resetPassword(token, password.value)
    success.value = 'Mật khẩu đã được cập nhật. Chuyển hướng tới trang đăng nhập...'
    setTimeout(() => router.push('/login'), 1500)
  } catch (err) {
    const msg = (err && err.response && err.response.data && err.response.data.message) || (err && err.message) || 'Đã có lỗi xảy ra. Vui lòng thử lại.'
    error.value = msg
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.reset-password-page {
  max-width: 420px;
  margin: 48px auto;
  padding: 24px;
  border: 1px solid #e6e6e6;
  border-radius: 8px;
  background: #fff;
}
.reset-password-page h2 {
  margin: 0 0 16px;
  font-size: 20px;
}
.form .field {
  margin-bottom: 12px;
}
.form label {
  display: block;
  margin-bottom: 6px;
  color: #333;
}
.form input[type="password"] {
  width: 100%;
  padding: 8px 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.actions {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-top: 14px;
}
.actions .link {
  color: #1f6feb;
  text-decoration: none;
}
.actions button[disabled] {
  opacity: 0.6;
}
.notice {
  padding: 10px;
  margin-top: 8px;
  border-radius: 4px;
}
.notice.error { background: #fff0f0; color: #a00; border: 1px solid #f2c2c2 }
.notice.success { background: #f0fff4; color: #046; border: 1px solid #bfe6c7 }
</style>
