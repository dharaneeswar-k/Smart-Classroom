// shared/faculty-layout.js
document.addEventListener('DOMContentLoaded', () => {
    const appShell = document.querySelector('.app-shell') || document.body;

    // Inject Sidebar
    const sidebarHTML = `
        <nav class="sidebar-glass fade-in-up">
            <div class="mb-5 px-2">
                <h4 class="fw-bold m-0" style="letter-spacing: -0.5px;">Smart Class<span style="color:var(--color-primary-soft)">.AI</span></h4>
                <small class="text-white-50">Faculty Portal</small>
            </div>
            
            <div class="flex-grow-1">
                <a href="/frontend/faculty/dashboard.html" class="nav-link-glass ${window.location.href.includes('dashboard') ? 'active' : ''}">
                    <i data-lucide="layout-dashboard"></i> Dashboard
                </a>
                <a href="/frontend/faculty/attendance.html" class="nav-link-glass ${window.location.href.includes('attendance') ? 'active' : ''}">
                    <i data-lucide="users"></i> Attendance
                </a>
                <a href="/frontend/faculty/od-requests.html" class="nav-link-glass ${window.location.href.includes('od') ? 'active' : ''}">
                    <i data-lucide="file-check"></i> OD Requests
                </a>
                 <a href="/frontend/faculty/reports.html" class="nav-link-glass ${window.location.href.includes('reports') ? 'active' : ''}">
                    <i data-lucide="bar-chart-2"></i> Reports
                </a>
            </div>

            <div class="mt-auto border-top border-secondary pt-3">
                <div class="d-flex align-items-center gap-3 mb-3 px-2">
                    <div class="rounded-circle bg-white text-primary d-flex align-items-center justify-content-center fw-bold" style="width:36px; height:36px;">
                        ${(localStorage.getItem('username') || 'F').charAt(0).toUpperCase()}
                    </div>
                    <div style="line-height:1.2">
                        <small class="d-block text-white fw-semibold">${localStorage.getItem('username') || 'Faculty'}</small>
                        <small class="text-white-50" style="font-size:0.7em">Online</small>
                    </div>
                </div>
                <button onclick="Auth.logout()" class="btn btn-sm w-100 btn-outline-light" style="border-radius:8px;">Sign Out</button>
            </div>
        </nav>
    `;

    // Prepend Sidebar
    // Note: We assume the page has a <main class="main-content"> structure
    // If not, we wrap the content. But following the plan, updates to HTML will ensure structure.
    document.body.insertAdjacentHTML('afterbegin', sidebarHTML);

    // Setup Icons
    if (window.lucide) lucide.createIcons();
});
