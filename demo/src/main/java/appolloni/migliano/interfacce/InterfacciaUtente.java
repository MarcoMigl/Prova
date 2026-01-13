package appolloni.migliano.interfacce;

import java.sql.SQLException;

import appolloni.migliano.entity.Utente;

public interface InterfacciaUtente {

    void salvaUtente(Utente u) throws SQLException;
    Utente cercaUtente(String email) throws SQLException;
    void aggiornaPassword(String email, String nuovaPass) throws SQLException;
    
}
