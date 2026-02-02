<template>
  <div class="auth-page">
    <el-card class="auth-card">
      <h2>Đăng ký</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item label="Tài khoản" prop="username">
          <el-input v-model="form.username" autocomplete="username" />
        </el-form-item>

        <el-form-item label="Email" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>

        <el-form-item label="Mật khẩu" prop="password">
          <el-input v-model="form.password" type="password" autocomplete="new-password" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit" :loading="loading">Đăng ký</el-button>
          <router-link to="/login" style="margin-left:12px">Đăng nhập</router-link>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const auth = useAuthStore()

const formRef = ref(null)
const form = ref({ username: '', email: '', password: '' })
const loading = ref(false)

const rules = {
  username: [{ required: true, message: 'Vui lòng nhập tài khoản', trigger: 'blur' }],
  email: [{ required: true, message: 'Vui lòng nhập email', trigger: 'blur' }],
  password: [{ required: true, message: 'Vui lòng nhập mật khẩu', trigger: 'blur' }]
}

async function onSubmit() {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    const res = await auth.register(form.value)
    loading.value = false
    if (res.success) {
      ElMessage.success('Đăng ký thành công. Vui lòng đăng nhập.')
      router.push('/login')
    } else {
      ElMessage.error(res.error || 'Đăng ký thất bại')
    }
  })
}
</script>

<style scoped>
.auth-page { display:flex; align-items:center; justify-content:center; padding:40px }
.auth-card { width:400px }
</style>
