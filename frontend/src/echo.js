import Echo from 'laravel-echo';
import Pusher from 'pusher-js';

window.Pusher = Pusher;

const pusherClient = new Pusher(import.meta.env.VITE_REVERB_APP_KEY, {
    wsHost: import.meta.env.VITE_REVERB_HOST,
    wsPort: import.meta.env.VITE_REVERB_PORT ?? 80,
    wssPort: import.meta.env.VITE_REVERB_PORT ?? 443,
    forceTLS: true,
    encrypted: true,
    disableStats: true,
    cluster: '',
    authEndpoint: import.meta.env.VITE_REVERB_AUTH_HOST,
    auth: {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    },
    authorizer: (channel, options) => {
        return {
            authorize: (socketId, callback) => {
                const params = new URLSearchParams({
                    socket_id: socketId,
                    channel_name: channel.name
                });

                fetch(`${import.meta.env.VITE_REVERB_AUTH_HOST}?${params}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(response => response.json())
                .then(response => {
                    callback(false, response);
                })
                .catch(error => {
                    callback(true, error);
                });
            }
        };
    }
});

window.Echo = new Echo({
    broadcaster: 'reverb',
    client: pusherClient,
    key: import.meta.env.VITE_REVERB_APP_KEY,
    wsHost: import.meta.env.VITE_REVERB_HOST,
    wsPort: import.meta.env.VITE_REVERB_PORT ?? 80,
    wssPort: import.meta.env.VITE_REVERB_PORT ?? 443,
    forceTLS: true,
    encrypted: true,
    disableStats: true,
    enabledTransports: ['ws', 'wss'],
    cluster: ''
});