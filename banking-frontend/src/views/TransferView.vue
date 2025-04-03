<template>
    <h1>Transfer money:</h1>
    <form class="form-container" @submit.prevent="transferFunds">
        <label for="TransactionType">Choose Transaction Type</label>
        <select v-model="TransactionType" id="TransactionType">
            <option value="TRANSFER">Transfer</option>
            <option value="DEPOSIT">Deposit</option>
            <option value="Withdraw">Withdraw</option>
        </select>

        <label for="amount">Amount: </label>
        <input type="text" id="amount" name="amount">

        <label for="description">Description: </label>
        <input type="text" id="description" name="description">

        <label for="toUsername" :disabled="TransactionType !== 'TRANSFER'" :class="{ hidden: TransactionType !== 'TRANSFER' }">Recipient Username: </label>
        <input type="text" id="toUsername" name="toUsername" :disabled="TransactionType !== 'TRANSFER'" :class="{ hidden: TransactionType !== 'TRANSFER' }">

        <button type="submit">Submit Transaction!</button>
    </form>
</template>

<script>
import { reactive } from 'vue'
export default {
    data() {

        return {
            TransactionType: '',

            formData {
                amount = 0
                
            }
        }
    },
    methods: {
    }
}
</script>

<style>
.form-container {
    display: grid;
    /* Makes form container a grid container, allowing css grid layout */
    grid-template-columns: auto 1fr;
    /* Defines two columns, first "auto" (wide as needed). Second 1fr takes remaining space*/
    gap: 10px;
    /* 10 px between grid items, rows and cols */
    max-width: 500px;
    /* max form width of 300px */
}
.hidden {
  display: none;
}

</style>