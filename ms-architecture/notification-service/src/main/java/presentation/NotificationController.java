/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.ObjectMapperConfig;
import dto.NotificationRequestDTO;
import dto.NotificationResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import service.NotificationService;
import service.NotificationServiceImpl;

/**
 *
 * @author Carlo
 */
@WebServlet("/notifications/*")
public class NotificationController extends HttpServlet{
    
    private final NotificationService notificationService= new NotificationServiceImpl();
    private final ObjectMapper objectMapper= ObjectMapperConfig.createObjectMapper();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        try {
            NotificationRequestDTO requestDTO = objectMapper.readValue(request.getReader(), NotificationRequestDTO.class);

            NotificationResponseDTO createdNotification = notificationService.create(requestDTO);

            response.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(response.getWriter(), createdNotification);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear la notificación: " + e.getMessage());
            objectMapper.writeValue(response.getWriter(), error);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        String pathInfo = request.getPathInfo(); 

        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Debe especificar el ID del usuario en la URL");
            objectMapper.writeValue(response.getWriter(), error);
            return;
        }

        try {
            int userId = Integer.parseInt(pathInfo.substring(1));

            List<NotificationResponseDTO> notifications = notificationService.getByUserId(userId);

            objectMapper.writeValue(response.getWriter(), notifications);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> error = new HashMap<>();
            error.put("error", "ID de usuario inválido");
            objectMapper.writeValue(response.getWriter(), error);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al listar notificaciones: " + e.getMessage());
            objectMapper.writeValue(response.getWriter(), error);
        }
    }
    
}
