/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.math.BigDecimal;

/**
 *
 * @author Carlo
 */
public class DonacionRequestDTO {

    private int idUser;
    private int idCampaña;
    private BigDecimal monto;

    public DonacionRequestDTO() {
    }

    public DonacionRequestDTO(int idUser, int idCampaña, BigDecimal monto) {
        this.idUser = idUser;
        this.idCampaña = idCampaña;
        this.monto = monto;
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

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    
    

}
