package appolloni.migliano.cli;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneStrutture;
import appolloni.migliano.exception.CampiVuotiException;
import appolloni.migliano.exception.EntitaNonTrovata;

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
        System.out.println("       SEGNALA UNA NUOVA STRUTTURA      "); //NOSONAR
        System.out.println("========================================"); //NOSONAR
        System.out.println("Grazie " + studenteLoggato.getName() + " per il tuo contributo!"); //NOSONAR

        try {
            // 1. Dati Obbligatori
            System.out.print("Nome Struttura*: "); //NOSONAR
            String nome = scanner.nextLine().trim();

            System.out.print("Città*: "); //NOSONAR
            String citta = scanner.nextLine().trim();

            String tipoStruttura = selezionaOpzione("Tipo Struttura*:", new String[]{"Privata", "Pubblica"});

            // 2. Altri dettagli
            System.out.print("Indirizzo: "); //NOSONAR
            String indirizzo = scanner.nextLine().trim();

            System.out.print("Orario (es. 09:00 - 18:00): "); //NOSONAR
            String orario = scanner.nextLine().trim();

            System.out.print("Nome del Gestore (lascia vuoto se non lo sai): "); //NOSONAR
            String gestore = scanner.nextLine().trim();
            if (gestore.isEmpty()) gestore = "Sconosciuto";

            String tipoAttivita = selezionaOpzione("Tipo Attività:", new String[]{"Bar", "Università", "Biblioteca"});

            // 3. Servizi
            boolean wifi = chiediConferma("C'è il WiFi?");
            boolean ristorazione = chiediConferma("C'è un servizio ristorazione?");

            // 4. Creazione Bean e Configurazione (Coerente con la logica Host)
            // Usiamo il costruttore a 6 parametri come definito nel controller/factory
            BeanStruttura struttura = new BeanStruttura(tipoStruttura, nome, citta, indirizzo, wifi, ristorazione);
            
            // Impostiamo i campi aggiuntivi tramite setter
            struttura.setOrario(orario);
            struttura.setGestore(gestore);
            struttura.setTipoAttivita(tipoAttivita);
            struttura.setFoto("placeholder.png"); // Default obbligatorio per il DB

            // 5. Invio al Controller (Metodo corretto: creaStruttura)
            // Nota: qui passiamo studenteLoggato che è di tipo "Studente" (non "Host")
            // Il controller gestirà la differenza tramite bean.getTipo()
            controller.creaStruttura(studenteLoggato, struttura);

            System.out.println("\n✅ Grazie! La struttura è stata segnalata con successo."); //NOSONAR
            System.out.println("Premi Invio per tornare al Menu Principale..."); //NOSONAR
            scanner.nextLine();

        } catch (CampiVuotiException e) {
            System.err.println("\n⚠️ Errore: " + e.getMessage()); //NOSONAR
        } catch (SQLException e) {
            System.err.println("\n❌ Errore Database: " + e.getMessage()); //NOSONAR
        } catch (IOException e) {
            System.err.println("\n❌ Errore di salvataggio: " + e.getMessage()); //NOSONAR
        } catch (EntitaNonTrovata | IllegalArgumentException e) {
            System.err.println("\n❌ Errore validazione: " + e.getMessage()); //NOSONAR
        } catch (Exception e) {
            System.err.println("\n❌ Errore imprevisto: " + e.getMessage()); //NOSONAR
        }
    }

    private String selezionaOpzione(String titolo, String[] opzioni) {
        while (true) {
            System.out.println("\n" + titolo); //NOSONAR
            for (int i = 0; i < opzioni.length; i++) {
                System.out.println((i + 1) + ") " + opzioni[i]); //NOSONAR
            }
            System.out.print("Scelta (numero): "); //NOSONAR
            try {
                int scelta = Integer.parseInt(scanner.nextLine());
                if (scelta >= 1 && scelta <= opzioni.length) {
                    return opzioni[scelta - 1];
                }
            } catch (Exception e) { /* Continua il ciclo */ }
            System.out.println("Scelta non valida, riprova."); //NOSONAR
        }
    }

    private boolean chiediConferma(String domanda) {
        System.out.print(domanda + " (s/n): "); //NOSONAR
        String risp = scanner.nextLine().trim().toLowerCase();
        return risp.equals("s") || risp.equals("si");
    }

}
