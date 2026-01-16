package appolloni.migliano.cli;

import java.util.List;
import java.util.Scanner;

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
            System.out.println("\n========================================"); //NOSONAR
            System.out.println("          DETTAGLI STRUTTURA            "); //NOSONAR
            System.out.println("========================================"); //NOSONAR

            // 1. Visualizzazione Informazioni
            stampaInfoStruttura();

            // 2. Caricamento e Visualizzazione Recensioni
            System.out.println("\n--- RECENSIONI UTENTI ---"); //NOSONAR
            try {
                List<BeanRecensioni> lista = controllerRecensione.cercaRecensioniPerStruttura(beanStruttura);
                 if (lista.isEmpty()) {
                System.out.println("[ Nessuna recensione ancora presente ]"); //NOSONAR
            } else {
                for (BeanRecensioni b : lista) {
                    System.out.printf("- %s: %d/5  | %s\n", b.getAutore(), b.getVoto(), b.getTesto()); //NOSONAR
                }
            }
            } catch (Exception e) {
               System.out.println("Errore caricamento recensioni"); //NOSONAR
            }
            
            // 3. Menu Azioni
            System.out.println("\n----------------------------------------"); //NOSONAR
            System.out.println("S) Scrivi una recensione"); //NOSONAR
            System.out.println("I) Torna indietro"); //NOSONAR
            System.out.print("Scelta: "); //NOSONAR

            String scelta = scanner.nextLine().toUpperCase();

            switch (scelta) {
                case "S" -> new ScriviRecensioneCLI(beanUtente, beanStruttura).start();
                    // Al ritorno, il ciclo while ricaricherà le recensioni aggiornate
                case "I" -> back = true;
                default -> System.out.println("Scelta non valida."); //NOSONAR
            }
        }
    }

    private void stampaInfoStruttura() {
        System.out.println("NOME:            " + beanStruttura.getName()); //NOSONAR
        System.out.println("CATEGORIA:       " + (beanStruttura.getTipoAttivita() != null ? beanStruttura.getTipoAttivita().toUpperCase() : "GENERICA")); //NOSONAR
        System.out.println("CITTA':          " + beanStruttura.getCitta()); //NOSONAR
        System.out.println("INDIRIZZO:       " + beanStruttura.getIndirizzo()); //NOSONAR
        System.out.println("ORARIO:          " + (beanStruttura.getOrario() != null ? beanStruttura.getOrario() : "Non disponibile")); //NOSONAR
        System.out.println("GESTORE:         " + (beanStruttura.getGestore() != null ? beanStruttura.getGestore() : "Pubblica")); //NOSONAR
        
    
        System.out.print("WIFI:            " + (beanStruttura.hasWifi() ? "[SI]" : "[NO]")); //NOSONAR
        System.out.println(" | RISTORAZIONE: " + (beanStruttura.hasRistorazione() ? "[SI]" : "[NO]")); //NOSONAR

        String foto = beanStruttura.getFoto();
        System.out.println("FOTO DISPONIBILE: " + foto); //NOSONAR
    }
}

