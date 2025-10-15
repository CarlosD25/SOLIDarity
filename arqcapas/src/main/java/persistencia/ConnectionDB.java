package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionDB {
    
    private static Connection conn;
    
    public static Connection getConnection() throws SQLException {
        
        if (conn == null || conn.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            String URL = "jdbc:postgresql://localhost:5432/arqcapas";  
            String USER = "postgres";
            String PASSWORD = "1976";
            
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        
        return conn;
    }
}
