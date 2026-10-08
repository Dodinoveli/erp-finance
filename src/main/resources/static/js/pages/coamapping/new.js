var coamapping = {
	init: function() {
		this.bindEvents();
	},

	bindEvents: function() {
		const self = this;

		const form = document.getElementById("formCoaMappingNew");
		if (form.dataset.bound === "true") return;
		form.dataset.bound = "true";

		console.log("Form formBankAccount ditemukan, memasang event listener...");
		form.addEventListener("submit", function(e) {
			e.preventDefault();
			console.log("Submit terdeteksi!");
			self.create(this);
		});
	},

	create: async function(formEl) {
		const formData = new FormData(formEl);
		const id = formData.get("mappingId");
		const payload = Object.fromEntries(formData.entries());
		this.showErrors({}, formEl);
		if (!id) {
			this.save(formEl, payload);
		} else {
		}
	},

	save: async function(formEl, payload) {
		var table = new DataTable("#coamappingTable");
		Toast.loading("Sedang menyimpan data...");
		try {
			var response = await fetch(`/api/v1/coamapping`, {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
				},
				credentials: "include",
				body: JSON.stringify(payload),
			});

			var result = await response.json();
			if (!response.ok) {
				throw {
					status: response.status,
					message: result?.message,
					errors: result?.errors,
					data: result,
				};
			}
			await Toast.success(result.message, "Sukses!", 2000);
			// Modal tetap terbuka
			formEl.reset();
			table.ajax.reload(null, false);
			return result;
		} catch (err) {
			console.log("Full Error Object = ", err);
			const errors = err?.errors || err?.data?.errors;
			if (errors) {
				this.showErrors(errors, formEl);
				Toast.error(err.message, "Gagal menyimpan data", 3000);
			} else {
				Toast.error(err.message, "Opps Error", 3000);
			}
			throw err;
		}
	},

	showErrors: function(errors, formEl) {
		if (!formEl) return; // 🔥 guard biar gak error lagi

		// 🔥 reset hanya di form ini
		formEl
			.querySelectorAll(".is-invalid")
			.forEach((i) => i.classList.remove("is-invalid"));

		formEl.querySelectorAll('[id^="err-"]').forEach((e) => (e.innerText = ""));

		// 🔥 set error
		for (const field in errors) {
			const input = formEl.querySelector(`[name = "${field}"]`);
			const errorEl = formEl.querySelector("#err-" + field);

			if (input) input.classList.add("is-invalid");
			if (errorEl) errorEl.innerText = errors[field];
		}
	},
};
export function initCoamapping() {
	coamapping.init();
}
