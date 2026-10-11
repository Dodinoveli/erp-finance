var newCoaMappingLines = {
	init: function() {
		this.bindEvents();
	},

	bindEvents: function() {
		const self = this;
		self.showCoaMapping();
		self.showCoa();

		const add = document.getElementById("tambah-detail-jurnal");
		add.addEventListener("click", function() {
			self.tambahData()
		})

		const simpan = document.getElementById("btn-simpan");
		simpan.addEventListener("click", function() {
			self.simpanData()
		})
	},

	showCoaMapping: async function() {
		const url = "/api/v1/coamappingline/find-coa-mapping-by-company-id";
		try {
			const response = await fetch(url);
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const result = await response.json();
			if (result.status === "success" || result.code === 200 || result.message === "success") {
				const selectElement = document.getElementById('mapping_id');
				selectElement.innerHTML = '<option value="">-- Pilih --</option>';
				result.data.forEach(mapping => {
					const option = document.createElement('option');
					option.value = mapping.mappingId;
					//option.dataset.accountName = ac.accountName;
					option.textContent = `${mapping.transactionType} - ${mapping.paymentType} - ${mapping.description}`;
					selectElement.appendChild(option);
				});
			}
		} catch (error) {
			console.log(error)
		}
	},

	showCoa: async function() {
		const url = "/api/v1/coamappingline/find-coa-by-company-id";
		try {
			const response = await fetch(url);
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const result = await response.json();
			if (result.status === "success" || result.code === 200 || result.message === "success") {
				const selectElement = document.getElementById('account_id');
				selectElement.innerHTML = '<option value="">-- Pilih Coa --</option>';
				result.data.forEach(mapping => {
					const option = document.createElement('option');
					option.value = mapping.accountId;
					//option.dataset.accountName = ac.accountName;
					option.textContent = `${mapping.accountCode} - ${mapping.accountName}`;
					selectElement.appendChild(option);
				});
			}
		} catch (error) {
			console.log(error)
		}
	},

	tambahData: function() {
		var accountIdSelect = document.querySelector("#account_id");
		var positionSelect = document.querySelector("#position");
		var amountTypeSelect = document.querySelector("#amount_type");
		var isActive = document.querySelector("#is_active");

		const accountId = accountIdSelect.value;
		const accountName = accountIdSelect.options[accountIdSelect.selectedIndex].text;
		const position = positionSelect.value;
		const amountType = amountTypeSelect.value;
		const isActiveCh = isActive.checked;

		if (!accountId || !position || !amountType) {
			alert("Silakan pilih akun , posisi atau sumber nominal.");
			return;
		}

		const tabel = document.querySelector("#detailJurnalTableBody");

		// Cek apakah akun sudah ada di tabel 
		const isDuplicate = Array.from(tabel.rows).some(row => row.dataset.accountId === accountId);
		if (isDuplicate) {
			alert("Akun COA tersebut sudah ditambahkan!");
			return;
		}

		const row = tabel.insertRow();

		// untuk simpan ke db
		row.dataset.accountId = accountId;
		row.dataset.position = position;
		row.dataset.amountType = amountType;
		row.dataset.isActive = isActiveCh;

		// untuk tampil ke ui
		row.insertCell(0).textContent = accountName;
		row.insertCell(1).textContent = position;
		row.insertCell(2).textContent = amountType;
		row.insertCell(3).textContent = isActiveCh;

		// Kolom tombol hapus 
		const actionCell = row.insertCell(4);
		const deleteButton = document.createElement("button");
		deleteButton.type = "button";
		deleteButton.className = "btn btn-sm btn-outline-danger";
		deleteButton.textContent = "Hapus";
		deleteButton.addEventListener("click", function() {
			row.remove();
		}
		);
		actionCell.appendChild(deleteButton);
	},

	simpanData: async function() {

		const mappingId = document.querySelector("#mapping_id").value;
		const tabel = document.querySelector("#detailJurnalTableBody");

		if (!mappingId) {
			alert("Mapping COA belum dipilih.");
			return;
		}

		if (!tabel.rows.length) {
			alert("Detail COA Mapping belum ditambahkan.");
			return;
		}

		const lines = Array.from(tabel.rows).map(row => ({
			coaId: row.dataset.accountId,
			position: row.dataset.position,
			amountType: row.dataset.amountType,
			isActive: row.dataset.isActive === "true"
		}));

		try {
			Toast.loading("Menyimpan data...");
			const response = await fetch(
				`/api/v1/coamappingline/${mappingId}/lines`,
				{
					method: "POST",
					headers: {
						"Content-Type": "application/json"
					},
					body: JSON.stringify(lines)
				}
			);
			
			const result = await response.json();
			if (!response.ok) {
				Toast.error(result.message || "Gagal menyimpan detail COA Mapping.", "Gagal", 3000); return;
			}


			// Jika penyimpanan berhasil 
			await Toast.success( result.message || "Detail COA Mapping berhasil disimpan.", "Berhasil!", 2000 );
		} catch (error) {
			console.error("Kesalahan:", error);
			//alert(error.message || "Terjadi kesalahan saat menyimpan data.");
			Toast.error(error.message, "Gagal", 3000)
		}
	}
}
     
export function initNewCoaMappingLines() {
	newCoaMappingLines.init();
}