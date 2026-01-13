package appolloni.migliano.controller;


import appolloni.migliano.entity.Struttura;
import appolloni.migliano.exception.CampiVuotiException;
import appolloni.migliano.exception.EntitaNonTrovata;
import appolloni.migliano.factory.FactoryDAO;
import appolloni.migliano.factory.FactoryStrutture;
import appolloni.migliano.interfacce.InterfacciaDaoStruttura;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.bean.BeanStruttura;



public class ControllerGestioneStrutture {

  
   private InterfacciaDaoStruttura daoStrutture = FactoryDAO.getDAOStrutture();
   

    public void creaStruttura(BeanUtenti bean, BeanStruttura beanStr) throws CampiVuotiException,SQLException,IOException, EntitaNonTrovata, IllegalArgumentException{

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
      
        if(type.isEmpty() || nomeStruttura.isEmpty() || citta.isEmpty()|| indirizzo.isEmpty() || orario.isEmpty() ||responsabile.isEmpty()|| tipoAtt.isEmpty()){
          throw new CampiVuotiException("Completa tutti i campi.");
        }

       Struttura nuovStruttura =  daoStrutture.cercaStruttura(nomeStruttura,responsabile);
       if( nuovStruttura != null){
         throw new IllegalArgumentException("La struttura esiste");
       }else{

        Struttura struttura = FactoryStrutture.creazioneStrutture(type, nomeStruttura, citta, indirizzo,wifi, ristorazione);
        
       if (struttura == null) {
             throw new EntitaNonTrovata("Creazione struttura fallita.");
       }

       struttura.setGestore(responsabile);
       struttura.setTipoAttivita(tipoAtt);
       struttura.setOrario(orario);

       struttura.setFoto(foto);

        String emailHost = null;

      if(bean.getTipo().equals("Host")){
        
        emailHost = bean.getEmail();
      }
      daoStrutture.salvaStruttura(struttura,emailHost);

    }
  }



    public BeanStruttura visualizzaStrutturaHost(String emailHost) throws SQLException, IOException, IllegalArgumentException {
        if(emailHost == null ){
            throw new IllegalArgumentException("Host non vaido");

        }
      
         Struttura struttura = daoStrutture.recuperaStrutturaPerHost(emailHost);
         BeanStruttura beanStruttura = new BeanStruttura(struttura.getTipo(), struttura.getName(), struttura.getCitta(), struttura.getIndirizzo(), struttura.hasWifi(), struttura.hasRistorazione());
         beanStruttura.setFoto(struttura.getFoto());
         beanStruttura.setOrario(struttura.getOrario());
         beanStruttura.setTipoAttivita(struttura.getTipoAttivita());
         beanStruttura.setGestore(struttura.getGestore());
        return beanStruttura;

    }

    public void cambiaFoto(String emailHost, String nomeFoto) throws CampiVuotiException, SQLException,IOException {
     if (emailHost == null || nomeFoto == null) {throw new CampiVuotiException("Dati mancanti");}
      daoStrutture.aggiornaFotoStruttura(emailHost, nomeFoto);
    
    }

    public void aggiornaStruttura(BeanStruttura struttura, String vecchionNome) throws IOException, SQLException, CampiVuotiException{
        if(struttura.getCitta().isEmpty() || struttura.getGestore().isEmpty() || struttura.getIndirizzo().isEmpty()|| struttura.getName().isEmpty()|| struttura.getOrario().isEmpty()||struttura.getTipoAttivita().isEmpty()){
            throw new CampiVuotiException("Dati mancanti");
        }
        Struttura struttura2 = FactoryStrutture.creazioneStrutture(struttura.getTipo(), struttura.getName(), struttura.getCitta(), struttura.getIndirizzo(), struttura.hasWifi(), struttura.hasRistorazione());
        struttura2.setTipoAttivita(struttura.getTipoAttivita());
        struttura2.setGestore(struttura.getGestore());
        struttura2.setOrario(struttura.getOrario());
        daoStrutture.updateStruttura(struttura2, vecchionNome);
    
  }

  public List<BeanStruttura> cercaStrutture(String nome, String citta, String tipo) throws IOException, SQLException {
    
        if(nome != null && nome.isEmpty()) {nome = null;}
        if(citta != null && citta.isEmpty()) {citta = null;}
        if(tipo != null && (tipo.isEmpty() || tipo.equals("Tutti"))) {tipo = null;}

        List<BeanStruttura> listaBeans = new ArrayList<>();
        List<Struttura> listaEntities = daoStrutture.ricercaStruttureConFiltri(nome, citta, tipo);
        
        for (Struttura s : listaEntities) {
            BeanStruttura b = new BeanStruttura(s.getTipo(),s.getName(),s.getCitta(),s.getIndirizzo(),s.hasWifi(),s.hasRistorazione());
            b.setFoto(s.getFoto()); 
            b.setOrario(s.getOrario());
            b.setTipoAttivita(s.getTipoAttivita());
            b.setGestore(s.getGestore());
            
            listaBeans.add(b);
        }

        return listaBeans;
  }

    

}
