<template>
    <form class="form-container" @submit.prevent="loginUser">
        <label for="username">Username: </label>
        <input type="text" id="username" name="username" v-model="formData.username">

        <label for="password">Password: </label>
        <input type="text" id="password" name="password" v-model="formData.password">

    </form>
    <button type="submit">Login!</button>
</template>

<script>
import axios from "axios";
export default {
    data() {
        return {
            formData: {
                username: "",
                password: "",
            },
            loading: false,
            message: "",
            errorMessages: [],
        };
    },
    methods: {
        async loginUser() {
            this.loading = true;
            this.message = "";
            this.errorMessages = [];

            try {
                const response = await axios.post("http://localhost:8081/auth/login", this.formData, {
                    headers: { "Content-Type": "application/json" },
                })

                if (response.status === 200) {
                    this.message = "Logged in successfully!";
                    const authKey = response.data;

                    console.log("Authentication key: " + authKey);
                } else {
                    this.message = "Login failed!"
                }

                console.log("Response", response.data)
            } catch (error) {
                if (error.response && error.response.status === 400) {
                    this.errorMessages = error.response.data;
                } else {
                    console.error("error submitting form: ", error);
                    this.message = "An unexpected error occurred. Please try again later.";
                }
            }

            this.loading = false;
        }

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
    max-width: 300px;
    /* max form width of 300px */
}
</style>