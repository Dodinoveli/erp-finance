// ======================================================
// 🍞 TOAST LOADING & NOTIFICATION HELPER
// ======================================================
const Toast = Swal.mixin({
  toast: true,
  position: 'top-end',
  showConfirmButton: false,
  timer: 3000,
  timerProgressBar: true,
  didOpen: (toast) => {
    toast.addEventListener('mouseenter', Swal.stopTimer);
    toast.addEventListener('mouseleave', Swal.resumeTimer);
  }
});

let toastLoadingInstance = null;

function showToastLoading(text = "Memproses...") {
  // Close existing toast loading if any
  if (toastLoadingInstance) {
    Swal.close();
    toastLoadingInstance = null;
  }


  Toast.fire({
    title: text,
    showConfirmButton: false,
    timer: null,
    willOpen: () => {
      Swal.showLoading();
    }
  }).then(() => {
    toastLoadingInstance = null;
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
// 🔄 GLOBAL LOADING COUNTER (ANTI FLICKER)
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
let isRefreshing = false;
let refreshPromise = null;
const API_BASE_URL =
  `${window.location.protocol}//${window.location.hostname}` +
  `${window.location.port ? ":" + window.location.port : ""}`;

// ======================================================
// 🚨 HANDLE RESPONSE (HARDENED) Jangan lagi logout di 401
// ======================================================
async function handleResponse(response) {
  // 🔐 auto logout jika unauthorized
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

  // ❌ HTTP error
  if (!response.ok) {
    const message =
      data?.message ||
      data?.error ||
      response.statusText ||
      "Terjadi kesalahan pada server";

    const err = new Error(message);
    err.data = data;
    err.status = response.status;
    throw err;
  }

  // 📭 No Content
  if (response.status === 204) return null;

  return data;
}

// ======================================================
// 🌐 CORE FETCH (WRAPPER UTAMA)
// ======================================================
async function apiFetch(url, options = {}) {
  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), 30000);

  const headers = {
    ...(options.headers || {}),
  };

  // ⚠️ jangan set JSON kalau FormData
  if (!(options.body instanceof FormData)) {
    headers["Content-Type"] = "application/json";
  }

  const useLoading = options.loadingText !== false;

  if (useLoading) {
    startLoading(options.loadingText);
  }

  try {
    let response = await fetch(API_BASE_URL + url, {
      ...options,
      headers,
      credentials: "include", // 🔥 JWT cookie
      signal: controller.signal,
    });

    // ======================================================
    // 🔥 AUTO REFRESH HANDLE 401
    // ======================================================
    if (response.status === 401 && !url.includes("api/auth/refresh")) {
      console.warn("401 detected → trying refresh...");
      
      // 🔥 Toast info
      Toast.fire({
        icon: 'info',
        title: 'Session expired, refreshing...',
        timer: 2000
      });

      try {
        if (!isRefreshing) {
          isRefreshing = true;
          await doRefresh();
          isRefreshing = false;
        } else {
          await refreshPromise;
        }

        // 🔁 retry request setelah refresh
        response = await fetch(API_BASE_URL + url, {
          ...options,
          headers,
          credentials: "include",
          signal: controller.signal,
        });
      } catch (err) {
        console.error("Refresh gagal → redirect login");
        
        // 🔥 Toast error
        Toast.fire({
          icon: 'error',
          title: 'Session expired',
          text: 'Please login again'
        });
        
        setTimeout(() => {
          window.location.href = "/login";
        }, 1500);
        
        throw err;
      }
    }
    // ======================================================
    // 🔥 NORMAL HANDLE RESPONSE
    // ======================================================
    return await handleResponse(response);
  } catch (err) {
    if (err.name === "AbortError") {
      throw new Error("Request timeout (30s)");
    }

    // 🔥 jangan bungkus ulang error dari server
    if (err instanceof Error) {
      throw err;
    }

    throw new Error("Network error");
  } finally {
    clearTimeout(timeoutId);

    if (useLoading) {
      stopLoading();
    }
  }
}

async function apiFetchGet(url, options = {}) {
  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), 30000);

  const headers = {
    ...(options.headers || {}),
  };

  // ⚠️ jangan set JSON kalau FormData
  if (!(options.body instanceof FormData)) {
    headers["Content-Type"] = "application/json";
  }

  try {
    const response = await fetch(API_BASE_URL + url, {
      ...options,
      headers,
      credentials: "include", // 🔥 JWT cookie
      signal: controller.signal,
    });

    return await handleResponse(response);
  } catch (err) {
    if (err.name === "AbortError") {
      throw new Error("Request timeout (30s)");
    }

    // 🔥 jangan bungkus ulang error dari server
    if (err instanceof Error) {
      throw err;
    }

    throw new Error("Network error");
  } finally {
    clearTimeout(timeoutId);
  }
}

// ======================================================
// 🚀 PUBLIC HTTP API
// ======================================================
window.api = {
  get: (url) =>
    apiFetchGet(url, {
      method: "GET",
    }),

  post: (url, data, text) =>
    apiFetch(url, {
      method: "POST",
      body: data instanceof FormData ? data : JSON.stringify(data),
      loadingText: text,
    }),

  put: (url, data, text) =>
    apiFetch(url, {
      method: "PUT",
      body: data instanceof FormData ? data : JSON.stringify(data),
      loadingText: text,
    }),

  delete: (url, text) =>
    apiFetch(url, {
      method: "DELETE",
      loadingText: text,
    }),
};

async function doRefresh() {
  if (!refreshPromise) {
    refreshPromise = fetch(API_BASE_URL + "/api/auth/refresh", {
      method: "POST",
      credentials: "include",
    })
      .then((res) => {
        if (!res.ok) throw new Error("Refresh gagal");
        return res.json().catch(() => ({}));
      })
      .finally(() => {
        refreshPromise = null;
      });
  }

  return refreshPromise;
}