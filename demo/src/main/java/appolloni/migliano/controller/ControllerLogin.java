package appolloni.migliano.controller;

import java.sql.SQLException;

import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.entity.Utente;
import appolloni.migliano.exception.CredenzialiSbagliateException;
import appolloni.migliano.exception.EmailNonValidaException;
import appolloni.migliano.factory.FactoryDAO;
import appolloni.migliano.interfacce.InterfacciaUtente;

public class ControllerLogin {
    private InterfacciaUtente daoUtente = FactoryDAO.getDaoUtente();

    public BeanUtenti verificaUtente(BeanUtenti bean) throws SQLException,EmailNonValidaException,CredenzialiSbagliateException{


        if(!bean.getEmail().contains("@")){
          throw new EmailNonValidaException("Formato email non valida");
        }
        Utente user = daoUtente.cercaUtente(bean.getEmail());

        if(user == null){
           throw new CredenzialiSbagliateException("Nessun utente registrato con questa email.");
        }

         if(!(user.getPass()).equals(bean.getPassword())){
            throw new CredenzialiSbagliateException("Password non corretta.");
         }

      return new BeanUtenti(user.getTipo(), user.getName(), user.getCognome(), user.getEmail(), user.getPass(), user.getCitta());


   

        
    }

}
