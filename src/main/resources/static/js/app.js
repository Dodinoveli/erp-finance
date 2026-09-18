
// 1. js client
import { initClientList } from "./pages/client/list.js";
import { initClientNew } from "./pages/client/new.js";
import { initClientEdit } from "./pages/client/edit.js";

// 2 supplier
import { initSupplirList } from "./pages/supplier/list.js";
import { initSupplirNew } from "./pages/supplier/new.js";
import { initSupplirEdit } from "./pages/supplier/edit.js";


// 2. js bankaccount
import { initBankAccountList } from "./pages/bankaccount/list.js";
import { initBankAccountNew } from "./pages/bankaccount/new.js";
// import { initBankAccountEdit } from "./pages/bankaccount/edit.js";
// import { initBankAccountDetail } from "./pages/bankaccount/detail.js";

// 3. proyek 
import { initProjectList } from "./pages/project/list.js";
import { initProjectNew } from "./pages/project/new.js";
import { initProjectEdit } from "./pages/project/edit.js";

import { initChartOfAccounts } from "./pages/chartofaccounts/list.js"
import { initChartOfAccountsTemplates } from "./pages/chartofaccountstemplates/list.js"
// ==============================
// GLOBAL HTMX LOADING
// ==============================

document.body.addEventListener("htmx:beforeRequest", function () {
    const loading = document.getElementById("loading");

    if (loading) {
        loading.classList.add("show");
    }
});

document.body.addEventListener("htmx:afterRequest", function () {
    const loading = document.getElementById("loading");
    if (loading) {
        loading.classList.remove("show");
    }
});

function initPage() {
    // client
    if (document.querySelector("#clientTable")) {
        initClientList();
    }
    if (document.querySelector("#formClient")) {
        initClientNew();
    }

    if (document.querySelector("#formClientEdit")) {
        initClientEdit();
    }

    // suppliers
    if (document.querySelector("#supplierTable")) {
        initSupplirList();
    }

    if (document.querySelector("#formSupplier")) {
        initSupplirNew();
    }

    if (document.querySelector("#formSupplierEdit")) {
        initSupplirEdit();
    }


    if (document.querySelector("#bankTable")) {
        initBankAccountList();
    }

    if (document.querySelector("#formBankAccount")) {
        initBankAccountNew();
    }

    // project New
    if (document.querySelector("#projectTable")) {
        initProjectList();
    }

    if (document.querySelector("#formProjectNew")) {
        initProjectNew();
    }

    if (document.querySelector("#formProjectEdit")) {
        initProjectEdit();
    }

    if (document.querySelector("#coa-table")) {
        initChartOfAccounts();
    }

    // accountTemplates
    if (document.querySelector("#coa-table-templates")) {
        initChartOfAccountsTemplates();
    }

}

// ==============================
// INITIAL LOAD
// ==============================
document.addEventListener("DOMContentLoaded", function () {
    initPage();
});

// ==============================
// HTMX PARTIAL NAVIGATION
// ==============================
document.body.addEventListener("htmx:afterSwap", function (event) {
    if (event.detail.target?.id === "page-content") {
        initPage();
    }

}
);