// ======================================================
// 🍞 TOAST LOADING & NOTIFICATION HELPER
// ======================================================

const Toast = Swal.mixin({
  toast: true,
  position: "top-end",
  showConfirmButton: false,
  timer: 3000,
  timerProgressBar: true,

  didOpen: (toast) => {
    toast.addEventListener("mouseenter", Swal.stopTimer);
    toast.addEventListener("mouseleave", Swal.resumeTimer);
  },
});

let toastLoadingInstance = null;

function showToastLoading(text = "Memproses...") {
  if (toastLoadingInstance) {
    Swal.close();
    toastLoadingInstance = null;
  }

  Toast.fire({
    title: text,
    showConfirmButton: false,
    timer: null,
    didOpen: () => {
      Swal.showLoading();
    },
  });

  toastLoadingInstance = true;
}

function hideToastLoading() {
  if (toastLoadingInstance) {
    Swal.close();
    toastLoadingInstance = null;
  }
}

// ======================================================
// 🔄 GLOBAL LOADING COUNTER
// ======================================================

let loadingCounter = 0;

function startLoading(text = "Memproses...") {
  loadingCounter++;

  if (loadingCounter === 1) {
    showToastLoading(text || "Memproses...");
  }
}

function stopLoading() {
  loadingCounter = Math.max(0, loadingCounter - 1);

  if (loadingCounter === 0) {
    hideToastLoading();
  }
}

// ======================================================
// 🌐 BASE URL DINAMIS
// ======================================================

const API_BASE_URL =
  `${window.location.protocol}//${window.location.hostname}` +
  `${window.location.port ? ":" + window.location.port : ""}`;

// ======================================================
// 🔐 REFRESH STATE
// ======================================================

let refreshPromise = null;

// ======================================================
// 🚨 HANDLE RESPONSE
// ======================================================

async function handleResponse(response) {
  // 403 = unauthorized / forbidden
  if (response.status === 403) {
    window.location.href = "/login";
    throw new Error("Session expired");
  }

  const contentType = response.headers.get("content-type") || "";

  let data = null;

  try {
    if (contentType.includes("application/json")) {
      data = await response.json();
    } else {
      const text = await response.text();

      data = text ? { message: text } : null;
    }
  } catch {
    data = null;
  }

  // ==================================================
  // ❌ HTTP ERROR
  // ==================================================

  if (!response.ok) {
    const message =
      data?.message ||
      data?.error ||
      response.statusText ||
      "Terjadi kesalahan pada server";

    const error = new Error(message);

    error.data = data;
    error.status = response.status;

    throw error;
  }

  // ==================================================
  // 📭 NO CONTENT
  // ==================================================

  if (response.status === 204) {
    return null;
  }

  return data;
}

// ======================================================
// 🔄 REFRESH TOKEN
// ======================================================

async function doRefresh() {
  if (!refreshPromise) {
    refreshPromise = fetch(API_BASE_URL + "/api/auth/refresh", {
      method: "POST",
      credentials: "include",
    })
      .then(async (response) => {
        if (!response.ok) {
          throw new Error("Refresh gagal");
        }

        try {
          return await response.json();
        } catch {
          return {};
        }
      })
      .finally(() => {
        refreshPromise = null;
      });
  }

  return refreshPromise;
}

// ======================================================
// 🌐 CORE FETCH
// ======================================================

async function apiFetch(url, options = {}) {
  const controller = new AbortController();

  const timeoutId = setTimeout(() => controller.abort(), 30000);

  // ==================================================
  // HEADERS
  // ==================================================

  const headers = {
    ...(options.headers || {}),
  };

  // JSON hanya jika ada body dan bukan FormData
  if (options.body && !(options.body instanceof FormData)) {
    headers["Content-Type"] = headers["Content-Type"] || "application/json";
  }

  // ==================================================
  // LOADING
  // ==================================================

  const useLoading = options.loadingText !== false;

  if (useLoading) {
    startLoading(options.loadingText || "Memproses...");
  }

  try {
    const requestOptions = {
      ...options,
      headers,
      credentials: "include",
      signal: controller.signal,
    };

    delete requestOptions.loadingText;

    // ==================================================
    // 🔍 DEBUG
    // ==================================================

    console.log(`${requestOptions.method || "GET"} ${API_BASE_URL}${url}`);

    // ==================================================
    // 🚀 REQUEST
    // ==================================================

    let response = await fetch(API_BASE_URL + url, requestOptions);

    // ==================================================
    // 🔥 401 → REFRESH → RETRY
    // ==================================================

    if (response.status === 401 && !url.includes("/api/auth/refresh")) {
      console.warn("401 detected → trying refresh...");

      try {
        await doRefresh();

        // retry request
        response = await fetch(API_BASE_URL + url, requestOptions);
      } catch (error) {
        console.error("Refresh gagal → redirect login");

        Toast.fire({
          icon: "error",
          title: "Session expired",
          text: "Please login again",
        });

        setTimeout(() => {
          window.location.href = "/login";
        }, 1500);

        throw error;
      }
    }

    // ==================================================
    // 📦 RESPONSE
    // ==================================================

    return await handleResponse(response);
  } catch (error) {
    if (error.name === "AbortError") {
      throw new Error("Request timeout (30s)");
    }

    if (error instanceof Error) {
      throw error;
    }

    throw new Error("Network error");
  } finally {
    clearTimeout(timeoutId);

    if (useLoading) {
      stopLoading();
    }
  }
}

// ======================================================
// 🔎 BUILD QUERY STRING
// ======================================================

function buildQueryString(params = {}) {
  const query = new URLSearchParams();

  Object.entries(params).forEach(([key, value]) => {
    if (value !== null && value !== undefined) {
      query.append(key, String(value));
    }
  });

  return query.toString();
}

// ======================================================
// 🚀 PUBLIC HTTP API
// ======================================================

window.api = {
  // ==================================================
  // GET
  // ==================================================

  get: function (url, params = {}) {
    const queryString = buildQueryString(params);

    if (queryString) {
      url += (url.includes("?") ? "&" : "?") + queryString;
    }

    console.log("========== API GET ==========");

    console.log("URL:", API_BASE_URL + url);

    console.log("PARAMS:", params);

    console.log("=============================");

    return apiFetch(url, {
      method: "GET",
      loadingText: false,
    });
  },

  // ==================================================
  // POST
  // ==================================================

  post: function (url, data, text) {
    return apiFetch(url, {
      method: "POST",

      body: data instanceof FormData ? data : JSON.stringify(data),

      loadingText: text,
    });
  },

  // ==================================================
  // PUT
  // ==================================================

  put: function (url, data, text) {
    return apiFetch(url, {
      method: "PUT",

      body: data instanceof FormData ? data : JSON.stringify(data),

      loadingText: text,
    });
  },

  // ==================================================
  // DELETE
  // ==================================================

  delete: function (url, text) {
    return apiFetch(url, {
      method: "DELETE",
      loadingText: text,
    });
  },
};
