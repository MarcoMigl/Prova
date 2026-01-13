package appolloni.migliano.entity;



public class Host extends Utente {
    private String nomeAttivita;
    private String tipoAttivita;
    private Struttura struttura;
  
     public Host(String nome, String cognome, String email, String citta,String tipo, String password){
        super(nome, cognome,email,citta,tipo, password);
        struttura = null;
        
    }
    public void setNomeAttivita(String nomeAttivita){
        this.nomeAttivita = nomeAttivita;
    }   

    public void setTipoAttivita(String tipoAttivita){
        this.tipoAttivita = tipoAttivita;
    }   

    public String getNomeAttivita(){
        return this.nomeAttivita;
    }
    public String getTipoAttivita(){
        return this.tipoAttivita;
    }  

    public void setStruttura(Struttura str){
        this.struttura = str;
    }

    public Struttura getStruttura(){
        return this.struttura;
    }
}
