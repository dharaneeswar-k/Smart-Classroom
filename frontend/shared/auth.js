// shared/auth.js

const ROLES = {
    ADMIN: 'ROLE_ADMIN',
    FACULTY: 'ROLE_FACULTY',
    STUDENT: 'ROLE_STUDENT'
};

const Auth = {
    isAuthenticated: () => {
        return !!localStorage.getItem('jwt_token');
    },

    getRole: () => {
        return localStorage.getItem('user_role');
    },

    getUsername: () => {
        return localStorage.getItem('username');
    },

    login: (token, role, username) => {
        localStorage.setItem('jwt_token', token);
        localStorage.setItem('user_role', role);
        localStorage.setItem('username', username);
    },

    logout: () => {
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('user_role');
        localStorage.removeItem('username');
        window.location.href = '/auth/login.html';
    },

    // Guard function to protect pages
    checkAuth: (requiredRole) => {
        const token = localStorage.getItem('jwt_token');
        const role = localStorage.getItem('user_role');

        if (!token || !role) {
            window.location.href = '/auth/login.html';
            return false;
        }

        if (requiredRole && role !== requiredRole) {
            alert('Access Denied');
            if (role === ROLES.ADMIN) window.location.href = '/admin/dashboard.html';
            else if (role === ROLES.FACULTY) window.location.href = '/faculty/dashboard.html';
            else if (role === ROLES.STUDENT) window.location.href = '/student/dashboard.html';
            return false;
        }
        return true;
    }
};

window.Auth = Auth;
