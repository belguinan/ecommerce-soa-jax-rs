<script setup>
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import PrimaryButton from '@/components/PrimaryButton.vue';
import { useFetch } from '@/composables/useFetch';
import { useSwal } from '@/composables/useSwal';
import { formatPhoneNumber, userApiEndpoint, setLocalStorage } from '@/assets/js/helpers.js';

const router = useRouter();
const { fetchJson } = useFetch();
const { errorAlert, successAlert } = useSwal();

const loading = ref(false);
const form = ref({
    username: '',
    name: '',
    email: '',
    password: '',
    passwordConfirmation: '',
    phoneNumber: '',
    address: '',
    city: ''
});

const errorResponse = ref({});

// Watch and format phone number
watch(() => form.value.phoneNumber, (newValue) => {
    if (newValue) {
        form.value.phoneNumber = formatPhoneNumber(newValue);
    }
});

const validateForm = () => {
    const errors = {};

    if (!form.value.name) {
        errors['name'] = 'Name is required';
    } else if (form.value.name.length < 2 || form.value.name.length > 199) {
        errors['name'] = 'Name must be between 2 and 199 characters';
    }

    const phoneDigits = form.value.phoneNumber.replace(/\D/g, '');
    if (!form.value.phoneNumber) {
        errors['phoneNumber'] = 'Phone number is required';
    } else if (phoneDigits.length !== 10) {
        errors['phoneNumber'] = 'Please enter a valid 10-digit phone number';
    }

    if (!form.value.email) {
        errors['email'] = 'Email is required';
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.value.email)) {
        errors['email'] = 'Please enter a valid email address';
    }

    if (!form.value.address) {
        errors['address'] = 'Address is required';
    }

    if (!form.value.city) {
        errors['city'] = 'City is required';
    }

    if (!form.value.username) {
        errors['username'] = 'Username is required';
    } else if (form.value.username.length < 4 || form.value.username.length > 20) {
        errors['username'] = 'Username must be between 4 and 20 characters';
    }

    if (!form.value.password) {
        errors['password'] = 'Password is required';
    } else if (form.value.password.length < 4 || form.value.password.length > 199) {
        errors['password'] = 'Password must be between 4 and 199 characters';
    }

    if (!form.value.passwordConfirmation) {
        errors['passwordConfirmation'] = 'Password confirmation is required';
    } else if (form.value.password !== form.value.passwordConfirmation) {
        errors['passwordConfirmation'] = 'Passwords do not match';
    }

    return errors;
};

const handleSignup = async () => {

    if (loading.value) return;

    const validationErrors = validateForm();
    if (Object.keys(validationErrors).length > 0) {
        errorResponse.value = { errors: validationErrors };
        return;
    }

    document.body.loader().show()
    loading.value = true;
    errorResponse.value = {};

    try {
        const formData = { ...form.value, phoneNumber: form.value.phoneNumber.replace(/\D/g, '')};
        delete formData.passwordConfirmation;

        const endpoint = userApiEndpoint('register');

        const response = await fetchJson(endpoint, {
            method: 'POST',
            body: formData
        });

        if (response?.id > 0) {
            setLocalStorage('user', response)
            localStorage.setItem('user', JSON.stringify(response));
            successAlert("Registration was successful").then(() => router.push({ name: 'login' }));
            return;
        }

        handleErrorResponse(response);
    } catch (err) {
        errorResponse.value.message = typeof err.message === 'string'
            ? err.message
            : 'Something went wrong!';
        errorAlert(errorResponse.value.message);
    } finally {
        document.body.loader().hide()
        loading.value = false;
    }
};

const handleErrorResponse = (response) => {
    if (!response.errors) return;
    errorResponse.value = response;
    errorResponse.value.message = response.message || 'Registration failed!';
}
</script>

<template>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-12 col-lg-9">
                <h2 class="mb-4 fw-light h4">Create Account</h2>

                <form @submit.prevent="handleSignup">
                    <!-- Personal Information Card -->

                    <div class="card bg-body border-secondary mb-4 rounded-4 p-3">
                        <div class="card-header bg-transparent border-0 pb-0">
                            <h5 class="mb-0 text-muted small fw-semibold">Your Personal Information</h5>
                        </div>
                        <div class="card-body">
                            <div class="row g-3">
                                <div class="col-12 col-md-6">
                                    <div class="form-floating" :class="{'is-invalid': errorResponse?.errors?.['name']}">
                                        <input
                                            v-model="form.name"
                                            type="text"
                                            class="form-control"
                                            :class="{'is-invalid': errorResponse?.errors?.['name']}"
                                            placeholder="Full Name"
                                            id="name"
                                        >
                                        <label>Full Name</label>
                                        <div v-if="errorResponse?.errors?.['name']" class="invalid-feedback">
                                            {{ errorResponse.errors['name'] }}
                                        </div>
                                    </div>
                                </div>

                                <div class="col-12 col-md-6">
                                    <div class="form-floating" :class="{'is-invalid': errorResponse?.errors?.['phoneNumber']}">
                                        <input
                                            v-model="form.phoneNumber"
                                            type="tel"
                                            class="form-control"
                                            :class="{'is-invalid': errorResponse?.errors?.['phoneNumber']}"
                                            placeholder="Phone Number"
                                            id="phoneNumber"
                                        >
                                        <label>Phone Number</label>
                                        <div v-if="errorResponse?.errors?.['phoneNumber']" class="invalid-feedback">
                                            {{ errorResponse.errors['phoneNumber'] }}
                                        </div>
                                    </div>
                                </div>

                                <div class="col-12">
                                    <div class="form-floating" :class="{'is-invalid': errorResponse?.errors?.['email']}">
                                        <input
                                            v-model="form.email"
                                            type="email"
                                            class="form-control"
                                            :class="{'is-invalid': errorResponse?.errors?.['email']}"
                                            placeholder="Email"
                                            id="email"
                                        >
                                        <label>Email</label>
                                        <div v-if="errorResponse?.errors?.['email']" class="invalid-feedback">
                                            {{ errorResponse.errors['email'] }}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Address Card -->
                    <div class="card bg-body border-secondary mb-4 rounded-4 p-3">
                        <div class="card-header bg-transparent border-0 pb-0">
                            <h5 class="mb-0 text-muted small fw-semibold">Your Address</h5>
                        </div>
                        <div class="card-body">
                            <div class="row g-3">
                                <div class="col-12">
                                    <div class="form-floating" :class="{'is-invalid': errorResponse?.errors?.['address']}">
                                        <input
                                            v-model="form.address"
                                            type="text"
                                            class="form-control"
                                            :class="{'is-invalid': errorResponse?.errors?.['address']}"
                                            placeholder="Address"
                                            id="address"
                                        >
                                        <label>Address</label>
                                        <div v-if="errorResponse?.errors?.['address']" class="invalid-feedback">
                                            {{ errorResponse.errors['address'] }}
                                        </div>
                                    </div>
                                </div>

                                <div class="col-12">
                                    <div class="form-floating" :class="{'is-invalid': errorResponse?.errors?.['city']}">
                                        <input
                                            v-model="form.city"
                                            type="text"
                                            class="form-control"
                                            :class="{'is-invalid': errorResponse?.errors?.['city']}"
                                            placeholder="City"
                                            id="city"
                                        >
                                        <label>City</label>
                                        <div v-if="errorResponse?.errors?.['city']" class="invalid-feedback">
                                            {{ errorResponse.errors['city'] }}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Account Credentials Card -->
                    <div class="card bg-body border-secondary mb-4 rounded-4 p-3">
                        <div class="card-header bg-transparent border-0 pb-0">
                            <h5 class="mb-0 text-muted small fw-semibold">Your Account Credentials</h5>
                        </div>
                        <div class="card-body">
                            <div class="row g-3">
                                <div class="col-12">
                                    <div class="form-floating" :class="{'is-invalid': errorResponse?.errors?.['username']}">
                                        <input
                                            v-model="form.username"
                                            type="text"
                                            class="form-control"
                                            :class="{'is-invalid': errorResponse?.errors?.['username']}"
                                            placeholder="Username"
                                            id="username"
                                        >
                                        <label>Username</label>
                                        <div v-if="errorResponse?.errors?.['username']" class="invalid-feedback">
                                            {{ errorResponse.errors['username'] }}
                                        </div>
                                    </div>
                                </div>

                                <div class="col-12 col-md-6">
                                    <div class="form-floating" :class="{'is-invalid': errorResponse?.errors?.['password']}">
                                        <input
                                            v-model="form.password"
                                            type="password"
                                            class="form-control"
                                            :class="{'is-invalid': errorResponse?.errors?.['password']}"
                                            placeholder="Password"
                                            id="password"
                                        >
                                        <label>Password</label>
                                        <div v-if="errorResponse?.errors?.['password']" class="invalid-feedback">
                                            {{ errorResponse.errors['password'] }}
                                        </div>
                                    </div>
                                </div>

                                <div class="col-12 col-md-6">
                                    <div class="form-floating" :class="{'is-invalid': errorResponse?.errors?.['passwordConfirmation']}">
                                        <input
                                            v-model="form.passwordConfirmation"
                                            type="password"
                                            class="form-control"
                                            :class="{'is-invalid': errorResponse?.errors?.['passwordConfirmation']}"
                                            placeholder="Confirm Password"
                                            id="passwordConfirmation"
                                        >
                                        <label>Confirm Password</label>
                                        <div v-if="errorResponse?.errors?.['passwordConfirmation']" class="invalid-feedback">
                                            {{ errorResponse.errors['passwordConfirmation'] }}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Submit Button -->
                    <div class="d-grid gap-2">
                        <PrimaryButton
                            class="btn-dark text-uppercase py-3"
                            :class="{'opacity-25': loading}"
                            :disabled="loading"
                        >
                            {{ loading ? 'Creating Account...' : 'Sign Up' }}
                        </PrimaryButton>
                    </div>

                    <!-- Login Link -->
                    <div class="text-center mt-4">
                        <p class="mb-0 fw-semibold">
                            Already have an account?
                            <router-link :to="{ name: 'login' }" class="text-decoration-none">
                                Login here
                            </router-link>
                        </p>
                    </div>

                    <div v-if="errorResponse?.message" class="alert alert-danger small text-center mt-4 mb-0">
                        {{ errorResponse.message }}
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>