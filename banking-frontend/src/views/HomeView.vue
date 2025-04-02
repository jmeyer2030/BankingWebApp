<script setup>
import axios from "axios";
import { onMounted, ref } from "vue";

const accountInfo = ref(null);
const loading = ref(true);
const tokenValid = ref(false);
const message = ref("");

onMounted(() => {
  loading.value = true;
  axios.post("http://localhost:8081/api/account", {}, { withCredentials: true })
    .then(response => {
      console.log("Account info:", response.data);
      accountInfo.value = response.data;
      tokenValid.value = true;
      // You can update your reactive data here
    })
    .catch(error => {
      tokenValid.value = false;
      message.value = error.message;
      console.error("Failed to fetch account info:", error.response?.data || error.message);
      // Handle auth failure, maybe redirect or show a message
    });
    loading.value = false;
});
</script>

<template>
  <div v-if="loading">
    <h1>Awaiting server response. Please wait.</h1>
  </div>
  <div v-else>
    <div v-if="tokenValid">
      <h1> Welcome {{ accountInfo.username }}</h1>
      <h2> Account Balance: ${{ accountInfo.balance / 100 }}.{{ accountInfo.balance % 100 }}{{ accountInfo.balance % 10 }}</h2>
      <h2> Account Type: {{ accountInfo.accountType }}</h2>
    </div>
    <div v-else>
      <h1>Session error!</h1>
      <p>{{ message }}</p>
      <p>Please login, or relog in to generate</p>
    </div>
  </div>
  <!--
  <div>
    <h1> Welcome user!</h1>
    <h2> Transfer, Deposit, or Withdraw </h2>
    <h2> balance: </h2>
    <h2> Transactions: </h2>
  </div>
-->
</template>
