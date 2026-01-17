package appolloni.migliano.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import appolloni.migliano.HelperErrori;
import appolloni.migliano.bean.BeanGruppo;
import appolloni.migliano.bean.BeanMessaggi;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerChat;

public class GUIChat {

    @FXML private Label lbNomeGruppo;
    @FXML private ListView<String> listaMessaggi;
    @FXML private TextField txtMessaggio;
    @FXML private Label lbInvio;
    @FXML private Label lbMateria;
    @FXML private Label lbCitta;
    @FXML private Label lbLuogo;

    private BeanUtenti beanUtente;
    private BeanGruppo beanGruppo;
    private static final String ERROREGENERICO = "Errore generico: ";
    
    private ControllerChat controllerChat = new ControllerChat();

    public void initData(BeanUtenti bean, BeanGruppo gruppoCorr) {
        this.beanUtente = bean;
        this.beanGruppo = gruppoCorr;

        lbNomeGruppo.setText(gruppoCorr.getNome());
        caricaInformazioniGruppo();
        aggiornaLista();
    }

    private void caricaInformazioniGruppo() {

        try{
          BeanGruppo infoComplete = controllerChat.recuperaInfoGruppo(this.beanGruppo);
          if (infoComplete != null) {
            lbMateria.setText(infoComplete.getMateria());
            lbCitta.setText(infoComplete.getCitta());
            lbLuogo.setText(infoComplete.getLuogo() != null ? infoComplete.getLuogo() : "Non specificato");
        }
        }catch(SQLException e){
         HelperErrori.errore("Errore caricamento: ", e.getMessage());

        }catch(Exception e){

            HelperErrori.errore("Errore imprevisto: ", e.getMessage());
        }
    }

    public void aggiornaLista() {
        listaMessaggi.getItems().clear();
        try {
            List<BeanMessaggi> lista = controllerChat.recuperaMessaggi(this.beanGruppo);
            
            for (BeanMessaggi b : lista) {
               
                String riga = b.getMittente() + ": " + b.getMess();
                listaMessaggi.getItems().add(riga);
            }

            if (!listaMessaggi.getItems().isEmpty()) {
                listaMessaggi.scrollTo(listaMessaggi.getItems().size() - 1);
            }
        } catch (SQLException e) {
            HelperErrori.errore("Errore:", e.getMessage());
        }catch(IllegalArgumentException e){
            HelperErrori.errore(ERROREGENERICO,e.getMessage());
        }
    }

    @FXML
    public void inviaMessaggio() {
        String text = txtMessaggio.getText().trim();

        try {
            controllerChat.inviaMessaggio(beanUtente, beanGruppo, text);
            
            aggiornaLista();
            txtMessaggio.clear();
            lbInvio.setText(""); 
            
        } catch (SQLException e) {
           HelperErrori.errore("Errore: ", e.getMessage());
        }catch(IllegalArgumentException e){
            HelperErrori.errore(ERROREGENERICO,e.getMessage());
        }
    }
    
    @FXML
    public void clickAbbandona(ActionEvent event) {
        try {
            controllerChat.abbandonaGruppo(beanUtente, beanGruppo);
            tornaIndietro(event);
        } catch (SQLException e) {
           HelperErrori.errore("Errore", e.getMessage());
        } catch(Exception e){
            HelperErrori.errore(ERROREGENERICO,e.getMessage());

        }
    }

    @FXML
    public void tornaIndietro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
            Parent root = loader.load();
            GUImainMenu mainMenu = loader.getController();
            mainMenu.initData(beanUtente);

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            
         
            stage.getScene().setRoot(root); 

        } catch (IOException e) {
            e.printStackTrace();
            lbInvio.setText("Errore navigazione.");
        }
    }
}