package appolloni.migliano.factory;
import appolloni.migliano.entity.Studente;
import appolloni.migliano.entity.Utente;
import appolloni.migliano.entity.Host;

public class FactoryUtenti {

    
    private FactoryUtenti() {
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }

    public static Utente creazione(String type, String nome, String cognome, String email, String citta, String pass ){
        switch (type) {
            case "Studente" :
                return  new Studente(nome,cognome, email, citta,type, pass);
       
       
            case "Host":
                return new Host(nome, cognome, email,citta, type, pass);
             

            default:
                throw new IllegalArgumentException("Errore tipo di struttura non supportato: "+ type);

          
        }

    }
}
