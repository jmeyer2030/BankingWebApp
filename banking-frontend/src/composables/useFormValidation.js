import { reactive, isRef, unref } from 'vue';

export function useFormValidation(formData, rules) {
    const errors = reactive({});

    const getRules = () => {
        return isRef(rules) ? rules.value : rules;
    };

    const validate = () => {
        const currentRules = getRules();
        let isValid = true;

        for (const field in currentRules) {
            const value = formData[field];
            const fieldRules = currentRules[field];
            errors[field] = '';

            for (const rule of fieldRules) {
                const errorMsg = rule(value);
                if (errorMsg) {
                    errors[field] = errorMsg;
                    isValid = false;
                    break; // stop on first error for this field
                }
            }
        }

        return isValid;
    };

    const resetErrors = () => {
        for (const field in errors) {
            errors[field] = '';
        }
    };

    return { errors, validate, resetErrors };
}
