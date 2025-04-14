<template>
    <div class="mt-12">
        <h1 class="text-2xl font-semibold mb-6 text-gray-800 text-center">Create an Account</h1>
        <form class="max-w-md mx-auto" @submit.prevent="registerUser">
            <div class="relative z-0 w-full mb-5 group">
                <input type="text" id="username" name="username" v-model="formData.username" :class="inputClass"
                    placeholder=" " minlength="3">
                <label for="username" :class="labelClass">Username: </label>
            </div>
            <div class="relative z-0 w-full mb-5 group">
                <input type="text" id="password" name="password" v-model="formData.password" :class="inputClass"
                    placeholder=" ">
                <label for="password" :class="labelClass">Password: </label>
            </div>
            <div class="relative z-0 w-full mb-5 group">
                <input type="text" id="email" name="email" v-model="formData.email" :class="inputClass" placeholder=" ">
                <label for="email" :class="labelClass">Email: </label>
            </div>
            <div class="grid md:grid-cols-2 md:gap-6">
                <div class="relative z-0 w-full mb-5 group">
                    <input type="text" id="firstname" name="firstname" v-model="formData.firstname" :class="inputClass"
                        placeholder=" ">
                    <label for="firstname" :class="labelClass">First Name: </label>
                </div>

                <div class="relative z-0 w-full mb-5 group">
                    <input type="text" id="lastname" name="lastname" v-model="formData.lastname" :class="inputClass"
                        placeholder=" ">
                    <label for="lastname" :class="labelClass">Last Name: </label>
                </div>
            </div>
            <button type="submit" :class="submitClass" :disabled="!isFormValid"> Submit! </button>
        </form>

        <div class="text-center mt-8">
            <div v-if="loading">
                <p>Submitting... Please wait.</p>
            </div>
            <div v-else>
                <p v-if="message">{{ message }}</p>
                <div v-if="errorMessages.length">
                    <b>Issues in form submission, fix the following errors:</b>
                    <ul>
                        <li v-for="(error, index) in errorMessages" :key="index">{{ error }}</li>
                    </ul>
                </div>
            </div>
        </div>

    </div>

</template>

<script setup>
import { ref, reactive } from 'vue'
import axios from 'axios'
import { computed } from 'vue'

const isFormValid = computed(() =>
    formData.username.trim() &&
    formData.password.trim() &&
    formData.firstname.trim() &&
    formData.lastname.trim() &&
    formData.email.trim()
)

// Reactive form state
const formData = reactive({
    username: '',
    password: '',
    firstname: '',
    lastname: '',
    email: '',
})

// Styling classes
const inputClass = "block py-2.5 px-0 w-full text-sm text-gray-800 bg-transparent border-0 border-b-2 border-gray-500 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer";
const labelClass = "peer-focus:font-medium absolute text-sm text-gray-800 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:start-0 rtl:peer-focus:translate-x-1/4 rtl:peer-focus:left-auto peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6";

const submitClass = "text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center disabled:cursor-not-allowed disabled:bg-gray-600";


// Other UI state
const loading = ref(false)
const message = ref('')
const errorMessages = ref([])

async function registerUser() {
    loading.value = true
    message.value = ''
    errorMessages.value = []

    try {
        const response = await axios.post(
            'http://localhost:8081/api/forms/register',
            formData,
            { headers: { 'Content-Type': 'application/json' } }
        )

        if (response.status === 200) {
            message.value = 'Account created successfully!'
        } else {
            message.value = 'Account creation failed!'
        }

        console.log('Response:', response.data)
    } catch (error) {
        if (error.response && error.response.status === 400) {
            errorMessages.value = error.response.data
        } else {
            console.error('Error submitting form:', error)
            message.value = 'An unexpected error occurred. Please try again later.'
        }
    }

    loading.value = false
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
    max-width: 300px;
    /* max form width of 300px */

}
</style>