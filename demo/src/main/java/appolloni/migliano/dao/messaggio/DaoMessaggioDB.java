package appolloni.migliano.dao.messaggio;

import appolloni.migliano.entity.Gruppo;
import appolloni.migliano.entity.Messaggio;
import java.util.*;
import appolloni.migliano.entity.Utente;
import appolloni.migliano.factory.FactoryDAO;
import appolloni.migliano.interfacce.InterfacciaMessaggi;
import appolloni.migliano.interfacce.InterfacciaUtente;

import java.sql.*;

public class DaoMessaggioDB implements InterfacciaMessaggi {
    private static final String CERCAMESSAGGIO = "SELECT testo, nome_gruppo, email_mittente, data_invio FROM messaggi WHERE nome_gruppo = ? ORDER BY data_invio ASC";
    private static final String NUOVOMESS = "INSERT INTO MESSAGGI (testo,nome_gruppo, email_mittente, data_invio ) VALUES (?,?,?,?) ";
   
    private Connection conn;
    public DaoMessaggioDB(Connection connessione){
        this.conn = connessione;
    }
    
    @Override
    public void nuovoMessaggio(Messaggio messaggio) throws SQLException {

        String sql = NUOVOMESS;
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, messaggio.getMess());
            ps.setString(2, messaggio.getGruppo().getNome());
            ps.setString(3,messaggio.getMittente().getEmail() );
            ps.setTimestamp(4, messaggio.getTime());

            ps.executeUpdate();
        } 

    }
    
    @Override
    public List<Messaggio> cercaMessaggio(Gruppo gruppo) throws SQLException{
        List<Messaggio> messaggi = new ArrayList<>();

        String sql = CERCAMESSAGGIO;

        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, gruppo.getNome());
           try( ResultSet rs = ps.executeQuery()){
    
             while(rs.next()){
                 String mess = rs.getString(1);
                 String emailMitt= rs.getString(3);
                 Timestamp time = rs.getTimestamp(4);
                 InterfacciaUtente dao = FactoryDAO.getDaoUtente();
                 Utente mittente = dao.cercaUtente(emailMitt);
                 Messaggio messaggio = new Messaggio(mess, gruppo, mittente);
                 messaggio.setTime(time);
                 messaggi.add(messaggio);
             }

            }
        } 



        return messaggi;
    }
}