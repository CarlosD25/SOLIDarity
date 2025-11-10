/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mapper;

import dto.UserLastPdfDTO;
import dto.UserRequestDTO;
import dto.UserResponseDTO;
import model.User;

/**
 *
 * @author Carlo
 */
public interface UserMapper {
    
    UserResponseDTO toDTO(User user);
    User toEntity(UserRequestDTO userRequestDTO);
    UserLastPdfDTO toUserPdfDTO(User user);
    
}
