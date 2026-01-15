package appolloni.migliano.interfacce;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import appolloni.migliano.entity.Struttura;
public interface InterfacciaDaoStruttura {

     void salvaStruttura(Struttura struttura2, String email) throws SQLException,IOException;
     Struttura cercaStruttura(String nome, String gestore) throws SQLException, IOException;
     List<Struttura> ricercaStruttureConFiltri(String nome,String citta, String tipo) throws SQLException,IOException;
     Struttura recuperaStrutturaPerHost(String email) throws SQLException, IOException;
     void updateStruttura(Struttura struttura, String vecchioNome) throws SQLException, IOException;
     List<String> recuperaNomiStrutture(String citta) throws SQLException,IOException;
     void aggiornaFotoStruttura(String emailHost, String fotoNuova) throws SQLException, IOException;
     void aggiornaHost(Struttura struttura, String nuovaEmail) throws SQLException, IOException;
     
     

    
}