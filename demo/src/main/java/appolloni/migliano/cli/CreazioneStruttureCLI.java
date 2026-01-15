package appolloni.migliano.cli;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
            String tipoProprieta = selezionaOpzione("Tipo Proprietà:", new String[]{"Privata", "Pubblica"});
            
            System.out.print("Città: ");  //NOSONAR
            String citta = scanner.nextLine().trim();
             
            System.out.print("Indirizzo: ");  //NOSONAR
            String indirizzo = scanner.nextLine().trim();
            
            System.out.print("Orario apertura (es. 08:00-20:00): ");  //NOSONAR
            String orario = scanner.nextLine().trim();

            boolean wifi = chiediConferma("La struttura dispone di WiFi?");
            boolean ristorazione = chiediConferma("La struttura dispone di servizio ristorazione?");

            // 2. Gestione Foto (Implementazione simile alla GUI)
            String nomeFotoFinale = "placeholder.png";  
            System.out.print("Vuoi caricare una foto? Inserisci il percorso completo (o premi Invio per saltare): ");  //NOSONAR
            String pathFoto = scanner.nextLine().trim();
            
            if (!pathFoto.isEmpty()) {
                File fileFoto = new File(pathFoto);
                if (fileFoto.exists() && fileFoto.isFile()) {
                    nomeFotoFinale = salvaFileSuDisco(fileFoto);
                    System.out.println("Foto caricata con successo: " + nomeFotoFinale);  //NOSONAR
                } else {
                    System.out.println("File non trovato. Verrà usata l'immagine predefinita.");  //NOSONAR
                }
            }

            // 3. Salvataggio Utente 
            controllerUtente.creazioneUtente(utenteCorrente);
            System.out.println("Account Host creato correttamente...");  //NOSONAR

            // 4. Preparazione BeanStruttura
            BeanStruttura beanStruttura = new BeanStruttura(tipoProprieta, utenteCorrente.getNomeAttivita(), citta, indirizzo, wifi, ristorazione);
            beanStruttura.setOrario(orario);
            beanStruttura.setTipoAttivita(utenteCorrente.getTipoAttivita());
            beanStruttura.setGestore(utenteCorrente.getEmail()); // Usiamo l'email come nel database
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
        } catch (IOException e) {
            System.err.println("\n[ERRORE I/O] Impossibile gestire il file immagine.");  //NOSONAR
        } catch (Exception e) {
            System.err.println("\n[ERRORE] " + e.getMessage());  //NOSONAR
            e.printStackTrace();
        }
    }

    // Metodo per il salvataggio file 
    private String salvaFileSuDisco(File fileInput) throws IOException {
        String folderPath = System.getProperty("user.home") + File.separator + "IspwImages";
        File folder = new File(folderPath);
        if (!folder.exists()) folder.mkdir();

        String nuovoNome = "Struttura_" + System.currentTimeMillis() + "_" + fileInput.getName();
        File destinazione = new File(folder, nuovoNome);

        Files.copy(fileInput.toPath(), destinazione.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return nuovoNome;
    }

    private void riprova() {
        System.out.print("Vuoi riprovare l'inserimento? (s/n): ");  //NOSONAR
        if(scanner.nextLine().equalsIgnoreCase("s")) {
            start();
        }
    }

    private String selezionaOpzione(String titolo, String[] opzioni) {
        while (true) {
            System.out.println("\n" + titolo);  //NOSONAR
            for (int i = 0; i < opzioni.length; i++) {
                System.out.println((i + 1) + ") " + opzioni[i]);  //NOSONAR
            }
            System.out.print("Scelta (numero): ");  //NOSONAR
            try {
                int scelta = Integer.parseInt(scanner.nextLine());
                if (scelta >= 1 && scelta <= opzioni.length) {
                    return opzioni[scelta - 1];
                }
            } catch (Exception e) { /* Errore di parsing, continua il ciclo */ }
            System.out.println("Scelta non valida.");  //NOSONAR
        }
    }

    private boolean chiediConferma(String domanda) {
        System.out.print(domanda + " (s/n): ");  //NOSONAR
        String risp = scanner.nextLine().trim().toLowerCase();
        return risp.equals("s") || risp.equals("si");
    }
}
