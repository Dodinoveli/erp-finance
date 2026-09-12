
// 1. js client
import { initClientList } from "./pages/client/list.js";
import { initClientNew } from "./pages/client/new.js";
import { initClientEdit } from "./pages/client/edit.js";

// 2. js bankaccount
import { initBankAccountList } from "./pages/bankaccount/list.js";
import { initBankAccountNew } from "./pages/bankaccount/new.js";
// import { initBankAccountEdit } from "./pages/bankaccount/edit.js";
// import { initBankAccountDetail } from "./pages/bankaccount/detail.js";

document.body.addEventListener("htmx:afterSwap", function (event) {

    if (event.detail.target?.id !== "page-content") {
        return;
    }

    if (document.querySelector("#clientTable")) {
        initClientList();
    }

    if (document.querySelector("#formClient")) {
        initClientNew();
    }

    if (document.querySelector("#formClientEdit")) {
        initClientEdit();
    }

    if (document.querySelector("#bankTable")) {
        initBankAccountList();
    }

    if (document.querySelector("#formBankAccount")) {
        initBankAccountNew();
    }

    // if (document.querySelector("#clientTable")) {
    //   initClient();
    // }

}
);