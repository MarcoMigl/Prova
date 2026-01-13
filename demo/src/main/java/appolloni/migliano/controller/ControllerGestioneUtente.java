package appolloni.migliano.controller;

import java.io.IOException;
import java.sql.SQLException;

import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.factory.FactoryDAO;
import appolloni.migliano.factory.FactoryUtenti;
import appolloni.migliano.interfacce.InterfacciaUtente;
import appolloni.migliano.entity.Utente;
import appolloni.migliano.exception.CampiVuotiException;
import appolloni.migliano.exception.EmailNonValidaException;

public class ControllerGestioneUtente{

    private InterfacciaUtente daoUtente = FactoryDAO.getDaoUtente();
    

    public void creazioneUtente(BeanUtenti bean) throws IllegalArgumentException, CampiVuotiException, EmailNonValidaException,IOException, SQLException{
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
         Utente esistente = daoUtente.cercaUtente(email);
        
         if(esistente != null){
            throw new IllegalArgumentException("Utente già esistente");
           }

          Utente utente = FactoryUtenti.creazione(type, nome, cognome, email, citta,password);

        
          if(utente == null){
            throw new IOException("Creazione fallita");
         }


         if (bean.getTipo().equals("Host") &&(bean.getNomeAttivita() == null || bean.getNomeAttivita().isEmpty() || 
            bean.getTipoAttivita() == null || bean.getTipoAttivita().isEmpty())) {
            throw new CampiVuotiException("Errore: Dati attività mancanti per l'Host.");
           }
          daoUtente.salvaUtente(utente);


    }

    public BeanUtenti recuperaInformazioniUtenti(BeanUtenti utente) throws SQLException{

         Utente user = daoUtente.cercaUtente(utente.getEmail());

         return new BeanUtenti(user.getTipo(), user.getName(), user.getCognome(), user.getEmail(), user.getPass(), user.getCitta());
        
        
        

    }

    public boolean modificaPassword(String vecchiaPass, String nuovaPass, BeanUtenti utente) throws SQLException{
        
         Utente user = daoUtente.cercaUtente(utente.getEmail());

         if(user.getPass().equals(vecchiaPass)){
            daoUtente.aggiornaPassword(user.getEmail(), nuovaPass);
            return true;
         }
        
        return false;

    }

    
    
}
