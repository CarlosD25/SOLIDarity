/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mapper;

import dto.NotificationRequestDTO;
import dto.NotificationResponseDTO;
import model.Notification;

/**
 *
 * @author Carlo
 */
public interface NotificationMapper {
    
    Notification toEntity(NotificationRequestDTO requestDTO);
    NotificationResponseDTO toDto(Notification notification);
    
}
