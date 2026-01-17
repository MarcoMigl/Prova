package appolloni.migliano.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import appolloni.migliano.bean.BeanUtenti;

import java.io.IOException;
import java.sql.SQLException;

import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.controller.ControllerGestioneStrutture;
import appolloni.migliano.exception.CampiVuotiException;
import appolloni.migliano.exception.CreazioneFallita;


public class GUISegnalaStruttura {

    @FXML private TextField txtNome;
    @FXML private TextField txtCitta;
    @FXML private TextField txtIndirizzo;
    @FXML private TextField txtOrario;
    @FXML private ComboBox<String> comboTipoAttivita;
    @FXML private ComboBox<String> comboTipo;
    @FXML private CheckBox checkWifi;
    @FXML private CheckBox checkRistorazione;
    @FXML private Label lblRisultato;

    private BeanUtenti studenteLoggato;
    private ControllerGestioneStrutture controllerApp = new ControllerGestioneStrutture(); 
    private static final String RED = "-fx-text-fill: red;";

    public void initData(BeanUtenti utente) {
        this.studenteLoggato = utente;
        comboTipo.getItems().addAll("Privata","Pubblica");
        comboTipoAttivita.getItems().addAll("Bar","Università","Biblioteca");
    }

    @FXML
    public void clickSegnala(ActionEvent event) {
         String tipo = comboTipo.getValue();
         String tipoAtt = comboTipoAttivita.getValue();
        if(tipo== null || tipoAtt == null){
            lblRisultato.setText("Scegliere una opzione!");
            lblRisultato.setStyle(RED);
            return;
        }
        try {
         
            if( txtNome.getText().isEmpty() || txtCitta.getText().isEmpty()|| txtIndirizzo.getText().isEmpty()|| txtOrario.getText().isEmpty()) {
                lblRisultato.setText("Compila i campi.");
                lblRisultato.setStyle(RED);
                return;
            }

            BeanStruttura struttura = new BeanStruttura(
                tipo, 
                txtNome.getText().trim(), 
                txtCitta.getText().trim(), 
                txtIndirizzo.getText().trim(), 
                checkWifi.isSelected(), 
                checkRistorazione.isSelected()
            );

            struttura.setOrario(txtOrario.getText().trim());
            struttura.setGestore("Sconosciuto");
            struttura.setTipoAttivita(tipoAtt);
            
            controllerApp.creaStruttura(studenteLoggato, struttura); 
            

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successo");
            alert.setHeaderText(null);
            alert.setContentText("Grazie! La struttura è stata segnalata con successo.");
            alert.showAndWait(); 

            clickIndietro(event);
        }catch(CampiVuotiException e){
            lblRisultato.setText("Errore, compilare tutti i campi");
             lblRisultato.setStyle(RED);

        } catch (IllegalArgumentException e) {
            lblRisultato.setText("Errore: " + "Struttura già esistente !");
            lblRisultato.setStyle(RED);
        } catch(SQLException | IOException e){
            lblRisultato.setText("Errore: salvataggio dati !");
            lblRisultato.setStyle(RED);
        }catch(CreazioneFallita e){
            lblRisultato.setText("Errore: caricamento dei dati!");
            lblRisultato.setStyle(RED);

        }
    }

    @FXML
    public void clickIndietro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenu.fxml")); 
            Parent root = loader.load();
            
            GUImainMenu controllerMenu = loader.getController();
            controllerMenu.initData(studenteLoggato); 

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            
            stage.getScene().setRoot(root); 

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}