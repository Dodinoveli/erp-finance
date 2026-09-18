export function initChartOfAccountsTemplates() {

    const intCoaTemplates = {
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
            this.table = new Tabulator("#coa-table-templates", {

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
            Toast.loading("Menginstal Data  Coa...");
            const url = "/api/v1/accounts/install";
            try {
                const response = await fetch(url, {
                    method: "POST"
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
                return result;
            } catch (err) {
                console.log("Full Error Object = ", err);
                const errors = err?.errors || err?.data?.errors;
                if (errors) {
                    // this.showErrors(errors, formEl);
                    Toast.error(err.message, "Gagal", 3000)
                } else {
                    Toast.error(err.message, "Opps Error", 3000)
                }
                throw err;
            }
        }

    };
    intCoaTemplates.init();   // ✅ panggil init
    return intCoaTemplates;   // ✅ return supaya bisa dipakai/destroy
}