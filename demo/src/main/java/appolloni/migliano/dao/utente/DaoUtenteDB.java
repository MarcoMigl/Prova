package appolloni.migliano.dao.utente;

import java.sql.*;
import appolloni.migliano.entity.Utente;
import appolloni.migliano.factory.FactoryUtenti;
import appolloni.migliano.entity.Host;
import appolloni.migliano.interfacce.InterfacciaUtente;

public class DaoUtenteDB implements InterfacciaUtente{
    private static final String SALVAUTENTE = "INSERT INTO utenti(dtype,nome,cognome,email,citta,password,nome_attivita,tipo_attivita)" + "VALUES (?,?,?,?,?,?,?,?)";
    private static final String CERCAUTENTE = "SELECT dtype, nome, cognome, email, citta, password, nome_attivita, tipo_attivita FROM utenti WHERE email = ?" ;
    private static final String UPDATE = "UPDATE utenti SET password = ? WHERE email = ?";
    private Connection conn; 
    public DaoUtenteDB(Connection connessione){
        this.conn = connessione;
    }

    @Override
    public void salvaUtente(Utente u) throws SQLException{

        String sql = SALVAUTENTE;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(2, u.getName());
            ps.setString(3, u.getCognome());
            ps.setString(4, u.getEmail());
            ps.setString(5, u.getCitta());
            ps.setString(6, u.getPass());

            if(u instanceof Host host){
                ps.setString(1, "Host");
                ps.setString(7, host.getNomeAttivita());
                ps.setString(8, host.getTipoAttivita());

            }else{
                ps.setString(1,"Studente");
                ps.setNull(7, Types.VARCHAR);
                ps.setNull(8, Types.VARCHAR);
            }
            ps.executeUpdate();
        } 
    }

    @Override
    public Utente cercaUtente(String search) throws SQLException{
        String sql = CERCAUTENTE;
        Utente u = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, search);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
             String tipo = rs.getString("dtype");
             if("Host".equals(tipo)){
                u = FactoryUtenti.creazione(tipo, rs.getString("nome"),rs.getString("cognome"),rs.getString("email"),rs.getString("citta"),rs.getString("password"));
                ((Host)u).setTipoAttivita(rs.getString("tipo_attivita"));
                ((Host)u).setNomeAttivita(rs.getString("nome_attivita"));
             }else{
                u = FactoryUtenti.creazione(tipo, rs.getString("nome"),rs.getString("cognome"),rs.getString("email"),rs.getString("citta"),rs.getString("password"));


             }
            }
        } 

        return u;
    }


    @Override
    public void aggiornaPassword(String email, String nuovaPass) throws SQLException{
        String sql = UPDATE;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuovaPass);
            ps.setString(2, email);
            ps.executeUpdate();
        } 
    }

}