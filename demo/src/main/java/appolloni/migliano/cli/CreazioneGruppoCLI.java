package appolloni.migliano.cli;

import java.util.List;
import java.util.Scanner;

import appolloni.migliano.bean.BeanGruppo;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneGruppo;

public class CreazioneGruppoCLI {

    private final ControllerGestioneGruppo controller;
    private final Scanner scanner;
    private final BeanUtenti utenteLoggato;

    public CreazioneGruppoCLI(BeanUtenti utente) {
        this.controller = new ControllerGestioneGruppo();
        this.scanner = new Scanner(System.in);
        this.utenteLoggato = utente;
    }

    public void start() {
        System.out.println("\n--- CREAZIONE NUOVO GRUPPO DI STUDIO ---"); //NOSONAR


        try {
            System.out.print("Tutti i campi sono obbligatori!"); //NOSONAR
            // 1. Dati base
            System.out.print("Inserisci il nome del gruppo: "); //NOSONAR
            String nome = scanner.nextLine();

            System.out.print("Inserisci la materia di studio: "); //NOSONAR
            String materia = scanner.nextLine();

            // 2. Localizzazione e recupero strutture
            System.out.print("Inserisci la città: "); //NOSONAR
            String citta = scanner.nextLine();

            List<String> strutture = controller.getListaStruttureDisponibili(citta);
            String luogoScelto = "";

            if (strutture.isEmpty()) {
                System.out.println("Nessuna struttura trovata in questa città. Inserimento manuale."); //NOSONAR
                System.out.print("Inserisci il luogo: "); //NOSONAR
                luogoScelto = scanner.nextLine();
            } else {
                luogoScelto = selezionaStrutturaUI(strutture);
            }

            // 3. Impacchettamento nel Bean e chiamata al Controller
            BeanGruppo nuovoGruppo = new BeanGruppo(nome, materia, utenteLoggato.getEmail(), luogoScelto, citta);
            
            controller.creaGruppo(utenteLoggato, nuovoGruppo);
            
            System.out.println("\n Gruppo '" + nome + "' creato con successo!"); //NOSONAR
            System.out.println("Sei stato aggiunto automaticamente come Amministratore."); //NOSONAR

        } catch (IllegalArgumentException e) {
             System.out.println("Errore validazione"); //NOSONAR

        } catch (Exception e) {
             System.out.println("Errore imprevisto"); //NOSONAR

        }
    }

    private String selezionaStrutturaUI(List<String> strutture) {
        System.out.println("\nStrutture disponibili a " + utenteLoggato.getCitta() + ":"); //NOSONAR
        for (int i = 0; i < strutture.size(); i++) {
            System.out.println((i + 1) + ") " + strutture.get(i)); //NOSONAR
        }
        System.out.println((strutture.size() + 1) + ") Altro (inserimento manuale)"); //NOSONAR

        while (true) {
            System.out.print("Seleziona un'opzione: "); //NOSONAR
            String input = scanner.nextLine();
            try {
                int scelta = Integer.parseInt(input);
                if (scelta >= 1 && scelta <= strutture.size()) {
                    return strutture.get(scelta - 1);
                } else if (scelta == strutture.size() + 1) {
                    System.out.print("Inserisci il nome del luogo: "); //NOSONAR
                    return scanner.nextLine();
                }
            } catch (NumberFormatException e) {
                // Continua il loop
            }
            System.out.println("Scelta non valida."); //NOSONAR
        }
    }

}