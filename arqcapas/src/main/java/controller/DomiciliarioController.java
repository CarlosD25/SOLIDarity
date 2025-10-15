/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import model.Domiciliario;
import servicio.DomiciliarioService;

/**
 *
 * @author Carlo
 */
@WebServlet("/domiciliarios/*")
public class DomiciliarioController extends HttpServlet {

    private DomiciliarioService ds = new DomiciliarioService();
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String idPath = req.getPathInfo();

        if (idPath == null || idPath.isEmpty() || !idPath.startsWith("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), Map.of("error", "ID inválido"));
            return;
        }

        try {
            int id = Integer.parseInt(idPath.substring(1));
            boolean deleted = ds.delete(id);

            if (deleted) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT); 
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                mapper.writeValue(resp.getWriter(), Map.of("error", "Domiciliario con id " + id + " no encontrado"));
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), Map.of("error", "ID inválido"));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), Map.of("error", "Error interno del servidor"));
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            BufferedReader br = req.getReader();
            Domiciliario domiciliario = mapper.readValue(br, Domiciliario.class);

            Domiciliario domiCreated = ds.create(domiciliario);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            mapper.writeValue(resp.getWriter(), domiCreated);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error al crear el domiciliario\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String idPath = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            if (idPath == null || idPath.isEmpty() || !idPath.startsWith("/")) {

                List<Domiciliario> domiciliarios = ds.getAll();
                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(resp.getWriter(), domiciliarios);

            } else {

                int id = Integer.parseInt(idPath.substring(1));
                Domiciliario d = ds.getById(id);

                if (d != null) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    mapper.writeValue(resp.getWriter(), d);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    mapper.writeValue(resp.getWriter(),
                            Map.of("error", "Domiciliario con id " + id + " no encontrado"));
                }
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), Map.of("error", "ID inválido"));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), Map.of("error", "Error interno del servidor"));
            e.printStackTrace();
        }

    }

}
