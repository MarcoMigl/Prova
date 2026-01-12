package appolloni.migliano.cli;

import java.util.Scanner;

import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerCreazioneStrutture;

public class CreazioneStruttureCLI {

    private final ControllerCreazioneStrutture controller;
    private final Scanner scanner;
    private final BeanUtenti utenteCorrente;

    public CreazioneStruttureCLI(BeanUtenti bean) {
        this.controller = new ControllerCreazioneStrutture();
        this.scanner = new Scanner(System.in);
        this.utenteCorrente = bean;
    }

    public void start() {
        System.out.println("\n========================================");
        System.out.println("   REGISTRAZIONE DELLA TUA STRUTTURA    ");
        System.out.println("========================================");
        System.out.println("Benvenuto " + utenteCorrente.getName() + ", inserisci i dati del locale.");

        try {
            // 1. Dati testuali
            System.out.print("Nome Struttura: ");
            String nome = scanner.nextLine().trim();

            String tipo = selezionaDaLista("Tipo Struttura:", new String[]{"Privata", "Pubblica"});
            
            System.out.print("Città: ");
            String citta = scanner.nextLine().trim();
            
            System.out.print("Indirizzo: ");
            String indirizzo = scanner.nextLine().trim();
            
            System.out.print("Nome del Gestore (o referente): ");
            String gestore = scanner.nextLine().trim();

            String tipoAtt = selezionaDaLista("Tipo Attività:", new String[]{"Bar", "Biblioteca", "Università"});

            // 2. Gestione CheckBox (Si/No)
            boolean wifi = chiediConferma("La struttura dispone di WiFi?");
            boolean ristorazione = chiediConferma("La struttura dispone di servizio ristorazione?");

            // Orario di default
             System.out.print("Orario: ");
            String orario = scanner.nextLine().trim();

            // 3. Validazione e invio
            if (nome.isEmpty() || citta.isEmpty() || indirizzo.isEmpty() || gestore.isEmpty() || tipoAtt.isEmpty() || orario.isEmpty()) {
                throw new IllegalArgumentException("Informazioni mancanti! Tutti i campi sono obbligatori.");
            }

            BeanStruttura beanStruttura = new BeanStruttura(tipo, nome, citta, indirizzo, orario, wifi, ristorazione, tipoAtt, gestore);
            // 4. Chiamata al Controller per il salvataggio nel DBMS
            controller.creazioneUtente(utenteCorrente);
            System.out.println("\n Registrazione effettuata con successo!");
            controller.CreaStruttura(utenteCorrente, beanStruttura);

            System.out.println("\n Struttura registrata con successo!");
            
            // 4. Navigazione finale
            System.out.println("Premi invio per andare al Menu...");
            scanner.nextLine();
            new HostMenuCLI(utenteCorrente).start();

        } catch (IllegalArgumentException e) {
            System.err.println("\n Errore dati: " + e.getMessage());
            System.out.println("Riprovare? (s/n)");
            if(scanner.nextLine().equalsIgnoreCase("s")) start();
        } catch (Exception e) {
            System.err.println("\n Errore imprevisto: " + e.getMessage());
        }
    }

    // Helper per simulare ComboBox
    private String selezionaDaLista(String titolo, String[] opzioni) {
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
            } catch (NumberFormatException e) { /* continua */ }
            System.out.println("Scelta non valida.");
        }
    }

    // Helper per simulare CheckBox
    private boolean chiediConferma(String domanda) {
        System.out.print(domanda + " (s/n): ");
        String risp = scanner.nextLine().trim().toLowerCase();
        return risp.equals("s") || risp.equals("si");
    }
}
