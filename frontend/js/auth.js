document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    const errorAlert = document.getElementById('loginError');
    const submitBtn = e.target.querySelector('button');

    const username = usernameInput.value.trim();
    const password = passwordInput.value.trim();

    // Reset State
    errorAlert.classList.add('d-none');
    submitBtn.disabled = true;
    submitBtn.textContent = 'Signing in...';

    try {
        const response = await api.post('/auth/login', { username, password });

        if (response && response.token) {
            // Login Success
            localStorage.setItem('jwt_token', response.token);

            // Decode Token to get Role (Simple Base64 decode for payload)
            const payload = JSON.parse(atob(response.token.split('.')[1]));
            const role = payload.role || payload.authorities[0]; // Adjust based on your JWT structure

            localStorage.setItem('user_role', role);
            localStorage.setItem('username', username);

            // Redirect based on role
            if (role === 'ROLE_ADMIN') {
                window.location.href = 'admin/dashboard.html';
            } else if (role === 'ROLE_FACULTY') {
                window.location.href = 'faculty/dashboard.html';
            } else if (role === 'ROLE_STUDENT') {
                window.location.href = 'student/dashboard.html';
            } else {
                throw new Error('Unknown Role');
            }

        } else {
            throw new Error('Invalid credentials');
        }

    } catch (error) {
        console.error(error);
        errorAlert.textContent = 'Login failed. Check credentials.';
        errorAlert.classList.remove('d-none');
        passwordInput.value = '';
    } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Sign In';
    }
});
