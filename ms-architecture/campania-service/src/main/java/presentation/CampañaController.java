/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.ObjectMapperConfig;
import dto.CampañaEstadoRequestDTO;
import dto.CampañaImagenDTO;
import dto.CampañaRequestDTO;
import dto.CampañaResponseDTO;
import dto.CampañaResumenDTO;
import dto.CampañaUpdateDTO;
import dto.DonacionRequestDTO;
import exception.CampañaNotFoundException;
import exception.NotificationServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import service.CampañaService;
import service.CampañaServiceImpl;

/**
 *
 * @author Carlo
 */
@WebServlet("/campañas/*")
@MultipartConfig
public class CampañaController extends HttpServlet {

    private final CampañaService campañaService = new CampañaServiceImpl();
    private ObjectMapper objectMapper = ObjectMapperConfig.createObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            try {
                CampañaRequestDTO dto = objectMapper.readValue(request.getReader(), CampañaRequestDTO.class);
                CampañaResponseDTO created = campañaService.create(dto);

                response.setStatus(HttpServletResponse.SC_CREATED);
                objectMapper.writeValue(response.getWriter(), created);

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Error al crear la campaña: " + e.getMessage());
                objectMapper.writeValue(response.getWriter(), error);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        String pathInfo = request.getPathInfo();

        try {

            if (pathInfo != null && pathInfo.matches("/\\d+/imagen")) {
                int id = Integer.parseInt(pathInfo.split("/")[1]);
                Part filePart = request.getPart("imagen");
                InputStream inputStream = filePart.getInputStream();

                CampañaImagenDTO result = campañaService.actualizarImagenCampaña(id, inputStream);

                response.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(response.getWriter(), result);
                return;
            }

            if (pathInfo != null && pathInfo.matches("/\\d+/donacion")) {
                int id = Integer.parseInt(pathInfo.split("/")[1]);

                DonacionRequestDTO donacionRequestDTO
                        = objectMapper.readValue(request.getReader(), DonacionRequestDTO.class);

                campañaService.actualizarMontoRecaudado(donacionRequestDTO);

                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }

            if (pathInfo != null && pathInfo.matches("/\\d+/estado")) {
                int id = Integer.parseInt(pathInfo.split("/")[1]);

                CampañaEstadoRequestDTO estadoRequestDTO
                        = objectMapper.readValue(request.getReader(), CampañaEstadoRequestDTO.class);

                campañaService.actualizarEstado(id, estadoRequestDTO);

                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }

            if (pathInfo != null && pathInfo.matches("/\\d+")) {
                int id = Integer.parseInt(pathInfo.split("/")[1]);

                CampañaUpdateDTO campañaUpdateDTO = objectMapper.readValue(request.getReader(), CampañaUpdateDTO.class);

                CampañaResponseDTO updatedCampaña = campañaService.update(id, campañaUpdateDTO);

                response.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(response.getWriter(), updatedCampaña);
                return;
            }

        } catch (CampañaNotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(ex.getMessage());
        } catch (NotificationServiceException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al procesar la solicitud: " + e.getMessage());
            objectMapper.writeValue(response.getWriter(), error);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String pathInfo = req.getPathInfo();
            String statusParam = req.getParameter("status"); 

            if (statusParam != null && !statusParam.isEmpty()) {
                List<CampañaResumenDTO> campañasPorStatus = campañaService.getByStatus(statusParam);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(resp.getWriter(), campañasPorStatus);
                return;
            }
            
            if (pathInfo == null || pathInfo.equals("/")) {
                List<CampañaResumenDTO> campañas = campañaService.getAll();

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(resp.getWriter(), campañas);
                return;
            }

            if (pathInfo.matches("/\\d+")) {
                int id = Integer.parseInt(pathInfo.substring(1));
                try {
                    CampañaResponseDTO campaña = campañaService.getById(id);

                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    objectMapper.writeValue(resp.getWriter(), campaña);
                } catch (CampañaNotFoundException ex) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write(ex.getMessage());
                }
                return;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error al procesar la solicitud.");
        }
    }

}
