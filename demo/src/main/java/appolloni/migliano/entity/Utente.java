package appolloni.migliano.entity;

public abstract class Utente {

    private String nome;
    private String cognome;
    private String email;
    private String citta;
    private String tipo;
    private String password;

    protected Utente(String nomeUtente, String cognomeUtente, String emailUtente, String cittaUtente, String tipoUtente, String ps){

        this.nome = nomeUtente;
        this.cognome = cognomeUtente;
        this.email = emailUtente;
        this.citta = cittaUtente;
        this.tipo = tipoUtente;
        this.password = ps;
    }

    public void seTPass(String pass){
        this.password = pass;

    }

    public String getPass(){

        return this.password;
    }

    public void setName(String nomeUtente){
        this.nome = nomeUtente;
    }

    public void setCognome(String cognomeUtente){
        this.cognome = cognomeUtente;
    }

    public void setEmail(String emailUtente){
        this.email = emailUtente;
    }

    public void setCitta(String cittaUtente){
        this.citta = cittaUtente;
    }

    public void setTipo(String tipoUtente){
        this.tipo = tipoUtente;
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
