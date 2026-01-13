package appolloni.migliano.controller;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



import appolloni.migliano.bean.BeanGruppo;
import appolloni.migliano.bean.BeanMessaggi;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.entity.Gruppo;
import appolloni.migliano.entity.Messaggio;
import appolloni.migliano.entity.Utente;
import appolloni.migliano.factory.FactoryDAO;
import appolloni.migliano.factory.FactoryMessaggi;
import appolloni.migliano.interfacce.InterfacciaGruppo;
import appolloni.migliano.interfacce.InterfacciaMessaggi;
import appolloni.migliano.interfacce.InterfacciaUtente;

public class ControllerChat {

    private InterfacciaGruppo daoGruppo;
    private InterfacciaMessaggi daoMessaggi;
    private InterfacciaUtente daoUtente;

    public ControllerChat() {
        this.daoGruppo = FactoryDAO.getDaoGruppo();
        this.daoMessaggi = FactoryDAO.getDaoMessaggi();
        this.daoUtente = FactoryDAO.getDaoUtente();
    }
    public BeanGruppo recuperaInfoGruppo(BeanGruppo beanInput) throws Exception, SQLException{
        
    try{
        Gruppo g = daoGruppo.cercaGruppo(beanInput.getNome());
        
        if (g == null) throw new Exception("Gruppo non esistente");

        
         BeanGruppo gruppo = new BeanGruppo(g.getNome(),g.getMateria(),g.getAdmin().getName(),g.getLuogo(),g.getMateria());
         return gruppo;
     }catch(SQLException e){
        throw new SQLException("Errore recupero dati");

     }
    }
    public List<BeanMessaggi> recuperaMessaggi(BeanGruppo beanGruppo) throws SQLException, Exception {
        List<BeanMessaggi> listaBeans = new ArrayList<>();

     try{
        Gruppo entityGruppo = daoGruppo.cercaGruppo(beanGruppo.getNome());
        
        if (entityGruppo != null) {
            List<Messaggio> listaEntity = daoMessaggi.cercaMessaggio(entityGruppo);
            
            for (Messaggio m : listaEntity) {
                BeanMessaggi b = new BeanMessaggi();
                b.setMess(m.getMess());
                String nomeMittente = (m.getMittente() != null) ? m.getMittente().getName() : "Sconosciuto";
                b.setMittente(nomeMittente); 
                
                listaBeans.add(b);
            }
        }
        return listaBeans;
     }catch(SQLException e){
        throw new SQLException("Errore caricamento dei dati");
     }catch(Exception e){
        throw e;
     }
    }

    public void inviaMessaggio(BeanUtenti mittente, BeanGruppo gruppo, String testo) throws SQLException, Exception {
        if(testo == null || testo.trim().isEmpty()) return;

      try{

        Utente user = daoUtente.cercaUtente(mittente.getEmail());
        Gruppo g = daoGruppo.cercaGruppo(gruppo.getNome());

        Messaggio messaggio = FactoryMessaggi.creaMessaggio(testo, g, user);
        daoMessaggi.nuovoMessaggio(messaggio);
    
      }catch(SQLException e){

        throw new SQLException("Errore caricamento dei dati");
      }catch(Exception e){
        throw e;
      }
    }

    public void abbandonaGruppo(BeanUtenti utente, BeanGruppo gruppo) throws SQLException, Exception {

      try{
        Utente user = daoUtente.cercaUtente(utente.getEmail());
        Gruppo gruppo_utente = daoGruppo.cercaGruppo(gruppo.getNome());
        if(utente.getEmail().equals(gruppo.getAdmin())){

            daoGruppo.eliminaGruppo(gruppo_utente.getNome());

        }else{
            daoGruppo.abbandonaGruppo(gruppo_utente.getNome(), user.getEmail());
        }
     
     }catch(SQLException e){
        throw new SQLException("Errore caricamento dei dati.");
     }catch(Exception e){
        throw e;
     }
    }
}
