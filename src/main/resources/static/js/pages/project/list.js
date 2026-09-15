
import { library } from '/js/utility/library.js';
export function initProjectList() {
    function formatRupiah(data) {
        return library.formatRupiah(data);
    }

    function formatDateIndonesia(data) {
        return library.formatDateIndonesia(data);
    }
    var table = new DataTable("#projectTable", {
        ordering: false,
        processing: true,
        serverSide: true,
        columnDefs: [
            { targets: 0, width: '50px' },   // Kode
            { targets: 1, width: '100px' },  // Nama
            { targets: 2, width: '100px' },  // Kontak
            {
                targets: 3, width: '200px',
            },
            { targets: 4, width: '100px' },
            { targets: 5, width: '200px', type: 'string', },
            { targets: 6, width: '150px' },
            // { targets: 7, width: '60px' },
            { targets: 7, width: '60px' },  // aksi
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
                const response = await fetch(`/api/v1/project?${params}`, {
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
            [10, 25, 50, 100]
        ],
        columns: [
            { data: "projectCode" },
            { data: "projectPo" },
            {
                data: "poDate",
                render: function (data) {
                    return formatDateIndonesia(data);
                }
            },
            { data: "name" },
            { data: "projectType" },
            { data: "clientName" },
            {
                data: "contractValue",
                render: function (data, type, row) {
                    // data = contractValue
                    // row = seluruh data object (termasuk ppn jika ada)

                    if (type === 'display' || type === 'filter') {
                        // const contractValue = data || 0;
                        // const ppn = row.ppn || 0; // Ambil ppn dari data
                        // const total = contractValue + ppn;

                        return `
                                <div>
                                    <div><strong>Status: ${row.taxType}</strong></div>
                                    <div>Nilai: ${formatRupiah(row.contractValue)}</div>
                                    <div>DPP: ${formatRupiah(row.dpp)}</div>
                                    <div>PPN 11%: ${formatRupiah(row.totalTax)}</div>
                                    <div><strong>Total: ${formatRupiah(row.totalAmount)}</strong></div>
                                </div>
                            `;
                    }
                    return data;
                }
            },

            {
                data: null,
                orderable: false,
                searchable: false,
                render: function (data, type, row) {
                    return ` 
                            <a href="/project/detail/${row.projectId}"
                            onclick="event.preventDefault(); library.navigate('/project/detail/${row.projectId}')"
                            class="detail-actions-btn"
                            title="Lihat Detail"
                            style="text-decoration:none">
                                <i class="bi bi-eye"></i>
                            </a>`;
                },
            },
        ],
    });

    var searchBox = document.querySelector(
        "#projectTable_wrapper .dt-search",
    );

    if (searchBox && !searchBox.querySelector(".btn-add")) {
        searchBox.insertAdjacentHTML(
            "beforeend",
            `
                    <a href="/project/new"
                        class="btn btn-primary btn-sm ms-2 d-inline-flex align-items-center justify-content-center btn-add"
                        onclick="event.preventDefault(); library.navigate('/project/new')">
                    <i class="bi bi-plus"></i>
                    </a>`,
        );
    }

    table.on("draw", function () {
        htmx.process(document.querySelector("#projectTable tbody"));
    });

    async function total() {
        // const url = "/api/v1/clients/total-new";

        // try {
        //     const response = await fetch(url, {
        //         method: "GET",
        //         credentials: "include",
        //     });
        //     if (!response.ok) {
        //         throw new Error(`Response status: ${response.status}`);
        //     }
        //     const result = await response.json();
        //     document.getElementById("total-client").textContent = result.total;
        //     document.getElementById("client-aktif").textContent =
        //         result.totalActive;
        //     document.getElementById("client-nonaktif").textContent =
        //         result.totalInactive;
        //     document.getElementById("client-baru").textContent =
        //         result.totalNew;
        // } catch (error) { }
    }

    total();
}


