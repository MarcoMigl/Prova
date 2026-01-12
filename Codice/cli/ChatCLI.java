package appolloni.migliano.cli;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import appolloni.migliano.bean.BeanGruppo;
import appolloni.migliano.bean.BeanMessaggi;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerChat;

public class ChatCLI {
    private final ControllerChat controller;
    private final Scanner scanner;
    private final BeanUtenti utenteLoggato;
    private final BeanGruppo gruppoCorrente;

    public ChatCLI(BeanUtenti utente, BeanGruppo gruppo) {
        this.controller = new ControllerChat();
        this.scanner = new Scanner(System.in);
        this.utenteLoggato = utente;
        this.gruppoCorrente = gruppo;
    }

    public void start() {
        boolean exit = false;
        while (!exit) {
            try { 
                System.out.println("\n--- CHAT: " + gruppoCorrente.getNome() + " ---");
                
                
                mostraMessaggi(); 
                
                System.out.println("\n[1] Invia Messaggio | [2] Refresh | [3] Abbandona Gruppo | [4] Esci");
                System.out.print("Scelta: ");
                String scelta = scanner.nextLine();

                switch (scelta) {
                    case "1" -> inviaMessaggioUI();
                    case "2" -> {} 
                    case "3" -> {
                        abbandonaGruppoUI();
                        exit = true;
                    }
                    case "4" -> exit = true;
                    default -> System.out.println("Scelta non valida.");
                }
            } catch (SQLException e) {
                System.err.println("Errore database: " + e.getMessage());
                // Se c'è un errore grave al database, per uscire
                // exit = true; 
            }
        }
    }

    private void mostraMessaggi() throws SQLException {
        try {
            List<BeanMessaggi> messaggi = controller.recuperaMessaggi(gruppoCorrente);
        if (messaggi.isEmpty()) {
            System.out.println("(Nessun messaggio presente)");
        } else {
            for (BeanMessaggi m : messaggi) {
                System.out.println("[" + m.getMittente() + "]: " + m.getMess());
            }
        }
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    private void inviaMessaggioUI() throws SQLException {
        System.out.print("Scrivi messaggio: ");
        String testo = scanner.nextLine();
       try {
           controller.inviaMessaggio(utenteLoggato, gruppoCorrente, testo);
       } catch (Exception e) {
        e.printStackTrace();
       } 
    }

    private void abbandonaGruppoUI() throws SQLException {
        System.out.print("Sei sicuro di voler lasciare/eliminare il gruppo? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
           try {
               controller.abbandonaGruppo(utenteLoggato, gruppoCorrente);
           } catch (Exception e) {
            e.printStackTrace();
           } 
            System.out.println("Gruppo abbandonato.");
        }
    }
}
