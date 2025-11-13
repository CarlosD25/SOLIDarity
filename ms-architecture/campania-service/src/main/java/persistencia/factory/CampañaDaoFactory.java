/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.factory;

import model.TipoDB;
import persistencia.CampañaDao;
import persistencia.CampañaDaoImpl;

/**
 *
 * @author Carlo
 */
public class CampañaDaoFactory {
 
    
    public static CampañaDao getCampañaDao(TipoDB tipo){
        switch (tipo) {
            case POSTGRES:
                return new CampañaDaoImpl();
            default:
                throw new AssertionError();
        }
    }
}
