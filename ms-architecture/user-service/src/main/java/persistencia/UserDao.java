/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia;

import java.util.List;
import model.User;

/**
 *
 * @author Carlo
 */
public interface UserDao {
    
    User create(User user);
    User getById(int id);
    List<User> getAll();
    User update(int id, User user);
    void delete(int id);
    void assignRoleToUser(int userId, int roleId);
    
}
