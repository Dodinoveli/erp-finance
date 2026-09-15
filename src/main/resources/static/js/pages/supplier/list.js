export function initSupplirList() {
    var table = new DataTable("#supplierTable", {
        ordering: false,
        processing: true,
        serverSide: true,
        columnDefs: [
            { targets: 0, width: '50px' },   // Kode
            { targets: 1, width: '200px' },  // Nama
            { targets: 2, width: '120px' },  // Kontak
            {
                targets: 3, width: '120px', type: 'string',
            }, // telp
            { targets: 4, width: '150px' },  // email
            { targets: 5, width: '100px' },  // status
            { targets: 6, width: '60px' },  // aksi
        ],
        layout: {
            topStart: {
                pageLength: true,
            },
            topEnd: {
                search: true,
            },
        },
        language: {
            search: "",
            searchPlaceholder: "Cari nama...",
            lengthMenu: "_MENU_ Data Perhalaman",
            info: "Menampilkan _START_–_END_ dari _TOTAL_ data",
            infoEmpty: "Tidak ada data",
            zeroRecords: "Data tidak ditemukan",
            emptyTable: "Belum ada data",
            paginate: {
                first: "«",
                last: "»",
                next: "›",
                previous: "‹",
            },
        },

        ajax: async function (data, callback) {
            console.log("DATATABLES DATA:", data);
            const keyword = data.search?.value?.trim() || "";
            const params = new URLSearchParams({
                draw: data.draw,
                start: data.start,
                length: data.length,
                keyword: data.search?.value?.trim() || "",
            });

            console.log("KEYWORD JS =", keyword);
            try {
                const response = await fetch(`/api/v1/suppliers?${params}`, {
                    credentials: "include",
                });
                console.log("RESPONSE:", response);
                const result = await response.json();

                callback({
                    draw: data.draw,
                    recordsTotal: result.recordsTotal,
                    recordsFiltered: result.recordsFiltered,
                    data: result.data,
                });
            } catch (error) {
                console.error("Gagal mengambil data:", error);

                callback({
                    draw: data.draw,
                    recordsTotal: 0,
                    recordsFiltered: 0,
                    data: [],
                });
            }
        },

        pageLength: 10,
        lengthMenu: [
            [10, 25, 50, 100],
            [10, 25, 50, 100],
        ],
        columns: [
            { data: "supplierCode" },
            { data: "supplierName" },
            { data: "supplierContactPerson" },
            { data: "supplierEmail" },
            { data: "supplierType" },
            {
                data: "supplierIsActive",
                render: function (data) {
                    return data
                        ? '<span class="badge bg-success">Aktif</span>'
                        : '<span class="badge bg-secondary">Tidak Aktif</span>';
                },
            },
            {
                data: null,
                orderable: false,
                searchable: false,
                render: function (data, type, row) {
                    return `
                <a href="/supplier/detail/${row.supplierId}"
                    onclick="event.preventDefault(); library.navigate('/supplier/detail/${row.supplierId}')"
                    class="detail-actions-btn"
                    title="Lihat Detail">
                    <i class="bi bi-eye"></i>
                </a>`;
                },
            },
        ],
    });

    var searchBox = document.querySelector(
        "#supplierTable_wrapper .dt-search",
    );

    if (searchBox && !searchBox.querySelector(".btn-add")) {
        searchBox.insertAdjacentHTML(
            "beforeend",
            `
        <a href="/supplier/new"
            class="btn btn-primary btn-sm ms-2 d-inline-flex align-items-center justify-content-center btn-add"
            onclick="event.preventDefault(); library.navigate('/supplier/new')">
           <i class="bi bi-plus"></i>
        </a>`,
        );
    }

    table.on("draw", function () {
        htmx.process(document.querySelector("#supplierTable tbody"));
    });

    async function total() {
        const url = "/api/v1/suppliers/total-new";

        try {
            const response = await fetch(url, {
                method: "GET",
                credentials: "include",
            });
            if (!response.ok) {
                throw new Error(`Response status: ${response.status}`);
            }
            const result = await response.json();
            document.getElementById("total-supplier").textContent =
                result.total;
            document.getElementById("supplier-aktif").textContent =
                result.totalActive;
            document.getElementById("supplier-nonaktif").textContent =
                result.totalInactive;
            document.getElementById("supplier-baru").textContent =
                result.totalNew;
        } catch (error) { }
    }

    total();
}
