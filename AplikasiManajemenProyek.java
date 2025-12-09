import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class AplikasiManajemenProyek{

    public static void login(String usn, String pass){
        
    }

    public static void register(String usn, String pass){
        
        try {
            FileWriter fw = new FileWriter("UserData.txt");
            fw.write(usn + ":" + pass);
            fw.close();
            System.out.println("Akun Berhasil Dibuat!");
        } catch (IOException e) {
            System.out.println("Terjadi Error Saat Penulisan File!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //Inisialisasi 
        Scanner input = new Scanner(System.in);

        

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
            System.out.print("-> Masukkan Password");
            String password = input.next();
            login(username, password);
        }else if(pilihanMenu == 2){
            System.out.print("-> Buat Username: ");
            String regUsername = input.next();
            System.out.print("-> Buat Password untuk akun " + regUsername + ": ");
            String regPassword = input.next();
            register(regUsername, regPassword);
        }else if(pilihanMenu == 3){

        }else if(pilihanMenu == 4){
            System.out.println("-> Aplikasi akan ditutup!");
            System.exit(0);
        }else{
            System.out.println("-> Input Tidak Valid!");
        }


    }
}

