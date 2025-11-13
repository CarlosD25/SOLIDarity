/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import com.milista.datos.MiLista;
import dto.UserLastPdfDTO;
import dto.UserRequestDTO;
import dto.UserResponseDTO;
import java.util.List;
import model.Rol;
import model.User;

/**
 *
 * @author Carlo
 */
public class UserMapperImpl implements UserMapper {

    private static UserMapperImpl userMapperImpl = new UserMapperImpl();

    private UserMapperImpl() {
    }

    public static class Holder{
        private static final UserMapperImpl USER_MAPPER_IMPL = new UserMapperImpl();
    }
    
    public static UserMapperImpl getInstance() {
        return Holder.USER_MAPPER_IMPL;
    }

    @Override
    public UserResponseDTO toDTO(User user) {
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setName(user.getName());
        responseDTO.setTelefono(user.getTelefono());
        responseDTO.setAddress(user.getAddress());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setImagenUrl(user.getImagenUrl());
        return responseDTO;

    }

    @Override
    public User toEntity(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setTelefono(userRequestDTO.getTelefono());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setAddress(userRequestDTO.getAddress());
        return user;
    }

    @Override
    public UserLastPdfDTO toUserPdfDTO(User user) {

        UserLastPdfDTO lastPdfDTO = new UserLastPdfDTO();
        lastPdfDTO.setId(user.getId());
        lastPdfDTO.setName(user.getName());
        lastPdfDTO.setEmail(user.getEmail());
        lastPdfDTO.setImagenUrl(user.getImagenUrl());

        return lastPdfDTO;

    }

}
