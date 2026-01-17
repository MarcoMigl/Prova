package appolloni.migliano;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import appolloni.migliano.factory.FactoryUI;
import appolloni.migliano.interfacce.InterfacciaGrafica;

public class Launcher {
 private static final Logger logger = Logger.getLogger(Launcher.class.getName());
    public static void main(String[] args) {
        

        String tipoPersistenza = Configurazione.JDBC;
        String tipoInterfaccia = "GUI";
        
        if (args.length > 0) {
   
            if (args[0].equalsIgnoreCase("FILE")) {
                tipoPersistenza = Configurazione.FILE;
            }else if(args[0].equalsIgnoreCase("DEMO")){
                tipoPersistenza = Configurazione.DEMO;
            }
            if (args.length > 1 && (args[1].equalsIgnoreCase("CLI"))) {
                    tipoInterfaccia = "CLI";
            }
        }


        Configurazione.setTipoPersistenza(tipoPersistenza);
        Configurazione.setTipoInterfaccia(tipoInterfaccia);

        logger.log(Level.INFO, ">>> MODALITÀ AVVIO: {0} | INTERFACCIA: {1} <<<", new Object[]{tipoPersistenza, tipoInterfaccia});
        try {
            if(tipoPersistenza.equals(Configurazione.JDBC)){

                connessioneDB();
            }
            
            InterfacciaGrafica ui = FactoryUI.getInterfaccia();
            
            ui.avvia(args);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore critico durante l'avvio dell'applicazione", e);
        } finally {
            if(tipoPersistenza.equals(Configurazione.JDBC)){
             chiudiConnessione();
         }
        }
    }


    private static void connessioneDB() throws SQLException{
        DBConnection.getInstance().getConnection(); 
        logger.info("Connessione al Database stabilita.");
    }
    


    private static void chiudiConnessione() {

        try {
            DBConnection.getInstance().closeConnection();
             logger.info("Connessione al Database chiusa.");
            } catch (Exception e) {
             logger.warning("Errore durante la chiusura della connessione DB.");
            }
    }
}