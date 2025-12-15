import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.time.LocalDate;

public class AplikasiManajemenProyek{

    //===Function Login Akun (param = username from username input, password from password input)

    static boolean login(String usn, String pass) {
        boolean login = false;
        boolean akunDitemukan = false;
        try {
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
            System.out.println("(!) Terjadi Error Pada Sistem!");
        }

        if(akunDitemukan) {
            login = true;
            System.out.println("-> Login Berhasil!");
        }else {
            login = false;
            System.out.println("(!) Username atau Password Salah!");
        }
        return login;
    }

    //===Function Register Akun

    static void register(String usn, String pass, String statAcc) {
        try {
            boolean usernameDigunakan = false;
            BufferedReader reader = new BufferedReader(new FileReader("UserData.txt"));
            String baris;
            while((baris = reader.readLine()) != null) {
                String dataAkun[] = baris.split(":");
                String username = dataAkun[0];
                if(username.equals(usn)) {
                    usernameDigunakan = true;
                }
            }
            reader.close();
            
            if(usernameDigunakan) {
                System.out.println("(!) Akun Sudah Ada!");
            } else {
                FileWriter writer = new FileWriter("UserData.txt", true); //Append mode agar file tidak dioverwrite tetapi ditambahkan
                writer.write(usn + ":" + pass + ":" + statAcc + System.lineSeparator()); //Agar data yang baru tertulis pada new line, bukan disebelah data lama
                writer.close();
                System.out.println("(!) Akun Berhasil Dibuat!");
            }
        } catch (IOException e) {
            System.out.println("(!) Terjadi Kesalahan Pada Sistem!");
        }
    }

    //===Function Menentukan Status Akun (admin/pekerja)

    static boolean statusAdmin(String usn, String pass) {
        boolean statusAdmin = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("UserData.txt"));
            String baris;
            while((baris = reader.readLine()) != null) {
                String data[] = baris.split(":");
                String username = data[0];
                String password = data[1];
                if(username.equals(usn) && password.equals(pass)) {
                    String statusAkun = data[2];
                    if(statusAkun.equals("admin")) {
                        statusAdmin = true;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("(!) Terjadi Error Saat Verifikasi Status Akun!");
        }
        return statusAdmin;
    }

    //===Function Dashboard Admin

    static void dashboardAdmin() {
        Scanner input = new Scanner(System.in);

        while (true) { 
            System.out.println("\n");
            System.out.println("[1] Lihat Proyek");
            System.out.println("[2] Tambah Proyek");
            System.out.println("[3] Edit Proyek");
            System.out.println("[4] Hapus Proyek");
            System.out.println("[5] Keluar");
            System.out.print("-> Masukkan Pilihan: ");
            int pilihanMenuAdmin = input.nextInt();

            if(pilihanMenuAdmin == 1) {

            } else if(pilihanMenuAdmin == 2) {
                tambahProyek();
            } else if(pilihanMenuAdmin == 3) {

            } else if(pilihanMenuAdmin == 4) {

            } else if(pilihanMenuAdmin == 5) {
                System.exit(pilihanMenuAdmin);
                break;
            } else {
                System.out.println("(!) Pilihan Tidak Valid!");
            }
        }
        input.close();
    }

    //=== Function Lihat Proyek (Admin)

    static void lihatProyek() {

    }

    //=== Function Tambah Proyek (Admin)

    static void tambahProyek() {
        Scanner input = new Scanner(System.in);

        System.out.print("-> Masukkan Nama Proyek: ");
        String namaProyek = input.nextLine();
        System.out.print("-> Masukkan Total Anggaran Proyek: ");
        int anggaranProyek = input.nextInt();
        input.nextLine();
        System.out.print("-> Masukkan Deadline (Tenggat) Proyek (YYYY-MM-DD): ");
        String inputDeadlineProyek = input.next();
        input.nextLine();
        LocalDate deadlineProyek = LocalDate.parse(inputDeadlineProyek);
        
        String pekerjaProyek[] = new String[20];
        String jobdeskPekerja[] = new String[20];
        int i = 0;
        while (true) { 
            System.out.print("-> Masukkan Pekerja (Masukkan 'Berhenti' Untuk Berhenti): ");
            pekerjaProyek[i] = input.nextLine();
            if(pekerjaProyek[i].equals("Berhenti")) {
                break;
            }
            System.out.print("-> Masukkan Jobdesk " + pekerjaProyek[i] + ": ");
            jobdeskPekerja[i] = input.nextLine();
            i++;
        }

        //---Gabungkan Array pekerjaProyek[] dan jobdeskPekerja[] agar mudah dibaca dan diwrite

        String dataPekerja = "";
        for(int j = 0; j < i; j++) {
            //---Char - berperan sebagai separator antara nama pekerja dan jobdesknya
            dataPekerja += pekerjaProyek[j] + "-" + jobdeskPekerja[j];
            if(j < (i-1)) {
                //---Char , berperan sebagai separator antara setiap dataPekerja
                dataPekerja += ",";
            }
        }

        try {
            FileWriter writer = new FileWriter("ProjectData.txt", true);
            writer.write(namaProyek + ":" + anggaranProyek + ":" + deadlineProyek + ":" + dataPekerja + System.lineSeparator());
            writer.close();
            System.out.println("-> Proyek Berhasil Ditambahkan!");
        } catch (IOException e) {
            System.out.println("(!) Terjadi Error Saat Penambahan Proyek!");
        }

    }


    //===Function Dashboard Pekerja



    //===Main Function

    public static void main(String[] args) {
        //Inisialisasi 

        Scanner input = new Scanner(System.in);
        boolean statusLogin = false;
        boolean statusAdmin = false;

        //---Onboarding Page (Halaman Pembuka)

        String asciiColorMerah = "\u001B[31m";
        String asciiColorHijau = "\u001B[32m";
        String asciiColorBiru = "\u001B[34m";
        String asciiColorReset = "\u001B[0m";
        System.out.println(asciiColorMerah + "=================================================================================" + asciiColorReset);
        System.out.println(asciiColorHijau + "  ____            _           _         __  __                                   " + asciiColorReset);
        System.out.println(asciiColorHijau + " |  _ \\ _ __ ___ (_) ___  ___| |_      |  \\/  | __ _ _ __   __ _  __ _  ___ _ __ " + asciiColorReset);
        System.out.println(asciiColorHijau + " | |_) | '__/ _ \\| |/ _ \\/ __| __|     | |\\/| |/ _` | '_ \\ / _` |/ _` |/ _ \\ '__|" + asciiColorReset);
        System.out.println(asciiColorHijau + " |  __/| | | (_) | |  __/ (__| |_      | |  | | (_| | | | | (_| | (_| |  __/ |   " + asciiColorReset);
        System.out.println(asciiColorHijau + " |_|   |_|  \\___// |\\___|\\___|\\__|     |_|  |_|\\__,_|_| |_|\\__,_|\\__, |\\___|_|   " + asciiColorReset);
        System.out.println(asciiColorHijau + "               |__/                                              |___/           " + asciiColorReset);
        System.out.println(asciiColorMerah + "=================================================================================" + asciiColorReset);


        System.out.println(asciiColorBiru + "                   Selamat Datang di Aplikasi Manajemen Proyek!" + asciiColorReset);

        //Select Menu

        String username = "";

        while(true) {
            System.out.println("\n");
            System.out.println("[1] Login");
            System.out.println("[2] Register");
            System.out.println("[3] About");
            System.out.println("[4] Exit");
            System.out.println(" ");
            System.out.print("Masukkan Pilihan: ");
            int pilihanMenu = input.nextInt();
            if(pilihanMenu == 1) {
                System.out.print("-> Masukkan Username: ");
                username = input.next();
                System.out.print("-> Masukkan Password: ");
                String password = input.next();
                statusLogin = login(username, password);
                statusAdmin = statusAdmin(username, password);
                if(statusLogin){
                    break;
                }
            }else if(pilihanMenu == 2) {
                System.out.print("-> Buat Username: ");
                String regUsername = input.next();
                System.out.print("-> Buat Password untuk akun " + regUsername + ": ");
                String regPassword = input.next();
                System.out.print("-> Status Akun (admin/pekerja): ");
                String statusAkun = input.next();
                if(statusAkun.equals("admin") || statusAkun.equals("pekerja")){
                    register(regUsername, regPassword, statusAkun);
                }
            }else if(pilihanMenu == 3) {

            }else if(pilihanMenu == 4) {
                System.out.println("-> Aplikasi akan ditutup!");
                System.exit(0);
                break;
            }else {
                System.out.println("-> Input Tidak Valid!");
            }
        }
        
        //---Fitur Admin

        if(statusLogin && statusAdmin) {
            System.out.println("\n");
            System.out.println(asciiColorMerah + "=================================================================================" + asciiColorReset);
            System.out.println(asciiColorHijau + "                 ____            _     _                         _ " + asciiColorReset);
            System.out.println(asciiColorHijau + "                |  _ \\  __ _ ___| |__ | |__   ___   __ _ _ __ __| |" + asciiColorReset);
            System.out.println(asciiColorHijau + "                | | | |/ _` / __| '_ \\| '_ \\ / _ \\ / _` | '__/ _` |" + asciiColorReset);
            System.out.println(asciiColorHijau + "                | |_| | (_| \\__ \\ | | | |_) | (_) | (_| | | | (_| |" + asciiColorReset);
            System.out.println(asciiColorHijau + "                |____/ \\__,_|___/_| |_|_.__/ \\___/ \\__,_|_|  \\__,_|" + asciiColorReset);
            System.out.println("                                                                   ");
            System.out.println(asciiColorMerah + "=================================================================================" + asciiColorReset);
            System.out.println(asciiColorBiru + "                   Selamat Datang di Dashboard Admin " + username + "!" + asciiColorReset);

            //---Dashboard Admin

            dashboardAdmin();
        }


        //---Fitur Pekerja

        if(statusLogin && !statusAdmin) {
            System.out.println("\n");
            System.out.println(asciiColorMerah + "=================================================================================" + asciiColorReset);
            System.out.println(asciiColorHijau + "                 ____            _     _                         _ " + asciiColorReset);
            System.out.println(asciiColorHijau + "                |  _ \\  __ _ ___| |__ | |__   ___   __ _ _ __ __| |" + asciiColorReset);
            System.out.println(asciiColorHijau + "                | | | |/ _` / __| '_ \\| '_ \\ / _ \\ / _` | '__/ _` |" + asciiColorReset);
            System.out.println(asciiColorHijau + "                | |_| | (_| \\__ \\ | | | |_) | (_) | (_| | | | (_| |" + asciiColorReset);
            System.out.println(asciiColorHijau + "                |____/ \\__,_|___/_| |_|_.__/ \\___/ \\__,_|_|  \\__,_|" + asciiColorReset);
            System.out.println("                                                                   ");
            System.out.println(asciiColorMerah + "=================================================================================" + asciiColorReset);
            System.out.println(asciiColorBiru + "                   Selamat Datang di Dashboard Pekerja " + username + "!" + asciiColorReset);

            //---Dashboard Pekerja

            //---Select Menu Pekerja
        }
        
    }
}