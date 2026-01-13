package appolloni.migliano.factory;

import java.sql.SQLException;

import appolloni.migliano.Configurazione;
import appolloni.migliano.DAO.DaoGruppo.DaoGruppoDB;
import appolloni.migliano.DAO.DaoGruppo.DaoGruppoDemo;
import appolloni.migliano.DAO.DaoMessaggi.DaoMessaggioDB;
import appolloni.migliano.DAO.DaoMessaggi.DaoMessaggioDemo;
import appolloni.migliano.DAO.DaoRecensioni.DaoRecensioniDB;
import appolloni.migliano.DAO.DaoRecensioni.DaoRecensioniDemo;
import appolloni.migliano.DAO.DaoRecensioni.DaoRecensioniFile;
import appolloni.migliano.DAO.DaoStrutture.DAOStruttureDB;
import appolloni.migliano.DAO.DaoStrutture.DAOStruttureDemo;
import appolloni.migliano.DAO.DaoStrutture.DAOStruttureFILE;
import appolloni.migliano.DAO.DaoUtente.DaoUtenteDB;
import appolloni.migliano.DAO.DaoUtente.DaoUtenteDEMO;
import appolloni.migliano.DBConnection;
import appolloni.migliano.interfacce.InterfacciaDaoRecensioni;
import appolloni.migliano.interfacce.InterfacciaDaoStruttura;
import appolloni.migliano.interfacce.InterfacciaGruppo;
import appolloni.migliano.interfacce.InterfacciaMessaggi;
import appolloni.migliano.interfacce.InterfacciaUtente;


public class FactoryDAO {
    private static final String tipo = Configurazione.getTipoPersistenza();

    public static InterfacciaDaoStruttura getDAOStrutture() {
    try {
        if(tipo.equals(Configurazione.JDBC)){
            return new DAOStruttureDB(DBConnection.getConnection());
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Qui potresti decidere di lanciare una RuntimeException per non 
        // dover aggiungere 'throws' ovunque, ma segnalare comunque l'errore
        throw new RuntimeException("Errore critico di connessione al DB", e);
    }

    if(tipo.equals(Configurazione.DEMO)){
        return new DAOStruttureDemo();
    } else {
        return new DAOStruttureFILE();
    }
}

    public static InterfacciaDaoRecensioni getDaoRecensioni() {
        try {
            if (tipo.equals(Configurazione.JDBC)) {
            return new DaoRecensioniDB(DBConnection.getConnection());
            }
        } catch (SQLException e) {
       e.printStackTrace();
       throw new RuntimeException("Errore critico di connessione al DB", e);
    }

    if(tipo.equals(Configurazione.DEMO)){
            return new DaoRecensioniDemo();
        } else {
            return new DaoRecensioniFile();
        }
}
    
    public static InterfacciaGruppo getDaoGruppo(){
        try {
            if(tipo.equals(Configurazione.JDBC)){
            return new DaoGruppoDB(DBConnection.getConnection());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore di connesione al DB", e);
        }
                return new DaoGruppoDemo();
            
        
    }

    public static InterfacciaMessaggi getDaoMessaggi(){
        try {
            if(tipo.equals(Configurazione.JDBC)){
            return new DaoMessaggioDB(DBConnection.getConnection());

        }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore di connessione al DB", e);
        }
            return new DaoMessaggioDemo();

        }
    

    public static InterfacciaUtente getDaoUtente(){
        try {
            if(tipo.equals(Configurazione.JDBC)){
            return new DaoUtenteDB(DBConnection.getConnection());
        }
            
        } catch (SQLException e) {
        }
            return new DaoUtenteDEMO();
        
    }
}

