package appolloni.migliano.cli;

import java.util.Scanner;

import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneUtente;
import appolloni.migliano.exception.CampiVuotiException;
import appolloni.migliano.exception.EmailNonValidaException;

public class CreazioneUtenteCLI {

    private final ControllerGestioneUtente controller;
    private final Scanner scanner;

    public CreazioneUtenteCLI() {
        this.controller = new ControllerGestioneUtente();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("\n--- REGISTRAZIONE NUOVO UTENTE ---"); //NOSONAR

        try {
            // 1. Scelta del Tipo di Utente
            String tipo = selezionaTipoUtente();

            // 2. Acquisizione Dati Comuni
            System.out.print("Nome: "); //NOSONAR
            String nome = scanner.nextLine().trim();
            System.out.print("Cognome: "); //NOSONAR
            String cognome = scanner.nextLine().trim();
            System.out.print("Email: "); //NOSONAR
            String email = scanner.nextLine().trim().toLowerCase();
            System.out.print("Città: "); //NOSONAR
            String citta = scanner.nextLine().trim();
            System.out.print("Password: "); //NOSONAR
            String password = scanner.nextLine().trim();

            // Validazione base dei campi obbligatori
            if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || citta.isEmpty() || password.isEmpty()) {
                throw new CampiVuotiException("Controlli di aver inserito una mail valida o dati correttamente.");
            }
            if(!email.contains("@")){
                throw new EmailNonValidaException("Inserire una email valida.");
            }

            // 3. Creazione del BeanUtenti
            BeanUtenti beanUtenti = new BeanUtenti(tipo, nome, cognome, email, password, citta);

            // Gestione dati extra se l'utente è un Host
            if ("Host".equalsIgnoreCase(tipo)) {
                System.out.println("Benvenuto Host! Reindirizzamento alla registrazione dei dettagli della struttura..."); //NOSONAR
                System.out.println("\n--- DATI ATTIVITÀ (Obbligatori per Host) ---"); //NOSONAR
                String tipoatt = selezionaDaLista();
                System.out.print("Nome Attività: "); //NOSONAR
                String strutt = scanner.nextLine().trim();

                beanUtenti.setNomeAttivita(strutt);
                beanUtenti.setTipoAttivita(tipoatt);
                
                // Lanciamo la CLI delle strutture
                new CreazioneStruttureCLI(beanUtenti).start();
                
                // Usciamo dopo che la CLI delle strutture ha terminato (successo o errore)
                return; 
                
            } else {
                // 4. Studente: salvataggio e navigazione immediata
                salvaUtente(beanUtenti);
                System.out.println("Benvenuto Studente! Reindirizzamento al menu principale..."); //NOSONAR
                new MenuPrincipaleCLI(beanUtenti).start();
                return;
            }

        } catch (CampiVuotiException | EmailNonValidaException e) {
            System.err.println("\n Errore nei dati inseriti: " + e.getMessage()); //NOSONAR
            System.out.println("Vuoi riprovare la registrazione? (s/n)"); //NOSONAR
            if(scanner.nextLine().equalsIgnoreCase("s")) start();
        } catch (Exception e) {
            System.err.println("\n Errore tecnico durante la registrazione: " + e.getMessage()); //NOSONAR
        }
    }

    private void salvaUtente(BeanUtenti beanUtenti) {
        try {
            controller.creazioneUtente(beanUtenti);
            System.out.println("\n Registrazione effettuata con successo!"); //NOSONAR
        } catch (Exception e) {
            System.out.println("Errore caricamento: " + e.getMessage()); //NOSONAR
        }
    }

    private String selezionaTipoUtente() {
        while (true) {
            System.out.println("Seleziona il tuo ruolo:"); //NOSONAR
            System.out.println("1) Studente"); //NOSONAR
            System.out.println("2) Host"); //NOSONAR
            System.out.print("Scelta: "); //NOSONAR
            String scelta = scanner.nextLine();
            if (scelta.equals("1")) return "Studente";
            if (scelta.equals("2")) return "Host";
            System.out.println("Scelta non valida. Inserisci 1 o 2."); //NOSONAR
        }
    }

    private String selezionaDaLista() {
        while (true) {
            System.out.println("Seleziona il tipo di struttura:"); //NOSONAR
            System.out.println("1) Privata"); //NOSONAR
            System.out.println("2) Pubblica"); //NOSONAR
            System.out.print("Scelta: "); //NOSONAR
            String scelta = scanner.nextLine();
            if (scelta.equals("1")) return "Privata";
            if (scelta.equals("2")) return "Pubblica";
            System.out.println("Scelta non valida. Inserisci 1 o 2."); //NOSONAR
        }
    }
}