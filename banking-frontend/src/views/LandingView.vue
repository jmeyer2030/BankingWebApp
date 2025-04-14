<template>
    <div class="mt-12 text-center">

    <div v-if="auth.user">

        <h2 class="text-3xl font-semibold text-gray-800 mb-4">Welcome {{ auth.user.username }}.</h2>
    </div>
    <div v-else>
        <h2 class="text-3xl font-semibold text-gray-800 mb-4">Welcome.</h2>
        <p>Sign in, or create an account to view banking features.</p>
    </div>
    </div>
</template>

<script setup>
import { useAuthStore } from "@/stores/auth";

const auth = useAuthStore();
</script>