const UI = {
    /**
     * @param {string} targetId - ID dari tbody tabel
     * @param {number} rowCount - Mau berapa baris skeleton?
     */
    showSkeleton(targetId, rowCount = 10) {
        const tbody = document.getElementById(targetId);
        if (!tbody) return;

        // Ambil jumlah kolom dari header tabel secara otomatis
        const colCount = tbody.closest('table').querySelectorAll('thead th').length;
        
        let html = '';
        for (let i = 0; i < rowCount; i++) {
            html += `<tr class="skeleton-row">`;
            for (let j = 0; j < colCount; j++) {
                // Kolom pertama dikasih class sticky-col biar konsisten dengan tabelmu
                const isFirst = j === 0 ? 'sticky-col' : '';
                const randomWidth = [50, 75, 100][Math.floor(Math.random() * 3)];
                
                html += `<td class="${isFirst}"><div class="skeleton-line w-${randomWidth}"></div></td>`;
            }
            html += `</tr>`;
        }
        tbody.insertAdjacentHTML('beforeend', html);
    },

    hideSkeleton() {
        document.querySelectorAll('.skeleton-row').forEach(el => el.remove());
    }
};