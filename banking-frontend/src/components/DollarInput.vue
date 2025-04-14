<template>
    <div class="relative z-0 w-full mb-5 group">
        <span class="absolute left-0 top-2.5 text-gray-500 text-sm 
                    transition-opacity duration-200 pointer-events-none" :class="{
                        'opacity-0': !modelValue && !isAmountFocused,
                        'opacity-100': modelValue || isAmountFocused
                    }">$</span>

        <input type="number" :id="id" :name="id" :class="inputClass + ' pl-4'" placeholder=" " step="0.01" min="0.01"
            @input="handleInput" @focus="isAmountFocused = true"
            @blur="(e) => { isAmountFocused = false; formatAmount(e); }">
        <label for="amount" :class="labelClass">Amount</label>
    </div>
</template>


<script setup>
import { ref, reactive } from 'vue'

const inputClass = "block py-2.5 px-0 w-full text-sm text-gray-800 bg-transparent border-0 border-b-2 border-gray-500 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer";
const labelClass = "peer-focus:font-medium absolute text-sm text-gray-800 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:start-0 rtl:peer-focus:translate-x-1/4 rtl:peer-focus:left-auto peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6";

const props = defineProps({
    modelValue: [String, Number],
    label: String,
    id: {
        type: String,
        default: 'amount'
    }
})
const emit = defineEmits(['update:modelValue'])


// when true, should show $
const isAmountFocused = ref(false);

function handleInput(event) {
    // Limit decimal to two points
    let value = event.target.value;
    const [intPart, decimalPart] = value.split('.');

    if (decimalPart && decimalPart.length > 2) {
        value = `${intPart}.${decimalPart.slice(0, 2)}`;
        event.target.value = value;
    }

    // Emit
    emit('update:modelValue', value);
}

// Formats amount on blur
function formatAmount(event) {
    let value = event.target.value;

    if (!value) return;

    const parsed = parseFloat(value);
    if (isNaN(parsed)) {
        emit('update:modelValue', '');
        event.target.value = '';
        return;
    }

    const formatted = parsed.toFixed(2); // forces two decimal places
    event.target.value = formatted;
    emit('update:modelValue', formatted);
}

</script>