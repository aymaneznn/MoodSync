<script setup>
import { ref } from 'vue';
import { logout } from '@/service/userService';
import { useRouter } from 'vue-router';

import AppMenuItem from './AppMenuItem.vue';

const router = useRouter();

const model = ref([
    {
        label: 'Home',
        items: [
            { label: 'Feed', icon: 'pi pi-fw pi-home', to: '/' },
            { label: 'Messages', icon: 'pi pi-fw pi-inbox', to: '/messages' },
            { label: 'My Posts', icon: 'pi pi-fw pi-pencil', to: '/my-posts' },
            { label: 'My Likes', icon: 'pi pi-fw pi-heart', to: '/my-likes' },
        ]
    },
    {
        label: 'Settings',
        items: [
            { label: 'Account', icon: 'pi pi-fw pi-user-edit', to: '/edit-profile' },
            { label: 'Privacy', icon: 'pi pi-fw pi-lock', to: '/settings/privacy' },
        ]
    },
    {
        label: 'Support',
        items: [
            { label: 'Help Center', icon: 'pi pi-fw pi-question-circle', to: '/support/help-center' },
            { label: 'Contact Us', icon: 'pi pi-fw pi-envelope', to: '/support/contact' },
        ]
    },
    {
        label: 'Other',
        items: [
            { label: 'Recomendations', icon: 'pi pi-fw pi-chart-line', to: '/analyse' },
            { 
                label: 'Logout', 
                icon: 'pi pi-fw pi-power-off', 
                to: '/auth/login',
                action: () => {
                    logout(); 
                    router.push('/auth/login');
                }
            },
        ]
    },
]);
</script>

<template>
    <ul class="layout-menu">
        <template v-for="(item, i) in model" :key="i">
            <app-menu-item 
                v-if="!item.separator" 
                :item="item" 
                :index="i"
                @click="item.action && item.action()"
            />
            <li v-if="item.separator" class="menu-separator"></li>
        </template>
    </ul>
</template>

<style lang="scss" scoped></style>
