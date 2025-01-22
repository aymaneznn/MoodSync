<template>
    <div class="min-h-screen flex items-center justify-center bg-gray-100">
      <div class="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
        <h1 class="text-2xl font-bold mb-6 text-center">Login</h1>
        <form @submit.prevent="handleSubmit">
          <div class="mb-4">
            <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
            <InputText
              id="email"
              v-model="form.email"
              class="w-full mt-1"
              :class="{ 'p-invalid': errors.email }"
            />
            <small v-if="errors.email" class="p-error">{{ errors.email }}</small>
          </div>
  
          <div class="mb-6">
            <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
            <Password
              id="password"
              v-model="form.password"
              class="w-full mt-1"
              :class="{ 'p-invalid': errors.password }"
              toggleMask
            />
            <small v-if="errors.password" class="p-error">{{ errors.password }}</small>
          </div>
  
          <Button
            type="submit"
            label="Login"
            class="w-full bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded"
          />
  
          <div class="mt-4 text-center">
            <p class="text-sm text-gray-600">Or</p>
          </div>
  
          <Button
            label="Login with Google"
            class="w-full bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded mt-4"
            @click="handleGoogleLogin"
          />
  
          <div class="mt-4 text-center">
            <p class="text-sm text-gray-600">
              Don't have an account?
              <a href="/signup" class="text-blue-500 hover:text-blue-600">Sign Up</a>
            </p>
          </div>
        </form>
  
        <Message v-if="successMessage" severity="success" class="mt-4">{{ successMessage }}</Message>
        <Message v-if="errorMessage" severity="error" class="mt-4">{{ errorMessage }}</Message>
      </div>
    </div>
  </template>
  
  <script>
  import { ref } from 'vue';
  import InputText from 'primevue/inputtext';
  import Password from 'primevue/password';
  import Button from 'primevue/button';
  import Message from 'primevue/message';
  
  export default {
    components: {
      InputText,
      Password,
      Button,
      Message,
    },
    setup() {
      const form = ref({
        email: '',
        password: '',
      });
  
      const errors = ref({});
      const successMessage = ref('');
      const errorMessage = ref('');
  
      const validateForm = () => {
        errors.value = {};
  
        if (!form.value.email) {
          errors.value.email = 'Email is required.';
        } else if (!/\S+@\S+\.\S+/.test(form.value.email)) {
          errors.value.email = 'Email is invalid.';
        }
  
        if (!form.value.password) {
          errors.value.password = 'Password is required.';
        }
  
        return Object.keys(errors.value).length === 0;
      };
  
      const handleSubmit = () => {
        if (validateForm()) {
          // Simulate API call
          setTimeout(() => {
            successMessage.value = 'Login successful! Redirecting...';
            errorMessage.value = '';
            // Redirect to the home page or dashboard
          }, 1000);
        } else {
          errorMessage.value = 'Please fix the errors in the form.';
        }
      };
  
      const handleGoogleLogin = () => {
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
        handleGoogleLogin,
      };
    },
  };
  </script>
  
  <style scoped>
  /* Add custom styles if needed */
  </style>