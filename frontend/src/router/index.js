import { createRouter, createWebHistory } from 'vue-router';
import { isAuthenticated } from '@/service/userService';
import AppLayout from '@/layout/AppLayout.vue';
import { getUserProfile } from '@/service/apiService';
import { ref, onMounted } from 'vue';

const user = ref();

const fetchUserProfile = async () => {
    try {
        const response = await getUserProfile(localStorage.getItem('userId'));
        console.log("localstorage", localStorage.getItem('userId'));
        
        user.value = response;
    } catch (error) {
        console.error('Error fetching user profile:', error);
    }
};

onMounted(async () => {
    await fetchUserProfile();
});

const routes = [
    { path: '/auth/signup', name: 'signup', component: () => import('@/components/SignupPage.vue') },
    { path: '/auth/login', name: 'login', component: () => import('@/components/LoginPage.vue') },
    {
        path: '/',
        component: AppLayout,
        beforeEnter: (to, from, next) => {
            if (!isAuthenticated()) {
                next('/auth/login'); // Rediriger si non connecté
            } else {
                next();
            }
        },
        children: [
            { path: '/', name: 'homefeed', component: () => import('@/views/HomeFeed.vue') },
            { path: '/profile/:userId', name: 'ProfilePage', component: () => import('@/views/ProfilePage.vue'), props: true },
            { path: '/analyse', name: 'analyse', component: () => import('@/views/EmotionAnalyzer.vue') },
            { path: '/edit-profile', name: 'editProfile', component: () => import('@/views/EditProfile.vue') },
            { path: '/contact-us', name: 'contactUs', component: () => import('@/views/ContactUs.vue') },
            { path: '/my-posts', name: 'myPosts', component: () => import('@/views/MyPost.vue') },
            { path: '/lyrics-generator', name: 'lyricsGenerator', component: () => import('@/views/LyricsGenerator.vue') }
        ]
    },
    {
        path: '/admin',
        name: 'admin',
        component: () => import('@/views/Admin.vue'),
        meta: { requiresAuth: true },
        beforeEnter: async (to, from, next) => {
            await fetchUserProfile();
            console.log(user.value);
            console.log('localstorage', localStorage.getItem('userId'));
            if (user.value.role === 'admin') {
                next();
            } else {
                next('/');
            }
        }
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;
