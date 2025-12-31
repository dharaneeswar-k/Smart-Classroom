document.addEventListener('DOMContentLoaded', () => {
    const sidebarHTML = `
        <nav class="sidebar-glass fade-in-up">
            <div class="mb-5 px-2">
                <h4 class="fw-bold m-0" style="letter-spacing: -0.5px;">Smart Class<span style="color:var(--color-primary-soft)">.AI</span></h4>
                <small class="text-white-50">Admin Console</small>
            </div>
            
            <div class="flex-grow-1">
                <a href="/frontend/admin/dashboard.html" class="nav-link-glass ${window.location.href.includes('dashboard') ? 'active' : ''}">
                    <i data-lucide="layout-dashboard"></i> Dashboard
                </a>
                <div class="my-2 border-top border-white opacity-10"></div>
                <small class="text-uppercase text-white-50 px-2 mb-2 d-block" style="font-size:0.7em">Management</small>
                
                <a href="/frontend/admin/classrooms.html" class="nav-link-glass ${window.location.href.includes('classrooms') ? 'active' : ''}">
                    <i data-lucide="monitor"></i> Classrooms
                </a>
                <a href="/frontend/admin/faculty.html" class="nav-link-glass ${window.location.href.includes('faculty') ? 'active' : ''}">
                    <i data-lucide="users"></i> Faculty
                </a>
                <a href="/frontend/admin/students.html" class="nav-link-glass ${window.location.href.includes('students') ? 'active' : ''}">
                    <i data-lucide="graduation-cap"></i> Students
                </a>
                <a href="/frontend/admin/cameras.html" class="nav-link-glass ${window.location.href.includes('cameras') ? 'active' : ''}">
                    <i data-lucide="camera"></i> Cameras
                </a>
            </div>

            <div class="mt-auto border-top border-secondary pt-3">
                 <div class="d-flex align-items-center gap-3 mb-3 px-2">
                    <div class="rounded-circle bg-white text-primary d-flex align-items-center justify-content-center fw-bold" style="width:36px; height:36px;">
                        A
                    </div>
                    <div style="line-height:1.2">
                        <small class="d-block text-white fw-semibold">Administrator</small>
                    </div>
                </div>
                <button onclick="Auth.logout()" class="btn btn-sm w-100 btn-outline-light" style="border-radius:8px;">Sign Out</button>
            </div>
        </nav>
    `;

    document.body.insertAdjacentHTML('afterbegin', sidebarHTML);
    if (window.lucide) lucide.createIcons();
});
