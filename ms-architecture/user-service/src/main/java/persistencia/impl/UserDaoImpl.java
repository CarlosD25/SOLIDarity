/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.impl;

import com.milista.datos.MiLista;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import config.Config;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.StatusPDF;
import model.User;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;
import persistencia.connection.ConnectionMongoDB;
import persistencia.connection.ConnectionPostgresDB;
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
        this.gridFSBucket = GridFSBuckets.create(database, "" + Config.get("MONGO_BUCKET"));
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
        return null;
    }

    @Override
    public User findById(int id) {

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

        return null;
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
    public User updatePostgres(int id, User user) {
        StringBuilder sql = new StringBuilder("update users set ");
        List<Object> params = new ArrayList<>();

        if (user.getName() != null) {
            sql.append("name = ?, ");
            params.add(user.getName());
        }

        if (user.getTelefono() != null) {
            sql.append("telefono = ?, ");
            params.add(user.getTelefono());
        }

        if (user.getAddress() != null) {
            sql.append("address = ?, ");
            params.add(user.getAddress());
        }

        if (user.getPassword() != null) {
            sql.append("password = ?, ");
            params.add(user.getPassword());
        }

        if (user.getEmail() != null) {
            sql.append("email = ?, ");
            params.add(user.getEmail());
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            params.add(hashedPassword);
        }

        if (params.isEmpty()) {
            return findById(id);
        }

        sql.setLength(sql.length() - 2);
        sql.append(" where id = ?");
        params.add(id);

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return findById(id);
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
                    .append("user_id", user.getId())
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
        MongoCollection<Document> filesCollection = database.getCollection(Config.get("MONGO_BUCKET") + ".files");

        Document query = new Document("metadata.user_id", id);

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

    @Override
    public List<User> findUsersWithLastPdfByState(String estado) {

        Map<Integer, User> lastPdfPerUser = new LinkedHashMap<>();

        try (MongoCursor<GridFSFile> cursor = gridFSBucket.find(Filters.eq("metadata.estado", estado))
                .sort(Sorts.descending("uploadDate"))
                .iterator()) {
            while (cursor.hasNext()) {
                GridFSFile file = cursor.next();
                Document metadata = file.getMetadata();
                int userId = metadata.getInteger("user_id");

                if (!lastPdfPerUser.containsKey(userId)) {
                    User user = new User();
                    user.setId(userId);
                    user.setName(metadata.getString("nombre"));
                    user.setEmail(metadata.getString("email"));

                    lastPdfPerUser.put(userId, user);
                }
            }
        }

        return new ArrayList<>(lastPdfPerUser.values());
    }

    @Override
    public GridFSFile findLatestPdfByUserId(int userId) {
        try (MongoCursor<GridFSFile> cursor = gridFSBucket.find(
                Filters.eq("metadata.user_id", userId))
                .sort(Sorts.descending("uploadDate"))
                .limit(1)
                .iterator()) {

            if (cursor.hasNext()) {
                return cursor.next();
            }
        }
        return null;
    }

    @Override
    public InputStream getPdfStream(GridFSFile file) {
        return gridFSBucket.openDownloadStream(file.getObjectId());
    }

    @Override
    public void actualizarPdfEstado(int idUsuario, StatusPDF statusPDF) {
        MongoCollection<Document> filesCollection = database.getCollection(Config.get("MONGO_BUCKET") + ".files");

        Document query = new Document("metadata.user_id", idUsuario);
        Document sort = new Document("uploadDate", -1);
        Document ultimoPdf = filesCollection.find(query).sort(sort).first();

        if (ultimoPdf != null) {
            Document metadata = (Document) ultimoPdf.get("metadata");
            if (metadata != null) {
                metadata.put("estado", statusPDF.name());

                filesCollection.updateOne(
                        new Document("_id", ultimoPdf.getObjectId("_id")),
                        new Document("$set", new Document("metadata", metadata))
                );
            }
        }
    }

    @Override
    public boolean existById(int id) {
        String sql = "select 1 from users where id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next();

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;

        }
    }

    @Override
    public void updateMongo(int id, User user) {
        MongoCollection<Document> filesCollection = database.getCollection(Config.get("MONGO_BUCKET") + ".files");

        Document fieldsToUpdate = new Document();
        if (user.getName() != null) {
            fieldsToUpdate.append("metadata.nombre", user.getName());
        }
        if (user.getEmail() != null) {
            fieldsToUpdate.append("metadata.email", user.getEmail());
        }

        if (fieldsToUpdate.isEmpty()) {
            return;
        }

        filesCollection.updateMany(
                new Document("metadata.user_id", id),
                new Document("$set", fieldsToUpdate)
        );
    }

    @Override
    public User findByEmail(String email) {
        String sql = "select * from users where email = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setName(rs.getString("name"));
                    u.setTelefono(rs.getString("telefono"));
                    u.setAddress(rs.getString("address"));
                    u.setEmail(rs.getString("email"));
                    u.setActive(rs.getBoolean("active"));
                    u.setPassword(rs.getString("password"));
                    return u;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, "Error al buscar usuario por email", ex);
        }

        return null;
    }

}
