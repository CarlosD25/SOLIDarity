/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia;

import java.util.List;
import model.Notification;

/**
 *
 * @author Carlo
 */
public interface NotificationDao {
    
    Notification create(Notification notification);
    List<Notification> findByUserId(int id);
    
}
