document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const btn = e.target.querySelector('button');
    const user = document.getElementById('username').value;
    const pass = document.getElementById('password').value;

    btn.disabled = true;
    btn.innerHTML = 'Verifying...';
    document.getElementById('loginError').classList.add('d-none');

    try {
        const res = await api.post('/auth/login', { username: user, password: pass });

        if (res && res.token) {
            const payload = JSON.parse(atob(res.token.split('.')[1]));
            const role = payload.role || payload.authorities[0];
            Auth.login(res.token, role, user);

            // Redirect
            if (role === 'ROLE_ADMIN') window.location.href = '../admin/dashboard.html';
            else if (role === 'ROLE_FACULTY') window.location.href = '../faculty/dashboard.html';
            else if (role === 'ROLE_STUDENT') window.location.href = '../student/dashboard.html';
        } else {
            throw new Error();
        }
    } catch (err) {
        document.getElementById('loginError').classList.remove('d-none');
        btn.disabled = false;
        btn.innerHTML = 'Sign In to Portal';
    }
});
