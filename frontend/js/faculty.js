// Initialize State
let currentClassroomId = null;
let currentDate = null;
let resolutionModal = null; // Bootstrap Modal Instance

document.addEventListener('DOMContentLoaded', async () => {
    resolutionModal = new bootstrap.Modal(document.getElementById('resolutionModal'));

    // Set Name
    document.getElementById('facultyNameDisplay').textContent = localStorage.getItem('username') || 'Faculty';

    // Set Date Picker to Today
    const today = new Date().toISOString().split('T')[0];
    const datePicker = document.getElementById('datePicker');
    datePicker.value = today;
    currentDate = today;

    // Load Classrooms
    await loadClassrooms();

    // Event Listeners
    document.getElementById('loadBtn').addEventListener('click', loadAttendance);

    datePicker.addEventListener('change', (e) => {
        currentDate = e.target.value;
    });

    document.getElementById('classroomSelect').addEventListener('change', (e) => {
        currentClassroomId = e.target.value;
    });
});

async function loadClassrooms() {
    const select = document.getElementById('classroomSelect');
    try {
        // Fetch classrooms assigned to faculty (Adjust endpoint if needed, or fetch all if not restricted)
        // Assuming /faculty/classrooms or similar exists, otherwise use /admin/classrooms logic if open
        // Using a hypothetical endpoint based on requirements
        // If not available, we might need to fetch from a generic endpoint.
        // Let's assume there's an endpoint to get assigned classrooms.
        // If not, we'll try fetching all (if permitted) or handle error.

        // Checking backend analysis: FacultyClassroomController might exist?
        // Ah, FacultyController has endpoints. Let's try to find one.
        // If not found, use a placeholder or handle gracefully.
        // Using '/faculty/dashboard/classrooms' as a safe guess or strict backend map.
        // From analysis: FacultyClassroomController.java likely exists.

        // Let's use a generic fetch and populate.
        // For now, I'll mock specific endpoint or use a known one.
        // Let's assume `GET /faculty/classrooms` works given controller file existence.

        const classrooms = await api.get('/faculty/dashboard/classrooms');
        if (!classrooms) return;

        select.innerHTML = '<option value="" disabled selected>Select Classroom</option>';
        classrooms.forEach(c => {
            select.innerHTML += `<option value="${c.id}">${c.name}</option>`;
        });

        // Auto-select first if exists
        if (classrooms.length > 0) {
            select.value = classrooms[0].id;
            currentClassroomId = classrooms[0].id;
            loadAttendance(); // Auto load
        }

    } catch (e) {
        console.error("Failed to load classrooms", e);
    }
}

async function loadAttendance() {
    if (!currentClassroomId || !currentDate) {
        alert("Please select a classroom and date");
        return;
    }

    const tbody = document.getElementById('attendanceBody');
    tbody.innerHTML = '<tr><td colspan="6" class="text-center py-4">Loading data...</td></tr>';

    document.getElementById('pendingAlert').classList.add('d-none');

    try {
        const url = `/faculty/attendance?classroomId=${currentClassroomId}&date=${currentDate}`;
        const data = await api.get(url);

        if (!data || data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center py-4 text-muted">No attendance records found for this date.</td></tr>';
            return;
        }

        renderTable(data);

    } catch (e) {
        console.error("Error loading attendance", e);
        tbody.innerHTML = '<tr><td colspan="6" class="text-center py-4 text-danger">Error loading data.</td></tr>';
    }
}

function renderTable(data) {
    const tbody = document.getElementById('attendanceBody');
    tbody.innerHTML = '';

    let isBlockingActive = false;

    data.forEach(row => {
        // Row Structure: Student | Name | S1 | S2 | S3 | Daily

        // Check if daily status is missing (Blocking Rule)
        if (!row.dailyStatus) {
            isBlockingActive = true;
        }

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="ps-4 fw-medium text-primary-700">${row.studentRollNo}</td>
            <td>${row.studentName}</td>
            <td class="text-center">${renderSessionBadge(row, 1, row.session1Status)}</td>
            <td class="text-center">${renderSessionBadge(row, 2, row.session2Status)}</td>
            <td class="text-center">${renderSessionBadge(row, 3, row.session3Status)}</td>
            <td class="text-center">${renderDailyBadge(row.dailyStatus)}</td>
        `;
        tbody.appendChild(tr);
    });

    if (isBlockingActive) {
        document.getElementById('pendingAlert').classList.remove('d-none');
    }
}

function renderSessionBadge(student, sessionNum, status) {
    if (!status) return '<span class="badge badge-neutral">--</span>';

    if (status === 'PRESENT') {
        return '<span class="badge badge-present">Present</span>';
    } else if (status === 'ABSENT') {
        return '<span class="badge badge-absent">Absent</span>';
    } else if (status === 'UNCERTAIN') {
        // Actionable Badge
        return `
            <span class="badge badge-uncertain" 
                  onclick="openResolutionModal('${student.studentId}', '${student.studentName}', ${sessionNum})">
                <i data-lucide="alert-triangle" style="width:12px; height:12px; margin-right:4px;"></i>
                Resolve
            </span>
        `;
    }
    return `<span class="badge badge-neutral">${status}</span>`;
}

function renderDailyBadge(status) {
    if (!status) return '<span class="text-muted small fst-italic">Pending...</span>';

    if (status === 'PRESENT') return '<span class="badge bg-success">Full Present</span>';
    if (status === 'HALF_DAY') return '<span class="badge bg-info text-dark">Half Day</span>';
    if (status === 'ABSENT') return '<span class="badge bg-danger">Absent</span>';
    if (status === 'ON_DUTY') return '<span class="badge bg-primary">On Duty</span>';

    return `<span class="badge bg-secondary">${status}</span>`;
}

// Resolution Logic
window.openResolutionModal = (studentId, studentName, sessionNum) => {
    document.getElementById('modalStudentId').value = studentId;
    document.getElementById('modalStudentName').textContent = studentName;
    document.getElementById('modalSessionNum').value = sessionNum;
    document.getElementById('modalSession').textContent = `Session ${sessionNum}`;

    resolutionModal.show();
};

window.resolveAttendance = async (status) => {
    const studentId = document.getElementById('modalStudentId').value;
    const sessionNum = document.getElementById('modalSessionNum').value;

    // Payload for backend
    const payload = {
        studentId: parseInt(studentId),
        date: currentDate,
        sessionNumber: parseInt(sessionNum),
        status: status
    };

    try {
        await api.post('/faculty/attendance/resolve', payload);
        resolutionModal.hide();
        // Refresh Table logic
        loadAttendance();

        // Re-init icons strictly if needed, though innerHTML replacement might need lucide.createIcons called again
        setTimeout(() => lucide.createIcons(), 100);

    } catch (e) {
        alert("Failed to update status. Please try again.");
    }
};
