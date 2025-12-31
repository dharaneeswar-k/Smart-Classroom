const API_BASE_URL = 'http://localhost:8081';

const api = {
    // Helper to get headers
    getHeaders: () => {
        const token = localStorage.getItem('jwt_token');
        const headers = {
            'Content-Type': 'application/json'
        };
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }
        return headers;
    },

    // Generic fetch wrapper
    request: async (endpoint, options = {}) => {
        const url = `${API_BASE_URL}${endpoint}`;
        const config = {
            ...options,
            headers: {
                ...api.getHeaders(),
                ...options.headers
            }
        };

        try {
            const response = await fetch(url, config);

            // Handle 401 Unauthorized globally
            if (response.status === 401) {
                console.warn('Unauthorized access. Redirecting to login.');
                localStorage.removeItem('jwt_token');
                localStorage.removeItem('user_role');
                window.location.href = '/index.html';
                return null;
            }

            // Handle non-200 generally
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || `API Error: ${response.status}`);
            }

            // Return JSON if content exists
            const contentLength = response.headers.get("content-length");
            if (contentLength && contentLength === "0") return null;

            // Try parsing JSON, fallback to null/text if empty
            try {
                return await response.json();
            } catch (e) {
                return null;
            }

        } catch (error) {
            console.error('API Request Failed:', error);
            throw error;
        }
    },

    get: (endpoint) => api.request(endpoint, { method: 'GET' }),
    post: (endpoint, body) => api.request(endpoint, { method: 'POST', body: JSON.stringify(body) }),
    put: (endpoint, body) => api.request(endpoint, { method: 'PUT', body: JSON.stringify(body) }),
    delete: (endpoint) => api.request(endpoint, { method: 'DELETE' })
};
