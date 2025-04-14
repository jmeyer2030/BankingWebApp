<script setup>
import { ref, onMounted } from 'vue';
import { RouterLink, RouterView } from 'vue-router'
import navbar from './components/NavBar.vue'
import axios from 'axios';
import { useAuthStore } from './stores/auth';

const message = ref('');
const auth = useAuthStore();
auth.hydrate();


onMounted(() => {
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
        <navbar />
        <RouterView />
    </div>
</template>

<style scoped>
#serverStatus {
    float: right;
}
</style>
