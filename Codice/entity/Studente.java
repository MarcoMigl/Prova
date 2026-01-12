package appolloni.migliano.entity;

import java.util.ArrayList;

public class Studente extends Utente {
    private ArrayList<Gruppo> gruppi;


    public Studente(String nome, String cognome, String email, String citta, String tipo, String password){
        super(nome, cognome,email,citta,tipo, password);
        gruppi = new ArrayList<Gruppo>();
    }

    public void addGruppo(Gruppo gruppoUtente){
        this.gruppi.add(gruppoUtente);
    }

    public void removeGruppo(Gruppo gruppoUtente){
        this.gruppi.remove(gruppoUtente);
    }

    public ArrayList<Gruppo> getGruppo(){
        return this.gruppi;
    }
    
}
