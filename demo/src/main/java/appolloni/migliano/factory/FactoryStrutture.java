package appolloni.migliano.factory;

import appolloni.migliano.entity.Struttura;


public class FactoryStrutture {

    
    private FactoryStrutture() {
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }
    
    public static Struttura creazioneStrutture(String type,String nome, String citta, String addr,boolean wifi, boolean ristorazione){
       if (!type.equals("Pubblica") && !type.equals("Privata")) {
            throw new IllegalArgumentException("Tipo struttura non valido: " + type);
        }  
        return new Struttura(type, nome, citta, addr, wifi, ristorazione);
    }
}
