<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import PrimaryButton from '@/components/PrimaryButton.vue'
import { useFetch } from '@/composables/useFetch'
import { useSwal } from '@/composables/useSwal'
import { formatPhoneNumber, userApiEndpoint, setLocalStorage } from '@/assets/js/helpers'

const router = useRouter()
const { fetchJson } = useFetch()
const { errorAlert, successAlert } = useSwal()

const loading = ref(false)
const form = ref({
    username: '',
    name: '',
    email: '',
    password: '',
    passwordConfirmation: '',
    phoneNumber: '',
    address: '',
    city: ''
})

const errorResponse = ref({})

watch(() => form.value.phoneNumber, (newValue) => {
    if (newValue) {
        form.value.phoneNumber = formatPhoneNumber(newValue)
    }
})

const validateForm = () => {
    const errors = {}

    if (!form.value.name) {
        errors['name'] = 'Name is required'
    } else if (form.value.name.length < 2 || form.value.name.length > 199) {
        errors['name'] = 'Name must be between 2 and 199 characters'
    }

    const phoneDigits = form.value.phoneNumber.replace(/\D/g, '')
    if (!form.value.phoneNumber) {
        errors['phoneNumber'] = 'Phone number is required'
    } else if (phoneDigits.length !== 10) {
        errors['phoneNumber'] = 'Please enter a valid 10-digit phone number'
    }

    if (!form.value.address) {
        errors['address'] = 'Address is required'
    }

    if (!form.value.city) {
        errors['city'] = 'City is required'
    }

    if (form.value.password) {
        if (form.value.password.length < 4 || form.value.password.length > 199) {
            errors['password'] = 'Password must be between 4 and 199 characters'
        }

        if (!form.value.passwordConfirmation) {
            errors['passwordConfirmation'] = 'Password confirmation is required'
        } else if (form.value.password !== form.value.passwordConfirmation) {
            errors['passwordConfirmation'] = 'Passwords do not match'
        }
    }

    return errors
}

const handleUpdate = async () => {
    if (loading.value) return

    const validationErrors = validateForm()

    if (Object.keys(validationErrors).length > 0) {
        errorResponse.value = { errors: validationErrors }
        return
    }

    document.body.loader().show()
    loading.value = true
    errorResponse.value = {}

    try {
        const formData = { ...form.value, phoneNumber: form.value.phoneNumber.replace(/\D/g, '') }

        if (!formData.password) {
            delete formData.password
        }

        delete formData.passwordConfirmation
        delete formData.email
        delete formData.username

        const endpoint = userApiEndpoint('profile')
        const response = await fetchJson(endpoint, {
            method: 'PUT',
            body: formData
        })

        if (response?.id > 0) {
            setLocalStorage('user', response)
            successAlert('Profile updated successfully')
            router.back()
            return
        }

        errorResponse.value = response
        errorResponse.value.message = response.message || 'Update failed!'

    } catch (err) {
        errorResponse.value.message = typeof err.message === 'string'
            ? err.message
            : 'Something went wrong!'
        errorAlert(errorResponse.value.message)
    } finally {
        document.body.loader().hide()
        loading.value = false
    }
}

const loadUserProfile = async () => {
    try {
        const endpoint = userApiEndpoint('profile')
        const response = await fetchJson(endpoint)

        form.value = {
            ...response,
            password: '',
            passwordConfirmation: '',
            phoneNumber: formatPhoneNumber(response.phoneNumber)
        }

        setLocalStorage('user', response)

    } catch (err) {
        errorAlert('Failed to load profile data')
        router.push({ name: 'home' })
    }
}

onMounted(() => {
    loadUserProfile()
})
</script>

<template>

    <div class="container py-4 min-vh-100">

        <div class="row justify-content-center">
            <div class="col-12 col-lg-8">
                <div class="d-flex align-items-center mb-4">
                    <button
                        class="btn btn-link text-dark p-0 me-3"
                        @click="router.back()"
                    >
                        <i class="bi bi-arrow-left fs-4"></i>
                    </button>
                    <h2 class="mb-0 fw-light h4">Edit Profile</h2>
                </div>

                <form @submit.prevent="handleUpdate">
                    <div class="card bg-white border-0 shadow-sm rounded-4 p-2 mb-4">
                        <div class="card-header bg-transparent border-0 pb-0">
                            <h5 class="mb-0 text-muted small fw-semibold">Your Personal Information</h5>
                        </div>
                        <div class="card-body">
                            <div class="row g-3">
                                <div class="col-12">
                                    <div class="form-floating" :class="{'is-invalid': errorResponse?.errors?.['name']}">
                                        <input
                                            v-model="form.name"
                                            type="text"
                                            class="form-control bg-light"
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

                                <div class="col-12">
                                    <div class="form-floating">
                                        <input
                                            v-model="form.phoneNumber"
                                            type="tel"
                                            class="form-control bg-light"
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
                                    <div class="form-floating">
                                        <input
                                            v-model="form.email"
                                            type="email"
                                            class="form-control bg-light"
                                            disabled
                                            placeholder="Email"
                                        >
                                        <label>Email</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="card bg-white border-0 shadow-sm rounded-4 p-2 mb-4">
                        <div class="card-header bg-transparent border-0 pb-0">
                            <h5 class="mb-0 text-muted small fw-semibold">Your Address</h5>
                        </div>
                        <div class="card-body">
                            <div class="row g-3">
                                <div class="col-12">
                                    <div class="form-floating">
                                        <input
                                            v-model="form.address"
                                            type="text"
                                            class="form-control bg-light"
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
                                    <div class="form-floating">
                                        <input
                                            v-model="form.city"
                                            type="text"
                                            class="form-control bg-light"
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

                    <div class="card bg-white border-0 shadow-sm rounded-4 p-2 mb-4">
                        <div class="card-header bg-transparent border-0 pb-0">
                            <h5 class="mb-0 text-muted small fw-semibold">Change Password</h5>
                        </div>
                        <div class="card-body">
                            <div class="row g-3">
                                <div class="col-12 col-md-6">
                                    <div class="form-floating">
                                        <input
                                            v-model="form.password"
                                            type="password"
                                            class="form-control bg-light"
                                            :class="{'is-invalid': errorResponse?.errors?.['password']}"
                                            placeholder="New Password"
                                            id="password"
                                        >
                                        <label>New Password</label>
                                        <div v-if="errorResponse?.errors?.['password']" class="invalid-feedback">
                                            {{ errorResponse.errors['password'] }}
                                        </div>
                                    </div>
                                </div>

                                <div class="col-12 col-md-6">
                                    <div class="form-floating">
                                        <input
                                            v-model="form.passwordConfirmation"
                                            type="password"
                                            class="form-control bg-light"
                                            :class="{'is-invalid': errorResponse?.errors?.['passwordConfirmation']}"
                                            placeholder="Confirm New Password"
                                            id="passwordConfirmation"
                                        >
                                        <label>Confirm New Password</label>
                                        <div v-if="errorResponse?.errors?.['passwordConfirmation']" class="invalid-feedback">
                                            {{ errorResponse.errors['passwordConfirmation'] }}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>


                    <div class="gap-2 text-end">
                        <div class="btn-group">
                            <button
                                type="button"
                                class="btn bg-dark-subtle px-4 fw-semibold"
                                @click="router.back()"
                            >
                                Cancel
                            </button>
                            <PrimaryButton
                                class="btn btn-dark px-4 fw-semibold"
                                :class="{'opacity-25': loading}"
                                :disabled="loading"
                            >
                                {{ loading ? 'Updating Profile...' : 'Update Profile' }}
                            </PrimaryButton>
                        </div>
                    </div>

                    <div v-if="errorResponse?.message" class="alert alert-danger small text-center mt-4 mb-0">
                        {{ errorResponse.message }}
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>