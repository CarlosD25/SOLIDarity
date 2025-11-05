/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.impl;

import com.milista.datos.MiLista;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.Sorts;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Rol;
import model.StatusPDF;
import model.User;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;
import persistencia.ConnectionMongoDB;
import persistencia.ConnectionPostgresDB;
import persistencia.UserDao;

/**
 *
 * @author Carlo
 */
public class UserDaoImpl implements UserDao {

    private Connection conn;
    private final MongoDatabase database;

    private final GridFSBucket gridFSBucket;

    public UserDaoImpl() {

        try {
            this.conn = ConnectionPostgresDB.getInstance();
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, "Ocurrió un error en UserDaoImpl", ex);
        }

        this.database = ConnectionMongoDB.getDatabase();
        this.gridFSBucket = GridFSBuckets.create(database, "pdfsUsuarios");
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
                        u.setEmail(rs.getString("email"));
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

        String sql = "select * from users u where u.id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    User u = new User();
                    u.setId(id);
                    u.setName(rs.getString("name"));
                    u.setTelefono(rs.getString("telefono"));
                    u.setAddress(rs.getString("address"));
                    u.setActive(rs.getBoolean("active"));
                    u.setEmail(rs.getString("email"));
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

        String sql = "select u.id, u.name, u.telefono, u.address, u.email from users u ";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    User u = new User();

                    u.setId(rs.getInt("id"));
                    u.setName(rs.getString("name"));
                    u.setTelefono(rs.getString("telefono"));
                    u.setAddress(rs.getString("address"));
                    u.setEmail(rs.getString("email"));
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
    public void deactivate(int id) {

        String sql = "update users set active = false where id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("No se encontró ningún usuario con id = " + id);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

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

    @Override
    public void activate(int id) {
        String sql = "update users set active = true where id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("No se encontró ningún usuario con id = " + id);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void saveUserPDF(User user, StatusPDF estado, String fileName, InputStream file) {
        try {

            Document metadata = new Document()
                    .append("userId", user.getId())
                    .append("nombre", user.getName())
                    .append("email", user.getEmail())
                    .append("estado", estado.name());

            GridFSUploadOptions options = new GridFSUploadOptions()
                    .metadata(metadata);

            gridFSBucket.uploadFromStream(fileName, file, options);

        } catch (Exception e) {
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, "Error al subir PDF", e);
        }
    }

    @Override
    public boolean hasRole(int id, String role) {

        String sql = "select 1 from roles_users where id_user= ? and id_rol = "
                + "(select id from roles where nombre=?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setString(2, role);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next();

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public String getPdfState(int id) {
        MongoCollection<Document> filesCollection = database.getCollection("pdfsUsuarios.files");

        Document query = new Document("metadata.userId", id);  

        Document sort = new Document("uploadDate", -1);  

        Document fileDoc = filesCollection.find(query).sort(sort).first();  

        if (fileDoc != null) {
            Document metadata = (Document) fileDoc.get("metadata");

            if (metadata != null && metadata.containsKey("estado")) {
                return metadata.getString("estado");
            }
        }

        return null;
    }

}
