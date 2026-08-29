// ==========================================
// SERVICE WORKER SUPER SIMPLE UNTUK PWA
// ==========================================

const CACHE_NAME = 'kontraktor-app-v1';

// File-file yang mau disimpan biar bisa diakses offline
const urlsToCache = [
  '/',
  '/manifest.json'
  // Tambahkan file CSS/JS penting kalau ada, contoh:
  // '/css/style.css',
  // '/js/app.js'
];

// ==========================================
// 1. SAAT INSTALL (Caching file)
// ==========================================
self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(cache => {
        console.log('✅ Cache berhasil dibuka');
        return cache.addAll(urlsToCache);
      })
      .catch(err => {
        console.log('❌ Gagal cache:', err);
      })
  );
});

// ==========================================
// 2. SAAT USER BUKA HALAMAN (Fetch)
// ==========================================
self.addEventListener('fetch', event => {
  event.respondWith(
    caches.match(event.request)
      .then(response => {
        // Kalau ada di cache, kasih dari cache
        if (response) {
          return response;
        }
        // Kalau ga ada, ambil dari internet
        return fetch(event.request);
      })
  );
});

// ==========================================
// 3. SAAT SERVICE WORKER AKTIF (Hapus cache lama)
// ==========================================
self.addEventListener('activate', event => {
  const cacheWhitelist = [CACHE_NAME];
  event.waitUntil(
    caches.keys().then(cacheNames => {
      return Promise.all(
        cacheNames.map(cacheName => {
          if (cacheWhitelist.indexOf(cacheName) === -1) {
            console.log('🗑️ Hapus cache lama:', cacheName);
            return caches.delete(cacheName);
          }
        })
      );
    })
  );
});