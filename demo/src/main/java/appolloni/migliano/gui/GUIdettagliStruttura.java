package appolloni.migliano.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.image.Image;      
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.control.ListView;
import appolloni.migliano.HelperErrori;
import appolloni.migliano.bean.BeanRecensioni;
import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerRecensioni;

public class GUIdettagliStruttura {
  
    @FXML private ImageView imgStruttura; 
    @FXML private Label lblNoFoto;        
    @FXML private Label lblNome;
    @FXML private Label lblTipoAttivita;
    @FXML private Label lblCitta;
    @FXML private Label lblIndirizzo;
    @FXML private Label lblOrario;
    @FXML private Label lblWifi;
    @FXML private Label lblRistorazione;
    @FXML private Label lblGestore;
    @FXML private ListView<String> listRecensioni;
    
    private ControllerRecensioni controllerRecensione = new ControllerRecensioni();
    private BeanUtenti beanUtente;
    private BeanStruttura beanStruttura;

    private static final String RED = "-fx-text-fill: red;";
    private static final String FORMATO = "%s\nVoto: %d/5\n\"%s\"";


 public void initData(BeanUtenti utente, BeanStruttura strutturaParziale) {
    this.beanUtente = utente;

    this.beanStruttura = strutturaParziale; 


    if (this.beanStruttura != null) {
      
        lblNome.setText(this.beanStruttura.getName());
        
        lblTipoAttivita.setText(this.beanStruttura.getTipoAttivita() != null ? this.beanStruttura.getTipoAttivita() : "N/D");
        lblCitta.setText(this.beanStruttura.getCitta() != null ? this.beanStruttura.getCitta() : "N/D");
        lblIndirizzo.setText(this.beanStruttura.getIndirizzo() != null ? this.beanStruttura.getIndirizzo() : "N/D");
        lblOrario.setText(this.beanStruttura.getOrario() != null ? this.beanStruttura.getOrario() : "N/D");
        
     
        impostaBooleano(lblWifi, this.beanStruttura.hasWifi());
        impostaBooleano(lblRistorazione, this.beanStruttura.hasRistorazione());

        lblGestore.setText(this.beanStruttura.getGestore().equalsIgnoreCase("system_no_host") ?  "-" : this.beanStruttura.getGestore());
        
        caricaRecensioni();
        caricaFotoStruttura();
    }
 }

 private void impostaBooleano(Label label, boolean valore) {
    if (valore) {
        label.setText("Sì");
        label.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
    } else {
        label.setText("No");
        label.setStyle(RED);
    }
 }
    
    private void caricaFotoStruttura() {
        String nomeFoto = beanStruttura.getFoto(); 
        
   
        String userFolder = System.getProperty("user.home") + File.separator + "IspwImages" + File.separator + nomeFoto;
        File fileUtente = new File(userFolder);

        if (fileUtente.exists()) {
            imgStruttura.setImage(new Image(fileUtente.toURI().toString()));
            lblNoFoto.setVisible(false);
        } else {
        
            lblNoFoto.setVisible(true);
            imgStruttura.setImage(null);
        }
    }

    public void caricaRecensioni(){
        listRecensioni.getItems().clear(); 
        try {
            List<BeanRecensioni> lista = controllerRecensione.cercaRecensioniPerStruttura(this.beanStruttura);
            
            if (lista.isEmpty()) {
                listRecensioni.getItems().add("Nessuna recensione ancora presente.");
            } else {
                for (BeanRecensioni b : lista) {
                    
                    String riga = String.format(FORMATO, 
                        b.getAutore(), b.getVoto(), b.getTesto());
                    listRecensioni.getItems().add(riga);
                }
            }
        }catch(SQLException e){
            HelperErrori.errore("Errore caricamento recensioni: ", e.getMessage());
        }catch(IOException e){
          HelperErrori.errore("Errore salvataggio:", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            listRecensioni.getItems().add("Errore caricamento recensioni.");
        }
    }

    @FXML
    public void clickScrivi(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scriviRecensione.fxml"));
        Parent root = loader.load();
        
        GUIScriviRecensione controller = loader.getController();
        controller.initData(beanUtente, beanStruttura); 

        Stage newStage = new Stage(); 
        newStage.setTitle("Scrivi Recensione");
        newStage.setScene(new Scene(root));
        

        newStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        newStage.showAndWait(); 
    
        caricaRecensioni();
    } 

    @FXML
    public void clickIndietro(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ricerca.fxml"));
        Parent root = loader.load();
        
        GUIRicerca controller = loader.getController();
        controller.initData(beanUtente); 

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.getScene().setRoot(root);
    }
}