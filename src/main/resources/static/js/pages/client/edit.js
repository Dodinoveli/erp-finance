import { library } from '/js/utility/library.js';
var clientModelEdit = {
    init: function () {
        this.bindEvents();
        // this.generateCode();
    },

    bindEvents: function () {
        const self = this;
        const form = document.getElementById("formClientEdit");
        if (!form) {
            console.log("Form Client ditemukan, memasang event listener...");
            return;
        }

        if (form.dataset.bound === "true") return;
        form.dataset.bound = "true";

        form.addEventListener("submit", (e) => {
            e.preventDefault();
            this.udateClient(form);
        });
    },

    udateClient: async function (formEl) {
        var formData = new FormData(formEl);

        var id = formData.get("clientId");
        if (!id) {
            Toast.error("ID Client tidak ditemukan", "Gagal");
            return;
        }

        var payload = Object.fromEntries(formData.entries());
        console.log("CREATE CLIENT PAYLOAD:", payload);
        this.showErrors({}, formEl);
        Toast.loading("Menyimpan data...");
        try {
            var response = await fetch(`/api/v1/clients/${id}`, {
                method: "PUT",
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
                    message: result?.message || "Gagal menyimpan data",
                    errors: result?.errors,
                    data: result,
                };
            }

            await Toast.success(result.message, "Berhasil!", 2000);
            setTimeout(() => {
                library.navigate(`/client/detail/${id}`);
            }, 1000);
            return result;
        } catch (err) {
            const errors = err?.errors || err?.data?.errors;
            if (errors) {
                this.showErrors(errors, formEl);
                Toast.error(
                    err.message,
                    "Gagal Menyimpan data, periksa inputan",
                    3000,
                );
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


export function initClientEdit() {
    clientModelEdit.init();
}
