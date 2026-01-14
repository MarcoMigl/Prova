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
        System.out.println("\n========================================");
        System.out.println("   REGISTRAZIONE DELLA TUA STRUTTURA    ");
        System.out.println("========================================");
        System.out.println("Benvenuto " + utenteCorrente.getName() + ", inserisci i dati del locale.");

        try {
            // 1. Acquisizione dati tramite input
            String tipo = selezionaDaLista("Tipo Struttura:", new String[]{"Privata", "Pubblica"});
            
            System.out.print("Città: ");
            String citta = scanner.nextLine().trim();
            
            System.out.print("Indirizzo: ");
            String indirizzo = scanner.nextLine().trim();
            
            System.out.print("Orario apertura (es. 08:00-20:00): ");
            String orario = scanner.nextLine().trim();

            // 2. Gestione CheckBox (Si/No)
            boolean wifi = chiediConferma("La struttura dispone di WiFi?");
            boolean ristorazione = chiediConferma("La struttura dispone di servizio ristorazione?");

            // 3. Creazione dell'utente 
            controllerUtente.creazioneUtente(utenteCorrente);
            System.out.println("Registrazione Utente effettuata...");

            // 4. Preparazione BeanStruttura
            
            BeanStruttura beanStruttura = new BeanStruttura(tipo, utenteCorrente.getNomeAttivita(), citta, indirizzo, wifi, ristorazione);
            beanStruttura.setOrario(orario);
            beanStruttura.setTipoAttivita(utenteCorrente.getTipoAttivita());
            beanStruttura.setGestore(utenteCorrente.getName());
            beanStruttura.setFoto("placeholder.png"); // Default per CLI se non implementi upload

            // 5. Chiamata al Controller per il salvataggio
            controllerStrutture.creaStruttura(utenteCorrente, beanStruttura);

            System.out.println("\n[OK] Struttura '" + utenteCorrente.getNomeAttivita() + "' registrata con successo!");
            
            // 6. Navigazione finale
            System.out.println("Premi invio per tornare al Menu...");
            scanner.nextLine();
           
             new HostMenuCLI(utenteCorrente).start(); 

        } catch (CampiVuotiException e) {
            System.err.println("\n[ERRORE] " + e.getMessage());
            riprova();
        } catch (SQLException e) {
            System.out.println("Errore Database");
        } catch (IOException e) {
            System.out.println("Errore I/O");
        } catch (Exception e) {
            System.err.println("\n[ERRORE IMPREVISTO] " + e.getMessage());
        }
    }

    private void riprova() {
        System.out.print("Vuoi riprovare l'inserimento? (s/n): ");
        if(scanner.nextLine().equalsIgnoreCase("s")) {
            start();
        }
    }

    // Helper per simulare ComboBox
    private String selezionaDaLista(String titolo, String[] opzioni) {
        while (true) {
            System.out.println("\n" + titolo);
            for (int i = 0; i < opzioni.length; i++) {
                System.out.println((i + 1) + ") " + opzioni[i]);
            }
            System.out.print("Scelta (numero): ");
            try {
                int scelta = Integer.parseInt(scanner.nextLine());
                if (scelta >= 1 && scelta <= opzioni.length) {
                    return opzioni[scelta - 1];
                }
            } catch (NumberFormatException e) { /* Fall through */ }
            System.out.println("Scelta non valida, riprova.");
        }
    }

    // Helper per simulare CheckBox
    private boolean chiediConferma(String domanda) {
        System.out.print(domanda + " (s/n): ");
        String risp = scanner.nextLine().trim().toLowerCase();
        return risp.equals("s") || risp.equals("si");
    }
}