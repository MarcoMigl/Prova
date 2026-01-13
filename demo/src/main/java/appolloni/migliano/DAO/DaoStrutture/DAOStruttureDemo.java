package appolloni.migliano.DAO.DaoStrutture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import appolloni.migliano.ClassiDiErrori.CampiVuotiException;
import appolloni.migliano.entity.Struttura;
import appolloni.migliano.interfacce.InterfacciaDaoStruttura;

public class DAOStruttureDemo implements InterfacciaDaoStruttura {


    private static List<Struttura> tabellaStrutture = new ArrayList<>();

    public DAOStruttureDemo() {
    }

    @Override
    public void salvaStruttura(Struttura s, String email) throws CampiVuotiException {
     
        if (email != null && (s.getGestore() == null || s.getGestore().isEmpty())) {
           throw new CampiVuotiException("Dati mancanti");
        }
        
        tabellaStrutture.add(s);
    }

    @Override
    public Struttura cercaStruttura(String nomeStruttura, String gestore) throws IOException {
        for (Struttura s : tabellaStrutture) {
            if (s.getName().equals(nomeStruttura) && s.getGestore().equals(gestore)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public List<Struttura> ricercaStruttureConFiltri(String nome, String citta, String tipo) throws IOException {
        List<Struttura> risultati = new ArrayList<>();

        for (Struttura s : tabellaStrutture) {
            boolean match = true;
            if (nome != null && !nome.isEmpty()) {
                if (!s.getName().toLowerCase().contains(nome.toLowerCase())) {
                    match = false;
                }
            }
            if (match && citta != null && !citta.isEmpty()) {
                if (!s.getCitta().toLowerCase().contains(citta.toLowerCase())) {
                    match = false;
                }
            }
            if (match && tipo != null && !tipo.isEmpty()) {
                if (!s.getTipoAttivita().equalsIgnoreCase(tipo)) {
                    match = false;
                }
            }

            if (match) {
                risultati.add(s);
            }
        }
        return risultati;
    }

    @Override
    public Struttura recuperaStrutturaPerHost(String emailHost) throws IOException {
        for (Struttura s : tabellaStrutture) {
            if (s.getGestore().equalsIgnoreCase(emailHost)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public void updateStruttura(Struttura strutturaNuova, String vecchioNome) throws IOException {
        for (int i = 0; i < tabellaStrutture.size(); i++) {
            Struttura s = tabellaStrutture.get(i);
            
            if (s.getName().equalsIgnoreCase(vecchioNome)) {
                tabellaStrutture.set(i, strutturaNuova);
                return; 
            }
        }
    }

    @Override
    public void aggiornaFotoStruttura(String emailHost, String fotoNuova) throws IOException {
        for (Struttura s : tabellaStrutture) {
            if (s.getGestore().equalsIgnoreCase(emailHost)) {
                s.setFoto(fotoNuova);
                return;
            }
        }
    }

    @Override
    public List<String> recuperaNomiStrutture(String citta) throws IOException {
        List<String> nomi = new ArrayList<>();
        
        for (Struttura s : tabellaStrutture) {
            if (s.getCitta().trim().equalsIgnoreCase(citta.trim())) {
                nomi.add(s.getName());
            }
        }
        return nomi;
    }
}