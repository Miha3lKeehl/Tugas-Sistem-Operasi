import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/* =========================
   DATA THREAD
   ========================= */
class DataThread {
    int angkaFaktorial;
    int jumlahFibonacci;
    String namaFile;

    DataThread(int angkaFaktorial, int jumlahFibonacci, String namaFile) {
        this.angkaFaktorial = angkaFaktorial;
        this.jumlahFibonacci = jumlahFibonacci;
        this.namaFile = namaFile;
    }
}

/* =========================
   THREAD 1: FAKTORIAL
   ========================= */
class FaktorialRunnable implements Runnable {

    private DataThread data;

    FaktorialRunnable(DataThread data) {
        this.data = data;
    }

    @Override
    public void run() {
        long hasil = 1;

        for (int i = 1; i <= data.angkaFaktorial; i++) {
            hasil *= i;
        }

        synchronized (System.out) {
            System.out.println("\n=== Thread 1: Faktorial ===");
            System.out.println(
                "Faktorial dari " +
                data.angkaFaktorial +
                " = " +
                hasil
            );
        }
    }
}

/* =========================
   THREAD 2: FIBONACCI
   ========================= */
class FibonacciRunnable implements Runnable {

    private DataThread data;

    FibonacciRunnable(DataThread data) {
        this.data = data;
    }

    @Override
    public void run() {
        long a = 0;
        long b = 1;

        synchronized (System.out) {
            System.out.println("\n=== Thread 2: Fibonacci ===");
            System.out.println(
                "Deret Fibonacci hingga suku ke-" +
                data.jumlahFibonacci + ":"
            );

            for (int i = 0; i < data.jumlahFibonacci; i++) {
                System.out.print(a + " ");

                long berikutnya = a + b;
                a = b;
                b = berikutnya;
            }

            System.out.println();
        }
    }
}

/* =========================
   THREAD 3: MEMBACA FILE
   ========================= */
class BacaFileRunnable implements Runnable {

    private DataThread data;

    BacaFileRunnable(DataThread data) {
        this.data = data;
    }

    @Override
    public void run() {
        synchronized (System.out) {
            System.out.println("\n=== Thread 3: Membaca Berkas ===");

            try {
                BufferedReader reader =
                    new BufferedReader(
                        new FileReader(data.namaFile)
                    );

                String baris;

                System.out.println("Isi berkas:");

                while ((baris = reader.readLine()) != null) {
                    System.out.println(baris);
                }

                reader.close();

            } catch (IOException e) {
                System.out.println("Berkas tidak dapat dibuka.");
            }
        }
    }
}

/* =========================
   MAIN
   ========================= */
public class Program {

    public static void main(String[] args) throws InterruptedException {

        java.util.Scanner input =
            new java.util.Scanner(System.in);

        /* Input */
        System.out.print("Masukkan angka untuk faktorial: ");
        int angkaFaktorial = input.nextInt();

        System.out.print("Masukkan jumlah suku Fibonacci: ");
        int jumlahFibonacci = input.nextInt();

        System.out.print("Masukkan nama berkas teks: ");
        String namaFile = input.next();

        /* Data yang digunakan oleh thread */
        DataThread data = new DataThread(
            angkaFaktorial,
            jumlahFibonacci,
            namaFile
        );

        /* Membuat Runnable */
        Runnable faktorial = new FaktorialRunnable(data);
        Runnable fibonacci = new FibonacciRunnable(data);
        Runnable bacaFile = new BacaFileRunnable(data);

        /* Membuat 3 Thread */
        Thread thread1 = new Thread(faktorial);
        Thread thread2 = new Thread(fibonacci);
        Thread thread3 = new Thread(bacaFile);

        /* Menjalankan 3 thread */
        thread1.start();
        thread2.start();
        thread3.start();

        /* Menunggu semua thread selesai */
        thread1.join();
        thread2.join();
        thread3.join();

        System.out.println("\nSemua thread telah selesai.");

        input.close();
    }
}