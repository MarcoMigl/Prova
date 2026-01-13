package appolloni.migliano.cli;

import java.util.List;
import java.util.Scanner;

import appolloni.migliano.HelperErrori;
import appolloni.migliano.bean.BeanRecensioni;
import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerRecensioni;

public class DettagliStrutturaCLI {

    private final Scanner scanner;
    private final BeanUtenti beanUtente;
    private final BeanStruttura beanStruttura;
    private final ControllerRecensioni controllerRecensione;

    public DettagliStrutturaCLI(BeanUtenti utente, BeanStruttura struttura) {
        this.scanner = new Scanner(System.in);
        this.beanUtente = utente;
        this.beanStruttura = struttura;
        this.controllerRecensione = new ControllerRecensioni();
    }

    public void start() {
        boolean back = false;

        while (!back) {
            System.out.println("\n========================================");
            System.out.println("          DETTAGLI STRUTTURA            ");
            System.out.println("========================================");

            // 1. Visualizzazione Informazioni
            stampaInfoStruttura();

            // 2. Caricamento e Visualizzazione Recensioni
            System.out.println("\n--- RECENSIONI UTENTI ---");
            try {
                List<BeanRecensioni> lista = controllerRecensione.cercaRecensioniPerStruttura(beanStruttura);
                 if (lista.isEmpty()) {
                System.out.println("[ Nessuna recensione ancora presente ]");
            } else {
                for (BeanRecensioni b : lista) {
                    System.out.printf("- %s: %d/5 ⭐ | %s\n", b.getAutore(), b.getVoto(), b.getTesto());
                }
            }
            } catch (Exception e) {
               HelperErrori.errore("Errore caricamento recensioni: ", e.getMessage());
            }
            
            // 3. Menu Azioni
            System.out.println("\n----------------------------------------");
            System.out.println("S) Scrivi una recensione");
            System.out.println("I) Torna indietro");
            System.out.print("Scelta: ");

            String scelta = scanner.nextLine().toUpperCase();

            switch (scelta) {
                case "S" -> {
                    new ScriviRecensioneCLI(beanUtente, beanStruttura).start();
                    // Al ritorno, il ciclo while ricaricherà le recensioni aggiornate
                }
                case "I" -> back = true;
                default -> System.out.println("Scelta non valida.");
            }
        }
    }

    private void stampaInfoStruttura() {
        System.out.println("NOME:            " + beanStruttura.getName());
        System.out.println("CATEGORIA:       " + (beanStruttura.getTipoAttivita() != null ? beanStruttura.getTipoAttivita().toUpperCase() : "GENERICA"));
        System.out.println("CITTA':          " + beanStruttura.getCitta());
        System.out.println("INDIRIZZO:       " + beanStruttura.getIndirizzo());
        System.out.println("ORARIO:          " + (beanStruttura.getOrario() != null ? beanStruttura.getOrario() : "Non disponibile"));
        System.out.println("GESTORE:         " + (beanStruttura.getGestore() != null ? beanStruttura.getGestore() : "Pubblica"));
        
        // Servizi con formattazione testuale per i colori (simulati)
        System.out.print("WIFI:            " + (beanStruttura.hasWifi() ? "[SI]" : "[NO]"));
        System.out.println(" | RISTORAZIONE: " + (beanStruttura.hasRistorazione() ? "[SI]" : "[NO]"));

        // Gestione Foto (Testuale)
        String foto = beanStruttura.getFoto();
        System.out.println("FOTO DISPONIBILE: " + (foto != null && !foto.isEmpty() ? foto : "Nessuna foto"));
    }
}
