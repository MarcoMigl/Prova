package appolloni.migliano.cli;

import java.util.List;
import java.util.Scanner;


import appolloni.migliano.bean.BeanGruppo;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneGruppo;

public class MenuPrincipaleCLI {

    private final Scanner scanner;
    private final BeanUtenti bean;
    private final ControllerGestioneGruppo gestioneGruppi;

    public MenuPrincipaleCLI(BeanUtenti utente) {
        this.scanner = new Scanner(System.in);
        this.bean = utente;
        this.gestioneGruppi = new ControllerGestioneGruppo();
    }

    public void start() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n========================================"); //NOSONAR
            System.out.println("          STUDIO-APP: HOME             "); //NOSONAR
            System.out.println("========================================"); //NOSONAR
            System.out.println("Ciao, " + bean.getName() + "!"); //NOSONAR
            System.out.println("----------------------------------------"); //NOSONAR

            List<BeanGruppo> gruppiUtente = null;

            
            try {
                gruppiUtente = gestioneGruppi.visualizzaGruppi(bean);
                
                System.out.println("I TUOI GRUPPI:"); //NOSONAR
                if (gruppiUtente == null || gruppiUtente.isEmpty()) {
                    System.out.println("[ Nessun gruppo iscritto ]"); //NOSONAR
                } else {
                    for (int i = 0; i < gruppiUtente.size(); i++) {
                        System.out.println((i + 1) + ") Entra nella Chat di: " + gruppiUtente.get(i).getNome()); //NOSONAR
                    }
                }
            } catch (Exception e) {
                // Questo cattura l'errore SQL o logico e permette al menu di continuare a vivere
                 System.out.println("Errore nel caricamento dei gruppi"); //NOSONAR
            }

            System.out.println("----------------------------------------"); //NOSONAR
            System.out.println("AZIONI:"); //NOSONAR
            System.out.println("G) Crea Gruppo"); //NOSONAR
            System.out.println("R) Cerca Gruppi o Strutture"); //NOSONAR
            System.out.println("P) Visualizza Profilo"); //NOSONAR
            System.out.println("S) Segnala Struttura"); //NOSONAR
            System.out.println("L) Logout"); //NOSONAR
            System.out.print("\n Scelta: "); //NOSONAR

            String scelta = scanner.nextLine().toUpperCase();

            // 2. GESTIONE DELLA NAVIGAZIONE
            if (scelta.matches("\\d+")) { // Se l'utente ha inserito un numero
                gestioneSceltaChat(scelta, gruppiUtente);
            } else {
                switch (scelta) {
                    case "R" -> new RicercaCLI(bean).start(); 
                    case "P" -> new ProfiloUtenteCLI(bean).start();
                    case "G" -> new CreazioneGruppoCLI(bean).start();
                    case "S" -> new SegnalaStrutturaCLI(bean).start();
                    case "L" -> {
                        System.out.println("Logout effettuato. Arrivederci!"); //NOSONAR
                        exit = true; 
                    }
                    default -> System.out.println("Scelta non valida."); //NOSONAR
                }
            }
        }
    }

    private void gestioneSceltaChat(String scelta, List<BeanGruppo> gruppi) {
        int indice = Integer.parseInt(scelta) - 1;
        if (gruppi != null && indice >= 0 && indice < gruppi.size()) {
            BeanGruppo selezionato = gruppi.get(indice);
            System.out.println("\n--- Apertura Chat: " + selezionato.getNome() + " ---"); //NOSONAR
            new ChatCLI(bean, selezionato).start();
        } else {
            System.out.println("Indice gruppo non valido."); //NOSONAR
        }
    }

}

