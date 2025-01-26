<script setup>
    const randomComponentId = ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
        (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
    )
    const props = defineProps({
        contentStyle: {
            type: String,
            default: 'width: 100%; max-width: 800px;'
        }
    })



</script>
<template>

    <button 
        type="button" 
        data-bs-toggle="offcanvas" 
        :data-bs-target="`#${randomComponentId}`" 
        :aria-controls="randomComponentId"
    >
        <slot name="button" />

        <Teleport to="body">
            <div 
                :style="contentStyle"
                class="offcanvas offcanvas-start" 
                tabindex="-1" 
                :id="randomComponentId" 
                :aria-labelledby="`${randomComponentId}-Label`"
            >
                <div class="offcanvas-header">
                    <h5 class="offcanvas-title" :id="`${randomComponentId}-Label`">
                        <slot name="title" />
                    </h5>
                    <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                </div>
                <div class="offcanvas-body">
                    <slot name="body" />
                </div>
            </div>
        </Teleport>
    </button>

</template>