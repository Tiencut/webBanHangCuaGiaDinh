<template>
  <!-- Add Customer Modal -->
  <el-dialog v-model="showAddModal" title="Thêm khách hàng" width="500px">
    <el-form :model="newCustomer" label-width="140px">
      <el-form-item label="Tên khách hàng" required>
        <el-input v-model="newCustomer.name" required></el-input>
      </el-form-item>
      <el-form-item label="Loại khách hàng">
        <el-select v-model="newCustomer.customerType" placeholder="Chọn loại khách hàng">
          <el-option label="Khách lẻ" value="RETAIL" />
          <el-option label="Khách sỉ" value="WHOLESALE" />
          <el-option label="Garage sửa chữa" value="GARAGE" />
          <el-option label="Đại lý" value="DEALER" />
          <el-option label="VIP" value="VIP" />
        </el-select>
      </el-form-item>
      <el-form-item label="Trạng thái">
        <el-select v-model="newCustomer.status" placeholder="Chọn trạng thái">
          <el-option label="Hoạt động" value="ACTIVE" />
          <el-option label="Không hoạt động" value="INACTIVE" />
          <el-option label="Danh sách đen" value="BLACKLISTED" />
        </el-select>
      </el-form-item>
      <el-form-item label="Người liên hệ">
        <el-input v-model="newCustomer.contactPerson"></el-input>
      </el-form-item>
      <!-- <el-form-item label="Tên công ty">
        <el-input v-model="newCustomer.companyName"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="Mã số thuế">
        <el-input v-model="newCustomer.taxCode"></el-input>
      </el-form-item> -->
      <el-form-item label="Số điện thoại">
        <el-input v-model="newCustomer.phone"></el-input>
      </el-form-item>
      <el-form-item label="Email">
        <el-input v-model="newCustomer.email"></el-input>
      </el-form-item>
      <el-form-item label="Địa chỉ">
        <el-input v-model="newCustomer.address"></el-input>
      </el-form-item>
      <el-form-item label="Hạn mức công nợ">
        <el-input v-model.number="newCustomer.creditLimit" type="number" min="0"></el-input>
      </el-form-item>
      <el-form-item label="Công nợ hiện tại">
        <el-input v-model.number="newCustomer.currentDebt" type="number" min="0"></el-input>
      </el-form-item>
      <el-form-item label="Chiết khấu (%)">
        <el-input v-model.number="newCustomer.discountPercentage" type="number" min="0" max="100"></el-input>
      </el-form-item>
      <el-form-item label="Điểm tích lũy">
        <el-input v-model.number="newCustomer.loyaltyPoints" type="number" min="0"></el-input>
      </el-form-item>
      <el-form-item label="Hãng xe ưu tiên">
        <el-input v-model="newCustomer.preferredVehicleBrands" placeholder="VD: Hino,Isuzu,Hyundai"></el-input>
      </el-form-item>
      <el-form-item label="Ghi chú">
        <el-input v-model="newCustomer.notes" type="textarea"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="showAddModal = false">Hủy</el-button>
      <el-button type="primary" @click="addCustomer">Thêm</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue';
import { ElMessage } from 'element-plus';
import customerService from '@/services/customerService';

const props = defineProps({
  show: Boolean,
});

const emit = defineEmits(['update:show', 'customerAdded']);

const newCustomer = ref({
  name: '',
  address: '',
  phone: '',
  email: '',
  contactPerson: '',
  customerType: 'RETAIL',
  creditLimit: 0,
  currentDebt: 0,
  discountPercentage: 0,
  loyaltyPoints: 0,
  preferredVehicleBrands: '',
  notes: '',
  status: 'ACTIVE',
});

watch(() => props.show, (newVal) => {
  if (newVal) {
    resetForm();
  }
});

const addCustomer = async () => {
  if (!newCustomer.value.name || !newCustomer.value.customerType || !newCustomer.value.status) {
    ElMessage.error('Vui lòng nhập đầy đủ Tên khách hàng, Loại khách hàng và Trạng thái!');
    return;
  }

  const payload = { ...newCustomer.value };
  const numberFields = ['creditLimit', 'currentDebt', 'discountPercentage', 'loyaltyPoints'];
  numberFields.forEach(field => {
    if (payload[field] === '' || payload[field] === null || isNaN(payload[field])) {
      delete payload[field];
    } else {
      payload[field] = Number(payload[field]);
    }
  });

  Object.keys(payload).forEach(key => {
    if (payload[key] === '' || payload[key] === null) {
      delete payload[key];
    }
  });

  try {
    await customerService.createCustomer(payload);
    ElMessage.success('Thêm khách hàng thành công!');
    emit('update:show', false);
    emit('customerAdded');
  } catch (e) {
    console.error('Lỗi khi thêm khách hàng:', e);
    ElMessage.error('Lỗi khi thêm khách hàng.');
  }
};

const resetForm = () => {
  newCustomer.value = {
    name: '',
    address: '',
    phone: '',
    email: '',
    contactPerson: '',
    customerType: 'RETAIL',
    creditLimit: 0,
    currentDebt: 0,
    discountPercentage: 0,
    loyaltyPoints: 0,
    preferredVehicleBrands: '',
    notes: '',
    status: 'ACTIVE',
  };
};

const closeDialog = () => {
  emit('update:show', false);
};
</script>

<style scoped>
/* Có thể thêm các style riêng cho modal tại đây nếu cần */
</style>