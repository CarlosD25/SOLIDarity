/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author Carlo
 */
public class DonacionResponseDTO {
    
    private int id;
    private int idUser;
    private int idCampaña;
    private BigDecimal monto;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime fecha;
    

    public DonacionResponseDTO() {
    }

    public DonacionResponseDTO(int id, BigDecimal monto, LocalDateTime fecha, int idUser, int idCampaña) {
        this.id = id;
        this.monto = monto;
        this.fecha = fecha;
        this.idUser = idUser;
        this.idCampaña = idCampaña;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdCampaña() {
        return idCampaña;
    }

    public void setIdCampaña(int idCampaña) {
        this.idCampaña = idCampaña;
    }
    
    
}
