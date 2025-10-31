/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dto.UserRequestDTO;
import dto.UserResponseDTO;
import java.util.List;
import mapper.UserMapper;
import mapper.UserMapperImpl;
import model.Rol;
import model.User;
import persistencia.RolDao;
import persistencia.UserDao;
import persistencia.impl.RolDaoImpl;
import persistencia.impl.UserDaoImpl;

/**
 *
 * @author Carlo
 */
public class UserServiceImpl implements UserService {

    private final UserDao userDao = new UserDaoImpl();
    private final UserMapper userMapper = UserMapperImpl.getInstance();
    private final RolDao rolDao = new RolDaoImpl();

    @Override
    public UserResponseDTO create(UserRequestDTO userRequestDTO) {
        User user = userMapper.toEntity(userRequestDTO);
        Rol rol = rolDao.existsRol("ROLE_USER")? 
                rolDao.findByName("ROLE_USER") : rolDao.save(new Rol("ROLE_USER"));
        user.setActive(true);
        user = userDao.create(user);
        userDao.assignRoleToUser(user.getId(), rol.getId());
        return userMapper.toDTO(user);
    }

    @Override
    public UserResponseDTO getById(int id) {
        
        return userMapper.toDTO(userDao.getById(id));
        
    }

    @Override
    public List<UserResponseDTO> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public UserResponseDTO update(int id, UserRequestDTO user) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
