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
            String tipo = selezionaTipoUtente();

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

            validazioneInput(nome, cognome, email, citta, password);

            BeanUtenti beanUtenti = new BeanUtenti(tipo, nome, cognome, email, password, citta);

            // Gestione Host vs Studente
            if ("Host".equalsIgnoreCase(tipo)) {
                gestisciRegistrazioneHost(beanUtenti);
            } else {
                salvaUtenteNelSistema(beanUtenti);
            }

            eseguiNavigazionePostRegistrazione(tipo, beanUtenti);

        } catch (CampiVuotiException | EmailNonValidaException e) {
            System.err.println("\n Errore nei dati: " + e.getMessage()); //NOSONAR
            chiediRiprova();
        } catch (Exception e) {
            // Qui gestiamo l'errore generico senza Logger
            System.err.println("\n Errore tecnico: " + e.getMessage()); //NOSONAR
        }
    }

    private void validazioneInput(String n, String co, String e, String ci, String p) throws CampiVuotiException, EmailNonValidaException {
        if (n.isEmpty() || co.isEmpty() || e.isEmpty() || ci.isEmpty() || p.isEmpty()) {
            throw new CampiVuotiException("Tutti i campi sono obbligatori.");
        }
        if (!e.contains("@")) {
            throw new EmailNonValidaException("Inserire una email valida.");
        }
    }

    private void salvaUtenteNelSistema(BeanUtenti bean) {
        // Metodo estratto per eliminare il nested try
        try {
            controller.creazioneUtente(bean);
            System.out.println("\n Registrazione effettuata con successo!"); //NOSONAR
        } catch (Exception e) {
            System.err.println("Errore durante il salvataggio: " + e.getMessage()); //NOSONAR
        }
    }

    private void gestisciRegistrazioneHost(BeanUtenti bean) {
        System.out.println("Configurazione struttura per Host..."); //NOSONAR
        new CreazioneStruttureCLI(bean).start();
    }

    private void eseguiNavigazionePostRegistrazione(String tipo, BeanUtenti bean) {
        if ("Host".equalsIgnoreCase(tipo)) {
            new HostMenuCLI(bean).start();
        } else {
            System.out.println("Benvenuto Studente! Caricamento menu..."); //NOSONAR
            new MenuPrincipaleCLI(bean).start();
        }
    }

    private void chiediRiprova() {
        System.out.println("Vuoi riprovare la registrazione? (s/n)"); //NOSONAR
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            start();
        }
    }

    private String selezionaTipoUtente() {
        while (true) {
            System.out.println("Seleziona il tuo ruolo:"); //NOSONAR
            System.out.println("1) Studente"); //NOSONAR
            System.out.println("2) Host"); //NOSONAR
            System.out.print("Scelta: "); //NOSONAR
            String scelta = scanner.nextLine();
            if ("1".equals(scelta)) return "Studente";
            if ("2".equals(scelta)) return "Host";
            System.out.println("Scelta non valida."); //NOSONAR
        }
    }
}
