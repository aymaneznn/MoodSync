import { createRouter, createWebHistory } from 'vue-router';
import { isAuthenticated } from '@/service/userService';
import AppLayout from '@/layout/AppLayout.vue';

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
            { path: '/analyse', name: 'analyse', component: () => import('@/views/EmotionAnalyzer.vue') }
        ]
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;
