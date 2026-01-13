package appolloni.migliano.entity;

public class Recensione {
    private Utente autore;
    private String testo;
    private int voto;
    private Struttura strutturaRecensita;

    public Recensione(String text, int num, Utente utente, Struttura struttura) {
        this.testo = text;
        this.voto = num;
        this.autore = utente;
        this.strutturaRecensita = struttura;
    }

    public void setAutore(Utente user){
        this.autore = user;
    }

    public Utente getAutore(){
        return this.autore;
    }

    public void setStrutturaRecensita(Struttura struttura){
        this.strutturaRecensita = struttura;
    }

    public Struttura getStrutturaRecensita(){
        return this.strutturaRecensita;
    }
    
    public void setTesto(String txt){

        this.testo = txt;
    }

    public String getTesto(){
        return this.testo;
    }

    public void setVoto(int votazione){
        this.voto = votazione;
    }

    public int getVoto(){
        return this.voto;
    }
}
