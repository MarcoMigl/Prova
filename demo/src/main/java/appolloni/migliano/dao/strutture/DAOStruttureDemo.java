package appolloni.migliano.dao.strutture;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import appolloni.migliano.entity.Struttura;
import appolloni.migliano.interfacce.InterfacciaDaoStruttura;

public class DAOStruttureDemo implements InterfacciaDaoStruttura {


    private static final List<Struttura> tabellaStrutture = new ArrayList<>();


    @Override
    public void salvaStruttura(Struttura s, String email) throws SQLException {
     
        if (email != null && (s.getGestore() == null || s.getGestore().isEmpty())) {
           throw new SQLException("Errore");
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
    public List<Struttura> ricercaStruttureConFiltri(String nome, String citta, String tipo) {
    List<Struttura> risultati = new ArrayList<>();
    
     for (Struttura s : tabellaStrutture) {
        if (soddisfaFiltri(s, nome, citta, tipo)) {
            risultati.add(s);
        }
     }
     return risultati;
    }

    private boolean soddisfaFiltri(Struttura s, String nome, String citta, String tipo) {
     if (nome != null && !nome.isEmpty() && !s.getName().toLowerCase().contains(nome.toLowerCase())) {
        return false;
     }
     if (citta != null && !citta.isEmpty() && !s.getCitta().toLowerCase().contains(citta.toLowerCase())) {
         return false;
     }
     return !(tipo != null && !tipo.isEmpty() && !s.getTipoAttivita().equalsIgnoreCase(tipo)) ;
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

    @Override
    public void aggiornaHost(Struttura strutturaNuova, String gestore) throws IOException {
        for (Struttura s : tabellaStrutture) {
            if (s.getGestore().equalsIgnoreCase("system_no_host") && 
                s.getName().equalsIgnoreCase(strutturaNuova.getName())) {
                
                s.setGestore(gestore);
                s.setIndirizzo(strutturaNuova.getIndirizzo());
                s.setOrario(strutturaNuova.getOrario());
                s.setWifi(strutturaNuova.hasWifi());
                s.setRistorazione(strutturaNuova.hasRistorazione());
                s.setFoto(strutturaNuova.getFoto());
                return; 
            }
        }
    }
}