package appolloni.migliano.dao.gruppo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import appolloni.migliano.entity.Gruppo;
import appolloni.migliano.interfacce.InterfacciaGruppo;

public class DaoGruppoDemo implements InterfacciaGruppo {

    private static List<Gruppo> gruppiDB = new ArrayList<>();
    private static List<String[]> iscrizioniDB = new ArrayList<>();

    @Override
    public void creaGruppo(Gruppo gruppo) throws SQLException {
        gruppiDB.add(gruppo);

        if (gruppo.getAdmin() != null) {
            String[] rigaIscrizione = {gruppo.getNome(), gruppo.getAdmin().getEmail()};
            iscrizioniDB.add(rigaIscrizione);
        }else{
            throw new SQLException("Impossibile creare il gruppo");
        }
    }

    @Override
    public Gruppo cercaGruppo(String nome) throws SQLException {
        for (Gruppo g : gruppiDB) {
            if (g.getNome().equals(nome)) {
                return g;
            }
        }
        throw new SQLException("Gruppo non trovato");

    }

    @Override
    public List<Gruppo> recuperaGruppiUtente(String emailUtente) throws SQLException {
        List<Gruppo> lista = new ArrayList<>();

        for (String[] riga : iscrizioniDB) {
            String nomeGruppo = riga[0];
            String emailIscritto = riga[1];

            if (emailIscritto.equals(emailUtente)) {
                Gruppo g = cercaGruppo(nomeGruppo);
                if (g != null) {
                    lista.add(g);
                }
            }
        }
        return lista;
    }

    @Override
    public void iscriviUtente(String nomeGruppo, String emailUtente) throws SQLException {
        String[] nuovaIscrizione = {nomeGruppo, emailUtente};
        iscrizioniDB.add(nuovaIscrizione);
    }

    @Override
    public boolean esisteGruppo(String nomeGruppo) throws SQLException {
            return cercaGruppo(nomeGruppo) != null;
              
        }

    @Override
    public List<Gruppo> ricercaGruppiConFiltri(String nome, String citta, String materia) throws SQLException {
        List<Gruppo> risultati = new ArrayList<>();

        for (Gruppo g : gruppiDB) {
            boolean match = true;

            if (nome != null && !g.getNome().contains(nome)) {
                match = false;
            }
   
            if (citta != null && !g.getCitta().equals(citta)) {
                match = false;
            }
            if (materia != null && !g.getMateria().contains(materia)) {
                match = false;
            }

            if (match) {
                risultati.add(g);
            }
        }
        return risultati;
    }

    @Override
    public void abbandonaGruppo(String nomeGruppo, String emailUtente) throws SQLException {
        Iterator<String[]> iter = iscrizioniDB.iterator();
        while (iter.hasNext()) {
            String[] riga = iter.next();
            if (riga[0].equals(nomeGruppo) && riga[1].equals(emailUtente)) {
                iter.remove();
            }
        }
    }

    @Override
    public void eliminaGruppo(String nomeGruppo) throws SQLException {
        gruppiDB.removeIf(g -> g.getNome().equals(nomeGruppo));
        iscrizioniDB.removeIf(riga -> riga[0].equals(nomeGruppo));
        
        // (Nota: se avessi una lista Messaggi, dovresti pulire anche quella qui)
    }
}