
import { library } from '/js/utility/library.js';
export function initBankAccountList() {
    var table = new DataTable("#bankTable", {
        paging: false,
        ordering: false,
        processing: true,
        serverSide: true,
        columnDefs: [
            { targets: 0, width: '50px' },   // Kode
            { targets: 1, width: '200px' },  // Nama
            { targets: 2, width: '120px' },  // Kontak
            {
                targets: 3, width: '120px', type: 'string',
                className: 'text-start',
                orderable: false
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
        },

        ajax: async function (data, callback) {
            console.log("DATATABLES DATA:", data);
            const keyword = data.search?.value?.trim() || "";
            // const params = new URLSearchParams({
            //     draw: data.draw,
            //     keyword: data.search?.value?.trim() || "",
            // });

            // console.log("KEYWORD JS =", keyword);
            try {
                const response = await fetch(`/api/v1/bankaccounts`, {
                    credentials: "include",
                });
                console.log("RESPONSE:", response);
                const result = await response.json();

                callback({
                    draw: data.draw,
                    data: result.data
                });
            } catch (error) {
                console.error("Gagal mengambil data:", error);

                callback({
                    draw: data.draw,
                    data: [],
                });
            }
        },

        lengthMenu: [
            [10, 25, 50, 100],
            [10, 25, 50, 100]
        ],
        columns: [
            { data: "accountCode" },
            { data: "accountName" },
            { data: "bankName" },
            { data: "accountNumber" },
            { data: "normalBalance" },
            {
                data: "isActive",
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
                <a href="/client/detail/${row.clientId}"
                  hx-get="/client/detail/${row.clientId}"
                  hx-target="#page-content"
                  hx-push-url="true"
                  hx-indicator="#loading"
                  class="btn btn-primary btn-sm"
                  title="Lihat Detail"
                  style="text-decoration:none">
                    <i class="bi bi-eye"></i>
                </a>`;
                },
            },
        ],
    });

    var searchBox = document.querySelector(
        "#bankTable_wrapper .dt-search",
    );

    if (searchBox && !searchBox.querySelector(".btn-add")) {
        searchBox.insertAdjacentHTML(
            "beforeend",
            `
                    <a href="/bankaccount/new"
                        class="btn btn-primary btn-sm ms-2 d-inline-flex align-items-center justify-content-center btn-add"
                        hx-get="/bankaccount/new"
                        hx-target="#page-content" hx-push-url="true" hx-indicator="#loading">
                    <i class="bi bi-plus"></i>
                    </a>`,
        );
    }


    table.on("draw", function () {
        htmx.process(document.querySelector("#bankTable tbody"));
    });
}
