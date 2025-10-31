/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia;

import java.util.List;
import model.Donacion;

/**
 *
 * @author Carlo
 */
public interface DonacionDao {
    
    Donacion save(Donacion donacion);
    List<Donacion> getByCampañaId(int id);
    
}
