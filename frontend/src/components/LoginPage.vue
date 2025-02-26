<template>
    <div class="min-h-screen flex items-center justify-center bg-gray-100">
        <div class="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
            <h1 class="text-2xl font-bold mb-6 text-center">Login</h1>
            <form @submit.prevent="handleSubmit">
                <div class="mb-4">
                    <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
                    <InputText id="email" v-model="form.email" class="w-full mt-1" :class="{ 'p-invalid': errors.email }" />
                    <small v-if="errors.email" class="p-error">{{ errors.email }}</small>
                </div>

                <div class="mb-6">
                    <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
                    <Password id="password" v-model="form.password" class="w-full mt-1" :class="{ 'p-invalid': errors.password }" toggleMask />
                    <small v-if="errors.password" class="p-error">{{ errors.password }}</small>
                </div>

                <Button type="submit" label="Login" class="w-full bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded" />

                <div class="mt-4 text-center">
                    <p class="text-sm text-gray-600">Or</p>
                </div>

                <Button label="Login with Google" class="w-full bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded mt-4" @click="handleGoogleLogin" />

                <div class="mt-4 text-center">
                    <p class="text-sm text-gray-600">
                        Don't have an account?
                        <a href="/auth/signup" class="text-blue-500 hover:text-blue-600">Sign Up</a>
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
import { login } from '@/service/userService';
import { useRouter } from 'vue-router';

export default {
    components: {
        InputText,
        Password,
        Button,
        Message
    },
    setup() {
        const router = useRouter();
        const form = ref({
            email: '',
            password: ''
        });

        const errors = ref({});
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

        const handleSubmit = async () => {
            if (validateForm()) {
                try {
                    const response = await login({
                        email: form.value.email,
                        password: form.value.password
                    });

                    // Stocker le token dans localStorage
                    localStorage.setItem('token', response.token);

                    // Rediriger vers le feed après connexion
                    router.push('/');
                } catch (error) {
                    console.error('Login failed:', error);
                    errorMessage.value = 'Invalid credentials. Please try again.';
                }
            } else {
                errorMessage.value = 'Please fix the errors in the form.';
            }
        };

        return {
            form,
            errors,
            errorMessage,
            handleSubmit
        };
    }
};
</script>


<style scoped>

</style>
