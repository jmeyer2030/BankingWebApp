import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/AccountView.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'landing',
            component: () => import('../views/LandingView.vue'),
        },
        {
            path: '/account',
            name: 'account',
            component: () => import('../views/AccountView.vue'),
        },
        {
            path: '/about',
            name: 'about',
            component: () => import('../views/AboutView.vue'),
        },
        {
            path: '/login',
            name: 'login',
            component: () => import('../views/LoginView.vue'),
        },
        {
            path: '/signup',
            name: 'signup',
            component: () => import('../views/SignupView.vue'),
        },
        {
            path: '/account/logout',
            name: 'logout',
            component: () => import('../views/LogoutView.vue'),
        },
        {
            path: '/account/transaction',
            name: 'transaction',
            component: () => import('../views/TransactionView.vue'),
        },
        {
            path: '/account/history',
            name: 'history',
            component: () => import('../views/HistoryView.vue'),
        },
    ],
})

export default router
