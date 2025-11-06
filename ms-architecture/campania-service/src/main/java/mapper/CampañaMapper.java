/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mapper;

import dto.CampañaRequestDTO;
import dto.CampañaResponseDTO;
import dto.CampañaResumenDTO;
import model.Campaña;

/**
 *
 * @author Carlo
 */
public interface CampañaMapper {
    
    CampañaResponseDTO toResponseDto(Campaña campaña);
    Campaña toEntity(CampañaRequestDTO campañaRequestDTO);
    CampañaResumenDTO toResumenDto(Campaña campaña);
    
}
