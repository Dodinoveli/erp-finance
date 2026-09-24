
const bankAccontsEdit = {

    init: function () {
        this.bindEvents();
    },

    bindEvents: function () {
        const self = this;
        const accountId = document.getElementById("account_id");

        const bankName = document.getElementById("bank_name");
        const bankBranch = document.getElementById("bank_branch");
        const accountNumber = document.getElementById("account_number");
        const accountHolder = document.getElementById("account_holder");

        const form = document.getElementById("formBankAccountEdit");
        if (form) {
            console.log("Form formBankAccount ditemukan, memasang event listener...");
            form.addEventListener("submit", function (e) {
                e.preventDefault();
                console.log("Submit terdeteksi!");
                // self.updateBankAccount(this);
            });
        } else {
            console.warn("Elemen #formBankAccount tidak ditemukan di DOM saat init!");
        }

        if (accountId) {
            accountId.addEventListener("change", function () {
                console.log("akun id ", accountId)
                let txt = this.options[this.selectedIndex].text;
                let parentId = this.value;
                console.log("akun id ", parentId)
                if (parentId !== null) {
                    // alert(val) #d9d1d1
                    console.log("akun id ", parentId)
                    bankName.readOnly = true;
                    bankName.style.background = "#d9d1d1";

                    bankBranch.readOnly = true;
                    bankBranch.style.background = "#d9d1d1";

                    accountNumber.readOnly = true;
                    accountNumber.style.background = "#d9d1d1";

                    accountHolder.readOnly = true;
                    accountHolder.style.background = "#d9d1d1";

                    // kosongkan field
                    document.getElementById("bank_name").value = "";
                    document.getElementById("bank_branch").value = "";
                    document.getElementById("account_number").value = "";
                    document.getElementById("account_holder").value = "";
                } else {
                    bankName.readOnly = false;
                    bankName.style.background = "#ffff";

                    bankBranch.readOnly = false;
                    bankBranch.style.background = "#ffff";

                    accountNumber.readOnly = false;
                    accountNumber.style.background = "#ffff";

                    accountHolder.readOnly = false;
                    accountHolder.style.background = "#ffff";
                }

                self.showCoa(parentId);
            });
        }
    },

    showCoa: async function (keyword) {
        const url = "/api/v1/bankaccounts/find-cash-and-bank-accounts-by-company-id";
        try {
            const response = await fetch(url);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const result = await response.json();
            if (result.status === "success") {
                const selectElement = document.getElementById('account_id');
                selectElement.innerHTML = '<option value="">-- Pilih Tipe Akun --</option>';
                const coaList = result.data;
                coaList.forEach(ac => {
                    const option = document.createElement('option');
                    option.value = ac.childAccountId;
                    option.textContent = ac.childAccountCode + '-' + ac.childAccountName;
                    selectElement.appendChild(option);
                });
            }
        } catch (error) { }
    },

    // updateBankAccount: async function (formEl) {
    //     const formData = new FormData(formEl);
    //     const id = formData.get("bankAccountId");
    //     const payload = Object.fromEntries(formData.entries());
    //     if (id == null) alert("id tidak valid")
    //     this.showErrors({}, formEl);

    //     try {

    //         const result = await fetch(`/api/v1/bankaccounts/${id}`, payload, "Mengubah Data...");
    //         if (result && result.success !== false) {
    //             await Toast.fire({
    //                 icon: 'success',
    //                 title: 'Berhasil!',
    //                 text: result.message,
    //                 timer: 3000,
    //                 showConfirmButton: false
    //             });
    //             setTimeout(() => {
    //                 window.location.href = `/bankaccount/detail/${id}`;
    //             }, 500);
    //             return result;
    //         } else {
    //             throw new Error(result?.message || 'Gagal Menyimpan data');
    //         }
    //     } catch (err) {
    //         console.log("Full Error Object = ", err);
    //         const errors = err?.errors || err?.data?.errors;
    //         if (errors) {
    //             this.showErrors(errors, formEl);
    //             Toast.fire({
    //                 icon: 'error',
    //                 title: 'Gagal Menyimpan Data, Periksa inputan'
    //             });
    //         } else {
    //             Swal.fire({
    //                 icon: "error",
    //                 title: "Oops...",
    //                 text: err.message,
    //             });
    //         }
    //         throw errors;
    //     }
    // },

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
