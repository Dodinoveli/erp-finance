const projectEdit = {
    clientsLoaded: false,

    init: function () {
        this.bindEvents();
        this.bindClientEvents();
    },

    bindEvents: function () {
        const self = this;
        // self.showClient();
        const form = document.getElementById("formProjectEdit");

        if (form.dataset.bound === "true") return
        form.dataset.bound = "true";

        form.addEventListener("submit", (e) => {
            e.preventDefault();
            this.updateProject(form);
        });
    },

    bindClientEvents: function () {
        const select = document.getElementById("client_id");
        if (!select) {
            return;
        }
        select.addEventListener("focus", () => {
            this.loadClients();
        });
    },

    loadClients: async function () {
        console.log("jalankan klient ")
        if (this.clientsLoaded) {
            return;
        }
        const selectElement = document.getElementById('client_id');
        const currentClientId = selectElement.value;

        try {
            const url = "/api/v1/project/clients";
            const response = await fetch(url);

            if (!response.ok) {
                throw new Error(`Response status: ${response.status}`);
            }

            const result = await response.json();
            console.log(result);

            if (result.status === "success") {

                if (!selectElement) {
                    return;
                }

                const client = result.data;
                selectElement.innerHTML = "";
                client.forEach(client => {
                    const option = document.createElement('option');
                    option.value = client.clientId;
                    option.textContent = `${client.clientName}`;
                    if (String(client.clientId) === String(currentClientId)) {
                        option.selected = true;
                    }

                    selectElement.appendChild(option);
                });
                this.clientsLoaded = true;
            }
        } catch (e) {
            console.log("error project")
        }
    },

    updateProject: async function (formEl) {
        const formData = new FormData(formEl);
        const id = formData.get("projectId");
        const payload = Object.fromEntries(formData.entries());
        this.showErrors({}, formEl);
        Toast.loading("Menyimpan data...");
        try {
            const url = `/api/v1/project/${id}`;
            const response = await fetch(url, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
                body: JSON.stringify(payload)
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
            await Toast.success(result.message, "Berhasil", 2000);
            setTimeout(() => {
                library.navigate(`/project/detail/${id}`);
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

export function initProjectEdit() {
    projectEdit.init();
}

