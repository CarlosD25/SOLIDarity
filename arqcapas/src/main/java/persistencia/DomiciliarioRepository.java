/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import model.Domiciliario;
import model.Usuario;

/**
 *
 * @author Carlo
 */
public class DomiciliarioRepository {

    private Connection conn;

    public DomiciliarioRepository() {
        try {
            this.conn = ConnectionDB.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DomiciliarioRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Domiciliario create(Domiciliario domiciliario) {

        String sql = "insert into domiciliarios (id_usuario) values(?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, domiciliario.getUsuario().getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        domiciliario.setId(rs.getInt(1));
                        return domiciliario;
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public List<Domiciliario> getAll() {

        String sql = "select d.id as id_domiciliario, "
                + "u.id as usuario_id, u.nombre, u.telefono, u.email, u.password, u.activo "
                + "from domiciliarios d "
                + "join usuarios u on d.id_usuario = u.id";

        List<Domiciliario> list = new MiLista<>();

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Domiciliario d = new Domiciliario();
                d.setId(rs.getInt("id_domiciliario"));

                Usuario u = new Usuario();
                u.setId(rs.getInt("usuario_id"));
                u.setNombre(rs.getString("nombre"));
                u.setTelefono(rs.getString("telefono"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setActivo(rs.getBoolean("activo"));

                d.setUsuario(u);

                list.add(d);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;

    }

    public Domiciliario getById(int id) {
        String sql = "select d.id, d.id_usuario from domiciliarios d where d.id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Domiciliario d = new Domiciliario();
                    d.setId(rs.getInt("id"));

                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id_usuario")); 
                    d.setUsuario(u);

                    return d;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public boolean delete(int domiciliarioId) {
        Domiciliario domiciliario = getById(domiciliarioId);
        if (domiciliario == null) {
            return false;
        }

        int usuarioId = domiciliario.getUsuario().getId();

        String deleteRolesSql = "delete from roles_usuario where id_usuario = ?";
        String deleteDomiciliarioSql = "delete from domiciliarios where id = ?";
        String deleteUsuarioSql = "delete from usuarios where id = ?";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(deleteRolesSql)) {
                ps.setInt(1, usuarioId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(deleteDomiciliarioSql)) {
                ps.setInt(1, domiciliarioId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(deleteUsuarioSql)) {
                ps.setInt(1, usuarioId);
                ps.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;

        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
