/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import model.Status;

/**
 *
 * @author Carlo
 */
public class CampañaEstadoRequestDTO {
    
    private Status status;

    public CampañaEstadoRequestDTO(Status status) {
        this.status = status;
    }

    public CampañaEstadoRequestDTO() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    
}
