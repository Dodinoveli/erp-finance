export function initChartOfAccounts() {
    const initCo = {

        table: null,

        init() {
            this.initTable();
        },

        initTable() {

            this.table = new Tabulator("#coa-table", {

                ajaxURL: "/api/v1/accounts/tree",
                ajaxResponse: function (url, params, response) {

                    return response.data;

                },
                height: "550px",
                layout: "fitColumns",
                responsiveLayout: "collapse",
                // layout: "fitColumns",
                // responsiveLayout: "collapse",
                // responsiveLayout: false,
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
                        width: 130
                    },
                    {
                        title: "Level",
                        field: "accountLevel"
                    },
                    {
                        title: "Header",
                        field: "isHeader",
                        formatter: "tickCross",
                        width: 120
                    },
                    {
                        title: "Bisa Jurnal",
                        field: "isPostable",
                        formatter: "tickCross",
                        width: 130
                    },
                    {
                        title: "Aksi",
                        field: "aksi",
                        hozAlign: "center",
                        headerHozAlign: "center",
                        headerSort: false,
                        width: 100,
                        minWidth: 100,
                        maxWidth: 100,
                        resizable: false,

                        formatter: function (cell) {
                            const data = cell.getRow().getData();

                            let id;
                            if (data.accountLevel !== 3) {
                                return "";
                            } else {
                                id = data.accountId;
                                return `
                                <div class="d-flex justify-content-center align-items-center h-100">
                                    <a href="/coa/detail/${id}"  
                                    onclick="event.preventDefault(); library.navigate('/coa/detail/${id}')"
                                    class="detail-actions-btn"
                                    title="Lihat Detail"><i class="bi bi-eye"></i></a>
                                </div>
                                `;
                            }
                        }
                    }
                ]

            });
        }

    };
    initCo.init();
    return initCo;
}