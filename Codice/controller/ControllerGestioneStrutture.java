package appolloni.migliano.controller;


import appolloni.migliano.entity.Struttura;
import appolloni.migliano.factory.FactoryDAO;
import appolloni.migliano.factory.FactoryStrutture;
import appolloni.migliano.interfacce.InterfacciaDaoStruttura;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import appolloni.migliano.ClassiDiErrori.CampiVuotiException;

import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.bean.BeanStruttura;



public class ControllerGestioneStrutture {

  
   private InterfacciaDaoStruttura daoStrutture = FactoryDAO.getDAOStrutture();
   

    public void CreaStruttura(BeanUtenti bean, BeanStruttura beanStr) throws CampiVuotiException,Exception,SQLException{

      String type = beanStr.getTipo();
      String nomeStruttura = beanStr.getName();
      String citta = beanStr.getCitta();
      String indirizzo = beanStr.getIndirizzo();
      String orario = beanStr.getOrario();
      boolean wifi = beanStr.hasWifi();
      boolean ristorazione = beanStr.hasRistorazione();
      String responsabile = beanStr.getGestore();
      String tipoAtt = beanStr.getTipoAttivita();
      String foto = beanStr.getFoto();
      
      try{
        
        if(type.isEmpty() || nomeStruttura.isEmpty() || citta.isEmpty()|| indirizzo.isEmpty() || orario.isEmpty() ||responsabile.isEmpty()|| tipoAtt.isEmpty()){
          throw new CampiVuotiException("Completa tutti i campi.");
        }

       Struttura nuovStruttura =  daoStrutture.cercaStruttura(nomeStruttura,responsabile);
       if( nuovStruttura != null){
         throw new Exception("La struttura esiste");
       }else{

        Struttura struttura = FactoryStrutture.creazioneStrutture(type, nomeStruttura, citta, indirizzo, orario,wifi, ristorazione, tipoAtt, responsabile);
        
       if (struttura == null) {
             throw new Exception("Creazione struttura fallita.");
       }

       struttura.setFoto(foto);

        String emailHost = null;

      if(bean.getTipo().equals("Host")){
        
        emailHost = bean.getEmail();
      }else{
        emailHost = null;
      }
      daoStrutture.salvaStruttura(struttura,emailHost);

      }
    }catch(IOException e){
      throw e;
    
    }catch(SQLException e){

      e.printStackTrace();
      throw e;
    }

  

  }



    public BeanStruttura visualizzaStrutturaHost(String emailHost) throws Exception {
        if(emailHost.isEmpty() || emailHost == null ){
            throw new Exception("Host non vaido");

        }
        BeanStruttura beanStruttura = null;
        try{
         Struttura struttura = daoStrutture.recuperaStrutturaPerHost(emailHost);

         beanStruttura = new BeanStruttura(struttura.getTipo(), struttura.getName(), struttura.getCitta(), struttura.getIndirizzo(),struttura.getOrario(), struttura.hasWifi(), struttura.hasRistorazione(), struttura.getTipoAttivita(), struttura.getGestore());
         beanStruttura.setFoto(struttura.getFoto());
        }catch(SQLException e){
            e.printStackTrace();
            throw e;

        }catch(IOException e){
            throw e;

        }
        return beanStruttura;

    }

    public void cambiaFoto(String emailHost, String nomeFoto) throws CampiVuotiException, SQLException, Exception, IOException {
     if (emailHost == null || nomeFoto == null) throw new CampiVuotiException("Dati mancanti");
     try {
      daoStrutture.aggiornaFotoStruttura(emailHost, nomeFoto);
     }catch(SQLException e){
        e.printStackTrace();
        throw e;
     }catch(IOException e){
        throw e;
     }catch(Exception e){
        throw e;
     }
    }

    public void aggiornaStruttura(BeanStruttura struttura, String vecchionNome) throws Exception, SQLException, CampiVuotiException{
        if(struttura.getCitta().isEmpty() || struttura.getGestore().isEmpty() || struttura.getIndirizzo().isEmpty()|| struttura.getName().isEmpty()|| struttura.getOrario().isEmpty()||struttura.getTipoAttivita().isEmpty()){
            throw new CampiVuotiException("Dati mancanti");
        }
        Struttura struttura2 = new Struttura(struttura.getTipo(), struttura.getName(), struttura.getCitta(), struttura.getIndirizzo(), struttura.getOrario(), struttura.hasWifi(), struttura.hasRistorazione(), struttura.getTipoAttivita(), struttura.getGestore());
        try{
         daoStrutture.updateStruttura(struttura2, vecchionNome);
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }catch(IOException e){
            throw e;
        }
  }

  public List<BeanStruttura> cercaStrutture(String nome, String citta, String tipo) throws Exception, SQLException {
    
        if(nome != null && nome.isEmpty()) nome = null;
        if(citta != null && citta.isEmpty()) citta = null;
        if(tipo != null && (tipo.isEmpty() || tipo.equals("Tutti"))) tipo = null;

        List<BeanStruttura> listaBeans = new ArrayList<>();
        try{
        List<Struttura> listaEntities = daoStrutture.ricercaStruttureConFiltri(nome, citta, tipo);
        
        for (Struttura s : listaEntities) {
            BeanStruttura b = new BeanStruttura(s.getTipo(),s.getName(),s.getCitta(),s.getIndirizzo(),s.getOrario(),s.hasWifi(),s.hasRistorazione(),s.getTipoAttivita(),s.getGestore());
            b.setFoto(s.getFoto()); 
            
            listaBeans.add(b);
        }

        }catch(SQLException e){
            e.printStackTrace();
            throw e;

        }catch(Exception e){
            e.printStackTrace();
            throw e;

        }
        return listaBeans;
  }

    

}
