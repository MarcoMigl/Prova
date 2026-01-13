package appolloni.migliano.DAO.DaoMessaggi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import appolloni.migliano.entity.Gruppo;
import appolloni.migliano.entity.Messaggio;
import appolloni.migliano.interfacce.InterfacciaMessaggi;

public class DaoMessaggioDemo implements InterfacciaMessaggi{
    private static List<Messaggio> tabellaMessaggi = new ArrayList<>();

    @Override
    public void nuovoMessaggio(Messaggio messaggio) throws SQLException {
        tabellaMessaggi.add(messaggio);
    }

    @Override
    public List<Messaggio> cercaMessaggio(Gruppo gruppo) throws SQLException {
        List<Messaggio> risultato = new ArrayList<>();

       
        for (Messaggio m : tabellaMessaggi) {
            
            if (m.getGruppo().getNome().equals(gruppo.getNome())) {
                risultato.add(m);
            }
        }

 
        Collections.sort(risultato, new Comparator<Messaggio>() {
            @Override
            public int compare(Messaggio m1, Messaggio m2) {
                if(m1.getTime() == null || m2.getTime() == null) return 0;
                return m1.getTime().compareTo(m2.getTime());
            }
        });

        return risultato;
    }
    
}
