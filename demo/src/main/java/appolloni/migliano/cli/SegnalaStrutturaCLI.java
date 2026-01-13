package appolloni.migliano.cli;

import java.util.Scanner;

import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerCreazioneStrutture;

public class SegnalaStrutturaCLI {

    private final Scanner scanner;
    private final BeanUtenti studenteLoggato;
    private final ControllerCreazioneStrutture controller;

    public SegnalaStrutturaCLI(BeanUtenti utente) {
        this.scanner = new Scanner(System.in);
        this.studenteLoggato = utente;
        this.controller = new ControllerCreazioneStrutture();
    }

    public void start() {
        System.out.println("\n========================================");
        System.out.println("       SEGNALA UNA NUOVA STRUTTURA      ");
        System.out.println("========================================");
        System.out.println("Grazie " + studenteLoggato.getName() + " per il tuo contributo!");

        try {
            // 1. Dati Obbligatori
            System.out.print("Nome Struttura*: ");
            String nome = scanner.nextLine().trim();

            System.out.print("Città*: ");
            String citta = scanner.nextLine().trim();

            String tipoStruttura = selezionaOpzione("Tipo Struttura*:", new String[]{"Privata", "Pubblica"});

            // Validazione immediata
            if (nome.isEmpty() || citta.isEmpty() || tipoStruttura == null) {
                System.err.println("\n Errore: Compila i campi obbligatori (Nome, Città, Tipo)!");
                return;
            }

            // 2. Dati Facoltativi / Dettagli
            System.out.print("Indirizzo: ");
            String indirizzo = scanner.nextLine().trim();

            System.out.print("Orario (es. 09:00 - 18:00): ");
            String orario = scanner.nextLine().trim();

            System.out.print("Nome del Gestore (lascia vuoto se non lo sai): ");
            String gestore = scanner.nextLine().trim();
            if (gestore.isEmpty()) gestore = "Sconosciuto";

            String tipoAttivita = selezionaOpzione("Tipo Attività:", new String[]{"Bar", "Università", "Biblioteca"});

            // 3. Servizi
            boolean wifi = chiediConferma("C'è il WiFi?");
            boolean ristorazione = chiediConferma("C'è un servizio ristorazione?");

            // 4. Creazione Bean e invio
            BeanStruttura struttura = new BeanStruttura(
                tipoStruttura, nome, citta, indirizzo, orario, 
                wifi, ristorazione, tipoAttivita, gestore
            );

            controller.creaStruttura(studenteLoggato, struttura);

            System.out.println("\n Grazie! La struttura è stata segnalata con successo.");
            System.out.println("Premi Invio per tornare al Menu Principale...");
            scanner.nextLine();

        } catch (Exception e) {
            System.err.println("\n Errore durante la segnalazione: " + e.getMessage());
        }
    }

    private String selezionaOpzione(String titolo, String[] opzioni) {
        while (true) {
            System.out.println(titolo);
            for (int i = 0; i < opzioni.length; i++) {
                System.out.println((i + 1) + ") " + opzioni[i]);
            }
            System.out.print("Scelta: ");
            try {
                int scelta = Integer.parseInt(scanner.nextLine());
                if (scelta >= 1 && scelta <= opzioni.length) {
                    return opzioni[scelta - 1];
                }
            } catch (Exception e) { /* Ripete il ciclo */ }
            System.out.println("Scelta non valida.");
        }
    }

    private boolean chiediConferma(String domanda) {
        System.out.print(domanda + " (s/n): ");
        String risp = scanner.nextLine().trim().toLowerCase();
        return risp.equals("s") || risp.equals("si");
    }
}
