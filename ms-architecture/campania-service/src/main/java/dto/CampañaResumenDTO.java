/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.math.BigDecimal;
import model.Status;

/**
 *
 * @author Carlo
 */
public class CampañaResumenDTO {
    
    private int id;
    private int idBeneficiario;
    private String titulo;
    private Status status;
    private BigDecimal montoObjetivo;
    private BigDecimal montoRecaudado;

    public CampañaResumenDTO() {
    }

    public CampañaResumenDTO(int id, int idBeneficiario, String titulo, Status status, BigDecimal montoObjetivo, BigDecimal montoRecaudado) {
        this.id = id;
        this.idBeneficiario = idBeneficiario;
        this.titulo = titulo;
        this.status = status;
        this.montoObjetivo = montoObjetivo;
        this.montoRecaudado = montoRecaudado;
    }

    public int getIdBeneficiario() {
        return idBeneficiario;
    }

    public void setIdBeneficiario(int idBeneficiario) {
        this.idBeneficiario = idBeneficiario;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getMontoObjetivo() {
        return montoObjetivo;
    }

    public void setMontoObjetivo(BigDecimal montoObjetivo) {
        this.montoObjetivo = montoObjetivo;
    }

    public BigDecimal getMontoRecaudado() {
        return montoRecaudado;
    }

    public void setMontoRecaudado(BigDecimal montoRecaudado) {
        this.montoRecaudado = montoRecaudado;
    }

    
    
}
