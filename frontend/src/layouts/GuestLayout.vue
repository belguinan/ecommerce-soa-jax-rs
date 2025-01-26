<!-- src/layouts/GuestLayout.vue -->
<template>
    <div class="container-fluid h-100 align-items-center justify-content-center transition-all bg-white">
        <div class="row h-100">
            <div class="d-none d-xl-flex col-xl-6 align-items-center d-flex position-relative bg-body">
                <div class="pt-5 pb-4 mx-auto">
                    <ApplicationLogo class="img-fluid mb-4 max-w-80px" />
                </div>
            </div>

            <div class="d-flex flex-column flex-xl-row align-items-center shadow-sm z-1 col-xl-6 bg-body bg-xl-white">
                <div class="text-center pt-5 pb-4 d-block d-xl-none">
                    <ApplicationLogo class="img-fluid mb-4 max-w-80px" />
                </div>

                <router-view></router-view>
            </div>
        </div>
    </div>
</template>

<script setup>
import ApplicationLogo from '@/components/ApplicationLogo.vue';
</script>