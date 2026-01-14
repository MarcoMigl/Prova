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
        System.out.println("\n--- CREAZIONE NUOVO GRUPPO DI STUDIO ---");

        // Controllo sul tipo utente
        if (!"Studente".equalsIgnoreCase(utenteLoggato.getTipo())) {
            System.out.println("Errore: Solo gli studenti possono creare gruppi.");
            return;
        }

        try {
            // 1. Dati base
            System.out.print("Inserisci il nome del gruppo: ");
            String nome = scanner.nextLine();

            System.out.print("Inserisci la materia di studio: ");
            String materia = scanner.nextLine();

            // 2. Localizzazione e recupero strutture
            System.out.print("Inserisci la città: ");
            String citta = scanner.nextLine();

            List<String> strutture = controller.getListaStruttureDisponibili(citta);
            String luogoScelto = "";

            if (strutture.isEmpty()) {
                System.out.println("Nessuna struttura trovata in questa città. Inserimento manuale.");
                System.out.print("Inserisci il luogo: ");
                luogoScelto = scanner.nextLine();
            } else {
                luogoScelto = selezionaStrutturaUI(strutture);
            }

            // 3. Impacchettamento nel Bean e chiamata al Controller
            BeanGruppo nuovoGruppo = new BeanGruppo(nome, materia, utenteLoggato.getEmail(), luogoScelto, citta);
            
            controller.creaGruppo(utenteLoggato, nuovoGruppo);
            
            System.out.println("\n✅ Gruppo '" + nome + "' creato con successo!");
            System.out.println("Sei stato aggiunto automaticamente come Amministratore.");

        } catch (IllegalArgumentException e) {
             System.out.println("Errore validazione");

        } catch (Exception e) {
             System.out.println("Errore imprevisto");

        }
    }

    private String selezionaStrutturaUI(List<String> strutture) {
        System.out.println("\nStrutture disponibili a " + utenteLoggato.getCitta() + ":");
        for (int i = 0; i < strutture.size(); i++) {
            System.out.println((i + 1) + ") " + strutture.get(i));
        }
        System.out.println((strutture.size() + 1) + ") Altro (inserimento manuale)");

        while (true) {
            System.out.print("Seleziona un'opzione: ");
            String input = scanner.nextLine();
            try {
                int scelta = Integer.parseInt(input);
                if (scelta >= 1 && scelta <= strutture.size()) {
                    return strutture.get(scelta - 1);
                } else if (scelta == strutture.size() + 1) {
                    System.out.print("Inserisci il nome del luogo: ");
                    return scanner.nextLine();
                }
            } catch (NumberFormatException e) {
                // Continua il loop
            }
            System.out.println("Scelta non valida.");
        }
    }
}