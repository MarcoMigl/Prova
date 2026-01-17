package appolloni.migliano.cli;

import java.util.List;
import java.util.Scanner;

import appolloni.migliano.bean.BeanGruppo;
import appolloni.migliano.bean.BeanRecensioni;
import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneGruppo;
import appolloni.migliano.controller.ControllerGestioneStrutture;
import appolloni.migliano.controller.ControllerRecensioni;
public class RicercaCLI {

    private  ControllerGestioneStrutture controllerStrutture = new ControllerGestioneStrutture();
    private  ControllerGestioneGruppo controllerGruppo = new ControllerGestioneGruppo();
    private final ControllerRecensioni controllerRecensioni;
    private final Scanner scanner;
    private final BeanUtenti beanUtente;

    public RicercaCLI(BeanUtenti beanUtente) {
        this.controllerStrutture = new ControllerGestioneStrutture();
        this.controllerGruppo = new ControllerGestioneGruppo();
        this.controllerRecensioni = new ControllerRecensioni();
        this.scanner = new Scanner(System.in);
        this.beanUtente = beanUtente;
    }

    public void start() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- MENU RICERCA ---"); //NOSONAR
            System.out.println("1. Cerca Gruppi di Studio"); //NOSONAR
            System.out.println("2. Cerca Strutture"); //NOSONAR
            System.out.println("3. Torna al menu precedente"); //NOSONAR
            System.out.print("Scelta: "); //NOSONAR

            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1" -> gestioneRicercaGruppi();
                case "2" -> gestioneRicercaStrutture();
                case "3" -> back = true;
                default -> System.out.println("Scelta non valida."); //NOSONAR
            }
        }
    }

    // --- SEZIONE GRUPPI DI STUDIO ---

    private void gestioneRicercaGruppi() {
        System.out.println("\n--- RICERCA GRUPPI ---"); //NOSONAR
        System.out.print("Filtra per nome (vuoto per skip): "); //NOSONAR
        String nome = promptInput();
        System.out.print("Filtra per città (vuoto per skip): "); //NOSONAR
        String citta = promptInput();
        System.out.print("Filtra per materia (vuoto per skip): "); //NOSONAR
        String materia = promptInput();

        try {
            List<BeanGruppo> risultati = controllerGruppo.cercaGruppi(nome, citta, materia);
            if (risultati.isEmpty()) {
                System.out.println("Nessun gruppo trovato."); //NOSONAR
            } else {
                mostraRisultatiGruppi(risultati);
            }
        } catch (Exception e) {
            System.err.println("Errore durante la ricerca: " + e.getMessage()); //NOSONAR
        }
    }

    private void mostraRisultatiGruppi(List<BeanGruppo> risultati) {
        System.out.println("\nGRUPPI TROVATI:"); //NOSONAR
        for (int i = 0; i < risultati.size(); i++) {
            BeanGruppo g = risultati.get(i);
            System.out.printf("%d) %-20s | %-15s | %-15s%n", (i + 1), g.getNome(), g.getCitta(), g.getMateria()); //NOSONAR
        }
        gestioneSelezioneGruppo(risultati);
    }

    private void gestioneSelezioneGruppo(List<BeanGruppo> risultati) {
        System.out.print("\nInserisci il numero del gruppo per unirti (o 0 per annullare): "); //NOSONAR
        try {
            int indice = Integer.parseInt(scanner.nextLine()) - 1;
            if (indice >= 0 && indice < risultati.size()) {
                controllerGruppo.aggiungiGruppo(beanUtente, risultati.get(indice));
                System.out.println("[OK] Ti sei unito al gruppo con successo!"); //NOSONAR
            }
        } catch (Exception e) {
            System.out.println("Operazione non riuscita: " + e.getMessage()); //NOSONAR
        }
    }

    // --- SEZIONE STRUTTURE ---

    private void gestioneRicercaStrutture() {
        System.out.println("\n--- RICERCA STRUTTURE ---"); //NOSONAR
        System.out.print("Nome struttura (vuoto per skip): "); //NOSONAR
        String nome = promptInput();
        System.out.print("Città (vuoto per skip): "); //NOSONAR
        String citta = promptInput();
        System.out.print("Tipo (Tutti, Bar, Biblioteca, Università): "); //NOSONAR
        String tipo = promptInput();

        try {
            List<BeanStruttura> risultati = controllerStrutture.cercaStrutture(nome, citta, tipo);
            if (risultati.isEmpty()) {
                System.out.println("Nessuna struttura trovata."); //NOSONAR
            } else {
                mostraRisultatiStrutture(risultati);
            }
        } catch (Exception e) {
            System.err.println("Errore durante la ricerca: " + e.getMessage()); //NOSONAR
        }
    }

    private void mostraRisultatiStrutture(List<BeanStruttura> risultati) {
        System.out.println("\nSTRUTTURE TROVATE:"); //NOSONAR
        for (int i = 0; i < risultati.size(); i++) {
            BeanStruttura s = risultati.get(i);
            System.out.println((i + 1) + ") " + s.getName() + " [" + s.getTipoAttivita() + "] - " + s.getCitta()); //NOSONAR
        }
        gestioneSelezioneStruttura(risultati);
    }

    private void gestioneSelezioneStruttura(List<BeanStruttura> risultati) {
        System.out.print("\nInserisci il numero della struttura per i dettagli (o 0 per annullare): "); //NOSONAR
        try {
            int indice = Integer.parseInt(scanner.nextLine()) - 1;
            if (indice >= 0 && indice < risultati.size()) {
                visualizzaDettagliStruttura(risultati.get(indice));
            }
        } catch (Exception e) {
            System.out.println("Scelta non valida."); //NOSONAR
        }
    }

    private void visualizzaDettagliStruttura(BeanStruttura s) {
        System.out.println("\n========================================"); //NOSONAR
        System.out.println("           DETTAGLI STRUTTURA           "); //NOSONAR
        System.out.println("========================================"); //NOSONAR
        System.out.println("NOME:         " + s.getName()); //NOSONAR
        System.out.println("CATEGORIA:    " + (s.getTipoAttivita() != null ? s.getTipoAttivita().toUpperCase() : "GENERICA")); //NOSONAR
        System.out.println("CITTA':       " + s.getCitta()); //NOSONAR
        System.out.println("INDIRIZZO:    " + s.getIndirizzo()); //NOSONAR
        System.out.println("ORARIO:       " + (s.getOrario() != null ? s.getOrario() : "Non disponibile")); //NOSONAR
        System.out.print("WIFI:         " + (s.hasWifi() ? "[SI]" : "[NO]")); //NOSONAR
        System.out.println(" | RISTORAZIONE: " + (s.hasRistorazione() ? "[SI]" : "[NO]")); //NOSONAR
        System.out.println("FOTO:         " + "Foto non disponibili in versione CLI. "); //NOSONAR

        // 2. Caricamento e Visualizzazione Recensioni
        System.out.println("\n--- RECENSIONI UTENTI ---"); //NOSONAR
        try {
            List<BeanRecensioni> lista = controllerRecensioni.cercaRecensioniPerStruttura(s);
            if (lista.isEmpty()) {
                System.out.println("[ Nessuna recensione ancora presente ]"); //NOSONAR
            } else {
                for (BeanRecensioni b : lista) {
                    System.out.printf("- %s: %d/5  | %s%n", b.getAutore(), b.getVoto(), b.getTesto()); //NOSONAR
                }
            }
        } catch (Exception e) {
            System.out.println("[ Errore nel caricamento delle recensioni ]"); //NOSONAR
        }

        // 3. Menu Azioni
        System.out.println("\n----------------------------------------"); //NOSONAR
        System.out.println("S) Scrivi una recensione"); //NOSONAR
        System.out.println("I) Torna indietro"); //NOSONAR
        System.out.print("Scelta: "); //NOSONAR

        String scelta = scanner.nextLine().toUpperCase();

        if (scelta.equals("S")) {
            new ScriviRecensioneCLI(beanUtente, s).start();
            // Ricorsione per rinfrescare la pagina e mostrare la nuova recensione
            visualizzaDettagliStruttura(s);
        }
    }


    private String promptInput() {
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? null : input;
    }

}