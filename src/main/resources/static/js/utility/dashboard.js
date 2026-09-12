var sideChoices = document.querySelectorAll('.side-color');
        function applySidebarColor(choice) {
            document.documentElement.style.setProperty('--side', choice.dataset.side);
            document.documentElement.style.setProperty('--side2', choice.dataset.side2);
            document.documentElement.style.setProperty('--side-border', choice.dataset.border);
            document.documentElement.style.setProperty('--side-line', choice.dataset.line);
            sideChoices.forEach(x => x.classList.remove('active'));
            choice.classList.add('active');
            localStorage.setItem('kl-sidebar-theme', JSON.stringify({
                side: choice.dataset.side,
                side2: choice.dataset.side2,
                border: choice.dataset.border,
                line: choice.dataset.line
            }));
        }

        sideChoices.forEach(choice => {
            choice.addEventListener('click', () => applySidebarColor(choice));
        });

        const savedSide = localStorage.getItem('kl-sidebar-theme');
        if (savedSide) {
            try {
                const s = JSON.parse(savedSide);
                document.documentElement.style.setProperty('--side', s.side);
                document.documentElement.style.setProperty('--side2', s.side2);
                document.documentElement.style.setProperty('--side-border', s.border);
                document.documentElement.style.setProperty('--side-line', s.line);
                sideChoices.forEach(x => {
                    if (x.dataset.side === s.side) x.classList.add('active');
                });
            } catch (e) { }
        } else {
            const defaultSide = document.querySelector('.side-default');
            if (defaultSide) defaultSide.classList.add('active');
        }


        const profileWrap = document.getElementById('profileWrap');
        const profileBtn = document.getElementById('profileBtn');
        const logoutBtn = document.getElementById('logoutBtn');

        profileBtn.addEventListener('click', e => {
            e.stopPropagation();
            const isOpen = profileWrap.classList.toggle('open');
            profileBtn.setAttribute('aria-expanded', isOpen ? 'true' : 'false');
        });

        logoutBtn.addEventListener('click', () => {
            // Demo: ganti alert ini dengan endpoint logout Spring Security Anda.
            handleLogout()
        });


        document.addEventListener('click', e => {
            if (profileWrap && !profileWrap.contains(e.target)) {
                profileWrap.classList.remove('open');
                profileBtn.setAttribute('aria-expanded', 'false');
            }
        });


        const themeBtn = document.getElementById('themeBtn');
        const themePanel = document.getElementById('themePanel');
        const bgChoices = document.querySelectorAll('.bg-choice');

        themeBtn.addEventListener('click', e => {
            e.stopPropagation();
            themePanel.classList.toggle('show');
        });

        bgChoices.forEach(choice => {
            choice.addEventListener('click', () => {
                const bg = choice.dataset.bg;
                document.documentElement.style.setProperty('--bg', bg);
                localStorage.setItem('kl-dashboard-bg', bg);
                bgChoices.forEach(x => x.classList.remove('active'));
                choice.classList.add('active');
            });
        });

        const savedBg = localStorage.getItem('kl-dashboard-bg');
        if (savedBg) {
            document.documentElement.style.setProperty('--bg', savedBg);
            bgChoices.forEach(x => {
                if (x.dataset.bg === savedBg) x.classList.add('active');
            });
        } else {
            document.querySelector('.bg-default').classList.add('active');
        }

        document.addEventListener('click', e => {
            if (!themePanel.contains(e.target) && e.target !== themeBtn) {
                themePanel.classList.remove('show');
            }
        });

        document.querySelectorAll('.tree>.link').forEach(b => b.onclick = () => b.parentElement.classList.toggle('open'));
        const side = document.getElementById('sidebar'), open = document.getElementById('open');
        open.onclick = () => side.classList.toggle('show');
        document.addEventListener('click', e => { if (innerWidth <= 760 && side.classList.contains('show') && !side.contains(e.target) && e.target !== open) side.classList.remove('show') });


        function handleLogout() {
            Swal.fire({
                title: "Logout?",
                text: "Anda akan keluar dari sesi ini.",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Ya, Logout!",
                cancelButtonText: "Batal",
                reverseButtons: true, // Opsional: Supaya tombol Batal di kiri
            }).then((result) => {
                if (result.isConfirmed) {
                    // Tampilkan loading saat proses request ke server
                    Swal.fire({
                        title: "Sedang memproses...",
                        allowOutsideClick: false,
                        didOpen: () => {
                            Swal.showLoading();
                        },
                    });

                    fetch("/api/auth/logout", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                            // Jika CSRF aktif, jangan lupa tambahkan header X-XSRF-TOKEN di sini
                        },
                    })
                        .then((response) => {
                            if (response.ok) {
                                Swal.fire({
                                    icon: "success",
                                    title: "Berhasil!",
                                    text: "Anda telah logout.",
                                    showConfirmButton: false,
                                    timer: 1500,
                                }).then(() => {
                                    window.location.href = "/login";
                                });
                            } else {
                                Swal.fire(
                                    "Gagal!",
                                    "Terjadi kesalahan saat logout.",
                                    "error",
                                );
                            }
                        })
                        .catch((error) => {
                            console.error("Error:", error);
                            Swal.fire(
                                "Error!",
                                "Terjadi kesalahan koneksi ke server.",
                                "error",
                            );
                        });
                }
            });
        };