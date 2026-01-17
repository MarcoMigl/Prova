package appolloni.migliano.gui;

import java.io.IOException;
import java.sql.SQLException;

import appolloni.migliano.HelperErrori;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneUtente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUIprofiloUtente {

    @FXML private Label lblNome;
    @FXML private Label lblCognome;
    @FXML private Label lblEmail;
    @FXML private Label lblCitta;
    @FXML private Label lblTipo;
    @FXML private VBox boxPassword;
    @FXML private Button btnTogglePassword;
    @FXML private PasswordField txtOldPass;
    @FXML private PasswordField txtNewPass;
    @FXML private PasswordField txtConfirmPass;
    @FXML private Label lblErrorePass;

    private BeanUtenti beanUtente;
    private  static final String RED = "-fx-text-fill: red;";
    private ControllerGestioneUtente controllerProfiloUtente;

    public void initData(BeanUtenti utente){
        this.beanUtente = utente;
        controllerProfiloUtente = new ControllerGestioneUtente();
        caricaInformazioni(beanUtente);
    }

    private void caricaInformazioni(BeanUtenti beanUtenti){

      try{
  
        BeanUtenti newBean = controllerProfiloUtente.recuperaInformazioniUtenti(beanUtenti);
        
        lblNome.setText(newBean.getName());
        lblCognome.setText(newBean.getCognome());
        lblEmail.setText(newBean.getEmail());
        lblCitta.setText(newBean.getCitta());
        lblTipo.setText(newBean.getTipo());
      }catch(SQLException e){
        HelperErrori.errore("Errore caricamento", e.getMessage());
      }catch(Exception e){
        HelperErrori.errore("Errore generico:", e.getMessage());
      }
    }

    @FXML
    public void clickIndietro(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
        Parent root = loader.load();
        GUImainMenu guImainMenu = loader.getController();
        guImainMenu.initData(beanUtente);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        

        stage.getScene().setRoot(root); 
    }

    @FXML
    public void clickTogglePassword(ActionEvent event) {
        boolean isVisible = boxPassword.isVisible();
        
        boxPassword.setVisible(!isVisible);
        boxPassword.setManaged(!isVisible); 
        
        if (!isVisible) {
            btnTogglePassword.setText("Annulla Modifica Password ▲");
            btnTogglePassword.setStyle("-fx-background-color: transparent; -fx-text-fill: #dc3545; -fx-border-color: #dc3545; -fx-border-radius: 5; -fx-cursor: hand;");
        } else {
            btnTogglePassword.setText("Modifica Password ▼");
            btnTogglePassword.setStyle("-fx-background-color: transparent; -fx-text-fill: #007bff; -fx-border-color: #007bff; -fx-border-radius: 5; -fx-cursor: hand;");
            
   
            lblErrorePass.setText("");
            txtOldPass.clear();
            txtNewPass.clear();
            txtConfirmPass.clear();
        }
    }

    @FXML
    public void clickSalvaPassword(ActionEvent event) {
        String oldP = txtOldPass.getText();
        String newP = txtNewPass.getText();
        String confirmP = txtConfirmPass.getText();
        
        if (newP.isEmpty() || oldP.isEmpty()) {
            lblErrorePass.setText("Riempi tutti i campi.");
            lblErrorePass.setStyle(RED);
            return;
        }
        
        if (!newP.equals(confirmP)) {
            lblErrorePass.setText("Le nuove password non coincidono.");
            lblErrorePass.setStyle(RED);
            return;
        }
        
        try{

         boolean esito = controllerProfiloUtente.modificaPassword(oldP, newP, beanUtente);
        
            if(esito){
            lblErrorePass.setText("Password modificata con successo!");
            lblErrorePass.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            
            txtOldPass.clear();
            txtNewPass.clear();
            txtConfirmPass.clear();
         } else {
            lblErrorePass.setText("Password attuale errata.");
            lblErrorePass.setStyle(RED);
         }

        }catch(Exception e){

            HelperErrori.errore("Errore:", e.getMessage());
        }
    }
}