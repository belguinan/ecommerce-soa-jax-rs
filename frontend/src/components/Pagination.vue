<script setup>

const props = defineProps({
    currentPage: {
        type: Number,
        required: true
    },
    totalPages: {
        type: Number,
        required: true
    },
    maxVisiblePages: {
        type: Number,
        default: 5
    }
});

const emit = defineEmits(['page-change']);

const getPageNumbers = () => {
    let pages = [];
    const { currentPage, totalPages, maxVisiblePages } = props;

    let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = startPage + maxVisiblePages - 1;

    if (endPage > totalPages) {
        endPage = totalPages;
        startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
    }

    return pages;
};

const handlePageClick = (page) => {
    if (page !== props.currentPage && page >= 1 && page <= props.totalPages) {
        emit('page-change', page);
    }
};
</script>

<template>
    <nav aria-label="Page navigation" v-if="totalPages > 1">
        <ul class="pagination">

            <li class="page-item" :class="{ disabled: currentPage === 1 }">
                <button 
                    class="page-link" 
                    @click="handlePageClick(currentPage - 1)"
                    :disabled="currentPage === 1"
                >
                    &laquo;
                </button>
            </li>

            <li v-if="getPageNumbers()[0] > 1" class="page-item">
                <button class="page-link" @click="handlePageClick(1)">1</button>
            </li>

            <li v-if="getPageNumbers()[0] > 2" class="page-item disabled">
                <span class="page-link">...</span>
            </li>

            <li 
                v-for="page in getPageNumbers()" 
                :key="page"
                class="page-item"
                :class="{ active: page === currentPage }"
            >
                <button 
                    class="page-link" 
                    @click="handlePageClick(page)"
                >
                    {{ page }}
                </button>
            </li>

            <li v-if="getPageNumbers()[getPageNumbers().length - 1] < totalPages - 1" class="page-item disabled">
                <span class="page-link">...</span>
            </li>

            <li v-if="getPageNumbers()[getPageNumbers().length - 1] < totalPages" class="page-item">
                <button 
                    class="page-link" 
                    @click="handlePageClick(totalPages)"
                >
                    {{ totalPages }}
                </button>
            </li>

            <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                <button 
                    class="page-link"
                    @click="handlePageClick(currentPage + 1)"
                    :disabled="currentPage === totalPages"
                >
                    &raquo;
                </button>
            </li>
        </ul>
    </nav>
</template>
