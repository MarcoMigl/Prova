package appolloni.migliano.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import appolloni.migliano.HelperErrori;
import appolloni.migliano.bean.BeanGruppo;
import appolloni.migliano.bean.BeanStruttura;
import appolloni.migliano.bean.BeanUtenti;
import appolloni.migliano.controller.ControllerGestioneGruppo;
import appolloni.migliano.controller.ControllerGestioneStrutture;


public class GUIRicerca {

    @FXML private ComboBox<String> comboScelta;
    @FXML private Label lblErrore;
    @FXML private VBox containerRisultati;
    @FXML private Button btnCerca;
    @FXML private VBox boxGruppi;
    @FXML private TextField txtGruppoNome;
    @FXML private TextField txtGruppoCitta;
    @FXML private TextField txtGruppoMateria;

    @FXML private VBox boxStrutture;
    @FXML private TextField txtStrutturaNome;
    @FXML private TextField txtStrutturaCitta;
    @FXML private ComboBox<String> comboStrutturaTipo;

    private BeanUtenti beanUtente;
    private ControllerGestioneGruppo controllerGruppo = new ControllerGestioneGruppo();
    private ControllerGestioneStrutture controllerStrutture= new ControllerGestioneStrutture();
    private static final String GRUPPO = "Gruppo";
    private static final String STRUTTURA = "Struttura";
    private static final String COLORE= "-fx-text-fill: red;";


    @FXML
   public void initialize() {
    comboScelta.getItems().addAll(GRUPPO, STRUTTURA);
    
    comboScelta.setPromptText("Scegli il tipo di ricerca");

    comboStrutturaTipo.getItems().addAll("Tutti", "Bar", "Biblioteca", "Università");
    
    comboScelta.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> aggiornaForm(newVal));
 
   }

    public void initData(BeanUtenti bean) {
        this.beanUtente = bean;
    }

    private void aggiornaForm(String scelta) {
        lblErrore.setText("");
        containerRisultati.getChildren().clear();
        
        if (GRUPPO.equals(scelta)) {
            boxGruppi.setVisible(true);   boxGruppi.setManaged(true);
            boxStrutture.setVisible(false); boxStrutture.setManaged(false);
        } else if (STRUTTURA.equals(scelta)) {
            boxStrutture.setVisible(true);  boxStrutture.setManaged(true);
            boxGruppi.setVisible(false);  boxGruppi.setManaged(false);
        }
    }

    @FXML
    public void clickCerca() {
        String scelta = comboScelta.getValue();
        if(scelta == null){
            lblErrore.setText("Errore, segli prima il tipo di ricerca");
            lblErrore.setStyle(COLORE);
            return;
        }
        containerRisultati.getChildren().clear();
       
        
        try {
            if (GRUPPO.equals(scelta)) {
                cercaGruppi();
            } else {
                cercaStrutture();
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblErrore.setText("Errore: " + e.getMessage());
        }
    }

    private void cercaGruppi() {
        String nome = txtGruppoNome.getText().trim();
        String citta = txtGruppoCitta.getText().trim();
        String materia = txtGruppoMateria.getText().trim();

        try{
         List<BeanGruppo> risultati = controllerGruppo.cercaGruppi(nome, citta, materia);
        
        if(risultati.isEmpty()) {
            Label empty = new Label("Nessun gruppo trovato.");
            empty.setStyle("-fx-text-fill: black;");
            containerRisultati.getChildren().add(empty);
            return;
        }

        for (BeanGruppo g : risultati) {
           

            HBox riga = new HBox(10);
            riga.setStyle("-fx-border-color: #ccc; -fx-padding: 10; -fx-background-color: white; -fx-background-radius: 5;");
            riga.setAlignment(Pos.CENTER_LEFT);

            VBox info = new VBox(2);
           
            Label lNome = new Label(g.getNome()); 
            lNome.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: black;");
            
            Label lDettagli = new Label(g.getCitta() + " • " + g.getMateria());
            lDettagli.setStyle("-fx-text-fill: #555;"); 
            
            info.getChildren().addAll(lNome, lDettagli);

            Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
            
            Button btnJoin = new Button("Unisci");
            btnJoin.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-cursor: hand;");
            
            btnJoin.setOnAction(event -> {
                try {
                    controllerGruppo.aggiungiGruppo(beanUtente, g);
                    btnJoin.setText("Iscritto!");
                    btnJoin.setDisable(true);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Errore: " + "Sei già parte del Gruppo!");
                    alert.show();
                    e.printStackTrace();
                }
            });
            
            riga.getChildren().addAll(info, spacer, btnJoin);
            containerRisultati.getChildren().add(riga);
        }
     }catch(SQLException e){
        HelperErrori.errore("Errore caricamento dati", e.getMessage());
     }
    }

    private void cercaStrutture(){
        String nome = txtStrutturaNome.getText().trim();
        String citta = txtStrutturaCitta.getText().trim();
        String tipo = comboStrutturaTipo.getValue();
        if("Tutti".equals(tipo)) {tipo = null;}

        try{
         List<BeanStruttura> risultati = controllerStrutture.cercaStrutture(nome, citta, tipo);
      

         if(risultati.isEmpty()) {
            Label empty = new Label("Nessuna struttura trovata.");
            empty.setStyle("-fx-text-fill: black;");
            containerRisultati.getChildren().add(empty);
            return;
         }
 
         for (BeanStruttura s : risultati) {
            

            HBox riga = new HBox(10);
            riga.setStyle("-fx-border-color: #ccc; -fx-padding: 10; -fx-background-color: white; -fx-background-radius: 5;");
            riga.setAlignment(Pos.CENTER_LEFT);

            VBox info = new VBox(5);
            
            String nomeStr = (s.getName() != null) ? s.getName() : "Nome sconosciuto";
         
            Label lNome = new Label(nomeStr); 
            lNome.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: black;");
            
            String tipoStr = (s.getTipoAttivita() != null) ? s.getTipoAttivita() : "Attività";
            String cittaStr = (s.getCitta() != null) ? s.getCitta() : "Città ignota";
            Label lDettagli = new Label(tipoStr + " a " + cittaStr);
            lDettagli.setStyle("-fx-text-fill: #555;");

            info.getChildren().addAll(lNome, lDettagli);
            Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

            Button btnDettagli = new Button("Dettagli");
            btnDettagli.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-cursor: hand;");
            
           
            btnDettagli.setOnAction(e -> {
              try {
               FXMLLoader loader = new FXMLLoader(getClass().getResource("/infoStruttura.fxml"));
               Parent root = loader.load();

               
               GUIdettagliStruttura controllerDettagli = loader.getController();
               controllerDettagli.initData(beanUtente, s); 

               Stage stage = (Stage) containerRisultati.getScene().getWindow();
               stage.getScene().setRoot(root); 

              } catch (IOException ex) {
                 ex.printStackTrace();
              }
            });
    

            riga.getChildren().addAll(info,spacer,btnDettagli);
            containerRisultati.getChildren().add(riga);
        }
     }catch(SQLException e){
        HelperErrori.errore("Errore caricamento:" ,e.getMessage());
     }catch(IOException e){
        HelperErrori.errore("Errore caricamento file: ", e.getMessage());
     }
    }

    @FXML
    public void clickIndietro(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
        Parent root = loader.load();
        GUImainMenu mainMenu = loader.getController();
        mainMenu.initData(beanUtente);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}