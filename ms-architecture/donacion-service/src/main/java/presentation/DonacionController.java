/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.ObjectMapperConfig;
import dto.DonacionRequestDTO;
import dto.DonacionResponseDTO;
import exception.DonationFailedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import service.DonacionService;
import service.DonacionServiceImpl;

/**
 *
 * @author Carlo
 */
@WebServlet("/donaciones/*")
public class DonacionController extends HttpServlet {

    private final DonacionService donacionService;
    private final ObjectMapper objectMapper;

    public DonacionController() {
        donacionService = new DonacionServiceImpl();
        objectMapper = ObjectMapperConfig.createObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            BufferedReader br = req.getReader();
            DonacionRequestDTO donacionDTO = objectMapper.readValue(br, DonacionRequestDTO.class);

            DonacionResponseDTO donacionCreada = donacionService.save(donacionDTO);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getWriter(), donacionCreada);

        } catch (DonationFailedException ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error al registrar la donación.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String pathInfo = req.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Debe especificar el id de la campaña en la URL.");
                return;
            }

            String[] parts = pathInfo.split("/");

            if (parts.length != 3 || !parts[1].equals("campaña")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("URL inválida. Debe ser /donaciones/campaña/{id}");
                return;
            }

            int idCampaña;
            try {
                idCampaña = Integer.parseInt(parts[2]);
            } catch (NumberFormatException ex) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("El id de la campaña debe ser numérico.");
                return;
            }

            List<DonacionResponseDTO> donaciones = donacionService.getByCampañaId(idCampaña);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            objectMapper.writeValue(resp.getWriter(), donaciones);

        } catch (Exception ex) {
            ex.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error al obtener las donaciones.");
        }
    }

}
