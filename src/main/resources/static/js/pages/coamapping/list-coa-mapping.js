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

		ajax: async function(data, callback) {
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
				render: function(data) {
					return `<span class="badge bg-secondary">${data} Data</span>`;
				},
			},
			{
				data: null,
				orderable: false,
				searchable: false,
				render: function(data, type, row) {
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

	// window.openCoaMappingDetail = function (transactionType) {
	//   const modalEl = document.getElementById("detailModal");
	//   const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
	//   modal.show();

	//   htmx.ajax("GET", `/api/v1/coamapping/detail/${transactionType}`, {
	//     target: "#detailModalContent",
	//     swap: "innerHTML",
	//     indicator: "#loading",
	//   });
	// };
	window.openCoaMappingDetail = function(transactionType) {
		const modalEl = document.getElementById("detailModal");
		const modal = bootstrap.Modal.getOrCreateInstance(modalEl);

		modal.show();

		const content = document.getElementById("detailModalContent");

		content.innerHTML = `
        <div class="text-center py-4">
            Memuat detail...
        </div>
    `;

		fetch(`/api/v1/coamapping/detail/${transactionType}`)
			.then((response) => {
				if (!response.ok) {
					throw new Error("Gagal mengambil detail mapping");
				}
				return response.json();
			})
			.then((data) => {
				if (!data || data.length === 0) {
					content.innerHTML = `
                    <div class="text-center text-muted py-4">
                        Belum ada detail mapping.
                    </div>
                `;
					return;
				}

				let rows = "";

				data.forEach((item) => {
					rows += `
                    <tr data-id="${item.mappingId}">
                        <td>${item.transactionType}</td>
                        <td>${item.paymentType}</td>
                        <td>${item.description}</td>
                        <td>
                          <button type="button"
                              class="btn btn-sm btn-outline-primary"
                              onclick="editRow(this)">
                              <i class="bi bi-pencil"></i> Edit
                          </button>
                        </td>
                    </tr>
                `;
				});

				content.innerHTML = `
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead>
                            <tr>
                                <th>Jenis Transaksi</th>
                                <th>Jenis Pembayaran</th>
                                <th>Deskripsi</th>
                                <th>Aksi</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${rows}
                        </tbody>
                    </table>
                </div>
            `;
			})
			.catch((error) => {
				console.error(error);

				content.innerHTML = `
                <div class="alert alert-danger mb-0">
                    Gagal memuat detail mapping.
                </div>
            `;
			});
	};

	window.editRow = function(button) {
		const row = button.closest("tr");

		// const transactionType = row.children[0].textContent.trim();
		// const paymentType = row.children[1].textContent.trim();
		const description = row.children[2].textContent.trim();

		// row.children[0].innerHTML = `
		//     <input type="text"
		//            class="form-control form-control-sm"
		//            value="${transactionType}">
		// `;

		// row.children[1].innerHTML = `
		//     <input type="text"
		//            class="form-control form-control-sm"
		//            value="${paymentType}">
		// `;

		row.children[2].innerHTML = `
        <input type="text"
               class="form-control form-control-sm"
               value="${description}">
    `;

		button.outerHTML = `
        <button type="button"
                class="btn btn-sm btn-success"
                onclick="updateCoaMapping(this)">
            <i class="bi bi-check-lg"></i> Simpan
        </button>
    `;
	};

	window.updateCoaMapping = async function(button) {
		const row = button.closest("tr");
		const mappingId = row.dataset.id;
		const description = row.children[2].querySelector("input").value.trim();
		Toast.loading("Sedang menyimpan perubahan...");
		const data = {
			description: description,
		};

		try {
			const response = await fetch(`/api/v1/coamapping/${mappingId}`, {
				method: "PUT",
				headers: {
					"Content-Type": "application/json",
				},
				credentials: "include",
				body: JSON.stringify(data),
			});

			if (!response.ok) {
				throw new Error("Gagal menyimpan data");
			}

			const result = await response.json();

			// tampilkan kembali sebagai text
			row.children[2].textContent = result.data.description;

			// ubah tombol kembali menjadi Edit
			button.outerHTML = `
            <button type="button"
                    class="btn btn-sm btn-outline-primary"
                    onclick="editRow(this)">
                <i class="bi bi-pencil"></i> Edit
            </button>
        `;
			await Toast.success(result.message, "Sukses!", 2000);
		} catch (error) {
			console.error("Gagal menyimpan:", error);

			alert("Gagal menyimpan perubahan.");
		}
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

	table.on("draw", function() {
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
