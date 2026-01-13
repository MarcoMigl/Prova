package appolloni.migliano;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 1. Diciamo al caricatore DOVE si trova il file grafico
        // Nota: Il percorso inizia con "/" che indica la cartella "resources"
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/home.fxml"));
        
        // 2. Carichiamo la scena (il contenuto del file FXML)
        // Impostiamo anche le dimensioni della finestra (es. 400 larghezza, 500 altezza)
        Scene scene = new Scene(fxmlLoader.load(), 800, 1200);
        
        // 3. Configuriamo il Palcoscenico (la finestra)
        stage.setTitle( "App di : University spot");
        stage.setScene(scene);
        stage.setResizable(false); // Opzionale: impedisce di ridimensionare la finestra
        
        // 4. Azione!
        stage.show();
    }

    // Questo è il punto di ingresso standard di Java
    public static void main(String[] args) {
        launch(); // Questo comando avvia tutto il motore JavaFX
    }
}
