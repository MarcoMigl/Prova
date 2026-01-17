package appolloni.migliano.cli;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneStrutture;
import appolloni.migliano.controller.ControllerGestioneUtente;
import appolloni.migliano.exception.CampiVuotiException;

public class CreazioneStruttureCLI {

    private final ControllerGestioneStrutture controllerStrutture;
    private final ControllerGestioneUtente controllerUtente;
    private final Scanner scanner;
    private final BeanUtenti utenteCorrente;

    public CreazioneStruttureCLI(BeanUtenti bean) {
        this.controllerStrutture = new ControllerGestioneStrutture();
        this.controllerUtente = new ControllerGestioneUtente();
        this.scanner = new Scanner(System.in);
        this.utenteCorrente = bean;
    }

    public void start() {
        System.out.println("\n========================================"); //NOSONAR
        System.out.println("    REGISTRAZIONE DELLA TUA STRUTTURA    "); //NOSONAR
        System.out.println("========================================"); //NOSONAR
        System.out.println("Benvenuto " + utenteCorrente.getName());  //NOSONAR
        System.out.println("Struttura: " + utenteCorrente.getNomeAttivita() + " (" + utenteCorrente.getTipoAttivita() + ")");  //NOSONAR

        try {
            // 1. Acquisizione dati
            System.out.print("Città: ");  //NOSONAR
            String citta = scanner.nextLine().trim();
             
            System.out.print("Indirizzo: ");  //NOSONAR
            String indirizzo = scanner.nextLine().trim();
            
            String orario = acquisisciOrario();
            String tipoAttivita = selezionaOpzione("Tipo Attività:", new String[]{"Bar", "Università", "Biblioteca"});

            boolean wifi = chiediConferma("La struttura dispone di WiFi?");
            boolean ristorazione = chiediConferma("La struttura dispone di servizio ristorazione?");

            // 2. Gestione Foto 
            String nomeFotoFinale = "Foto non disponibili in versione CLI.";  
            System.out.print("Foto non disponibili in versione CLI. ");  //NOSONAR
             

            // 3. Salvataggio Utente 
            controllerUtente.creazioneUtente(utenteCorrente);
            System.out.println("Account Host creato correttamente...");  //NOSONAR

            // 4. Preparazione BeanStruttura
            BeanStruttura beanStruttura = new BeanStruttura(utenteCorrente.getTipoAttivita(), utenteCorrente.getNomeAttivita(), citta, indirizzo, wifi, ristorazione);
            beanStruttura.setOrario(orario);
            beanStruttura.setTipoAttivita(tipoAttivita);
            beanStruttura.setGestore(utenteCorrente.getEmail()); 
            beanStruttura.setFoto(nomeFotoFinale);

            // 5. Logica di Rivendicazione vs Nuova Creazione
            if (controllerStrutture.esistenzaStruttura(utenteCorrente.getNomeAttivita())) {
                System.out.println("Struttura già segnalata dal sistema. Procedo con la rivendicazione...");  //NOSONAR
                controllerStrutture.rivendicaStruttura(beanStruttura, utenteCorrente.getEmail());
            } else {
                controllerStrutture.creaStruttura(utenteCorrente, beanStruttura);
            }

            System.out.println("\n[OK] Registrazione completata con successo!");  //NOSONAR
            System.out.println("Premi invio per accedere al tuo pannello...");  //NOSONAR
            scanner.nextLine();
            
            new HostMenuCLI(utenteCorrente).start(); 

        } catch (CampiVuotiException e) {
            System.err.println("\n[ERRORE] Dati mancanti: " + e.getMessage());  //NOSONAR
            riprova();
        } catch (SQLException e) {
            System.err.println("\n[ERRORE DB] Errore durante il salvataggio: " + e.getMessage());  //NOSONAR
            // Interrompe il flusso in caso di errore DB (es. email duplicata)
          
        } catch (IOException e) {
            System.err.println("\n[ERRORE I/O] Impossibile gestire il file immagine.");  //NOSONAR
           
        } catch (Exception e) {
            System.err.println("\n[ERRORE] " + e.getMessage());  //NOSONAR
        }
    }

    private void riprova() {
        System.out.print("Vuoi riprovare l'inserimento? (s/n): ");  //NOSONAR
        if(scanner.nextLine().equalsIgnoreCase("s")) {
            start();
        }
    }

   private String acquisisciOrario() {
    String input;
    while (true) {
        System.out.print("Orario apertura (es. 08:00-20:00): "); //NOSONAR
        input = scanner.nextLine().trim();

        if (input.isEmpty()) return "";

        if (validaFormatoEIntervallo(input)) {
            return input;
        }
        
        System.out.println("[ERRORE] Formato non valido o orario incoerente."); //NOSONAR
    }
}

private boolean validaFormatoEIntervallo(String input) {
    String regex = "^([0-1]?\\d|2[0-3]):[0-5]\\d-([0-1]?\\d|2[0-3]):[0-5]\\d$";
    
    // 1. Controllo Regex (Livello 1)
    if (!input.matches(regex)) {
        return false;
    }

    String[] parti = input.split("-");
    // Controllo sicurezza array
    if (parti.length < 2) return false;

    String[] inizio = parti[0].split(":");
    String[] fine = parti[1].split(":");

    if (inizio.length < 2 || fine.length < 2) return false;

    // 2. Calcolo logico
    int minutiInizio = Integer.parseInt(inizio[0]) * 60 + Integer.parseInt(inizio[1]);
    int minutiFine = Integer.parseInt(fine[0]) * 60 + Integer.parseInt(fine[1]);

    if (minutiFine <= minutiInizio) {
        System.out.println("[ERRORE] L'orario di chiusura deve essere successivo a quello di apertura."); //NOSONAR
        return false;
    }

    return true;
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
    private boolean chiediConferma(String domanda) {
        System.out.print(domanda + " (s/n): ");  //NOSONAR
        String risp = scanner.nextLine().trim().toLowerCase();
        return risp.equals("s") || risp.equals("si");
    }

}


