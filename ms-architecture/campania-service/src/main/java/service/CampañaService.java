/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import dto.CampañaEstadoRequestDTO;
import dto.CampañaRequestDTO;
import dto.CampañaResponseDTO;
import dto.CampañaImagenDTO;
import dto.CampañaResumenDTO;
import dto.CampañaUpdateDTO;
import dto.DonacionRequestDTO;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author Carlo
 */
public interface CampañaService {
    
    CampañaResponseDTO create(CampañaRequestDTO campañaRequestDTO);
    CampañaImagenDTO actualizarImagenCampaña(int id, InputStream imagen);
    List<CampañaResumenDTO> getAll();
    CampañaResponseDTO getById(int id);
    void actualizarMontoRecaudado(DonacionRequestDTO donacionRequestDTO);
    void actualizarEstado(int id, CampañaEstadoRequestDTO request);
    CampañaResponseDTO update(int id, CampañaUpdateDTO campañaUpdateDTO);
    List<CampañaResumenDTO> getByStatus(String status);
    
}
