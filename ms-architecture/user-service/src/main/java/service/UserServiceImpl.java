/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import client.NotificationClient;
import com.mongodb.client.gridfs.model.GridFSFile;
import dto.BeneficiarioStatusResponseDTO;
import dto.LoginRequestDTO;
import dto.NotificationRequestDTO;
import dto.PdfDTO;
import dto.PdfEstadoRequestDTO;
import dto.UserLastPdfDTO;
import dto.UserRequestDTO;
import dto.UserResponseDTO;
import exception.EmailAlreadyExistsException;
import exception.InvalidCredentialsException;
import exception.NotificationServiceException;
import exception.PdfNotFoundException;
import exception.UserNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.http.HttpResponse;
import java.util.List;
import mapper.UserMapper;
import mapper.UserMapperImpl;
import model.Rol;
import model.Roles;
import model.StatusPDF;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import persistencia.RolDao;
import persistencia.UserDao;
import persistencia.impl.RolDaoImpl;
import persistencia.impl.UserDaoImpl;

/**
 *
 * @author Carlo
 */
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;
    private final RolDao rolDao;
    private final NotificationClient client;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
        userMapper = UserMapperImpl.getInstance();
        rolDao = new RolDaoImpl();
        client = NotificationClient.getInstance();
    }

    @Override
    public UserResponseDTO create(UserRequestDTO userRequestDTO) {

        User user = userMapper.toEntity(userRequestDTO);
        User existingUser = userDao.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new EmailAlreadyExistsException("El correo electrónico ya está registrado.");
        }
        Rol rol = rolDao.existsRol(Roles.ROLE_DONANTE.name())
                ? rolDao.findByName(Roles.ROLE_DONANTE.name()) : rolDao.save(new Rol(Roles.ROLE_DONANTE));
        user.setActive(true);
        user = userDao.create(user);
        userDao.assignRoleToUser(user.getId(), rol.getId());
        return userMapper.toDTO(user);
    }

    @Override
    public UserResponseDTO getById(int id) {

        User u = userDao.findById(id);
        if (u == null) {
            throw new UserNotFoundException("Usuario con ID " + id + " no encontrado");
        }

        return userMapper.toDTO(u);

    }

    @Override
    public List<UserResponseDTO> getAll() {

        return userDao.getAll().stream().map(user -> userMapper.toDTO(user)).toList();

    }

    @Override
    public UserResponseDTO update(int id, UserRequestDTO user) {

        User u = userDao.findById(id);
        if (u == null) {
            throw new UserNotFoundException("Usuario con ID " + id + " no encontrado");
        }

        if (user.getName() != null) {
            u.setName(user.getName());
        }

        if (user.getTelefono() != null) {
            u.setTelefono(user.getTelefono());
        }

        if (user.getAddress() != null) {
            u.setAddress(user.getAddress());
        }

        if (user.getPassword() != null) {
            u.setPassword(user.getPassword());
        }

        if (user.getEmail() != null) {
            u.setEmail(user.getEmail());
        }

        userDao.updateMongo(id, u);
        return userMapper.toDTO(userDao.updatePostgres(id, u));

    }

    @Override
    public void deactivate(int id) {
        userDao.deactivate(id);
    }

    @Override
    public void activate(int id) {
        userDao.activate(id);
    }

    @Override
    public void saveUserPDF(int id, String filename, InputStream inputStream) {

        User u = userDao.findById(id);

        if (u == null) {
            throw new UserNotFoundException("Usuario con ID " + id + " no encontrado");
        }

        userDao.saveUserPDF(u, StatusPDF.PENDIENTE, filename, inputStream);

    }

    @Override
    public BeneficiarioStatusResponseDTO getBeneficiarioStatus(int id) {

        String status = userDao.getPdfState(id);
        if (status == null) {
            throw new PdfNotFoundException("El usuario con ID " + id + " aún no tiene ningún PDF.");
        }
        BeneficiarioStatusResponseDTO responseDTO = new BeneficiarioStatusResponseDTO();
        responseDTO.setStatus(status);
        return responseDTO;
    }

    @Override
    public List<UserLastPdfDTO> findUsersWithLastPdfByState(String estado) {

        return userDao.findUsersWithLastPdfByState(estado).stream().map(userMapper::toUserPdfDTO).toList();

    }

    @Override
    public PdfDTO findLatestPdfByUserId(int userId) {
        GridFSFile pdfFile = userDao.findLatestPdfByUserId(userId);

        if (pdfFile == null) {
            throw new PdfNotFoundException("El usuario con ID " + userId + " aún no tiene ningún PDF.");
        }

        InputStream stream = userDao.getPdfStream(pdfFile);
        return new PdfDTO(stream, pdfFile.getFilename());
    }

    @Override
    public void actualizarPdfEstado(int id, PdfEstadoRequestDTO pdfEstadoRequestDTO) {

        if (!userDao.existById(id)) {
            throw new UserNotFoundException("Usuario con ID " + id + " no encontrado");
        }

        userDao.actualizarPdfEstado(id, pdfEstadoRequestDTO.getStatusPDF());

        NotificationRequestDTO notificationRequestDTO = new NotificationRequestDTO();
        notificationRequestDTO.setIdUser(id);
        notificationRequestDTO.setMessage("Felicidades, tu hoja de vida ha sido " + pdfEstadoRequestDTO.getStatusPDF());

        try {
            HttpResponse<String> response = client.enviarNotificacion(notificationRequestDTO);

            if (response.statusCode() != HttpURLConnection.HTTP_CREATED) {
                throw new NotificationServiceException(
                        "Error de comunicación con el servicio de notificaciones"
                );
            }

        } catch (IOException | InterruptedException e) {
            throw new NotificationServiceException("Error de comunicación con el servicio de notificaciones");
        }

    }

    @Override
    public UserResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userDao.findByEmail(loginRequestDTO.getEmail());

        if (user == null) {
            throw new InvalidCredentialsException("Usuario o contraseña inválidos ");
        }

        if (!BCrypt.checkpw(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Usuario o contraseña inválidos ");
        }

        return userMapper.toDTO(user);
    }

}
