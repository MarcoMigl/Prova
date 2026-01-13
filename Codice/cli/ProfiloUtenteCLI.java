package appolloni.migliano.cli;

import java.util.Scanner;

import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerCreazioneUtente;
import appolloni.migliano.HelperErrori;

public class ProfiloUtenteCLI {

    private final ControllerCreazioneUtente controller;
    private final Scanner scanner;
    private final BeanUtenti utenteLoggato;

    public ProfiloUtenteCLI(BeanUtenti utente) {
        this.controller = new ControllerCreazioneUtente();
        this.scanner = new Scanner(System.in);
        this.utenteLoggato = utente;
    }

    public void start() {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- GESTIONE PROFILO ---");
            System.out.println("1. Visualizza le mie informazioni");
            System.out.println("2. Cambia Password");
            System.out.println("3. Torna al menu principale");
            System.out.print("Scelta: ");

            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1" -> mostraInfoUI();
                case "2" -> cambiaPasswordUI();
                case "3" -> back = true;
                default -> System.out.println("Scelta non valida.");
            }
        }
    }

    private void mostraInfoUI() {
        try {
            // Usiamo l'email dell'utente già loggato
            BeanUtenti risultato = controller.recuperaInformazioniUtenti(utenteLoggato);
            
            System.out.println("\n--- DETTAGLI PROFILO ---");
            System.out.println("Nome:    " + risultato.getName());
            System.out.println("Cognome: " + risultato.getCognome());
            System.out.println("Email:   " + risultato.getEmail());
            System.out.println("Città:   " + risultato.getCitta());
        } catch (Exception e) {
            HelperErrori.errore("Errore nel recupero dati: ", e.getMessage());
        }
    }

    private void cambiaPasswordUI() {
        try {
            System.out.print("Inserisci la vecchia password: ");
            String vecchia = scanner.nextLine();
            
            System.out.print("Inserisci la nuova password: ");
            String nuova = scanner.nextLine();

            boolean successo = controller.modificaPassword(vecchia, nuova, utenteLoggato);
            
            if (successo) {
                System.out.println("Password aggiornata con successo!");
            } else {
                System.err.println("Errore: La password attuale inserita non è corretta.");
            }
        } catch (Exception e) {
            System.err.println("Errore durante l'operazione: " + e.getMessage());
        }
    }
}
