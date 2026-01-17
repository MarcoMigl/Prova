package appolloni.migliano.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import appolloni.migliano.HelperErrori;

import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.controller.ControllerGestioneStrutture;

public class GUIModificaStruttura {

    @FXML private TextField txtNome;
    @FXML private TextField txtIndirizzo;
    @FXML private TextField txtCitta;
    @FXML private TextField txtOrario;
    @FXML private CheckBox chkWifi;
    @FXML private CheckBox chkRistorazione;

    private BeanStruttura strutturaCorrente;

    private String vecchioNome; 

    private ControllerGestioneStrutture controllerApp = new ControllerGestioneStrutture();
    public void initData(BeanStruttura struttura) {
        this.strutturaCorrente = struttura;
        this.vecchioNome = struttura.getName(); 

            txtNome.setText(struttura.getName());
            txtIndirizzo.setText(struttura.getIndirizzo());
            txtCitta.setText(struttura.getCitta());
            txtOrario.setText(struttura.getOrario());
            chkWifi.setSelected(struttura.hasWifi());
            chkRistorazione.setSelected(struttura.hasRistorazione());
    }

    @FXML
    public void clickSalva(ActionEvent event) {
        try {

            if (txtNome.getText().trim().isEmpty()) {
                HelperErrori.errore("Il nome non può essere vuoto!","Inserire il nome");
            }
            strutturaCorrente.setName(txtNome.getText().trim());
            strutturaCorrente.setIndirizzo(txtIndirizzo.getText().trim());
            strutturaCorrente.setCitta(txtCitta.getText().trim());
            strutturaCorrente.setOrario(txtOrario.getText().trim());
            strutturaCorrente.setWifi(chkWifi.isSelected());
            strutturaCorrente.setRistorazione(chkRistorazione.isSelected());


            controllerApp.aggiornaStruttura(strutturaCorrente, vecchioNome);

            chiudiFinestra(event);

        }catch(IOException e){
          HelperErrori.errore("Errore salvataggio:", e.getMessage());
        }catch(SQLException e ){
            e.printStackTrace();
            HelperErrori.errore("Errore caricamento dati:", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            HelperErrori.errore("Errore:", e.getMessage());
        }
    }

    @FXML
    public void clickAnnulla(ActionEvent event) {
        chiudiFinestra(event);
    }

    private void chiudiFinestra(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
 
}