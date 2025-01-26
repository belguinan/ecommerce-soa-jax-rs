import { useFetch } from '@/composables/useFetch.js'
const { fetchJson } = useFetch();

export function getCustomersData(params = {}) {
    return new Promise((resolve, error) => {
        document.body.loader().show()
        fetchJson('/customer/customers?' + new URLSearchParams(params || {}).toString(), {
            method: 'GET'
        })
        .then(response => {
            resolve(response)
        })
        .catch(err => error(err))
        .finally(() => document.body.loader().hide())
    })
}

export function deleteCustomersData(row, index) {
    return new Promise((resolve, error) => {
        document.body.loader().show()
        fetchJson(`/customer/customers/${row.id}`, {
            method: 'DELETE'
        })
        .then(response => {
            resolve(response)
        })
        .finally(() => document.body.loader().hide())
    })
}

export function updateTranscript(id, params = {}) {
    return new Promise((resolve, failed) => {

        document.body.loader().show()
        
        return fetchJson(`/customer/transcripts/${id}`, {
            method: 'PUT',
            body: params
        })
        .then(async response => {
            resolve(response);
        })
        .catch(response => {
            const errors = Object.values(response)
            if (errors.length > 0) {
                errorAlert(errors[0])
            }
            failed(response)
        })
        .finally(() => document.body.loader().hide())
    })
} 