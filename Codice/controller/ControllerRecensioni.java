package appolloni.migliano.controller;

import appolloni.migliano.ClassiDiErrori.CampiVuotiException;
import appolloni.migliano.bean.BeanRecensioni;
import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.entity.Recensione;
import appolloni.migliano.entity.Struttura;
import appolloni.migliano.entity.Utente;
import appolloni.migliano.factory.FactoryDAO;
import appolloni.migliano.factory.FactoryRecensioni;
import appolloni.migliano.interfacce.InterfacciaDaoRecensioni;
import appolloni.migliano.interfacce.InterfacciaDaoStruttura;
import appolloni.migliano.interfacce.InterfacciaUtente;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerRecensioni {

    private InterfacciaDaoRecensioni daoRecensioni = FactoryDAO.getDaoRecensioni();
    private InterfacciaUtente daoUtente = FactoryDAO.getDaoUtente();
    private InterfacciaDaoStruttura daoStrutture = FactoryDAO.getDAOStrutture();

    public void inserisciRecensione(BeanRecensioni beanRecensione) throws SQLException, Exception {

        Utente user = daoUtente.cercaUtente(beanRecensione.getAutore());

     try{
        Struttura struttura = daoStrutture.cercaStruttura(beanRecensione.getIdStruttura(),beanRecensione.getGestoreStruttura());
        String autore = user.getEmail();
        String testo = beanRecensione.getTesto();
        int voto = beanRecensione.getVoto();

        
        
        if (autore == null || autore.isBlank())
            throw new CampiVuotiException("Autore mancante");

        if (testo == null || testo.isBlank())
            throw new CampiVuotiException("Testo recensione mancante");

        if (voto < 1 || voto > 5)
            throw new Exception("Voto non valido");


        Recensione recensione = FactoryRecensioni.creazioneRecensione(user, struttura, testo, voto);

        daoRecensioni.salvaRecensione(recensione);
      }catch(SQLException e){
        throw e;

      }catch(IOException e){
        throw e;
      }
    }

    public List<BeanRecensioni> cercaRecensioniPerStruttura(BeanStruttura beanStruttura) throws SQLException, Exception {
    List<BeanRecensioni> listaBean = new ArrayList<>();
        try{
         List<Recensione> listaEntity = daoRecensioni.getRecensioniByStruttura(
            beanStruttura.getName(), 
            beanStruttura.getGestore()
         );
         for (Recensione r : listaEntity) {
            BeanRecensioni b = new BeanRecensioni(r.getAutore().getEmail(),r.getTesto(),r.getVoto(),r.getStruttura_recensita().getName(),r.getStruttura_recensita().getGestore());
            listaBean.add(b);
           }
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }catch(IOException e){
            throw e;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    return listaBean;
    }
}
