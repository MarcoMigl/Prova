package appolloni.migliano.cli;

import java.util.Scanner;

import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneStrutture;

public class ModificaStrutturaCLI {

    private final Scanner scanner;
    private final BeanStruttura strutturaCorrente;
    private final String vecchioNome;
    private final ControllerGestioneStrutture controllerApp;
    private final BeanUtenti host;

    public ModificaStrutturaCLI(BeanUtenti host, BeanStruttura struttura) {
        this.scanner = new Scanner(System.in);
        this.strutturaCorrente = struttura;
        this.vecchioNome = struttura.getName(); 
        this.controllerApp = new ControllerGestioneStrutture();
        this.host = host;
    }

    public void start() {
        System.out.println("\n========================================"); //NOSONAR
        System.out.println("       MODIFICA DELLA STRUTTURA        "); //NOSONAR
        System.out.println("========================================"); //NOSONAR
        System.out.println("Ciao " + host.getName() + ", stai modificando la tua struttura."); //NOSONAR
        System.out.println("Lascia vuoto e premi Invio per mantenere il valore attuale."); //NOSONAR

        try {
            // 1. Modifica Nome
            System.out.print("Nome attuale [" + strutturaCorrente.getName() + "]: "); //NOSONAR
            String nuovoNome = scanner.nextLine().trim();
            if (!nuovoNome.isEmpty()) strutturaCorrente.setName(nuovoNome);

            // 2. Modifica Indirizzo
            System.out.print("Indirizzo attuale [" + strutturaCorrente.getIndirizzo() + "]: "); //NOSONAR
            String nuovoIndirizzo = scanner.nextLine().trim();
            if (!nuovoIndirizzo.isEmpty()) strutturaCorrente.setIndirizzo(nuovoIndirizzo);

            // 3. Modifica Città
            System.out.print("Città attuale [" + strutturaCorrente.getCitta() + "]: "); //NOSONAR
            String nuovaCitta = scanner.nextLine().trim();
            if (!nuovaCitta.isEmpty()) strutturaCorrente.setCitta(nuovaCitta);

            // 4. Modifica Orario
            System.out.print("Orario attuale [" + strutturaCorrente.getOrario() + "]: "); //NOSONAR
            String nuovoOrario = acquisisciOrario();
            if (!nuovoOrario.isEmpty()) strutturaCorrente.setOrario(nuovoOrario);

            // 5. Modifica Wifi
            strutturaCorrente.setWifi(chiediModificaBoolean("WiFi", strutturaCorrente.hasWifi()));

            // 6. Modifica Ristorazione
            strutturaCorrente.setRistorazione(chiediModificaBoolean("Ristorazione", strutturaCorrente.hasRistorazione()));

            System.out.print("Foto non disponibili in versione CLI. "); //NOSONAR
            
            
            if (strutturaCorrente.getName() == null || strutturaCorrente.getName().isEmpty()) {
                System.out.println("Il nome non può essere vuoto! Inserire il nome"); //NOSONAR
                return;
            }

            // SALVATAGGIO
            controllerApp.aggiornaStruttura(strutturaCorrente, vecchioNome);
            System.out.println("\n Struttura aggiornata con successo!"); //NOSONAR

        } catch (Exception e) {
             System.out.println("Errore salvataggio"); //NOSONAR
        }
    }


    private String acquisisciOrario() {
        String regex = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]-([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";
        String input;
        while (true) {
            input = scanner.nextLine().trim();
            if(input.isEmpty()){
                 return input;
            }else{
            if (input.matches(regex)) {
                String[] parti = input.split("-");
                String[] inizio = parti[0].split(":");
                String[] fine = parti[1].split(":");
                
                int minutiInizio = Integer.parseInt(inizio[0]) * 60 + Integer.parseInt(inizio[1]);
                int minutiFine = Integer.parseInt(fine[0]) * 60 + Integer.parseInt(fine[1]);
                
                if (minutiFine > minutiInizio) {
                    return input;
                } else {
                    System.out.println("[ERRORE] L'orario di chiusura deve essere successivo a quello di apertura."); //NOSONAR
                }
            } else {
                System.out.println("[ERRORE] Formato non valido. Usa HH:mm-HH:mm (es. 09:00-18:00)."); //NOSONAR
            }
        }
    }
    }
    /**
     * Helper per gestire la modifica dei valori boolean
     */
    private boolean chiediModificaBoolean(String campo, boolean valoreAttuale) {
        String stato = valoreAttuale ? "SI" : "NO";
        System.out.print(campo + " attuale [" + stato + "]. Cambiare? (s/n): "); //NOSONAR
        String risp = scanner.nextLine().trim().toLowerCase();
        
        if (risp.equals("s") || risp.equals("si")) {
            return !valoreAttuale; // Inverte il valore attuale
        }
        return valoreAttuale; // Mantiene il valore attuale (es. se preme solo Invio)
    }

}

