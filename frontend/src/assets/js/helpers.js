export const userApiEndpoint = (endpoint) => {
    const url = import.meta.env.VITE_USER_API_ENDPOINT;
    return url + (endpoint.startsWith('/') ? endpoint.substring(1) : endpoint)
};

export const setLocalStorage = (key, value) => {
    localStorage.setItem(key, JSON.stringify(value));
}

export const getLocalStorage = (key) => {
    return JSON.parse(localStorage.getItem(key)) || null;
}

export async function isLoggedIn() {
    try {
        const endpoint = userApiEndpoint('is-logged-in');
        const response = await fetchJson(endpoint, {
            method: 'POST',
        });
        if (response.status === 401) {
            return false;
        }
        return true;
    } catch (err) {
        console.error(err);
        return false;
    }
}

export const formatPhoneNumber = (value) => {
    if (!value) return value;
    const phoneNumber = value.replace(/[^\d]/g, '');
    const lastTenDigits = phoneNumber.slice(-10);
    if (lastTenDigits.length < 4) return lastTenDigits;
    if (lastTenDigits.length < 7) return `(${lastTenDigits.slice(0, 3)}) ${lastTenDigits.slice(3)}`;
    return `(${lastTenDigits.slice(0, 3)}) ${lastTenDigits.slice(3, 6)}-${lastTenDigits.slice(6, 10)}`;
};