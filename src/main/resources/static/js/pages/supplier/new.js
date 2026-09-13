var supplierNew = {
    init: function () {
        this.bindEvents();
    },

    bindEvents: function () {
        const self = this;
        const form = document.getElementById("formSupplier");
        if (form.dataset.bound === "true") return;
        form.dataset.bound = "true";
        console.log("Form Client supplier, memasang event listener...");
        form.addEventListener("submit", function (e) {
            e.preventDefault();
            console.log("Submit terdeteksi!");
            self.createSupplier(form);
        });
    },

    createSupplier: async function (formEl) {
        const formData = new FormData(formEl);
        const payload = Object.fromEntries(formData.entries());
        this.showErrors({}, formEl);
        Toast.loading("Menyimpan data...");

        try {
            const url = `/api/v1/suppliers`;
            var response = await fetch(url, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
                body: JSON.stringify(payload),
            });

            const result = await response.json();
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
                var url = "/supplier";
                htmx
                    .ajax("GET", url, {
                        target: "#page-content",
                        swap: "innerHTML",
                        indicator: "#loading",
                    })
                    .then(() => {
                        window.history.pushState({}, "", url);
                    });
            }, 1500);

            return result;
        } catch (err) {
            console.log("Full Error Object = ", err);
            const errors = err?.errors || err?.data?.errors;
            if (errors) {
                this.showErrors(errors, formEl);
                Toast.error(err.message, "Error", 3000);
            } else {
                Toast.error(err.message, "Opps Error", 3000);
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

export function initSupplirNew() {
    supplierNew.init();
}

