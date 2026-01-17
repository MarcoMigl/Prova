package appolloni.migliano.gui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import appolloni.migliano.DBConnection;
import appolloni.migliano.HelperErrori;
import appolloni.migliano.bean.BeanGruppo;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneGruppo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUImainMenu {
    
   
  
    @FXML private Label lblSideNome;
    @FXML private Label lblSideEmail;
    @FXML private Label lblSideRuolo;
    @FXML private Label lblSideCitta;
    @FXML private VBox containerGruppi; 
    @FXML private Button bNuovaStruttura;
    @FXML private Button bNuovoGruppo;
    @FXML private Button logout;
    @FXML private Button bProfilo;
    
    private BeanUtenti bean;
    private ControllerGestioneGruppo controllerGruppo = new ControllerGestioneGruppo();

    public void initData(BeanUtenti utente){
        this.bean = utente;
        
        caricaSidebarProfilo();
        caricaGruppi();
    }

    private void caricaSidebarProfilo() {
        if (bean != null) {
            lblSideNome.setText(bean.getName() + " " + bean.getCognome());
            lblSideEmail.setText(bean.getEmail());
            lblSideRuolo.setText(bean.getTipo().toUpperCase());
            lblSideCitta.setText(bean.getCitta() != null ? bean.getCitta() : "N/D");
        }
    }

    private void caricaGruppi(){
        containerGruppi.getChildren().clear();
        try{
            List<BeanGruppo> gruppoUser = controllerGruppo.visualizzaGruppi(bean);
            
            if (gruppoUser != null && !gruppoUser.isEmpty()) {
                for(BeanGruppo g : gruppoUser){
                    HBox riga = new HBox(10);
                    riga.setAlignment(Pos.CENTER_LEFT);
                    riga.setStyle("-fx-border-color: #eeeeee; -fx-padding: 10; -fx-background-color: white; -fx-background-radius: 5;");

                    VBox info = new VBox(2);
                    Label label = new Label(g.getNome());
                    label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                    Label subLabel = new Label(g.getMateria());
                    subLabel.setStyle("-fx-text-fill: #777; -fx-font-size: 12px;");
                    info.getChildren().addAll(label, subLabel);

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    Button b = new Button("Apri Chat");
                    b.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-cursor: hand;");
                    b.setOnAction(e -> apriChat(g));

                    riga.getChildren().addAll(info, spacer, b);
                    containerGruppi.getChildren().add(riga);
                }
            } else {
                containerGruppi.getChildren().add(new Label("Non sei iscritto a nessun gruppo."));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void apriChat(BeanGruppo gruppoSelezionato) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chat.fxml")); 
            Parent root = loader.load();

            GUIChat chatController = loader.getController();
            chatController.initData(this.bean, gruppoSelezionato);

            Stage stage = (Stage) containerGruppi.getScene().getWindow();
            stage.getScene().setRoot(root); 

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickProfilo(ActionEvent event) {
        try{
         cambiaPagina(event, "/profiloUtente.fxml", controller ->  ((GUIprofiloUtente) controller).initData(bean));
        }catch(IOException e){
            HelperErrori.errore("Errore I/O: ", e.getMessage());

        }
    }

    public void clickRicerca(ActionEvent event) throws IOException{
        cambiaPagina(event, "/ricerca.fxml", controller -> ((GUIRicerca) controller).initData(this.bean));
    }

    public void clickNuovaStruttura(ActionEvent event) throws IOException{
        cambiaPagina(event, "/segnalaStruttura.fxml", controller -> ((GUISegnalaStruttura) controller).initData(this.bean));
    }
    
    public void clickNuovoGruppo(ActionEvent event) throws IOException{
        cambiaPagina(event, "/creazioneGruppo.fxml", controller -> ((GUICreazioneGruppo) controller).initData(bean));
    }

    public void clickLogout(ActionEvent event) throws IOException, SQLException {
        DBConnection.getInstance().closeConnection();
        cambiaPagina(event, "/home.fxml", null);
    }

    private void cambiaPagina(ActionEvent event, String fxml, InitControllerAction action) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        if (action != null) action.init(loader.getController());
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
    
    interface InitControllerAction { void init(Object controller); }
}