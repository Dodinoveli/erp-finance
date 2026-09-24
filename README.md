# 🏗️ ERP Finance - Aplikasi Keuangan untuk Kontraktor

**ERP Finance** adalah aplikasi manajemen keuangan berbasis web yang dirancang khusus untuk membantu kontraktor mengelola keuangan proyek secara efisien dan akurat.

---

## 🎯 Masalah yang Dipecahkan

Kontraktor sering menghadapi tantangan:
- ❌ Anggaran proyek membengkak (overbudget)
- ❌ Arus kas tidak terkendali
- ❌ Laporan keuangan tidak akurat
- ❌ Kesulitan mengelola pajak konstruksi

**ERP Finance hadir sebagai solusi!**

---

## ✨ Fitur Unggulan

| Modul | Deskripsi |
|-------|-----------|
| 📊 **Dashboard** | Pantau kesehatan keuangan seluruh proyek secara real-time |
| 🏗️ **Manajemen Proyek** | Kelola RAB, termin, dan retensi proyek |
| 👥 **Manajemen Klien** | Data klien dan histori transaksi |
| 🏢 **Manajemen Supplier** | Kelola supplier dan pembelian material |
| 💳 **Bank Accounts** | Kelola rekening bank dan COA |
| 👤 **Manajemen User** | Autentikasi dengan JWT dan role-based access |
| 📈 **Laporan Keuangan** | Job costing, WIP, dan profitabilitas proyek |

---

## 🛠️ Teknologi

- **Backend:** Java 21, Spring Boot 3, Spring Security, JWT
- **Frontend:** Thymeleaf, HTML5, CSS3, JavaScript
- **Database:** PostgreSQL, Flyway Migration
- **Build Tool:** Maven
- **Version Control:** Git & GitHub

---

## 🚀 Cara Menjalankan

### Prasyarat
- JDK 21 atau lebih baru
- Maven 3.8+
- PostgreSQL 18

### Langkah-langkah

```bash
# 1. Clone repository
git clone https://github.com/Dodinoveli/erp-finance.git

# 2. Masuk ke folder proyek
cd erp-finance

# 3. Konfigurasi database di application.yml

# 4. Jalankan aplikasi
mvn spring-boot:run

# 5. Buka browser dan akses
http://localhost:8080

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
