/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author Carlo
 */
public class Donacion {
    
    private int id;
    private int idUser;
    private int idCampaña;
    private BigDecimal monto;
    private Timestamp fecha;
    

    public Donacion() {
    }

    public Donacion(int id, BigDecimal monto, Timestamp fecha, int idUser, int idCampaña) {
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

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
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
