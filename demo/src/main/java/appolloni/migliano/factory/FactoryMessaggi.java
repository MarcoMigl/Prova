package appolloni.migliano.factory;

import appolloni.migliano.entity.Gruppo;
import appolloni.migliano.entity.Messaggio;
import appolloni.migliano.entity.Utente;

public class FactoryMessaggi {

    
    private FactoryMessaggi() {
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }
    public static Messaggio creaMessaggio(String testo, Gruppo gruppo, Utente utente){
        return new Messaggio(testo, gruppo, utente);
    }
    
}
