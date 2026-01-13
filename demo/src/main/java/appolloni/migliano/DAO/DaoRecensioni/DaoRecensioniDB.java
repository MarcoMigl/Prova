package appolloni.migliano.DAO.DaoRecensioni;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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



public class DaoRecensioniDB implements InterfacciaDaoRecensioni {

    private final Connection conn;
    private final InterfacciaUtente daoUtente;
    private final InterfacciaDaoStruttura daoStruttura;

    public DaoRecensioniDB(Connection conn) {
        this.conn = conn;
        this.daoStruttura = FactoryDAO.getDAOStrutture();
        this.daoUtente = FactoryDAO.getDaoUtente();
    }

    @Override
    public void salvaRecensione(Recensione r) throws SQLException {

        String sql = "INSERT INTO recensioni(autore, nome_struttura, gestore_struttura, voto, testo) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        
        String emailPulita = r.getAutore().getEmail().trim();
        System.out.println("DEBUG DAO - Email: [" + emailPulita + "] - Lunghezza: " + emailPulita.length());
        
    
        ps.setString(1, emailPulita); 
        ps.setString(2, r.getStruttura_recensita().getName().trim()); 
        ps.setString(3, r.getStruttura_recensita().getGestore().trim()); 
        ps.setInt(4, r.getVoto()); 
        ps.setString(5, r.getTesto().trim()); 

        ps.executeUpdate();

        if (!conn.getAutoCommit()) {
            conn.commit();
        }
    }catch(SQLException e){
        e.printStackTrace();
        throw e;
    }

    }

    public List<Recensione> getRecensioniByStruttura(String idStruttura, String nomeGestore) throws SQLException, Exception {
        List<Recensione> lista = new ArrayList<>();
        String sql = "SELECT * FROM recensioni WHERE nome_struttura = ? AND gestore_struttura = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idStruttura);
            ps.setString(2,nomeGestore);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try{
                     Struttura struttura = daoStruttura.cercaStruttura(rs.getString("nome_struttura"),rs.getString("gestore_struttura"));
                     Utente autore = daoUtente.cercaUtente(rs.getString("autore"));
                     Recensione r = new Recensione(rs.getString("testo"),rs.getInt("voto"),autore,struttura);
                     lista.add(r);
                    }catch(SQLException e){
                        e.printStackTrace();
                        throw e;
                    }catch(Exception e){
                        e.printStackTrace();
                        throw e;
                        
                    }
                }
            }catch(SQLException e){
                e.printStackTrace();
                throw e;

            }
        }
        return lista;
    }
}