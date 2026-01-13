package appolloni.migliano.bean;


public class BeanGruppo {
    private String nomeGruppo;
    private String materiaStudio;
    private String admin;
    private String luogo;
    private String citta;
    

    public BeanGruppo(String nome,String materia, String emailAd, String luogoStudio, String cittaStudio){
     this.nomeGruppo = nome;
     this.admin =emailAd;
     this.materiaStudio = materia;
     this.luogo = luogoStudio;
     this.citta = cittaStudio;
    }

    public void setCitta(String newCitta){
        this.citta = newCitta;
    }

    public String getCitta(){
       return this.citta; 
    }

    public void setLuogo(String newLuogo){
        this.luogo=newLuogo;
    }

    public String getLuogo(){
        return this.luogo;
    }

    public String getAdmin(){
        return this.admin;

    }

    public void setAdmin(String email){
        this.admin = email;

    }

    public void setMateria(String materia){
        this.materiaStudio = materia;

    }

    public String getMateria(){
        return this.materiaStudio;
    }

    public String getNome(){
        return this.nomeGruppo;
    }

    public void setNome(String nome){
       this.nomeGruppo = nome;
    }

   
    
}
