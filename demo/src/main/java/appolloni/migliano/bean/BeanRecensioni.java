package appolloni.migliano.bean;

public class BeanRecensioni {

    private String autore;
    private String testo;
    private int voto;
    private String idStruttura;
    private String gestoreStruttura;

    public BeanRecensioni(String autore, String testo, int voto, String idStruttura, String gestore) {
        this.autore = autore;
        this.testo = testo;
        this.voto = voto;
        this.idStruttura = idStruttura;
        this.gestoreStruttura = gestore;
    }

    public String getGestoreStruttura() {
        return gestoreStruttura;
    }

    public void setGestoreStruttura(String gestoreStruttura) {
        this.gestoreStruttura = gestoreStruttura;
    }
    
    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }

    public String getIdStruttura() {
        return idStruttura;
    }

    public void setIdStruttura(String idStruttura) {
        this.idStruttura = idStruttura;
    }
}