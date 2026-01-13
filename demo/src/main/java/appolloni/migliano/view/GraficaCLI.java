package appolloni.migliano.view;

import appolloni.migliano.interfacce.InterfacciaGrafica;
import appolloni.migliano.cli.HomeCLI;

public class GraficaCLI implements InterfacciaGrafica {
    @Override
    public void avvia(String[] args) {
        System.out.println("--- Avvio modalità CLI ---");
        HomeCLI home = new HomeCLI();
        home.start();
    }
}