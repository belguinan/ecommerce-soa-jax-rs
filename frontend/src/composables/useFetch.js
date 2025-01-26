import {getLocalStorage} from "@/assets/js/helpers";

export function useFetch() {
	
	const fetchWrapper = (url, params = {}) => {
		params.headers = {
	        'Content-Type': 'application/json',
	        'Accept': 'application/json',
	    }

		const token = getLocalStorage('token');

	    if (token) {
	    	params.headers.Authorization = `Bearer ${token}`;
	    }

	    if (params.body) {
	    	params.body = JSON.stringify(params.body)
	    }

	    return fetch(`${url}`, params);
	}

	return {
		fetch: fetchWrapper,

		async fetchJson(url, params) {
			let response = await fetchWrapper(url, params)

			if (response.status === 500) {
				response = await response.json()
				throw new Error(response.message || response.statusText)
			}

		    return await response.json()
		}
	}
}