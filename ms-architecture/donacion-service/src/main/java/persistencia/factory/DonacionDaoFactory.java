/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.factory;

import model.TipoDB;
import persistencia.DonacionDao;
import persistencia.DonacionDaoImpl;

/**
 *
 * @author Carlo
 */
public class DonacionDaoFactory {
    
    
    public static DonacionDao getDonacionDao(TipoDB tipo){
        switch (tipo) {
            case POSTGRES:
                return new DonacionDaoImpl();
            default:
                throw new AssertionError();
        }
    }
    
}
