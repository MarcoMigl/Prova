package appolloni.migliano.cli;

import java.util.Scanner;

import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerLogin;

public class LoginCLI {

    private final ControllerLogin controller;
    private final Scanner scanner;

    public LoginCLI() {
        this.controller = new ControllerLogin();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("\n--- LOGIN UTENTE ---");
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        // Controllo campi vuoti
        if (email.isEmpty() || password.isEmpty()) {
            System.err.println("Errore: informazioni mancanti!");
            start();
            return;
        }

        // 1. Prepariamo il bean con i dati inseriti
        BeanUtenti beanLogin = new BeanUtenti(null, null, null, null, null, null);
        beanLogin.setEmail(email);
        beanLogin.setPassword(password);

        try {
            // 2. Chiamata al controller per la verifica
            BeanUtenti utenteLoggato = controller.verificaUtente(beanLogin);
            
            if (utenteLoggato != null) {
                System.out.println("\nLogin effettuato! Benvenuto " + utenteLoggato.getName());
                
                // 3. Reindirizzamento differenziato in base al tipo
                reindirizzaUtente(utenteLoggato);
            }
            
        } catch (Exception e) {
            System.err.println("\n Errore di accesso: " + e.getMessage());
            System.out.println("Riprova oppure scrivi 'esci' per terminare.");
            if (!scanner.nextLine().equalsIgnoreCase("esci")) {
                start();
            }
        }
    }

    private void reindirizzaUtente(BeanUtenti utente) {
        
        if ("Studente".equalsIgnoreCase(utente.getTipo())) {
            System.out.println("[Sistema] Caricamento Menu Studente...");
            MenuPrincipaleCLI menuStudente = new MenuPrincipaleCLI(utente);
            menuStudente.start();
        } else {
            System.out.println("[Sistema] Caricamento Menu Host...");
            HostMenuCLI menuHost = new HostMenuCLI(utente);
            menuHost.start();
            
        }
    }
}
