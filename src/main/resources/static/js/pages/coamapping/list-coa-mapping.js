export function initCoaMappingList() {
  var table = new DataTable("#coamappingTable", {
    ordering: false,
    processing: true,
    serverSide: true,
    columnDefs: [
      { targets: 0, width: "250px" },
      { targets: 1, width: "150" },
      { targets: 2, width: "100px" },
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
        const response = await fetch(`/api/v1/coamapping?${params}`, {
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
      { data: "transactionType" },
      {
        data: "total",
        render: function (data) {
          return `<span class="badge bg-secondary">${data} Data</span>`;
        },
      },
      {
        data: null,
        orderable: false,
        searchable: false,
        render: function (data, type, row) {
          return `
            <button type="button"
                    class="detail-actions-btn"
                    title="Lihat Detail"
                    onclick="openCoaMappingDetail('${row.transactionType}')">
                <i class="bi bi-eye"></i>
            </button>
        `;
        },
      },
    ],
  });

  window.openCoaMappingDetail = function (transactionType) {
    const modalEl = document.getElementById("detailModal");
    const modal = bootstrap.Modal.getOrCreateInstance(modalEl);

    modal.show();

    htmx.ajax("GET", `/coamapping/detail/${transactionType}`, {
      target: "#detailModalContent",
      swap: "innerHTML",
      indicator: "#loading",
    });
  };

  // coamappingTable_wrapper di buat otomatis oleh data tables
  var searchBox = document.querySelector("#coamappingTable_wrapper .dt-search");

  if (searchBox && !searchBox.querySelector(".btn-add")) {
    searchBox.insertAdjacentHTML(
      "beforeend",
      `
        <button type="button"  class="btn btn-primary btn-sm ms-2 d-inline-flex align-items-center justify-content-center btn-add"  
            data-bs-toggle="modal" data-bs-target="#modalCoaMapping">
           <i class="bi bi-plus"></i>
            </button>
        `,
    );
  }

  table.on("draw", function () {
    htmx.process(document.querySelector("#coamappingTable tbody"));
  });

  // async function total() {
  //     const url = "/api/v1/clients/total-new";

  //     try {
  //         const response = await fetch(url, {
  //             method: "GET",
  //             credentials: "include",
  //         });
  //         if (!response.ok) {
  //             throw new Error(`Response status: ${response.status}`);
  //         }
  //         const result = await response.json();
  //         document.getElementById("total-client").textContent = result.total;
  //         document.getElementById("client-aktif").textContent =
  //             result.totalActive;
  //         document.getElementById("client-nonaktif").textContent =
  //             result.totalInactive;
  //         document.getElementById("client-baru").textContent =
  //             result.totalNew;
  //     } catch (error) { }
  // }

  // total();
}
