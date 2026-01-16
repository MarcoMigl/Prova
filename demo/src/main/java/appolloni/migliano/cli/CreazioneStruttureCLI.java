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
            beanStruttura.setTipoAttivita(utenteCorrente.getTipoAttivita());
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
            return; 
        } catch (IOException e) {
            System.err.println("\n[ERRORE I/O] Impossibile gestire il file immagine.");  //NOSONAR
            return;
        } catch (Exception e) {
            System.err.println("\n[ERRORE] " + e.getMessage());  //NOSONAR
            return;
        }
    }

    private void riprova() {
        System.out.print("Vuoi riprovare l'inserimento? (s/n): ");  //NOSONAR
        if(scanner.nextLine().equalsIgnoreCase("s")) {
            start();
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
        System.out.print(domanda + " (s/n): ");  //NOSONAR
        String risp = scanner.nextLine().trim().toLowerCase();
        return risp.equals("s") || risp.equals("si");
    }
}