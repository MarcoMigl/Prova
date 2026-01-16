package appolloni.migliano.cli;

import java.util.List;
import java.util.Scanner;

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
            System.out.println("\n========================================"); //NOSONAR
            System.out.println("          PANNELLO GESTORE (HOST)       "); //NOSONAR
            System.out.println("========================================"); //NOSONAR
            System.out.println("Benvenuto, " + beanUtente.getName()); //NOSONAR

            try {
                // 1. Carica e visualizza info struttura
                BeanStruttura struttura = controllerGestioneStruttura.visualizzaStrutturaHost(beanUtente.getEmail());
                mostraDettagliStruttura(struttura);

                // 2. Carica e visualizza recensioni
                System.out.println("\n--- ULTIME RECENSIONI ---"); //NOSONAR
                List<BeanRecensioni> recensioni = controllerRecensioni.cercaRecensioniPerStruttura(struttura);
                if (recensioni.isEmpty()) {
                    System.out.println("[ Nessuna recensione ricevuta ]"); //NOSONAR
                } else {
                    for (BeanRecensioni r : recensioni) {
                        System.out.println("- " + r.getAutore() + ": [" + r.getVoto() + "/5 ] \"" + r.getTesto() + "\""); //NOSONAR
                    }
                }

                // 3. Menu Azioni
                System.out.println("\n----------------------------------------"); //NOSONAR
                System.out.println("1) Modifica Dati Struttura"); //NOSONAR
                System.out.println("2) Logout"); //NOSONAR
                System.out.print("Scelta: "); //NOSONAR

                String scelta = scanner.nextLine();

                switch (scelta) {
                    case "1" -> new ModificaStrutturaCLI(beanUtente, struttura).start();
                    case "2" -> {
                        System.out.println("Logout in corso..."); //NOSONAR
                        back = true;
                    }
                    default -> System.out.println("Scelta non valida."); //NOSONAR
                }

            } catch (Exception e) {
                 System.out.println("Errore nel caricamento dati"); //NOSONAR
                 e.printStackTrace();
                back = true;
            }
        }
    }

    private void mostraDettagliStruttura(BeanStruttura s) {
        System.out.println("\nDETTAGLI STRUTTURA:"); //NOSONAR
        System.out.println("Nome:       " + s.getName()); //NOSONAR
        System.out.println("Città:      " + s.getCitta() + " (" + s.getIndirizzo() + ")"); //NOSONAR
        System.out.println("Tipo:       " + s.getTipo() + " - " + s.getTipoAttivita()); //NOSONAR
        System.out.println("Orario:     " + s.getOrario()); //NOSONAR
        System.out.println("Servizi:    WiFi: " + (s.hasWifi() ? "Sì" : "No") +  //NOSONAR
                           " | Ristorazione: " + (s.hasRistorazione() ? "Sì" : "No")); //NOSONAR
        System.out.println("Foto:       " + "Foto non disponibile in versione CLI."); //NOSONAR
    }


}