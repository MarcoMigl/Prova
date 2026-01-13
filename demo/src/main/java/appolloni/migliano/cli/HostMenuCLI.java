package appolloni.migliano.cli;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import appolloni.migliano.HelperErrori;
import appolloni.migliano.bean.BeanRecensioni;
import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneStrutture;
import appolloni.migliano.controller.ControllerRecensioni;

public class HostMenuCLI {

    private final Scanner scanner;
    private final BeanUtenti beanUtente;
    private final ControllerGestioneStrutture controllerGestioneStruttura;
    private final ControllerRecensioni controllerRecensioni;

    public HostMenuCLI(BeanUtenti utente) {
        this.scanner = new Scanner(System.in);
        this.beanUtente = utente;
        this.controllerGestioneStruttura = new ControllerGestioneStrutture();
        this.controllerRecensioni = new ControllerRecensioni();
    }

    public void start() {
        boolean back = false;

        while (!back) {
            System.out.println("\n========================================");
            System.out.println("          PANNELLO GESTORE (HOST)       ");
            System.out.println("========================================");
            System.out.println("Benvenuto, " + beanUtente.getName());

            try {
                // 1. Carica e visualizza info struttura
                BeanStruttura struttura = controllerGestioneStruttura.visualizzaStrutturaHost(beanUtente.getEmail());
                mostraDettagliStruttura(struttura);

                // 2. Carica e visualizza recensioni
                System.out.println("\n--- ULTIME RECENSIONI ---");
                List<BeanRecensioni> recensioni = controllerRecensioni.cercaRecensioniPerStruttura(struttura);
                if (recensioni.isEmpty()) {
                    System.out.println("[ Nessuna recensione ricevuta ]");
                } else {
                    for (BeanRecensioni r : recensioni) {
                        System.out.println("- " + r.getAutore() + ": [" + r.getVoto() + "/5 ⭐] \"" + r.getTesto() + "\"");
                    }
                }

                // 3. Menu Azioni
                System.out.println("\n----------------------------------------");
                System.out.println("1) Modifica Dati Struttura");
                System.out.println("2) Cambia Foto Struttura");
                System.out.println("3) Logout");
                System.out.print("Scelta: ");

                String scelta = scanner.nextLine();

                switch (scelta) {
                    case "1" -> new ModificaStrutturaCLI(beanUtente, struttura).start();
                    case "2" -> gestioneCambioFoto();
                    case "3" -> {
                        System.out.println("Logout in corso...");
                        back = true;
                    }
                    default -> System.out.println("Scelta non valida.");
                }

            } catch (Exception e) {
                 HelperErrori.errore("Errore nel caricamento dati:", e.getMessage());
                back = true;
            }
        }
    }

    private void mostraDettagliStruttura(BeanStruttura s) {
        System.out.println("\nDETTAGLI STRUTTURA:");
        System.out.println("Nome:       " + s.getName());
        System.out.println("Città:      " + s.getCitta() + " (" + s.getIndirizzo() + ")");
        System.out.println("Tipo:       " + s.getTipo() + " - " + s.getTipoAttivita());
        System.out.println("Orario:     " + s.getOrario());
        System.out.println("Servizi:    WiFi: " + (s.hasWifi() ? "Sì" : "No") + 
                           " | Ristorazione: " + (s.hasRistorazione() ? "Sì" : "No"));
        System.out.println("Foto:       " + (s.getFoto() != null ? s.getFoto() : "Nessuna"));
    }

    private void gestioneCambioFoto() {
        System.out.print("\nInserisci il percorso completo della nuova immagine: ");
        String path = scanner.nextLine();
        File file = new File(path);

        if (file.exists() && file.isFile()) {
            try {
                
                controllerGestioneStruttura.cambiaFoto(beanUtente.getEmail(), file.getName());
                System.out.println(" Foto aggiornata con successo (Simulazione salvataggio)");
            } catch (Exception e) {
                HelperErrori.errore("Errore aggiornamento foto:", e.getMessage());
            }
        } else {
            System.out.println(" Errore: File non trovato al percorso specificato.");
        }
    }
}