package appolloni.migliano.entity;

import java.sql.Timestamp;;

public class Messaggio {
    private String text;
    private Gruppo gruppo; 
    private Utente mittente;
    private Timestamp dataInvio;
    
    public Messaggio(String messaggio, Gruppo gruppo, Utente user){
        this.dataInvio = new Timestamp(System.currentTimeMillis());
        this.gruppo = gruppo;
        this.text = messaggio;
        this.mittente = user;

    }
    public Timestamp getTime(){
        return this.dataInvio;
    }
    public void setTime(Timestamp time){
        this.dataInvio = time;
    }
    public void setMess(String testo){

        this.text = testo;
    }
    
    public String getMess(){
        return this.text;
    }

    public void setGruppo(Gruppo g){
        this.gruppo = g;

    }
    public Gruppo getGruppo(){
        return this.gruppo;
    }
    public void setMittente(Utente u){
        this.mittente = u;
    }
    public Utente getMittente(){
        return this.mittente;
    }
}
