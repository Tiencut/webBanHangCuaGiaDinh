<template>
  <div class="auth-page">
    <el-card class="auth-card">
      <h2>Quên mật khẩu</h2>
      <el-form :model="form" ref="formRef" label-position="top">
        <el-form-item label="Email" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit" :loading="loading">Gửi liên kết đặt lại</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { authAPI } from '../services'

const formRef = ref(null)
const form = ref({ email: '' })
const loading = ref(false)

async function onSubmit() {
  if (!form.value.email) return ElMessage.error('Vui lòng nhập email')
  loading.value = true
  try {
    await authAPI.forgotPassword(form.value.email, window.location.origin + '/reset-password')
    ElMessage.success('Nếu email tồn tại, liên kết đã được gửi.')
  } catch (e) {
    ElMessage.error('Không thể gửi email, thử lại sau.')
  } finally { loading.value = false }
}
</script>

<style scoped>
.auth-page { display:flex; align-items:center; justify-content:center; padding:40px }
.auth-card { width:400px }
</style>
