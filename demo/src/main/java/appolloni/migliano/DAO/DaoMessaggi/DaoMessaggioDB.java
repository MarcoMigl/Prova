package appolloni.migliano.DAO.DaoMessaggi;


import appolloni.migliano.entity.Gruppo;
import appolloni.migliano.entity.Messaggio;
import java.util.*;
import appolloni.migliano.entity.Utente;
import appolloni.migliano.factory.FactoryDAO;
import appolloni.migliano.interfacce.InterfacciaMessaggi;
import appolloni.migliano.interfacce.InterfacciaUtente;

import java.sql.*;

public class DaoMessaggioDB implements InterfacciaMessaggi {

   
    private Connection conn;
    public DaoMessaggioDB(Connection connessione){
        this.conn = connessione;
    }
    
    @Override
    public void nuovoMessaggio(Messaggio messaggio) throws SQLException {

        String sql = "INSERT INTO MESSAGGI (testo,nome_gruppo, email_mittente, data_invio ) VALUES (?,?,?,?) ";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, messaggio.getMess());
            ps.setString(2, messaggio.getGruppo().getNome());
            ps.setString(3,messaggio.getMittente().getEmail() );
            ps.setTimestamp(4, messaggio.getTime());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }
    
    @Override
    public List<Messaggio> cercaMessaggio(Gruppo gruppo) throws SQLException, Exception{
        List<Messaggio> messaggi = new ArrayList<>();

        String sql = "SELECT * FROM messaggi WHERE nome_gruppo = ? ORDER BY data_invio ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, gruppo.getNome());
           try( ResultSet rs = ps.executeQuery()){
    
             while(rs.next()){
                 String mess = rs.getString("testo");
                 String emailMitt= rs.getString("email_mittente");
                 Timestamp time = rs.getTimestamp("data_invio");
                 InterfacciaUtente dao = FactoryDAO.getDaoUtente();
                 Utente mittente = dao.cercaUtente(emailMitt);
                 Messaggio messaggio = new Messaggio(mess, gruppo, mittente);
                 messaggio.setTime(time);
                 messaggi.add(messaggio);
             }

            }catch(SQLException e){
                e.printStackTrace();
                throw e;

            }catch(Exception e){
                e.printStackTrace();
                throw e;

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }



        return messaggi;
    }
}