package appolloni.migliano.cli;

import java.util.Scanner;

import appolloni.migliano.exception.CampiVuotiException;
import appolloni.migliano.exception.EmailNonValidaException;
import appolloni.migliano.HelperErrori;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneUtente;

public class CreazioneUtenteCLI {

    private final ControllerGestioneUtente controller;
    private final Scanner scanner;

    public CreazioneUtenteCLI() {
        this.controller = new ControllerGestioneUtente();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("\n--- REGISTRAZIONE NUOVO UTENTE ---");

        try {
            // 1. Scelta del Tipo di Utente
            String tipo = selezionaTipoUtente();

            // 2. Acquisizione Dati Comuni
            System.out.print("Nome: ");
            String nome = scanner.nextLine().trim();
            System.out.print("Cognome: ");
            String cognome = scanner.nextLine().trim();
            System.out.print("Email: ");
            String email = scanner.nextLine().trim().toLowerCase();
            System.out.print("Città: ");
            String citta = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            // Validazione base dei campi obbligatori
            if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || citta.isEmpty() || password.isEmpty()) {
                throw new CampiVuotiException("Controlli di aver inserito una mail valida o  dati correttamente.");
            }
            if(!email.contains("@")){
                throw new EmailNonValidaException("Inserire una email valida.");
            }

            // 3. Creazione del BeanUtenti
            BeanUtenti beanUtente = new BeanUtenti(tipo, nome, cognome, email, password, citta);

            // Gestione dati extra se l'utente è un Host
            if ("Host".equalsIgnoreCase(tipo)) {
                System.out.println("Benvenuto Host! Reindirizzamento alla registrazione dei dettagli della struttura...");
                System.out.println("\n--- DATI ATTIVITÀ (Obbligatori per Host) ---");
                
                // Avviamo la CLI per la creazione della struttura fisica
                new CreazioneStruttureCLI(beanUtente).start();
                
            }else{
                // 4. Chiamata al Controller per il salvataggio nel DBMS
                try {
                    controller.creazioneUtente(beanUtente);
                    System.out.println("\n Registrazione effettuata con successo!");
                } catch (Exception e) {
                    HelperErrori.errore("Errore caricamento: ", e.getMessage());
                 }
           
        }

            // 5. NAVIGAZIONE POST-REGISTRAZIONE
            // Qui gestiamo il reindirizzamento in base al ruolo
            if ("Host".equalsIgnoreCase(tipo)) {
                System.out.println("Benvenuto Host! Reindirizzamento al menu...");
                
                new HostMenuCLI(beanUtente).start();
                
            } else {
                System.out.println("Benvenuto Studente! Reindirizzamento al menu principale...");
                
                // Avviamo la Home/Menu principale per lo studente
                new MenuPrincipaleCLI(beanUtente).start();
            }

        } catch (CampiVuotiException e) {
            System.err.println("\n Errore nei dati inseriti: " + e.getMessage());
            System.out.println("Vuoi riprovare la registrazione? (s/n)");
            if(scanner.nextLine().equalsIgnoreCase("s")) start();
        }catch (EmailNonValidaException e) {
            System.err.println("\n Errore nei dati inseriti: " + e.getMessage());
            System.out.println("Vuoi riprovare la registrazione? (s/n)");
            if(scanner.nextLine().equalsIgnoreCase("s")) start();
        }
         catch (Exception e) {
            System.err.println("\n Errore tecnico durante la registrazione: " + e.getMessage());
            // In caso di errore nel DB, mostriamo lo stack trace per debug (opzionale)
            // e.printStackTrace(); 
        }
    }

    /**
     * Menu di selezione per il tipo di utente
     */
    private String selezionaTipoUtente() {
        while (true) {
            System.out.println("Seleziona il tuo ruolo:");
            System.out.println("1) Studente");
            System.out.println("2) Host");
            System.out.print("Scelta: ");
            String scelta = scanner.nextLine();
            if (scelta.equals("1")) return "Studente";
            if (scelta.equals("2")) return "Host";
            System.out.println("Scelta non valida. Inserisci 1 o 2.");
        }
    }

    
}