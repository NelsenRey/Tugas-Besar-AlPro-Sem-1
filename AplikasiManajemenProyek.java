import java.util.Scanner;

public class AplikasiManajemenProyek{
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        

        //Onboarding Page (Halaman Pembuka)

        String asciiColorMerah = "\u001B[31m";
        String asciiColorHijau = "\u001B[32m";
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

        System.out.println("Selamat Datang di Aplikasi Manajemen Proyek! ");
    }
}

