/**
 * ini coa mapping line 
 */
export function InitCoaMappingLine() {
	var table = new DataTable("#coamappingTableLine", {
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
				const response = await fetch(`/api/v1/coamappingline?${params}`, {
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
			{
				data: null,
				render: function(data, type, row) {
					return `${row.transactionType} - ${row.paymentType}`;
				}
			},
			{ data: "position" },
			{ data: "lineOrder" },
			{ data: "" },
			{
				data: null,
				orderable: false,
				searchable: false,
				render: function(data, type, row) {
					return `
				            <button type="button"
				                    class="detail-actions-btn"
				                    title="Lihat Detail"
				                    onclick="">
				                <i class="bi bi-eye"></i>
				            </button>
				        `;
				},
			},
		],

	});

	var searchBox = document.querySelector("#coamappingTableLine_wrapper .dt-search");

	if (searchBox && !searchBox.querySelector(".btn-add")) {
		searchBox.insertAdjacentHTML(
			"beforeend",
			`
	    <button type="button"  class="btn btn-primary btn-sm ms-2 d-inline-flex align-items-center justify-content-center btn-add"  
	        data-bs-toggle="modal" data-bs-target="#modalCoaMappingLine">
	       <i class="bi bi-plus"></i>
	        </button>
	    `,
		);
	}

	table.on("draw", function() {
		htmx.process(document.querySelector("#coamappingTableLine tbody"));
	});
}