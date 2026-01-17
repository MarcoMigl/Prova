package appolloni.migliano.gui;

import java.io.IOException;
import java.sql.SQLException;

import appolloni.migliano.HelperErrori;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneUtente;
import appolloni.migliano.exception.CampiVuotiException;
import appolloni.migliano.exception.CreazioneFallita;
import appolloni.migliano.exception.EmailNonValidaException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox; 
import javafx.stage.Stage;

public class CreazioneGUIController {

    
    @FXML private ComboBox<String> comboTipo; 
    @FXML private Button btnRegistra;
    @FXML private TextField txtNome;
    @FXML private TextField txtCognome;
    @FXML private TextField txtEmail;
    @FXML private TextField txtCitta;
    @FXML private PasswordField txtPass;
    @FXML private Label lblRisultato;


    @FXML private VBox boxDatiHost; 
    @FXML private TextField txtNomeAttivita;
    @FXML private ComboBox<String> comboTipoAtt;
    

    private ControllerGestioneUtente controllerCreazioneUtente = new ControllerGestioneUtente();
    private static final String COLORE= "-fx-text-fill: red;";


    @FXML
    public void initialize() {
     
        comboTipo.getItems().addAll("Studente", "Host");
        comboTipoAtt.getItems().addAll("Bar","Università", "Biblioteca");

        comboTipo.getSelectionModel().selectedItemProperty().addListener((obs, vecchioValore, nuovoValore) -> {
            if ("Host".equals(nuovoValore)) {

                boxDatiHost.setVisible(true);
                boxDatiHost.setManaged(true);
            } else {

                boxDatiHost.setVisible(false);
                boxDatiHost.setManaged(false);
            }
        });
    }


    @FXML 
    public void clickRegistra(ActionEvent event) throws IOException {
    
        String tipo = comboTipo.getValue(); 
        String nome = txtNome.getText().trim();
        String cognome = txtCognome.getText().trim();
        String email = txtEmail.getText().trim().toLowerCase();
        String citta = txtCitta.getText().trim();
        String password = txtPass.getText().trim();

         if(tipo == null){
                lblRisultato.setText("Scegliere il tipo ");
                lblRisultato.setStyle(COLORE);
                return;
        }

        try {

           
            BeanUtenti beanUtente = new BeanUtenti(tipo, nome, cognome, email, password, citta);
            

            if (tipo.equals("Host")) {
                String nomeAtt = txtNomeAttivita.getText();
                String tipoAtt = comboTipoAtt.getValue();
                if(tipoAtt == null){
                 lblRisultato.setText("Scegliere il tipo di struttura! ");
                 lblRisultato.setStyle(COLORE);
                 return;

                }
                
                beanUtente.setTipoAttivita(tipoAtt);
                beanUtente.setNomeAttivita(nomeAtt);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/creazioneStrutture.fxml"));
                Parent root = loader.load();
                CreazioneGUIStrutture creazioneGUIStrutture = loader.getController();
                creazioneGUIStrutture.initData(beanUtente);

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                
                stage.getScene().setRoot(root);
                stage.setTitle("Registrazione Struttura");

                
                
            }else{
              controllerCreazioneUtente.creazioneUtente(beanUtente);
              FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
              Parent root = loader.load();
              GUImainMenu guImainMenu = loader.getController();
              guImainMenu.initData(beanUtente);

              Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                
              stage.getScene().setRoot(root);

            }


            lblRisultato.setText("Registrazione Effettuata con Successo!");
            lblRisultato.setStyle("-fx-text-fill: green;");
            pulisci(); 

        }catch(EmailNonValidaException | CampiVuotiException e){
            lblRisultato.setText( e.getMessage());
            lblRisultato.setStyle(COLORE);

        }catch(SQLException e){
            HelperErrori.errore("Errore caricamento dati", e.getMessage());

        }catch(CreazioneFallita e){
            HelperErrori.errore("Errore creazione utente", e.getMessage());
        } catch (Exception e) {
            HelperErrori.errore("Errore Generico:", e.getMessage());
            e.printStackTrace();
        }
    }

        
    public void clickIndietro(ActionEvent event) throws IOException{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.getScene().setRoot(root);
    }
    
    
    private void pulisci() {
        txtNome.clear();
        txtCitta.clear();
        txtCognome.clear();
        txtEmail.clear();
        txtPass.clear();
        comboTipo.getSelectionModel().clearSelection();
        txtNomeAttivita.clear();
        comboTipoAtt.getSelectionModel().clearSelection();
    }
}
