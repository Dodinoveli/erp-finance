class Toast {
  static fire(options = {}) {
    return Swal.fire({
      toast: true,
      position: "top-end",
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true,
      ...options,
    });
  }

  static success(message, title = "Berhasil!", timer = 3000) {
    return this.fire({
      icon: "success",
      title: title,
      text: message,
      timer: timer,
      color: "#065f46",
      background: "#d1fae5",
    });
  }

  static error(message, title = "Gagal!", timer = 3000) {
    return this.fire({
      icon: "error",
      title: title,
      text: message,
      timer: timer,
      background: "#fee2e2",
      color: "#991b1b",
    });
  }

  static warning(message, title = "Peringatan!", timer = 3000) {
    return this.fire({
      icon: "warning",
      title: title,
      text: message,
      timer: timer,
      background: "#fef3c7",
      color: "#92400e",
    });
  }

  static info(message, title = "Informasi", timer = 3000) {
    return this.fire({
      icon: "info",
      title: title,
      text: message,
      timer: timer,
      background: "#dbeafe",
      color: "#1e40af",
    });
  }

  static loading(message = "Memproses...") {
    return this.fire({
      title: message,
      timer: undefined,
      timerProgressBar: false,
      allowOutsideClick: false,
      allowEscapeKey: false,
      background: "#f8fafc",
      color: "#334155",
      didOpen: () => {
        Swal.showLoading();
      },
    });
  }

  static close() {
    Swal.close();
  }
}

window.Toast = Toast;
