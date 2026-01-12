package appolloni.migliano.controller;

import java.sql.SQLException;
import appolloni.migliano.ClassiDiErrori.CampiVuotiException;
import appolloni.migliano.ClassiDiErrori.EmailNonValidaException;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.factory.FactoryDAO;
import appolloni.migliano.factory.FactoryUtenti;
import appolloni.migliano.interfacce.InterfacciaUtente;
import appolloni.migliano.entity.Utente;

public class ControllerGestioneUtente{

    private InterfacciaUtente daoUtente = FactoryDAO.getDaoUtente();
    

    public void creazioneUtente(BeanUtenti bean) throws IllegalArgumentException, Exception, CampiVuotiException, EmailNonValidaException{
        String type = bean.getTipo(); 
        String nome = bean.getName();
        String cognome = bean.getCognome(); 
        String email = bean.getEmail(); 
        String citta = bean.getCitta(); 
        String password = bean.getPassword();
        
        if(type.isEmpty() || nome.isEmpty()|| cognome.isEmpty()|| email.isEmpty()|| citta.isEmpty()|| password.isEmpty()){
            throw new CampiVuotiException("Errore: Inserire i dati in tutti i campi.");
        }

        if(!email.contains("@")){
            throw new EmailNonValidaException("Formato email non valido");
        }

        try{
         Utente esistente = daoUtente.cercaUtente(email);
        
         if(esistente != null){
            throw new Exception("Utente già esistente");
           }

          Utente utente = FactoryUtenti.Creazione(type, nome, cognome, email, citta,password);

        
          if(utente == null){
            throw new Exception("Creazione fallita");
         }


         if (bean.getTipo().equals("Host")){
           if (bean.getNomeAttivita() == null || bean.getNomeAttivita().isEmpty() || 
            bean.getTipoAttivita() == null || bean.getTipoAttivita().isEmpty()) {
            throw new CampiVuotiException("Errore: Dati attività mancanti per l'Host.");
           }
          }
         try{
          daoUtente.salvaUtente(utente);
         }catch(SQLException e){
            throw e;
         }
        }catch(SQLException e){
         throw e;
        } 


    }

    public BeanUtenti recuperaInformazioniUtenti(BeanUtenti utente) throws SQLException, Exception{

        try{

         Utente user = daoUtente.cercaUtente(utente.getEmail());

         BeanUtenti bean = new BeanUtenti(user.getTipo(), user.getName(), user.getCognome(), user.getEmail(), user.getPass(), user.getCitta());
        
         return bean;
        }catch(SQLException e){
            e.printStackTrace();
            throw e;

        }catch(Exception e){
            e.printStackTrace();
            throw e;

        }

    }

    public boolean modificaPassword(String vecchiaPass, String nuovaPass, BeanUtenti utente) throws SQLException, Exception{
        
        try{
         Utente user = daoUtente.cercaUtente(utente.getEmail());

         if(user.getPass().equals(vecchiaPass)){
            daoUtente.aggiornaPassword(user.getEmail(), nuovaPass);
            return true;
         }
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }catch(Exception e){
            e.printStackTrace();
            throw e;

        }

        return false;

    }

    
    
}
