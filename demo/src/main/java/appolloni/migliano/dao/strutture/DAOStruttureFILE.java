package appolloni.migliano.dao.strutture;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import appolloni.migliano.entity.Struttura;
import appolloni.migliano.factory.FactoryStrutture;
import appolloni.migliano.interfacce.InterfacciaDaoStruttura;

public class DAOStruttureFILE  implements InterfacciaDaoStruttura{
    private static final String CSVFILE = "strutture.csv";
    private static final String FORMATOCSV = "%s;%s;%s;%s;%s;%s;%s;%b;%b;%s";

    @Override
    public void salvaStruttura(Struttura s, String email) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSVFILE, true))) {
            String riga = String.format(FORMATOCSV,
                s.getName(), s.getTipo(), s.getCitta(), s.getIndirizzo(), s.getOrario(),
                (email != null ? email : "system_no_host"),
                s.getFoto(), s.hasWifi(), s.hasRistorazione(), s.getTipoAttivita()
            );
            bw.write(riga);
            bw.newLine();
        }
    }

    @Override
    public Struttura cercaStruttura(String nomeStruttura, String gestore) throws IOException{
        File file = new File(CSVFILE);
        
        if(!file.exists()) return null;
        Struttura struttura = null;
        try(BufferedReader br = new BufferedReader(new FileReader (file))) {
            String line;
            while((line = br.readLine()) != null){
                String[] dati = line.split(";");
                if(dati.length >= 10 && dati[0].equals(nomeStruttura)&& dati[5].equals(gestore)){
                    struttura = FactoryStrutture.creazioneStrutture(dati[1], dati[0],dati[2],dati[3], Boolean.parseBoolean(dati[7]), Boolean.parseBoolean(dati[8]));
                    struttura.setTipoAttivita(dati[9]);
                    struttura.setGestore(dati[5]);
                    struttura.setFoto(dati[6]);
                    struttura.setOrario(dati[4]);
                    return struttura;
                }

            }
        }
        return struttura;
        
    }

   @Override
    public List<Struttura> ricercaStruttureConFiltri(String nome, String citta, String tipo) throws IOException {
     List<Struttura> lista = new ArrayList<>();
     File file = new File(CSVFILE);
     if (!file.exists()) {return lista;}

     try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] dati = line.split(";");
            if (dati.length < 10) {continue;} 
            if (soddisfaFiltri(dati, nome, citta, tipo)) {
                lista.add(creaStrutturaDaCsv(dati));
            }
        }
     }
     return lista;
    }


    private boolean soddisfaFiltri(String[] dati, String nome, String citta, String tipo) {

     if (nome != null && !nome.isEmpty() && !dati[0].toLowerCase().contains(nome.toLowerCase())) {
        return false;
     }

     if (citta != null && !citta.isEmpty() && !dati[2].toLowerCase().contains(citta.toLowerCase())) {
        return false;
     }

     return !(tipo != null && !tipo.isEmpty() && !dati[9].equalsIgnoreCase(tipo));
    }


    private Struttura creaStrutturaDaCsv(String[] dati) {
     String foto = dati[6];
     if (foto == null || foto.isEmpty() || "null".equalsIgnoreCase(foto)) {
        foto = "placeholder.png";
     }

     Struttura s = FactoryStrutture.creazioneStrutture(
        dati[1], 
        dati[0],
        dati[2], 
        dati[3],
        Boolean.parseBoolean(dati[7]), 
        Boolean.parseBoolean(dati[8])
     );
     s.setFoto(foto);
     s.setTipoAttivita(dati[9]);
     s.setGestore(dati[5]);
     s.setOrario(dati[4]);
     return s;
    }



    @Override
    public Struttura recuperaStrutturaPerHost(String emailHost) throws IOException {
        File file = new File(CSVFILE);
        if (!file.exists()) { return null;}

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] dati = line.split(";");
                if (dati.length >= 10 && dati[5].equals(emailHost)) {
                    Struttura s = FactoryStrutture.creazioneStrutture(
                        dati[1], 
                        dati[0], 
                        dati[2], 
                        dati[3], 
                        Boolean.parseBoolean(dati[7]), 
                        Boolean.parseBoolean(dati[8])
                    );
                    s.setFoto(dati[6]);
                    s.setTipoAttivita(dati[9]);
                    s.setGestore(dati[5]);
                    s.setOrario(dati[4]);
                    return s;
                }
            }
        }
        return null;
    }

   @Override
   public void updateStruttura(Struttura struttura, String vecchioNome) throws IOException {
    File file = new File(CSVFILE);
    if (!file.exists()) {return;}

     List<String> righeDaRiscrivere = new ArrayList<>();
     boolean trovato = false;

     try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] dati = line.split(";");
    
            if (dati.length > 0 && dati[0].equalsIgnoreCase(vecchioNome)) {
               
                String rigaAggiornata = String.format(FORMATOCSV,
                        struttura.getName(), struttura.getTipo(), struttura.getCitta(),
                        struttura.getIndirizzo(), struttura.getOrario(), struttura.getGestore(),
                        struttura.getFoto(), struttura.hasWifi(), struttura.hasRistorazione(),
                        struttura.getTipoAttivita());
                
                righeDaRiscrivere.add(rigaAggiornata); 
                trovato = true;
            } else {
                righeDaRiscrivere.add(line); 
            }
        }
     } 


      if (trovato) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) { 
            for (String riga : righeDaRiscrivere) {
                bw.write(riga);
                bw.newLine();
            }
        }
     }
    }

    @Override
    public void aggiornaFotoStruttura(String emailHost, String fotoNuova) throws IOException {
        File file = new File(CSVFILE);
        if(!file.exists()) {return;}

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null){
                String[] dati = line.split(";");
                if(dati[5].equalsIgnoreCase(emailHost.toLowerCase())){
                    try(BufferedWriter wr = new BufferedWriter(new FileWriter(file,true))){
                        String newDato = String.format(FORMATOCSV, dati[0],dati[1],dati[2],dati[3],dati[4],dati[5],fotoNuova, dati[7],dati[8],dati[9]);
                        wr.write(newDato);
                        wr.newLine();
                    }
                }

            }
            
        }
        
        
    }

    @Override 
    public List<String> recuperaNomiStrutture(String citta) throws IOException {
        List<String> listaNomi = new ArrayList<>();
        
        File file = new File(CSVFILE);
        if (!file.exists()) {return listaNomi;}

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] dati = line.split(";");
                
                if (dati.length >= 3) {
                    String cittaNelFile = dati[2];
                    
                    if (cittaNelFile != null && cittaNelFile.trim().equalsIgnoreCase(citta.trim())) {
                        listaNomi.add(dati[0]);
                    }
                }
            }
        } 

        return listaNomi;
    }
}
