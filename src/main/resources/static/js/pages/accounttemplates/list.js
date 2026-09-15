export function initAccountTemplates() {

            const accountTemplates = {
                table: null,

                init: function () {
                    this.bindEvents()

                },

                bindEvents: function () {
                    this.initTable();

                    const btn = document.getElementById("btn-install");
                    if (btn) {
                        btn.addEventListener("click", (e) => {
                            e.preventDefault();
                            this.install();
                        })
                    }
                },

                initTable: function () {
                    this.table = new Tabulator("#coa-table", {

                        ajaxURL: "/api/v1/accounttemplates/tree",
                        ajaxResponse: function (url, params, response) {

                            return response.data;

                        },
                        height: "600px",

                        layout: "fitColumns",
                        responsiveLayout: "collapse",
                        dataTree: true,
                        dataTreeChildField: "children",
                        // dataTreeStartExpanded: true,
                        dataTreeChildIndent: 30,

                        dataTreeStartExpanded: function (row, level) {
                            return level === 0;
                        },

                        rowFormatter: function (row) {

                            const data = row.getData();
                            const element = row.getElement();

                            if (!data.children || data.children.length === 0) {
                                element.classList.add("coa-leaf");
                            } else {
                                element.classList.remove("coa-leaf");
                            }

                        },

                        columns: [
                            {
                                title: "Kode",
                                field: "accountCode",
                                width: 180
                            },
                            {
                                title: "Nama Akun",
                                field: "accountName",
                                widthGrow: 3,
                                formatter: function (cell) {

                                    const data = cell.getRow().getData();

                                    if (data.isHeader) {
                                        return `<strong>${data.accountName}</strong>`;
                                    }

                                    return data.accountName;
                                }
                            },
                            {
                                title: "Tipe",
                                field: "accountType"
                            },
                            {
                                title: "Saldo Normal",
                                field: "normalBalance",
                                width: 120
                            },
                            {
                                title: "Level",
                                field: "accountLevel"
                            },
                            {
                                title: "Header",
                                field: "isHeader",
                                formatter: "tickCross"
                            },
                            {
                                title: "Bisa Jurnal",
                                field: "isPostable",
                                formatter: "tickCross"
                            }
                        ]

                    });
                },

                install: async function () {
                    try {
                        const result = await api.post(`/api/v1/accounts/install`, "", "Menginstall Coa...");
                        if (result && result.success !== false) {
                            await Toast.fire({
                                icon: 'success',
                                title: 'Berhasil!',
                                text: result.message,
                                timer: 3000,
                                showConfirmButton: false
                            });
                            setTimeout(() => {
                                window.location.href = '/coa';
                            }, 500);
                            return result;
                        } else {
                            throw new Error(result?.message || 'Menginstall Coa');
                        }
                    } catch (err) {
                        console.log("Full Error Object = ", err);
                        const errors = err?.errors || err?.data?.errors;
                        if (errors) {
                            this.showErrors(errors, formEl);
                            Toast.fire({
                                icon: 'error',
                                title: 'Menginstall Coa'
                            });
                        } else {
                            Swal.fire({
                                icon: "error",
                                title: "Oops...",
                                text: err.message,
                            });
                        }
                        throw errors;
                    }
                }

            };
    accountTemplates.init();   // ✅ panggil init
    return accountTemplates;   // ✅ return supaya bisa dipakai/destroy
}