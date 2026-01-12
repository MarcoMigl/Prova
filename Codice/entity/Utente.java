package appolloni.migliano.entity;

public abstract class Utente {

    private String nome;
    private String cognome;
    private String email;
    private String citta;
    private String tipo;
    private String password;

    public Utente(String nome_utente, String cognome_utente, String email_utente, String citta_utente, String tipo_utente, String ps){

        this.nome = nome_utente;
        this.cognome = cognome_utente;
        this.email = email_utente;
        this.citta = citta_utente;
        this.tipo = tipo_utente;
        this.password = ps;
    }

    public void seTPass(String pass){
        this.password = pass;

    }

    public String getPass(){

        return this.password;
    }

    public void setName(String nome_utente){
        this.nome = nome_utente;
    }

    public void setCognome(String cognome_utente){
        this.cognome = cognome_utente;
    }

    public void setEmail(String email_utente){
        this.email = email_utente;
    }

    public void setCitta(String citta_utente){
        this.citta = citta_utente;
    }

    public void setTipo(String tipo_utente){
        this.tipo = tipo_utente;
    }

    public String getName(){
        return this.nome;
    }

    public String getCognome(){
        return this.cognome;
    }

    public String getEmail(){
        return email;
    }
    public String getCitta(){
        return citta;

    }
    public String getTipo(){
        return tipo;
    }
}
