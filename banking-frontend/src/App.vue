<script setup>
import { ref, onMounted } from 'vue';
import { RouterLink, RouterView } from 'vue-router'
import navbar from './components/NavBar.vue'
import HelloWorld from './components/HelloWorld.vue'
import axios from 'axios';

const message = ref('');

onMounted(() => {
  //console.log("Making request to the backend...");
  axios.get('http://localhost:8081/api/home')
    .then(response => {
      message.value = response.data;
    })
    .catch(error => {
      console.error("Error fetching data:", error)
    });
});

</script>

<template>
  <div>
    <h1 v-if="message">{{ message }}</h1>
    <h1 v-else> Server status: Offline</h1>
    <h2> test h2! </h2>
    <navbar />
    <RouterView />
  </div>
</template>

<style scoped></style>
