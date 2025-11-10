/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.InputStream;

/**
 *
 * @author Carlo
 */
public class PdfDTO {
    private InputStream pdfStream;
    private String fileName;

    public PdfDTO() {
    }

    public PdfDTO(InputStream pdfStream, String fileName) {
        this.pdfStream = pdfStream;
        this.fileName = fileName;
    }

    public InputStream getPdfStream() {
        return pdfStream;
    }

    public void setPdfStream(InputStream pdfStream) {
        this.pdfStream = pdfStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
}
