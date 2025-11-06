/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import model.Status;

/**
 *
 * @author Carlo
 */
public class CampañaResponseDTO {
    
    private int id;
    private int idBeneficiario;
    private String titulo;
    private Status status;
    private String descripcion;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime fechaInicio;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime fechaFinalizacion;
    private BigDecimal montoObjetivo;
    private BigDecimal montoRecaudado;
    private String imagenUrl;

    public CampañaResponseDTO() {
    }

    public CampañaResponseDTO(int id, int idBeneficiario, String titulo, Status status, String descripcion, LocalDateTime fechaInicio, LocalDateTime fechaFinalizacion, BigDecimal montoObjetivo, BigDecimal montoRecaudado, String imagenUrl) {
        this.id = id;
        this.idBeneficiario = idBeneficiario;
        this.titulo = titulo;
        this.status = status;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFinalizacion = fechaFinalizacion;
        this.montoObjetivo = montoObjetivo;
        this.montoRecaudado = montoRecaudado;
        this.imagenUrl = imagenUrl;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
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

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public int getIdBeneficiario() {
        return idBeneficiario;
    }

    public void setIdBeneficiario(int idBeneficiario) {
        this.idBeneficiario = idBeneficiario;
    }

    
    
}
