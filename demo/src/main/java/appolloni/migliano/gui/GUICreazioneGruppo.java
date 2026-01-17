package appolloni.migliano.gui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import appolloni.migliano.HelperErrori;
import appolloni.migliano.bean.BeanGruppo;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneGruppo;
import appolloni.migliano.exception.CampiVuotiException;
import appolloni.migliano.exception.CreazioneFallita;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GUICreazioneGruppo {

    @FXML private TextField txtNome;
    @FXML private TextField txtMateria;
    @FXML private TextField txtCitta;
    @FXML private ComboBox<String> comboLuogo;
    @FXML private Button bCrea;
    @FXML private Label lbRisultato;

    private BeanUtenti bean;
    private ControllerGestioneGruppo controllerCreazioneGruppo = new ControllerGestioneGruppo();
    private static final String ORANGE= "-fx-text-fill: orange;";
    private static final String RED = "-fx-text-fill: red;";
    public void initData(BeanUtenti utente){
        this.bean = utente;
        comboLuogo.setEditable(true);
        comboLuogo.setOnShowing(event -> caricaStruttureDisponibili());
    }

    private void caricaStruttureDisponibili() {
        String cittaInserita = txtCitta.getText().trim();
        
        comboLuogo.getItems().clear();

        if (cittaInserita.isEmpty()) {
            lbRisultato.setText("Inserisci prima una città!");
            lbRisultato.setStyle(ORANGE);
            return;
        }

        try {

            List<String> strutture = controllerCreazioneGruppo.getListaStruttureDisponibili(cittaInserita);
            
            if (strutture.isEmpty()) {
                lbRisultato.setText("Nessuna struttura trovata a " + cittaInserita);
                lbRisultato.setStyle(ORANGE);
            } else {
                lbRisultato.setText(""); 
                comboLuogo.getItems().addAll(strutture);
            }
        
        }catch(SQLException exception){
            HelperErrori.errore("Errore", exception.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            lbRisultato.setText(e.getMessage());
            lbRisultato.setStyle(RED);
        }
    }

    @FXML
    public void clickCrea(ActionEvent event) {
        String nomeGruppo = txtNome.getText().trim();
        String materia = txtMateria.getText().trim();
        String citta = txtCitta.getText().trim();


        String rawLuogo = comboLuogo.getValue();
        
        String luogo = (rawLuogo == null || rawLuogo.trim().isEmpty()) ? "Sconosciuto" : rawLuogo.trim();
        try {
            BeanGruppo beanGruppo = new BeanGruppo(nomeGruppo, materia, bean.getEmail(), luogo, citta);
            controllerCreazioneGruppo.creaGruppo(bean, beanGruppo);
            
            lbRisultato.setText("Gruppo creato con successo!");
            lbRisultato.setStyle("-fx-text-fill: green;");
            pulisci();

            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
            Parent root = loader.load();
            GUImainMenu mainMenu = loader.getController();
            mainMenu.initData(bean);

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            
            stage.getScene().setRoot(root); 

        } catch(SQLException e){
           HelperErrori.errore("Errore creazione gruppo:", "Gruppo esistente");
        }catch(CampiVuotiException e){
            lbRisultato.setText(e.getMessage());
            lbRisultato.setStyle(RED);

        }catch(CreazioneFallita e){
            HelperErrori.errore("Errore creazione gruppo:", e.getMessage());

        } catch(Exception e){
            e.printStackTrace();
            lbRisultato.setText("Errore creazione: " + e.getMessage());
            lbRisultato.setStyle(RED);
        }
    }

    @FXML
    public void clickIndietro(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
        Parent root = loader.load();
        GUImainMenu guImainMenu = loader.getController();
        guImainMenu.initData(this.bean);
        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
    
        stage.getScene().setRoot(root); 
    }

    @FXML
    public void pulisci(){
        txtMateria.clear();
        txtNome.clear();
        txtCitta.clear();
        comboLuogo.getItems().clear();
        comboLuogo.getSelectionModel().clearSelection();
    }
}