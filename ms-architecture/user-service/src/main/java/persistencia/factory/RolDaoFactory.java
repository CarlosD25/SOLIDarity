/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.factory;

import model.TipoDB;
import persistencia.RolDao;
import persistencia.impl.RolDaoImpl;

/**
 *
 * @author Carlo
 */
public class RolDaoFactory {
    
    public static RolDao getRolDao(TipoDB tipo){
        switch (tipo) {
            case POSTGRES:
                return new RolDaoImpl();
            default:
                throw new IllegalArgumentException("Tipo de base de datos no soportado: " + tipo);
        }
    }
    
}
