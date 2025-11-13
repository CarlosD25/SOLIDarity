/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.factory;

import model.TipoDB;
import persistencia.UserDao;
import persistencia.impl.UserDaoImpl;


/**
 *
 * @author Carlo
 */
public class UserDaoFactory {

    public static UserDao getUserDao(TipoDB tipo) {
        switch (tipo) {
            case POSTGRES:
                return new UserDaoImpl();
            default:
                throw new IllegalArgumentException("Tipo de base de datos no soportado: " + tipo);
        }
    }
}
