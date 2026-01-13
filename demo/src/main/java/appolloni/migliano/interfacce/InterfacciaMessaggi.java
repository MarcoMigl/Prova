package appolloni.migliano.interfacce;

import appolloni.migliano.entity.Gruppo;
import appolloni.migliano.entity.Messaggio;

import java.sql.SQLException;
import java.util.List;

public interface InterfacciaMessaggi {
    
    void nuovoMessaggio(Messaggio messaggio) throws SQLException;
    List<Messaggio> cercaMessaggio(Gruppo gruppo) throws SQLException;

}