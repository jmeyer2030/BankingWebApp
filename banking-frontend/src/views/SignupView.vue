<template>
    <h1> Fill out the form then press submit: </h1>
    <form class="form-container" @submit.prevent="registerUser">
        <label for="username">Username: </label>
        <input type="text" id="username" name="username" v-model="formData.username">

        <label for="password">Password: </label>
        <input type="text" id="password" name="password" v-model="formData.password">

        <label for="firstname">First Name: </label>
        <input type="text" id="firstname" name="firstname" v-model="formData.firstname">

        <label for="lastname">Last Name: </label>
        <input type="text" id="lastname" name="lastname" v-model="formData.lastname">

        <label for="email">Email: </label>
        <input type="text" id="email" name="email" v-model="formData.email">

        <button type="submit"> Submit! </button>
    </form>

    <p v-if="loading">Submitting... Please wait.</p>
    <p v-if="message">{{ message }}</p>
    <ul v-if="errorMessages.length">
        <li v-for="(error, index) in errorMessages" :key="index">{{ error }}</li>
    </ul>
</template>

<script>
import axios from "axios";
export default {
    data() {
        return {
            formData: {
                username: "",
                password: "",
                firstname: "",
                lastname: "",
                email: "",
            },
            loading: false,
            message: "",
            errorMessages: [],
        };
    },
    methods: {
        async registerUser() {
            this.loading = true;
            this.message = "";
            this.errorMessages = [];

            try {
                const response = await axios.post("http://localhost:8081/api/forms/register", this.formData, {
                    headers: { "Content-Type": "application/json"},
                })
                
                if (response.status === 200) {
                    this.message = "Account created successfully!"
                } else {
                    this.message = "Account creation failed!";
                }

                console.log("Response:", response.data);
            } catch (error) {
                if (error.response && error.response.status === 400) {
                    this.errorMessages = error.response.data;
                } else {
                    console.error("Error submitting form: ", error);
                    this.message = "An unexpected error occurred. Please try again later.";
                }
            }

            this.loading = false;
        }
    }
};



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