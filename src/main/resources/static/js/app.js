/**
 * Global Page Initializer
 *
 * File ini merupakan pusat inisialisasi JavaScript aplikasi.
 *
 * Tanggung jawab:
 * - Mengimpor module JavaScript dari setiap halaman.
 * - Mendeteksi halaman berdasarkan elemen HTML tertentu.
 * - Menjalankan fungsi init sesuai halaman yang sedang aktif.
 * - Menjalankan kembali inisialisasi setelah HTMX melakukan
 *   partial navigation pada #page-content.
 * - Mengatur global loading indicator selama request HTMX.
 *
 * Arsitektur:
 * app.js
 *   └── pages/
 *       ├── client/
 *       ├── supplier/
 *       ├── bankaccount/
 *       ├── project/
 *       ├── chartofaccounts/
 *       └── coamapping/
 *
 * Catatan:
 * Logic masing-masing halaman tetap berada di module-nya sendiri.
 * File ini hanya bertugas sebagai orchestrator / pengatur
 * inisialisasi halaman.
 */

/**
 * 1. CLIENT
 */
import { initClientList } from "./pages/client/list.js"; 
import { initClientNew } from "./pages/client/new.js";
import { initClientEdit } from "./pages/client/edit.js";

/**
 * 2. Supplier
*/
import { initSupplirList } from "./pages/supplier/list.js";
import { initSupplirNew } from "./pages/supplier/new.js";
import { initSupplirEdit } from "./pages/supplier/edit.js";

/**
 *  3.  Bankaccount
 */
import { initBankAccountList } from "./pages/bankaccount/list.js";
import { initBankAccountNew } from "./pages/bankaccount/new.js";
import { initBankAccountEdit } from "./pages/bankaccount/edit.js";

/**
 * 4. Proyek  
 */
import { initProjectList } from "./pages/project/list.js";
import { initProjectNew } from "./pages/project/new.js";
import { initProjectEdit } from "./pages/project/edit.js";

/**
 * 5. Coa Templates 
 */
import { initChartOfAccounts } from "./pages/chartofaccounts/list.js"
import { initChartOfAccountsTemplates } from "./pages/chartofaccountstemplates/list.js"

/**
 * 6. coamapping
*/
import { initCoamapping } from "./pages/coamapping/new.js"
import { initCoaMappingList } from "./pages/coamapping/list-coa-mapping.js"

/**
 * 7. coamapping line
*/
import { InitCoaMappingLine } from "./pages/coamapping/list-coa-mappingline.js"

/**
 * GLOBAL HTMX LOADING
*/
document.body.addEventListener("htmx:beforeRequest", function() {
	const loading = document.getElementById("loading");

	if (loading) {
		loading.classList.add("show");
	}
});

document.body.addEventListener("htmx:afterRequest", function() {
	const loading = document.getElementById("loading");
	if (loading) {
		loading.classList.remove("show");
	}
});

function initPage() {
	//1. client
	if (document.querySelector("#clientTable")) {
		initClientList();
	}
	if (document.querySelector("#formClient")) {
		initClientNew();
	}
	if (document.querySelector("#formClientEdit")) {
		initClientEdit();
	}

	//2. suppliers
	if (document.querySelector("#supplierTable")) {
		initSupplirList();
	}
	if (document.querySelector("#formSupplier")) {
		initSupplirNew();
	}
	if (document.querySelector("#formSupplierEdit")) {
		initSupplirEdit();
	}

	//3. bankaccount
	if (document.querySelector("#bankTable")) {
		initBankAccountList();
	}
	if (document.querySelector("#formBankAccountNew")) {
		initBankAccountNew();
	}
	if (document.querySelector("#formBankAccountEdit")) {
		initBankAccountEdit();
	}

	//4. project
	if (document.querySelector("#projectTable")) {
		initProjectList();
	}
	if (document.querySelector("#formProjectNew")) {
		initProjectNew();
	}
	if (document.querySelector("#formProjectEdit")) {
		initProjectEdit();
	}

	//5. coa template
	if (document.querySelector("#coa-table")) {
		initChartOfAccounts();
	}
	if (document.querySelector("#coa-table-templates")) {
		initChartOfAccountsTemplates();
	}

	//6. coa mapping
	if (document.querySelector("#formCoaMappingNew")) {
		initCoamapping();
	}
	if (document.querySelector("#coamappingTable")) {
		initCoaMappingList();
	}

	//7. coa mapping line
	if (document.querySelector("#coamappingTableLine")) {
		InitCoaMappingLine();
	}

}

// ==============================
// INITIAL LOAD
// ==============================
document.addEventListener("DOMContentLoaded", function() {
	initPage();
});

// ==============================
// HTMX PARTIAL NAVIGATION
// ==============================
document.body.addEventListener("htmx:afterSwap", function(event) {
	if (event.detail.target?.id === "page-content") {
		initPage();
	}

}
);