package appolloni.migliano.factory;

import java.sql.Connection;
import java.sql.SQLException;

import appolloni.migliano.Configurazione;
import appolloni.migliano.DBConnection;
import appolloni.migliano.dao.gruppo.*;
import appolloni.migliano.dao.messaggio.DaoMessaggioDB;
import appolloni.migliano.dao.messaggio.DaoMessaggioDemo;
import appolloni.migliano.dao.recensione.DaoRecensioniDB;
import appolloni.migliano.dao.recensione.DaoRecensioniDemo;
import appolloni.migliano.dao.recensione.DaoRecensioniFile;
import appolloni.migliano.dao.strutture.DAOStruttureDB;
import appolloni.migliano.dao.strutture.DAOStruttureDemo;
import appolloni.migliano.dao.strutture.DAOStruttureFILE;
import appolloni.migliano.dao.utente.DaoUtenteDB;
import appolloni.migliano.dao.utente.DaoUtenteDEMO;
import appolloni.migliano.interfacce.InterfacciaDaoRecensioni;
import appolloni.migliano.interfacce.InterfacciaDaoStruttura;
import appolloni.migliano.interfacce.InterfacciaGruppo;
import appolloni.migliano.interfacce.InterfacciaMessaggi;
import appolloni.migliano.interfacce.InterfacciaUtente;

public class FactoryDAO {
    
    
    private static final String TIPO = Configurazione.getTipoPersistenza();
    private static final String ERRMSG = "Impossibile connettersi al Database";

    private FactoryDAO() {
        throw new IllegalStateException("Utility class");
    }

    public static InterfacciaDaoStruttura getDAOStrutture() {
        if (Configurazione.JDBC.equals(TIPO)) {
            return new DAOStruttureDB(getConn());
        } else if (Configurazione.DEMO.equals(TIPO)) {
            return new DAOStruttureDemo();
        } else {
            return new DAOStruttureFILE();
        }
    }

    public static InterfacciaDaoRecensioni getDaoRecensioni() {
        if (Configurazione.JDBC.equals(TIPO)) {
            return new DaoRecensioniDB(getConn());
        } else if (Configurazione.DEMO.equals(TIPO)) {
            return new DaoRecensioniDemo();
        } else {
            return new DaoRecensioniFile();
        }
    }
    
    public static InterfacciaGruppo getDaoGruppo() {
        if (Configurazione.JDBC.equals(TIPO)) {
            return new DaoGruppoDB(getConn());
        } else {
            return new DaoGruppoDemo();
        }
    }

    public static InterfacciaMessaggi getDaoMessaggi() {
        if (Configurazione.JDBC.equals(TIPO)) {
            return new DaoMessaggioDB(getConn());
        } else {
            return new DaoMessaggioDemo();
        }
    }

    public static InterfacciaUtente getDaoUtente() {
        if (Configurazione.JDBC.equals(TIPO)) {
            return new DaoUtenteDB(getConn());
        } else {
            return new DaoUtenteDEMO();
        }
    }


    private static Connection getConn() {
        try {
            return DBConnection.getConnection();
        } catch (SQLException e) {
          
            throw new IllegalAccessError(ERRMSG);
        }
    }
}