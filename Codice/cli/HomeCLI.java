package appolloni.migliano.cli;

import java.util.Scanner;

public class HomeCLI {

    private final Scanner scanner;

    public HomeCLI() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n========================================");
            System.out.println("        BENVENUTO IN STUDIO-APP          ");
            System.out.println("========================================");
            System.out.println("1. Accedi (Login)");
            System.out.println("2. Registrati (Crea nuovo utente)");
            System.out.println("3. Esci");
            System.out.print("Scegli un'opzione: ");

            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1" -> vaiALogin();
                case "2" -> vaiARegistrazione();
                case "3" -> {
                    System.out.println("Chiusura applicazione. Arrivederci!");
                    exit = true;
                }
                default -> System.out.println(" Scelta non valida, riprova.");
            }
        }
    }

    private void vaiALogin() {
        // Avvio del modulo di Login
        System.out.println("\n--- Apertura Login ---");
        LoginCLI loginView = new LoginCLI();
        loginView.start();
        
        /* Nota: Quando l'utente farà 'Logout' dal MenuPrincipale, 
         il controllo tornerà qui automaticamente permettendogli 
         di loggarsi con un altro account o uscire.
        */
    }

    private void vaiARegistrazione() {
        // Avvio del modulo di Registrazione
        System.out.println("\n--- Apertura Registrazione ---");
        CreazioneUtenteCLI registrazioneView = new CreazioneUtenteCLI();
        registrazioneView.start();
    }
}
