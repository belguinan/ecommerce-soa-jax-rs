<script setup>
import {onMounted, ref} from 'vue';
import { useRouter } from 'vue-router';
import PrimaryButton from '@/components/PrimaryButton.vue';
import { useFetch } from '@/composables/useFetch';
import { useSwal } from '@/composables/useSwal';
import {getLocalStorage, setLocalStorage, userApiEndpoint} from '@/assets/js/helpers.js';

const router = useRouter();
const { fetchJson } = useFetch();
const { errorAlert } = useSwal();

const loading = ref(false);
const form = ref({
    username: '',
    password: ''
});

const errorResponse = ref({});

const handleLogin = async () => {
    if (loading.value) return;

    loading.value = true;
    errorResponse.value = {};

    document.body.loader().show()

    try {

        const endpoint = userApiEndpoint('login');

        const response = await fetchJson(endpoint, {
            method: 'POST',
            body: {...form.value}
        });

        if (response.token) {
            setLocalStorage('token', response.token);
            setLocalStorage('user', response.user);
        }

        if (response.user?.isSuperAdmin) {
            router.push({ name: 'stats.index' });
            return;
        }

        if (response.token) {
            router.push({ name: 'home' });
            return;
        }

        handleErrorResponse(response);
    } catch (err) {
        errorResponse.value.message = 'Invalid credentials!';
        errorAlert(errorResponse.value.message);
    } finally {
        document.body.loader().hide()
        loading.value = false;
    }
};

const handleErrorResponse = (response) => {
    if (!response.errors) return;
    errorResponse.value = response;
    errorResponse.value.message = response.message || 'Login failed!';
    if (response.message) {
        errorAlert(response.message);
    }
};


onMounted(() => {
    const user = getLocalStorage('user')

    if (user) {
        form.value.username = user?.username;
    }
})
</script>

<template>
    <div class="container">
        <div class="row">
            <div class="card bg-body border-secondary mb-4 rounded-4 p-3 max-w-500px mx-auto">
                <div class="card-body p-4">
                    <h2 class="mb-4 fw-light h4">Login to your account</h2>

                    <form @submit.prevent="handleLogin">
                        <div class="row g-3">
                            <!-- username -->
                            <div class="col-12">
                                <div class="form-floating" :class="{'is-invalid': errorResponse?.errors?.username}">
                                    <input
                                        v-model="form.username"
                                        type="text"
                                        class="form-control bg-light"
                                        :class="{'is-invalid': errorResponse?.errors?.email}"
                                        placeholder="Username"
                                        id="username"
                                        autocomplete="username"
                                    >
                                    <label>Username</label>
                                    <div v-if="errorResponse?.errors?.username" class="invalid-feedback">
                                        {{ errorResponse.errors.username }}
                                    </div>
                                </div>
                            </div>

                            <!-- Password -->
                            <div class="col-12">
                                <div class="form-floating" :class="{'is-invalid': errorResponse?.errors?.password}">
                                    <input
                                        v-model="form.password"
                                        type="password"
                                        class="form-control bg-light"
                                        :class="{'is-invalid': errorResponse?.errors?.password}"
                                        placeholder="Password"
                                        id="password"
                                        autocomplete="current-password"
                                    >
                                    <label>Password</label>
                                    <div v-if="errorResponse?.errors?.password" class="invalid-feedback">
                                        {{ errorResponse.errors.password[0] }}
                                    </div>
                                </div>
                            </div>

                            <!-- Submit Button -->
                            <div class="col-12">
                                <PrimaryButton
                                    class="btn-dark w-100 text-uppercase"
                                    style="height: 58px;"
                                    :class="{'opacity-25': loading}"
                                    :disabled="loading"
                                >
                                    {{ loading ? 'Logging in...' : 'Log in' }}
                                </PrimaryButton>
                            </div>
                        </div>

                        <div v-if="errorResponse?.message" class="alert alert-danger small text-center mt-4 mb-0">
                            {{ errorResponse.message }}
                        </div>
                    </form>
                </div>
            </div>

            <div class="col-12 text-center">
                <p class="mb-0 fw-semibold">
                    Don't have an account?
                    <router-link :to="{ name: 'signup' }" class="text-decoration-none">
                        Register here
                    </router-link>
                </p>
            </div>
        </div>
    </div>
</template>