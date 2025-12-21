import java.util.Scanner; //Input
import java.io.FileWriter; //Menulis file di ProjectData.txt dan UserData.txt
import java.io.IOException; //Handling error di file
import java.io.FileReader; //Membaca file di ProjectData.txt dan UserData.txt
import java.io.BufferedReader; //FileReader versi lebih cepat
import java.time.LocalDate; //Untuk deadline atau tenggat proyek

public class AplikasiManajemenProyek{

    //===Function ASCII Color - Memberikan Warna UI CLI (param = int pilihanWarna from developeer) 
    static String asciiColor(int pilihanWarna) {
        if(pilihanWarna == 0) {
            return "\u001B[0m"; //Reset (ditambahkan diakhir)
        }else if(pilihanWarna == 1) {
            return "\u001B[31m"; //Merah (error, )
        }else if(pilihanWarna == 2) {
            return "\u001B[32m"; //Hijau (inputan user)
        }else {
            return "\u001B[34m"; //Biru
        }
    }

    //===Function Login Akun (param = username from user input, password from user input)

    static boolean login(String usn, String pass) {
        boolean login = false;
        boolean akunDitemukan = false;
        try {
            //Inisialisasi variabel dan library
            BufferedReader reader = new BufferedReader(new FileReader("UserData.txt"));
            String baris;

            while((baris = reader.readLine()) != null) {
                //Membaca ":" sebagai pembatas (split)
                String dataAkun[] = baris.split(":");

                if(dataAkun.length >= 2) {
                    //Memisahkan format username:password:statusAkun dari UserData.txt menjadi variabel tersendiri untuk username dan password
                    String username = dataAkun[0];
                    String password = dataAkun[1];
                    if(username.equals(usn) && password.equals(pass)){
                        akunDitemukan = true;
                        break;
                    }
                }
            }            
        } catch (IOException e) {
            //Handling error dari library
            System.out.println(asciiColor(1) + "(!) Terjadi Error Pada Sistem!" + asciiColor(0));
        }

        //Memberikan notifikasi berhasil/tidak
        if(akunDitemukan) {
            login = true;
            System.out.println(asciiColor(3) + "-> Login Berhasil!" + asciiColor(0));
        }else {
            login = false;
            System.out.println(asciiColor(1) + "(!) Username atau Password Salah!" + asciiColor(0));
        }
        return login;
    }

    //===Function Register Akun (param = usn from user input, pass from user input, statAcc from UserData.txt)

    static void register(String usn, String pass, String statAcc) {
        try {
            //Inisialisasi variabel dan library
            boolean usernameDigunakan = false;
            BufferedReader reader = new BufferedReader(new FileReader("UserData.txt"));
            String baris;
            //Mengumpulkan data dari database
            while((baris = reader.readLine()) != null) {
                String dataAkun[] = baris.split(":");
                String username = dataAkun[0];
                if(username.equals(usn)) {
                    usernameDigunakan = true;
                }
            }
            reader.close();
            
            if(usernameDigunakan) {
                System.out.println(asciiColor(1) + "(!) Akun Sudah Ada!" + asciiColor(0));
            } else {
                FileWriter writer = new FileWriter("UserData.txt", true); //Append mode agar file tidak dioverwrite tetapi ditambahkan
                writer.write(usn + ":" + pass + ":" + statAcc + System.lineSeparator()); //Agar data yang baru tertulis pada new line, bukan disebelah data lama
                writer.close();
                System.out.println(asciiColor(3) + "(!) Akun Berhasil Dibuat!" + asciiColor(0));
            }
        } catch (IOException e) {
            //Handling error
            System.out.println(asciiColor(1) + "(!) Terjadi Kesalahan Pada Sistem!" + asciiColor(0));
        }
    }

    //===Function Menentukan Status Akun - Menentukan Akun Berstatus Manajer/Pekerja (param usn from user input, pass from user input)

    static boolean statusManajer(String usn, String pass) {
        boolean statusManajer = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("UserData.txt"));
            String baris;
            while((baris = reader.readLine()) != null) {
                //Memisah baris yang terbaca menjadi sebuah array
                String data[] = baris.split(":");
                //Mengambil String username dan password dari baris yang sudah dipecah menjadi array
                String username = data[0];
                String password = data[1];
                if(username.equals(usn) && password.equals(pass)) {
                    String statusAkun = data[2];
                    if(statusAkun.equals("manajer")) {
                        statusManajer = true;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(asciiColor(1) + "(!) Terjadi Error Saat Verifikasi Status Akun!" + asciiColor(0));
        }
        return statusManajer;
    }

    //===Function Dashboard Manajer - Masuk Ke Dashboard dan Unlock Fitur Manajer

    static void dashboardManajer() {
        //Inisialisasi
        Scanner input = new Scanner(System.in);
        //Menampilkan ringkasan proyek
        ringkasanProyek();
        while (true) { 
            //Select menu
            System.out.println("\n");
            System.out.println("[1] Lihat Proyek");
            System.out.println("[2] Tambah Proyek");
            System.out.println("[3] Edit Proyek");
            System.out.println("[4] Hapus Proyek");
            System.out.println("[5] Cari Proyek");
            System.out.println("[6] Keluar");
            System.out.print(asciiColor(2) + "-> Masukkan Pilihan: " + asciiColor(0));
            int pilihanMenu = input.nextInt();

            //Mengarahkan ke function-function yang sudah dibuat
            if(pilihanMenu == 1) {
                lihatProyek();
            } else if(pilihanMenu == 2) {
                tambahProyek();
            } else if(pilihanMenu == 3) {
                editProyek();
            } else if(pilihanMenu == 4) {
                hapusProyek();
            } else if(pilihanMenu == 5) {
                cariProyek();
            } else if(pilihanMenu == 6) {
                System.exit(pilihanMenu);
                break;
            } else {
                System.out.println(asciiColor(1) + "(!) Pilihan Tidak Valid!" + asciiColor(0));
            }
        }
        input.close();
    }

    //=== Function Ringkasan Proyek - Menampilkan Ringkasan Proyek dari Database
    
    static void ringkasanProyek() {
        //Inisialisasi variabel dan library
        int totalProyek = 0;
        int prioritasTinggi = 0;
        int prioritasSedang = 0;
        int prioritasRendah = 0;
        int totalPekerja = 0;
        int jobdeskSudah = 0;
        int jobdeskBelum = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader("ProjectData.txt"));
            String baris;

            //Mengumpulkan dan memisahkan data setiap atribut proyek dalam ProjectData.txt
            while((baris = reader.readLine()) != null) {
                //Dilihat dari jumlah baris pada ProjectData.txt
                totalProyek++;
                String data[] = baris.split(":");
                
                String prioritas = data[4];
                if(prioritas.equals("Tinggi")) {
                    prioritasTinggi++;
                } else if(prioritas.equals("Sedang")) {
                    prioritasSedang++;
                } else {
                    prioritasRendah++;
                }

                String dataPekerja[] = data[3].split(",");

                for(int i = 0; i < dataPekerja.length; i++) {
                    totalPekerja++;

                    String detailPekerja[] = dataPekerja[i].split("-");
                    if(detailPekerja[2].equals("Sudah")) {
                        jobdeskSudah++;
                    } else {
                        jobdeskBelum++;
                    }
                }
            }
            reader.close();

            //Hasil dioutputkan
            System.out.println("\n");
            System.out.println(asciiColor(2) + "(!) PROYEK YANG SEDANG BERLANGSUNG");
            System.out.println(asciiColor(3) + "|Total Proyek       : " + asciiColor(1) + totalProyek + asciiColor(0));
            System.out.println(asciiColor(3) + "|Prioritas Tinggi   : " + asciiColor(1) + prioritasTinggi + asciiColor(0));
            System.out.println(asciiColor(3) + "|Prioritas Sedang   : " + asciiColor(1) + prioritasSedang + asciiColor(0));
            System.out.println(asciiColor(3) + "|Prioritas Rendah   : " + asciiColor(1) + prioritasRendah + asciiColor(0));
            System.out.println(asciiColor(3) + "|Total Pekerja      : " + asciiColor(1) + totalPekerja + asciiColor(0));
            System.out.println(asciiColor(3) + "|Jobdesk Selesai    : " + asciiColor(1) + jobdeskSudah + asciiColor(0));
            System.out.println(asciiColor(3) + "|Jobdesk Belum      : " + asciiColor(1) + jobdeskBelum + asciiColor(0));
        } catch (IOException e) {
            //Handling library error
            System.out.println(asciiColor(1) + "(!) Terjadi Kesalahan Pada Sistem!" + asciiColor(0));
        }
    }

    //=== Function Format Tabel Lihat Proyek 
    
    static String formatTabel(String text, int lebarKolom) {
        if(text.length() > lebarKolom) {
            return text.substring(0, lebarKolom - 3) + "..."; //Apabila nama proyek atau lainnya terlalu panjang hingga melewati lebar kolom yang ditentukan, nama proyek akan dipotong sesuai lebar kolom lalu ditambahkan ... agar format tabel tetap rapih dan tidak berantakan
        }
        return String.format("%-" + lebarKolom + "s", text); //Menghasilkan format untuk penulisan string yang dimasukkan ke dalam function, %- ditambahkan agar teks rata kiri, lebarKolom + s menandakan maksimal karakter dalam kolom tersebut
    }

    //=== Function Lihat Proyek - Menampilkan List Proyek dari Database

    static void lihatProyek() {
        System.out.println(asciiColor(1) + "==================================================================================================================================================" + asciiColor(0)); //146 char
        System.out.println(asciiColor(2) + "                                             ______          _           _         _     _     _   " + asciiColor(0));
        System.out.println(asciiColor(2) + "                                             | ___ \\        (_)         | |       | |   (_)   | |  " + asciiColor(0));
        System.out.println(asciiColor(2) + "                                             | |_/ / __ ___  _  ___  ___| |_      | |    _ ___| |_ " + asciiColor(0));
        System.out.println(asciiColor(2) + "                                             |  __/ '__/ _ \\| |/ _ \\/ __| __|     | |   | / __| __|" + asciiColor(0));
        System.out.println(asciiColor(2) + "                                             | |  | | | (_) | |  __/ (__| |_      | |___| \\__ \\ |_ " + asciiColor(0));
        System.out.println(asciiColor(2) + "                                             \\_|  |_|  \\___/| |\\___|\\___|\\__|     \\_____/_|___/\\__|" + asciiColor(0));
        System.out.println(asciiColor(2) + "                                                           _/ |                                    " + asciiColor(0));
        System.out.println(asciiColor(2) + "                                                          |__/                                     " + asciiColor(0));

        System.out.println(asciiColor(1) + "==================================================================================================================================================" + asciiColor(0));
        System.out.println(asciiColor(3) + "__________________________________________________________________________________________________________________________________________________" + asciiColor(0));
        //Header tabel proyek
        System.out.println(asciiColor(3) + "|" + formatTabel("Nama Proyek", 30) + "|" + formatTabel("Anggaran Proyek", 25) + "|" + formatTabel("Deadline", 10) + "|" + formatTabel("Pekerja", 25) + "|" + formatTabel("Jobdesk", 30) + "|" + formatTabel("Status", 10) + "|" + formatTabel("Prioritas", 10) + "|" + asciiColor(0));
        System.out.println(asciiColor(3) + "__________________________________________________________________________________________________________________________________________________" + asciiColor(0));
        try {
            //Inisialisasi variabel dan library
            BufferedReader reader = new BufferedReader(new FileReader("ProjectData.txt"));
            String baris;
            //Mengumpulkan data dari ProjectData.txt
            while((baris = reader.readLine()) != null) {
                //Memecah baris menjadi atribut-atribut proyek
                String data[] = baris.split(":");
                String namaProyek = data[0];
                String anggaranProyek = data[1];
                String deadlineProyek = data[2];
                String dataPekerja[] = data[3].split(",");
                String prioritasProyek = data[4];

                for(int i = 0; i < dataPekerja.length; i++) {
                    //Memecah data pekerja menjadi lebih detail (nama, jobdesk, status jobdesk)
                    String detailpekerja[] = dataPekerja[i].split("-");
                    String namaPekerja = detailpekerja[0];
                    String jobdeskPekerja = detailpekerja[1];
                    String statusJobdesk = detailpekerja[2];
                    
                    //Output hasil proyek dari database
                    if(statusJobdesk.equals("Belum")) {
                        System.out.println(asciiColor(3) + "|" + asciiColor(0) + formatTabel(namaProyek, 30) + asciiColor(3) + "|" + asciiColor(0) + formatTabel(anggaranProyek, 25) + asciiColor(3) + "|" + asciiColor(0) + formatTabel(deadlineProyek, 10) + asciiColor(3) + "|" + asciiColor(0) + formatTabel(namaPekerja, 25) + asciiColor(3) + "|" + asciiColor(0) + asciiColor(1) + formatTabel(jobdeskPekerja, 30) + asciiColor(0) + asciiColor(3) + "|" + asciiColor(0) + asciiColor(1) + formatTabel(statusJobdesk, 10) + asciiColor(0) + asciiColor(3) + "|" + asciiColor(0) + formatTabel(prioritasProyek, 10) + asciiColor(3) + "|" + asciiColor(0));
                    } else {
                        System.out.println(asciiColor(3) + "|" + asciiColor(0) + formatTabel(namaProyek, 30) + asciiColor(3) + "|" + asciiColor(0) + formatTabel(anggaranProyek, 25) + asciiColor(3) + "|" + asciiColor(0) + formatTabel(deadlineProyek, 10) + asciiColor(3) + "|" + asciiColor(0) + formatTabel(namaPekerja, 25) + asciiColor(3) + "|" + asciiColor(0) + asciiColor(2) + formatTabel(jobdeskPekerja, 30) + asciiColor(0) + asciiColor(3) + "|" + asciiColor(0) + asciiColor(2) + formatTabel(statusJobdesk, 10) + asciiColor(0) + asciiColor(3) + "|" + asciiColor(0) + formatTabel(prioritasProyek, 10) + asciiColor(3) + "|" + asciiColor(0));
                    }

                    namaProyek = ""; //Variabel namaProyek, dll direset menjadi kosong agar tabel kebawah tidak mengulang penulisan namaProyek, dll
                    anggaranProyek = "";
                    deadlineProyek = ""; 
                    prioritasProyek = "";
                    
                }
                System.out.println(asciiColor(3) + "__________________________________________________________________________________________________________________________________________________" + asciiColor(0));
            }
        } catch (IOException e) {
            //Handling error library
            System.out.println(asciiColor(1) + "(!) Terjadi Kesalahan Pada Sistem!" + asciiColor(0));
        }
    }

    //=== Function Tambah Proyek - Fitur Khusus Manager Untuk Menambah Proyek Baru ke Database (ProjectData.txt)

    static void tambahProyek() {
        Scanner input = new Scanner(System.in);

        //Mengumpulkan data untuk proyek baru
        System.out.print(asciiColor(2) + "-> Masukkan Nama Proyek: " + asciiColor(0));
        String namaProyek = input.nextLine();
        System.out.print(asciiColor(2) + "-> Masukkan Total Anggaran Proyek: " + asciiColor(0));
        int anggaranProyek = input.nextInt();
        input.nextLine();
        System.out.print(asciiColor(2) + "-> Masukkan Deadline (Tenggat) Proyek (YYYY-MM-DD): " + asciiColor(0));
        String inputDeadlineProyek = input.next();
        input.nextLine();
        LocalDate deadlineProyek = LocalDate.parse(inputDeadlineProyek);
        
        String pekerjaProyek[] = new String[20];
        String jobdeskPekerja[] = new String[20];
        int i = 0;
        
        //Looping untuk input data pekerja (pekerja proyek umumnya lebih dari satu)
        while (true) { 
            System.out.print(asciiColor(2) + "-> Masukkan Pekerja (Masukkan 'Berhenti' Untuk Berhenti): " + asciiColor(0));
            pekerjaProyek[i] = input.nextLine();
            if(pekerjaProyek[i].equals("Berhenti")) {
                break;
            }
            System.out.print(asciiColor(2) + "-> Masukkan Jobdesk " + pekerjaProyek[i] + ": " + asciiColor(0));
            jobdeskPekerja[i] = input.nextLine();
            i++;
        }

        System.out.println(asciiColor(3) + "-> Prioritas Proyek" + asciiColor(0));
        System.out.println("   [1] Tinggi");
        System.out.println("   [2] Sedang");
        System.out.println("   [3] Rendah");
        System.out.print(asciiColor(3) + "-> Masukkan Pilihan Prioritas Proyek: " + asciiColor(0));
        int prioritasProyek = input.nextInt();
        String teksPrioritas = "";
        if(prioritasProyek > 3 || prioritasProyek < 1) {
            System.out.println(asciiColor(1) + "(!) Pilihan Tidak Valid!" + asciiColor(0));
        } else {
            teksPrioritas = prioritasProyek == 1 ? "Tinggi": prioritasProyek == 2 ? "Sedang": "Rendah";
        }

        //Gabungkan Array pekerjaProyek[] dan jobdeskPekerja[] agar mudah dibaca dan diwrite

        String dataPekerja = "";
        for(int j = 0; j < i; j++) {
            //Char - berperan sebagai separator antara nama pekerja dan jobdesknya
            dataPekerja += pekerjaProyek[j] + "-" + jobdeskPekerja[j] + "-Belum";
            if(j < (i-1)) {
                //Char , berperan sebagai separator antara setiap dataPekerja
                dataPekerja += ",";
            }
        }

        try {
            //Menuliskan data-data yang sudah dikumpulkan dari input ke file ProjectData.txt
            FileWriter writer = new FileWriter("ProjectData.txt", true);
            writer.write(namaProyek + ":" + anggaranProyek + ":" + deadlineProyek + ":" + dataPekerja + ":" + teksPrioritas + System.lineSeparator());
            writer.close();
            //Menginformasikan data berhasil ditambahkan
            System.out.println(asciiColor(3) + "-> Proyek Berhasil Ditambahkan!" + asciiColor(0));
        } catch (IOException e) {
            //Handling error library
            System.out.println(asciiColor(1) + "(!) Terjadi Error Saat Penambahan Proyek!" + asciiColor(0));
        }

    }

    //===Function Edit Proyek - Fitur Khusus Manajer Untuk Mengubah Data Proyek Yang Sudah Ada 

    static void editProyek() {
        Scanner input = new Scanner(System.in);
        lihatProyek(); //Menampilkan tabel daftar proyek yang berlangsung

        //Keyword hanya dari nama proyek
        System.out.print(asciiColor(2) + "-> Masukkan Nama Proyek Yang Ingin Diedit: " + asciiColor(0));
        String proyekDiedit = input.nextLine().trim();

        String hasilEdit = "";
        boolean ditemukan = false;

        try {
            //Inisialisasi variabel dan library
            BufferedReader reader = new BufferedReader(new FileReader("ProjectData.txt"));
            String baris;

            while((baris = reader.readLine()) != null) {
                String data[] = baris.split(":");
                String namaProyek = data[0];
                String anggaranProyek = data[1];
                String deadlineProyek = data[2];
                String dataPekerja = data[3];
                String prioritasProyek = data[4];

                if(namaProyek.equalsIgnoreCase(proyekDiedit)) {
                    ditemukan = true;

                    //Select menu untuk mengedit atribut dari proyek yang dipilih
                    System.out.println(asciiColor(3) + "Proyek Yang Dipilih: " + asciiColor(0) + namaProyek);
                    System.out.println(asciiColor(3) + "Atribut Yang Ingin Diedit: " + asciiColor(0));
                    System.out.println("[1] Nama Proyek");
                    System.out.println("[2] Anggaran Proyek");
                    System.out.println("[3] Deadline Proyek");
                    System.out.println("[4] Data Pekerja");
                    System.out.println("[5] Prioritas Proyek");
                    System.out.println("[6] Batal");
                    System.out.print(asciiColor(2) + "-> Masukkan Pilihan: " + asciiColor(0));
                    int pilihan = input.nextInt();
                    input.nextLine();

                    //Mengumpulkan data untuk atribut yang ingin diubah
                    switch(pilihan) {
                        case 1:
                            System.out.print(asciiColor(2) + "-> Nama Proyek Baru: " + asciiColor(0));
                            namaProyek = input.nextLine();
                            break;
                        case 2:
                            System.out.print(asciiColor(2) + "-> Anggaran Baru: " + asciiColor(0));
                            anggaranProyek = input.nextLine();
                            break;
                        case 3:
                            System.out.print(asciiColor(2) + "-> Deadline Baru (YYYY/MM/DD): " + asciiColor(0));
                            deadlineProyek = input.nextLine();
                            break;
                        case 4:
                            //Data pekerja diinput ulang
                            System.out.println(asciiColor(3) + "Edit Data Pekerja" + asciiColor(0));
                            System.out.println(asciiColor(1) + "Masukkan 'Berhenti' Untuk Berhenti");
                            String dataPekerjaEdit = "";

                            while(true) { 
                                System.out.print(asciiColor(2) + "-> Masukkan Data Pekerja (Nama-Jobdesk-Status): " + asciiColor(0));
                                String inputDataPekerjaEdit = input.nextLine();

                                if(inputDataPekerjaEdit.equalsIgnoreCase("Berhenti")) {
                                    break;
                                }

                                dataPekerjaEdit += inputDataPekerjaEdit + ",";
                            }
                            
                            //Untuk menghapus koma pada data pekerja terakhir
                            if(!dataPekerjaEdit.equals("!")) {
                                dataPekerjaEdit = dataPekerjaEdit.substring(0, dataPekerjaEdit.length() - 1);
                                dataPekerja = dataPekerjaEdit;
                            } 
                            break;
                        case 5:
                            System.out.println(asciiColor(3) + "Edit Prioritas Proyek" + asciiColor(0));
                            System.out.println("[1] Tinggi");
                            System.out.println("[2] Sedang");
                            System.out.println("[3] Rendah");
                            System.out.print(asciiColor(2) + "-> Prioritas Baru: " + asciiColor(0));
                            int pilihanPrioritas = input.nextInt();
                            if(pilihanPrioritas == 1) {
                                prioritasProyek = "Tinggi";
                            } else if(pilihanPrioritas == 2) {
                                prioritasProyek = "Sedang";
                            } else if(pilihanPrioritas == 3) {
                                prioritasProyek = "Rendah";
                            } else {
                                System.out.println(asciiColor(1) + "Input Tidak Valid!" + asciiColor(0));
                            }
                        case 6:
                            break;
                        default:
                            System.out.println(asciiColor(1) + "Input Tidak Valid!" + asciiColor(0));
                            return;
                    }
                }
                //Mengumpulkan data yang diubah ke dalam bentuk variabel baru
                hasilEdit += namaProyek + ":" + anggaranProyek + ":" + deadlineProyek + ":" + dataPekerja + ":" + prioritasProyek + System.lineSeparator();
            }
            reader.close();

            //Memasukkan hasil perubahan ke ProjectData.txt
            if(ditemukan) {
                FileWriter writer = new FileWriter("ProjectData.txt");
                writer.write(hasilEdit);
                writer.close();
                System.out.println(asciiColor(3) + "-> Proyek Berhasil Diedit " + asciiColor(0));
            }else {
                System.out.println(asciiColor(1) + "(!) Proyek Tidak Ditemukan!" + asciiColor(0));
            }
        } catch (IOException e) {
            System.out.println(asciiColor(1) + "(!) Terjadi Kesalahan Pada Sistem!" + asciiColor(0));
        }
    }

    //===Function Hapus Proyek - Fitur Khusus Manajer Untuk Menghapus Proyek

    static void hapusProyek() {
        Scanner input = new Scanner(System.in);

        lihatProyek();
        System.out.print(asciiColor(2) + "-> Masukkan Nama Proyek Yang Ingin Dihapus: " + asciiColor(0));
        //Input proyek yang akan dihapus
        String proyekDihapus = input.nextLine().trim();

        String proyekTidakDihapus = "";
        boolean ditemukan = false;

        try {
            //Inisialisasi variabel dan library
            BufferedReader reader = new BufferedReader(new FileReader("ProjectData.txt"));
            String baris; 
            //Membaca seluruh data di ProjectData.txt
            while((baris = reader.readLine()) != null) {
                String data[] = baris.split(":");
                String namaProyek = data[0];

                //Apabila nama proyek yang ingin dihapus ditemukan, boolean ditemukan = true
                if(namaProyek.equalsIgnoreCase(proyekDihapus)) {
                    ditemukan = true;
                    //Proyek yang dihapus akan diskip
                    continue;
                }
                //Logika penghapusan dibalik, jadi menghapus bukan menghilangkan langsung dari file ProjectData.txt, tapi menulis ulang data tanpa memasukkan data proyek yang dihapus
                proyekTidakDihapus += baris + System.lineSeparator();
            }

            reader.close();

            //Menulis ulang ProjectData.txt tanpa menuliskan proyek yang dihapus
            if(ditemukan) {
                FileWriter writer = new FileWriter("ProjectData.txt");
                writer.write(proyekTidakDihapus);
                writer.close(); //Jika tidak ditambahkan maka semua file akan terhapus
                System.out.println(asciiColor(3) + "-> Proyek Berhasil Dihapus!" + asciiColor(0));
            }else {
                System.out.println(asciiColor(1) + "(!) Proyek Tidak Ditemukan!" + asciiColor(0));
            }
        } catch(IOException e) {
            System.out.println(asciiColor(1) + "(!) Terjadi Kesalahan Pada Sistem!" + asciiColor(0));
        }
    }

    //===Function Cari Proyek - Fitur Untuk Mencari Proyek Yang Diinginkan

    static void cariProyek() {
        //Select menu mencari proyek berdasarkan atribut apa
        Scanner input = new Scanner(System.in);
        System.out.println(asciiColor(3) + "-> Cari Proyek Berdasarkan: " + asciiColor(0));
        System.out.println("   [1] Nama Proyek");
        System.out.println("   [2] Deadline Proyek");
        System.out.println("   [3] Nama Pekerja");
        System.out.println("   [4] Jobdesk Pekerja");
        System.out.println("   [5] Prioritas Proyek");
        System.out.print(asciiColor(2) + "-> Masukkan Pilihan: " + asciiColor(0));
        int pilihanSearch = input.nextInt();
        input.nextLine();

        //Mengumpulkan data atribut dari proyek yang dicari
        boolean pencarian = false;
        String keywordPencarian = "";
        if(pilihanSearch == 1) {
            System.out.print(asciiColor(2) + "-> Masukkan Nama Proyek Yang Dicari: " + asciiColor(0));
            keywordPencarian = input.nextLine();
            pencarian = true;
        }else if(pilihanSearch == 2) {
            System.out.print(asciiColor(2) + "-> Masukkan Deadline Dari Proyek Yang Dicari: " + asciiColor(0));
            keywordPencarian = input.nextLine();
            pencarian = true;
        }else if(pilihanSearch == 3) {
            System.out.print(asciiColor(2) + "-> Masukkan Nama Pekerja Yang Dicari: " + asciiColor(0));
            keywordPencarian = input.nextLine();
            pencarian = true;
        }else if(pilihanSearch == 4) {
            System.out.print(asciiColor(2) + "-> Masukkan Jobdesk Dari Proyek Yang Dicari: " + asciiColor(0));
            keywordPencarian = input.nextLine();
            pencarian = true;
        }else if(pilihanSearch == 5) {
            System.out.println("-> Prioritas Proyek");
            System.out.println("   [1] Tinggi");
            System.out.println("   [2] Sedang");
            System.out.println("   [3] Rendah");
            System.out.print(asciiColor(2) + "-> Masukkan Prioritas Proyek Yang Dicari: " + asciiColor(0));
            int prioritasProyek = input.nextInt();
            if(prioritasProyek > 3 || prioritasProyek < 1) {
                System.out.println(asciiColor(1) + "(!) Pilihan Tidak Valid!" + asciiColor(0));
            } else {
                keywordPencarian = prioritasProyek == 1 ? "Tinggi": prioritasProyek == 2 ? "Sedang": "Rendah";
                pencarian = true;
            }
        }else {
            System.out.println(asciiColor(1) + "(!) Pilihan Tidak Valid!" + asciiColor(0));
        }

        boolean ditemukan = false;

        try {
            //Inisialisasi variabel dan library
            BufferedReader reader = new BufferedReader(new FileReader("ProjectData.txt"));
            String baris;

            boolean proyekDitemukan;
            boolean pekerjaDitemukan;
            boolean proyekPertama = true;

            if(pencarian) {
                //Membaca seluruh data di ProjectData.txt
                while((baris = reader.readLine()) != null) {
                    String data[] = baris.split(":");

                    //Memisahkan atribut setiap proyek
                    String namaProyek = data[0];
                    String anggaranProyek = data[1];
                    String deadlineProyek = data[2];
                    String dataPekerja[] = data[3].split(",");
                    String prioritasProyek = data[4];

                    //Jika atribut sesuai dengan keyword, maka proyek ditemukan
                    proyekDitemukan = namaProyek.contains(keywordPencarian) || deadlineProyek.contains(keywordPencarian) || prioritasProyek.contains(keywordPencarian);
                    pekerjaDitemukan = false;

                    //Memisah dataPekerja[] menjadi detailPekerja[]
                    for(int i = 0; i < dataPekerja.length; i++) {
                        String detailPekerja[] = dataPekerja[i].split("-");
                        String namaPekerja = detailPekerja[0];
                        String jobdeskPekerja = detailPekerja[1];
                        //Jika atribut sesuai dengan keyword, maka proyek ditemukan
                        if(namaPekerja.contains(keywordPencarian) || jobdeskPekerja.contains(keywordPencarian)) {
                            pekerjaDitemukan = true;
                            break;
                        }
                    }

                    if(proyekDitemukan || pekerjaDitemukan) {
                        //Agar terdapat garis --------- sebagai pembelah setiap proyek yang ditampilkan
                        if(!proyekPertama) {
                            System.out.println(asciiColor(3) + "__________________________________________________________________________________________________________________________________________________" + asciiColor(0));
                        }
                        proyekPertama = false;  

                        //Agar header format tabel hanya diprint satu kali
                        if(!ditemukan) {
                            System.out.println(asciiColor(3) + "__________________________________________________________________________________________________________________________________________________" + asciiColor(0));
                            System.out.println(asciiColor(3) + "|" + formatTabel("Nama Proyek", 30) + "|" + formatTabel("Anggaran Proyek", 25) + "|" + formatTabel("Deadline", 15) + "|" + formatTabel("Pekerja", 30) + "|" + formatTabel("Jobdesk", 30) + "|" + formatTabel("Prioritas", 10) + "|" + asciiColor(0));
                            System.out.println(asciiColor(3) + "__________________________________________________________________________________________________________________________________________________" + asciiColor(0));
                        }

                        //Cetak proyek yang ditemukan
                        for(int i = 0; i < dataPekerja.length; i++) {
                            String detailPekerja[] = dataPekerja[i].split("-");
                            String namaPekerja = detailPekerja[0];
                            String jobdeskPekerja = detailPekerja[1];
                            String statusJobdesk = detailPekerja[2];

                            if(statusJobdesk.equals("Belum")) {
                                System.out.println(asciiColor(3) + "|" + asciiColor(0) + formatTabel(namaProyek, 30) + asciiColor(3) + "|" + asciiColor(0) + formatTabel(anggaranProyek, 25) + asciiColor(3) + "|" + asciiColor(0) + formatTabel(deadlineProyek, 10) + asciiColor(3) + "|" + asciiColor(0) + formatTabel(namaPekerja, 25) + asciiColor(3) + "|" + asciiColor(0) + asciiColor(1) + formatTabel(jobdeskPekerja, 30) + asciiColor(0) + asciiColor(3) + "|" + asciiColor(0) + asciiColor(1) + formatTabel(statusJobdesk, 10) + asciiColor(0) + asciiColor(3) + "|" + asciiColor(0) + formatTabel(prioritasProyek, 10) + asciiColor(3) + "|" + asciiColor(0));
                            } else {
                                System.out.println(asciiColor(3) + "|" + asciiColor(0) + formatTabel(namaProyek, 30) + asciiColor(3) + "|" + asciiColor(0) + formatTabel(anggaranProyek, 25) + asciiColor(3) + "|" + asciiColor(0) + formatTabel(deadlineProyek, 10) + asciiColor(3) + "|" + asciiColor(0) + formatTabel(namaPekerja, 25) + asciiColor(3) + "|" + asciiColor(0) + asciiColor(2) + formatTabel(jobdeskPekerja, 30) + asciiColor(0) + asciiColor(3) + "|" + asciiColor(0) + asciiColor(2) + formatTabel(statusJobdesk, 10) + asciiColor(0) + asciiColor(3) + "|" + asciiColor(0) + formatTabel(prioritasProyek, 10) + asciiColor(3) + "|" + asciiColor(0));
                            }
                                    
                            namaProyek = ""; //Variabel namaProyek, dll direset menjadi kosong agar tabel kebawah tidak mengulang penulisan namaProyek, dll
                            anggaranProyek = "";
                            deadlineProyek = ""; 
                            prioritasProyek = "";

                        }

                        //Memastikan semua data sudah dicek, apabila sudah dan keyword pencarian tidak ditemukan, maka akan false
                        ditemukan = true;
                    }
                }

                //Memberikan notifikasi jika proyek tidak ditemukan
                if(!ditemukan) {
                    System.out.println(asciiColor(1) + "(!) Proyek Tidak Ditemukan!" + asciiColor(0));
                } else {
                    System.out.println(asciiColor(3) + "__________________________________________________________________________________________________________________________________________________" + asciiColor(0));
                }
            } 
        } catch (IOException e) {
            //Handling error library
            System.out.println(asciiColor(1) + "(!) Terjadi Kesalahan Saat Mencari Proyek!" + asciiColor(0));
        }
    }


    //===Function Dashboard Pekerja - Menampilkan Dashboard Untuk Akun Pekerja
    static void dashboardPekerja(String username) {
        Scanner input = new Scanner(System.in);
        ringkasanProyek();
        //Menampilkan select menu
        while (true) { 
            System.out.println("\n");
            System.out.println("[1] Lihat Proyek");
            System.out.println("[2] Cari Proyek");
            System.out.println("[3] Edit Jobdesk");
            System.out.println("[4] Keluar");
            System.out.print(asciiColor(2) + "-> Masukkan Pilihan: " + asciiColor(0));
            int pilihanMenu = input.nextInt();

            //Mengarahkan ke function terkait sesuai dengan pilihan fitur
            if(pilihanMenu == 1) {
                lihatProyek();
            } else if(pilihanMenu == 2) {
                cariProyek();
            } else if(pilihanMenu == 3) {
                editJobdesk(username);
            } else if(pilihanMenu == 4) {
                System.exit(pilihanMenu);
                break;
            } else {
                System.out.println(asciiColor(1) + "(!) Pilihan Tidak Valid!" + asciiColor(0));
            }
        }
        input.close();
    }

    //===Function Edit Jobdesk - Mengedit Status Jobdesk Pekerja (Sudah/Belum) (param username from user input)
    static void editJobdesk(String username) {
        Scanner input = new Scanner(System.in);

        lihatProyek();

        System.out.print(asciiColor(2) + "-> Masukkan Nama Proyek / Jobdesk Yang Ingin Diubah: " + asciiColor(0));
        String keyword = input.nextLine().trim().toLowerCase();

        String user = username.trim().toLowerCase();
        String semuaData = "";
        boolean diubah = false;

        try {
            //Inisialisasi variabel dan library
            BufferedReader reader = new BufferedReader(new FileReader("ProjectData.txt"));
            String baris;

            //Membaca seluruh data di ProjectData.txt
            while ((baris = reader.readLine()) != null) {
                String[] data = baris.split(":");
                if (data.length < 5) continue; // data rusak, skip

                String namaProyek = data[0];
                String anggaran = data[1];
                String deadline = data[2];
                String prioritas = data[4];

                String[] dataPekerja = data[3].split(",");
                String newDataPekerja = "";

                for (int i = 0; i < dataPekerja.length; i++) {
                    String[] detail = dataPekerja[i].split("-");
                    if (detail.length < 3) {
                        continue;
                    }

                    String namaPekerja = detail[0];
                    String jobdesk = detail[1];
                    String status = detail[2];

                    //Nama pekerja harus sesuai dengan username akun, sehingga pekerja tidak bisa mengedit status jobdesk milik pekerja lainnya.
                    boolean milikUser = namaPekerja.trim().toLowerCase().equals(user);
                    boolean cocokKeyword = namaProyek.toLowerCase().contains(keyword) || jobdesk.toLowerCase().contains(keyword);

                    if (milikUser && status.equalsIgnoreCase("Belum") && cocokKeyword) {
                        //Menampilkan proyek dan jobdesk yang akan diubah statusnya
                        System.out.println(asciiColor(3) + "Proyek  : " + asciiColor(0) + namaProyek);
                        System.out.println(asciiColor(3) + "Jobdesk : " + asciiColor(0) + jobdesk);
                        System.out.print(asciiColor(2) + "-> Tandai sebagai selesai? (ya/tidak): " + asciiColor(0));

                        String jawab = input.nextLine().trim().toLowerCase();
                        if (jawab.equals("ya")) {
                            status = "Sudah";
                            diubah = true;
                        }
                    }

                    newDataPekerja += namaPekerja + "-" + jobdesk + "-" + status;
                    if (i != dataPekerja.length - 1) {
                        newDataPekerja += ",";
                    }
                }

                //Membuat variabel baru hasil perubahan status
                semuaData += namaProyek + ":" + anggaran + ":" + deadline + ":" + newDataPekerja + ":" + prioritas + "\n";
            }

            reader.close();
            //Menulis ulang data di ProjectData.txt
            if (diubah) {
                FileWriter writer = new FileWriter("ProjectData.txt");
                writer.write(semuaData);
                writer.close();
                System.out.println(asciiColor(3) + "-> Jobdesk berhasil diperbarui!" + asciiColor(0));
            } else {
                System.out.println(asciiColor(1) + "(!) Tidak ada jobdesk yang diubah" + asciiColor(0));
            }

        } catch (IOException e) {
            //Handling error library
            System.out.println(asciiColor(1) + "(!) Terjadi kesalahan pada sistem!" + asciiColor(0));
        }
    }


    //===Main Function

    public static void main(String[] args) {
        //Inisialisasi variabel dan library
        Scanner input = new Scanner(System.in);
        boolean statusLogin = false;
        boolean statusManajer = false;

        //Onboarding Page (Halaman Pembuka)

        System.out.println(asciiColor(1) + "==================================================================================================================================================" + asciiColor(0));
        System.out.println(asciiColor(2) + "                                    ____            _           _         __  __                                   " + asciiColor(0));
        System.out.println(asciiColor(2) + "                                   |  _ \\ _ __ ___ (_) ___  ___| |_      |  \\/  | __ _ _ __   __ _  __ _  ___ _ __ " + asciiColor(0));
        System.out.println(asciiColor(2) + "                                   | |_) | '__/ _ \\| |/ _ \\/ __| __|     | |\\/| |/ _` | '_ \\ / _` |/ _` |/ _ \\ '__|" + asciiColor(0));
        System.out.println(asciiColor(2) + "                                   |  __/| | | (_) | |  __/ (__| |_      | |  | | (_| | | | | (_| | (_| |  __/ |   " + asciiColor(0));
        System.out.println(asciiColor(2) + "                                   |_|   |_|  \\___// |\\___|\\___|\\__|     |_|  |_|\\__,_|_| |_|\\__,_|\\__, |\\___|_|   " + asciiColor(0));
        System.out.println(asciiColor(2) + "                                                 |__/                                              |___/           " + asciiColor(0));
        System.out.println(asciiColor(1) + "==================================================================================================================================================" + asciiColor(0));


        System.out.println(asciiColor(3) + "                                                  Selamat Datang di Aplikasi Manajemen Proyek!" + asciiColor(0));

        //Select menu untuk onboarding page

        String username = "";

        while(true) {
            System.out.println("\n");
            System.out.println("[1] Login");
            System.out.println("[2] Register");
            System.out.println("[3] Exit");
            System.out.println(" ");
            //Mengumpulkan data
            System.out.print(asciiColor(2) + "-> Masukkan Pilihan: " + asciiColor(0));
            int pilihanMenu = input.nextInt();

            //Pilihan login
            if(pilihanMenu == 1) {
                System.out.print(asciiColor(2) + "-> Masukkan Username: " + asciiColor(0));
                username = input.next();
                System.out.print(asciiColor(2) + "-> Masukkan Password: " + asciiColor(0));
                String password = input.next();
                statusLogin = login(username, password);
                statusManajer = statusManajer(username, password);
                if(statusLogin){
                    break;
                }
            //Pilihan register
            }else if(pilihanMenu == 2) {
                System.out.print(asciiColor(2) + "-> Buat Username: " + asciiColor(0));
                String regUsername = input.next();
                System.out.print(asciiColor(2) + "-> Buat Password untuk akun " + regUsername + ": " + asciiColor(0));
                String regPassword = input.next();
                System.out.print(asciiColor(2) + "-> Status Akun (manajer/pekerja): " + asciiColor(0));
                String statusAkun = input.next();
                if(statusAkun.equals("manajer") || statusAkun.equals("pekerja")){
                    register(regUsername, regPassword, statusAkun);
                }
            //Pilihan keluar (exit)
            }else if(pilihanMenu == 3) {
                System.out.println(asciiColor(1) + "-> Aplikasi akan ditutup!" + asciiColor(0));
                System.exit(0);
                break;
            }else {
                System.out.println(asciiColor(1) + "-> Input Tidak Valid!" + asciiColor(0));
            }
        }
        
        //Fitur Manajer (jika statusLogin benar dan statusManajer benar (akun berstatus manajer))

        if(statusLogin && statusManajer) {
            System.out.println("\n");
            System.out.println(asciiColor(1) + "==================================================================================================================================================" + asciiColor(0));
            System.out.println(asciiColor(2) + "                                                 ____            _     _                         _ " + asciiColor(0));
            System.out.println(asciiColor(2) + "                                                |  _ \\  __ _ ___| |__ | |__   ___   __ _ _ __ __| |" + asciiColor(0));
            System.out.println(asciiColor(2) + "                                                | | | |/ _` / __| '_ \\| '_ \\ / _ \\ / _` | '__/ _` |" + asciiColor(0));
            System.out.println(asciiColor(2) + "                                                | |_| | (_| \\__ \\ | | | |_) | (_) | (_| | | | (_| |" + asciiColor(0));
            System.out.println(asciiColor(2) + "                                                |____/ \\__,_|___/_| |_|_.__/ \\___/ \\__,_|_|  \\__,_|" + asciiColor(0));
            System.out.println("                                                                   ");
            System.out.println(asciiColor(1) + "==================================================================================================================================================" + asciiColor(0));
            System.out.println(asciiColor(3) + "                                                    Selamat Datang di Dashboard Manajer " + username + "!" + asciiColor(0));

            //Memanggil function dashboard manajer
            dashboardManajer();
        }


        //Fitur Pekerja (jika statusLogin benar dan statusManajer salah (akun berstatus pekerja))
        if(statusLogin && !statusManajer) {
            System.out.println("\n");
            System.out.println(asciiColor(1) + "==================================================================================================================================================" + asciiColor(0));
            System.out.println(asciiColor(2) + "                                                 ____            _     _                         _ " + asciiColor(0));
            System.out.println(asciiColor(2) + "                                                |  _ \\  __ _ ___| |__ | |__   ___   __ _ _ __ __| |" + asciiColor(0));
            System.out.println(asciiColor(2) + "                                                | | | |/ _` / __| '_ \\| '_ \\ / _ \\ / _` | '__/ _` |" + asciiColor(0));
            System.out.println(asciiColor(2) + "                                                | |_| | (_| \\__ \\ | | | |_) | (_) | (_| | | | (_| |" + asciiColor(0));
            System.out.println(asciiColor(2) + "                                                |____/ \\__,_|___/_| |_|_.__/ \\___/ \\__,_|_|  \\__,_|" + asciiColor(0));
            System.out.println("                                                                   ");
            System.out.println(asciiColor(1) + "==================================================================================================================================================" + asciiColor(0));
            System.out.println(asciiColor(3) + "                                                    Selamat Datang di Dashboard Pekerja " + username + "!" + asciiColor(0));

            //Memanggil function dashboard pekerja
            dashboardPekerja(username);
        }
    }
}