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
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import persistencia.ConnectionPostgresDB;
import persistencia.UserDao;

/**
 *
 * @author Carlo
 */
public class UserDaoImpl implements UserDao {

    private Connection conn;

    public UserDaoImpl() {

        try {
            this.conn = ConnectionPostgresDB.getInstance();
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, "Ocurrió un error en UserDaoImpl", ex);
        }

    }

    @Override
    public User create(User user) {
        String sql = "insert into users (name, telefono, email, password, address, active) values (?, ?, ?, ?, ?, ?)";
        User u = new User();

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.getName());
                ps.setString(2, user.getTelefono());
                ps.setString(3, user.getEmail());
                ps.setString(4, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
                ps.setString(5, user.getAddress());
                ps.setBoolean(6, user.isActive());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        u.setId(rs.getInt(1));
                        u.setName(rs.getString("name"));
                        u.setTelefono(rs.getString("telefono"));
                        u.setAddress(rs.getString("address"));
                    }

                }
                conn.commit();
                return u;

            } catch (SQLException ex) {
                try {
                    conn.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex1);
                }
                ex.printStackTrace();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new User();
    }

    @Override
    public User getById(int id) {

        String sql = "select u.name, u.telefono, u.address from users u where u.id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    User u = new User();
                    u.setId(id);
                    u.setName(rs.getString("name"));
                    u.setTelefono(rs.getString("telefono"));
                    u.setAddress(rs.getString("address"));
                    return u;

                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return new User();
    }

    @Override
    public List<User> getAll() {

        List<User> users = new MiLista<>();

        String sql = "select u.id, u.name, u.telefono, u.address from users u ";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    User u = new User();

                    u.setId(rs.getInt("id"));
                    u.setName(rs.getString("name"));
                    u.setTelefono(rs.getString("telefono"));
                    u.setAddress(rs.getString("address"));
                    users.add(u);

                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return users;
    }

    @Override
    public User update(int id, User user) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void assignRoleToUser(int userId, int roleId) {
        String sql = "insert into roles_users (id_rol,id_user) values (?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roleId);
            ps.setInt(2, userId);
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

}
