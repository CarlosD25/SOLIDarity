/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.factory;

import model.TipoDB;
import persistencia.NotificationDao;
import persistencia.impl.NotificationDaoImplMongo;
import persistencia.impl.NotificationDaoImplPostgres;

/**
 *
 * @author Carlo
 */
public class NotificationDaoFactory {

    public static NotificationDao getNotificationDao(TipoDB tipo) {
        switch (tipo) {
            case POSTGRES:
                return new NotificationDaoImplPostgres();
            case MONGO:
                return new NotificationDaoImplMongo();
            default:
                throw new IllegalArgumentException("Tipo de base de datos no soportado: " + tipo);
        }
    }
}
