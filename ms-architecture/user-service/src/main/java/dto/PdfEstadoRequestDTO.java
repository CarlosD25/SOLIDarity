/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import model.StatusPDF;

/**
 *
 * @author Carlo
 */
public class PdfEstadoRequestDTO {
    
    private StatusPDF statusPDF;

    public PdfEstadoRequestDTO() {
    }

    public PdfEstadoRequestDTO(StatusPDF statusPDF) {
        this.statusPDF = statusPDF;
    }

    public StatusPDF getStatusPDF() {
        return statusPDF;
    }

    public void setStatusPDF(StatusPDF statusPDF) {
        this.statusPDF = statusPDF;
    }
    
    
}
