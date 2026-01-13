package appolloni.migliano.bean;


public class BeanMessaggi {
    private String text;
    private String gruppo; 
    private String mittente;
    
    public void setMess(String testo){

        this.text = testo;
    }
    
    public String getMess(){
        return this.text;
    }

    public void setGruppo(String g){
        this.gruppo = g;

    }
    public String getGruppo(){
        return this.gruppo;
    }
    public void setMittente(String u){
        this.mittente = u;
    }
    public String getMittente(){
        return this.mittente;
    }
}

