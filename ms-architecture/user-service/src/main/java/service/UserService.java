/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;


import dto.UserRequestDTO;
import dto.UserResponseDTO;
import java.util.List;
import model.User;

/**
 *
 * @author Carlo
 */
public interface UserService {
    
    UserResponseDTO create(UserRequestDTO user);
    UserResponseDTO getById(int id);
    List<UserResponseDTO> getAll();
    UserResponseDTO update(int id, UserRequestDTO user);
    void delete(int id);
}
