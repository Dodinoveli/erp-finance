import { library } from '/js/utility/library.js';
var projectNew = {

    init: function () {
        this.bindEvents();
    },

    bindEvents: function () {
        const self = this;
        self.showClient();
        const form = document.getElementById("formProjectNew");

        if (form.dataset.bound === "true") return;
        form.dataset.bound = "true";

        form.addEventListener("submit", (e) => {
            e.preventDefault();
            self.createNewProject(form);
        })
    },

    showClient: async function () {
        const url = "/api/v1/project/clients";
        try {
            const response = await fetch(url);

            // Cek HTTP status (500, 404, dll)
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            // Parse response body menjadi JSON
            const result = await response.json();
            if (result.status === "success") {
                const selectElement = document.getElementById('client_id');
                selectElement.innerHTML = '<option value="">-- Pilih Klien --</option>';
                const coaList = result.data;
                coaList.forEach(client => {
                    const option = document.createElement('option');
                    option.value = client.clientId;
                    option.textContent = `${client.clientName}`;
                    selectElement.appendChild(option);
                });
            }
        } catch (e) {
            console.log("error project")
        }
    },

    createNewProject: async function (formEl) {
        const formData = new FormData(formEl);
        const payload = Object.fromEntries(formData.entries());
        this.showErrors({}, formEl);

        try {
            var response = await fetch(`/api/v1/project`, {
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
            await Toast.success(result.message, "Berhasil!", 2000);
            setTimeout(() => {
                var url = "/project";
                library.navigate(url);
            }, 1500);

            return result;
        } catch (err) {
            console.log("Full Error Object = ", err);
            const errors = err?.errors || err?.data?.errors;
            if (errors) {
                this.showErrors(errors, formEl);
                Toast.error(err.message, "Gagal", 3000)
            } else {
                Toast.error(err.message, "Opps Error", 3000)
            }
            throw err;
        }
    },

    showErrors: function (errors, formEl) {
        if (!formEl) return; // 🔥 guard biar gak error lagi

        // 🔥 reset hanya di form ini
        formEl
            .querySelectorAll(".is-invalid")
            .forEach((i) => i.classList.remove("is-invalid"));

        formEl
            .querySelectorAll('[id^="err-"]')
            .forEach((e) => (e.innerText = ""));

        // 🔥 set error
        for (const field in errors) {
            const input = formEl.querySelector(`[name = "${field}"]`);
            const errorEl = formEl.querySelector("#err-" + field);

            if (input) input.classList.add("is-invalid");
            if (errorEl) errorEl.innerText = errors[field];
        }
    },
};

export function initProjectNew() {
    projectNew.init();
}

