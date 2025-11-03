/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import dto.NotificationRequestDTO;
import dto.NotificationResponseDTO;
import java.util.List;

/**
 *
 * @author Carlo
 */
public interface NotificationService {
    
    NotificationResponseDTO create (NotificationRequestDTO requestDTO);
    List<NotificationResponseDTO> getByUserId(int id);
    
}
