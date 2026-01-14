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
        System.out.println("   REGISTRAZIONE DELLA TUA STRUTTURA    "); //NOSONAR
        System.out.println("========================================"); //NOSONAR
        System.out.println("Benvenuto " + utenteCorrente.getName() + ", inserisci i dati del locale."); //NOSONAR

        try {
            // 1. Acquisizione dati tramite input
            String tipo = selezionaDaLista("Tipo Struttura:", new String[]{"Privata", "Pubblica"});
            
            System.out.print("Città: "); //NOSONAR
            String citta = scanner.nextLine().trim();
            
            System.out.print("Indirizzo: "); //NOSONAR
            String indirizzo = scanner.nextLine().trim();
            
            System.out.print("Orario apertura (es. 08:00-20:00): "); //NOSONAR
            String orario = scanner.nextLine().trim();

            // 2. Gestione CheckBox (Si/No)
            boolean wifi = chiediConferma("La struttura dispone di WiFi?");
            boolean ristorazione = chiediConferma("La struttura dispone di servizio ristorazione?");

            // 3. Creazione dell'utente 
            controllerUtente.creazioneUtente(utenteCorrente);
            System.out.println("Registrazione Utente effettuata..."); //NOSONAR

            // 4. Preparazione BeanStruttura
            
            BeanStruttura beanStruttura = new BeanStruttura(tipo, utenteCorrente.getNomeAttivita(), citta, indirizzo, wifi, ristorazione);
            beanStruttura.setOrario(orario);
            beanStruttura.setTipoAttivita(utenteCorrente.getTipoAttivita());
            beanStruttura.setGestore(utenteCorrente.getName());
            beanStruttura.setFoto("placeholder.png"); // Default per CLI se non implementi upload

            // 5. Chiamata al Controller per il salvataggio
            controllerStrutture.creaStruttura(utenteCorrente, beanStruttura);

            System.out.println("\n[OK] Struttura '" + utenteCorrente.getNomeAttivita() + "' registrata con successo!"); //NOSONAR
            
            // 6. Navigazione finale
            System.out.println("Premi invio per tornare al Menu..."); //NOSONAR
            scanner.nextLine();
           
             new HostMenuCLI(utenteCorrente).start(); 

        } catch (CampiVuotiException e) {
            System.err.println("\n[ERRORE] " + e.getMessage()); //NOSONAR
            riprova();
        } catch (SQLException e) {
            System.out.println("Errore Database"); //NOSONAR
        } catch (IOException e) {
            System.out.println("Errore I/O"); //NOSONAR
        } catch (Exception e) {
            System.err.println("\n[ERRORE IMPREVISTO] " + e.getMessage()); //NOSONAR
        }
    }

    private void riprova() {
        System.out.print("Vuoi riprovare l'inserimento? (s/n): "); //NOSONAR
        if(scanner.nextLine().equalsIgnoreCase("s")) {
            start();
        }
    }

    // Helper per simulare ComboBox
    private String selezionaDaLista(String titolo, String[] opzioni) {
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
            } catch (NumberFormatException e) { /* Fall through */ }
            System.out.println("Scelta non valida, riprova."); //NOSONAR
        }
    }

    // Helper per simulare CheckBox
    private boolean chiediConferma(String domanda) {
        System.out.print(domanda + " (s/n): "); //NOSONAR
        String risp = scanner.nextLine().trim().toLowerCase();
        return risp.equals("s") || risp.equals("si");
    }

}

