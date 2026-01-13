package appolloni.migliano.factory;

import appolloni.migliano.entity.Gruppo;
import appolloni.migliano.entity.Utente;

public class FactoryGruppo {
    
    private FactoryGruppo() {
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }
    public static Gruppo creaGruppo(String nomeGruppo, Utente admin){
        return new Gruppo(nomeGruppo, admin);
    }
    
}
