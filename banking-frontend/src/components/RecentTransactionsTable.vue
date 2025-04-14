<template>
    <table
        class="transactions-table table-fixed min-w-full divide-y divide-gray-200 shadow-sm rounded-lg overflow-hidden">
        <thead class="bg-gray-100 text-gray-700 text-sm font-semibold uppercase tracking-wide">
            <tr class="divide-x divide-gray-300">
                <th class="px-4 py-3 text-left">ID</th>
                <th class="px-4 py-3 text-left">Date</th>
                <th class="px-4 py-3 text-left">Type</th>
                <th class="px-4 py-3 text-left">Sender</th>
                <th class="px-4 py-3 text-left">Recipient</th>
                <th class="px-4 py-3 text-left">Amount</th>
                <th class="px-4 py-3 text-left">Description</th>
            </tr>
        </thead>
        <tbody class="divide-y divide-gray-200 text-sm text-gray-800">
            <tr v-for="tx in sortedTransactions" :key="tx.id"
                class="hover:bg-gray-50 transition-colors duration-150 divide-x divide-gray-300">
                <td class="px-4 py-2">{{ tx.id }}</td>
                <td class="px-4 py-2">{{ new Date(tx.timestamp).toLocaleDateString('en-US', {
                    month: 'short', day: 'numeric',
                    year: 'numeric' }) }}</td>

                <td class="px-4 py-2 capitalize">{{ tx.transactionType }}</td>
                <td class="px-4 py-2">{{ tx.senderUsername }}</td>
                <td class="px-4 py-2">{{ tx.transactionType === 'transfer' ? tx.recipientUsername : tx.senderUsername }}
                </td>
                <td class="px-4 py-2"> ${{ (tx.amount / 100).toFixed(2) }} </td>
                <td class="px-4 py-2">{{ tx.description }}</td>
            </tr>
        </tbody>
    </table>
</template>

<script setup>
import { computed } from 'vue'


const props = defineProps({
    transactions: {
        type: Array,
        required: true
    }
})

// Sort most recent first
const sortedTransactions = computed(() =>
    [...props.transactions].sort(
        (a, b) => new Date(b.timestamp) - new Date(a.timestamp)
    )
)
</script>
