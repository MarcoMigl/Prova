package appolloni.migliano;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static DBConnection instance = null;
    private Connection conn = null;

    
    private DBConnection() {
        
    }

    // metodo per ottenere l'istanza 
    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

  
    public Connection getConnection() throws SQLException {
        if (this.conn == null || this.conn.isClosed()) {
            
            Properties props = loadConfig(); 

            try {
                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String pwd = props.getProperty("db.password");

                this.conn = DriverManager.getConnection(url, user, pwd);

            } catch (SQLException e) {
                throw new SQLException("Errore di connessione al Database", e);
            }
        }
        return this.conn;
    }
    private Properties loadConfig() throws SQLException {
        Properties props = new Properties();
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new SQLException("File config.properties non trovato nelle risorse!");
            }
            props.load(input);
            return props;
        } catch (IOException e) {
            throw new SQLException("Errore lettura config.properties", e);
        }
    }
    public void closeConnection() throws SQLException {
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        
    }
}