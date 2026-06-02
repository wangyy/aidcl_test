<script setup>
import { computed, onMounted, reactive, ref } from 'vue';
import { createUser, deleteUser, listUsers, updateUser } from './api/users';

const users = ref([]);
const loading = ref(false);
const saving = ref(false);
const error = ref('');
const editingUserId = ref(null);
const form = reactive({
  username: '',
  organization: '',
});

const isEditing = computed(() => editingUserId.value !== null);

function resetForm() {
  editingUserId.value = null;
  form.username = '';
  form.organization = '';
}

async function loadUsers() {
  loading.value = true;
  error.value = '';
  try {
    users.value = await listUsers();
  } catch (err) {
    error.value = err.message;
  } finally {
    loading.value = false;
  }
}

function startEdit(user) {
  editingUserId.value = user.userId;
  form.username = user.username;
  form.organization = user.organization;
}

async function submitForm() {
  if (!form.username.trim() || !form.organization.trim()) {
    error.value = '请填写用户名和用户所属机构';
    return;
  }

  saving.value = true;
  error.value = '';
  const payload = {
    username: form.username.trim(),
    organization: form.organization.trim(),
  };

  try {
    if (isEditing.value) {
      await updateUser(editingUserId.value, payload);
    } else {
      await createUser(payload);
    }
    resetForm();
    await loadUsers();
  } catch (err) {
    error.value = err.message;
  } finally {
    saving.value = false;
  }
}

async function removeUser(user) {
  if (!window.confirm(`确认删除用户「${user.username}」吗？`)) {
    return;
  }

  error.value = '';
  try {
    await deleteUser(user.userId);
    await loadUsers();
  } catch (err) {
    error.value = err.message;
  }
}

onMounted(loadUsers);
</script>

<template>
  <main class="page">
    <section class="hero">
      <p class="eyebrow">Vue + Spring Boot</p>
      <h1>用户管理 Demo</h1>
      <p>管理用户名、用户所属机构和用户 ID，后端数据存储到 MySQL。</p>
    </section>

    <section class="card form-card">
      <div>
        <h2>{{ isEditing ? '编辑用户' : '新增用户' }}</h2>
        <p>用户 ID 由数据库自动生成。</p>
      </div>

      <form class="form" @submit.prevent="submitForm">
        <label>
          用户名
          <input v-model="form.username" type="text" placeholder="请输入用户名" maxlength="64" />
        </label>
        <label>
          用户所属机构
          <input v-model="form.organization" type="text" placeholder="请输入所属机构" maxlength="128" />
        </label>
        <div class="actions">
          <button type="submit" :disabled="saving">{{ saving ? '保存中...' : isEditing ? '保存修改' : '新增用户' }}</button>
          <button v-if="isEditing" class="secondary" type="button" @click="resetForm">取消编辑</button>
        </div>
      </form>
    </section>

    <p v-if="error" class="error">{{ error }}</p>

    <section class="card">
      <div class="table-header">
        <div>
          <h2>用户列表</h2>
          <p>共 {{ users.length }} 条记录</p>
        </div>
        <button class="secondary" type="button" :disabled="loading" @click="loadUsers">刷新</button>
      </div>

      <div v-if="loading" class="empty">加载中...</div>
      <div v-else-if="users.length === 0" class="empty">暂无用户，请先新增一条。</div>
      <table v-else>
        <thead>
          <tr>
            <th>用户 ID</th>
            <th>用户名</th>
            <th>用户所属机构</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.userId">
            <td>{{ user.userId }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.organization }}</td>
            <td class="row-actions">
              <button class="link" type="button" @click="startEdit(user)">编辑</button>
              <button class="danger" type="button" @click="removeUser(user)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </section>
  </main>
</template>
