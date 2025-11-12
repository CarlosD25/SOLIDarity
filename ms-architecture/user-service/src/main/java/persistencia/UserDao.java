/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia;

import com.mongodb.client.gridfs.model.GridFSFile;
import java.io.InputStream;
import java.util.List;
import model.StatusPDF;
import model.User;

/**
 *
 * @author Carlo
 */
public interface UserDao {
    
    User create(User user);
    User findById(int id);
    List<User> getAll();
    User updatePostgres(int id, User user);
    void updateMongo(int id, User user);
    void deactivate(int id);
    void activate(int id);
    void assignRoleToUser(int userId, int roleId);
    void saveUserPDF(User user, StatusPDF estado, String fileName, InputStream file);
    boolean hasRole(int id, String role);
    String getPdfState(int id);
    List<User> findUsersWithLastPdfByState(String estado);
    GridFSFile findLatestPdfByUserId(int userId);
    InputStream getPdfStream(GridFSFile file);
    void actualizarPdfEstado(int idUsuario,StatusPDF statusPDF);
    boolean existById(int id);
    User findByEmail(String email);
}
