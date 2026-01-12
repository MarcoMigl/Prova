package appolloni.migliano.entity;
import java.util.ArrayList;


public class Gruppo {
    private String nomeGruppo;
    private ArrayList<Utente> membri;
    private String materiaStudio;
    private String luogo;
    private String citta;
    private Utente admin;
    private ArrayList<Messaggio> messaggi;

    public Gruppo(String nome, Utente u1){
     this.nomeGruppo = nome;
     this.admin = u1;
     this.membri = new ArrayList<Utente>();
     this.messaggi = new ArrayList<Messaggio>();
    }

    public void setLuogo(String newLuogo){
        this.luogo = newLuogo;
    }

    public String getLuogo(){
        return this.luogo;
    }

    public void setCitta(String newCitta){
        this.citta = newCitta;
    }

    public String getCitta(){
        return this.citta;
    }

    public void addMess(Messaggio mess){
        messaggi.add(mess);

    }
    public ArrayList<Messaggio> getMessaggi() {
        return messaggi;
    }
    public Utente getAdmin(){
        return this.admin;

    }

    public void setAdmin(Utente u){

        this.admin = u;
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

    public String setNome(String nome){
       return this.nomeGruppo = nome;
    }

    public boolean aggiungiMembro(Utente utente, Utente aggiunto){
        if(utente.getEmail().equals(aggiunto.getEmail())){
         if(!membri.contains(aggiunto)){
           membri.add(aggiunto);
           return true;
         }else{
           return false;
         }
        }else{
            System.out.println("Impossibile, solo l'admin può effettuare l'operazione");
            return false;
        }
    }

    public boolean rimuoviMembro(Utente utente, Utente rimosso){
        if(utente.getEmail().equals(this.admin.getEmail())){
         if(membri.contains(rimosso)){
             membri.remove(rimosso);
             return true;
         }else{
             return false;
         }
        }else{

            System.out.println("Impossibile, solo l'admin può effettuare l'operazione");
            return false;
        }
    }

    public void stampaMembri(){
        System.out.println("Membri del gruppo: \n");
        for(Utente u : membri){
            System.out.println(u.getName() + u.getCognome()+ "\n");
        }
    }
    
    public boolean cercaMembro(Utente utente){
        return membri.contains(utente);
    }
}
