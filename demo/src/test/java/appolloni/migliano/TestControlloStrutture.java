package appolloni.migliano;

import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneStrutture;
import appolloni.migliano.controller.ControllerGestioneUtente;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Appolloni Gabriele 0307344

 class TestControlloStrutture {
    private ControllerGestioneStrutture controllerGestioneStrutture;
    private ControllerGestioneUtente controllerGestioneUtente;
    private BeanStruttura beanStruttura;
    private BeanUtenti beanUtenti;
   

    @BeforeEach
    void setup() throws Exception{
        controllerGestioneStrutture = new ControllerGestioneStrutture();
        controllerGestioneUtente = new ControllerGestioneUtente();
        beanStruttura = new BeanStruttura("Pubblica", "Test", "Test", "Test", false, false);
        beanStruttura.setFoto("test.png");
        beanStruttura.setGestore("test@test");
        beanStruttura.setOrario("Test");
        beanStruttura.setTipoAttivita("Bar");

        beanUtenti = new BeanUtenti("Host", "Test", "Test", "test@test", "test", "Test");
        beanUtenti.setTipoAttivita(beanStruttura.getTipoAttivita());
        beanUtenti.setNomeAttivita(beanStruttura.getName());

  

        
        try{
            controllerGestioneUtente.creazioneUtente(beanUtenti);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @AfterEach
    void annullaOp() throws Exception{
        Connection conn;
         conn = DBConnection.getInstance().getConnection();    

         try(PreparedStatement ps = conn.prepareStatement("DELETE FROM strutture WHERE nome = ? AND gestore = ?")){
            ps.setString(1, beanStruttura.getName());
            ps.setString(2,beanStruttura.getGestore());
            ps.executeUpdate();

         }
         

      

         try(PreparedStatement ps = conn.prepareStatement("DELETE FROM utenti WHERE email = ?")){
            ps.setString(1, beanUtenti.getEmail());
            ps.executeUpdate();
         }

    }

    @Test
    void testCreaStruttura(){
        try{
            controllerGestioneStrutture.creaStruttura(beanUtenti, beanStruttura);
            BeanStruttura check = controllerGestioneStrutture.visualizzaStrutturaHost(beanStruttura.getGestore());
            assertEquals(beanStruttura.getName(), check.getName(),"Il nome dovrebbe essere uguale");
        }catch(Exception e){
            e.printStackTrace();
            fail("Non doveva lanciare eccezioni: "+ e.getMessage());
        }
    }

    @Test
    void testCreaStrutturaEsistente(){
        try{
         controllerGestioneStrutture.creaStruttura(beanUtenti, beanStruttura);
         assertThrows(IllegalArgumentException.class, () -> {controllerGestioneStrutture.creaStruttura(beanUtenti, beanStruttura); });
        }catch(Exception e){
            fail("Non deve fallire: "+ e.getMessage());
        }
    }
 

    

}
