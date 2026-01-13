package appolloni.migliano;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection conn;

    // Parametri DB
    private static final String URL = "jdbc:mysql://localhost:3306/corso_java2?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // user di MySQL
    private static final String PWD = "Micuzzo"; // password MySQL

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                conn = DriverManager.getConnection(URL, USER, PWD);
                System.out.println("Nuova connessione al DB aperta.");
            } catch (SQLException e) {
                System.err.println("ERRORE: Impossibile connettersi al DB.");
                e.printStackTrace();
                throw e; // rilancio l'eccezione per fermare il flusso
            }
        }
        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connessione al DB chiusa.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
