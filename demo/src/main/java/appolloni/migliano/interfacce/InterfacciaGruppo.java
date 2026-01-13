package appolloni.migliano.interfacce;

import java.sql.SQLException;
import java.util.List;

import appolloni.migliano.entity.Gruppo;

public interface InterfacciaGruppo {
    void creaGruppo(Gruppo gruppo) throws SQLException;
    Gruppo cercaGruppo(String nome) throws SQLException;
    List<Gruppo> recuperaGruppiUtente(String email) throws SQLException;
    void iscriviUtente(String nomeGruppo, String emailUtente) throws SQLException;
    boolean esisteGruppo(String nome) throws SQLException;
    List<Gruppo> ricercaGruppiConFiltri(String nome, String citta, String materia) throws SQLException;
    void abbandonaGruppo(String nomeGruppo, String emailUtente) throws SQLException;
    void eliminaGruppo(String nomeGruppo) throws SQLException;

}
