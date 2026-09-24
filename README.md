# 🏗️ ERP Finance

### **Contractor Financial Management System**

> **Mengendalikan keuangan proyek. Menjaga laba tetap terkawal.**

ERP Finance adalah **sistem manajemen keuangan berbasis web yang dirancang khusus untuk perusahaan kontraktor**.

Aplikasi ini membantu mengelola keuangan perusahaan dan proyek secara terintegrasi — mulai dari **Chart of Accounts, rekening bank, penerimaan, pengeluaran, hingga monitoring keuangan proyek**.

---

## 🎯 Mengapa ERP Finance?

Dalam bisnis konstruksi, setiap proyek memiliki karakteristik dan risiko keuangan yang berbeda.

Pengeluaran material, pembayaran supplier, termin proyek, retensi, hingga arus kas harus dapat dipantau dengan baik agar perusahaan memiliki gambaran yang jelas mengenai kondisi keuangannya.

ERP Finance dibangun untuk membantu kontraktor:

- 📌 Mengontrol pengeluaran proyek
- 💰 Memantau arus kas
- 🏗️ Mengelola keuangan setiap proyek
- 📊 Menyusun transaksi secara terstruktur
- 🧾 Mendokumentasikan bukti transaksi
- 📈 Memantau kinerja dan profitabilitas proyek

### **Fokus Utama**

> **Setiap proyek harus dapat diketahui uangnya masuk dari mana, keluar untuk apa, dan berapa laba yang dihasilkan.**

---

## ✨ Core Features

| Module | Description |
|:---|:---|
| 📊 **Dashboard** | Monitoring kondisi keuangan perusahaan dan proyek |
| 🏗️ **Project Management** | Mengelola nilai proyek, termin, retensi, dan aktivitas keuangan proyek |
| 📚 **Chart of Accounts** | Struktur akun keuangan perusahaan |
| 💳 **Bank Accounts** | Pengelolaan rekening bank yang terhubung dengan COA |
| 💰 **Penerimaan** | Pencatatan transaksi penerimaan dana |
| 💸 **Pengeluaran** | Pencatatan transaksi pengeluaran |
| 👥 **Client Management** | Pengelolaan data klien dan histori transaksi |
| 🏢 **Supplier Management** | Pengelolaan supplier dan kebutuhan pembelian |
| 🧾 **Transaction Evidence** | Dokumentasi nota dan bukti pembayaran |
| 🔐 **Authentication & Authorization** | JWT dan role-based access control |
| 📈 **Financial Reporting** | Informasi keuangan untuk membantu monitoring bisnis |

---

## 🏗️ Designed for Contractors

ERP Finance tidak dibuat sebagai aplikasi akuntansi generik.

Sistem dirancang dengan mempertimbangkan kebutuhan operasional perusahaan kontraktor, terutama hubungan antara:

```text
PROJECT
   │
   ├── Contract Value
   │
   ├── Revenue
   │
   ├── Project Expenses
   │
   ├── Cash Flow
   │
   ├── Receivables
   │
   └── Profitability
```

Tujuannya adalah menghubungkan **aktivitas proyek dengan kondisi keuangan perusahaan**.

---

## 🧩 System Architecture

```text
                    ┌─────────────────┐
                    │     Browser     │
                    └────────┬────────┘
                             │
                             ▼
                 ┌──────────────────────┐
                 │    Spring Boot App   │
                 │                      │
                 │  Controller          │
                 │       ↓              │
                 │  Service             │
                 │       ↓              │
                 │  Repository / JDBC   │
                 └──────────┬───────────┘
                            │
                            ▼
                 ┌──────────────────────┐
                 │      PostgreSQL      │
                 └──────────────────────┘
```

---

## ⚙️ Technology Stack

### Backend

- ☕ Java 21
- 🍃 Spring Boot 3
- 🔐 Spring Security
- 🔑 JWT Authentication
- 🗄️ JDBC / NamedParameterJdbcTemplate
- 📦 Maven

### Frontend

- 🌐 Thymeleaf
- 🎨 Bootstrap 5
- ⚡ HTMX
- 📜 JavaScript
- 🧱 HTML5 / CSS3

### Database & Migration

- 🐘 PostgreSQL
- 🛫 Flyway

### Development & Deployment

- 🔧 Git
- 🐙 GitHub
- 🤖 Jenkins
- ☁️ Cloudflare Tunnel

---

## 🔐 Security

ERP Finance menggunakan pendekatan security berbasis:

- JWT Authentication
- Access Token & Refresh Token
- HTTP Cookie
- Role-Based Access Control
- Company-level data isolation
- Protected application endpoints

---

## 🚀 Getting Started

### Prerequisites

Pastikan environment berikut telah tersedia:

```text
Java 21+
Maven 3.8+
PostgreSQL
Git
```

### Clone Repository

```bash
git clone https://github.com/Dodinoveli/erp-finance.git
```

### Enter Project Directory

```bash
cd erp-finance
```

### Configure Database

Sesuaikan konfigurasi database pada:

```text
src/main/resources/application.yml
```

### Run Application

```bash
mvn spring-boot:run
```

<<<<<<< HEAD
# 5. Buka browser dan akses
http://localhost:8080 
```

---
## License
MIT License © 2026 Abdody

=======
Kemudian buka:

```text
http://localhost:8080
```

---

## 🔄 Development Workflow

```text
Developer
    │
    ▼
 GitHub
    │
    ▼
 Jenkins
    │
    ▼
  Build
    │
    ▼
  Test
    │
    ▼
 Package
    │
    ▼
 Deploy
    │
    ▼
Application Server
```

---

## 🗺️ Roadmap

### Foundation

- [x] Authentication & Authorization
- [x] Company Management
- [x] Client Management
- [x] Supplier Management
- [x] Chart of Accounts
- [x] Bank Accounts

### Financial Management

- [x] Penerimaan
- [x] Pengeluaran
- [ ] Accounts Receivable
- [ ] Accounts Payable
- [ ] Project Finance
- [ ] Job Costing
- [ ] Profitability Analysis
- [ ] Financial Reports

### Platform

- [ ] Advanced Dashboard
- [ ] Document & Receipt Storage
- [ ] Notification System
- [ ] Production Deployment
- [ ] Multi-company SaaS Platform

> 🚧 **ERP Finance is actively under development.**

---

## 📸 Screenshots

Screenshots of the application will be added as development progresses.

---

## 📌 Project Status

**Development Status:** 🚧 Active Development

ERP Finance is currently being developed as a specialized financial management platform for contractors.

Features, architecture, and implementation details may evolve throughout development.

---

## 📄 License

This project is licensed under the **MIT License**.

Copyright © 2026 **Abdody**

See the [LICENSE](LICENSE) file for the full license text.

---

<p align="center">

### 🏗️ ERP Finance

**Financial Management for Contractors**

*Built with Java • Spring Boot • PostgreSQL*

</p>
>>>>>>> a0c0de0 (readme)
