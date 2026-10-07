#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

/* Mutex untuk mencegah output dari thread saling bertabrakan */
pthread_mutex_t mutex;

/* Data yang digunakan oleh thread */
typedef struct {
    int angka_faktorial;
    int jumlah_fibonacci;
    char nama_file[100];
} DataThread;

/* =========================
   THREAD 1: FAKTORIAL
   ========================= */
void *thread_faktorial(void *arg) {
    DataThread *data = (DataThread *)arg;

    long long hasil = 1;

    for (int i = 1; i <= data->angka_faktorial; i++) {
        hasil *= i;
    }

    pthread_mutex_lock(&mutex);

    printf("\n=== Thread 1: Faktorial ===\n");
    printf("Faktorial dari %d = %lld\n",
           data->angka_faktorial, hasil);

    pthread_mutex_unlock(&mutex);

    return NULL;
}

/* =========================
   THREAD 2: FIBONACCI
   ========================= */
void *thread_fibonacci(void *arg) {
    DataThread *data = (DataThread *)arg;

    long long a = 0;
    long long b = 1;

    pthread_mutex_lock(&mutex);

    printf("\n=== Thread 2: Fibonacci ===\n");
    printf("Deret Fibonacci hingga suku ke-%d:\n",
           data->jumlah_fibonacci);

    for (int i = 0; i < data->jumlah_fibonacci; i++) {
        printf("%lld ", a);

        long long berikutnya = a + b;
        a = b;
        b = berikutnya;
    }

    printf("\n");

    pthread_mutex_unlock(&mutex);

    return NULL;
}

/* =========================
   THREAD 3: MEMBACA FILE
   ========================= */
void *thread_baca_file(void *arg) {
    DataThread *data = (DataThread *)arg;

    FILE *file = fopen(data->nama_file, "r");

    pthread_mutex_lock(&mutex);

    printf("\n=== Thread 3: Membaca Berkas ===\n");

    if (file == NULL) {
        printf("Berkas tidak dapat dibuka.\n");
        pthread_mutex_unlock(&mutex);
        return NULL;
    }

    char karakter;

    printf("Isi berkas:\n");

    while ((karakter = fgetc(file)) != EOF) {
        putchar(karakter);
    }

    fclose(file);

    printf("\n");

    pthread_mutex_unlock(&mutex);

    return NULL;
}

/* =========================
   MAIN
   ========================= */
int main() {
    pthread_t thread1, thread2, thread3;
    DataThread data;

    /* Input */
    printf("Masukkan angka untuk faktorial: ");
    scanf("%d", &data.angka_faktorial);

    printf("Masukkan jumlah suku Fibonacci: ");
    scanf("%d", &data.jumlah_fibonacci);

    printf("Masukkan nama berkas teks: ");
    scanf("%99s", data.nama_file);

    /* Inisialisasi mutex */
    pthread_mutex_init(&mutex, NULL);

    /* Membuat 3 thread */
    pthread_create(&thread1, NULL, thread_faktorial, &data);
    pthread_create(&thread2, NULL, thread_fibonacci, &data);
    pthread_create(&thread3, NULL, thread_baca_file, &data);

    /* Menunggu semua thread selesai */
    pthread_join(thread1, NULL);
    pthread_join(thread2, NULL);
    pthread_join(thread3, NULL);

    /* Menghapus mutex */
    pthread_mutex_destroy(&mutex);

    printf("\nSemua thread telah selesai.\n");

    return 0;
}