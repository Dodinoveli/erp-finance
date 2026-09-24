
import { library } from '/js/utility/library.js';
export function initBankAccountList() {
    var table = new DataTable("#bankTable", {
        paging: false,
        ordering: false,
        processing: true,
        serverSide: false,

        columnDefs: [
            { targets: 0, width: '50px' },   // Kode
            { targets: 1, width: '120px' },  // BankAccount
            {
                targets: 2, width: '100px', type: 'string',
                className: 'text-start',
                orderable: false
            },  // Coa
            {
                targets: 3, width: '120px'
            }, // parent
            { targets: 4, width: '120px' },  // Aksi
            { targets: 5, width: '120px' },  // Aksi

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
            {
                data: null,
                render: (data, type, row) => `
                <div class="d-flex flex-column gap-1">
                    <div><span class="text-muted">Bank:</span> <strong>${row.bankName || '-'}</strong></div>
                    <div><span class="text-muted">Cabang:</span> ${row.bankBranch || '-'}</div>
                    <div><span class="text-muted">Rekening:</span> ${row.accountNumber || '-'}</div>
                    <div><span class="text-muted">A.N:</span> ${row.accountHolder || '-'}</div>
                </div>
                `
            },
            { data: "openingBalance" },
            {
                data: null,
                render: (data, type, row) => `
                <div>
                    <div class="small text-muted">
                        Parent: ${row.coa?.parentAccountCode || '-'} · ${row.coa?.parentAccountName || '-'}
                    </div>
                    <div class="fw-semibold">
                        ${row.coa?.childAccountCode || '-'} · ${row.coa?.childAccountName || '-'}
                    </div>
                </div>
            `
            },
            // {
            //     data: null,
            //     render: (data, type, row) =>
            //         `${row.coa.parentAccountCode} - ${row.coa.parentAccountName}`
            // },
            // {
            //     data: "isActive",
            //     render: function (data) {
            //         return data
            //             ? '<span class="badge bg-success">Aktif</span>'
            //             : '<span class="badge bg-secondary">Tidak Aktif</span>';
            //     },
            // },
            {
                data: null,
                orderable: false,
                searchable: false,
                render: function (data, type, row) {
                    return ` 
                <a href="/bankaccount/detail/${row.bankAccountId}"
                onclick="event.preventDefault(); library.navigate('/bankaccount/detail/${row.bankAccountId}')"
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
        "#bankTable_wrapper .dt-search",
    );

    if (searchBox && !searchBox.querySelector(".btn-add")) {
        searchBox.insertAdjacentHTML(
            "beforeend",
            `
                    <a href="/bankaccount/new"
                        class="btn btn-primary btn-sm ms-2 d-inline-flex align-items-center justify-content-center btn-add"
                        onclick="event.preventDefault(); library.navigate('/bankaccount/new')">
                    <i class="bi bi-plus"></i>
                    </a>`,
        );
    }


    table.on("draw", function () {
        htmx.process(document.querySelector("#bankTable tbody"));
    });
}
