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
public class CampañaUpdateDTO {
    
    private String titulo;
    private String descripcion;
    private BigDecimal montoObjetivo;

    public CampañaUpdateDTO() {
    }

    public CampañaUpdateDTO(String titulo, String descripcion, BigDecimal montoObjetivo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.montoObjetivo = montoObjetivo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMontoObjetivo() {
        return montoObjetivo;
    }

    public void setMontoObjetivo(BigDecimal montoObjetivo) {
        this.montoObjetivo = montoObjetivo;
    }
    
}
