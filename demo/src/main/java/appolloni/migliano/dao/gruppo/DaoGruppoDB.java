package appolloni.migliano.dao.gruppo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import appolloni.migliano.entity.Gruppo;
import appolloni.migliano.entity.Utente;
import appolloni.migliano.factory.FactoryDAO;
import appolloni.migliano.factory.FactoryUtenti;
import appolloni.migliano.interfacce.InterfacciaGruppo;
import appolloni.migliano.interfacce.InterfacciaUtente;

public class DaoGruppoDB implements InterfacciaGruppo {

    private final Connection conn;
    private static final String INSERTGRUPPO = "INSERT INTO gruppi (nome, materia_studio, email_admin,citta,luogo) VALUES (?, ?, ?,?,?)";
    private static final String INSERTISCRIZIONE = "INSERT INTO iscrizioni (nome_gruppo, email_utente) VALUES (?, ?)";
    private static final String SELECTCERCAGRUPPO = "SELECT nome, materia_studio, email_admin, citta, luogo FROM gruppi WHERE nome =?";
    private static final String RECUPERAGRUPPI = "SELECT g.nome, g.materia_studio, g.email_admin, g.citta, g.luogo " +
                     "FROM gruppi g " +
                     "JOIN iscrizioni i ON g.nome = i.nome_gruppo " +
                     "WHERE i.email_utente = ?";

    private static final String ESISTEGRUPPO = "SELECT count(*) FROM gruppi WHERE nome = ?";
    private static final String DELETEGRUPPO = "DELETE FROM iscrizioni WHERE nome_gruppo = ? AND email_utente = ?";
    private static final String ELIMINAGRUPPO = "DELETE FROM gruppi WHERE nome = ?";
    private static final String ELIMINAMESSAGGI = "DELETE FROM messaggi WHERE nome_gruppo = ?";
    private static final String ELIMINAISCRIZIONI =  "DELETE FROM iscrizioni WHERE nome_gruppo = ?";

    private static final String SELECTRICERCAFILTRI = "SELECT nome, materia_studio, email_admin, citta, luogo FROM gruppi WHERE 1=1 "; 

    public DaoGruppoDB(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void creaGruppo(Gruppo gruppo) throws SQLException {
        String sqlGruppo = INSERTGRUPPO;
        String sqlIscrizione = INSERTISCRIZIONE;

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlGruppo)) {
                ps.setString(1, gruppo.getNome());
                ps.setString(2, gruppo.getMateria());
                ps.setString(3, gruppo.getAdmin().getEmail());
                ps.setString(4, gruppo.getCitta());
                ps.setString(5, gruppo.getLuogo());
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlIscrizione)) {
                ps.setString(1, gruppo.getNome());
                ps.setString(2, gruppo.getAdmin().getEmail());
                ps.executeUpdate();
            }
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e; 
        } finally {
            
            conn.setAutoCommit(true);
        }
    }

    @Override
   public Gruppo cercaGruppo(String nome) throws SQLException{
        Gruppo gruppoCercato = null;
        String sql = SELECTCERCAGRUPPO;
        
        try (PreparedStatement ps= conn.prepareStatement(sql)){
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()){

                if(rs.next()){
                    String nomeGruppo = rs.getString(1);
                    String materia = rs.getString(2);
                    String admin = rs.getString(3);
                    String citta = rs.getString(4);
                    String luogo = rs.getString(5);

                    InterfacciaUtente dao = FactoryDAO.getDaoUtente();
                    Utente user = dao.cercaUtente(admin);
                
                    gruppoCercato = new Gruppo(nomeGruppo, user);
                    gruppoCercato.setMateria(materia);
                    gruppoCercato.setCitta(citta);
                    gruppoCercato.setLuogo(luogo);
                }
            } 
        } catch (Exception e) {
            throw new SQLException();
        }
        return gruppoCercato;
    }
    @Override
    public List<Gruppo> recuperaGruppiUtente(String emailUtente) throws SQLException {
        List<Gruppo> listaGruppi = new ArrayList<>();
        
 
        String sql =  RECUPERAGRUPPI;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emailUtente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String nome = rs.getString(1);
                    String materia = rs.getString(2);
                    String adminEmail = rs.getString(3);
                    
       
                    String citta = rs.getString(4);
                    String luogo = rs.getString(5);

                    Utente admin = FactoryUtenti.creazione("Studente", null, null, emailUtente, materia, null);
                    admin.setEmail(adminEmail);
                    
                    Gruppo g = new Gruppo(nome, admin);
                    g.setMateria(materia);
                    
                    g.setCitta(citta);
                    g.setLuogo(luogo);
                    
                    listaGruppi.add(g);
                }
            }
        }
        return listaGruppi;
    }
    @Override
    public void iscriviUtente(String nomeGruppo, String emailUtente) throws SQLException {
        String sql = INSERTISCRIZIONE;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nomeGruppo);
            ps.setString(2, emailUtente);
            ps.executeUpdate();
        }
    }
    
    @Override
    public boolean esisteGruppo(String nomeGruppo) throws SQLException{
        String sql = ESISTEGRUPPO;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nomeGruppo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    
   @Override
    public List<Gruppo> ricercaGruppiConFiltri(String nome, String citta, String materia) throws SQLException {
        List<Gruppo> lista = new ArrayList<>();
        InterfacciaUtente daoUtente = FactoryDAO.getDaoUtente();
        
        String sql = SELECTRICERCAFILTRI; 
        
        if (nome != null) sql += "AND nome LIKE ? ";
        if (citta != null) sql += "AND citta LIKE ? ";
        if (materia != null) sql += "AND materia_studio LIKE ? ";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;
            if (nome != null) ps.setString(index++, "%" + nome + "%");
            if (citta != null) ps.setString(index++, "%" + citta + "%");
            if (materia != null) ps.setString(index++, "%" + materia + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Utente u= daoUtente.cercaUtente(rs.getString("email_admin"));
                    Gruppo g = new Gruppo(rs.getString("nome"), u);
                    g.setCitta(rs.getString("citta"));
                    g.setLuogo(rs.getString("luogo"));
                    g.setMateria(rs.getString("materia_studio"));
                    g.setAdmin(u);  
                    lista.add(g);
                }
            }
        }
        return lista;
    }

    @Override
    public void abbandonaGruppo(String nomeGruppo, String emailUtente) throws SQLException {
        String sql = DELETEGRUPPO;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nomeGruppo);
            ps.setString(2, emailUtente);
            ps.executeUpdate();
        }
    }

    @Override
  public void eliminaGruppo(String nomeGruppo) throws SQLException {

    String sqlEliminaMessaggi = ELIMINAMESSAGGI;

    String sqlEliminaIscrizioni = ELIMINAISCRIZIONI;

    String sqlEliminaGruppo = ELIMINAGRUPPO;

    try {
        conn.setAutoCommit(false); 
        eseguiUpdate (sqlEliminaMessaggi,nomeGruppo);
        eseguiUpdate(sqlEliminaIscrizioni,nomeGruppo);
        eseguiUpdate(sqlEliminaGruppo, nomeGruppo);
        conn.commit();
        

    } catch (SQLException e) {
        conn.rollback(); 
        throw e;
    } finally {
        conn.setAutoCommit(true);
    }
}

private void eseguiUpdate(String sql, String parametro) throws SQLException {
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, parametro);
        ps.executeUpdate();
    }
}

}