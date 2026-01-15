package appolloni.migliano.dao.recensione;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import appolloni.migliano.entity.Recensione;
import appolloni.migliano.interfacce.InterfacciaDaoRecensioni;

public class DaoRecensioniDemo implements InterfacciaDaoRecensioni {

    private static final List<Recensione> tabellaRecensioni = new ArrayList<>();

    @Override
    public void salvaRecensione(Recensione r) throws IOException {
        tabellaRecensioni.add(r);
    }

    @Override
    public List<Recensione> getRecensioniByStruttura(String nomeStr, String gestore) throws IOException {
        List<Recensione> risultati = new ArrayList<>();

        for (Recensione r : tabellaRecensioni) {
            
    
            boolean nomeMatch = r.getStrutturaRecensita().getName().equalsIgnoreCase(nomeStr);
            boolean gestoreMatch = r.getStrutturaRecensita().getGestore().equalsIgnoreCase(gestore);

            if (nomeMatch && gestoreMatch) {
                risultati.add(r);
            }
        }
        
        return risultati;
    }
}
