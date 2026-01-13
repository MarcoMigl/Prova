package appolloni.migliano.dao.strutture;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import appolloni.migliano.entity.Struttura;
import appolloni.migliano.interfacce.InterfacciaDaoStruttura;
import appolloni.migliano.factory.FactoryStrutture;

public class DAOStruttureDB implements InterfacciaDaoStruttura {

    private static final String IMMAGINE = "placeholder.png";
    private final Connection conn;

    private static final String COLONNE = "nome, gestore, tipo, citta, indirizzo, orario_apertura, wifi, ristorazione, tipo_attivita, foto";

    private static final String INSERTSTRUTTURA = "INSERT INTO strutture (nome, gestore, tipo, citta, indirizzo, orario_apertura, wifi, ristorazione, tipo_attivita, foto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SELECTBASE = "SELECT " + COLONNE + " FROM strutture WHERE ";
    
    private static final String CERCASTRUTTURA = SELECTBASE + "nome = ? AND gestore = ?";
    private static final String RICERCAFILTRI = "SELECT " + COLONNE + " FROM strutture WHERE 1=1 "; 
    private static final String RECUPERABYHOST = SELECTBASE + "gestore = ?";
    private static final String RECUPERANOMI = "SELECT nome FROM strutture WHERE citta = ?";
    
    private static final String UPDATESTRUTTURA = "UPDATE strutture SET " +
            "indirizzo = ?, citta = ?, orario_apertura = ?, " + 
            "wifi = ?, ristorazione = ?, tipo_attivita = ?, foto = ? " +
            "WHERE nome = ? AND gestore = ?"; 

    private static final String UPDATEFOTO = "UPDATE strutture SET foto = ? WHERE gestore = ?";

    public DAOStruttureDB(Connection conn){
        this.conn = conn;
    }
 
    @Override
    public void salvaStruttura(Struttura struttura, String email) throws SQLException{
        final String GESTORE_DEFAULT = "system_no_host";
        String gestore = (email != null && !email.isEmpty()) ? email : GESTORE_DEFAULT;

        try (PreparedStatement ps = conn.prepareStatement(INSERTSTRUTTURA)){
            ps.setString(1, struttura.getName());
            ps.setString(2, gestore);
            ps.setString(3, struttura.getTipo());
            ps.setString(4, struttura.getCitta());
            ps.setString(5, struttura.getIndirizzo());
            ps.setString(6, struttura.getOrario());
            ps.setBoolean(7, struttura.hasWifi());
            ps.setBoolean(8, struttura.hasRistorazione());
            ps.setString(9, struttura.getTipoAttivita());
            ps.setString(10, struttura.getFoto());

            ps.executeUpdate();
            
            if (!conn.getAutoCommit()) {
                conn.commit();
            }
        }
    }

    @Override
    public Struttura cercaStruttura(String nome, String gestore) throws SQLException{
        Struttura struttura = null;
        try (PreparedStatement ps = conn.prepareStatement(CERCASTRUTTURA)) {
            ps.setString(1, nome);
            ps.setString(2, gestore);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    struttura = mappaResultSet(rs);
                }
            }
        }
        return struttura;
    }

    @Override
    public List<Struttura> ricercaStruttureConFiltri(String nome, String citta, String tipo) throws SQLException {
        List<Struttura> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(RICERCAFILTRI);

        if (nome != null) {sql.append("AND nome LIKE ? ");}
        if (citta != null) {sql.append("AND citta LIKE ? ");}
        if (tipo != null) {sql.append("AND tipo_attivita = ? ");}

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;
            if (nome != null) ps.setString(index++, "%" + nome + "%");
            if (citta != null) ps.setString(index++, "%" + citta + "%");
            if (tipo != null) ps.setString(index++, tipo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mappaResultSet(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public Struttura recuperaStrutturaPerHost(String emailHost) throws SQLException {
        Struttura struttura = null;
        try (PreparedStatement ps = conn.prepareStatement(RECUPERABYHOST)) {
            ps.setString(1, emailHost);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                     struttura = mappaResultSet(rs);
                }
            }
        } 
        return struttura;
    }
    
    @Override
    public void updateStruttura(Struttura s, String vecchioNome) throws SQLException {
        
        try (PreparedStatement ps = conn.prepareStatement(UPDATESTRUTTURA)) {
            ps.setString(1, s.getIndirizzo());
            ps.setString(2, s.getCitta());
            ps.setString(3, s.getOrario());
            ps.setBoolean(4, s.hasWifi());
            ps.setBoolean(5, s.hasRistorazione());
            ps.setString(6, s.getTipoAttivita());
            ps.setString(7, s.getFoto());
            
         
            ps.setString(8, vecchioNome);
            ps.setString(9, s.getGestore()); 

            int righeAggiornate = ps.executeUpdate();
            if (righeAggiornate == 0) {
                throw new SQLException("Update fallito: Struttura non trovata.");
            }
        }
    }

    @Override
    public void aggiornaFotoStruttura(String emailHost, String nomeNuovaFoto) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(UPDATEFOTO)) {
            ps.setString(1, nomeNuovaFoto);
            ps.setString(2, emailHost);
            ps.executeUpdate();
        }
    }

    @Override
    public List<String> recuperaNomiStrutture(String citta) throws SQLException {
        List<String> nomi = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(RECUPERANOMI)){
            ps.setString(1, citta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                   nomi.add(rs.getString("nome"));
                }
            }
        }
        return nomi;
    }

   
    private Struttura mappaResultSet(ResultSet rs) throws SQLException {
        
        Struttura s = FactoryStrutture.creazioneStrutture(
            rs.getString("tipo"),           
            rs.getString("nome"),           
            rs.getString("citta"),
            rs.getString("indirizzo"),
            rs.getBoolean("wifi"),
            rs.getBoolean("ristorazione")
        );

        String tipoAtt = rs.getString("tipo_attivita");
        String gestore = rs.getString("gestore");
        String foto = rs.getString("foto");
        String ora = rs.getString("orario_apertura");
        if (foto == null || foto.isEmpty()) {
            foto = IMMAGINE;
        }
        s.setFoto(foto);
        s.setTipoAttivita(tipoAtt);
        s.setGestore(gestore);
        s.setOrario(ora);
        
        return s;
    }
}