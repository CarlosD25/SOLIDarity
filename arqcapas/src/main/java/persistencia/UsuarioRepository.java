/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Rol;
import model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Carlo
 */
public class UsuarioRepository {

    private Connection conn;

    public UsuarioRepository() {
        try {
            this.conn = ConnectionDB.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Usuario create(Usuario usuario) {

        String sql = "insert into usuarios (nombre,telefono,email,password,activo) values(?,?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getTelefono());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt()));
            ps.setBoolean(5, usuario.isActivo());

            int rowsAffected = ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {

                    usuario.setId(rs.getInt(1));

                    String sql2 = "insert into roles_usuario (id_rol,id_usuario) values (?,?)";

                    for (Rol rol : usuario.getRoles()) {

                        try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {

                            ps2.setInt(1, rol.getId());
                            ps2.setInt(2, usuario.getId());
                            ps2.executeUpdate();

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                    }

                } else {
                    conn.rollback();
                    return null;
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return usuario;

    }

    public Usuario getById(int id) {
        String sql = "select id, nombre, telefono, email, password, activo from usuarios where id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNombre(rs.getString("nombre"));
                    u.setTelefono(rs.getString("telefono"));
                    u.setEmail(rs.getString("email"));
                    u.setPassword(rs.getString("password"));
                    u.setActivo(rs.getBoolean("activo"));
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
