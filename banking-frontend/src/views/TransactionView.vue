<template>
    <div v-if="auth.user">

        <h1 class="text-2xl font-semibold mb-6 text-gray-800 text-center">Submit a Transaction</h1>
        <form class="max-w-md mx-auto" @submit.prevent="postTransaction">

            <div class="relative z-0 w-full mb-5 group">

                <label for="TransactionType" :class="selectLabelClass">Transaction Type</label>
                <select :class="selectInputClass" v-model="formData.transactionType" id="TransactionType">
                    <option value="TRANSFER">Transfer</option>
                    <option value="DEPOSIT">Deposit</option>
                    <option value="WITHDRAW">Withdraw</option>
                </select>
            </div>

            <div class="relative z-0 w-full mb-5 group">
                <DollarInput v-model="formData.amount" label="amount" id="amount"></DollarInput>
            </div>

            <div class="relative z-0 w-full mb-5 group">
                <input type="text" v-model="formData.description" id="description" name="description"
                    :class="[inputClass, { 'border-red-500': errors.description }]" placeholder=" ">
                <label for="description" :class="labelClass">Description: </label>
                <p class="text-red-500 text-sm mt-1">{{ errors.description }}</p>
            </div>

            <div class="relative z-0 w-full mb-5 group">
                <input type="text" v-model="formData.recipientUsername" id="toUsername" name="toUsername"
                    :disabled="formData.transactionType !== 'TRANSFER'"
                    :class="[inputClass, { hidden: formData.transactionType !== 'TRANSFER', 'border-red-500': errors.recipientUsername }]"
                    placeholder=" ">
                <label for="toUsername" :disabled="formData.transactionType !== 'TRANSFER'"
                    :class="[labelClass, { hidden: formData.transactionType !== 'TRANSFER' }]">Recipient Username:
                </label>
                <p class="text-red-500 text-sm mt-1">{{ errors.recipientUsername }}</p>

            </div>

            <button type="submit" :class="submitClass" :disabled="!isFormValid"> Submit! </button>
        </form>
        <div class="max-w-md mx-auto text-center mt-8">
            <p v-if="errorMessage" class="text-red-500">{{ errorMessage }}</p>
            <p v-if="successMessage" class="text-grey-500">{{ successMessage }}</p>
        </div>
    </div>
    <div v-else>
        Login to make transactions!
    </div>

</template>

<script setup>
import { useFormValidation } from '@/composables/useFormValidation';
import axios from "axios";
import { useAuthStore } from "@/stores/auth";
import { computed } from 'vue'
import { reactive, ref } from 'vue'
import DollarInput from '@/components/DollarInput.vue'


const auth = useAuthStore();

const formData = reactive({
    amount: 0,
    transactionType: "",
    description: "",
    recipientUsername: "",
});

// Request status from backend
const loading = ref(false);
const successMessage = ref("");
const errorMessage = ref("");

const rules = computed(() => ({
    transactionType: [],
    recipientUsername: formData.transactionType === 'TRANSFER' ?
        [
            val => (!val ? 'Recipient is required for transactions' : ''),
            val => (val.trim().length < 3 ? 'Recipient name is too short' : ''),
        ] : [],
    amount: [
        val => (!val ? 'Amount is required' : ''),
        val => (+val < 0.01 ? 'Amount must be greater than $0.01' : ''),
    ],
    description: [
        val => (!val ? 'Description is required' : ''),
        val => (val.trim().length < 3 ? 'Description is too short' : ''),
    ],
}));


const { errors, validate } = useFormValidation(formData, rules);



// Submit transaction
async function postTransaction() {
    if (!validate()) {
        console.log('Validation failed: ', errors);
        return;
    }

    const payload = {
        ...formData,
        amount: Math.round(parseFloat(formData.amount) * 100),
    };

    loading.value = true;
    successMessage.value = "";
    errorMessage.value = "";
    console.log("Transaction being posted");
    try {
        const response = await axios.post(
            "http://localhost:8081/api/transactions/transact",
            payload,
            { withCredentials: true });
        console.log(response.data);
        if (response.status === 200) {
            successMessage.value = "transaction posted successfully!";
        } else {
            errorMessage.value = "transaction failed.";
        }
    } catch (error) {
        if (error.response && error.response.status === 400) {
            errorMessage.value = error.response.data;
        } else {
            console.error("error submitting form: ", error);
            errorMessage.value = error.response.data;
        }
    }
    loading.value = false;
}

// Form validation





// Styling code:
const inputClass = "block py-2.5 px-0 w-full text-sm text-gray-800 bg-transparent border-0 border-b-2 border-gray-500 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer";
const labelClass = "peer-focus:font-medium absolute text-sm text-gray-800 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:start-0 rtl:peer-focus:translate-x-1/4 rtl:peer-focus:left-auto peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6";

const selectInputClass = "bg-gray-50 border border-gray-300 text-gray-800 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5";
const selectLabelClass = "block mb-2 text-sm font-medium text-gray-900";

const submitClass = "text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center disabled:cursor-not-allowed disabled:bg-gray-600";

const isFormValid = computed(() => {
    if (formData.transactionType === 'TRANSFER') {
        return formData.recipientUsername.trim().length > 0 &&
            formData.amount > 0 &&
            formData.description.trim().length > 0;
    } else {
        return formData.amount > 0 &&
            formData.description.trim().length > 0;

    }
})


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
</style>