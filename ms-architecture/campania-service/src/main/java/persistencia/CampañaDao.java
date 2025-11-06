/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia;

import model.Campaña;

/**
 *
 * @author Carlo
 */
public interface CampañaDao {
    
    Campaña save(Campaña campaña);
    String actualizarImagenCampaña(int id, String imagenUrl);
    Campaña findById(int id);
    
}
