import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'default',
      component: () => import('../views/DefaultView.vue'),
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView,
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
      path: '/transfer',
      name: 'transfer',
      component: () => import('../views/TransferView.vue'),
    },
    {
      path: '/deposit',
      name: 'deposit',
      component: () => import('../views/DepositView.vue'),
    },
    {
      path: '/withdraw',
      name: 'withdraw',
      component: () => import('../views/WithdrawView.vue'),
    },
  ],
})

export default router
