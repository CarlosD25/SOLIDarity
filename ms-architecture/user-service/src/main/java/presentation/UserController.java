/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.BeneficiarioStatusResponseDTO;
import dto.UserRequestDTO;
import dto.UserResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.swing.JOptionPane;
import service.UserService;
import service.UserServiceImpl;

/**
 *
 * @author Carlo
 */
@WebServlet("/users/*")
@MultipartConfig
public class UserController extends HttpServlet {

    private final UserService userService = new UserServiceImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.endsWith("/pdfs")) {
            String[] parts = pathInfo.split("/");
            int userId = Integer.parseInt(parts[1]); // extraemos "1"

            Part filePart = req.getPart("file");
            String filename = filePart.getSubmittedFileName();
            InputStream fileContent = filePart.getInputStream();

            userService.saveUserPDF(userId, filename, fileContent);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("PDF subido correctamente.");
            return;
        }

        try {

            BufferedReader br = req.getReader();
            UserRequestDTO userRequestDTO = objectMapper.readValue(br, UserRequestDTO.class);

            UserResponseDTO userResponseDTO = userService.create(userRequestDTO);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getWriter(), userResponseDTO);
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String pathInfo = req.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                List<UserResponseDTO> usuarios = userService.getAll();

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(resp.getWriter(), usuarios);
                return;
            }

            if (pathInfo.matches("/\\d+")) {
                int id = Integer.parseInt(pathInfo.substring(1));

                UserResponseDTO userResponseDTO = userService.getById(id);

                if (userResponseDTO.getId() == 0) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("Usuario no encontrado.");
                    return;
                }

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(resp.getWriter(), userResponseDTO);
                return;
            }

            if (pathInfo.matches("/\\d+/beneficiario")) {
                String[] parts = pathInfo.split("/");
                int userId = Integer.parseInt(parts[1]); 

                BeneficiarioStatusResponseDTO statusResponse = userService.getBeneficiarioStatus(userId);

                if (statusResponse == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("No se encontró el estado del beneficiario.");
                    return;
                }

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                objectMapper.writeValue(resp.getWriter(), statusResponse);
                return;
            }

        } catch (NumberFormatException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("El ID debe ser numérico.");
        } catch (Exception ex) {
            ex.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error al procesar la solicitud.");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String pathInfo = req.getPathInfo();

            if (pathInfo == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Falta el ID del usuario.");
                return;
            }

            String[] parts = pathInfo.split("/");
            if (parts.length < 2) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("URL inválida.");
                return;
            }

            int id = Integer.parseInt(parts[1]);

            if (pathInfo.endsWith("/activate")) {
                userService.activate(id);
            } else if (pathInfo.endsWith("/deactivate")) {
                userService.deactivate(id);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Acción no reconocida. Use /activate o /deactivate");
                return;
            }

            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (NumberFormatException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("El ID debe ser numérico.");
        } catch (Exception ex) {
            ex.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error al procesar la solicitud.");
        }
    }

}
