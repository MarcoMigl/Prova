package appolloni.migliano.cli;


import java.util.Scanner;

import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneStrutture;


public class SegnalaStrutturaCLI {

    private final Scanner scanner;
    private final BeanUtenti studenteLoggato;
    private final ControllerGestioneStrutture controller;

    public SegnalaStrutturaCLI(BeanUtenti utente) {
        this.scanner = new Scanner(System.in);
        this.studenteLoggato = utente;
        this.controller = new ControllerGestioneStrutture();
    }

    public void start() {
        System.out.println("\n========================================"); //NOSONAR
        System.out.println("      SEGNALA UNA NUOVA STRUTTURA       "); //NOSONAR
        System.out.println("========================================"); //NOSONAR
        System.out.println("Studente: " + studenteLoggato.getName() + " " + studenteLoggato.getCognome()); //NOSONAR

        try {
            System.out.print("Nome Struttura da segnalare*: "); //NOSONAR
            String nome = scanner.nextLine().trim();

            System.out.print("Città*: "); //NOSONAR
            String citta = scanner.nextLine().trim();

            String tipoStruttura = selezionaOpzione("Tipo Struttura*:", new String[]{"Privata", "Pubblica"});

            System.out.print("Indirizzo: "); //NOSONAR
            String indirizzo = scanner.nextLine().trim();

             String orario = acquisisciOrario();

            System.out.print("Nome del Gestore (se lo conosci): "); //NOSONAR
            String gestore = scanner.nextLine().trim();
            if (gestore.isEmpty()) gestore = "Sconosciuto";

            String tipoAttivita = selezionaOpzione("Tipo Attività:", new String[]{"Bar", "Università", "Biblioteca"});

            boolean wifi = chiediConferma("C'è il WiFi?");
            boolean ristorazione = chiediConferma("C'è un servizio ristorazione?");

            // Creazione del Bean
            BeanStruttura struttura = new BeanStruttura(tipoStruttura, nome, citta, indirizzo, wifi, ristorazione);
            struttura.setOrario(orario);
            struttura.setGestore(gestore); // Nome testuale inserito dallo studente
            struttura.setTipoAttivita(tipoAttivita);
            struttura.setFoto("Foto non disponibili in versione CLI. ");

            
            // Il controller vedrà che bean.getTipo() è "Studente" e NON sovrascriverà nulla se esiste già.
            controller.creaStruttura(studenteLoggato, struttura);

            System.out.println("\n Grazie! La tua segnalazione è stata salvata."); //NOSONAR
            System.out.println("Premi Invio per tornare al menu..."); //NOSONAR
            scanner.nextLine();

        } catch (IllegalArgumentException e) {
            // Qui gestiamo il caso in cui la struttura esiste già (già segnalata o già di un Host)
            System.err.println("\n Attenzione: " + e.getMessage()); //NOSONAR
            System.out.println("Questa struttura è già presente nel nostro database."); //NOSONAR
        } catch (Exception e) {
            System.err.println("\n Errore durante la segnalazione: " + e.getMessage()); //NOSONAR
        }
    }

    private String selezionaOpzione(String titolo, String[] opzioni) {
        while (true) {
            System.out.println("\n" + titolo); //NOSONAR
            for (int i = 0; i < opzioni.length; i++) {
                System.out.println((i + 1) + ") " + opzioni[i]); //NOSONAR
            }
            System.out.print("Scelta: "); //NOSONAR
            try {
                int scelta = Integer.parseInt(scanner.nextLine());
                if (scelta >= 1 && scelta <= opzioni.length) return opzioni[scelta - 1];
            } catch (Exception e) {
            System.out.println("Riprova."); //NOSONAR
            }
        }
    }


     private String acquisisciOrario() {
        String regex = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]-([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";
        String input;
        while (true) {
            System.out.print("Orario apertura (es. 08:00-20:00): "); //NOSONAR
            input = scanner.nextLine().trim();
            
            if (input.matches(regex)) {
                String[] parti = input.split("-");
                String[] inizio = parti[0].split(":");
                String[] fine = parti[1].split(":");
                
                int minutiInizio = Integer.parseInt(inizio[0]) * 60 + Integer.parseInt(inizio[1]);
                int minutiFine = Integer.parseInt(fine[0]) * 60 + Integer.parseInt(fine[1]);
                
                if (minutiFine > minutiInizio) {
                    return input;
                } else {
                    System.out.println("[ERRORE] L'orario di chiusura deve essere successivo a quello di apertura."); //NOSONAR
                }
            } else {
                System.out.println("[ERRORE] Formato non valido. Usa HH:mm-HH:mm (es. 09:00-18:00)."); //NOSONAR
            }
        }
    }

    private boolean chiediConferma(String domanda) {
        System.out.print(domanda + " (s/n): "); //NOSONAR
        String risp = scanner.nextLine().trim().toLowerCase();
        return risp.equals("s") || risp.equals("si");
    }
}