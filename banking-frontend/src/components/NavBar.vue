<script setup>
import { useAuthStore } from "@/stores/auth";

const auth = useAuthStore();
console.log(auth.user);

</script>

<template>
  <nav class="bg-white shadow-md px-6 py-4 flex items-center justify-between">
    <!-- Left side -->
    <div class="flex items-center space-x-8">
      <div class="text-xl font-semibold text-blue-600">Banking Web App</div>

      <div class="hidden md:flex space-x-4" v-if="auth.user">
        <router-link to="/account" class="text-gray-700 hover:text-blue-600 transition">
          Account
        </router-link>
        <router-link to="/account/transaction" class="text-gray-700 hover:text-blue-600 transition">
          Transaction
        </router-link>
        <router-link to="/account/history" class="text-gray-700 hover:text-blue-600 transition">
          History
        </router-link>
      </div>
    </div>

    <!-- Right side -->
    <div class="flex items-center space-x-4">
      <div v-if="auth.user" class="hidden md:flex space-x-4">
        <router-link to="/account/logout" class="text-gray-700 hover:text-red-600 transition">
          Logout
        </router-link>
      </div>
      <div v-else class="hidden md:flex space-x-4">
        <router-link to="/signup" class="text-gray-700 hover:text-blue-600 transition"> Signup
        </router-link>
        <router-link to="/login" class="text-gray-700 hover:text-blue-600 transition">
          Login
        </router-link>
      </div>
      <router-link to="/about" class="text-gray-700 hover:text-blue-600 transition hidden md:block">
        About
      </router-link>
    </div>
  </nav>

</template>

<style></style>

<!--<nav class="navbar">
        <div class="left">
            <div class="site-name"> Banking Web App</div>
            <div class="nav-links" v-if="auth.user">
                <router-link to="/account">Account</router-link>
                <router-link to="/account/transaction">Transaction</router-link>
                <router-link to="/account/history">History</router-link>
            </div>
        </div>

        <div class="right">
            <div class="nav-links" v-if="auth.user">
                <router-link to="/account/logout">Logout</router-link>
            </div>
            <div class="nav-links" v-else>
                <router-link to="/signup">Signup</router-link>
                <router-link to="/login">Login</router-link>
            </div>
            <router-link to="/about">About</router-link>
        </div>
    </nav> -->
