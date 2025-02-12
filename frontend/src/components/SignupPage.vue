<script>
import { ref } from 'vue';
import InputText from 'primevue/inputtext';
import Password from 'primevue/password';
import Button from 'primevue/button';
import Message from 'primevue/message';
import { signup } from '@/service/userService';

export default {
  components: {
    InputText,
    Password,
    Button,
    Message,
  },
  setup() {
    const form = ref({
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
    });

    const errors = ref({});
    const successMessage = ref('');
    const errorMessage = ref('');

    const validateForm = () => {
      errors.value = {};

      if (!form.value.username) {
        errors.value.username = 'Username is required.';
      }

      if (!form.value.email) {
        errors.value.email = 'Email is required.';
      } else if (!/\S+@\S+\.\S+/.test(form.value.email)) {
        errors.value.email = 'Email is invalid.';
      }

      if (!form.value.password) {
        errors.value.password = 'Password is required.';
      } else if (form.value.password.length < 6) {
        errors.value.password = 'Password must be at least 6 characters.';
      }

      if (form.value.password !== form.value.confirmPassword) {
        errors.value.confirmPassword = 'Passwords do not match.';
      }

      return Object.keys(errors.value).length === 0;
    };

    const handleSubmit = async () => {
      if (!validateForm()) {
        errorMessage.value = 'Please fix the errors in the form.';
        return;
      }

      try {
        const userData = {
          "email": form.value.email,
          "passwordHash": form.value.password,
          "name": form.value.username,
          "role": "user",
          "preferences": {
            "theme": "dark",
            "notifications": true
          },
          "profilePictureUrl": "https://example.com/profile/john_doe.png",
          "connectionType": "independent",
          "externalAccountId": null
        };
        const response = await signup(userData);
        console.log('Signup successful:', response);
        successMessage.value = 'Signup successful! Redirecting...';
        errorMessage.value = '';
        // Redirect to login or home page after a delay
        setTimeout(() => {
          //window.location.href = '/login'; // Replace with your desired redirect path
        }, 2000);
      } catch (error) {
        console.error('Signup failed:', error);
        errorMessage.value = error.response?.data?.message || 'Signup failed. Please try again.';
      }
    };

    const handleGoogleSignUp = () => {
      // Redirect to Google OAuth endpoint
      window.location.href = 'https://accounts.google.com/o/oauth2/v2/auth?' +
        'client_id=YOUR_GOOGLE_CLIENT_ID&' +
        'redirect_uri=YOUR_REDIRECT_URI&' +
        'response_type=code&' +
        'scope=email profile&' +
        'access_type=offline&' +
        'prompt=consent';
    };

    return {
      form,
      errors,
      successMessage,
      errorMessage,
      handleSubmit,
      handleGoogleSignUp,
    };
  },
};
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
      <h1 class="text-2xl font-bold mb-6 text-center">Sign Up</h1>
      <form @submit.prevent="handleSubmit">
        <div class="mb-4">
          <label for="username" class="block text-sm font-medium text-gray-700">Username</label>
          <InputText id="username" v-model="form.username" class="w-full mt-1"
            :class="{ 'p-invalid': errors.username }" />
          <small v-if="errors.username" class="p-error">{{ errors.username }}</small>
        </div>

        <div class="mb-4">
          <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
          <InputText id="email" v-model="form.email" class="w-full mt-1" :class="{ 'p-invalid': errors.email }" />
          <small v-if="errors.email" class="p-error">{{ errors.email }}</small>
        </div>

        <div class="mb-4">
          <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
          <Password id="password" v-model="form.password" class="w-full mt-1" :class="{ 'p-invalid': errors.password }"
            toggleMask />
          <small v-if="errors.password" class="p-error">{{ errors.password }}</small>
        </div>

        <div class="mb-6">
          <label for="confirmPassword" class="block text-sm font-medium text-gray-700">Confirm Password</label>
          <Password id="confirmPassword" v-model="form.confirmPassword" class="w-full mt-1"
            :class="{ 'p-invalid': errors.confirmPassword }" toggleMask />
          <small v-if="errors.confirmPassword" class="p-error">{{ errors.confirmPassword }}</small>
        </div>

        <Button type="submit" label="Sign Up"
          class="w-full bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded" />

        <div class="mt-4 text-center">
          <p class="text-sm text-gray-600">Or</p>
        </div>

        <Button label="Sign Up with Google"
          class="w-full bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded mt-4"
          @click="handleGoogleSignUp" />

        <div class="mt-4 text-center">
          <p class="text-sm text-gray-600">
            Already have an account?
            <a href="/auth/login" class="text-blue-500 hover:text-blue-600">Log In</a>
          </p>
        </div>
      </form>

      <Message v-if="successMessage" severity="success" class="mt-4">{{ successMessage }}</Message>
      <Message v-if="errorMessage" severity="error" class="mt-4">{{ errorMessage }}</Message>
    </div>
  </div>
</template>

<style scoped></style>