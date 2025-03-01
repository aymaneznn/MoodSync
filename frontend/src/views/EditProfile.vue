<template>
    <Toast />
    <Card class="profile-card">
        <template #title>
            <div class="profile-header">
                <h2><i class="pi pi-user-edit"></i> Edit Profile</h2>
            </div>
        </template>

        <template #content>
            <div class="profile-content">
                <!-- Avatar with animation -->
                <div class="avatar-wrapper">
                    <Avatar 
                        v-if="user.profilePictureUrl" 
                        :image="user.profilePictureUrl" 
                        shape="circle" 
                        size="xlarge" 
                        class="profile-avatar"
                    />
                    <Skeleton v-else width="120px" height="120px" shape="circle" class="profile-avatar skeleton" />
                </div>

                <!-- Photo upload -->
                <FileUpload 
                    mode="basic" 
                    accept="image/*" 
                    customUpload 
                    chooseLabel="Change Photo" 
                    class="file-upload"
                    @select="handleFileUpload"
                />

                <!-- Name field -->
                <div class="input-group">
                    <label for="name">Name</label>
                    <InputText id="name" v-model="user.name" class="input-text"/>
                </div>

                <!-- Email field (disabled) -->
                <div class="input-group">
                    <label for="email">Email</label>
                    <InputText id="email" v-model="user.email" class="input-text"/>
                </div>

                <!-- Save button -->
                <Button 
                    label="Save" 
                    icon="pi pi-check" 
                    class="save-button"
                    :loading="loading"
                    @click="updateProfile"
                />
            </div>
        </template>
    </Card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useToast } from 'primevue/usetoast';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import Card from 'primevue/card';
import Avatar from 'primevue/avatar';
import FileUpload from 'primevue/fileupload';
import Toast from 'primevue/toast';
import Skeleton from 'primevue/skeleton';
import { getUserProfile } from '@/service/apiService.js';
import axios from 'axios';

// 🔹 States
const user = ref({ name: '', email: '', profilePictureUrl: '' });
const loading = ref(false);
const toast = useToast();

// 🔹 Fetch user profile
const fetchUserProfile = async () => {
    try {
        const userId = localStorage.getItem('userId');
        const token = localStorage.getItem('token');
        const data = await getUserProfile(userId, token);
        user.value = data;
    } catch (error) {
        console.error("Error fetching profile:", error);
        toast.add({ severity: 'error', summary: 'Error', detail: 'Unable to load profile.', life: 3000 });
    }
};

// 🔹 Update profile
const updateProfile = async () => {
    try {
        loading.value = true;
        const token = localStorage.getItem('token');
        await axios.put(`http://localhost:8080/users/${user.value.id}`, user.value, {
            headers: { Authorization: `Bearer ${token}` }
        });
        toast.add({ severity: 'success', summary: 'Success', detail: 'Profile updated!', life: 3000 });
    } catch (error) {
        console.error("Update error:", error);
        toast.add({ severity: 'error', summary: 'Error', detail: "Update failed.", life: 3000 });
    } finally {
        loading.value = false;
    }
};

// 🔹 Handle image upload
const handleFileUpload = async (event) => {
    const file = event.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);

    try {
        const token = localStorage.getItem('token');
        const response = await axios.post(`http://localhost:8080/users/${user.value.id}/upload`, formData, {
            headers: { Authorization: `Bearer ${token}`, 'Content-Type': 'multipart/form-data' }
        });

        user.value.profilePictureUrl = response.data.url;
        toast.add({ severity: 'success', summary: 'Success', detail: 'Profile picture updated!', life: 3000 });
    } catch (error) {
        console.error("Upload error:", error);
        toast.add({ severity: 'error', summary: 'Error', detail: "Image upload failed.", life: 3000 });
    }
};

// 🔹 Load user data on mount
onMounted(fetchUserProfile);
</script>

<style scoped>
/* Main container */
.profile-container {
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    background: linear-gradient(135deg, #f3f4f6, #e0e7ff);
    padding: 2rem;
}

/* Profile card */
.profile-card {
    width: 100%;
    max-width: 450px;
    padding: 2rem;
    border-radius: 15px;
    box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.1);
    background: white;
}

/* Header */
.profile-header {
    text-align: center;
    font-size: 1.4rem;
    font-weight: bold;
    color: #374151;
    margin-bottom: 1rem;
}

.profile-header i {
    color: #6366f1;
    margin-right: 8px;
}

/* Avatar */
.avatar-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 1rem;
}

.profile-avatar {
    border: 4px solid #6366f1;
    transition: transform 0.3s ease;
}

.profile-avatar:hover {
    transform: scale(1.1);
}

.skeleton {
    border-radius: 50%;
}

/* Upload */
.file-upload {
    width: 100%;
    text-align: center;
    background-color: #eef2ff;
    border-radius: 8px;
    padding: 10px;
    transition: background 0.3s ease;
}

.file-upload:hover {
    background-color: #dbeafe;
}

/* Inputs */
.input-group {
    margin-bottom: 1.5rem;
}

.input-group label {
    display: block;
    font-weight: bold;
    color: #374151;
    margin-bottom: 0.5rem;
}

.input-text {
    width: 100%;
    padding: 10px;
    border-radius: 8px;
    border: 1px solid #cbd5e1;
    font-size: 1rem;
    transition: border 0.3s ease;
}

.input-text:focus {
    border: 1px solid #6366f1;
    outline: none;
    box-shadow: 0 0 8px rgba(99, 102, 241, 0.3);
}

.input-disabled {
    background: #f3f4f6;
    color: #9ca3af;
    cursor: not-allowed;
}

/* Save button */
.save-button {
    width: 100%;
    background-color: #6366f1;
    border: none;
    padding: 12px;
    font-size: 1rem;
    font-weight: bold;
    color: white;
    border-radius: 8px;
    transition: background 0.3s ease;
}

.save-button:hover {
    background-color: #4f46e5;
}

.save-button:active {
    background-color: #4338ca;
}
</style>