<script setup>
import ApplicationLogo from '@/components/ApplicationLogo.vue'

const goBack = () => {
  window.history.back()
}

</script>

<template>
<div class="container-fluid h-100 align-items-center justify-content-center transition-all">
    <div class="row h-100">

        <div class="d-none d-md-flex col-md-5 col-lg-6 align-items-center d-flex position-relative">
            <router-link
                class="navbar-brand mx-auto z-1"
                :to="{ name: 'home' }"
            >
                <ApplicationLogo />
            </router-link>
        </div>

        <div ref="refForm" class="col-md-7 col-lg-6 d-flex align-items-center bg-danger bg-opacity-10 shadow-sm">
            <div class="w-100 p-3 p-md-5 max-w-500px mx-auto">


                <h1 class="display-1 fw-light text-primary mb-1">404</h1>
                <h2 class="fs-4 fw-light mb-3 mb-md-5">
                    Oops! The page you're looking for isn't here.
                </h2>

                <div class="text-muted mb-4 fw-semibold fst-italic">
                    You may have mistyped the address or the page may have moved.
                </div>
                <button @click="goBack" class="btn btn-primary px-4">
                    <i class="bi bi-arrow-left me-2"></i>
                    Go Back
                </button>

                <div class="mt-5 d-block d-md-none">
                    <ApplicationLogo class="img-fluid mt-5" />
                </div>
            </div>
        </div>

    </div>
</div>
</template>
