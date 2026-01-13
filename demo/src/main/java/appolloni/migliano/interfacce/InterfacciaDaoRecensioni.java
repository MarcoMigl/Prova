package appolloni.migliano.interfacce;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import appolloni.migliano.entity.Recensione;

public interface InterfacciaDaoRecensioni {

    void salvaRecensione(Recensione rec) throws SQLException,IOException;
    List<Recensione> getRecensioniByStruttura(String nomeStruttura, String nomeGestore) throws SQLException,IOException;

    
}