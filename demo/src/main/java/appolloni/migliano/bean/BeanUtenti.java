package appolloni.migliano.bean;

public class BeanUtenti {
    private String nome;
    private String cognome;
    private String tipo;
    private String email;
    private String password;
    private String citta;
    private String nomeAtt;
    private String tipoAtt;

    public BeanUtenti(String tipo, String nome, String cognome, String email, String password, String citta){
        this.nome = nome;
        this.cognome = cognome;
        this.tipo = tipo;
        this.citta = citta;
        this.email = email;
        this.password = password;


    }


    public String getName(){ 
        return this.nome;
    }


    public void setName(String name){
        this.nome = name;
    }

    public String getCognome(){
        return this.cognome;
    }

    public void setCognome(String cognome){
        this.cognome = cognome;
    }

    public String getTipo(){
        return this.tipo;
    }

    public void setTipo(String type){
        this.tipo = type;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }
    
    public String getPassword(){
        return this.password;
    }

    public void setPassword(String pass){
        this.password = pass;
    }

    public String getCitta(){
        return this.citta;
    }

    public void setCitta(String citta){
        this.citta = citta;
    }

    public void setTipoAttivita(String tipoAtt){
        this.tipoAtt = tipoAtt;
    }

    public String getTipoAttivita(){
        return this.tipoAtt;
    }

    public void setNomeAttivita(String nomeAtt){
        this.nomeAtt = nomeAtt;
    }

    public String getNomeAttivita(){
        return this.nomeAtt;
    }

}
