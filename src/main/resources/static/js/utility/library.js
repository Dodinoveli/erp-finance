// js/utility/library.js (sebagai module)
export const library = {
    formatRupiah: function (value) {
        return new Intl.NumberFormat('id-ID', {
            style: 'currency',
            currency: 'IDR',
            minimumFractionDigits: 0,
            maximumFractionDigits: 2
        }).format(value);
    },

    setTableHeight: function (id_wrapper, id_table) {
        const wrapper = document.getElementById(id_wrapper);
        if (!wrapper) {
            console.warn('⚠️ Wrapper tidak ditemukan');
            return;
        }

        const viewportHeight = window.innerHeight;
        const wrapperRect = wrapper.getBoundingClientRect();
        const wrapperTop = wrapperRect.top;

        let height = viewportHeight - wrapperTop - 20;
        height = Math.max(300, Math.min(800, height));

        wrapper.style.height = height + "px";
        wrapper.style.maxHeight = height + "px";
        wrapper.style.overflow = "auto";

        // Cari thead - otomatis cari di dalam wrapper atau berdasarkan id_table
        let thead = null;
        
        // Cari di dalam wrapper dulu
        thead = wrapper.querySelector('thead');
        
        // Jika tidak ketemu, cari berdasarkan id_table
        if (!thead && id_table) {
            const table = document.getElementById(id_table);
            if (table) {
                thead = table.querySelector('thead');
            }
        }
        
        // Jika masih tidak ketemu, cari global (untuk backward compatibility)
        if (!thead) {
            thead = document.querySelector('thead');
        }

        if (thead) {
            thead.style.position = 'sticky';
            thead.style.top = '0';
            thead.style.zIndex = '10';
            thead.style.background = '#f8f9fa';
        }

        console.log(`📐 Table height: ${height}px`);
},

};

// Tetap set ke window untuk akses global
window.library = library;