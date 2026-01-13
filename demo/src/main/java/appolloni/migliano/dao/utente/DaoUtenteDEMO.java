package appolloni.migliano.dao.utente;

import appolloni.migliano.interfacce.InterfacciaUtente;

import java.util.ArrayList;
import java.util.List;
import appolloni.migliano.entity.Utente;


public class DaoUtenteDEMO implements InterfacciaUtente {
  private static List<Utente> tabellaUtenti = new ArrayList<>();

   
   

    @Override
    public void salvaUtente(Utente u) {
        
        tabellaUtenti.add(u);
    }

    @Override
    public Utente cercaUtente(String search) {
    
        for (Utente u : tabellaUtenti) {
            if (u.getEmail().equals(search)) {
                return u;
            }
        }
        return null; 
    }

    @Override
    public void aggiornaPassword(String email, String nuovaPass)  {
        for (Utente u : tabellaUtenti) {
            if (u.getEmail().equals(email)) {
                u.seTPass(nuovaPass);
                return;
            }
        }
    }
}
