/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.BeneficiarioStatusResponseDTO;
import dto.PdfDTO;
import dto.PdfEstadoRequestDTO;
import dto.UserImagenDTO;
import dto.UserLastPdfDTO;
import dto.UserRequestDTO;
import dto.UserResponseDTO;
import exception.EmailAlreadyExistsException;
import exception.NotificationServiceException;
import exception.PdfNotFoundException;
import exception.UserNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import service.UserService;
import service.UserServiceImpl;

/**
 *
 * @author Carlo
 */
@WebServlet("/users/*")
@MultipartConfig
public class UserController extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public UserController() {
        userService = new UserServiceImpl();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            String pathInfo = req.getPathInfo();

            if (pathInfo != null && pathInfo.endsWith("/pdf")) {
                String[] parts = pathInfo.split("/");
                int userId = Integer.parseInt(parts[1]);

                Part filePart = req.getPart("file");
                String filename = filePart.getSubmittedFileName();
                InputStream fileContent = filePart.getInputStream();

                userService.saveUserPDF(userId, filename, fileContent);

                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("PDF subido correctamente.");
                return;
            }

        } catch (UserNotFoundException ex) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(ex.getMessage());
        } catch (EmailAlreadyExistsException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(ex.getMessage());
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String pathInfo = req.getPathInfo();

            if (pathInfo != null && pathInfo.equals("/pdf")) {
                String estado = req.getParameter("estado");
                if (estado == null || estado.isEmpty()) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("Debe proporcionar un estado válido.");
                    return;
                }

                List<UserLastPdfDTO> usuarios = userService.findUsersWithLastPdfByState(estado);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(resp.getWriter(), usuarios);
                return;
            }

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

            if (pathInfo != null && pathInfo.matches("/\\d+/pdf")) {
                String[] parts = pathInfo.split("/");
                int userId = Integer.parseInt(parts[1]);

                PdfDTO pdfDto = userService.findLatestPdfByUserId(userId);

                resp.setContentType("application/pdf");
                resp.setHeader("Content-Disposition", "inline; filename=\"" + pdfDto.getFileName() + "\"");

                try (InputStream is = pdfDto.getPdfStream(); ServletOutputStream os = resp.getOutputStream()) {

                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

                }

                return;
            }

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        } catch (NumberFormatException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("El ID debe ser numérico.");
        } catch (PdfNotFoundException ex) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(ex.getMessage());
        } catch (UserNotFoundException ex) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(ex.getMessage());
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
            } else if (pathInfo.endsWith("/pdf/status")) {

                PdfEstadoRequestDTO pdfEstadoRequestDTO = new ObjectMapper()
                        .readValue(req.getReader(), PdfEstadoRequestDTO.class);

                userService.actualizarPdfEstado(id, pdfEstadoRequestDTO);

                resp.setStatus(HttpServletResponse.SC_OK);
                return;
            } else if (pathInfo.endsWith("/imagen")) {
                
                Part filePart = req.getPart("imagen"); 
                if (filePart == null || filePart.getSize() == 0) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("No se recibió ninguna imagen");
                    return;
                }

                InputStream fileContent = filePart.getInputStream();
                UserImagenDTO updatedImage = userService.updateImageUser(id, fileContent);

                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(new ObjectMapper().writeValueAsString(updatedImage));
                return;

            } else if (parts.length == 2) {

                UserRequestDTO userRequestDTO = new ObjectMapper()
                        .readValue(req.getReader(), UserRequestDTO.class);

                UserResponseDTO updatedUser = userService.update(id, userRequestDTO);

                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(new ObjectMapper().writeValueAsString(updatedUser));
                return;

            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

        } catch (NumberFormatException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("El ID debe ser numérico.");
        } catch (UserNotFoundException ex) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(ex.getMessage());
        } catch (PdfNotFoundException ex) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(ex.getMessage());
        } catch (NotificationServiceException ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error al procesar la solicitud.");
        }
    }

}
