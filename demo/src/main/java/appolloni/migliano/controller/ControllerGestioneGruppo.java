package appolloni.migliano.controller;



import appolloni.migliano.bean.BeanGruppo;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.entity.Gruppo;
import appolloni.migliano.entity.Studente;
import appolloni.migliano.entity.Utente;
import appolloni.migliano.exception.CampiVuotiException;
import appolloni.migliano.factory.FactoryDAO;
import appolloni.migliano.factory.FactoryGruppo;
import appolloni.migliano.interfacce.InterfacciaDaoStruttura;
import appolloni.migliano.interfacce.InterfacciaGruppo;
import appolloni.migliano.interfacce.InterfacciaUtente;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerGestioneGruppo {

    private InterfacciaUtente dao = FactoryDAO.getDaoUtente();
    private InterfacciaGruppo daoGruppo = FactoryDAO.getDaoGruppo();
    private InterfacciaDaoStruttura daoStrutture = FactoryDAO.getDAOStrutture();

    public List<String> getListaStruttureDisponibili(String citta) throws CampiVuotiException, SQLException, IOException{
        if(citta == null || citta.trim().isEmpty()) throw new CampiVuotiException(citta);
        return daoStrutture.recuperaNomiStrutture(citta);
    }

    public void creaGruppo(BeanUtenti bean, BeanGruppo beanGruppo) throws SQLException, CampiVuotiException {

        if(!bean.getTipo().equals("Studente")){
             throw new SQLException("L'utente non ha i permessi");
        }
        
         if (beanGruppo.getNome() == null || beanGruppo.getNome().isEmpty()) {
            throw new SQLException("Nome gruppo obbligatorio.");
         }

         if(beanGruppo.getAdmin().isEmpty() || beanGruppo.getCitta().isEmpty() || beanGruppo.getLuogo().isEmpty() || beanGruppo.getMateria().isEmpty()){

            throw new CampiVuotiException("Dati mancanti, inserire tutti i campi");
         }
         Utente u1 = dao.cercaUtente(bean.getEmail());

         if(u1 == null){

            throw new SQLException("Utente admin non trovato");
         }
 
         Gruppo gruppo = FactoryGruppo.creaGruppo(beanGruppo.getNome(), u1);
         gruppo.setMateria(beanGruppo.getMateria());
         gruppo.setCitta(beanGruppo.getCitta());
         gruppo.setLuogo(beanGruppo.getLuogo());
         if(u1 instanceof Studente studente){
          studente.addGruppo(gruppo);
         }
         gruppo.aggiungiMembro(u1, u1);
          daoGruppo.creaGruppo(gruppo);
         


    }

    public List<BeanGruppo> visualizzaGruppi(BeanUtenti utenteLoggato) throws SQLException {
        List<BeanGruppo> listaBeans = new ArrayList<>();
           
            List<Gruppo> listaEntities = daoGruppo.recuperaGruppiUtente(utenteLoggato.getEmail());
            
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

        return listaBeans;
    }

    
     public void aggiungiGruppo(BeanUtenti beanUtenti, BeanGruppo gruppoScelto) throws SQLException{ 
         daoGruppo.iscriviUtente(gruppoScelto.getNome(), beanUtenti.getEmail());
        
    }
    
    
    public List<BeanGruppo> cercaGruppi(String nome, String citta, String materia) throws SQLException {
        if(nome != null && nome.isEmpty()) nome = null;
        if(citta != null && citta.isEmpty()) citta = null;
        if(materia != null && materia.isEmpty()) materia = null;

        
        List<BeanGruppo> list = new ArrayList<>();
        List<Gruppo> listGruppo = daoGruppo.ricercaGruppiConFiltri(nome, citta, materia);
        
        
        for(Gruppo g : listGruppo){
            BeanGruppo beanGruppo = new BeanGruppo(g.getNome(), g.getMateria(), g.getAdmin().getEmail(), g.getLuogo(),g.getCitta());
            list.add(beanGruppo);
        }
        return list;
    }



}
