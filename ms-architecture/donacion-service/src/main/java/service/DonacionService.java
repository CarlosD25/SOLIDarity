/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import dto.DonacionRequestDTO;
import dto.DonacionResponseDTO;
import java.util.List;
import model.Donacion;

/**
 *
 * @author Carlo
 */
public interface DonacionService {
    
    DonacionResponseDTO save(DonacionRequestDTO requestDTO);
    List<DonacionResponseDTO> getByCampañaId(int id);
    
}
