package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionPostgresDB {

    private static Connection connection;

    public static Connection getInstance() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ConnectionPostgresDB.class.getName())
                      .log(Level.SEVERE, "No se pudo cargar el driver PostgreSQL", ex);
                throw new SQLException("Driver PostgreSQL no encontrado", ex);
            }

            String USER = "postgres";
            String PASSWORD = "1234";
            String URL = "jdbc:postgresql://localhost:5433/donacion-service-db";

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
