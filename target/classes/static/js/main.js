/**
 * NCC Portal - Main JavaScript
 * Basic utility functions: search filter, confirm dialogs, form validation
 */

// --- Delete confirmation ---
function confirmDelete(message) {
    return confirm(message || 'Are you sure you want to delete this record? This action cannot be undone.');
}

// --- Attach confirm to all delete links ---
document.addEventListener('DOMContentLoaded', function () {

    // Delete buttons
    document.querySelectorAll('a.btn-danger, a[data-confirm]').forEach(function (link) {
        link.addEventListener('click', function (e) {
            var msg = link.getAttribute('data-confirm') || 'Are you sure you want to delete this record?';
            if (!confirm(msg)) {
                e.preventDefault();
            }
        });
    });

    // --- Client-side table search filter ---
    var searchInput = document.getElementById('tableSearch');
    if (searchInput) {
        searchInput.addEventListener('keyup', function () {
            var query = this.value.toLowerCase();
            var rows = document.querySelectorAll('.data-table tbody tr');
            rows.forEach(function (row) {
                var text = row.textContent.toLowerCase();
                row.style.display = text.includes(query) ? '' : 'none';
            });
        });
    }

    // --- Auto-dismiss alerts after 5 seconds ---
    var alerts = document.querySelectorAll('.alert.auto-dismiss');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.opacity = '0';
            setTimeout(function () { alert.style.display = 'none'; }, 500);
        }, 5000);
    });

    // --- Form: prevent double submission ---
    var forms = document.querySelectorAll('form.no-double-submit');
    forms.forEach(function (form) {
        form.addEventListener('submit', function () {
            var btn = form.querySelector('button[type="submit"]');
            if (btn) {
                btn.disabled = true;
                btn.textContent = 'Please wait...';
            }
        });
    });

    // --- Phone number: allow only digits ---
    document.querySelectorAll('input[data-type="phone"]').forEach(function (input) {
        input.addEventListener('input', function () {
            this.value = this.value.replace(/\D/g, '').slice(0, 10);
        });
    });

    // --- Aadhaar: allow only digits ---
    document.querySelectorAll('input[data-type="aadhaar"]').forEach(function (input) {
        input.addEventListener('input', function () {
            this.value = this.value.replace(/\D/g, '').slice(0, 12);
        });
    });

});
