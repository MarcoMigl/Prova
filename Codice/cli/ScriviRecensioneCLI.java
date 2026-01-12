package appolloni.migliano.cli;

import java.util.Scanner;

import appolloni.migliano.bean.BeanRecensioni;
import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerRecensioni;

public class ScriviRecensioneCLI {

    private final Scanner scanner;
    private final BeanUtenti beanUtente;
    private final BeanStruttura beanStruttura;
    private final ControllerRecensioni controllerRecensioni;

    public ScriviRecensioneCLI(BeanUtenti utente, BeanStruttura struttura) {
        this.scanner = new Scanner(System.in);
        this.beanUtente = utente;
        this.beanStruttura = struttura;
        this.controllerRecensioni = new ControllerRecensioni();
    }

    public void start() {
        System.out.println("\n========================================");
        System.out.println("       SCRIVI UNA RECENSIONE           ");
        System.out.println("========================================");
        System.out.println("Struttura: " + beanStruttura.getName());
        System.out.println("Città:     " + beanStruttura.getCitta());
        System.out.println("----------------------------------------");

        try {
            // 1. Gestione del Voto
            int voto = richiediVoto();

            // 2. Gestione del Testo
            System.out.println("Scrivi il tuo commento (premi Invio per confermare):");
            String testo = scanner.nextLine().trim();

            if (testo.isEmpty()) {
                System.out.println("Nota: Hai inviato una recensione senza testo.");
            }

            // 3. Creazione del Bean e invio al Controller
            BeanRecensioni beanRecensioni = new BeanRecensioni(
                beanUtente.getEmail(), 
                testo, 
                voto, 
                beanStruttura.getName(), 
                beanStruttura.getGestore()
            );

            controllerRecensioni.inserisciRecensione(beanRecensioni);

            System.out.println("\n Recensione inviata con successo!");
            System.out.println("Voto assegnato: " + voto + "/5");

        } catch (Exception e) {
            System.err.println("\n Errore durante l'invio: " + e.getMessage());
        }
        
        System.out.println("\nPremi Invio per tornare indietro...");
        scanner.nextLine();
    }

    /**
     * Simula lo Slider garantendo che il voto sia tra 1 e 5.
     */
    private int richiediVoto() {
        int voto = -1;
        while (voto < 1 || voto > 5) {
            System.out.print("Inserisci un voto (da 1 a 5): ");
            String input = scanner.nextLine();
            try {
                voto = Integer.parseInt(input);
                if (voto < 1 || voto > 5) {
                    System.out.println("Errore: Il voto deve essere compreso tra 1 e 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Errore: Inserisci un numero valido.");
            }
        }
        return voto;
    }
}
