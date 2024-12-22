import java.text.SimpleDateFormat;  // Mengimpor kelas untuk memformat tanggal dan waktu
import java.util.*;  // Mengimpor kelas utilitas (misalnya, Scanner, List, dll.)
import java.sql.*;  // Mengimpor kelas untuk operasi basis data SQL
import java.sql.Date;  // Mengimpor kelas Date untuk menangani tipe data SQL Date

// Antarmuka yang mendefinisikan operasi CRUD dasar (Create, Read, Update, Delete)
interface CRUDOperations {
    void create();  // Metode untuk menambahkan data baru
    void read();  // Metode untuk mengambil data
    void update();  // Metode untuk memperbarui data yang ada
    void delete();  // Metode untuk menghapus data
}

// Kelas abstrak yang mewakili item inventaris secara umum
abstract class ItemInventaris {
    protected String id;  // ID dari item inventaris
    protected String nama;  // Nama dari item inventaris

    // Konstruktor untuk menginisialisasi item inventaris
    public ItemInventaris(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    // Metode abstrak untuk menampilkan rincian item inventaris
    public abstract void displayDetails();
}

// Kelas yang mewakili peralatan musik (menurunkan dari ItemInventaris)
class PeralatanMusik extends ItemInventaris {
    private int jumlah;  // Jumlah peralatan musik
    private String kondisi;  // Kondisi peralatan musik

    // Konstruktor untuk menginisialisasi objek peralatan musik
    public PeralatanMusik(String id, String nama, int jumlah, String kondisi) {
        super(id, nama);  // Memanggil konstruktor dari kelas induk (ItemInventaris)
        this.jumlah = jumlah;
        this.kondisi = kondisi;
    }

    // Implementasi metode displayDetails untuk menampilkan rincian peralatan musik
    @Override
    public void displayDetails() {
        System.out.printf("%-15s%-30s%-15d%-15s\n", id, nama, jumlah, kondisi);
    }

    // Metode untuk mendapatkan jumlah peralatan musik
    public int getJumlah() {
        return jumlah;
    }
}

// Kelas utama yang mengelola manajemen inventaris musik dan mengimplementasikan operasi CRUD
public class ManajemenInventarisMusik implements CRUDOperations {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/inventaris_musik";  // URL koneksi ke basis data
    private static final String DB_USERNAME = "postgres";  // Username untuk koneksi basis data
    private static final String DB_PASSWORD = "naisya1";  // Password untuk koneksi basis data

    private List<PeralatanMusik> peralatanList = new ArrayList<>();  // Daftar untuk menyimpan peralatan musik
    private Scanner scanner = new Scanner(System.in);  // Scanner untuk input dari pengguna

    // Mendefinisikan warna untuk output terminal
    private static final String RESET = "\033[0m";  
    private static final String GREEN = "\033[0;32m";  
    private static final String BLUE = "\033[0;34m";  
    private static final String RED = "\033[0;31m";  
    private static final String CYAN = "\033[0;36m";
    private static final String YELLOW = "\033[0;33m";

    // Fungsi utama untuk menjalankan aplikasi
    public static void main(String[] args) {
        ManajemenInventarisMusik app = new ManajemenInventarisMusik();
        app.run();
    }

    // Metode untuk menjalankan menu dan interaksi dengan pengguna
    private void run() {
        boolean keluar = false;  // Variabel untuk menentukan apakah aplikasi harus berhenti
        System.out.println(GREEN + "+-----------------------------------------------------+");
        System.out.println("Selamat Datang di Sistem Manajemen Inventaris Musik");
        tampilkanTanggalWaktuSaatIni();  // Menampilkan tanggal dan waktu saat ini
        System.out.println("+-----------------------------------------------------+" + RESET);

        // Loop untuk menampilkan menu dan meminta input dari pengguna
        while (!keluar) {
            System.out.println(CYAN + "Menu:");
            System.out.println("1. Tambah Peralatan Musik");
            System.out.println("2. Lihat Peralatan Musik");
            System.out.println("3. Perbarui Peralatan Musik");
            System.out.println("4. Hapus Peralatan Musik");
            System.out.println("5. Hitung Total Jumlah Peralatan");
            System.out.println("6. Keluar" + RESET);

            System.out.print(GREEN + "Pilih menu: ");
            int pilihan = masukkanAngkaInteger("Pilihan");

            switch (pilihan) {
                case 1 -> create();  // Menambahkan peralatan musik
                case 2 -> read();  // Menampilkan daftar peralatan musik
                case 3 -> update();  // Memperbarui data peralatan musik
                case 4 -> delete();  // Menghapus data peralatan musik
                case 5 -> hitungTotalJumlah();  // Menghitung total jumlah peralatan musik
                case 6 -> {  // Keluar dari aplikasi
                    keluar = true;
                    System.out.println(RED + "Terima kasih telah menggunakan Sistem Manajemen Inventaris Musik." + RESET);
                }
                default -> System.out.println(RED + "Pilihan tidak valid. Coba lagi." + RESET);  // Menangani pilihan tidak valid
            }
        }
    }

    // Metode untuk menampilkan tanggal dan waktu saat ini
    private void tampilkanTanggalWaktuSaatIni() {
        Date tanggal = new Date(System.currentTimeMillis());  // Mendapatkan waktu saat ini
        SimpleDateFormat formatTanggal = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println(BLUE + "Tanggal dan Waktu: " + formatTanggal.format(tanggal) + RESET);
    }

    // Metode untuk meminta input angka dari pengguna
    private int masukkanAngkaInteger(String prompt) {
        while (true) {
            try {
                System.out.print(CYAN + prompt + ": " + RESET);
                return Integer.parseInt(scanner.nextLine());  // Membaca angka input dari pengguna
            } catch (NumberFormatException e) {
                System.out.println(RED + prompt + " harus berupa angka bulat yang valid!" + RESET);  // Menangani input yang tidak valid
            }
        }
    }

    // Metode untuk meminta input string dari pengguna
    private String masukkanString(String prompt) {
        while (true) {
            System.out.print(CYAN + prompt + ": " + RESET);
            String input = scanner.nextLine();
            if (!input.isEmpty()) {
                return input;  // Mengembalikan input yang valid
            }
            System.out.println(RED + prompt + " tidak boleh kosong!" + RESET);  // Menangani input kosong
        }
    }

    // Implementasi metode create untuk menambah peralatan musik
    @Override
    public void create() {
        String id = masukkanString("ID Peralatan");
        String nama = masukkanString("Nama Peralatan");
        int jumlah = masukkanAngkaInteger("Jumlah Peralatan");
        String kondisi = masukkanString("Kondisi Peralatan");

        PeralatanMusik peralatan = new PeralatanMusik(id, nama, jumlah, kondisi);  // Membuat objek peralatan musik
        peralatanList.add(peralatan);  // Menambahkan peralatan musik ke daftar

        // Menyimpan data peralatan musik ke dalam basis data
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "INSERT INTO peralatan_musik (id_peralatan, nama_peralatan, jumlah, kondisi) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            statement.setString(2, nama);
            statement.setInt(3, jumlah);
            statement.setString(4, kondisi);
            statement.executeUpdate();  // Menjalankan query untuk memasukkan data
            System.out.println(GREEN + "Data berhasil disimpan." + RESET);
        } catch (SQLException e) {
            System.out.println(RED + "Terjadi kesalahan: " + e.getMessage() + RESET);  // Menangani kesalahan saat berinteraksi dengan basis data
        }
    }

    // Implementasi metode read untuk menampilkan peralatan musik
    @Override
    public void read() {
        peralatanList.clear();  // Membersihkan daftar peralatan musik yang ada
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM peralatan_musik";  // Query untuk mengambil data peralatan musik
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Menampilkan data peralatan musik
            System.out.println(YELLOW + "+-----------------------------------------------------+");
            System.out.printf("%-15s%-30s%-15s%-15s\n", "ID", "Nama", "Jumlah", "Kondisi");
            System.out.println("+-----------------------------------------------------+" + RESET);
            while (resultSet.next()) {
                PeralatanMusik peralatan = new PeralatanMusik(
                    resultSet.getString("id_peralatan"),
                    resultSet.getString("nama_peralatan"),
                    resultSet.getInt("jumlah"),
                    resultSet.getString("kondisi")
                );
                peralatanList.add(peralatan);  // Menambahkan peralatan ke daftar
                peralatan.displayDetails();  // Menampilkan rincian peralatan
            }
            System.out.println("+-----------------------------------------------------+");
        } catch (SQLException e) {
            System.out.println(RED + "Terjadi kesalahan: " + e.getMessage() + RESET);  // Menangani kesalahan saat mengambil data dari basis data
        }
    }

    // Implementasi metode update untuk memperbarui peralatan musik
    @Override
    public void update() {
        String id = masukkanString("Masukkan ID Peralatan yang ingin diperbarui");
        String namaBaru = masukkanString("Nama Peralatan Baru");
        int jumlahBaru = masukkanAngkaInteger("Jumlah Baru");
        String kondisiBaru = masukkanString("Kondisi Baru");

        // Melakukan update data peralatan musik berdasarkan ID
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "UPDATE peralatan_musik SET nama_peralatan = ?, jumlah = ?, kondisi = ? WHERE id_peralatan = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, namaBaru);
            statement.setInt(2, jumlahBaru);
            statement.setString(3, kondisiBaru);
            statement.setString(4, id);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(GREEN + "Data berhasil diperbarui." + RESET);  // Menampilkan pesan jika data berhasil diperbarui
            } else {
                System.out.println(RED + "ID tidak ditemukan." + RESET);  // Menangani jika ID tidak ditemukan
            }
        } catch (SQLException e) {
            System.out.println(RED + "Terjadi kesalahan: " + e.getMessage() + RESET);  // Menangani kesalahan saat berinteraksi dengan basis data
        }
    }

    // Implementasi metode delete untuk menghapus peralatan musik
    @Override
    public void delete() {
        String id = masukkanString("Masukkan ID Peralatan yang ingin dihapus");

        // Menghapus data peralatan musik berdasarkan ID
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "DELETE FROM peralatan_musik WHERE id_peralatan = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println(GREEN + "Data berhasil dihapus." + RESET);  // Menampilkan pesan jika data berhasil dihapus
            } else {
                System.out.println(RED + "ID tidak ditemukan." + RESET);  // Menangani jika ID tidak ditemukan
            }
        } catch (SQLException e) {
            System.out.println(RED + "Terjadi kesalahan: " + e.getMessage() + RESET);  // Menangani kesalahan saat menghapus data dari basis data
        }
    }

    // Metode untuk menghitung total jumlah peralatan musik
    private void hitungTotalJumlah() {
        int total = 0;
        for (PeralatanMusik peralatan : peralatanList) {
            total += peralatan.getJumlah();  // Menambahkan jumlah peralatan musik
        }
        System.out.println(GREEN + "Total jumlah peralatan musik: " + total + RESET);  // Menampilkan total jumlah peralatan
    }
}