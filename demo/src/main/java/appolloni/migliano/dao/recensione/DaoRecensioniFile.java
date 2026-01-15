package appolloni.migliano.dao.recensione;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import appolloni.migliano.entity.Recensione;
import appolloni.migliano.entity.Struttura;
import appolloni.migliano.entity.Utente;
import appolloni.migliano.factory.FactoryDAO;
import appolloni.migliano.interfacce.InterfacciaDaoRecensioni;
import appolloni.migliano.interfacce.InterfacciaDaoStruttura;
import appolloni.migliano.interfacce.InterfacciaUtente;

public class DaoRecensioniFile implements InterfacciaDaoRecensioni {

    private static final String CSVFILE = "recensioni.csv";
    private static final String FORMATOCSV = "%s;%s;%s;%d;%s";


    @Override
    public void salvaRecensione(Recensione r) throws IOException {
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSVFILE, true))) {
            
            String testoPulito = r.getTesto().replace(";", ",").replace("\n", " ");
            
            String riga = String.format(FORMATOCSV,
                    r.getAutore().getEmail(),             
                    r.getStrutturaRecensita().getName(),
                    r.getStrutturaRecensita().getGestore(),
                    r.getVoto(),
                    testoPulito
            );

            bw.write(riga);
            bw.newLine();
        }
    }

    @Override
    public List<Recensione> getRecensioniByStruttura(String nomeStr, String gestore) throws IOException,SQLException {
        List<Recensione> lista = new ArrayList<>();
        File file = new File(CSVFILE);

        if (!file.exists()) {return lista;}
        InterfacciaDaoStruttura daoStruttura = FactoryDAO.getDAOStrutture();
        InterfacciaUtente daoUtente = FactoryDAO.getDaoUtente();
        Struttura strutturaTarget = null;


        
        strutturaTarget = daoStruttura.cercaStruttura(nomeStr, gestore);
    
        if (strutturaTarget == null) { return lista;}

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] dati = line.split(";");
                if (dati.length < 5) continue; 

                String csvEmail = dati[0];
                String csvNomeStr = dati[1];
                String csvGestoreStr = dati[2];
                int csvVoto = Integer.parseInt(dati[3]);
                String csvTesto = dati[4];

                if (csvNomeStr.equals(nomeStr) && csvGestoreStr.equals(gestore)) {
                    
                    Utente autore = daoUtente.cercaUtente(csvEmail);
                    
                    if (autore != null) {
                        Recensione r = new Recensione(csvTesto, csvVoto, autore, strutturaTarget);
                        lista.add(r);
                    }
                }
            }
        } 
        return lista;
    }
}