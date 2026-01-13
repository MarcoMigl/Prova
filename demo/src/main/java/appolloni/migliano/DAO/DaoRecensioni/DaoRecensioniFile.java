package appolloni.migliano.DAO.DaoRecensioni;

import java.io.*;
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

    private static final String CSV_FILE = "recensioni.csv";

    public DaoRecensioniFile() {
      
    }

    @Override
    public void salvaRecensione(Recensione r) throws IOException {
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            
            String testoPulito = r.getTesto().replace(";", ",").replace("\n", " ");
            
            String riga = String.format("%s;%s;%s;%d;%s",
                    r.getAutore().getEmail(),             
                    r.getStruttura_recensita().getName(),
                    r.getStruttura_recensita().getGestore(),
                    r.getVoto(),
                    testoPulito
            );

            bw.write(riga);
            bw.newLine();
        }catch(IOException e){
            e.printStackTrace();
            throw new IOException("Errore salvataggio su FILE.");

        }
    }

    @Override
    public List<Recensione> getRecensioniByStruttura(String nomeStr, String gestore) throws IOException,Exception {
        List<Recensione> lista = new ArrayList<>();
        File file = new File(CSV_FILE);

        if (!file.exists()) return lista;
        InterfacciaDaoStruttura daoStruttura = FactoryDAO.getDAOStrutture();
        InterfacciaUtente daoUtente = FactoryDAO.getDaoUtente();
        Struttura strutturaTarget = null;


        try{
         strutturaTarget = daoStruttura.cercaStruttura(nomeStr, gestore);
        }catch(Exception e){
            e.printStackTrace();
            throw e;

        }
        if (strutturaTarget == null) return lista;

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
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Errore salvataggio su file");
        }
        
        return lista;
    }
}