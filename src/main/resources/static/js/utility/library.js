// js/utility/library.js (sebagai module)
export const library = {

    // untuk redirect edit selesai 
    navigate: function (url) {
        const loading = document.getElementById("loading");

        if (loading) {
            loading.style.opacity = "1";
            loading.style.visibility = "visible";
            loading.style.pointerEvents = "auto";
        }

        return htmx.ajax("GET", url, {
            target: "#page-content",
            swap: "innerHTML"
        })
            .then(() => {
                window.history.pushState({}, "", url);
            })
            .catch((error) => {
                console.error("Gagal navigasi:", error);
                throw error;
            })
            .finally(() => {
                if (loading) {
                    loading.style.opacity = "0";
                    loading.style.visibility = "hidden";
                    loading.style.pointerEvents = "none";
                }
            });
    },

    formatRupiah: function (value) {
        return new Intl.NumberFormat('id-ID', {
            style: 'currency',
            currency: 'IDR',
            minimumFractionDigits: 0,
            maximumFractionDigits: 2
        }).format(value);
    },

    formatDateIndonesia: function (dateString) {
        if (!dateString) return '-';
        const date = new Date(dateString);
        return date.toLocaleDateString('id-ID', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    },

    setTableHeight: function (id_wrapper, id_table) {

        const wrapper = document.getElementById(id_wrapper);
        const table = document.getElementById(id_table);

        if (!wrapper || !table) {
            console.warn("⚠️ Wrapper atau table tidak ditemukan");
            return;
        }

        const tbody = table.querySelector("tbody");

        if (!tbody) {
            console.warn("⚠️ Tbody tidak ditemukan");
            return;
        }

        const rows = tbody.querySelectorAll("tr");
        const totalRows = rows.length;

        // Tidak ada data
        if (totalRows === 0) {
            wrapper.style.height = "auto";
            wrapper.style.maxHeight = "none";
            wrapper.style.overflowY = "hidden";
            wrapper.style.overflowX = "auto";
            return;
        }

        // Tinggi 1 row
        const rowHeight = rows[0].getBoundingClientRect().height;

        // Tinggi header
        const thead = table.querySelector("thead");

        const headerHeight = thead
            ? thead.getBoundingClientRect().height
            : 0;

        // Maksimal data yang ditampilkan
        const maxRows = 12;

        // Tinggi body maksimal 12 row
        const bodyHeight = rowHeight * maxRows;

        // Tinggi wrapper = header + 12 row
        const wrapperHeight = headerHeight + bodyHeight;

        wrapper.style.height = wrapperHeight + "px";
        wrapper.style.maxHeight = wrapperHeight + "px";

        // Scroll hanya kalau data lebih dari 12
        // if (totalRows > maxRows) {
        //     wrapper.style.overflowY = "auto";
        // } else {
        //     wrapper.style.overflowY = "hidden";
        // }

        // Horizontal scroll tetap aktif
        wrapper.style.overflowY = "auto";
        wrapper.style.overflowX = "auto";

        // Sticky header
        if (thead) {
            thead.style.position = "sticky";
            thead.style.top = "0";
            thead.style.zIndex = "10";
            thead.style.backgroundColor = "#f8f9fa";
        }

        console.log(`📊 Total rows: ${totalRows}`);
        console.log(`📊 Max rows: ${maxRows}`);
        console.log(`📐 Row height: ${rowHeight}px`);
        console.log(`📐 Wrapper height: ${wrapperHeight}px`);
        console.log(`📜 Scroll: ${totalRows > maxRows ? "ON" : "OFF"}`);
    }
};

// Tetap set ke window untuk akses global
window.library = library;