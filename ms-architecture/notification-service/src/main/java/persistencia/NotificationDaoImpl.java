/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia;

import com.milista.datos.MiLista;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Notification;

/**
 *
 * @author Carlo
 */
public class NotificationDaoImpl implements NotificationDao {

    private Connection conn;

    public NotificationDaoImpl() {

        try {
            conn = ConnectionPostgresDB.getInstance();
        } catch (SQLException ex) {
            Logger.getLogger(NotificationDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public Notification create(Notification notification) {
        String sql = "INSERT INTO notifications (user_id, message, fecha) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, notification.getIdUser());
            stmt.setString(2, notification.getMessage());
            stmt.setTimestamp(3, notification.getFecha());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear la notificación, no se afectó ninguna fila.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    notification.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Error al crear la notificación, no se obtuvo el ID generado.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Ocurrió un error al crear la notificación: " + e.getMessage());
        }

        return notification;
    }

    @Override
    public List<Notification> findByUserId(int id) {
        String sql = "SELECT * FROM notifications WHERE user_id = ?";
        List<Notification> notifications = new MiLista<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Notification n = new Notification();
                n.setId(rs.getInt("id"));
                n.setIdUser(rs.getInt("user_id"));
                n.setMessage(rs.getString("message"));
                n.setFecha(rs.getTimestamp("fecha"));
                notifications.add(n);
            }

        } catch (SQLException e) {
            System.err.println("Ocurrió un error al listar las notificaciones: " + e.getMessage());
        }

        return notifications;
    }

}
