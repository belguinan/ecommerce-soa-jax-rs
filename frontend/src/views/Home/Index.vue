<script setup>
import PhoneNumber from '@/components/PhoneNumber.vue';
import Pagination from '@/components/Pagination.vue';
import Table from '@/components/Table.vue';
import { ref } from 'vue';
import { useSwal } from '@/composables/useSwal.js'
import {getLocalStorage} from "@/assets/js/helpers";

const { confirm } = useSwal();

const rows = ref({})

const user = getLocalStorage('user')

function deleteAction(row, index) {
    return confirm("Please confirm this action", {
        icon: "warning"
    }).then(result => {
        if (result.isConfirmed) {
            //
        }
    })
}

</script>

<template>
    <div class="container mb-4">
        <div class="row gx-5">
            <div class="col-lg-4 col-xl-5 col-xxl-6 mt-n3 mt-lg-0">
                <h1 class="h3 fw-bold">
                    Welcome to Your Account {{ user.name }}!
                </h1>
                <h3 class="h5 small text-muted fw-light">
                    Showing {{ rows?.data?.length || 0 }} items
                </h3>
            </div>
        </div>
    </div>
</template>
