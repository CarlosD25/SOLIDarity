/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia;

import java.util.List;
import model.Rol;

/**
 *
 * @author Carlo
 */
public interface RolDao {
    
    boolean existsRol(String name);
    Rol save(Rol rol);
    List<Rol> getByUserId(int id);
    Rol findByName(String name);
    
}
