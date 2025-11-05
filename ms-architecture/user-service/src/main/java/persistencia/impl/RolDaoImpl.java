/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.impl;

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
import model.Roles;
import persistencia.ConnectionPostgresDB;
import persistencia.RolDao;

/**
 *
 * @author Carlo
 */
public class RolDaoImpl implements RolDao {

    private Connection conn;

    public RolDaoImpl() {
        try {
            this.conn = ConnectionPostgresDB.getInstance();
        } catch (SQLException ex) {
            Logger.getLogger(RolDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean existsRol(String name) {

        String sql = "select count(*) from roles r where r.nombre= ? ";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public Rol save(Rol rol) {
        String sql = "insert into roles (nombre) values (?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, rol.getRole().name());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        rol.setId(rs.getInt(1));
                    }
                }
                return rol;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return new Rol();
    }

    @Override
    public List<Rol> getByUserId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Rol findByName(String name) {

        String sql = "select * from roles where nombre = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Rol r = new Rol();
                    r.setId(rs.getInt(1));
                    r.setRole(Roles.valueOf(rs.getString(2)));
                    return r;
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return new Rol();

    }

}
