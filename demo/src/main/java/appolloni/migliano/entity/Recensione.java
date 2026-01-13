package appolloni.migliano.entity;

public class Recensione {
    private Utente autore;
    private String recensione;
    private int voto;
    private Struttura struttura_recensita;

    public Recensione(String text, int num, Utente utente, Struttura struttura) {
        this.recensione = text;
        this.voto = num;
        this.autore = utente;
        this.struttura_recensita = struttura;
    }

    public void setAutore(Utente user){
        this.autore = user;
    }

    public Utente getAutore(){
        return this.autore;
    }

    public void setStruttura_recensita(Struttura struttura){
        this.struttura_recensita = struttura;
    }

    public Struttura getStruttura_recensita(){
        return this.struttura_recensita;
    }
    
    public void setTesto(String txt){

        this.recensione = txt;
    }

    public String getTesto(){
        return this.recensione;
    }

    public void setVoto(int votazione){
        this.voto = votazione;
    }

    public int getVoto(){
        return this.voto;
    }
}
