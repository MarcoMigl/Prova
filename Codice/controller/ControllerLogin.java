package appolloni.migliano.controller;

import appolloni.migliano.ClassiDiErrori.CredenzialiSbagliateException;
import appolloni.migliano.ClassiDiErrori.EmailNonValidaException;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.entity.Utente;
import appolloni.migliano.factory.FactoryDAO;
import appolloni.migliano.interfacce.InterfacciaUtente;

public class ControllerLogin {
    private Utente user = null;
    private InterfacciaUtente daoUtente = FactoryDAO.getDaoUtente();

    public BeanUtenti verificaUtente(BeanUtenti bean) throws Exception{


        if(!bean.getEmail().contains("@")){
          throw new EmailNonValidaException("Formato email non valida");
        }
        user = daoUtente.cercaUtente(bean.getEmail());

        if(user == null){
           throw new CredenzialiSbagliateException("Nessun utente registrato con questa email.");
        }

         if(!(user.getPass()).equals(bean.getPassword())){
            throw new CredenzialiSbagliateException("Password non corretta.");
         }

      BeanUtenti beanUser = new BeanUtenti(user.getTipo(), user.getName(), user.getCognome(), user.getEmail(), user.getPass(), user.getCitta());


       return beanUser;

        
    }

}
