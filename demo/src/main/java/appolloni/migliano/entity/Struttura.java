package appolloni.migliano.entity;

import java.util.ArrayList;

public class Struttura {


    private String tipoAttivita; 
    private String tipoStruttura; 
    private String nomeStruttura; 
    private String citta;
    private String indirizzo;
    private boolean wifi;
    private boolean ristorazione;
    private String orario;
    private String gestore;
    private  ArrayList<Recensione> recensioni;
    private String foto;


    public Struttura(String tipo, String nome, String citta, String address, boolean wifi, boolean ristorazione){
        this.tipoStruttura = tipo;
        this.nomeStruttura = nome;
        this.citta = citta;
        this.indirizzo = address;
        this.wifi = wifi;
        this.ristorazione = ristorazione;
        this.recensioni = new ArrayList<>();
    }


    

    public void setFoto(String immagine){
        this.foto = immagine;
    }

    public String getFoto(){
        return this.foto;
    }


    public void setTipoAttivita(String type){
        this.tipoAttivita = type;
    }

    public String getTipoAttivita(){
        return this.tipoAttivita;
    }
    
    public void setGestore(String gest){
        this.gestore = gest;

    }

    public String getGestore(){
        return this.gestore;
    }

    public void setWifi(boolean wifi){
        this.wifi = wifi;
    }

    public void setRistorazione(boolean ristorazione){
        this.ristorazione = ristorazione;
    }

    public void setOrario(String orario){
        this.orario = orario;
    }

    public void setIndirizzo(String indirizzo){
        this.indirizzo = indirizzo;
    }

    public void setCitta(String citta){
        this.citta = citta;
    }   

    public void setName(String name){
        this.nomeStruttura = name;
    }   

    public boolean hasWifi(){
        return this.wifi;
    }

    public boolean hasRistorazione(){
        return this.ristorazione;
    }

    public String getTipo(){
        return this.tipoStruttura;

    }

    public String getName(){
        return this.nomeStruttura;
    }

    public String getCitta(){
        return this.citta;
    }

    public String getIndirizzo(){
        return this.indirizzo;
    }

    public String getOrario(){
        return this.orario;
    }

    public void aggiungiRecensione(Recensione recensione){
        recensioni.add(recensione);
    }

   
}
