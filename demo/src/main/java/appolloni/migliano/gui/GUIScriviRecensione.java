package appolloni.migliano.gui;

import appolloni.migliano.HelperErrori;
import appolloni.migliano.bean.BeanRecensioni;
import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerRecensioni;
import appolloni.migliano.exception.CampiVuotiException;
import appolloni.migliano.exception.CreazioneFallita;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class GUIScriviRecensione {

    @FXML private Label lblNomeStruttura;
    @FXML private Slider sliderVoto;
    @FXML private TextArea txtRecensione;
    @FXML private Label lblErrore;

    private BeanUtenti beanUtente;
    private BeanStruttura beanStruttura;
    
    private ControllerRecensioni controllerRecensioni = new ControllerRecensioni();

    public void initData(BeanUtenti utente, BeanStruttura struttura) {
        this.beanUtente = utente;
        this.beanStruttura = struttura;

        lblNomeStruttura.setText("Per: " + struttura.getName());
    }

    @FXML
    public void clickInvia(ActionEvent event) {
        try {
            int voto = (int) sliderVoto.getValue(); 
            String testo = txtRecensione.getText();

            if (voto < 1 || voto > 5) {
                lblErrore.setText("Il voto deve essere tra 1 e 5.");
                return;
                
            }

            BeanRecensioni beanRecensioni = new BeanRecensioni(beanUtente.getEmail(), testo, voto, beanStruttura.getName(), beanStruttura.getGestore());
            controllerRecensioni.inserisciRecensione(beanRecensioni);

            chiudiFinestra(event);

        }catch(CampiVuotiException e){
            lblErrore.setText(e.getMessage());

        }catch(CreazioneFallita e){
             HelperErrori.errore("Errore creazione recensione:", e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            lblErrore.setText(e.getMessage());
        }
    }

    @FXML
    public void clickAnnulla(ActionEvent event) {
        chiudiFinestra(event);
    }

    private void chiudiFinestra(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}