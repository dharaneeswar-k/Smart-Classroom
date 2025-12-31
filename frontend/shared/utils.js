const Utils = {
    formatDate: (dateStr) => {
        if (!dateStr) return '-';
        return new Date(dateStr).toLocaleDateString(undefined, {
            weekday: 'short', year: 'numeric', month: 'short', day: 'numeric'
        });
    },

    formatTime: (timeStr) => {
        if (!timeStr) return '-';
        return timeStr.substring(0, 5);
    },

    getBadge: (status) => {
        const s = (status || '').toUpperCase();
        if (s === 'PRESENT') return '<span class="badge badge-present">Present</span>';
        if (s === 'ABSENT') return '<span class="badge badge-absent">Absent</span>';
        if (s === 'UNCERTAIN') return '<span class="badge badge-uncertain">Uncertain</span>';
        if (s === 'ON_DUTY') return '<span class="badge badge-onduty">On Duty</span>';
        if (s === 'HALF_DAY') return '<span class="badge badge-uncertain">Half Day</span>';
        return '<span class="badge badge-neutral">-</span>';
    },

    showToast: (message, type = 'info') => {
        const toast = document.createElement('div');
        toast.className = `glass-card position-fixed bottom-0 end-0 m-4 p-3 fade-in-up text-center fw-bold`;
        toast.style.zIndex = '9999';
        toast.style.minWidth = '250px';

        if (type === 'success') toast.style.borderLeft = '4px solid var(--color-success)';
        if (type === 'error') toast.style.borderLeft = '4px solid var(--color-danger)';

        toast.innerHTML = message;
        document.body.appendChild(toast);

        setTimeout(() => toast.remove(), 3000);
    }
};

window.Utils = Utils;
