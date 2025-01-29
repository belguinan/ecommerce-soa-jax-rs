export const userApiEndpoint = (endpoint) => {
    const url = import.meta.env.VITE_USER_API_ENDPOINT;
    endpoint = String(endpoint);
    return url + (endpoint.startsWith('/') ? endpoint.substring(1) : endpoint)
}

export const productApiEndpoint = (endpoint) => {
    const url = import.meta.env.VITE_PRODUCT_API_ENDPOINT;
    endpoint = String(endpoint);
    return url + (endpoint.startsWith('/') ? endpoint.substring(1) : endpoint)
}

export const cartApiEndpoint = (endpoint) => {
    const url = import.meta.env.VITE_CART_API_ENDPOINT;
    endpoint = String(endpoint);
    return url + (endpoint.startsWith('/') ? endpoint.substring(1) : endpoint)
}

export const orderApiEndpoint = (endpoint) => {
    const url = import.meta.env.VITE_ORDER_API_ENDPOINT;
    endpoint = String(endpoint);
    return url + (endpoint.startsWith('/') ? endpoint.substring(1) : endpoint)
}

export const setLocalStorage = (key, value) => {
    localStorage.setItem(key, JSON.stringify(value));
}

export const getLocalStorage = (key) => {
    return JSON.parse(localStorage.getItem(key)) || null;
}

export const formatPhoneNumber = (value) => {
    if (!value) return value;
    const phoneNumber = value.replace(/[^\d]/g, '');
    const lastTenDigits = phoneNumber.slice(-10);
    if (lastTenDigits.length < 4) return lastTenDigits;
    if (lastTenDigits.length < 7) return `(${lastTenDigits.slice(0, 3)}) ${lastTenDigits.slice(3)}`;
    return `(${lastTenDigits.slice(0, 3)}) ${lastTenDigits.slice(3, 6)}-${lastTenDigits.slice(6, 10)}`;
}

export function formatPrice(price, currency = 'USD', locale = 'en-US') {
    if (price === null || price === undefined || price === '') {
        return '';
    }

    const numberPrice = typeof price === 'string' ? parseFloat(price) : price;
    
    if (isNaN(numberPrice)) {
        return '';
    }

    try {
        return new Intl.NumberFormat(locale, {
            style: 'currency',
            currency: currency,
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        }).format(numberPrice);
    } catch (error) {
        return `${currency} ${numberPrice.toFixed(2)}`;
    }
}