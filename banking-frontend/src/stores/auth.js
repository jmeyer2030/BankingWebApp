import { defineStore } from 'pinia'
import axios from 'axios'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        user: null,
    }),
    getters: {
        isLoggedIn: (state) => !!state.user,
    },
    actions: {
        async login(userData) {
            this.user = userData
            localStorage.setItem('user', JSON.stringify(userData))
        },
        async logout() {
            this.user = null
            localStorage.removeItem('user')

            await axios.post('http://localhost:8081/logout', null, {
                withCredentials: true
            });
        },
        hydrate() {
            const saved = localStorage.getItem('user');
            if (saved) {
                this.user = JSON.parse(saved);
            }
        }
    }
})