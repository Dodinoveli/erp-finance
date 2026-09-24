
const bankAccontsEdit = {

    init: function () {
        this.bindEvents();
    },

    bindEvents: function () {
        const self = this;
        // self.showCoa();
        const accountId = document.getElementById("account_id");

        const bankName = document.getElementById("bank_name");
        const bankBranch = document.getElementById("bank_branch");
        const accountNumber = document.getElementById("account_number");
        const accountHolder = document.getElementById("account_holder");

        const form = document.getElementById("formBankAccountEdit");
        if (form.dataset.bound === "true") return;
        form.dataset.bound = "true";

        console.log("Form formBankAccount ditemukan, memasang event listener...");
        form.addEventListener("submit", function (e) {
            e.preventDefault();
            console.log("Submit terdeteksi!");
            self.updateBankAccount(this);
        });

        if (accountId) {
            accountId.addEventListener("change", function () {
                console.log("akun id ", accountId)
                // let txt = this.options[this.selectedIndex].text;
                const option = this.options[this.selectedIndex];
                let parentId = this.value;
                console.log("akun id ", parentId)
                if (parentId !== "" && option.dataset.parentAccountName === "KAS") {
                    console.log("ini kas : ", option.dataset.parentAccountName)
                    bankName.readOnly = true;
                    bankName.classList.add("input-readonly");

                    bankBranch.readOnly = true;
                    bankBranch.classList.add("input-readonly");

                    accountNumber.readOnly = true;
                    accountNumber.classList.add("input-readonly");

                    accountHolder.readOnly = true;
                    accountHolder.classList.add("input-readonly");

                    // kosongkan field
                    document.getElementById("bank_name").value = "";
                    document.getElementById("bank_branch").value = "";
                    document.getElementById("account_number").value = "";
                    document.getElementById("account_holder").value = "";
                    document.querySelector('[name="accountType"]').value = option.dataset.parentAccountName;
                } else {
                    console.log("ini Bank : ", option.dataset.parentAccountName)
                    bankName.readOnly = false;
                    bankName.classList.add("input-readonly");

                    bankBranch.readOnly = false;
                    bankBranch.classList.add("input-readonly");

                    accountNumber.readOnly = false;
                    accountNumber.classList.add("input-readonly");

                    accountHolder.readOnly = false;
                    accountHolder.classList.add("input-readonly");
                    document.querySelector('[name="accountType"]').value = option.dataset.parentAccountName;
                }

            });
        }
    },

    // showCoa: async function () {
    //     const url = "/api/v1/bankaccounts/find-cash-and-bank-accounts-by-company-id";
    //     try {
    //         const response = await fetch(url);
    //         if (!response.ok) {
    //             throw new Error(`HTTP error! status: ${response.status}`);
    //         }
    //         const result = await response.json();
    //         if (result.status === "success") {
    //             const selectElement = document.getElementById('account_id');
    //             selectElement.innerHTML = '<option value="">-- Pilih Tipe Akun --</option>';
    //             const coaList = result.data;
    //             coaList.forEach(ac => {
    //                 const option = document.createElement('option');
    //                 option.value = ac.childAccountId;
    //                 option.dataset.accountName = ac.accountName;
    //                 option.textContent = ac.childAccountCode + '-' + ac.childAccountName;
    //                 selectElement.appendChild(option);
    //             });
    //         }
    //     } catch (error) { }
    // },

    updateBankAccount: async function (formEl) {
        const formData = new FormData(formEl);
        const id = formData.get("bankAccountId");
        const payload = Object.fromEntries(formData.entries());
        if (id == null) alert("id tidak valid")
        this.showErrors({}, formEl);
        Toast.loading("Menyimpan data...");
        try {

            const response = await fetch(`/api/v1/bankaccounts/${id}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
                body: JSON.stringify(payload),
            }
            );
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
                var url = `/bankaccount/detail/${id}`;

                htmx
                    .ajax("GET", url, {
                        target: "#page-content",
                        swap: "innerHTML",
                        indicator: "#loading"
                    })
                    .then(() => {
                        window.history.pushState({}, "", url);
                    });
            }, 2500);


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

export function initBankAccountEdit() {
    bankAccontsEdit.init()
}
