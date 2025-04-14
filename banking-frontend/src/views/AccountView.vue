<template>
    <div class="mt-12 text-center">

        <h2 class="text-2xl font-semibold text-gray-800 mb-4">Account Overview</h2>
        <div v-if="loading">
            <h1>Awaiting server response. Please wait.</h1>
        </div>
        <div v-else>
            <div v-if="auth.user">
                <h1 class="text-xl font-semibold text-gray-800"> Welcome, {{ auth.user.username }}.</h1>
                <h1 class="text-xl font-semibold text-gray-800"> Your current balance is: ${{ (auth.user.balance /
                    100).toFixed(2) }} </h1>

                <div class="mt-12 max-w-7xl mx-auto">
                    <h2 class="text-xl font-semibold text-gray-800 mb-4">Recent Transactions</h2>
                    <RecentTransactionsTable :transactions="auth.user.recentTransactions" />
                </div>

            </div>
            <div v-else>
                <h1>Session has expired, or you need to start a session.</h1>
                <p>{{ message }}</p>
                <p>Please login, or relog in to generate</p>
            </div>
        </div>
    </div>
</template>

<script setup>
import RecentTransactionsTable from "@/components/RecentTransactionsTable.vue";
import axios from "axios";
import { onMounted, ref } from "vue";
import { useAuthStore } from "@/stores/auth";

const auth = useAuthStore();
const loading = ref(true);
const message = ref("");

onMounted(() => {
    axios.post("http://localhost:8081/api/account", {}, { withCredentials: true })
        .then(response => {
            console.log("Account info:", response.data);

            auth.login(response.data);

            loading.value = false;
        })
        .catch(error => {
            message.value = error.message;
            console.error("Failed to fetch account info:", error.response?.data || error.message);

            auth.logout();

            loading.value = false;
        });
});
</script>

<style></style>
