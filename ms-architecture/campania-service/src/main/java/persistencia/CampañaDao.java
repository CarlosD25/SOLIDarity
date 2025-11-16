/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import model.Campaña;
import model.Status;

/**
 *
 * @author Carlo
 */
public interface CampañaDao {
    
    Campaña save(Campaña campaña);
    void actualizarImagenCampaña(int id, String imagenUrl);
    Campaña findById(int id);
    List<Campaña> getAll();
    void actualizarMontoRecaudado(int id,BigDecimal montoRecaudado);
    boolean existById(int id);
    void updateEstado(int id,  Status nuevoEstado);
    Campaña update(int id, Campaña campaña);
    List<Campaña> findByStatus(String status);
    void actualizarFechaFinalizacion(int id, Timestamp nuevaFechaFinalizacion);
    List<Campaña> findByBeneficiarioId(int beneficiarioId);
    
}
