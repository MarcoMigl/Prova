package appolloni.migliano.controller;



import appolloni.migliano.ClassiDiErrori.CampiVuotiException;
import appolloni.migliano.bean.BeanGruppo;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.entity.Gruppo;
import appolloni.migliano.entity.Studente;
import appolloni.migliano.entity.Utente;
import appolloni.migliano.factory.FactoryDAO;
import appolloni.migliano.interfacce.InterfacciaDaoStruttura;
import appolloni.migliano.interfacce.InterfacciaGruppo;
import appolloni.migliano.interfacce.InterfacciaUtente;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerCreazioneGruppo {

    private InterfacciaUtente dao = FactoryDAO.getDaoUtente();
    private InterfacciaGruppo dao_gruppo = FactoryDAO.getDaoGruppo();
    private InterfacciaDaoStruttura dao_strutture = FactoryDAO.getDAOStrutture();

    public List<String> getListaStruttureDisponibili(String citta) throws CampiVuotiException, Exception{
    try {
        if(citta == null || citta.trim().isEmpty()) throw new CampiVuotiException(citta);
        List<String> lista = new ArrayList<>();

        try {
          lista = dao_strutture.recuperaNomiStrutture(citta);
        }catch(IOException e){
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        
        return lista;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return new ArrayList<>();
}

    public void creaGruppo(BeanUtenti bean, BeanGruppo beanGruppo) throws Exception, CampiVuotiException {

        if(!bean.getTipo().equals("Studente")){
             throw new Exception("L'utente non ha i permessi");
        }
        
         if (beanGruppo.getNome() == null || beanGruppo.getNome().isEmpty()) {
            throw new Exception("Nome gruppo obbligatorio.");
         }

         if(beanGruppo.getAdmin().isEmpty() || beanGruppo.getCitta().isEmpty() || beanGruppo.getLuogo().isEmpty() || beanGruppo.getMateria().isEmpty()){

            throw new CampiVuotiException("Dati mancanti, inserire tutti i campi");
         }
         Utente u1 = dao.cercaUtente(bean.getEmail());

         if(u1 == null){

            throw new Exception("Utente admin non trovato");
         }
 
         Gruppo gruppo = null;
         gruppo = new Gruppo(beanGruppo.getNome(),u1);
         gruppo.setMateria(beanGruppo.getMateria());
         gruppo.setCitta(beanGruppo.getCitta());
         gruppo.setLuogo(beanGruppo.getLuogo());
         if(u1 instanceof Studente){
          ((Studente)u1).addGruppo(gruppo);
         }
         gruppo.aggiungiMembro(u1, u1);
         try{
          dao_gruppo.creaGruppo(gruppo);
         }catch(SQLException exception){
           exception.printStackTrace();
           throw exception;
           
         }


    }

    public List<BeanGruppo> visualizzaGruppi(BeanUtenti utenteLoggato) throws SQLException, Exception {
        List<BeanGruppo> listaBeans = new ArrayList<>();

        try {
           
            List<Gruppo> listaEntities = dao_gruppo.recuperaGruppiUtente(utenteLoggato.getEmail());
            
            for (Gruppo gruppo : listaEntities) {
                BeanGruppo bean = new BeanGruppo(
                    gruppo.getNome(),
                    gruppo.getMateria(),
                    gruppo.getAdmin().getEmail(),
                    gruppo.getLuogo(),
                    gruppo.getCitta()
                );
                
                if (gruppo.getAdmin() != null) {
                    bean.setAdmin(gruppo.getAdmin().getEmail());
                }
                
                listaBeans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return listaBeans;
    }




}
