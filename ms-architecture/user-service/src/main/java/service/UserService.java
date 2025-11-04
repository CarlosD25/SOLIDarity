/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;


import dto.BeneficiarioStatusResponseDTO;
import dto.UserRequestDTO;
import dto.UserResponseDTO;
import java.io.InputStream;
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
    void deactivate(int id);
    void activate(int id);
    void saveUserPDF(int id,String filename,InputStream inputStream);
    BeneficiarioStatusResponseDTO getBeneficiarioStatus(int id);
}
