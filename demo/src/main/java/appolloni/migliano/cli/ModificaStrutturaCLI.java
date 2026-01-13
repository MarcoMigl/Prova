package appolloni.migliano.cli;

import java.util.Scanner;

import appolloni.migliano.HelperErrori;
import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneStrutture;

public class ModificaStrutturaCLI {

    private final Scanner scanner;
    private final BeanStruttura strutturaCorrente;
    private final String vecchioNome;
    private final ControllerGestioneStrutture controllerApp;

    public ModificaStrutturaCLI(BeanUtenti host, BeanStruttura struttura) {
        this.scanner = new Scanner(System.in);
        this.strutturaCorrente = struttura;
        this.vecchioNome = struttura.getName(); 
        this.controllerApp = new ControllerGestioneStrutture();
    }

    public void start() {
        System.out.println("\n========================================");
        System.out.println("       MODIFICA DELLA STRUTTURA        ");
        System.out.println("========================================");
        System.out.println("Lascia vuoto e premi Invio per mantenere il valore attuale.");

        try {
            // 1. Modifica Nome
            System.out.print("Nome attuale [" + strutturaCorrente.getName() + "]: ");
            String nuovoNome = scanner.nextLine().trim();
            if (!nuovoNome.isEmpty()) strutturaCorrente.setName(nuovoNome);

            // 2. Modifica Indirizzo
            System.out.print("Indirizzo attuale [" + strutturaCorrente.getIndirizzo() + "]: ");
            String nuovoIndirizzo = scanner.nextLine().trim();
            if (!nuovoIndirizzo.isEmpty()) strutturaCorrente.setIndirizzo(nuovoIndirizzo);

            // 3. Modifica Città
            System.out.print("Città attuale [" + strutturaCorrente.getCitta() + "]: ");
            String nuovaCitta = scanner.nextLine().trim();
            if (!nuovaCitta.isEmpty()) strutturaCorrente.setCitta(nuovaCitta);

            // 4. Modifica Orario
            System.out.print("Orario attuale [" + strutturaCorrente.getOrario() + "]: ");
            String nuovoOrario = scanner.nextLine().trim();
            if (!nuovoOrario.isEmpty()) strutturaCorrente.setOrario(nuovoOrario);

            // 5. Modifica Wifi
            strutturaCorrente.setWifi(chiediModificaBoolean("WiFi", strutturaCorrente.hasWifi()));

            // 6. Modifica Ristorazione
            strutturaCorrente.setRistorazione(chiediModificaBoolean("Ristorazione", strutturaCorrente.hasRistorazione()));

            String fotoAttuale = strutturaCorrente.getFoto();
            System.out.print("Percorso foto attuale [" + (fotoAttuale != null && !fotoAttuale.isEmpty() ? fotoAttuale : "Nessuna") + "]: ");
            String nuovaFoto = scanner.nextLine().trim();
            
            if (!nuovaFoto.isEmpty()) {
                strutturaCorrente.setFoto(nuovaFoto);
            } 
            
            if (strutturaCorrente.getName() == null || strutturaCorrente.getName().isEmpty()) {
                HelperErrori.errore("Il nome non può essere vuoto!","Inserire il nome");
                return;
            }

            // SALVATAGGIO
            controllerApp.aggiornaStruttura(strutturaCorrente, vecchioNome);
            System.out.println("\n Struttura aggiornata con successo!");

        } catch (Exception e) {
             HelperErrori.errore("Errore salvataggio:", e.getMessage());
        }
    }

    /**
     * Helper per gestire la modifica dei valori boolean
     */
    private boolean chiediModificaBoolean(String campo, boolean valoreAttuale) {
        String stato = valoreAttuale ? "SI" : "NO";
        System.out.print(campo + " attuale [" + stato + "]. Cambiare? (s/n): ");
        String risp = scanner.nextLine().trim().toLowerCase();
        
        if (risp.equals("s") || risp.equals("si")) {
            return !valoreAttuale; // Inverte il valore attuale
        }
        return valoreAttuale; // Mantiene il valore attuale (es. se preme solo Invio)
    }
}