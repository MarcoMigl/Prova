package appolloni.migliano.factory;

import appolloni.migliano.Configurazione;
import appolloni.migliano.interfacce.InterfacciaGrafica;
import appolloni.migliano.view.GraficaCLI;
import appolloni.migliano.view.GraficaGUI;

public class FactoryUI {

    
    private FactoryUI() {
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }

    public static InterfacciaGrafica getInterfaccia() {
        String tipo = Configurazione.getTipoInterfaccia(); 

        if ("CLI".equals(tipo)) {
            return new GraficaCLI();
        } else {
            return new GraficaGUI();
        }
    }
}