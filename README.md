# Sistem Operasi — Demonstrasi Multithreading (C & Java)

Program praktikum mata kuliah **Sistem Operasi** yang mendemonstrasikan konsep **multithreading** dan **sinkronisasi antar-thread**. Proyek ini berisi dua implementasi yang setara, yaitu **bahasa C** (POSIX Threads / `pthread`) dan **bahasa Java** (`Runnable` + `Thread`).

## Deskripsi Proyek

Program menjalankan **tiga thread secara bersamaan** dari satu proses. Pengguna memasukkan tiga nilai lewat terminal, lalu setiap thread mengerjakan satu tugas independen:

| Thread | Tugas | Input yang dipakai |
|--------|-------|--------------------|
| Thread 1 | Menghitung **faktorial** suatu bilangan | Angka untuk faktorial |
| Thread 2 | Mencetak **deret Fibonacci** | Jumlah suku Fibonacci |
| Thread 3 | **Membaca dan menampilkan isi berkas teks** | Nama berkas teks |

**Tujuan:** memperlihatkan cara membuat, menjalankan, dan menunggu (*join*) beberapa thread, serta cara mencegah **output yang saling bertabrakan** saat banyak thread mencetak ke layar yang sama.

**Masalah yang diselesaikan:** ketika beberapa thread mencetak ke terminal secara bersamaan, keluarannya dapat tercampur dan sulit dibaca. Program ini memakai **mutex** (di C) dan blok **`synchronized`** (di Java) agar tiap thread menulis hasilnya secara utuh dan bergantian. Ini adalah contoh sederhana dari masalah *critical section*.

## Daftar Fitur

- **Tiga thread paralel** dengan tugas berbeda: faktorial, Fibonacci, dan pembacaan berkas.
- **Dua implementasi** dengan logika yang sama: C (`pthread`) dan Java (`Runnable`).
- **Sinkronisasi output** memakai `pthread_mutex_t` (C) dan `synchronized (System.out)` (Java).
- **Berbagi data antar-thread** lewat struktur `DataThread` (struct di C, kelas di Java).
- **Menunggu semua thread selesai** dengan `pthread_join()` (C) dan `Thread.join()` (Java).
- **Penanganan galat berkas:** jika berkas tidak ditemukan, program menampilkan `Berkas tidak dapat dibuka.` dan tidak berhenti mendadak.
- **Input interaktif** di terminal, sehingga mudah dicoba dengan nilai berbeda.
- **Berkas contoh `catatan.txt`** berisi ringkasan singkat tentang sistem operasi, siap dipakai sebagai masukan untuk Thread 3.
- **`main.exe` siap pakai** untuk Windows, tanpa perlu kompilasi ulang.

## Struktur Folder

```
Sistem Operasi/
├── main.c                  # Implementasi C (pthread)
├── main.exe                # Hasil kompilasi main.c untuk Windows (64-bit)
├── Program.java            # Implementasi Java (3 Runnable + Program utama)
├── Program.class           # Hasil kompilasi Java (bytecode)
├── DataThread.class        # (hasil kompilasi) kelas data bersama
├── FaktorialRunnable.class # (hasil kompilasi) tugas Thread 1
├── FibonacciRunnable.class # (hasil kompilasi) tugas Thread 2
├── BacaFileRunnable.class  # (hasil kompilasi) tugas Thread 3
├── catatan.txt             # Contoh berkas teks untuk dibaca Thread 3
└── .vscode/settings.json   # Pengaturan Java untuk VS Code
```

## Petunjuk Instalasi

### Prasyarat

| Kebutuhan | Untuk apa | Keterangan |
|-----------|-----------|------------|
| **GCC** (dengan dukungan `pthread`) | Versi C | Linux/macOS: bawaan atau lewat paket. Windows: **MinGW-w64** / MSYS2 |
| **JDK 25** (atau lebih baru) | Versi Java, jika memakai `.class` bawaan | JDK yang lebih lama harus mengompilasi ulang dari `Program.java` |
| **Git** (opsional) | Mengunduh repositori | |

### 1. Mengunduh proyek

Jika proyek berada di repositori Git:

```bash
git clone <url-repositori>
cd "Sistem Operasi"
```

Jika berupa berkas `.zip`, ekstrak lalu masuk ke folder hasil ekstraksi:

```bash
unzip Sistem_Operasi.zip
cd "Sistem Operasi"
```

### 2. Memasang kompiler

**Ubuntu / Debian**
```bash
sudo apt update
sudo apt install build-essential default-jdk
```

**Windows**
- Pasang [MSYS2](https://www.msys2.org/), lalu di terminal MSYS2 UCRT64 jalankan:
  ```bash
  pacman -S mingw-w64-ucrt-x86_64-gcc
  ```
- Pasang JDK, misalnya [Eclipse Temurin](https://adoptium.net/) atau Oracle JDK.

**macOS**
```bash
xcode-select --install      # menyediakan clang/gcc
brew install openjdk        # untuk Java
```

### 3. Mengompilasi

**Versi C**
```bash
gcc main.c -o main -pthread
```

**Versi Java**
```bash
javac Program.java
```

> Berkas `main.exe` dan `*.class` sudah tersedia di folder, jadi kompilasi hanya diperlukan jika Anda mengubah kode atau lingkungan Anda tidak cocok (lihat bagian *Pemecahan Masalah*).

## Cara Penggunaan

Jalankan program dari **dalam folder proyek** agar `catatan.txt` dapat ditemukan lewat nama relatif.

### Menjalankan versi C

```bash
# Linux / macOS
./main

# Windows (PowerShell / CMD)
.\main.exe
```

### Menjalankan versi Java

```bash
java Program
```

### Contoh sesi

Input pengguna ditandai dengan `<-`:

```text
Masukkan angka untuk faktorial: 5            <-
Masukkan jumlah suku Fibonacci: 10           <-
Masukkan nama berkas teks: catatan.txt       <-

=== Thread 1: Faktorial ===
Faktorial dari 5 = 120

=== Thread 2: Fibonacci ===
Deret Fibonacci hingga suku ke-10:
0 1 1 2 3 5 8 13 21 34

=== Thread 3: Membaca Berkas ===
Isi berkas:
Sistem operasi adalah perangkat lunak yang berfungsi sebagai penghubung ...
...

Semua thread telah selesai.
```

**Catatan:**
- **Urutan blok keluaran bisa berbeda** pada setiap eksekusi. Ketiga thread berjalan bersamaan dan penjadwalannya ditentukan oleh sistem operasi. Yang dijamin oleh mutex/`synchronized` adalah setiap blok tercetak **utuh**, tanpa tercampur dengan blok lain.
- Nama berkas tidak boleh mengandung spasi, karena dibaca dengan `scanf("%99s")` (C) dan `Scanner.next()` (Java). Panjang maksimal di versi C adalah 99 karakter.
- Berkas di folder lain dapat diberikan lewat path, misalnya `../data/contoh.txt`.

## Petunjuk Konfigurasi

Program ini **tidak memerlukan variabel lingkungan** (*environment variables*) maupun berkas konfigurasi. Seluruh masukan diberikan saat program berjalan.

Satu-satunya pengaturan tambahan adalah untuk editor:

- **`.vscode/settings.json`** mengatur parameter JVM bagi *Language Server* Java di VS Code (`-Xmx4G`, `-Xms100m`, dan opsi GC). Ini hanya memengaruhi kinerja editor, bukan program. Jika komputer Anda memiliki RAM kecil, turunkan nilai `-Xmx4G`, misalnya menjadi `-Xmx1G`.

## Pemecahan Masalah

### `Berkas tidak dapat dibuka.`
- **Penyebab:** nama berkas salah, atau program dijalankan dari folder lain.
- **Solusi:** pastikan Anda menjalankan program dari folder proyek, atau masukkan path lengkap/relatif yang benar. Cek juga huruf besar-kecil nama berkas, terutama di Linux dan macOS.

### Berkas dengan spasi pada nama tidak terbaca
- **Penyebab:** input dibaca hanya sampai spasi pertama.
- **Solusi:** ganti nama berkas tanpa spasi, misalnya `catatan_saya.txt`.

### Hasil faktorial bernilai negatif atau aneh
- **Penyebab:** *integer overflow*. Faktorial dihitung dengan tipe 64-bit (`long long` / `long`), yang hanya cukup sampai **20!**. Mulai dari 21! hasilnya melimpah (contoh: `21` menghasilkan `-4249290049419214848`).
- **Solusi:** gunakan angka **0–20**. Jika perlu angka lebih besar, ubah kode agar memakai `BigInteger` (Java) atau pustaka bilangan besar seperti GMP (C).

### Deret Fibonacci menjadi negatif di suku tinggi
- **Penyebab:** *overflow* yang sama. Tipe 64-bit hanya memuat sekitar **92 suku pertama** dengan benar.
- **Solusi:** gunakan jumlah suku **≤ 92**.

### Program berhenti dengan error saat input bukan angka
- **Penyebab:** di Java, `Scanner.nextInt()` melempar `InputMismatchException`. Di C, `scanf("%d")` gagal dan nilainya tidak terisi.
- **Solusi:** masukkan bilangan bulat untuk dua pertanyaan pertama.

### `UnsupportedClassVersionError: ... compiled by a more recent version of the Java Runtime`
- **Penyebab:** berkas `.class` bawaan dikompilasi dengan **JDK 25** (class file versi 69), sedangkan Java di komputer Anda lebih lama.
- **Solusi:** pasang JDK 25 atau lebih baru, **atau** kompilasi ulang dengan JDK Anda:
  ```bash
  javac Program.java
  java Program
  ```

### `javac: command not found` / `'javac' is not recognized`
- **Penyebab:** yang terpasang hanya JRE (hanya bisa menjalankan), atau JDK belum masuk ke `PATH`.
- **Solusi:** pasang **JDK** (bukan hanya JRE) dan pastikan folder `bin` JDK ada di variabel `PATH`. Cek dengan `javac -version`.

### `undefined reference to 'pthread_create'` saat kompilasi C
- **Penyebab:** pustaka thread belum ditautkan.
- **Solusi:** tambahkan opsi `-pthread`:
  ```bash
  gcc main.c -o main -pthread
  ```

### `main.exe` tidak mau berjalan / muncul pesan DLL hilang (mis. `libwinpthread-1.dll`)
- **Penyebab:** `main.exe` dikompilasi dengan MinGW-w64 dan membutuhkan `libwinpthread-1.dll` serta `libgcc_s_dw2-1.dll`. Berkas ini juga hanya berjalan di **Windows 64-bit**.
- **Solusi:** letakkan kedua DLL tersebut di folder yang sama dengan `main.exe` (tersedia di folder `bin` MinGW/MSYS2), **atau** kompilasi ulang dengan tautan statis:
  ```bash
  gcc main.c -o main.exe -pthread -static
  ```

### `./main.exe` tidak bisa dijalankan di Linux/macOS
- **Penyebab:** `main.exe` adalah program Windows (format PE).
- **Solusi:** kompilasi ulang dari `main.c` dengan `gcc main.c -o main -pthread`, lalu jalankan `./main`.

### `Permission denied` saat menjalankan `./main`
- **Solusi:** beri izin eksekusi dengan `chmod +x main`.

### Urutan keluaran berbeda setiap dijalankan
- Ini **perilaku normal** program multithreading (penjadwalan thread tidak deterministik), bukan galat. Jika ingin urutan tetap, jalankan thread satu per satu dengan memanggil `join()` tepat setelah `start()`/`pthread_create()`.

## Catatan Pengembangan

- Repositori Git sudah diinisialisasi (cabang `master`) namun **belum memiliki commit**. Jalankan `git add . && git commit -m "Commit awal"` untuk memulai pelacakan.
- Sebaiknya tambahkan `.gitignore` agar berkas hasil kompilasi (`*.class`, `*.exe`) tidak ikut terlacak.
- Berkas `catatan.txt` memakai akhir baris gaya Windows (`CRLF`); ini tidak memengaruhi program.
