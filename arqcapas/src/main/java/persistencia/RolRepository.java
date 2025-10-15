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
import model.Rol;

/**
 *
 * @author Carlo
 */
public class RolRepository {

    private Connection conn;

    public RolRepository() {
        try {
            this.conn = ConnectionDB.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(RolRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean existRol(String rolName) {
        String sql = "select count(*) from roles WHERE nombre = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rolName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public Rol save(Rol rol) {

        String sql = "insert into roles (nombre) values (?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, rol.getNombre());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        rol.setId(id);
                        return rol;
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

    public Rol findByName(String nombre) {

        String sql = "select * from roles where nombre = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Rol r = new Rol();
                    r.setId(rs.getInt(1));
                    r.setNombre(rs.getString(2));
                    return r;
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;

    }

    public List<Rol> getRolesByUsuarioId(int idUsuario) {
        List<Rol> roles = new MiLista<>();
        String sql = "select r.id, r.nombre "
                + "from roles r "
                + "join roles_usuario ur on r.id = ur.id_rol "
                + "where ur.id_usuario = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Rol rol = new Rol();
                    rol.setId(rs.getInt("id"));
                    rol.setNombre(rs.getString("nombre"));
                    roles.add(rol);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

}
