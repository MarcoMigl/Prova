package appolloni.migliano;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class Mainapp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/home.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("App di : University spot");
        stage.setScene(scene);
        stage.setResizable(true); 
        stage.setMaximized(true);
        
        stage.show();
    }

}