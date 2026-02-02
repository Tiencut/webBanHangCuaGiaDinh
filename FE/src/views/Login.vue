<template>
  <div class="auth-page">
    <el-card class="auth-card">
      <h2>Đăng nhập</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item label="Tài khoản" prop="username">
          <el-input v-model="form.username" autocomplete="username" />
        </el-form-item>

        <el-form-item label="Mật khẩu" prop="password">
          <el-input v-model="form.password" type="password" autocomplete="current-password" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit" :loading="loading">Đăng nhập</el-button>
          <router-link to="/register" style="margin-left:12px">Đăng ký</router-link>
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
const form = ref({ username: '', password: '' })
const loading = ref(false)

const rules = {
  username: [{ required: true, message: 'Vui lòng nhập tài khoản', trigger: 'blur' }],
  password: [{ required: true, message: 'Vui lòng nhập mật khẩu', trigger: 'blur' }]
}

async function onSubmit() {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    const res = await auth.login(form.value)
    loading.value = false
    if (res.success) {
      ElMessage.success('Đăng nhập thành công')
      router.push('/')
    } else {
      ElMessage.error(res.error || 'Đăng nhập thất bại')
    }
  })
}
</script>

<style scoped>
.auth-page { display:flex; align-items:center; justify-content:center; padding:40px }
.auth-card { width:400px }
</style>
