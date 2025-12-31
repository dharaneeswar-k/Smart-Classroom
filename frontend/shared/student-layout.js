// shared/student-layout.js
document.addEventListener('DOMContentLoaded', () => {
    const sidebarHTML = `
        <nav class="sidebar-glass fade-in-up">
            <div class="mb-5 px-2">
                <h4 class="fw-bold m-0" style="letter-spacing: -0.5px;">Smart Class<span style="color:var(--color-primary-soft)">.AI</span></h4>
                <small class="text-white-50">Student Portal</small>
            </div>
            
            <div class="flex-grow-1">
                <a href="/frontend/student/dashboard.html" class="nav-link-glass ${window.location.href.includes('dashboard') ? 'active' : ''}">
                    <i data-lucide="layout-dashboard"></i> Dashboard
                </a>
                <a href="/frontend/student/od.html" class="nav-link-glass ${window.location.href.includes('od') ? 'active' : ''}">
                    <i data-lucide="send"></i> Request OD
                </a>
                <a href="/frontend/student/history.html" class="nav-link-glass ${window.location.href.includes('history') ? 'active' : ''}">
                    <i data-lucide="history"></i> History
                </a>
            </div>

            <div class="mt-auto border-top border-secondary pt-3">
                 <div class="d-flex align-items-center gap-3 mb-3 px-2">
                    <div class="rounded-circle bg-white text-primary d-flex align-items-center justify-content-center fw-bold" style="width:36px; height:36px;">
                        ${(localStorage.getItem('username') || 'S').charAt(0).toUpperCase()}
                    </div>
                    <div style="line-height:1.2">
                        <small class="d-block text-white fw-semibold">${localStorage.getItem('username') || 'Student'}</small>
                    </div>
                </div>
                <button onclick="Auth.logout()" class="btn btn-sm w-100 btn-outline-light" style="border-radius:8px;">Sign Out</button>
            </div>
        </nav>
    `;

    document.body.insertAdjacentHTML('afterbegin', sidebarHTML);
    if (window.lucide) lucide.createIcons();
});
