package appolloni.migliano;

import appolloni.migliano.cli.HomeCLI;

public class MainCLI {
    public static void main(String[] args) {
        System.out.println("Avvio applicazione in modalità CLI...");

        try {
            // 1. Inizializza la connessione al DB (fondamentale!)
            // Assicurati che il tuo metodo si chiami così
            DBConnection.getConnection(); 

            // 2. Lancia la prima schermata
            HomeCLI home = new HomeCLI();
            home.start();

        } catch (Exception e) {
            System.err.println("Errore fatale durante l'avvio: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 3. Chiudi la connessione alla chiusura dell'app
            DBConnection.closeConnection();
            System.out.println("Connessione DB chiusa. Applicazione terminata.");
        }
    }
}