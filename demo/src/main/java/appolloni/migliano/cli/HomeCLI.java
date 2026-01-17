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
            System.out.println("\n========================================"); //NOSONAR
            System.out.println("        BENVENUTO IN STUDIO-APP          "); //NOSONAR
            System.out.println("========================================"); //NOSONAR
            System.out.println("1. Accedi (Login)"); //NOSONAR
            System.out.println("2. Registrati (Crea nuovo utente)"); //NOSONAR
            System.out.println("3. Esci"); //NOSONAR
            System.out.print("Scegli un'opzione: "); //NOSONAR

            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1" -> vaiALogin();
                case "2" -> vaiARegistrazione();
                case "3" -> {
                    System.out.println("Chiusura applicazione. Arrivederci!"); //NOSONAR
                    exit = true;
                }
                default -> System.out.println(" Scelta non valida, riprova."); //NOSONAR
            }
        }
    }

    private void vaiALogin() {
        // Avvio del modulo di Login
        System.out.println("\n--- Apertura Login ---"); //NOSONAR
        LoginCLI loginView = new LoginCLI();
        loginView.start();
        
        
    }

    private void vaiARegistrazione() {
        // Avvio del modulo di Registrazione
        System.out.println("\n--- Apertura Registrazione ---"); //NOSONAR
        CreazioneUtenteCLI registrazioneView = new CreazioneUtenteCLI();
        registrazioneView.start();
    }

}