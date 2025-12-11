import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class AplikasiManajemenProyek{

    //Function Login Akun
    public static boolean login(String usn, String pass) {
        boolean login = false;
        boolean akunDitemukan = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("UserData.txt"));
            String baris;

            while((baris = reader.readLine()) != null) {
                String dataAkun[] = baris.split(":");

                if(dataAkun.length >= 2) {
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

    //Function Register Akun
    public static void register(String usn, String pass, String statAcc) {
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
                FileWriter writer = new FileWriter("UserData.txt", true); //append mode agar file tidak dioverwrite tetapi ditambahkan
                writer.write(usn + ":" + pass + ":" + statAcc + System.lineSeparator()); //agar data yang baru tertulis pada new line, bukan disebelah data lama
                writer.close();
                System.out.println("(!) Akun Berhasil Dibuat!");
            }
        } catch (IOException e) {
            System.out.println("(!) Terjadi Kesalahan Pada Sistem!");
        }
    }

    //Function Menentukan Status Akun (admin/pekerja)
    public static boolean statusAdmin(String usn, String pass) {
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


    //Main Function
    public static void main(String[] args) {
        //Inisialisasi 

        Scanner input = new Scanner(System.in);
        boolean statusLogin = false;
        boolean statusAdmin = false;

        //Onboarding Page (Halaman Pembuka)

        String asciiColorMerah = "\u001B[31m";
        String asciiColorHijau = "\u001B[32m";
        String asciiColorBiru = "\u001B[34m";
        String asciiColorReset = "\u001B[0m";
        System.out.println(asciiColorHijau + "============================================================================" + asciiColorReset);
        System.out.println(asciiColorMerah + "______          _           _    ___  ___" + asciiColorReset);
        System.out.println(asciiColorMerah + "| ___ \\        (_)         | |   |  \\/  |" + asciiColorReset);
        System.out.println(asciiColorMerah + "| |_/ / __ ___  _  ___  ___| |_  | .  . | __ _ _ __   __ _  __ _  ___ _ __" + asciiColorReset);
        System.out.println(asciiColorMerah + "|  __/ '__/ _ \\| |/ _ \\/ __| __| | |\\/| |/ _` | '_ \\ / _` |/ _` |/ _ \\ '__|" + asciiColorReset);
        System.out.println(asciiColorMerah + "| |  | | | (_) | |  __/ (__| |_  | |  | | (_| | | | | (_| | (_| |  __/ |" + asciiColorReset);
        System.out.println(asciiColorMerah + "\\_|  |_|  \\___/| |\\___|\\___|\\__| \\_|  |_/\\__,_|_| |_|\\__,_|\\__, |\\___|_|" + asciiColorReset);
        System.out.println(asciiColorMerah + "              _/ |                                          __/ |" + asciiColorReset);
        System.out.println(asciiColorMerah + "             |__/                                          |___/" + asciiColorReset);
        System.out.println(asciiColorHijau + "============================================================================" + asciiColorReset);

        System.out.println(asciiColorBiru + "               Selamat Datang di Aplikasi Manajemen Proyek!" + asciiColorReset);

        //Select Menu

        while(true){
            System.out.println("\n");
            System.out.println("[1] Login");
            System.out.println("[2] Register");
            System.out.println("[3] About");
            System.out.println("[4] Exit");
            System.out.println(" ");
            System.out.print("Masukkan Pilihan: ");
            int pilihanMenu = input.nextInt();
            if(pilihanMenu == 1){
                System.out.print("-> Masukkan Username: ");
                String username = input.next();
                System.out.print("-> Masukkan Password: ");
                String password = input.next();
                statusLogin = login(username, password);
                statusAdmin = statusAdmin(username, password);
                if(statusLogin){
                    break;
                }
            }else if(pilihanMenu == 2){
                System.out.print("-> Buat Username: ");
                String regUsername = input.next();
                System.out.print("-> Buat Password untuk akun " + regUsername + ": ");
                String regPassword = input.next();
                System.out.print("-> Status Akun (admin/pekerja): ");
                String statusAkun = input.next();
                if(statusAkun.equals("admin") || statusAkun.equals("pekerja")){
                    register(regUsername, regPassword, statusAkun);
                }
            }else if(pilihanMenu == 3){

            }else if(pilihanMenu == 4){
                System.out.println("-> Aplikasi akan ditutup!");
                System.exit(0);
                break;
            }else{
                System.out.println("-> Input Tidak Valid!");
            }
        }
        
        //Fitur Admin

        if(statusLogin && statusAdmin){
            System.out.println("page admin");
        }


        //Fitur Pekerja

        if(statusLogin && !statusAdmin){
            System.out.println("page pekerja");
        }
        
    }
}

