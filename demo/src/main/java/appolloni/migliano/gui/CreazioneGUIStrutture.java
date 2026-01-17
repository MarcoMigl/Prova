package appolloni.migliano.gui;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

import appolloni.migliano.HelperErrori;
import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneStrutture;
import appolloni.migliano.exception.CampiVuotiException;
import appolloni.migliano.exception.CreazioneFallita;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;






public class CreazioneGUIStrutture {
    @FXML private ComboBox<String> comboTipo;
    @FXML private Label lblTipoAttivita;
    @FXML private CheckBox checkWifi;
    @FXML private CheckBox checkRistorazione;
    @FXML private Label lblNomeAttivita;
    @FXML private TextField txtCitta;
    @FXML private TextField txtIndirizzo;
    @FXML private Label lblGestore;
    @FXML private Label lblRisultato;
    @FXML private TextField txtOrario;
    @FXML private ImageView imgAnteprima;
    @FXML private Label lblNomeFile;
    private File fileImmagineSelezionato = null;
    private BeanUtenti beanCurr;



    private static final String COLORE = "-fx-text-fill: red;";
    private ControllerGestioneStrutture controllerGestioneStrutture = new ControllerGestioneStrutture();

     @FXML
     public void initialize(){
        comboTipo.getItems().addAll("Privata","Pubblica");
     }

     @FXML
     public void initData(BeanUtenti bean){
        this.beanCurr = bean;
        lblGestore.setText(beanCurr.getName()); 
        lblNomeAttivita.setText(beanCurr.getNomeAttivita());
        lblTipoAttivita.setText(beanCurr.getTipoAttivita());
     }


     @FXML
    public void clickCaricaFoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Scegli foto struttura");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg"));
        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        fileImmagineSelezionato = fileChooser.showOpenDialog(stage);

        if (fileImmagineSelezionato != null) {
            imgAnteprima.setImage(new Image(fileImmagineSelezionato.toURI().toString()));
            lblNomeFile.setText(fileImmagineSelezionato.getName());
        }
    }

     @FXML
     public void clickRegistra(){
        String tipoAttivita = beanCurr.getTipoAttivita();
        String nome = beanCurr.getNomeAttivita();
        String tipo = comboTipo.getValue();
        String citta = txtCitta.getText().trim();
        String indirizzo = txtIndirizzo.getText().trim();
        boolean wifi = checkWifi.isSelected();
        boolean ristorazione = checkRistorazione.isSelected();
        String gestore = beanCurr.getEmail();
        String orario = txtOrario.getText().trim();
        BeanStruttura beanStruttura;
        


        
        try{
            String nomeFotoFinale = "placeholder.png";

            if (fileImmagineSelezionato != null) {
                nomeFotoFinale = salvaFileSuDisco(fileImmagineSelezionato);
            }
              beanStruttura = new BeanStruttura(tipo, nome, citta, indirizzo, wifi, ristorazione);
              beanStruttura.setOrario(orario);
              beanStruttura.setTipoAttivita(tipoAttivita);
              beanStruttura.setGestore(gestore);
              beanStruttura.setFoto(nomeFotoFinale);

              if (cercaOrfana(nome)) {
           
              controllerGestioneStrutture.rivendicaStruttura(beanStruttura, gestore);
             } else {
              controllerGestioneStrutture.creaStrutturaHost(beanCurr, beanStruttura);
             }
             pulisci(); 
             lblRisultato.setText("Registrazione Effettuata con Successo!");
             lblRisultato.setStyle("-fx-text-fill: green;");

             FXMLLoader loader =new FXMLLoader(getClass().getResource("/hostMenu.fxml"));
             Parent root = loader.load();
             GUIhostMenu hostMenu = loader.getController();
             hostMenu.initData(beanCurr);

             Stage stage = (Stage)(lblRisultato).getScene().getWindow();
             stage.getScene().setRoot(root);

        }catch(CampiVuotiException e){

            lblRisultato.setText(e.getMessage());
            lblRisultato.setStyle(COLORE);
        
        }catch(SQLException e){
            HelperErrori.errore("Errore creazione struttura:", e.getMessage());

        }catch(IOException e){
            HelperErrori.errore("Errore salvataggio:", e.getMessage());
        }catch(CreazioneFallita e){
            HelperErrori.errore("Errore creazione struttura", e.getMessage());
        }catch(Exception e){
            lblRisultato.setText("Errore: "+ e.getMessage());
            lblRisultato.setStyle(COLORE);
        }

     }




    private boolean cercaOrfana(String nomeStruttura) throws IOException, SQLException{
        return controllerGestioneStrutture.esistenzaStruttura(nomeStruttura);

    }

   


     @FXML 
     public void clickIndietro(ActionEvent event) throws IOException{
        try{
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/creazioneUtente.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
             stage.getScene().setRoot(root);
            
    
        } catch (Exception e) {
            e.printStackTrace();
            lblRisultato.setText("errore, impossibile tornare indietro");
        }



     }


     private String salvaFileSuDisco(File fileInput) throws IOException {

        String folderPath = System.getProperty("user.home") + File.separator + "IspwImages";
        File folder = new File(folderPath);
        if (!folder.exists()) folder.mkdir();

        String nuovoNome = "Struttura_" + System.currentTimeMillis() + "_" + fileInput.getName();
        File destinazione = new File(folder, nuovoNome);

        Files.copy(fileInput.toPath(), destinazione.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        return nuovoNome;
    }

    private void pulisci(){
   
        txtIndirizzo.clear();
        txtCitta.clear();
        comboTipo.getSelectionModel().clearSelection();
        checkRistorazione.setSelected(false);
        checkWifi.setSelected(false);
    }
}