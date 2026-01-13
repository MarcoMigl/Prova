package appolloni.migliano.view;
import appolloni.migliano.interfacce.InterfacciaGrafica;
import javafx.application.Application;
import appolloni.migliano.Mainapp;

public class GraficaGUI implements InterfacciaGrafica {
    @Override
    public void avvia(String[] args) {
        Application.launch(Mainapp.class, args);
    }
}
