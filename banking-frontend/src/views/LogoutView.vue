<template>
    <div class="mt-12 text-center">
        <div v-if="auth.user">
        <h2 class="text-3xl font-semibold text-gray-800 mb-4">Confirm logout:</h2>
            <button 
            class="px-4 py-1 text-lg font-medium text-white bg-red-600 rounded-lg border border-red-700 hover:bg-red-700 transition-colors"
            @click="logoutUser">Logout</button>
        </div>
        <div v-else>
            <h2 class="text-3xl font-semibold text-gray-800 mb-4">Log out successful!</h2>
            <router-link to="/login">Login</router-link>
        </div>
    </div>
</template>

<script setup>

import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { reactive, ref } from 'vue';

const auth = useAuthStore();

async function logoutUser() {
    await auth.logout();
}


</script>