/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mapper;

import dto.DonacionRequestDTO;
import dto.DonacionResponseDTO;
import model.Donacion;

/**
 *
 * @author Carlo
 */
public interface DonacionMapper {
    
    Donacion toEntity(DonacionRequestDTO requestDTO);
    DonacionResponseDTO toDTO(Donacion donacion);
    
}
