/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import client.NotificationClient;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mongodb.client.gridfs.model.GridFSFile;
import config.CloudinaryConfig;
import config.Config;
import dto.BeneficiarioStatusResponseDTO;
import dto.LoginRequestDTO;
import dto.NotificationRequestDTO;
import dto.PdfDTO;
import dto.PdfEstadoRequestDTO;
import dto.UserImagenDTO;
import dto.UserLastPdfDTO;
import dto.UserRequestDTO;
import dto.UserResponseDTO;
import exception.EmailAlreadyExistsException;
import exception.InvalidCredentialsException;
import exception.NotificationServiceException;
import exception.PdfNotFoundException;
import exception.UserNotActiveException;
import exception.UserNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import mapper.UserMapper;
import mapper.UserMapperImpl;
import model.Rol;
import model.Roles;
import model.StatusPDF;
import model.TipoDB;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import persistencia.RolDao;
import persistencia.UserDao;
import persistencia.factory.RolDaoFactory;
import persistencia.factory.UserDaoFactory;

/**
 *
 * @author Carlo
 */
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;
    private final RolDao rolDao;
    private final NotificationClient client;
    private final Cloudinary cloudinary;

    public UserServiceImpl() {
        userDao = UserDaoFactory.getUserDao(TipoDB.POSTGRES);
        userMapper = UserMapperImpl.getInstance();
        rolDao = RolDaoFactory.getRolDao(TipoDB.POSTGRES);
        client = NotificationClient.getInstance();
        cloudinary = CloudinaryConfig.getCloudinary();
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
        user.setImagenUrl(Config.get("USER_IMAGE_DEFAULT"));
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
        if (pdfEstadoRequestDTO.getStatusPDF().equals(StatusPDF.APROBADA)) {
            notificationRequestDTO.setMessage("Felicidades, tu hoja de vida ha sido " + pdfEstadoRequestDTO.getStatusPDF());
        } else {
            notificationRequestDTO.setMessage("Lo sentimos, tu hoja de vida ha sido " + pdfEstadoRequestDTO.getStatusPDF()
                    + ". Te animamos a seguir mejorando tu perfil y postular a futuras oportunidades.");
        }

        try {
            HttpResponse<String> response = client.enviarNotificacion(notificationRequestDTO);

            if (response.statusCode() != HttpURLConnection.HTTP_CREATED) {
                throw new NotificationServiceException(
                        "Hubo un problema al enviar la notificación. Por favor, inténtalo de nuevo más tarde."
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

        if (!user.isActive()) {
            throw new UserNotActiveException("El usuario con ID " + user.getId() + " no está activo.");
        }

        if (!BCrypt.checkpw(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Usuario o contraseña inválidos ");
        }

        return userMapper.toDTO(user);
    }

    @Override
    public UserImagenDTO updateImageUser(int id, InputStream imagen) {

        try {
            User user = userDao.findById(id);
            if (user == null) {
                throw new UserNotFoundException("Usuario con ID " + id + " no encontrado");
            }

            byte[] bytes = imagen.readAllBytes();

            String publicId = "usuario_" + id;

            Map uploadResult = cloudinary.uploader().upload(
                    bytes,
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "folder", "usuarios",
                            "overwrite", true
                    )
            );

            if (!uploadResult.containsKey("secure_url")) {
                throw new RuntimeException("Error al subir la imagen a Cloudinary: no se obtuvo secure_url");
            }

            String url = (String) uploadResult.get("secure_url");

            userDao.actualizarImagenPostgres(id, url);

            userDao.actualizarImagenMongo(id, url);

            return new UserImagenDTO(url);

        } catch (IOException e) {
            throw new RuntimeException("Error de IO al procesar la imagen: " + e.getMessage(), e);
        }

    }

    @Override
    public List<UserResponseDTO> getAll(boolean activo) {
        return userDao.findByActivo(activo).stream().map(user -> userMapper.toDTO(user)).toList();
    }

}
