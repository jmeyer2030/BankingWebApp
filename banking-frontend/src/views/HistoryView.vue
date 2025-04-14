<template>
    <div class="text-center mt-6 max-w-7xl mx-auto mb-12">
        <h2 class="text-xl font-semibold text-gray-800 mb-12">Full Transaction History</h2>
        <RecentTransactionsTable :transactions="transactions" />
        <p class="text-sm">Showing page {{ currentPage }} of {{ totalPages }}</p>

        <button v-if="currentPage > 1" @click="prevPage">Previous Page</button>
        &nbsp;
        <button v-if="currentPage < totalPages" @click="nextPage">Next Page</button>
        <div>
            <label for="pageSize" class="text-sm">Page size </label>
            <select id="pageSize" class="border border-gray-300 rounded px-2 py-1 text-sm" v-model="pageSize"
                @change="changePageSize">
                <option :value="5">5</option>
                <option :value="10">10</option>
                <option :value="20">20</option>
                <option :value="50">50</option>
            </select>
        </div>
    </div>



</template>

<script setup>
import RecentTransactionsTable from '@/components/RecentTransactionsTable.vue';
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useAuthStore } from '@/stores/auth';

const auth = useAuthStore();
const transactions = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);
const totalPages = ref(0);
const loading = ref(true);

onMounted(() => {
    fetchTransactions();
})




async function fetchTransactions() {
    loading.value = true;
    try {
        const response = await axios.get("http://localhost:8081/api/transactions/history", {
            params: {
                accountId: auth.user.accountId,
                page: currentPage.value - 1,
                size: pageSize.value,
            },
            withCredentials: true,
        });
        console.log("response: ", response);
        console.log("response.data: ", response.data);

        if (response.data.success) {
            transactions.value = response.data.data.content;
            totalPages.value = response.data.data.totalPages;
        } else {
            console.error("Api error: ", response.data.message);
        }

    } catch (error) {
        console.error("error with the request: ", error);
    }


    loading.value = false;
}

async function changePageSize() {
    currentPage.value = 1;
    fetchTransactions();
}

async function nextPage() {
    if (currentPage.value < totalPages.value) {
        currentPage.value = currentPage.value + 1;
        fetchTransactions();
    }
}

async function prevPage() {
    if (currentPage.value > 1) {
        currentPage.value = currentPage.value - 1;
        fetchTransactions();
    }
}

</script>