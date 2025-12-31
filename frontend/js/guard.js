// Role Definitions
const ROLES = {
    ADMIN: 'ROLE_ADMIN',
    FACULTY: 'ROLE_FACULTY',
    STUDENT: 'ROLE_STUDENT'
};

// Check Auth on Page Load
(function checkAuth() {
    // Skip checking on login page
    if (window.location.pathname.endsWith('index.html') || window.location.pathname === '/' || window.location.pathname.endsWith('/frontend/')) {
        return;
    }

    const token = localStorage.getItem('jwt_token');
    const userRole = localStorage.getItem('user_role');

    if (!token || !userRole) {
        window.location.href = '../index.html'; // Adjust path logic as needed
        return;
    }

    // Role-based protection
    const currentPath = window.location.pathname;

    if (currentPath.includes('/admin/') && userRole !== ROLES.ADMIN) {
        alert('Access Denied: Admins only.');
        window.location.href = '../index.html';
    }
    else if (currentPath.includes('/faculty/') && userRole !== ROLES.FACULTY) {
        alert('Access Denied: Faculty only.');
        window.location.href = '../index.html';
    }
    else if (currentPath.includes('/student/') && userRole !== ROLES.STUDENT) {
        alert('Access Denied: Students only.');
        window.location.href = '../index.html';
    }

    // Setup Logout Button (if exists)
    document.addEventListener('DOMContentLoaded', () => {
        const logoutBtn = document.getElementById('logoutBtn');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', () => {
                localStorage.removeItem('jwt_token');
                localStorage.removeItem('user_role');
                window.location.href = '../index.html';
            });
        }
    });

})();
